package com.example.ui.screens

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.data.model.AccountEntity
import com.example.data.model.TransactionCategory
import com.example.data.model.TransactionEntity
import com.example.data.model.TransactionStatus
import com.example.data.model.TransactionType
import com.example.data.model.UserRole
import com.example.ui.components.PulsingStatusBadge
import com.example.ui.components.RiskBadge
import com.example.ui.components.TransactionRowItem
import com.example.ui.components.VirtualCardItem
import com.example.ui.components.BackupReminderBanner
import com.example.ui.components.VoiceSpendingQueryWidget
import com.example.ui.components.CurrencyExchangeWidget
import com.example.ui.components.CategorySpendingThresholdDialog
import com.example.ui.components.MonthOverMonthSpendingLineChart
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.NotificationsActive
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.CrimsonDanger
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyCardLight
import com.example.ui.theme.PurpleTech
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BankViewModel

@Composable
fun HomeScreen(
    viewModel: BankViewModel,
    onNavigateTransfer: () -> Unit,
    onNavigateSecurity: () -> Unit,
    onNavigateAiAdvisor: () -> Unit,
    onNavigateBills: () -> Unit,
    onNavigateLoans: () -> Unit,
    onNavigateInvestments: () -> Unit,
    onNavigateInsurance: () -> Unit,
    onNavigateApplications: () -> Unit,
    onNavigateAdmin: () -> Unit,
    onOpenDeposit: () -> Unit,
    onOpenWithdraw: () -> Unit,
    onOpenStatements: () -> Unit,
    onOpenAuth: () -> Unit,
    onNavigateGlobalEconomy: () -> Unit = {},
    onNavigateWorldIdentity: () -> Unit = {},
    onNavigateLifePlanner: () -> Unit = {},
    onNavigateWealthEngine: () -> Unit = {},
    onNavigateCrisisIntelligence: () -> Unit = {},
    onNavigateFutureOs: () -> Unit = {},
    onNavigateLanding: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val totalNetWorth by viewModel.totalNetWorth.collectAsStateWithLifecycle()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

    var selectedTxForDetails by remember { mutableStateOf<TransactionEntity?>(null) }
    var showThresholdDialog by remember { mutableStateOf(false) }
    var dashboardTab by remember { mutableIntStateOf(0) }
    var selectedAccountId by remember { mutableStateOf<Long?>(null) }
    var isPrivacyMode by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf<TransactionCategory?>(null) }
    var selectedFilterMode by remember { mutableStateOf(TransactionFilterMode.ALL) }

    val filteredTransactions = remember(transactions, searchQuery, selectedCategoryFilter, selectedFilterMode, selectedAccountId) {
        transactions.filter { tx ->
            val matchesAccount = selectedAccountId == null || tx.accountId == selectedAccountId

            val matchesQuery = searchQuery.isBlank() ||
                    tx.title.contains(searchQuery, ignoreCase = true) ||
                    tx.recipient.contains(searchQuery, ignoreCase = true) ||
                    tx.upiOrHandle.contains(searchQuery, ignoreCase = true) ||
                    tx.amount.toString().contains(searchQuery)

            val matchesCategory = selectedCategoryFilter == null || tx.category == selectedCategoryFilter

            val matchesFilterMode = when (selectedFilterMode) {
                TransactionFilterMode.ALL -> true
                TransactionFilterMode.EXPENSES -> tx.type == TransactionType.DEBIT
                TransactionFilterMode.INCOME -> tx.type == TransactionType.CREDIT
                TransactionFilterMode.FLAGGED -> tx.status == TransactionStatus.BLOCKED_FRAUD ||
                        tx.status == TransactionStatus.FLAGGED_REVIEW ||
                        tx.riskScore >= 50
            }

            matchesAccount && matchesQuery && matchesCategory && matchesFilterMode
        }
    }

    val totalIncomeMonth = remember(transactions) {
        transactions.filter { it.type == TransactionType.CREDIT }.sumOf { it.amount }
    }

    val totalSpentMonth = remember(transactions) {
        transactions.filter { it.type == TransactionType.DEBIT && it.status == TransactionStatus.COMPLETED }.sumOf { it.amount }
    }

    val blockedScamsCount = remember(transactions) {
        transactions.count { it.status == TransactionStatus.BLOCKED_FRAUD }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        // --- 1. User Greeting & JWT Auth Session Bar ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.clickable { onOpenAuth() }
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(
                                if (authState.currentUser?.role == UserRole.ADMIN) AmberOrange.copy(alpha = 0.25f) else CyberCyan.copy(alpha = 0.25f)
                            )
                            .border(1.dp, if (authState.currentUser?.role == UserRole.ADMIN) AmberOrange else CyberCyan, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = authState.currentUser?.fullName?.take(1) ?: "A",
                            color = if (authState.currentUser?.role == UserRole.ADMIN) AmberOrange else CyberCyan,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                    }
                    Column {
                        Text(
                            text = "Welcome back,",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = authState.currentUser?.fullName ?: "Alex Morgan",
                                color = TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            if (authState.currentUser?.role == UserRole.ADMIN) {
                                Surface(color = AmberOrange.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                                    Text("ADMIN", color = AmberOrange, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                }
                            }
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // System-wide Dark/Light Mode Dashboard Toggle Button
                    Surface(
                        color = if (isDarkMode) NavyCard else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isDarkMode) Navy700 else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier
                            .clickable { viewModel.toggleDarkMode() }
                            .testTag("dashboard_dark_mode_toggle")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                                contentDescription = if (isDarkMode) "Switch to Light Mode" else "Switch to Dark Mode",
                                tint = GoldAccent,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = if (isDarkMode) "Light" else "Dark",
                                color = if (isDarkMode) TextWhite else MaterialTheme.colorScheme.onSurface,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        color = if (isDarkMode) NavyCard else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isDarkMode) Navy700 else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier
                            .clickable { viewModel.lockAppBiometric() }
                            .testTag("dashboard_lock_biometric_btn")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 9.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fingerprint,
                                contentDescription = "Lock SmartBank Vault with Biometrics",
                                tint = CyberCyan,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = "Lock",
                                color = if (isDarkMode) TextWhite else MaterialTheme.colorScheme.onSurface,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        color = if (isDarkMode) NavyCard else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(20.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isDarkMode) Navy700 else MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                        ),
                        modifier = Modifier.clickable { onOpenAuth() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(EmeraldSuccess))
                            Text("JWT Active", color = CyberCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // --- 1.5. 30-Day Periodic Inactivity Backup Reminder ---
        item {
            BackupReminderBanner(
                viewModel = viewModel,
                onOpenExportDialog = onOpenStatements
            )
        }

        // --- 1.6 Dashboard Mode Selector (Live Banking vs Extended Suite) ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth().testTag("dashboard_mode_tabs"),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { dashboardTab = 0 }
                        .testTag("tab_live_dashboard"),
                    color = if (dashboardTab == 0) CyberCyan.copy(alpha = 0.2f) else NavyCard,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (dashboardTab == 0) CyberCyan else Navy700)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountBalance,
                            contentDescription = null,
                            tint = if (dashboardTab == 0) CyberCyan else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Live Dashboard",
                            color = if (dashboardTab == 0) CyberCyan else TextMuted,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Surface(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { dashboardTab = 1 }
                        .testTag("tab_extended_ecosystem"),
                    color = if (dashboardTab == 1) PurpleTech.copy(alpha = 0.2f) else NavyCard,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (dashboardTab == 1) PurpleTech else Navy700)
                ) {
                    Row(
                        modifier = Modifier.padding(vertical = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = if (dashboardTab == 1) PurpleTech else TextMuted,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Extended Ecosystem",
                            color = if (dashboardTab == 1) PurpleTech else TextMuted,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        if (dashboardTab == 0) {
            // --- 2. Real-Time Account Balance Summary Card ---
            item {
                AccountBalanceSummaryCard(
                    accounts = accounts,
                    totalNetWorth = totalNetWorth,
                    selectedAccountId = selectedAccountId,
                    isPrivacyMode = isPrivacyMode,
                    onTogglePrivacyMode = { isPrivacyMode = !isPrivacyMode },
                    onSelectAccount = { accId ->
                        selectedAccountId = if (selectedAccountId == accId) null else accId
                    },
                    onQuickDeposit = { accId -> viewModel.openDepositDialog(accId) },
                    onQuickTransfer = { _ -> onNavigateTransfer() },
                    onToggleFreeze = { accId, isFrozen -> viewModel.toggleCardFreeze(accId, isFrozen) },
                    modifier = Modifier.testTag("net_worth_hero_card")
                )
            }

        // --- 3. Primary Action Hub (Deposit, Withdraw, Transfer, Investments) ---
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "CORE BANKING OPERATIONS",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    QuickActionButton(
                        icon = Icons.Default.Send,
                        label = "Transfer",
                        bgColor = CyberCyan,
                        iconTint = Navy900,
                        testTag = "quick_action_send",
                        onClick = onNavigateTransfer
                    )
                    QuickActionButton(
                        icon = Icons.Default.Add,
                        label = "Deposit",
                        bgColor = EmeraldSuccess,
                        iconTint = Navy900,
                        testTag = "quick_action_deposit",
                        onClick = onOpenDeposit
                    )
                    QuickActionButton(
                        icon = Icons.Default.Remove,
                        label = "Withdraw",
                        bgColor = AmberOrange,
                        iconTint = Navy900,
                        testTag = "quick_action_withdraw",
                        onClick = onOpenWithdraw
                    )
                    QuickActionButton(
                        icon = Icons.Default.Receipt,
                        label = "Pay Bills",
                        bgColor = GoldAccent,
                        iconTint = Navy900,
                        testTag = "quick_action_bills",
                        onClick = onNavigateBills
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    QuickActionButton(
                        icon = Icons.Default.TrendingUp,
                        label = "Investments",
                        bgColor = ElectricBlue,
                        iconTint = TextWhite,
                        testTag = "quick_action_investments",
                        onClick = onNavigateInvestments
                    )
                    QuickActionButton(
                        icon = Icons.Default.Policy,
                        label = "Insurance",
                        bgColor = PurpleTech,
                        iconTint = TextWhite,
                        testTag = "quick_action_insurance",
                        onClick = onNavigateInsurance
                    )
                    QuickActionButton(
                        icon = Icons.Default.Description,
                        label = "Apply (Forms)",
                        bgColor = CyberCyan.copy(alpha = 0.85f),
                        iconTint = Navy900,
                        testTag = "quick_action_forms",
                        onClick = onNavigateApplications
                    )
                    QuickActionButton(
                        icon = Icons.Default.AdminPanelSettings,
                        label = "Admin Ops",
                        bgColor = AmberOrange,
                        iconTint = Navy900,
                        testTag = "quick_action_admin",
                        onClick = onNavigateAdmin
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    QuickActionButton(
                        icon = Icons.Default.NotificationsActive,
                        label = "Thresholds",
                        bgColor = AmberOrange,
                        iconTint = Navy900,
                        testTag = "quick_action_thresholds",
                        onClick = { showThresholdDialog = true }
                    )
                    QuickActionButton(
                        icon = Icons.Default.Description,
                        label = "Export CSV",
                        bgColor = CyberCyan,
                        iconTint = Navy900,
                        testTag = "quick_action_export_csv",
                        onClick = onOpenStatements
                    )
                    QuickActionButton(
                        icon = Icons.Default.AutoAwesome,
                        label = "AI Advisor",
                        bgColor = ElectricBlue,
                        iconTint = TextWhite,
                        testTag = "quick_action_advisor",
                        onClick = onNavigateAiAdvisor
                    )
                    QuickActionButton(
                        icon = Icons.Default.Security,
                        label = "Security",
                        bgColor = PurpleTech,
                        iconTint = TextWhite,
                        testTag = "quick_action_security",
                        onClick = onNavigateSecurity
                    )
                }
            }
        }

            // --- 4. Recent Bank Transactions Header & Filter Suite ---
            item {
                RecentTransactionsSectionHeader(
                    totalCount = filteredTransactions.size,
                    searchQuery = searchQuery,
                    onSearchQueryChange = { searchQuery = it },
                    selectedFilterMode = selectedFilterMode,
                    onFilterModeSelected = { selectedFilterMode = it },
                    selectedCategory = selectedCategoryFilter,
                    onCategorySelected = { selectedCategoryFilter = it },
                    onExportStatements = onOpenStatements
                )
            }

            // --- 5. Recent Bank Transactions List ---
            if (filteredTransactions.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("empty_transactions_card"),
                        colors = CardDefaults.cardColors(containerColor = NavyCard),
                        shape = RoundedCornerShape(16.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(
                                text = "No Recent Transactions",
                                color = TextWhite,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = if (searchQuery.isNotBlank() || selectedCategoryFilter != null || selectedFilterMode != TransactionFilterMode.ALL)
                                    "No transactions match your current search or category filters."
                                else
                                    "No transaction activity found.",
                                color = TextMuted,
                                fontSize = 12.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            if (searchQuery.isNotBlank() || selectedCategoryFilter != null || selectedFilterMode != TransactionFilterMode.ALL) {
                                androidx.compose.material3.TextButton(
                                    onClick = {
                                        searchQuery = ""
                                        selectedCategoryFilter = null
                                        selectedFilterMode = TransactionFilterMode.ALL
                                        selectedAccountId = null
                                    }
                                ) {
                                    Text("Reset Filters", color = CyberCyan, fontSize = 12.sp)
                                }
                            }
                        }
                    }
                }
            } else {
                items(filteredTransactions.take(15), key = { it.id }) { tx ->
                    RecentTransactionCardItem(
                        tx = tx,
                        account = accounts.find { it.id == tx.accountId },
                        onClick = { selectedTxForDetails = tx },
                        onFlagFraud = { viewModel.flagTransactionAsFraud(tx.id) }
                    )
                }
            }

            // --- 6. Month-Over-Month Spending Trends Line Chart ---
            item {
                MonthOverMonthSpendingLineChart(
                    viewModel = viewModel,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            // --- 7. Button to Explore Extended Ecosystem ---
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { dashboardTab = 1 }
                        .testTag("btn_switch_to_extended_ecosystem"),
                    colors = CardDefaults.cardColors(containerColor = NavyCard),
                    shape = RoundedCornerShape(16.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, PurpleTech.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(PurpleTech.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = PurpleTech, modifier = Modifier.size(20.dp))
                            }
                            Column {
                                Text("Explore Extended Financial OS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Forex, Voice AI, Threat Radar & 15 Portal Pages", color = TextMuted, fontSize = 11.sp)
                            }
                        }
                        Icon(Icons.Default.ArrowForward, contentDescription = null, tint = PurpleTech, modifier = Modifier.size(18.dp))
                    }
                }
            }
        } else {
            // dashboardTab == 1: Extended Ecosystem

        // --- 3.2. Voice Financial Intelligence (Speech-to-Text Room Query Engine) ---
        item {
            VoiceSpendingQueryWidget(
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // --- 3.5. Live Foreign Exchange Rates (Retrofit) & Currency Conversion ---
        item {
            CurrencyExchangeWidget(
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // --- 4. Virtual Cards Carousel ---
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "YOUR DIGITAL VAULTS & ACCOUNTS",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "${accounts.size} Active",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (accounts.isNotEmpty()) {
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(horizontal = 2.dp)
                    ) {
                        items(accounts) { account ->
                            VirtualCardItem(
                                account = account,
                                onFreezeToggle = {
                                    viewModel.toggleCardFreeze(account.id, account.isFrozen)
                                }
                            )
                        }
                    }
                }
            }
        }

        // --- 5. AI Threat Radar Widget ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateSecurity() }
                    .testTag("ai_threat_radar_card"),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.25f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(CyberCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "AI Guard",
                            tint = CyberCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("AI Threat Radar: Active", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Box(modifier = Modifier.size(7.dp).clip(CircleShape).background(EmeraldSuccess))
                        }
                        Text(
                            text = "Real-time UPI deep analysis, malware link inspector & zero-trust 2FA active.",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Inspect",
                        tint = CyberCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // --- 5.5 Global Economic Predictor Banner (10 Macro Signals & 4 AI Engines) ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateGlobalEconomy() }
                    .testTag("home_global_economic_predictor_banner"),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(GoldAccent.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = "Global Macro",
                            tint = GoldAccent,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Global Economic Predictor", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 6.dp)
                        }
                        Text(
                            text = "Continuous 10-signal macro analysis (Wars, Inflation, Oil, Climate) & 4 AI prediction engines.",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Open Global Predictor",
                        tint = GoldAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // --- 5.6 World Digital Financial Identity Banner (9 Linked Sovereign Modules) ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateWorldIdentity() }
                    .testTag("home_world_digital_identity_banner"),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(CyberCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "World Digital ID",
                            tint = CyberCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("World Digital Financial Identity", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            PulsingStatusBadge(pulseColor = CyberCyan, badgeSize = 6.dp)
                        }
                        Text(
                            text = "One Sovereign ID: Banking, Education, Labor, Tax, Health, Gov IDs, Signatures, Insurance, Assets.",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Open World ID",
                        tint = CyberCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // --- 5.7 AI Personal Life Planner Banner (7 Lifecycle Modules) ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateLifePlanner() }
                    .testTag("home_ai_personal_life_planner_banner"),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PurpleTech.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PurpleTech.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Psychology,
                            contentDescription = "AI Life Planner",
                            tint = PurpleTech,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("AI Personal Life Planner", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            PulsingStatusBadge(pulseColor = PurpleTech, badgeSize = 6.dp)
                        }
                        Text(
                            text = "Beyond finances: Career, Education, Healthcare, Marriage, Kids, FIRE & Estate planning.",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Open Life Planner",
                        tint = PurpleTech,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // --- 5.8 Universal Wealth Optimization Engine Banner (7 Autopilot Modules) ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateWealthEngine() }
                    .testTag("home_universal_wealth_engine_banner"),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(GoldAccent.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = "Universal Wealth Engine",
                            tint = GoldAccent,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Universal Wealth Engine", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 6.dp)
                        }
                        Text(
                            text = "Auto-optimizes 7 wealth vectors: Investments, Loans, Insurance, Bills, Savings, Taxes & Cashback.",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Open Wealth Engine",
                        tint = GoldAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // --- 5.9 Global Crisis Intelligence Banner (6 Crisis Defense Modules) ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateCrisisIntelligence() }
                    .testTag("home_global_crisis_intelligence_banner"),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonDanger.copy(alpha = 0.6f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(CrimsonDanger.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CrisisAlert,
                            contentDescription = "Crisis Intelligence",
                            tint = CrimsonDanger,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("Global Crisis Intelligence", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            PulsingStatusBadge(pulseColor = CrimsonDanger, badgeSize = 6.dp)
                        }
                        Text(
                            text = "Disaster defense: Freezes fraud, prioritzes payments, sends relief, predicts impact & protects SMBs.",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Open Crisis Intelligence",
                        tint = CrimsonDanger,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // --- 5.10 Master Future Financial OS (2050–2077) Banner (All 20 Next-Gen Pillars) ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateFutureOs() }
                    .testTag("home_future_financial_os_banner"),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.8f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(GoldAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.RocketLaunch,
                            contentDescription = "Financial OS 2077",
                            tint = GoldAccent,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("FINANCIAL OS (2050–2077)", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 6.dp)
                        }
                        Text(
                            text = "20 Next-Gen Pillars: Quantum Banking, Climate AI, Smart City, Planetary Network & Digital Twins.",
                            color = GoldAccent,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Open Financial OS",
                        tint = GoldAccent,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // --- 5.11 Public Portal & Global Website Directory Banner (15 Dedicated Pages) ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateLanding() }
                    .testTag("home_public_portal_banner"),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.8f))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(CyberCyan.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Public,
                            contentDescription = "Public Portal",
                            tint = CyberCyan,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text("PUBLIC PORTAL & SIDEBAR", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            PulsingStatusBadge(pulseColor = CyberCyan, badgeSize = 6.dp)
                        }
                        Text(
                            text = "Explore 15 Public Pages: Landing, Vision, Solutions, Pricing, Blog, Research, Careers & FAQs.",
                            color = CyberCyan,
                            fontSize = 11.sp
                        )
                    }

                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Open Public Portal",
                        tint = CyberCyan,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }

        // --- 5.11 Month-Over-Month Spending Trends Line Chart ---
        item {
            MonthOverMonthSpendingLineChart(
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // --- 5.12 Recharts Monthly Spending Trends Dashboard ---
        item {
            com.example.ui.components.RechartsSpendingTrendsChart(
                viewModel = viewModel,
                modifier = Modifier.fillMaxWidth()
            )
        }

        // --- 6. Back to Live Dashboard Button ---
        item {
            Button(
                onClick = { dashboardTab = 0 },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_back_to_live_dashboard"),
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Back to Live Dashboard", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

    if (showThresholdDialog) {
        CategorySpendingThresholdDialog(
            viewModel = viewModel,
            onDismiss = { showThresholdDialog = false }
        )
    }

    if (selectedTxForDetails != null) {
        val tx = selectedTxForDetails!!
        TransactionDetailDialog(
            tx = tx,
            account = accounts.find { it.id == tx.accountId },
            onDismiss = { selectedTxForDetails = null },
            onFlagFraud = {
                viewModel.flagTransactionAsFraud(tx.id)
                selectedTxForDetails = null
            }
        )
    }
}

@Composable
private fun QuickActionButton(
    icon: ImageVector,
    label: String,
    bgColor: androidx.compose.ui.graphics.Color,
    iconTint: androidx.compose.ui.graphics.Color,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier.clickable { onClick() }.testTag(testTag)
    ) {
        Box(
            modifier = Modifier
                .size(54.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(bgColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = iconTint,
                modifier = Modifier.size(24.dp)
            )
        }
        Text(
            text = label,
            color = TextWhite,
            fontSize = 11.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
