package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AccountEntity
import com.example.data.model.AccountType
import com.example.data.model.RiskLevel
import com.example.data.model.TransactionCategory
import com.example.data.model.TransactionEntity
import com.example.data.model.TransactionStatus
import com.example.data.model.TransactionType
import com.example.data.model.UserRole
import com.example.ui.components.PulsingStatusBadge
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Filter mode for recent bank transactions
 */
enum class TransactionFilterMode(val label: String) {
    ALL("All"),
    EXPENSES("Expenses"),
    INCOME("Income"),
    FLAGGED("Flagged / Risk")
}

/**
 * Main Dashboard UI Screen
 * Displays real-time account balance summary card and recent bank transactions
 */
@Composable
fun MainDashboardScreen(
    viewModel: BankViewModel,
    onNavigateTransfer: () -> Unit,
    onNavigateSecurity: () -> Unit,
    onNavigateBills: () -> Unit,
    onOpenDeposit: () -> Unit,
    onOpenWithdraw: () -> Unit,
    onOpenStatements: () -> Unit,
    onOpenAuth: () -> Unit,
    onNavigateMoreModules: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val totalNetWorth by viewModel.totalNetWorth.collectAsStateWithLifecycle()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

    var selectedAccountId by remember { mutableStateOf<Long?>(null) }
    var isPrivacyMode by remember { mutableStateOf(false) }
    var selectedTxForDetails by remember { mutableStateOf<TransactionEntity?>(null) }

    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf<TransactionCategory?>(null) }
    var selectedFilterMode by remember { mutableStateOf(TransactionFilterMode.ALL) }

    // Filter transactions based on search query, category, and filter mode
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

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 88.dp)
    ) {
        // --- 1. Top Greeting & System Session Header ---
        item {
            DashboardHeaderBar(
                userName = authState.currentUser?.fullName ?: "Alex Morgan",
                userRole = authState.currentUser?.role ?: UserRole.USER,
                isDarkMode = isDarkMode,
                onToggleDarkMode = { viewModel.toggleDarkMode() },
                onLockBiometric = { viewModel.lockAppBiometric() },
                onOpenAuth = onOpenAuth
            )
        }

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
                onQuickTransfer = { onNavigateTransfer() },
                onToggleFreeze = { accId, currentFrozen ->
                    viewModel.toggleCardFreeze(accId, currentFrozen)
                }
            )
        }

        // --- 3. Quick Action Hub ---
        item {
            DashboardQuickActionsRow(
                onTransfer = onNavigateTransfer,
                onDeposit = onOpenDeposit,
                onWithdraw = onOpenWithdraw,
                onBills = onNavigateBills,
                onExportStatements = onOpenStatements
            )
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
                    border = BorderStroke(1.dp, Navy700)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(CircleShape)
                                .background(CyberCyan.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Receipt,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(26.dp)
                            )
                        }
                        Text(
                            text = "No Transactions Found",
                            color = TextWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = if (searchQuery.isNotBlank() || selectedCategoryFilter != null || selectedFilterMode != TransactionFilterMode.ALL)
                                "No transactions match your current search or category filters."
                            else
                                "Your bank account has no recorded transactions yet.",
                            color = TextMuted,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                        if (searchQuery.isNotBlank() || selectedCategoryFilter != null || selectedFilterMode != TransactionFilterMode.ALL) {
                            OutlinedButton(
                                onClick = {
                                    searchQuery = ""
                                    selectedCategoryFilter = null
                                    selectedFilterMode = TransactionFilterMode.ALL
                                    selectedAccountId = null
                                },
                                shape = RoundedCornerShape(10.dp),
                                border = BorderStroke(1.dp, CyberCyan)
                            ) {
                                Text("Reset Filters", color = CyberCyan, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        } else {
            items(
                items = filteredTransactions.take(12),
                key = { it.id }
            ) { tx ->
                RecentTransactionCardItem(
                    tx = tx,
                    account = accounts.find { it.id == tx.accountId },
                    onClick = { selectedTxForDetails = tx },
                    onFlagFraud = { viewModel.flagTransactionAsFraud(tx.id) }
                )
            }
        }

        // --- 6. Quick Access to Extended Financial Suite ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onNavigateMoreModules() }
                    .testTag("btn_more_banking_modules"),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Navy700)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(CyberCyan.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Text("More Banking & Financial Modules", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Investments, Insurance, AI Underwriter, Loans & Settings", color = TextMuted, fontSize = 11.sp)
                        }
                    }
                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
                }
            }
        }
    }

    // --- Transaction Detail Bottom Dialog ---
    if (selectedTxForDetails != null) {
        val tx = selectedTxForDetails!!
        val matchingAccount = accounts.find { it.id == tx.accountId }

        TransactionDetailDialog(
            tx = tx,
            account = matchingAccount,
            onDismiss = { selectedTxForDetails = null },
            onFlagFraud = {
                viewModel.flagTransactionAsFraud(tx.id)
                selectedTxForDetails = null
            }
        )
    }
}

/**
 * Top Header Bar for Main Dashboard
 */
@Composable
private fun DashboardHeaderBar(
    userName: String,
    userRole: UserRole,
    isDarkMode: Boolean,
    onToggleDarkMode: () -> Unit,
    onLockBiometric: () -> Unit,
    onOpenAuth: () -> Unit
) {
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
                        if (userRole == UserRole.ADMIN) AmberOrange.copy(alpha = 0.25f) else CyberCyan.copy(alpha = 0.25f)
                    )
                    .border(1.dp, if (userRole == UserRole.ADMIN) AmberOrange else CyberCyan, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = userName.take(1).uppercase(),
                    color = if (userRole == UserRole.ADMIN) AmberOrange else CyberCyan,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            }
            Column {
                Text("SmartBank Sovereign", color = TextMuted, fontSize = 11.sp)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(userName, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    if (userRole == UserRole.ADMIN) {
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
            // Theme toggle button
            Surface(
                color = NavyCard,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Navy700),
                modifier = Modifier
                    .clickable { onToggleDarkMode() }
                    .testTag("main_dashboard_dark_mode_toggle")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "Toggle Theme",
                        tint = GoldAccent,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(if (isDarkMode) "Light" else "Dark", color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }

            // Biometric lock button
            Surface(
                color = NavyCard,
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, Navy700),
                modifier = Modifier
                    .clickable { onLockBiometric() }
                    .testTag("main_dashboard_lock_btn")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Fingerprint, contentDescription = "Lock App", tint = CyberCyan, modifier = Modifier.size(13.dp))
                    Text("Lock", color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

/**
 * Real-Time Account Balance Summary Card
 * Consolidates total net worth, real-time balances, cash flow metrics,
 * and multi-account selection carousel.
 */
@Composable
fun AccountBalanceSummaryCard(
    accounts: List<AccountEntity>,
    totalNetWorth: Double,
    selectedAccountId: Long?,
    isPrivacyMode: Boolean,
    onTogglePrivacyMode: () -> Unit,
    onSelectAccount: (Long) -> Unit,
    onQuickDeposit: (Long) -> Unit,
    onQuickTransfer: (Long) -> Unit,
    onToggleFreeze: (Long, Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedBalance by animateFloatAsState(
        targetValue = totalNetWorth.toFloat(),
        animationSpec = tween(durationMillis = 800),
        label = "animatedBalance"
    )

    val activeAccounts = remember(accounts) { accounts.count { !it.isFrozen } }
    val totalAvailableLimit = remember(accounts) { accounts.sumOf { it.dailyLimit - it.spentToday } }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("real_time_account_balance_summary_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        border = BorderStroke(1.dp, Brush.horizontalGradient(listOf(CyberCyan.copy(alpha = 0.6f), ElectricBlue.copy(alpha = 0.3f))))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.verticalGradient(
                        listOf(NavyCardLight, NavyCard, Navy900.copy(alpha = 0.95f))
                    )
                )
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header: Live Indicator & Privacy Eye
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 8.dp)
                    Text(
                        text = "REAL-TIME ACCOUNT BALANCE",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.3.sp
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Surface(
                        color = EmeraldSuccess.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "LIVE SYNC",
                            color = EmeraldSuccess,
                            fontSize = 9.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                        )
                    }

                    IconButton(
                        onClick = onTogglePrivacyMode,
                        modifier = Modifier.size(28.dp).testTag("btn_toggle_balance_privacy")
                    ) {
                        Icon(
                            imageVector = if (isPrivacyMode) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = if (isPrivacyMode) "Show Balance" else "Hide Balance",
                            tint = CyberCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Big Balance Display
            Column {
                if (isPrivacyMode) {
                    Text(
                        text = "$ ••••••••",
                        color = TextWhite,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                } else {
                    Text(
                        text = "$${"%,.2f".format(animatedBalance.toDouble())}",
                        color = TextWhite,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Text(
                    text = "Consolidated across ${accounts.size} bank accounts ($activeAccounts active)",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            // Inflow / Outflow Telemetry Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Navy900.copy(alpha = 0.7f))
                    .padding(vertical = 10.dp, horizontal = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier.size(26.dp).clip(CircleShape).background(EmeraldSuccess.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.ArrowDownward, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(15.dp))
                    }
                    Column {
                        Text("Available Daily Limit", color = TextMuted, fontSize = 10.sp)
                        Text(
                            text = if (isPrivacyMode) "$ ••••" else "$${"%,.2f".format(totalAvailableLimit)}",
                            color = EmeraldSuccess,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Navy700))

                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Box(
                        modifier = Modifier.size(26.dp).clip(CircleShape).background(ElectricBlue.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.CreditCard, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(15.dp))
                    }
                    Column {
                        Text("Primary Vault", color = TextMuted, fontSize = 10.sp)
                        Text(
                            text = accounts.firstOrNull()?.name ?: "Checking Account",
                            color = TextWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            HorizontalDivider(color = Navy700.copy(alpha = 0.7f))

            // Sub-Accounts Carousel Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "ACCOUNTS & CARDS",
                    color = TextMuted,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                if (selectedAccountId != null) {
                    Text(
                        text = "Clear Selection",
                        color = CyberCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onSelectAccount(selectedAccountId) }
                    )
                }
            }

            // Sub-Accounts Horizontal Carousel
            if (accounts.isNotEmpty()) {
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    items(accounts, key = { it.id }) { account ->
                        val isSelected = selectedAccountId == account.id
                        AccountMiniCard(
                            account = account,
                            isSelected = isSelected,
                            isPrivacyMode = isPrivacyMode,
                            onClick = { onSelectAccount(account.id) },
                            onDeposit = { onQuickDeposit(account.id) },
                            onToggleFreeze = { onToggleFreeze(account.id, account.isFrozen) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Individual Account Mini Card inside the balance carousel
 */
@Composable
private fun AccountMiniCard(
    account: AccountEntity,
    isSelected: Boolean,
    isPrivacyMode: Boolean,
    onClick: () -> Unit,
    onDeposit: () -> Unit,
    onToggleFreeze: () -> Unit
) {
    val borderColor = if (isSelected) CyberCyan else if (account.isFrozen) CrimsonDanger.copy(alpha = 0.5f) else Navy700
    val cardBackground = if (isSelected) NavyCardLight else Navy800

    Card(
        modifier = Modifier
            .width(220.dp)
            .clickable { onClick() }
            .testTag("account_card_${account.id}"),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        border = BorderStroke(if (isSelected) 1.5.dp else 1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Icon(
                        imageVector = when (account.type) {
                            AccountType.CHECKING -> Icons.Default.AccountBalance
                            AccountType.SAVINGS -> Icons.Default.AccountBalanceWallet
                            AccountType.INVESTMENT -> Icons.Default.AutoAwesome
                            AccountType.CREDIT_CARD -> Icons.Default.CreditCard
                        },
                        contentDescription = null,
                        tint = when (account.type) {
                            AccountType.CHECKING -> CyberCyan
                            AccountType.SAVINGS -> EmeraldSuccess
                            AccountType.INVESTMENT -> PurpleTech
                            AccountType.CREDIT_CARD -> GoldAccent
                        },
                        modifier = Modifier.size(16.dp)
                    )
                    Text(
                        text = account.type.name,
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Frozen badge or NFC indicator
                if (account.isFrozen) {
                    Surface(color = CrimsonDanger.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                        Text("FROZEN", color = CrimsonDanger, fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                    }
                } else {
                    Icon(Icons.Default.Nfc, contentDescription = "Contactless", tint = CyberCyan.copy(alpha = 0.7f), modifier = Modifier.size(14.dp))
                }
            }

            Column {
                Text(
                    text = account.name,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "•••• ${account.accountNumber.takeLast(4)}",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            // Balance
            Text(
                text = if (isPrivacyMode) "$ ••••" else "$${"%,.2f".format(account.balance)}",
                color = if (account.isFrozen) TextMuted else TextWhite,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )

            // Daily Limit bar
            val spentRatio = (account.spentToday / account.dailyLimit).coerceIn(0.0, 1.0).toFloat()
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                LinearProgressIndicator(
                    progress = { spentRatio },
                    modifier = Modifier.fillMaxWidth().height(4.dp).clip(CircleShape),
                    color = if (spentRatio > 0.8f) CrimsonDanger else CyberCyan,
                    trackColor = Navy900
                )
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Daily: $${"%,.0f".format(account.spentToday)}", color = TextMuted, fontSize = 9.sp)
                    Text("Limit: $${"%,.0f".format(account.dailyLimit)}", color = TextMuted, fontSize = 9.sp)
                }
            }

            // Quick mini card actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onDeposit,
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                    modifier = Modifier.height(28.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(3.dp))
                    Text("Deposit", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                IconButton(
                    onClick = onToggleFreeze,
                    modifier = Modifier.size(26.dp)
                ) {
                    Icon(
                        imageVector = if (account.isFrozen) Icons.Default.Lock else Icons.Default.LockOpen,
                        contentDescription = if (account.isFrozen) "Unfreeze" else "Freeze",
                        tint = if (account.isFrozen) CrimsonDanger else TextMuted,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}

/**
 * Dashboard Quick Actions Row (Transfer, Deposit, Withdraw, Pay Bills, Export)
 */
@Composable
private fun DashboardQuickActionsRow(
    onTransfer: () -> Unit,
    onDeposit: () -> Unit,
    onWithdraw: () -> Unit,
    onBills: () -> Unit,
    onExportStatements: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("dashboard_quick_actions_row"),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, Navy700)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            QuickActionButtonItem(
                icon = Icons.Default.Send,
                label = "Transfer",
                tintColor = CyberCyan,
                testTag = "btn_quick_transfer",
                onClick = onTransfer
            )
            QuickActionButtonItem(
                icon = Icons.Default.Add,
                label = "Deposit",
                tintColor = EmeraldSuccess,
                testTag = "btn_quick_deposit",
                onClick = onDeposit
            )
            QuickActionButtonItem(
                icon = Icons.Default.Remove,
                label = "Withdraw",
                tintColor = AmberOrange,
                testTag = "btn_quick_withdraw",
                onClick = onWithdraw
            )
            QuickActionButtonItem(
                icon = Icons.Default.Receipt,
                label = "Bills",
                tintColor = GoldAccent,
                testTag = "btn_quick_bills",
                onClick = onBills
            )
            QuickActionButtonItem(
                icon = Icons.Default.Description,
                label = "Statements",
                tintColor = PurpleTech,
                testTag = "btn_quick_statements",
                onClick = onExportStatements
            )
        }
    }
}

@Composable
private fun QuickActionButtonItem(
    icon: ImageVector,
    label: String,
    tintColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
            .padding(horizontal = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .clip(CircleShape)
                .background(tintColor.copy(alpha = 0.15f))
                .border(1.dp, tintColor.copy(alpha = 0.35f), CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tintColor,
                modifier = Modifier.size(20.dp)
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

/**
 * Header and Filter Suite for Recent Bank Transactions
 */
@Composable
fun RecentTransactionsSectionHeader(
    totalCount: Int,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedFilterMode: TransactionFilterMode,
    onFilterModeSelected: (TransactionFilterMode) -> Unit,
    selectedCategory: TransactionCategory?,
    onCategorySelected: (TransactionCategory?) -> Unit,
    onExportStatements: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "RECENT BANK TRANSACTIONS",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.3.sp
                )
                Surface(
                    color = CyberCyan.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = "$totalCount",
                        color = CyberCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }

            Text(
                text = "Export Statement",
                color = CyberCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clickable { onExportStatements() }
                    .testTag("btn_export_statements_link")
            )
        }

        // Live Search Input Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier
                .fillMaxWidth()
                .testTag("tx_search_field"),
            placeholder = { Text("Search by merchant, title, or amount...", color = TextMuted, fontSize = 12.sp) },
            leadingIcon = {
                Icon(Icons.Default.Search, contentDescription = "Search", tint = CyberCyan, modifier = Modifier.size(18.dp))
            },
            trailingIcon = {
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = { onSearchQueryChange("") }) {
                        Icon(Icons.Default.Clear, contentDescription = "Clear", tint = TextMuted, modifier = Modifier.size(16.dp))
                    }
                }
            },
            singleLine = true,
            shape = RoundedCornerShape(14.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = NavyCard,
                unfocusedContainerColor = NavyCard,
                focusedBorderColor = CyberCyan,
                unfocusedBorderColor = Navy700,
                focusedTextColor = TextWhite,
                unfocusedTextColor = TextWhite
            )
        )

        // Type Filter Chips: All, Expenses, Income, Flagged
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            TransactionFilterMode.values().forEach { mode ->
                val isSelected = selectedFilterMode == mode
                FilterChip(
                    selected = isSelected,
                    onClick = { onFilterModeSelected(mode) },
                    label = {
                        Text(
                            text = mode.label,
                            fontSize = 11.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = when (mode) {
                            TransactionFilterMode.EXPENSES -> CrimsonDanger.copy(alpha = 0.25f)
                            TransactionFilterMode.INCOME -> EmeraldSuccess.copy(alpha = 0.25f)
                            TransactionFilterMode.FLAGGED -> AmberOrange.copy(alpha = 0.25f)
                            TransactionFilterMode.ALL -> CyberCyan.copy(alpha = 0.25f)
                        },
                        selectedLabelColor = when (mode) {
                            TransactionFilterMode.EXPENSES -> CrimsonDanger
                            TransactionFilterMode.INCOME -> EmeraldSuccess
                            TransactionFilterMode.FLAGGED -> AmberOrange
                            TransactionFilterMode.ALL -> CyberCyan
                        },
                        containerColor = NavyCard,
                        labelColor = TextMuted
                    ),
                    border = BorderStroke(
                        1.dp,
                        if (isSelected) {
                            when (mode) {
                                TransactionFilterMode.EXPENSES -> CrimsonDanger
                                TransactionFilterMode.INCOME -> EmeraldSuccess
                                TransactionFilterMode.FLAGGED -> AmberOrange
                                TransactionFilterMode.ALL -> CyberCyan
                            }
                        } else Navy700
                    ),
                    shape = RoundedCornerShape(20.dp)
                )
            }

            // Category Filter Chips: Food, Shopping, Salary, Bills, Transfer
            listOf(
                TransactionCategory.SALARY,
                TransactionCategory.FOOD,
                TransactionCategory.SHOPPING,
                TransactionCategory.BILLS,
                TransactionCategory.TRANSFER,
                TransactionCategory.INVESTMENT
            ).forEach { cat ->
                val isSelected = selectedCategory == cat
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        onCategorySelected(if (isSelected) null else cat)
                    },
                    label = { Text(cat.name.lowercase().capitalize(Locale.US), fontSize = 11.sp) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricBlue.copy(alpha = 0.25f),
                        selectedLabelColor = CyberCyan,
                        containerColor = NavyCard,
                        labelColor = TextMuted
                    ),
                    border = BorderStroke(1.dp, if (isSelected) CyberCyan else Navy700),
                    shape = RoundedCornerShape(20.dp)
                )
            }
        }
    }
}

/**
 * Individual Transaction Item in the Recent Bank Transactions list
 */
@Composable
fun RecentTransactionCardItem(
    tx: TransactionEntity,
    account: AccountEntity?,
    onClick: () -> Unit,
    onFlagFraud: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDebit = tx.type == TransactionType.DEBIT
    val amountColor = if (isDebit) TextWhite else EmeraldSuccess
    val prefix = if (isDebit) "-$" else "+$"

    val formattedDate = remember(tx.timestamp) {
        SimpleDateFormat("MMM dd, hh:mm a", Locale.US).format(Date(tx.timestamp))
    }

    val isBlocked = tx.status == TransactionStatus.BLOCKED_FRAUD
    val isReview = tx.status == TransactionStatus.FLAGGED_REVIEW

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("recent_tx_item_${tx.id}"),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isBlocked -> Color(0xFF450A0A).copy(alpha = 0.6f)
                isReview -> AmberOrange.copy(alpha = 0.12f)
                else -> NavyCard
            }
        ),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(
            1.dp,
            when {
                isBlocked -> CrimsonDanger.copy(alpha = 0.6f)
                isReview -> AmberOrange.copy(alpha = 0.5f)
                else -> Navy700
            }
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                // Category Icon with Status Tint
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(
                            when {
                                isBlocked -> CrimsonDanger.copy(alpha = 0.2f)
                                isReview -> AmberOrange.copy(alpha = 0.2f)
                                tx.category == TransactionCategory.SALARY -> EmeraldSuccess.copy(alpha = 0.2f)
                                tx.category == TransactionCategory.BILLS -> GoldAccent.copy(alpha = 0.2f)
                                tx.category == TransactionCategory.FOOD -> AmberOrange.copy(alpha = 0.2f)
                                tx.category == TransactionCategory.SHOPPING -> PurpleTech.copy(alpha = 0.2f)
                                else -> CyberCyan.copy(alpha = 0.15f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when {
                            isBlocked -> Icons.Default.Security
                            isReview -> Icons.Default.Warning
                            tx.category == TransactionCategory.SALARY -> Icons.Default.CheckCircle
                            tx.category == TransactionCategory.BILLS -> Icons.Default.Receipt
                            tx.category == TransactionCategory.SHOPPING -> Icons.Default.CreditCard
                            tx.category == TransactionCategory.TRANSFER -> Icons.Default.Send
                            else -> Icons.Default.CreditCard
                        },
                        contentDescription = tx.category.name,
                        tint = when {
                            isBlocked -> CrimsonDanger
                            isReview -> AmberOrange
                            tx.category == TransactionCategory.SALARY -> EmeraldSuccess
                            tx.category == TransactionCategory.BILLS -> GoldAccent
                            tx.category == TransactionCategory.SHOPPING -> PurpleTech
                            else -> CyberCyan
                        },
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Transaction Titles and Metadata
                Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                    Text(
                        text = tx.title,
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = tx.recipient.ifBlank { tx.category.name },
                            color = TextMuted,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (account != null) {
                            Text("•", color = TextMuted, fontSize = 9.sp)
                            Text(
                                text = account.name,
                                color = CyberCyan.copy(alpha = 0.8f),
                                fontSize = 10.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                    Text(
                        text = formattedDate,
                        color = TextMuted.copy(alpha = 0.8f),
                        fontSize = 10.sp
                    )
                }
            }

            // Amount and Status Badge
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "$prefix${"%,.2f".format(tx.amount)}",
                    color = amountColor,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 15.sp
                )

                when {
                    isBlocked -> {
                        Surface(
                            color = CrimsonDanger.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                "BLOCKED",
                                color = CrimsonDanger,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                    isReview -> {
                        Surface(
                            color = AmberOrange.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                "REVIEW",
                                color = AmberOrange,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                    tx.status == TransactionStatus.PENDING -> {
                        Surface(
                            color = GoldAccent.copy(alpha = 0.25f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                "PENDING",
                                color = GoldAccent,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                    else -> {
                        Surface(
                            color = EmeraldSuccess.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                "COMPLETED",
                                color = EmeraldSuccess,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Detailed Transaction Audit and Receipt Dialog
 */
@Composable
fun TransactionDetailDialog(
    tx: TransactionEntity,
    account: AccountEntity?,
    onDismiss: () -> Unit,
    onFlagFraud: () -> Unit
) {
    val isDebit = tx.type == TransactionType.DEBIT
    val formattedDate = remember(tx.timestamp) {
        SimpleDateFormat("EEEE, MMMM dd, yyyy 'at' hh:mm:ss a", Locale.US).format(Date(tx.timestamp))
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.92f)
                .clip(RoundedCornerShape(22.dp))
                .testTag("transaction_detail_dialog"),
            color = Navy900,
            border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Receipt, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
                        Text("Transaction Audit Receipt", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextWhite)
                    }
                }

                HorizontalDivider(color = Navy700)

                // Big Amount
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = if (isDebit) "-$${"%,.2f".format(tx.amount)}" else "+$${"%,.2f".format(tx.amount)}",
                        color = if (isDebit) TextWhite else EmeraldSuccess,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = tx.title,
                        color = TextMuted,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Details Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = NavyCard),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, Navy700)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        AuditRow("Transaction ID", "TXN-${tx.id.toString().padStart(8, '0')}")
                        AuditRow("Recipient / Merchant", tx.recipient.ifBlank { "Standard Bank Transfer" })
                        if (tx.upiOrHandle.isNotBlank()) {
                            AuditRow("UPI / Handle", tx.upiOrHandle)
                        }
                        AuditRow("Account", account?.name ?: "Primary Checking")
                        AuditRow("Category", tx.category.name)
                        AuditRow("Date & Time", formattedDate)
                        AuditRow("Execution Status", tx.status.name)
                        AuditRow("AI Risk Score", "${tx.riskScore}/100 (${tx.riskLevel.name})")
                        if (tx.riskReason.isNotBlank()) {
                            AuditRow("Security Audit", tx.riskReason)
                        }
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (tx.status != TransactionStatus.BLOCKED_FRAUD) {
                        OutlinedButton(
                            onClick = onFlagFraud,
                            modifier = Modifier.weight(1f).testTag("btn_dialog_flag_fraud"),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonDanger),
                            border = BorderStroke(1.dp, CrimsonDanger),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(Icons.Default.Shield, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Flag Fraud", fontSize = 12.sp)
                        }
                    }

                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).testTag("btn_dialog_close"),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Dismiss", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun AuditRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = TextMuted, fontSize = 11.sp)
        Text(
            text = value,
            color = TextWhite,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.End,
            modifier = Modifier.fillMaxWidth(0.65f),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}
