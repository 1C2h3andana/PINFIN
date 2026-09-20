package com.example.data.repository

import com.example.data.dao.BankDao
import com.example.data.model.AccountEntity
import com.example.data.model.AccountType
import com.example.data.model.ApplicationFormEntity
import com.example.data.model.ApplicationStatus
import com.example.data.model.ApplicationType
import com.example.data.model.AssetType
import com.example.data.model.BillEntity
import com.example.data.model.BudgetEntity
import com.example.data.model.ClaimStatus
import com.example.data.model.DailySpendingPoint
import com.example.data.model.InsuranceClaimEntity
import com.example.data.model.InsurancePolicyEntity
import com.example.data.model.InsuranceType
import com.example.data.model.InvestmentEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.NotificationType
import com.example.data.model.PolicyStatus
import com.example.data.model.RiskEvaluation
import com.example.data.model.RiskLevel
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.SecurityEventType
import com.example.data.model.SecurityLogEntity
import com.example.data.model.SupportTicketEntity
import com.example.data.model.TicketStatus
import com.example.data.model.TransactionCategory
import com.example.data.model.TransactionEntity
import com.example.data.model.TransactionStatus
import com.example.data.model.TransactionType
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.domain.engine.FraudDetectionEngine
import com.example.domain.security.AuthCrypto
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BankRepository(private val bankDao: BankDao) {

    val users: Flow<List<UserEntity>> = bankDao.getAllUsers()
    val accounts: Flow<List<AccountEntity>> = bankDao.getAllAccounts()
    val transactions: Flow<List<TransactionEntity>> = bankDao.getAllTransactions()
    val highRiskTransactions: Flow<List<TransactionEntity>> = bankDao.getHighRiskTransactions()
    val amlReviewTransactions: Flow<List<TransactionEntity>> = bankDao.getAmlReviewTransactions()
    val budgets: Flow<List<BudgetEntity>> = bankDao.getAllBudgets()
    val savingsGoals: Flow<List<SavingsGoalEntity>> = bankDao.getAllSavingsGoals()
    val bills: Flow<List<BillEntity>> = bankDao.getAllBills()
    val investments: Flow<List<InvestmentEntity>> = bankDao.getAllInvestments()
    val insurancePolicies: Flow<List<InsurancePolicyEntity>> = bankDao.getAllInsurancePolicies()
    val insuranceClaims: Flow<List<InsuranceClaimEntity>> = bankDao.getAllInsuranceClaims()
    val applications: Flow<List<ApplicationFormEntity>> = bankDao.getAllApplications()
    val notifications: Flow<List<NotificationEntity>> = bankDao.getAllNotifications()
    val securityLogs: Flow<List<SecurityLogEntity>> = bankDao.getAllSecurityLogs()
    val supportTickets: Flow<List<SupportTicketEntity>> = bankDao.getAllTickets()

    /**
     * Observable reactive stream of 30 daily spending data points aggregated from Room database.
     * Evaluates all completed DEBIT transactions over the last 30 days.
     */
    val spendingTrendsLast30Days: Flow<List<DailySpendingPoint>> =
        bankDao.getSpendingTransactionsSince(System.currentTimeMillis() - (30L * 86_400_000L))
            .map { txList -> calculateDailySpending(txList, 30) }

    suspend fun seedInitialDataIfEmpty() {
        // 1. Seed Users
        val existingUsers = bankDao.getAllUsers().first()
        if (existingUsers.isEmpty()) {
            val saltUser = AuthCrypto.generateSalt()
            val passHashUser = AuthCrypto.hashPassword("Password@123", saltUser)
            val jwtUser = AuthCrypto.createJwtToken(1L, "alex.morgan@smartbank.ai", "USER")

            val saltAdmin = AuthCrypto.generateSalt()
            val passHashAdmin = AuthCrypto.hashPassword("Admin@Master2026", saltAdmin)
            val jwtAdmin = AuthCrypto.createJwtToken(2L, "admin.security@smartbank.ai", "ADMIN")

            val defaultUsers = listOf(
                UserEntity(
                    id = 1,
                    fullName = "Alex Morgan",
                    email = "alex.morgan@smartbank.ai",
                    passwordHash = passHashUser,
                    salt = saltUser,
                    role = UserRole.USER,
                    jwtToken = jwtUser,
                    isBiometricEnabled = true,
                    pinCode = "1234"
                ),
                UserEntity(
                    id = 2,
                    fullName = "Chief Security Officer",
                    email = "admin.security@smartbank.ai",
                    passwordHash = passHashAdmin,
                    salt = saltAdmin,
                    role = UserRole.ADMIN,
                    jwtToken = jwtAdmin,
                    isBiometricEnabled = true,
                    pinCode = "9999"
                )
            )
            bankDao.insertUsers(defaultUsers)
        }

        // 2. Seed Accounts
        val existingAccounts = bankDao.getAllAccounts().first()
        if (existingAccounts.isEmpty()) {
            val defaultAccounts = listOf(
                AccountEntity(
                    id = 1,
                    name = "Primary Premier Checking",
                    accountNumber = "98234829104",
                    routingNumber = "021000021",
                    balance = 12450.75,
                    type = AccountType.CHECKING,
                    cardNumber = "4532 •••• •••• 8841",
                    expiry = "09/29",
                    cvv = "382",
                    isFrozen = false,
                    dailyLimit = 5000.0,
                    spentToday = 340.0,
                    isContactlessEnabled = true,
                    isInternationalEnabled = true
                ),
                AccountEntity(
                    id = 2,
                    name = "High-Yield AI Savings Vault",
                    accountNumber = "48102938192",
                    routingNumber = "021000021",
                    balance = 34890.50,
                    type = AccountType.SAVINGS,
                    cardNumber = "5412 •••• •••• 4192",
                    expiry = "11/30",
                    cvv = "912",
                    isFrozen = false,
                    dailyLimit = 10000.0,
                    spentToday = 0.0,
                    isContactlessEnabled = true,
                    isInternationalEnabled = false
                ),
                AccountEntity(
                    id = 3,
                    name = "Apex Sapphire Metal Card",
                    accountNumber = "37829104821",
                    routingNumber = "021000021",
                    balance = 1420.30,
                    type = AccountType.CREDIT_CARD,
                    cardNumber = "3782 •••••• 48210",
                    expiry = "04/28",
                    cvv = "741",
                    isFrozen = false,
                    dailyLimit = 7500.0,
                    spentToday = 120.0,
                    isContactlessEnabled = true,
                    isInternationalEnabled = true
                ),
                AccountEntity(
                    id = 4,
                    name = "Global Investment Portfolio Vault",
                    accountNumber = "19283746501",
                    routingNumber = "021000021",
                    balance = 52400.00,
                    type = AccountType.INVESTMENT,
                    cardNumber = "4000 •••• •••• 9921",
                    expiry = "01/30",
                    cvv = "551",
                    isFrozen = false,
                    dailyLimit = 25000.0,
                    spentToday = 0.0,
                    isContactlessEnabled = false,
                    isInternationalEnabled = true
                )
            )
            bankDao.insertAccounts(defaultAccounts)

            val now = System.currentTimeMillis()
            val hourMs = 3600_000L
            val dayMs = 86400_000L

            val defaultTransactions = listOf(
                TransactionEntity(
                    id = 1,
                    accountId = 1,
                    title = "Salary Direct Deposit",
                    recipient = "Tech Innovations Corp",
                    upiOrHandle = "payroll@techinnovations.com",
                    amount = 5800.00,
                    category = TransactionCategory.SALARY,
                    type = TransactionType.CREDIT,
                    timestamp = now - (2 * dayMs),
                    status = TransactionStatus.COMPLETED,
                    riskScore = 2,
                    riskLevel = RiskLevel.LOW,
                    riskReason = "Verified Employer ACH Direct Deposit",
                    is2FaVerified = false
                ),
                TransactionEntity(
                    id = 2,
                    accountId = 1,
                    title = "Whole Foods Organic Market",
                    recipient = "Whole Foods NYC",
                    upiOrHandle = "pos.wf982@retailpay",
                    amount = 142.50,
                    category = TransactionCategory.FOOD,
                    type = TransactionType.DEBIT,
                    timestamp = now - (4 * hourMs),
                    status = TransactionStatus.COMPLETED,
                    riskScore = 8,
                    riskLevel = RiskLevel.LOW,
                    riskReason = "Local contactless terminal match",
                    is2FaVerified = false
                ),
                TransactionEntity(
                    id = 3,
                    accountId = 1,
                    title = "Apple Store Fifth Ave",
                    recipient = "Apple Retail",
                    upiOrHandle = "apple.fifthave@applepay",
                    amount = 1299.00,
                    category = TransactionCategory.SHOPPING,
                    type = TransactionType.DEBIT,
                    timestamp = now - (1 * dayMs),
                    status = TransactionStatus.COMPLETED,
                    riskScore = 25,
                    riskLevel = RiskLevel.LOW,
                    riskReason = "High-ticket hardware purchase with Passkey 2FA confirmation",
                    is2FaVerified = true
                ),
                TransactionEntity(
                    id = 4,
                    accountId = 1,
                    title = "SUSPICIOUS: Lottery Processing Fee",
                    recipient = "FastPrize Intl LLC",
                    upiOrHandle = "claim.prize992@paynow.xyz",
                    amount = 1500.00,
                    category = TransactionCategory.TRANSFER,
                    type = TransactionType.DEBIT,
                    timestamp = now - (5 * hourMs),
                    status = TransactionStatus.BLOCKED_FRAUD,
                    riskScore = 95,
                    riskLevel = RiskLevel.CRITICAL,
                    riskReason = "BLOCKED: Known scam keyword 'Prize' with suspicious domain .xyz and unverified merchant",
                    is2FaVerified = false
                ),
                TransactionEntity(
                    id = 5,
                    accountId = 1,
                    title = "High-Yield Interest Payout",
                    recipient = "SmartBank AI Reserve",
                    upiOrHandle = "interest@smartbank.ai",
                    amount = 118.40,
                    category = TransactionCategory.INVESTMENT,
                    type = TransactionType.CREDIT,
                    timestamp = now - (3 * dayMs),
                    status = TransactionStatus.COMPLETED,
                    riskScore = 1,
                    riskLevel = RiskLevel.LOW,
                    riskReason = "Automated Monthly Yield Payout",
                    is2FaVerified = false
                ),
                TransactionEntity(
                    id = 6,
                    accountId = 1,
                    title = "Electric Utility Grid Bill",
                    recipient = "ConEdison NY",
                    upiOrHandle = "conedison.bill@securepay.org",
                    amount = 135.20,
                    category = TransactionCategory.BILLS,
                    type = TransactionType.DEBIT,
                    timestamp = now - (5 * dayMs),
                    status = TransactionStatus.COMPLETED,
                    riskScore = 4,
                    riskLevel = RiskLevel.LOW,
                    riskReason = "Verified recurring utility merchant",
                    is2FaVerified = false
                ),
                TransactionEntity(
                    id = 7,
                    accountId = 1,
                    title = "Overseas Wire Transfer (AML Flagged)",
                    recipient = "Global Trade Ltd (Seychelles)",
                    upiOrHandle = "trade.seychelles@swiftnet.biz",
                    amount = 8900.00,
                    category = TransactionCategory.TRANSFER,
                    type = TransactionType.DEBIT,
                    timestamp = now - (12 * hourMs),
                    status = TransactionStatus.FLAGGED_REVIEW,
                    riskScore = 65,
                    riskLevel = RiskLevel.HIGH,
                    riskReason = "AML Tier 2 Flag: Cross-border high value wire to offshore jurisdiction",
                    is2FaVerified = true
                ),
                TransactionEntity(
                    id = 8,
                    accountId = 1,
                    title = "Metro Rail & Transit Pass",
                    recipient = "MTA Transit NY",
                    upiOrHandle = "mta.metro@omny.info",
                    amount = 132.00,
                    category = TransactionCategory.OTHER,
                    type = TransactionType.DEBIT,
                    timestamp = now - (7 * dayMs),
                    status = TransactionStatus.COMPLETED,
                    riskScore = 2,
                    riskLevel = RiskLevel.LOW
                ),
                TransactionEntity(
                    id = 9,
                    accountId = 1,
                    title = "Blue Bottle Artisan Espresso",
                    recipient = "Blue Bottle Coffee",
                    upiOrHandle = "bluebottle@pos.square",
                    amount = 18.50,
                    category = TransactionCategory.FOOD,
                    type = TransactionType.DEBIT,
                    timestamp = now - (9 * dayMs),
                    status = TransactionStatus.COMPLETED,
                    riskScore = 1,
                    riskLevel = RiskLevel.LOW
                ),
                TransactionEntity(
                    id = 10,
                    accountId = 1,
                    title = "Amazon Prime & Electronics",
                    recipient = "Amazon Retail",
                    upiOrHandle = "payments@amazon.com",
                    amount = 179.90,
                    category = TransactionCategory.SHOPPING,
                    type = TransactionType.DEBIT,
                    timestamp = now - (11 * dayMs),
                    status = TransactionStatus.COMPLETED,
                    riskScore = 5,
                    riskLevel = RiskLevel.LOW
                ),
                TransactionEntity(
                    id = 11,
                    accountId = 1,
                    title = "Trader Joe's Fresh Market",
                    recipient = "Trader Joe's",
                    upiOrHandle = "tj902@grocerypay.us",
                    amount = 94.60,
                    category = TransactionCategory.FOOD,
                    type = TransactionType.DEBIT,
                    timestamp = now - (13 * dayMs),
                    status = TransactionStatus.COMPLETED,
                    riskScore = 2,
                    riskLevel = RiskLevel.LOW
                ),
                TransactionEntity(
                    id = 12,
                    accountId = 1,
                    title = "Equinox Fitness Health Club",
                    recipient = "Equinox Gyms",
                    upiOrHandle = "membership@equinox.com",
                    amount = 240.00,
                    category = TransactionCategory.HEALTHCARE,
                    type = TransactionType.DEBIT,
                    timestamp = now - (15 * dayMs),
                    status = TransactionStatus.COMPLETED,
                    riskScore = 2,
                    riskLevel = RiskLevel.LOW
                ),
                TransactionEntity(
                    id = 13,
                    accountId = 1,
                    title = "Uber Executive Airport Ride",
                    recipient = "Uber Technologies",
                    upiOrHandle = "uber.us@uberpay.com",
                    amount = 58.40,
                    category = TransactionCategory.OTHER,
                    type = TransactionType.DEBIT,
                    timestamp = now - (18 * dayMs),
                    status = TransactionStatus.COMPLETED,
                    riskScore = 3,
                    riskLevel = RiskLevel.LOW
                ),
                TransactionEntity(
                    id = 14,
                    accountId = 1,
                    title = "Barnes & Noble Tech Guides",
                    recipient = "Barnes & Noble",
                    upiOrHandle = "bn.union_sq@retail",
                    amount = 45.00,
                    category = TransactionCategory.EDUCATION,
                    type = TransactionType.DEBIT,
                    timestamp = now - (20 * dayMs),
                    status = TransactionStatus.COMPLETED,
                    riskScore = 2,
                    riskLevel = RiskLevel.LOW
                ),
                TransactionEntity(
                    id = 15,
                    accountId = 1,
                    title = "Sweetgreen Healthy Dining",
                    recipient = "Sweetgreen NYC",
                    upiOrHandle = "pos.sg48@greenpay",
                    amount = 22.80,
                    category = TransactionCategory.FOOD,
                    type = TransactionType.DEBIT,
                    timestamp = now - (22 * dayMs),
                    status = TransactionStatus.COMPLETED,
                    riskScore = 1,
                    riskLevel = RiskLevel.LOW
                ),
                TransactionEntity(
                    id = 16,
                    accountId = 1,
                    title = "Target Household Restock",
                    recipient = "Target Stores",
                    upiOrHandle = "target.pay@target.com",
                    amount = 115.30,
                    category = TransactionCategory.SHOPPING,
                    type = TransactionType.DEBIT,
                    timestamp = now - (25 * dayMs),
                    status = TransactionStatus.COMPLETED,
                    riskScore = 4,
                    riskLevel = RiskLevel.LOW
                ),
                TransactionEntity(
                    id = 17,
                    accountId = 1,
                    title = "Shell Highway Fuel & Service",
                    recipient = "Shell Oil Station",
                    upiOrHandle = "station.shell71@fuelpay",
                    amount = 62.50,
                    category = TransactionCategory.OTHER,
                    type = TransactionType.DEBIT,
                    timestamp = now - (27 * dayMs),
                    status = TransactionStatus.COMPLETED,
                    riskScore = 3,
                    riskLevel = RiskLevel.LOW
                ),
                TransactionEntity(
                    id = 18,
                    accountId = 1,
                    title = "Nobu Downtown Fine Dining",
                    recipient = "Nobu Downtown",
                    upiOrHandle = "nobu.hospitality@nydining",
                    amount = 285.00,
                    category = TransactionCategory.FOOD,
                    type = TransactionType.DEBIT,
                    timestamp = now - (29 * dayMs),
                    status = TransactionStatus.COMPLETED,
                    riskScore = 6,
                    riskLevel = RiskLevel.LOW
                )
            )
            bankDao.insertTransactions(defaultTransactions)

            // 3. Budgets
            val defaultBudgets = listOf(
                BudgetEntity(category = TransactionCategory.FOOD, monthlyLimit = 800.0, currentSpent = 460.0),
                BudgetEntity(category = TransactionCategory.SHOPPING, monthlyLimit = 1500.0, currentSpent = 1299.0),
                BudgetEntity(category = TransactionCategory.BILLS, monthlyLimit = 600.0, currentSpent = 385.20),
                BudgetEntity(category = TransactionCategory.ENTERTAINMENT, monthlyLimit = 400.0, currentSpent = 120.0),
                BudgetEntity(category = TransactionCategory.HEALTHCARE, monthlyLimit = 350.0, currentSpent = 75.0),
                BudgetEntity(category = TransactionCategory.EDUCATION, monthlyLimit = 500.0, currentSpent = 150.0)
            )
            bankDao.insertBudgets(defaultBudgets)

            // 4. Savings Goals
            val defaultGoals = listOf(
                SavingsGoalEntity(id = 1, title = "Emergency Safety Reserve (6 Months)", targetAmount = 30000.0, currentAmount = 24890.50, targetDate = "Dec 2026", iconKey = "SHIELD"),
                SavingsGoalEntity(id = 2, title = "Tokyo & Kyoto Tech Expedition", targetAmount = 6500.0, currentAmount = 4200.00, targetDate = "Nov 2026", iconKey = "TRAVEL"),
                SavingsGoalEntity(id = 3, title = "Cybersecurity Lab Server Rack", targetAmount = 3500.0, currentAmount = 2100.00, targetDate = "Oct 2026", iconKey = "TECH")
            )
            bankDao.insertSavingsGoals(defaultGoals)

            // 5. Bills
            val defaultBills = listOf(
                BillEntity(id = 1, billerName = "ConEdison Electricity", category = "Utilities", amount = 135.20, dueDate = "Aug 24, 2026", isAutoPay = true, isPaid = true),
                BillEntity(id = 2, billerName = "Verizon 5G Ultra Broadband", category = "Internet", amount = 89.99, dueDate = "Aug 28, 2026", isAutoPay = true, isPaid = false),
                BillEntity(id = 3, billerName = "Metropolitan Luxury Apartment Rent", category = "Housing", amount = 2400.00, dueDate = "Sep 01, 2026", isAutoPay = false, isPaid = false),
                BillEntity(id = 4, billerName = "Apex Sapphire Statement Balance", category = "Credit Card", amount = 1420.30, dueDate = "Sep 05, 2026", isAutoPay = false, isPaid = false)
            )
            bankDao.insertBills(defaultBills)

            // 6. Investments
            val defaultInvestments = listOf(
                InvestmentEntity(id = 1, symbol = "AAPL", name = "Apple Inc.", assetType = AssetType.STOCK, sharesOrUnits = 45.0, avgBuyPrice = 185.20, currentPrice = 224.50, dailyChangePercent = 2.45, allocationPercent = 28.0),
                InvestmentEntity(id = 2, symbol = "NVDA", name = "Nvidia Corporation", assetType = AssetType.STOCK, sharesOrUnits = 25.0, avgBuyPrice = 98.40, currentPrice = 128.90, dailyChangePercent = 4.12, allocationPercent = 22.0),
                InvestmentEntity(id = 3, symbol = "BTC", name = "Bitcoin Digital Gold", assetType = AssetType.CRYPTO, sharesOrUnits = 0.35, avgBuyPrice = 58200.0, currentPrice = 64500.0, dailyChangePercent = 3.20, allocationPercent = 20.0),
                InvestmentEntity(id = 4, symbol = "VOO", name = "Vanguard S&P 500 ETF", assetType = AssetType.ETF, sharesOrUnits = 30.0, avgBuyPrice = 440.00, currentPrice = 512.40, dailyChangePercent = 0.85, allocationPercent = 18.0),
                InvestmentEntity(id = 5, symbol = "GOLD", name = "Physical Allocated Gold Vault", assetType = AssetType.GOLD, sharesOrUnits = 4.0, avgBuyPrice = 2150.00, currentPrice = 2480.00, dailyChangePercent = 0.40, allocationPercent = 12.0)
            )
            bankDao.insertInvestments(defaultInvestments)

            // 7. Insurance Policies
            val defaultPolicies = listOf(
                InsurancePolicyEntity(id = 1, policyNumber = "POL-HLT-88219", type = InsuranceType.HEALTH, planName = "Executive Comprehensive Health Shield", provider = "Allianz Global Life", coverageAmount = 500000.0, monthlyPremium = 145.0, nextBillingDate = "Sep 01, 2026", status = PolicyStatus.ACTIVE, keyFeatures = "Zero Co-Pay • Global Hospital Network • AI Diagnostics"),
                InsurancePolicyEntity(id = 2, policyNumber = "POL-CYB-10948", type = InsuranceType.CYBER_SHIELD, planName = "Identity Theft & Cyber Fraud Protection", provider = "SmartBank CyberGuard", coverageAmount = 100000.0, monthlyPremium = 24.99, nextBillingDate = "Sep 05, 2026", status = PolicyStatus.ACTIVE, keyFeatures = "$100k Zero Liability • Dark Web Monitoring • SIM Swap Defense"),
                InsurancePolicyEntity(id = 3, policyNumber = "POL-AUT-44912", type = InsuranceType.AUTO, planName = "Zero-Depreciation EV & Auto Guard", provider = "Liberty Mutual", coverageAmount = 65000.0, monthlyPremium = 110.0, nextBillingDate = "Sep 15, 2026", status = PolicyStatus.ACTIVE, keyFeatures = "Roadside Assistance • Battery Coverage • Instant Crash Detection")
            )
            bankDao.insertInsurancePolicies(defaultPolicies)

            // 8. Application Forms
            val defaultApplications = listOf(
                ApplicationFormEntity(
                    id = 1,
                    type = ApplicationType.LOAN,
                    applicantName = "Alex Morgan",
                    email = "alex.morgan@smartbank.ai",
                    phone = "+1 (555) 982-1049",
                    annualIncome = 145000.0,
                    employmentType = "Full-Time Senior Software Engineer",
                    requestedAmountOrTier = "$45,000 Home Renovation Loan",
                    idProofType = "US Passport / SSN Verified",
                    idProofNumber = "P9823***41",
                    status = ApplicationStatus.AI_PRE_APPROVED,
                    aiScore = 92,
                    aiRemarks = "AI Underwriting: Exceptional credit health (785 FICO), low debt-to-income (18.4%), instantly pre-approved at 7.2% APR."
                ),
                ApplicationFormEntity(
                    id = 2,
                    type = ApplicationType.CREDIT_CARD,
                    applicantName = "Alex Morgan",
                    email = "alex.morgan@smartbank.ai",
                    phone = "+1 (555) 982-1049",
                    annualIncome = 145000.0,
                    employmentType = "Full-Time Senior Software Engineer",
                    requestedAmountOrTier = "Titanium CyberShield Card ($25,000 Limit)",
                    idProofType = "Driver's License",
                    idProofNumber = "DL-NY-8821***",
                    status = ApplicationStatus.APPROVED,
                    aiScore = 95,
                    aiRemarks = "AI Underwriting: Tier 1 Prime rating. Card minted and ready for digital wallet link."
                )
            )
            bankDao.insertApplications(defaultApplications)

            // 9. Notifications
            val defaultNotifications = listOf(
                NotificationEntity(
                    id = 1,
                    title = "🚨 Real-Time Fraud Shield Action",
                    message = "SmartBank AI blocked a $1,500.00 fraudulent transfer to FastPrize Intl (.xyz domain). Your balance is fully protected.",
                    type = NotificationType.FRAUD_WARNING,
                    timestamp = now - (5 * hourMs),
                    isRead = false,
                    actionRoute = "security"
                ),
                NotificationEntity(
                    id = 2,
                    title = "💰 Salary Direct Deposit Credited",
                    message = "$5,800.00 from Tech Innovations Corp was successfully deposited to Primary Checking.",
                    type = NotificationType.TRANSACTION_ALERT,
                    timestamp = now - (2 * dayMs),
                    isRead = false,
                    actionRoute = "home"
                ),
                NotificationEntity(
                    id = 3,
                    title = "📄 Bill Due in 4 Days",
                    message = "Verizon 5G Broadband ($89.99) is due on Aug 28, 2026. Auto-Pay is active.",
                    type = NotificationType.PAYMENT_DUE,
                    timestamp = now - (1 * dayMs),
                    isRead = true,
                    actionRoute = "bills"
                ),
                NotificationEntity(
                    id = 4,
                    title = "🛡️ Security Health Score: 98/100",
                    message = "Hardware biometric passkeys and 2FA are active. Zero suspicious logins detected.",
                    type = NotificationType.SECURITY_ALERT,
                    timestamp = now - (6 * hourMs),
                    isRead = true,
                    actionRoute = "security"
                )
            )
            bankDao.insertNotifications(defaultNotifications)

            // 10. Security Logs
            val defaultLogs = listOf(
                SecurityLogEntity(id = 1, timestamp = now - (15 * 60_000L), eventType = SecurityEventType.LOGIN_SUCCESS, details = "Biometric Passkey Authentication Verified via Pixel 8 Pro", severity = RiskLevel.LOW),
                SecurityLogEntity(id = 2, timestamp = now - (5 * hourMs), eventType = SecurityEventType.SUSPICIOUS_TRANSFER_BLOCKED, details = "Automated AI Shield blocked $1,500 transfer to known phishing handle", severity = RiskLevel.CRITICAL),
                SecurityLogEntity(id = 3, timestamp = now - (1 * dayMs), eventType = SecurityEventType.OTP_CHALLENGE_ISSUED, details = "Step-up 2FA Challenge completed for $1,299 Apple purchase", severity = RiskLevel.MEDIUM),
                SecurityLogEntity(id = 4, timestamp = now - (2 * dayMs), eventType = SecurityEventType.DEVICE_LINKED, details = "New Hardware Key Pixel 8 Pro authorized as primary trusted device", severity = RiskLevel.LOW)
            )
            bankDao.insertSecurityLogs(defaultLogs)

            // 11. Support Tickets
            val defaultTickets = listOf(
                SupportTicketEntity(id = 1, title = "Dispute suspicious prize claim charge", description = "I received a message claiming I won a lottery and almost sent money. SmartBank blocked it.", category = "Fraud Dispute", status = TicketStatus.RESOLVED, aiResponse = "SmartBank AI automated threat defense blocked this vector. No funds were debited. Your card details remain encrypted.")
            )
            bankDao.insertTicket(defaultTickets[0])
        }
    }

    // --- Authentication Operations ---
    suspend fun authenticateUser(email: String, passwordAttempt: String): UserEntity? {
        val user = bankDao.getUserByEmail(email.trim().lowercase()) ?: return null
        val isValid = AuthCrypto.verifyPassword(passwordAttempt, user.salt, user.passwordHash)
        if (isValid) {
            val freshToken = AuthCrypto.createJwtToken(user.id, user.email, user.role.name)
            val updated = user.copy(jwtToken = freshToken)
            bankDao.updateUser(updated)
            logSecurityEvent(SecurityEventType.LOGIN_SUCCESS, "User ${user.fullName} logged in successfully (JWT issued)", RiskLevel.LOW)
            return updated
        } else {
            logSecurityEvent(SecurityEventType.LOGIN_FAILED, "Failed login attempt for email $email", RiskLevel.HIGH)
            return null
        }
    }

    suspend fun registerUser(fullName: String, email: String, password: String, role: UserRole = UserRole.USER): UserEntity {
        val salt = AuthCrypto.generateSalt()
        val hash = AuthCrypto.hashPassword(password, salt)
        val tempJwt = AuthCrypto.createJwtToken(0L, email, role.name)
        val user = UserEntity(
            fullName = fullName,
            email = email.trim().lowercase(),
            passwordHash = hash,
            salt = salt,
            role = role,
            jwtToken = tempJwt,
            isBiometricEnabled = true
        )
        val userId = bankDao.insertUser(user)
        val finalJwt = AuthCrypto.createJwtToken(userId, user.email, role.name)
        val finalUser = user.copy(id = userId, jwtToken = finalJwt)
        bankDao.updateUser(finalUser)
        logSecurityEvent(SecurityEventType.LOGIN_SUCCESS, "New account registered for $fullName ($email)", RiskLevel.LOW)
        return finalUser
    }

    // --- Deposit & Withdrawal Operations ---
    suspend fun performDeposit(accountId: Long, amount: Double, depositSource: String, note: String): Boolean {
        if (amount <= 0) return false
        val account = bankDao.getAccountById(accountId) ?: return false

        bankDao.updateAccountBalance(accountId, amount)

        val tx = TransactionEntity(
            accountId = accountId,
            title = "Deposit via $depositSource",
            recipient = account.name,
            upiOrHandle = "deposit@smartbank.ai",
            amount = amount,
            category = TransactionCategory.DEPOSIT,
            type = TransactionType.CREDIT,
            timestamp = System.currentTimeMillis(),
            status = TransactionStatus.COMPLETED,
            riskScore = 3,
            riskLevel = RiskLevel.LOW,
            riskReason = "Verified Inward Electronic Deposit",
            is2FaVerified = false,
            note = note
        )
        bankDao.insertTransaction(tx)

        val notif = NotificationEntity(
            title = "💵 Deposit Confirmed",
            message = "$${"%,.2f".format(amount)} was successfully credited to ${account.name}.",
            type = NotificationType.TRANSACTION_ALERT,
            actionRoute = "home"
        )
        bankDao.insertNotification(notif)

        logSecurityEvent(SecurityEventType.DEPOSIT_COMPLETED, "Deposit of $${"%,.2f".format(amount)} into account ${account.accountNumber}", RiskLevel.LOW)
        return true
    }

    suspend fun performWithdrawal(accountId: Long, amount: Double, destination: String, note: String): Boolean {
        if (amount <= 0) return false
        val account = bankDao.getAccountById(accountId) ?: return false
        if (account.balance < amount) return false

        bankDao.updateAccountBalance(accountId, -amount)

        val tx = TransactionEntity(
            accountId = accountId,
            title = "Withdrawal to $destination",
            recipient = destination,
            upiOrHandle = "atm.withdrawal@network",
            amount = amount,
            category = TransactionCategory.WITHDRAWAL,
            type = TransactionType.DEBIT,
            timestamp = System.currentTimeMillis(),
            status = TransactionStatus.COMPLETED,
            riskScore = 15,
            riskLevel = RiskLevel.LOW,
            riskReason = "Authorized Cash / Wire Withdrawal",
            is2FaVerified = true,
            note = note
        )
        bankDao.insertTransaction(tx)

        val notif = NotificationEntity(
            title = "🏧 Withdrawal Processed",
            message = "$${"%,.2f".format(amount)} was debited from ${account.name} to $destination.",
            type = NotificationType.TRANSACTION_ALERT,
            actionRoute = "home"
        )
        bankDao.insertNotification(notif)

        logSecurityEvent(SecurityEventType.WITHDRAWAL_COMPLETED, "Withdrawal of $${"%,.2f".format(amount)} from account ${account.accountNumber}", RiskLevel.LOW)
        return true
    }

    // --- Transfer Operation ---
    suspend fun executeTransfer(
        sourceAccountId: Long,
        recipient: String,
        upiOrHandle: String,
        amount: Double,
        category: TransactionCategory,
        note: String,
        is2FaBypassed: Boolean = false
    ): Pair<Boolean, TransactionEntity> {
        val account = bankDao.getAccountById(sourceAccountId) ?: throw IllegalStateException("Account not found")

        val riskEval = FraudDetectionEngine.evaluateTransaction(
            amount = amount,
            recipient = recipient,
            upiOrHandle = upiOrHandle,
            currentBalance = account.balance,
            note = note
        )

        if (riskEval.shouldBlock) {
            val blockedTx = TransactionEntity(
                accountId = sourceAccountId,
                title = "BLOCKED: $recipient",
                recipient = recipient,
                upiOrHandle = upiOrHandle,
                amount = amount,
                category = category,
                type = TransactionType.DEBIT,
                timestamp = System.currentTimeMillis(),
                status = TransactionStatus.BLOCKED_FRAUD,
                riskScore = riskEval.score,
                riskLevel = riskEval.level,
                riskReason = riskEval.recommendation,
                is2FaVerified = false,
                note = note
            )
            val txId = bankDao.insertTransaction(blockedTx)
            logSecurityEvent(SecurityEventType.SUSPICIOUS_TRANSFER_BLOCKED, "AI Fraud Shield blocked $amount transfer to $recipient: ${riskEval.recommendation}", RiskLevel.CRITICAL)
            return Pair(false, blockedTx.copy(id = txId))
        }

        // Deduct balance
        bankDao.updateAccountBalance(sourceAccountId, -amount)
        bankDao.addBudgetSpent(category, amount)

        val completedTx = TransactionEntity(
            accountId = sourceAccountId,
            title = "Transfer to $recipient",
            recipient = recipient,
            upiOrHandle = upiOrHandle,
            amount = amount,
            category = category,
            type = TransactionType.DEBIT,
            timestamp = System.currentTimeMillis(),
            status = TransactionStatus.COMPLETED,
            riskScore = riskEval.score,
            riskLevel = riskEval.level,
            riskReason = riskEval.recommendation,
            is2FaVerified = is2FaBypassed || riskEval.requires2Fa,
            note = note
        )
        val txId = bankDao.insertTransaction(completedTx)

        val notif = NotificationEntity(
            title = "💸 Transfer Sent Successfully",
            message = "$${"%,.2f".format(amount)} transferred to $recipient ($upiOrHandle).",
            type = NotificationType.TRANSACTION_ALERT,
            actionRoute = "home"
        )
        bankDao.insertNotification(notif)

        logSecurityEvent(SecurityEventType.LOGIN_SUCCESS, "Transfer of $amount completed to $recipient", RiskLevel.LOW)
        return Pair(true, completedTx.copy(id = txId))
    }

    // --- Investments Operations ---
    suspend fun buyInvestment(symbol: String, shares: Double, pricePerShare: Double, accountId: Long = 4): Boolean {
        val totalCost = shares * pricePerShare
        val investmentAccount = bankDao.getAccountById(accountId) ?: return false
        if (investmentAccount.balance < totalCost) return false

        bankDao.updateAccountBalance(accountId, -totalCost)

        val existing = bankDao.getInvestmentBySymbol(symbol)
        if (existing != null) {
            val totalShares = existing.sharesOrUnits + shares
            val newAvgPrice = ((existing.sharesOrUnits * existing.avgBuyPrice) + totalCost) / totalShares
            bankDao.updateInvestment(existing.copy(sharesOrUnits = totalShares, avgBuyPrice = newAvgPrice, currentPrice = pricePerShare))
        }

        val tx = TransactionEntity(
            accountId = accountId,
            title = "Buy $symbol ($shares units)",
            recipient = "Exchange Market Execution",
            upiOrHandle = "broker@smartbank.ai",
            amount = totalCost,
            category = TransactionCategory.INVESTMENT,
            type = TransactionType.DEBIT,
            timestamp = System.currentTimeMillis(),
            status = TransactionStatus.COMPLETED,
            riskScore = 5,
            riskLevel = RiskLevel.LOW,
            riskReason = "Direct Broker Order Execution"
        )
        bankDao.insertTransaction(tx)

        val notif = NotificationEntity(
            title = "📈 Investment Order Executed",
            message = "Purchased $shares units of $symbol for $${"%,.2f".format(totalCost)}.",
            type = NotificationType.TRANSACTION_ALERT,
            actionRoute = "investments"
        )
        bankDao.insertNotification(notif)
        return true
    }

    suspend fun sellInvestment(symbol: String, shares: Double, pricePerShare: Double, accountId: Long = 4): Boolean {
        val existing = bankDao.getInvestmentBySymbol(symbol) ?: return false
        if (existing.sharesOrUnits < shares) return false

        val proceeds = shares * pricePerShare
        bankDao.updateAccountBalance(accountId, proceeds)

        val remainingShares = existing.sharesOrUnits - shares
        bankDao.updateInvestment(existing.copy(sharesOrUnits = remainingShares, currentPrice = pricePerShare))

        val tx = TransactionEntity(
            accountId = accountId,
            title = "Sell $symbol ($shares units)",
            recipient = "Exchange Market Liquidation",
            upiOrHandle = "broker@smartbank.ai",
            amount = proceeds,
            category = TransactionCategory.INVESTMENT,
            type = TransactionType.CREDIT,
            timestamp = System.currentTimeMillis(),
            status = TransactionStatus.COMPLETED,
            riskScore = 5,
            riskLevel = RiskLevel.LOW,
            riskReason = "Direct Broker Sell Execution"
        )
        bankDao.insertTransaction(tx)

        val notif = NotificationEntity(
            title = "📉 Investment Sold",
            message = "Sold $shares units of $symbol. $${"%,.2f".format(proceeds)} credited to investment vault.",
            type = NotificationType.TRANSACTION_ALERT,
            actionRoute = "investments"
        )
        bankDao.insertNotification(notif)
        return true
    }

    // --- Insurance Operations ---
    suspend fun submitInsuranceClaim(policyId: Long, policyType: InsuranceType, amount: Double, incidentDate: String, description: String): Long {
        val claim = InsuranceClaimEntity(
            policyId = policyId,
            policyType = policyType,
            claimAmount = amount,
            incidentDate = incidentDate,
            description = description,
            status = ClaimStatus.SUBMITTED,
            timestamp = System.currentTimeMillis()
        )
        val claimId = bankDao.insertInsuranceClaim(claim)

        val notif = NotificationEntity(
            title = "🛡️ Insurance Claim Submitted",
            message = "Claim #CLM-$claimId for $${"%,.2f".format(amount)} has been submitted to claims underwriter.",
            type = NotificationType.APPLICATION_UPDATE,
            actionRoute = "insurance"
        )
        bankDao.insertNotification(notif)
        return claimId
    }

    // --- Application Forms Operations ---
    suspend fun submitApplication(application: ApplicationFormEntity): Long {
        val appId = bankDao.insertApplication(application)

        val notif = NotificationEntity(
            title = "📋 Application Received: ${application.type.name}",
            message = "Your application for ${application.requestedAmountOrTier} is ${application.status.name.replace('_', ' ')} with AI score ${application.aiScore}/100.",
            type = NotificationType.APPLICATION_UPDATE,
            actionRoute = "applications"
        )
        bankDao.insertNotification(notif)
        return appId
    }

    suspend fun updateApplicationStatus(appId: Long, status: ApplicationStatus) {
        bankDao.updateApplicationStatus(appId, status)
    }

    // --- Admin Operations ---
    suspend fun adminApproveTransaction(txId: Long) {
        bankDao.updateTransactionStatus(txId, TransactionStatus.COMPLETED)
        logSecurityEvent(SecurityEventType.ADMIN_TRANSACTION_APPROVED, "Admin manually approved flagged transaction #$txId", RiskLevel.LOW)
    }

    suspend fun adminRevertTransaction(txId: Long) {
        bankDao.updateTransactionStatus(txId, TransactionStatus.REVERTED)
        logSecurityEvent(SecurityEventType.ADMIN_TRANSACTION_REVERTED, "Admin manually reverted/rejected flagged transaction #$txId", RiskLevel.MEDIUM)
    }

    // --- Notifications Management ---
    suspend fun markNotificationRead(id: Long) = bankDao.markNotificationRead(id)
    suspend fun markAllNotificationsRead() = bankDao.markAllNotificationsRead()
    suspend fun clearAllNotifications() = bankDao.clearAllNotifications()

    // --- Card Controls & Logs ---
    suspend fun toggleCardFreeze(accountId: Long, isFrozen: Boolean) {
        bankDao.updateCardFrozen(accountId, isFrozen)
        logSecurityEvent(SecurityEventType.CARD_FROZEN, "Account card #$accountId frozen state changed to $isFrozen", RiskLevel.MEDIUM)
    }

    suspend fun toggleContactless(accountId: Long, isEnabled: Boolean) = bankDao.updateContactless(accountId, isEnabled)
    suspend fun toggleInternational(accountId: Long, isEnabled: Boolean) = bankDao.updateInternational(accountId, isEnabled)
    suspend fun updateDailyLimit(accountId: Long, limit: Double) = bankDao.updateDailyLimit(accountId, limit)

    suspend fun payBill(billId: Long, accountId: Long = 1) {
        val bill = bankDao.getAllBills().first().find { it.id == billId } ?: return
        if (bill.isPaid) return

        bankDao.markBillPaid(billId)
        bankDao.updateAccountBalance(accountId, -bill.amount)
        bankDao.addBudgetSpent(TransactionCategory.BILLS, bill.amount)

        val tx = TransactionEntity(
            accountId = accountId,
            title = "Bill Payment: ${bill.billerName}",
            recipient = bill.billerName,
            upiOrHandle = "autopay@${bill.billerName.lowercase().replace(" ", "")}.com",
            amount = bill.amount,
            category = TransactionCategory.BILLS,
            type = TransactionType.DEBIT,
            timestamp = System.currentTimeMillis(),
            status = TransactionStatus.COMPLETED,
            riskScore = 5,
            riskLevel = RiskLevel.LOW,
            riskReason = "Scheduled Bill Settlement"
        )
        bankDao.insertTransaction(tx)
    }

    suspend fun toggleBillAutoPay(billId: Long, autoPay: Boolean) = bankDao.toggleBillAutoPay(billId, autoPay)

    suspend fun depositToSavingsGoal(goalId: Long, amount: Double, sourceAccountId: Long = 1) {
        val account = bankDao.getAccountById(sourceAccountId) ?: return
        if (account.balance < amount) return

        bankDao.updateAccountBalance(sourceAccountId, -amount)
        bankDao.depositToSavingsGoal(goalId, amount)

        val tx = TransactionEntity(
            accountId = sourceAccountId,
            title = "Deposit to Savings Goal",
            recipient = "Savings Vault #$goalId",
            upiOrHandle = "vault@smartbank.ai",
            amount = amount,
            category = TransactionCategory.INVESTMENT,
            type = TransactionType.DEBIT,
            timestamp = System.currentTimeMillis(),
            status = TransactionStatus.COMPLETED,
            riskScore = 2,
            riskLevel = RiskLevel.LOW,
            riskReason = "Internal Goal Transfer"
        )
        bankDao.insertTransaction(tx)
    }

    suspend fun submitSupportTicket(title: String, description: String, category: String): Long {
        val aiResponse = "Our automated AI Concierge reviewed your inquiry regarding '$title'. If this is related to a dispute or fraud check, all associated account cards have been monitored. Reference #TKT-${System.currentTimeMillis() % 100000}."
        val ticket = SupportTicketEntity(
            title = title,
            description = description,
            category = category,
            status = TicketStatus.AI_RESOLVING,
            aiResponse = aiResponse
        )
        return bankDao.insertTicket(ticket)
    }

    suspend fun createSavingsGoal(goal: SavingsGoalEntity): Long = bankDao.insertSavingsGoal(goal)

    suspend fun flagTransactionAsFraud(txId: Long) {
        bankDao.updateTransactionStatus(txId, TransactionStatus.FLAGGED_REVIEW)
        logSecurityEvent(SecurityEventType.SUSPICIOUS_TRANSFER_BLOCKED, "User flagged transaction #$txId as potential fraud", RiskLevel.HIGH)
    }

    suspend fun logSecurityEvent(eventType: SecurityEventType, details: String, severity: RiskLevel) {
        val log = SecurityLogEntity(
            eventType = eventType,
            details = details,
            severity = severity
        )
        bankDao.insertSecurityLog(log)
    }

    companion object {
        /**
         * Aggregates transactions into discrete daily spending points over the specified number of days (default 30).
         * Calculates total expenditure per day and transaction count for rendering in D3.js line charts.
         */
        fun calculateDailySpending(
            transactions: List<TransactionEntity>,
            days: Int = 30
        ): List<DailySpendingPoint> {
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
            val todayStart = calendar.timeInMillis
            val oneDayMs = 86_400_000L

            val shortDateFormat = SimpleDateFormat("MMM dd", Locale.US)
            val isoDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)

            val result = mutableListOf<DailySpendingPoint>()

            for (i in (days - 1) downTo 0) {
                val dayStart = todayStart - (i * oneDayMs)
                val dayEnd = dayStart + oneDayMs
                val dayDate = Date(dayStart)

                val dayTx = transactions.filter { tx ->
                    tx.type == TransactionType.DEBIT &&
                    tx.status != TransactionStatus.BLOCKED_FRAUD &&
                    tx.status != TransactionStatus.REVERTED &&
                    tx.timestamp in dayStart until dayEnd
                }

                val totalSpent = dayTx.sumOf { it.amount }
                val count = dayTx.size

                result.add(
                    DailySpendingPoint(
                        date = shortDateFormat.format(dayDate),
                        fullDate = isoDateFormat.format(dayDate),
                        timestamp = dayStart,
                        amount = Math.round(totalSpent * 100.0) / 100.0,
                        transactionCount = count
                    )
                )
            }
            return result
        }
    }
}
