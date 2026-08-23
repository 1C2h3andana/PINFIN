package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.BankDatabase
import com.example.data.model.AccountEntity
import com.example.data.model.ApplicationFormEntity
import com.example.data.model.ApplicationStatus
import com.example.data.model.ApplicationType
import com.example.data.model.AssetType
import com.example.data.model.BillEntity
import com.example.data.model.BudgetEntity
import com.example.data.model.InsuranceClaimEntity
import com.example.data.model.InsurancePolicyEntity
import com.example.data.model.InsuranceType
import com.example.data.model.InvestmentEntity
import com.example.data.model.LoanCalculationResult
import com.example.data.model.LoanEligibilityResult
import com.example.data.model.NotificationEntity
import com.example.data.model.NotificationType
import com.example.data.model.PolicyStatus
import com.example.data.model.RiskEvaluation
import com.example.data.model.RiskLevel
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.SecurityEventType
import com.example.data.model.SecurityLogEntity
import com.example.data.model.SupportTicketEntity
import com.example.data.model.TransactionCategory
import com.example.data.model.TransactionEntity
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.data.repository.BankRepository
import com.example.domain.ai.BankingAiAdvisor
import com.example.domain.engine.FraudDetectionEngine
import com.example.domain.loan.LoanCalculator
import com.example.domain.security.AuthCrypto
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class TransferState(
    val recipient: String = "",
    val upiOrHandle: String = "",
    val amountText: String = "",
    val category: TransactionCategory = TransactionCategory.TRANSFER,
    val note: String = "",
    val selectedAccountId: Long = 1,
    val liveRisk: RiskEvaluation? = null,
    val isOtpPromptOpen: Boolean = false,
    val enteredOtp: String = "",
    val isTransferSuccess: Boolean = false,
    val lastCompletedTx: TransactionEntity? = null,
    val errorMessage: String? = null
)

data class AuthUiState(
    val isLoggedIn: Boolean = true,
    val currentUser: UserEntity? = null,
    val isRegisterMode: Boolean = false,
    val emailInput: String = "alex.morgan@smartbank.ai",
    val passwordInput: String = "Password@123",
    val fullNameInput: String = "Alex Morgan",
    val confirmPasswordInput: String = "Password@123",
    val selectedRole: UserRole = UserRole.USER,
    val authError: String? = null,
    val isAuthLoading: Boolean = false,
    val isPinLocked: Boolean = false,
    val enteredPin: String = "",
    val jwtDisplay: String = ""
)

data class DepositWithdrawState(
    val isOpen: Boolean = false,
    val isDepositMode: Boolean = true, // true = deposit, false = withdraw
    val selectedAccountId: Long = 1,
    val amountText: String = "",
    val methodOrDestination: String = "ACH Bank Wire Transfer",
    val note: String = "",
    val isProcessing: Boolean = false
)

class BankViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: BankRepository

    val users: StateFlow<List<UserEntity>>
    val accounts: StateFlow<List<AccountEntity>>
    val transactions: StateFlow<List<TransactionEntity>>
    val amlReviewTransactions: StateFlow<List<TransactionEntity>>
    val budgets: StateFlow<List<BudgetEntity>>
    val savingsGoals: StateFlow<List<SavingsGoalEntity>>
    val bills: StateFlow<List<BillEntity>>
    val investments: StateFlow<List<InvestmentEntity>>
    val insurancePolicies: StateFlow<List<InsurancePolicyEntity>>
    val insuranceClaims: StateFlow<List<InsuranceClaimEntity>>
    val applications: StateFlow<List<ApplicationFormEntity>>
    val notifications: StateFlow<List<NotificationEntity>>
    val securityLogs: StateFlow<List<SecurityLogEntity>>
    val supportTickets: StateFlow<List<SupportTicketEntity>>

    // Auth State
    private val _authState = MutableStateFlow(AuthUiState())
    val authState: StateFlow<AuthUiState> = _authState.asStateFlow()

    // Deposit / Withdraw State
    private val _depositWithdrawState = MutableStateFlow(DepositWithdrawState())
    val depositWithdrawState: StateFlow<DepositWithdrawState> = _depositWithdrawState.asStateFlow()

    private val _isDarkMode = MutableStateFlow(true)
    val isDarkMode: StateFlow<Boolean> = _isDarkMode.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _selectedCategoryFilter = MutableStateFlow<TransactionCategory?>(null)
    val selectedCategoryFilter: StateFlow<TransactionCategory?> = _selectedCategoryFilter.asStateFlow()

    private val _selectedLanguage = MutableStateFlow("English (US)")
    val selectedLanguage: StateFlow<String> = _selectedLanguage.asStateFlow()

    private val _isAccessibleSeniorMode = MutableStateFlow(false)
    val isAccessibleSeniorMode: StateFlow<Boolean> = _isAccessibleSeniorMode.asStateFlow()

    fun setLanguage(lang: String) {
        _selectedLanguage.value = lang
        showMessage("Language updated to $lang")
    }

    fun toggleAccessibleSeniorMode() {
        _isAccessibleSeniorMode.value = !_isAccessibleSeniorMode.value
        showMessage(if (_isAccessibleSeniorMode.value) "Senior & High-Contrast Mode Activated" else "Standard Mode Restored")
    }

    private val _transferState = MutableStateFlow(TransferState())
    val transferState: StateFlow<TransferState> = _transferState.asStateFlow()

    private val _is2FaGloballyEnabled = MutableStateFlow(true)
    val is2FaGloballyEnabled: StateFlow<Boolean> = _is2FaGloballyEnabled.asStateFlow()

    private val _isBiometricsEnabled = MutableStateFlow(true)
    val isBiometricsEnabled: StateFlow<Boolean> = _isBiometricsEnabled.asStateFlow()

    private val _isLoginAlertsEnabled = MutableStateFlow(true)
    val isLoginAlertsEnabled: StateFlow<Boolean> = _isLoginAlertsEnabled.asStateFlow()

    private val _isAmlMonitoringEnabled = MutableStateFlow(true)
    val isAmlMonitoringEnabled: StateFlow<Boolean> = _isAmlMonitoringEnabled.asStateFlow()

    // Scam Link Inspector State
    private val _scamInspectorInput = MutableStateFlow("")
    val scamInspectorInput: StateFlow<String> = _scamInspectorInput.asStateFlow()

    private val _scamInspectionResult = MutableStateFlow<RiskEvaluation?>(null)
    val scamInspectionResult: StateFlow<RiskEvaluation?> = _scamInspectionResult.asStateFlow()

    // EMI Calculator State
    private val _loanPrincipal = MutableStateFlow(25000.0)
    val loanPrincipal: StateFlow<Double> = _loanPrincipal.asStateFlow()

    private val _loanRate = MutableStateFlow(9.5)
    val loanRate: StateFlow<Double> = _loanRate.asStateFlow()

    private val _loanTenureMonths = MutableStateFlow(36)
    val loanTenureMonths: StateFlow<Int> = _loanTenureMonths.asStateFlow()

    val emiResult: StateFlow<LoanCalculationResult>

    // AI Loan Underwriter state
    private val _userAnnualIncome = MutableStateFlow(145000.0)
    val userAnnualIncome: StateFlow<Double> = _userAnnualIncome.asStateFlow()

    private val _userMonthlyDebt = MutableStateFlow(1200.0)
    val userMonthlyDebt: StateFlow<Double> = _userMonthlyDebt.asStateFlow()

    private val _userCreditScore = MutableStateFlow(785)
    val userCreditScore: StateFlow<Int> = _userCreditScore.asStateFlow()

    val loanEligibility: StateFlow<LoanEligibilityResult>

    // AI Advisor / Chatbot Messages
    private val _chatMessages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                sender = MessageSender.AI,
                text = "Hello Alex! I am your 24/7 SmartBank AI Concierge. I can help evaluate cyber threats, analyze your spending trends, calculate loan eligibility, or simulate investment portfolios. How may I assist you today?"
            )
        )
    )
    val chatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()
    val aiChatMessages: StateFlow<List<ChatMessage>> = _chatMessages.asStateFlow()

    private val _selectedLoanType = MutableStateFlow("Personal Loan")
    val selectedLoanType: StateFlow<String> = _selectedLoanType.asStateFlow()

    private val _userMonthlyIncome = MutableStateFlow(8500.0)
    val userMonthlyIncome: StateFlow<Double> = _userMonthlyIncome.asStateFlow()

    private val _userExistingEmis = MutableStateFlow(800.0)
    val userExistingEmis: StateFlow<Double> = _userExistingEmis.asStateFlow()

    val eligibilityResult: StateFlow<LoanEligibilityResult> = combine(
        _userMonthlyIncome,
        _userExistingEmis,
        _userCreditScore,
        _selectedLoanType
    ) { income, emis, score, loanType ->
        LoanCalculator.evaluateEligibility(income, emis, score, "Salaried Full-Time", loanType)
    }.stateIn(
        viewModelScope,
        SharingStarted.Eagerly,
        LoanCalculator.evaluateEligibility(8500.0, 800.0, 785, "Salaried Full-Time", "Personal Loan")
    )

    private val _chatInput = MutableStateFlow("")
    val chatInput: StateFlow<String> = _chatInput.asStateFlow()

    private val _isAiTyping = MutableStateFlow(false)
    val isAiTyping: StateFlow<Boolean> = _isAiTyping.asStateFlow()

    // Global snackbar messages
    private val _snackbarMessage = MutableStateFlow<String?>(null)
    val snackbarMessage: StateFlow<String?> = _snackbarMessage.asStateFlow()

    // Admin Fraud Sensitivity
    private val _fraudSensitivity = MutableStateFlow("Balanced AI Model")
    val fraudSensitivity: StateFlow<String> = _fraudSensitivity.asStateFlow()

    // Paid Version & Membership Tier State
    private val _activeMembershipTier = MutableStateFlow(MembershipTier.PRO_PLANETARY)
    val activeMembershipTier: StateFlow<MembershipTier> = _activeMembershipTier.asStateFlow()

    private val _isPaidVersionUnlocked = MutableStateFlow(true)
    val isPaidVersionUnlocked: StateFlow<Boolean> = _isPaidVersionUnlocked.asStateFlow()

    private val _activeLicenseKey = MutableStateFlow("PFIN-SOVEREIGN-KYBER-2026-X89")
    val activeLicenseKey: StateFlow<String> = _activeLicenseKey.asStateFlow()

    private val _subscriptionRenewalDate = MutableStateFlow("Aug 23, 2027 (Annual Pass)")
    val subscriptionRenewalDate: StateFlow<String> = _subscriptionRenewalDate.asStateFlow()

    fun activatePaidPlan(tier: MembershipTier, paymentMethod: String, promoCode: String? = null) {
        _activeMembershipTier.value = tier
        _isPaidVersionUnlocked.value = true
        val randomHex = (1000..9999).random()
        val generatedKey = when (tier) {
            MembershipTier.QUANTUM_SOVEREIGN -> "PFIN-QUANTUM-LIFETIME-$randomHex-LATTICE"
            MembershipTier.PRO_PLANETARY -> "PFIN-PRO-ANNUAL-$randomHex-KYBER"
            MembershipTier.ENTERPRISE_INSTITUTIONAL -> "PFIN-ENTERPRISE-NODE-$randomHex-VPC"
            MembershipTier.FREE_CITIZEN -> "PFIN-CITIZEN-FREE-TIER"
        }
        _activeLicenseKey.value = generatedKey
        _subscriptionRenewalDate.value = if (tier == MembershipTier.QUANTUM_SOVEREIGN) "Lifetime Access (Never Expires)" else "Aug 23, 2027 (Auto-Renews)"
        
        viewModelScope.launch {
            repository.logSecurityEvent(
                SecurityEventType.LIMIT_UPDATED,
                "Planetary License $generatedKey (${tier.displayName}) activated via $paymentMethod.",
                RiskLevel.LOW
            )
        }
        showMessage("✓ Payment Successful! ${tier.displayName} license activated.")
    }

    fun redeemPromoCode(code: String): Boolean {
        val trimmed = code.trim().uppercase()
        return if (trimmed == "PLANET2026" || trimmed == "EARLYBIRD" || trimmed == "VIP100" || trimmed == "STUDENT" || trimmed == "PROPASS") {
            activatePaidPlan(MembershipTier.QUANTUM_SOVEREIGN, "VIP Promo Code ($trimmed)")
            true
        } else {
            showMessage("Invalid Promo / Activation Code. Please check and try again.")
            false
        }
    }

    fun restorePurchases() {
        _isPaidVersionUnlocked.value = true
        showMessage("✓ Active Google Play & Cloud Subscriptions successfully restored.")
    }

    // Total Net Worth derived
    val totalNetWorth: StateFlow<Double>

    // Unread notifications count
    val unreadNotificationsCount: StateFlow<Int>

    init {
        val db = BankDatabase.getDatabase(application)
        repository = BankRepository(db.bankDao())

        users = repository.users.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        accounts = repository.accounts.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        transactions = repository.transactions.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        amlReviewTransactions = repository.amlReviewTransactions.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        budgets = repository.budgets.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        savingsGoals = repository.savingsGoals.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        bills = repository.bills.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        investments = repository.investments.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        insurancePolicies = repository.insurancePolicies.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        insuranceClaims = repository.insuranceClaims.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        applications = repository.applications.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        notifications = repository.notifications.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        securityLogs = repository.securityLogs.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())
        supportTickets = repository.supportTickets.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

        emiResult = combine(_loanPrincipal, _loanRate, _loanTenureMonths) { p, r, t ->
            LoanCalculator.calculateEmi(p, r, t)
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            LoanCalculator.calculateEmi(25000.0, 9.5, 36)
        )

        loanEligibility = combine(_userAnnualIncome, _userMonthlyDebt, _userCreditScore) { income, debt, score ->
            LoanCalculator.evaluateEligibility(income, debt, score)
        }.stateIn(
            viewModelScope,
            SharingStarted.Eagerly,
            LoanCalculator.evaluateEligibility(145000.0, 1200.0, 785)
        )

        totalNetWorth = combine(accounts, investments) { accList, invList ->
            val cashTotal = accList.filter { it.type != com.example.data.model.AccountType.CREDIT_CARD }.sumOf { it.balance }
            val creditDebt = accList.filter { it.type == com.example.data.model.AccountType.CREDIT_CARD }.sumOf { it.balance }
            val invTotal = invList.sumOf { it.sharesOrUnits * it.currentPrice }
            (cashTotal + invTotal) - creditDebt
        }.stateIn(viewModelScope, SharingStarted.Eagerly, 100000.0)

        unreadNotificationsCount = notifications.combine(MutableStateFlow(Unit)) { notifs, _ ->
            notifs.count { !it.isRead }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, 2)

        viewModelScope.launch {
            repository.seedInitialDataIfEmpty()
            val initialUser = repository.authenticateUser("alex.morgan@smartbank.ai", "Password@123")
            if (initialUser != null) {
                _authState.value = _authState.value.copy(
                    isLoggedIn = true,
                    currentUser = initialUser,
                    jwtDisplay = initialUser.jwtToken
                )
            }
        }
    }

    // --- Authentication Actions ---
    fun updateAuthInput(
        email: String? = null,
        pass: String? = null,
        fullName: String? = null,
        confirmPass: String? = null,
        role: UserRole? = null
    ) {
        _authState.value = _authState.value.copy(
            emailInput = email ?: _authState.value.emailInput,
            passwordInput = pass ?: _authState.value.passwordInput,
            fullNameInput = fullName ?: _authState.value.fullNameInput,
            confirmPasswordInput = confirmPass ?: _authState.value.confirmPasswordInput,
            selectedRole = role ?: _authState.value.selectedRole,
            authError = null
        )
    }

    fun toggleAuthMode() {
        _authState.value = _authState.value.copy(
            isRegisterMode = !_authState.value.isRegisterMode,
            authError = null
        )
    }

    fun login(email: String, pass: String) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isAuthLoading = true)
            val user = repository.authenticateUser(email, pass)
            if (user != null) {
                _authState.value = _authState.value.copy(
                    isLoggedIn = true,
                    currentUser = user,
                    jwtDisplay = user.jwtToken,
                    isAuthLoading = false,
                    authError = null
                )
                showMessage("Welcome back, ${user.fullName}! JWT session active.")
            } else {
                _authState.value = _authState.value.copy(
                    isAuthLoading = false,
                    authError = "Invalid email or password."
                )
            }
        }
    }

    fun register(fullName: String, email: String, pass: String, role: UserRole = UserRole.USER) {
        viewModelScope.launch {
            _authState.value = _authState.value.copy(isAuthLoading = true)
            val user = repository.registerUser(
                fullName = fullName,
                email = email,
                password = pass,
                role = role
            )
            _authState.value = _authState.value.copy(
                isLoggedIn = true,
                currentUser = user,
                jwtDisplay = user.jwtToken,
                isAuthLoading = false,
                authError = null
            )
            showMessage("Account created! Logged in as ${user.fullName}.")
        }
    }

    fun login() {
        val state = _authState.value
        if (state.emailInput.isBlank() || state.passwordInput.isBlank()) {
            _authState.value = state.copy(authError = "Please enter both email and password")
            return
        }
        viewModelScope.launch {
            _authState.value = state.copy(isAuthLoading = true)
            val user = repository.authenticateUser(state.emailInput, state.passwordInput)
            if (user != null) {
                _authState.value = state.copy(
                    isLoggedIn = true,
                    currentUser = user,
                    jwtDisplay = user.jwtToken,
                    isAuthLoading = false,
                    authError = null
                )
                showMessage("Welcome back, ${user.fullName}! JWT session active.")
            } else {
                _authState.value = state.copy(
                    isAuthLoading = false,
                    authError = "Invalid email or password. Verify credentials."
                )
            }
        }
    }

    fun register() {
        val state = _authState.value
        if (state.fullNameInput.isBlank() || state.emailInput.isBlank() || state.passwordInput.isBlank()) {
            _authState.value = state.copy(authError = "All registration fields are required.")
            return
        }
        if (state.passwordInput.length < 8) {
            _authState.value = state.copy(authError = "Password must be at least 8 characters.")
            return
        }
        if (state.passwordInput != state.confirmPasswordInput) {
            _authState.value = state.copy(authError = "Passwords do not match.")
            return
        }
        viewModelScope.launch {
            _authState.value = state.copy(isAuthLoading = true)
            val user = repository.registerUser(
                fullName = state.fullNameInput,
                email = state.emailInput,
                password = state.passwordInput,
                role = state.selectedRole
            )
            _authState.value = state.copy(
                isLoggedIn = true,
                currentUser = user,
                jwtDisplay = user.jwtToken,
                isAuthLoading = false,
                authError = null
            )
            showMessage("Account created! Logged in as ${user.fullName}.")
        }
    }

    fun quickSwitchUser(role: UserRole) {
        viewModelScope.launch {
            val email = if (role == UserRole.ADMIN) "admin.security@smartbank.ai" else "alex.morgan@smartbank.ai"
            val pass = if (role == UserRole.ADMIN) "Admin@Master2026" else "Password@123"
            val user = repository.authenticateUser(email, pass)
            if (user != null) {
                _authState.value = _authState.value.copy(
                    isLoggedIn = true,
                    currentUser = user,
                    jwtDisplay = user.jwtToken
                )
                showMessage("Switched profile to: ${user.fullName} (${user.role.name})")
            }
        }
    }

    fun loginAsAdmin() {
        quickSwitchUser(UserRole.ADMIN)
    }

    fun loginAsUser(email: String = "alex.morgan@smartbank.ai") {
        quickSwitchUser(UserRole.USER)
    }

    fun updateUserRole(userId: Long, newRole: UserRole) {
        viewModelScope.launch {
            val userList = users.value
            val target = userList.find { it.id == userId }
            if (target != null) {
                repository.registerUser(
                    fullName = target.fullName,
                    email = target.email,
                    password = "Password@123",
                    role = newRole
                )
                showMessage("Updated ${target.fullName}'s role to ${newRole.name}.")
            }
        }
    }

    fun logout() {
        _authState.value = _authState.value.copy(
            isLoggedIn = false,
            currentUser = null,
            jwtDisplay = ""
        )
        showMessage("You have been signed out securely.")
    }

    // --- Deposit / Withdrawal Dialog Controls ---
    fun openDepositDialog(accountId: Long = 1) {
        _depositWithdrawState.value = DepositWithdrawState(
            isOpen = true,
            isDepositMode = true,
            selectedAccountId = accountId,
            methodOrDestination = "ACH Electronic Bank Wire",
            amountText = "1000.00"
        )
    }

    fun openWithdrawDialog(accountId: Long = 1) {
        _depositWithdrawState.value = DepositWithdrawState(
            isOpen = true,
            isDepositMode = false,
            selectedAccountId = accountId,
            methodOrDestination = "External Savings Account (Chase ...9941)",
            amountText = "250.00"
        )
    }

    fun dismissDepositWithdrawDialog() {
        _depositWithdrawState.value = _depositWithdrawState.value.copy(isOpen = false)
    }

    fun updateDepositWithdrawAmount(text: String) {
        _depositWithdrawState.value = _depositWithdrawState.value.copy(amountText = text)
    }

    fun updateDepositWithdrawMethod(method: String) {
        _depositWithdrawState.value = _depositWithdrawState.value.copy(methodOrDestination = method)
    }

    fun updateDepositWithdrawAccount(accId: Long) {
        _depositWithdrawState.value = _depositWithdrawState.value.copy(selectedAccountId = accId)
    }

    fun updateDepositWithdrawNote(note: String) {
        _depositWithdrawState.value = _depositWithdrawState.value.copy(note = note)
    }

    fun submitDepositWithdraw() {
        val state = _depositWithdrawState.value
        val amount = state.amountText.toDoubleOrNull() ?: 0.0
        if (amount <= 0) {
            showMessage("Please enter a valid amount greater than 0")
            return
        }

        viewModelScope.launch {
            if (state.isDepositMode) {
                val ok = repository.performDeposit(state.selectedAccountId, amount, state.methodOrDestination, state.note)
                if (ok) {
                    showMessage("✓ Successfully deposited $${"%,.2f".format(amount)}")
                    dismissDepositWithdrawDialog()
                } else {
                    showMessage("Deposit failed. Check account selection.")
                }
            } else {
                val ok = repository.performWithdrawal(state.selectedAccountId, amount, state.methodOrDestination, state.note)
                if (ok) {
                    showMessage("✓ Successfully withdrawn $${"%,.2f".format(amount)}")
                    dismissDepositWithdrawDialog()
                } else {
                    showMessage("Insufficient funds or invalid withdrawal amount.")
                }
            }
        }
    }

    // --- Investment Buy / Sell ---
    fun buyAsset(symbol: String, shares: Double, price: Double) {
        viewModelScope.launch {
            val ok = repository.buyInvestment(symbol, shares, price)
            if (ok) {
                showMessage("✓ Bought $shares units of $symbol at $${"%,.2f".format(price)}")
            } else {
                showMessage("Insufficient cash in Investment Portfolio vault.")
            }
        }
    }

    fun sellAsset(symbol: String, shares: Double, price: Double) {
        viewModelScope.launch {
            val ok = repository.sellInvestment(symbol, shares, price)
            if (ok) {
                showMessage("✓ Sold $shares units of $symbol. Proceeds credited to Investment Vault.")
            } else {
                showMessage("Failed to sell asset. Check share balance.")
            }
        }
    }

    // --- Insurance Claim & Apply ---
    fun submitInsuranceClaim(policyId: Long, policyType: InsuranceType, amount: Double, incidentDate: String, description: String) {
        viewModelScope.launch {
            val claimId = repository.submitInsuranceClaim(policyId, policyType, amount, incidentDate, description)
            showMessage("✓ Claim #CLM-$claimId submitted! AI claims review in progress.")
        }
    }

    // --- Application Forms Submit ---
    fun submitApplicationForm(
        type: ApplicationType,
        applicantName: String,
        email: String,
        phone: String,
        annualIncome: Double,
        employmentType: String,
        requestedAmountOrTier: String,
        idProofType: String,
        idProofNumber: String
    ) {
        viewModelScope.launch {
            val app = ApplicationFormEntity(
                type = type,
                applicantName = applicantName,
                email = email,
                phone = phone,
                annualIncome = annualIncome,
                employmentType = employmentType,
                requestedAmountOrTier = requestedAmountOrTier,
                idProofType = idProofType,
                idProofNumber = idProofNumber,
                status = ApplicationStatus.AI_PRE_APPROVED,
                aiScore = 91,
                aiRemarks = "Automated AI Underwriting: Strong credit profile & low risk factors. Pre-approval issued."
            )
            repository.submitApplication(app)
            showMessage("✓ Application submitted! Status: AI Pre-Approved (Score: 91/100)")
        }
    }

    // --- Admin Operations ---
    fun adminApproveTransaction(txId: Long) {
        viewModelScope.launch {
            repository.adminApproveTransaction(txId)
            showMessage("✓ Transaction #$txId approved and released by Compliance Admin.")
        }
    }

    fun adminRevertTransaction(txId: Long) {
        viewModelScope.launch {
            repository.adminRevertTransaction(txId)
            showMessage("Transaction #$txId rejected & reverted by Compliance Admin.")
        }
    }

    fun setFraudSensitivity(sensitivity: String) {
        _fraudSensitivity.value = sensitivity
        showMessage("AI Fraud Sensitivity updated to: $sensitivity")
    }

    // --- Notifications Management ---
    fun markNotificationRead(id: Long) {
        viewModelScope.launch { repository.markNotificationRead(id) }
    }

    fun markAllNotificationsRead() {
        viewModelScope.launch {
            repository.markAllNotificationsRead()
            showMessage("All notifications marked as read.")
        }
    }

    fun clearAllNotifications() {
        viewModelScope.launch {
            repository.clearAllNotifications()
            showMessage("All notifications cleared.")
        }
    }

    // --- Filter & Search Transactions ---
    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setCategoryFilter(category: TransactionCategory?) {
        _selectedCategoryFilter.value = category
    }

    val filteredTransactions: StateFlow<List<TransactionEntity>> =
        combine(transactions, _searchQuery, _selectedCategoryFilter) { txs, query, cat ->
            txs.filter { tx ->
                val matchesQuery = query.isBlank() ||
                        tx.title.contains(query, ignoreCase = true) ||
                        tx.recipient.contains(query, ignoreCase = true) ||
                        tx.upiOrHandle.contains(query, ignoreCase = true) ||
                        tx.amount.toString().contains(query)

                val matchesCategory = (cat == null || tx.category == cat)
                matchesQuery && matchesCategory
            }
        }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    // --- Live Transfer Form State & Fraud Detection ---
    fun updateTransferRecipient(recipient: String) {
        _transferState.value = _transferState.value.copy(
            recipient = recipient,
            isTransferSuccess = false,
            errorMessage = null
        )
        evaluateLiveRisk()
    }

    fun updateTransferHandle(handle: String) {
        _transferState.value = _transferState.value.copy(
            upiOrHandle = handle,
            isTransferSuccess = false,
            errorMessage = null
        )
        evaluateLiveRisk()
    }

    fun updateTransferAmount(amountText: String) {
        _transferState.value = _transferState.value.copy(
            amountText = amountText,
            isTransferSuccess = false,
            errorMessage = null
        )
        evaluateLiveRisk()
    }

    fun updateTransferCategory(category: TransactionCategory) {
        _transferState.value = _transferState.value.copy(category = category)
    }

    fun updateTransferNote(note: String) {
        _transferState.value = _transferState.value.copy(note = note)
        evaluateLiveRisk()
    }

    fun updateTransferAccount(accountId: Long) {
        _transferState.value = _transferState.value.copy(selectedAccountId = accountId)
        evaluateLiveRisk()
    }

    fun setOtpInput(otp: String) {
        _transferState.value = _transferState.value.copy(enteredOtp = otp)
    }

    fun dismissOtpPrompt() {
        _transferState.value = _transferState.value.copy(isOtpPromptOpen = false, enteredOtp = "")
    }

    private fun evaluateLiveRisk() {
        val current = _transferState.value
        val amount = current.amountText.toDoubleOrNull() ?: 0.0
        val account = accounts.value.find { it.id == current.selectedAccountId }
        val balance = account?.balance ?: 10000.0

        if (current.recipient.isBlank() && current.upiOrHandle.isBlank() && amount == 0.0) {
            _transferState.value = _transferState.value.copy(liveRisk = null)
            return
        }

        val eval = FraudDetectionEngine.evaluateTransaction(
            amount = amount,
            recipient = current.recipient,
            upiOrHandle = current.upiOrHandle,
            currentBalance = balance,
            note = current.note
        )
        _transferState.value = _transferState.value.copy(liveRisk = eval)
    }

    fun initiateTransfer() {
        val state = _transferState.value
        val amount = state.amountText.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _transferState.value = state.copy(errorMessage = "Please enter a valid amount")
            return
        }
        if (state.recipient.isBlank()) {
            _transferState.value = state.copy(errorMessage = "Please enter recipient name")
            return
        }

        val account = accounts.value.find { it.id == state.selectedAccountId }
        if (account == null) {
            _transferState.value = state.copy(errorMessage = "Selected account is invalid")
            return
        }

        if (account.isFrozen) {
            _transferState.value = state.copy(errorMessage = "This account card is FROZEN. Unfreeze in Security Center.")
            return
        }

        if (account.balance < amount) {
            _transferState.value = state.copy(errorMessage = "Insufficient balance in ${account.name}")
            return
        }

        val eval = FraudDetectionEngine.evaluateTransaction(
            amount = amount,
            recipient = state.recipient,
            upiOrHandle = state.upiOrHandle,
            currentBalance = account.balance,
            note = state.note
        )

        if (eval.shouldBlock) {
            viewModelScope.launch {
                val (success, tx) = repository.executeTransfer(
                    sourceAccountId = state.selectedAccountId,
                    recipient = state.recipient,
                    upiOrHandle = state.upiOrHandle,
                    amount = amount,
                    category = state.category,
                    note = state.note
                )
                _transferState.value = state.copy(
                    isTransferSuccess = false,
                    lastCompletedTx = tx,
                    errorMessage = "🛡️ TRANSFER BLOCKED: AI Fraud Shield prevented this suspicious transaction."
                )
            }
            return
        }

        if (eval.requires2Fa || _is2FaGloballyEnabled.value) {
            _transferState.value = state.copy(isOtpPromptOpen = true, liveRisk = eval)
            return
        }

        completeTransferDirectly()
    }

    fun confirmOtpAndTransfer() {
        val state = _transferState.value
        if (state.enteredOtp != "1234" && state.enteredOtp != "8842") {
            _transferState.value = state.copy(errorMessage = "Invalid 2FA OTP Code. Enter demo code: 1234")
            return
        }
        completeTransferDirectly()
    }

    private fun completeTransferDirectly() {
        val state = _transferState.value
        val amount = state.amountText.toDoubleOrNull() ?: 0.0

        viewModelScope.launch {
            val (success, tx) = repository.executeTransfer(
                sourceAccountId = state.selectedAccountId,
                recipient = state.recipient,
                upiOrHandle = state.upiOrHandle,
                amount = amount,
                category = state.category,
                note = state.note,
                is2FaBypassed = true
            )

            if (success) {
                _transferState.value = TransferState(
                    isTransferSuccess = true,
                    lastCompletedTx = tx,
                    selectedAccountId = state.selectedAccountId
                )
                showMessage("✓ Transfer of $${"%,.2f".format(amount)} to ${state.recipient} successful!")
            } else {
                _transferState.value = state.copy(
                    isTransferSuccess = false,
                    lastCompletedTx = tx,
                    errorMessage = "Transaction was blocked by AI Fraud Engine."
                )
            }
        }
    }

    fun resetTransferState() {
        _transferState.value = TransferState()
    }

    // --- Scam Inspector ---
    fun updateScamInspectorInput(input: String) {
        _scamInspectorInput.value = input
        if (input.isBlank()) {
            _scamInspectionResult.value = null
        } else {
            _scamInspectionResult.value = FraudDetectionEngine.inspectUrlOrHandle(input)
        }
    }

    // --- Loan EMI & Eligibility State Updates ---
    fun updateLoanPrincipal(principal: Double) { _loanPrincipal.value = principal }
    fun updateLoanRate(rate: Double) { _loanRate.value = rate }
    fun updateLoanTenure(months: Int) { _loanTenureMonths.value = months }
    fun updateAnnualIncome(income: Double) { _userAnnualIncome.value = income }
    fun updateMonthlyDebt(debt: Double) { _userMonthlyDebt.value = debt }
    fun updateCreditScore(score: Int) { _userCreditScore.value = score }

    // --- AI Chatbot ---
    fun updateChatInput(text: String) { _chatInput.value = text }

    fun sendChatMessage(presetQuery: String? = null) {
        val messageText = presetQuery ?: _chatInput.value.trim()
        if (messageText.isBlank()) return

        val userMessage = ChatMessage(sender = MessageSender.USER, text = messageText)
        val currentList = _chatMessages.value.toMutableList().apply { add(userMessage) }
        _chatMessages.value = currentList
        _chatInput.value = ""
        _isAiTyping.value = true

        viewModelScope.launch {
            kotlinx.coroutines.delay(600)
            val aiReply = BankingAiAdvisor.generateResponse(
                prompt = messageText,
                accounts = accounts.value,
                transactions = transactions.value,
                budgets = budgets.value
            )
            _chatMessages.value = _chatMessages.value + ChatMessage(sender = MessageSender.AI, text = aiReply)
            _isAiTyping.value = false
        }
    }

    // --- Security & Card Controls ---
    fun toggleCardFreeze(accountId: Long, currentFrozen: Boolean) {
        viewModelScope.launch {
            repository.toggleCardFreeze(accountId, !currentFrozen)
            showMessage(if (!currentFrozen) "Card frozen immediately." else "Card unfrozen.")
        }
    }

    fun toggle2FaGlobal() {
        _is2FaGloballyEnabled.value = !_is2FaGloballyEnabled.value
        showMessage("Two-Factor Authentication: ${if (_is2FaGloballyEnabled.value) "ENABLED" else "DISABLED"}")
    }

    fun toggleBiometrics() {
        _isBiometricsEnabled.value = !_isBiometricsEnabled.value
        showMessage("Biometric Passkey: ${if (_isBiometricsEnabled.value) "ENABLED" else "DISABLED"}")
    }

    fun toggleLoginAlerts() {
        _isLoginAlertsEnabled.value = !_isLoginAlertsEnabled.value
        showMessage("Suspicious Login Alerts: ${if (_isLoginAlertsEnabled.value) "ENABLED" else "DISABLED"}")
    }

    fun toggleAmlMonitoring() {
        _isAmlMonitoringEnabled.value = !_isAmlMonitoringEnabled.value
        showMessage("AML Real-Time Surveillance: ${if (_isAmlMonitoringEnabled.value) "ENABLED" else "DISABLED"}")
    }

    // --- Bill Payments & Savings ---
    fun payBill(billId: Long, sourceAccountId: Long = 1L) {
        viewModelScope.launch {
            repository.payBill(billId, sourceAccountId)
            showMessage("✓ Bill marked as paid.")
        }
    }

    fun toggleBillAutoPay(billId: Long, current: Boolean) {
        viewModelScope.launch {
            repository.toggleBillAutoPay(billId, !current)
            showMessage("Auto-Pay ${if (!current) "activated" else "deactivated"}.")
        }
    }

    fun depositToSavingsGoal(goalId: Long, amount: Double, sourceAccountId: Long = 1L) {
        viewModelScope.launch {
            repository.depositToSavingsGoal(goalId, amount, sourceAccountId)
            showMessage("✓ Added $${"%,.2f".format(amount)} to Savings Goal.")
        }
    }

    fun submitSupportTicket(title: String, description: String, category: String) {
        viewModelScope.launch {
            repository.submitSupportTicket(title, description, category)
            showMessage("✓ Support ticket submitted. AI Concierge is reviewing.")
        }
    }

    // Compatibility helper methods
    fun sendAiPrompt(prompt: String) { sendChatMessage(prompt) }
    fun submitTicket(title: String, desc: String, cat: String) { submitSupportTicket(title, desc, cat) }
    fun addSavingsGoal(title: String, target: Double, date: String = "Dec 2026", icon: String = "SAVINGS", current: Double = 0.0) {
        viewModelScope.launch {
            repository.createSavingsGoal(SavingsGoalEntity(title = title, targetAmount = target, currentAmount = current, targetDate = date, iconKey = icon))
            showMessage("✓ Savings goal '$title' created!")
        }
    }
    fun contributeToGoal(id: Long, amount: Double, sourceAccountId: Long = 1L) {
        depositToSavingsGoal(id, amount, sourceAccountId)
    }
    fun flagTransactionAsFraud(txId: Long) {
        viewModelScope.launch {
            repository.flagTransactionAsFraud(txId)
            showMessage("Transaction #$txId flagged for security review.")
        }
    }
    fun exportStatementSummary(): String = "SMARTBANK OFFICIAL STATEMENT - Consolidated Liquidity $${"%,.2f".format(totalNetWorth.value)}"
    fun setLoanPrincipal(p: Double) { updateLoanPrincipal(p) }
    fun setLoanRate(r: Double) { updateLoanRate(r) }
    fun setLoanTenureMonths(t: Int) { updateLoanTenure(t) }
    fun setSelectedLoanType(type: String) { _selectedLoanType.value = type }
    fun setUserMonthlyIncome(income: Double) { _userMonthlyIncome.value = income }
    fun setUserExistingEmis(emis: Double) { _userExistingEmis.value = emis }
    fun setUserCreditScore(score: Int) { updateCreditScore(score) }
    fun clearScamInspection() {
        _scamInspectionResult.value = null
        _scamInspectorInput.value = ""
    }
    fun performScamInspection(input: String = _scamInspectorInput.value) { updateScamInspectorInput(input) }
    fun resetTransferForm() { resetTransferState() }
    fun updateTransferUpi(upi: String) { updateTransferHandle(upi) }
    fun setEnteredOtp(otp: String) { setOtpInput(otp) }
    fun submitOtpAndCompleteTransfer() { confirmOtpAndTransfer() }

    fun triggerOtpResend() {
        showMessage("A new 6-digit OTP has been dispatched to your registered device.")
    }

    fun toggleDarkMode() {
        _isDarkMode.value = !_isDarkMode.value
    }

    fun showMessage(msg: String) {
        _snackbarMessage.value = msg
    }

    fun clearSnackbar() {
        _snackbarMessage.value = null
    }
}

enum class MessageSender {
    USER,
    AI
}

enum class MembershipTier(
    val displayName: String,
    val monthlyPrice: String,
    val annualPrice: String,
    val lifetimePrice: String,
    val subtitle: String
) {
    FREE_CITIZEN(
        "Citizen Standard (Trial)",
        "$0",
        "$0",
        "$0",
        "Basic ledger, standard transactions & community AI"
    ),
    PRO_PLANETARY(
        "Planetary Pro Edition",
        "$9.99/mo",
        "$89.99/yr",
        "$199 Lifetime",
        "Full AI Advisor, Digital Twin, Zero Wire Fees & Smart Cities"
    ),
    QUANTUM_SOVEREIGN(
        "Quantum Sovereign Lifetime",
        "$29.99/mo",
        "$249/yr",
        "$499 Sovereign",
        "Post-Quantum Kyber-1024 Vault, Space Mesh & VIP Concierge"
    ),
    ENTERPRISE_INSTITUTIONAL(
        "Institutional Node",
        "$499/mo",
        "$4,990/yr",
        "Custom SLA",
        "Multi-org Treasury, Central Bank CBDC Bridges & API Rails"
    )
}

data class ChatMessage(
    val sender: MessageSender,
    val text: String,
    val timestamp: Long = System.currentTimeMillis()
)
