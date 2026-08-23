package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AccountType
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
import com.example.ui.theme.PurpleTech
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BankViewModel

enum class BankingModulePageType(
    val title: String,
    val shortLabel: String,
    val icon: ImageVector,
    val color: Color,
    val routeKey: String
) {
    ACCOUNT_SUMMARY("Account Summary", "Accounts", Icons.Default.AccountBalance, CyberCyan, "banking_summary"),
    BALANCE_OVERVIEW("Balance Overview & Multi-Currency", "Balances", Icons.Default.AccountBalanceWallet, EmeraldSuccess, "banking_balances"),
    DEPOSIT("Deposit & Instant Inflow", "Deposit", Icons.Default.ArrowDownward, CyberCyan, "banking_deposit"),
    WITHDRAWAL("Withdrawal & Liquidity Outflow", "Withdraw", Icons.Default.ArrowUpward, AmberOrange, "banking_withdrawal"),
    MONEY_TRANSFER("Instant Domestic Transfer", "Transfer", Icons.Default.Send, ElectricBlue, "banking_transfer"),
    INTERNATIONAL_TRANSFER("Cross-Border SWIFT & FX", "Global FX", Icons.Default.Public, PurpleTech, "banking_international"),
    SCHEDULED_PAYMENTS("Scheduled & Recurring Payments", "Scheduled", Icons.Default.Schedule, GoldAccent, "banking_scheduled"),
    STANDING_INSTRUCTIONS("Standing Smart Treasury Rules", "Rules", Icons.Default.AutoAwesome, CyberCyan, "banking_standing"),
    BENEFICIARY_MANAGEMENT("Beneficiary & Payee Registry", "Payees", Icons.Default.Person, ElectricBlue, "banking_beneficiaries"),
    ACCOUNT_STATEMENTS("Audited Statements & Tax Exports", "Statements", Icons.Default.Description, EmeraldSuccess, "banking_statements")
}

// -------------------------------------------------------------------------
// MASTER BANKING MODULE HUB (10-Page Switcher)
// -------------------------------------------------------------------------
@Composable
fun BankingModuleHubScreen(
    viewModel: BankViewModel,
    initialPage: BankingModulePageType = BankingModulePageType.ACCOUNT_SUMMARY,
    onNavigateToPage: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedPage by remember { mutableStateOf(initialPage) }
    val totalBalance by viewModel.totalNetWorth.collectAsStateWithLifecycle()
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Master Banking Balance Top Ribbon
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, selectedPage.color.copy(alpha = 0.6f))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(selectedPage.color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = selectedPage.icon,
                        contentDescription = null,
                        tint = selectedPage.color,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = selectedPage.title,
                            color = TextWhite,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                        PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 6.dp)
                    }
                    Text(
                        text = "Total Liquidity: $%,.2f USD • FedNow / SEPA Instant Active".format(totalBalance),
                        color = selectedPage.color,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // 10-Tab Scrollable Bar
        ScrollableTabRow(
            selectedTabIndex = BankingModulePageType.values().indexOf(selectedPage),
            containerColor = NavyCard,
            contentColor = selectedPage.color,
            edgePadding = 4.dp,
            indicator = { tabPositions ->
                val idx = BankingModulePageType.values().indexOf(selectedPage)
                if (idx in tabPositions.indices) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[idx]),
                        color = selectedPage.color,
                        height = 3.dp
                    )
                }
            }
        ) {
            BankingModulePageType.values().forEach { page ->
                val isSelected = selectedPage == page
                Tab(
                    selected = isSelected,
                    onClick = {
                        selectedPage = page
                        onNavigateToPage?.invoke(page.routeKey)
                    },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = page.icon,
                                contentDescription = null,
                                tint = if (isSelected) page.color else TextMuted,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = page.shortLabel,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) page.color else TextMuted
                            )
                        }
                    },
                    modifier = Modifier.testTag("tab_banking_${page.routeKey}")
                )
            }
        }

        // Animated Screen Content Switcher
        AnimatedContent(
            targetState = selectedPage,
            transitionSpec = {
                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width } + fadeOut()
                )
            },
            label = "BankingPageTransition",
            modifier = Modifier.weight(1f)
        ) { targetPage ->
            when (targetPage) {
                BankingModulePageType.ACCOUNT_SUMMARY -> AccountSummaryScreen(viewModel = viewModel)
                BankingModulePageType.BALANCE_OVERVIEW -> BalanceOverviewScreen(viewModel = viewModel)
                BankingModulePageType.DEPOSIT -> DepositOperationsScreen(viewModel = viewModel)
                BankingModulePageType.WITHDRAWAL -> WithdrawalOperationsScreen(viewModel = viewModel)
                BankingModulePageType.MONEY_TRANSFER -> DomesticMoneyTransferScreen(viewModel = viewModel)
                BankingModulePageType.INTERNATIONAL_TRANSFER -> InternationalTransferScreen(viewModel = viewModel)
                BankingModulePageType.SCHEDULED_PAYMENTS -> ScheduledPaymentsScreen(viewModel = viewModel)
                BankingModulePageType.STANDING_INSTRUCTIONS -> StandingInstructionsScreen(viewModel = viewModel)
                BankingModulePageType.BENEFICIARY_MANAGEMENT -> BeneficiaryManagementScreen(viewModel = viewModel)
                BankingModulePageType.ACCOUNT_STATEMENTS -> AccountStatementsScreen(viewModel = viewModel)
            }
        }
    }
}

// -------------------------------------------------------------------------
// 1. ACCOUNT SUMMARY SCREEN
// -------------------------------------------------------------------------
@Composable
fun AccountSummaryScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    var newAccountName by remember { mutableStateOf("High-Yield Treasury Reserve") }
    var selectedType by remember { mutableStateOf(AccountType.SAVINGS) }
    var isCreated by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            BankingSectionHeader(
                title = "Sovereign Account Summary",
                subtitle = "Comprehensive multi-account ledger, routing numbers, IBANs, and APY rates.",
                icon = Icons.Default.AccountBalance,
                color = CyberCyan
            )
        }

        // Active Accounts Cards
        item {
            Text("ACTIVE VAULT ACCOUNTS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                accounts.forEach { account ->
                    Surface(
                        color = NavyCard,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(CyberCyan.copy(alpha = 0.15f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(account.name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Acc: ${account.accountNumber} • ${account.type}", color = TextMuted, fontSize = 10.sp, fontFamily = FontFamily.Monospace)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("$%,.2f USD".format(account.balance), color = EmeraldSuccess, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                                Text("APY: 4.85%", color = GoldAccent, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // APPLICATION FORM: Open New Sovereign Sub-Account
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("OPEN NEW SOVEREIGN SUB-ACCOUNT", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = newAccountName,
                        onValueChange = { newAccountName = it },
                        label = { Text("Account Name / Purpose", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        AccountType.values().take(3).forEach { type ->
                            val isSel = selectedType == type
                            Surface(
                                color = if (isSel) CyberCyan.copy(alpha = 0.2f) else NavyCard,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) CyberCyan else Navy700),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedType = type }
                            ) {
                                Text(
                                    text = type.name,
                                    color = if (isSel) CyberCyan else TextMuted,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 10.dp)
                                )
                            }
                        }
                    }

                    if (isCreated) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("New sub-account '$newAccountName' provisioned with unique IBAN & routing keys!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isCreated = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Create Sub-Account & Issue IBAN", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 2. BALANCE OVERVIEW SCREEN
// -------------------------------------------------------------------------
@Composable
fun BalanceOverviewScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val totalBalance by viewModel.totalNetWorth.collectAsStateWithLifecycle()
    var convertFromAmount by remember { mutableStateOf("1000") }
    var convertTargetCurrency by remember { mutableStateOf("EUR (€)") }
    var isConverted by remember { mutableStateOf(false) }

    val multiCurrencies = listOf(
        Triple("US Dollar (USD)", "$%,.2f".format(totalBalance * 0.65), EmeraldSuccess),
        Triple("Euro (EUR)", "€%,.2f".format(totalBalance * 0.22 * 0.92), CyberCyan),
        Triple("British Pound (GBP)", "£%,.2f".format(totalBalance * 0.08 * 0.79), ElectricBlue),
        Triple("Bitcoin (BTC)", "%.4f BTC".format(totalBalance * 0.05 / 65000.0), GoldAccent)
    )

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            BankingSectionHeader(
                title = "Balance Overview & FX Holdings",
                subtitle = "Multi-currency asset distribution, real-time forex valuation, and yield allocations.",
                icon = Icons.Default.AccountBalanceWallet,
                color = EmeraldSuccess
            )
        }

        // Multi-Currency Breakdown
        item {
            Text("GLOBAL LIQUIDITY RESERVES", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                multiCurrencies.forEach { (name, amount, color) ->
                    Surface(
                        color = NavyCard,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(modifier = Modifier.size(34.dp).clip(CircleShape).background(color.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.CurrencyExchange, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text("Spot FX Rate Locked • Zero Spread", color = TextMuted, fontSize = 9.sp)
                            }
                            Text(amount, color = color, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // APPLICATION FORM: Instant Multi-Currency Swap
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("INSTANT FX SPOT CONVERSION", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = convertFromAmount,
                            onValueChange = { convertFromAmount = it },
                            label = { Text("Amount ($ USD)", fontSize = 11.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = bankingTextFieldColors(EmeraldSuccess),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = convertTargetCurrency,
                            onValueChange = { convertTargetCurrency = it },
                            label = { Text("Target Currency", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = bankingTextFieldColors(EmeraldSuccess),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    if (isConverted) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Converted $convertFromAmount USD to $convertTargetCurrency with zero slippage!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isConverted = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Execute Instant Spot Conversion", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 3. DEPOSIT OPERATIONS SCREEN
// -------------------------------------------------------------------------
@Composable
fun DepositOperationsScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var depositAmount by remember { mutableStateOf("5000") }
    var depositMethod by remember { mutableStateOf("FedNow Instant Settlement / Wire") }
    var referenceNote by remember { mutableStateOf("Quarterly Capital Inflow") }
    var isDeposited by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            BankingSectionHeader(
                title = "Deposit & Instant Liquidity Inflow",
                subtitle = "ACH Direct Deposit, FedNow Instant settlement, and Mobile Check Capture with OCR.",
                icon = Icons.Default.ArrowDownward,
                color = CyberCyan
            )
        }

        // APPLICATION FORM: Deposit Inflow
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("INITIATE INSTANT DEPOSIT INFLOW", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = depositAmount,
                        onValueChange = { depositAmount = it },
                        label = { Text("Deposit Amount ($ USD)", fontSize = 11.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = depositMethod,
                        onValueChange = { depositMethod = it },
                        label = { Text("Deposit Inflow Channel", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = referenceNote,
                        onValueChange = { referenceNote = it },
                        label = { Text("Reference / Deposit Memo", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (isDeposited) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Deposit of $$depositAmount USD settled instantly into primary vault account!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            val amt = depositAmount.toDoubleOrNull() ?: 100.0
                            viewModel.showMessage("✓ Deposit of $%,.2f settled into account!".format(amt))
                            isDeposited = true
                        },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Confirm Instant Inflow Deposit", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 4. WITHDRAWAL OPERATIONS SCREEN
// -------------------------------------------------------------------------
@Composable
fun WithdrawalOperationsScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var withdrawAmount by remember { mutableStateOf("1500") }
    var destinationIban by remember { mutableStateOf("US89 FEDNOW 0192 8492 0182 99") }
    var authPasskey by remember { mutableStateOf("••••••••") }
    var isWithdrawn by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            BankingSectionHeader(
                title = "Withdrawal & Outflow Liquidity",
                subtitle = "Real-time outgoing wire transfers, contactless ATM authorization tokens, and hardware enclave 2FA.",
                icon = Icons.Default.ArrowUpward,
                color = AmberOrange
            )
        }

        // APPLICATION FORM: Outgoing Withdrawal
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("AUTHORIZE OUTGOING LIQUIDITY WITHDRAWAL", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = withdrawAmount,
                        onValueChange = { withdrawAmount = it },
                        label = { Text("Withdrawal Amount ($ USD)", fontSize = 11.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(AmberOrange),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = destinationIban,
                        onValueChange = { destinationIban = it },
                        label = { Text("Destination Account / IBAN", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(AmberOrange),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = authPasskey,
                        onValueChange = { authPasskey = it },
                        label = { Text("Security Enclave Passkey (2FA)", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(AmberOrange),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (isWithdrawn) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Withdrawal of $$withdrawAmount authorized and dispatched via FedNow!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            val amt = withdrawAmount.toDoubleOrNull() ?: 100.0
                            viewModel.showMessage("✓ Withdrawal of $%,.2f authorized!".format(amt))
                            isWithdrawn = true
                        },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberOrange),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Sign & Execute Outgoing Withdrawal", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 5. DOMESTIC MONEY TRANSFER SCREEN
// -------------------------------------------------------------------------
@Composable
fun DomesticMoneyTransferScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var recipientName by remember { mutableStateOf("Elena Vance") }
    var transferAmount by remember { mutableStateOf("2400") }
    var paymentMemo by remember { mutableStateOf("Private Family Trust Allocation") }
    var isTransferred by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            BankingSectionHeader(
                title = "Instant Domestic Money Transfer",
                subtitle = "Sub-second FedNow, RTP network, and instant ACH peer-to-peer settlement.",
                icon = Icons.Default.Send,
                color = ElectricBlue
            )
        }

        // APPLICATION FORM: Domestic Transfer
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("SEND INSTANT DOMESTIC PAYMENT", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = recipientName,
                        onValueChange = { recipientName = it },
                        label = { Text("Recipient Name / DID Handle", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = transferAmount,
                        onValueChange = { transferAmount = it },
                        label = { Text("Transfer Amount ($ USD)", fontSize = 11.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = paymentMemo,
                        onValueChange = { paymentMemo = it },
                        label = { Text("Payment Description / Memo", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (isTransferred) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Transfer of $$transferAmount to $recipientName settled with instant finality!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            val amt = transferAmount.toDoubleOrNull() ?: 100.0
                            viewModel.showMessage("✓ Domestic transfer of $%,.2f to $recipientName confirmed!".format(amt))
                            isTransferred = true
                        },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Authorize Instant Domestic Transfer", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 6. INTERNATIONAL TRANSFER SCREEN
// -------------------------------------------------------------------------
@Composable
fun InternationalTransferScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var recipientIban by remember { mutableStateOf("CH93 0076 2011 6238 5291 0") }
    var swiftBic by remember { mutableStateOf("UBSWCHZH80A (UBS Switzerland)") }
    var foreignAmount by remember { mutableStateOf("12500") }
    var targetCurrency by remember { mutableStateOf("CHF (Swiss Franc)") }
    var isSwiftDispatched by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            BankingSectionHeader(
                title = "Cross-Border SWIFT & RippleNet FX",
                subtitle = "High-value cross-border wires, ISO 20022 messaging, locked FX rates, and correspondent routing.",
                icon = Icons.Default.Public,
                color = PurpleTech
            )
        }

        // APPLICATION FORM: International Transfer
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("CROSS-BORDER WIRE & SWIFT / ISO 20022", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = recipientIban,
                        onValueChange = { recipientIban = it },
                        label = { Text("International Beneficiary IBAN", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(PurpleTech),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = swiftBic,
                        onValueChange = { swiftBic = it },
                        label = { Text("SWIFT / BIC Bank Identifier", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(PurpleTech),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = foreignAmount,
                            onValueChange = { foreignAmount = it },
                            label = { Text("Amount ($ USD)", fontSize = 11.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = bankingTextFieldColors(PurpleTech),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = targetCurrency,
                            onValueChange = { targetCurrency = it },
                            label = { Text("Payout Currency", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = bankingTextFieldColors(PurpleTech),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    if (isSwiftDispatched) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("ISO 20022 SWIFT Wire of $$foreignAmount USD dispatched to $swiftBic!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isSwiftDispatched = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleTech),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Dispatch Cross-Border SWIFT Wire", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 7. SCHEDULED PAYMENTS SCREEN
// -------------------------------------------------------------------------
@Composable
fun ScheduledPaymentsScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var payeeName by remember { mutableStateOf("AWS Quantum Cloud Infrastructure") }
    var scheduledAmount by remember { mutableStateOf("850.00") }
    var recurrenceFrequency by remember { mutableStateOf("Monthly on 1st") }
    var executionDate by remember { mutableStateOf("2026-09-01") }
    var isScheduled by remember { mutableStateOf(false) }

    val activeSchedules = listOf(
        Triple("Metropolitan Mortgage & Land Trust", "$3,200.00 / mo", GoldAccent),
        Triple("Quantum Cloud AI Compute Services", "$850.00 / mo", CyberCyan),
        Triple("Health & Sovereign Umbrella Insurance", "$420.00 / mo", EmeraldSuccess)
    )

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            BankingSectionHeader(
                title = "Scheduled & Recurring Payments",
                subtitle = "Automated recurring wire executions, utility billers, and calendar-linked payment schedules.",
                icon = Icons.Default.Schedule,
                color = GoldAccent
            )
        }

        // Active Scheduled Payments
        item {
            Text("ACTIVE SCHEDULED PAYMENT FLOWS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                activeSchedules.forEach { (payee, amount, color) ->
                    Surface(
                        color = NavyCard,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(modifier = Modifier.size(34.dp).clip(CircleShape).background(color.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.CalendarMonth, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(payee, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text("Next Run: Sep 1, 2026 • Auto-Debit Vault", color = TextMuted, fontSize = 9.sp)
                            }
                            Text(amount, color = color, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }

        // APPLICATION FORM: Create Scheduled Payment
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("CREATE NEW SCHEDULED RECURRING PAYMENT", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = payeeName,
                        onValueChange = { payeeName = it },
                        label = { Text("Biller / Payee Name", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(GoldAccent),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = scheduledAmount,
                            onValueChange = { scheduledAmount = it },
                            label = { Text("Amount ($ USD)", fontSize = 11.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = bankingTextFieldColors(GoldAccent),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = recurrenceFrequency,
                            onValueChange = { recurrenceFrequency = it },
                            label = { Text("Frequency", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = bankingTextFieldColors(GoldAccent),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    if (isScheduled) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Recurring payment to '$payeeName' scheduled successfully!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isScheduled = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Lock & Schedule Recurring Payment", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 8. STANDING INSTRUCTIONS SCREEN
// -------------------------------------------------------------------------
@Composable
fun StandingInstructionsScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var sweepThreshold by remember { mutableFloatStateOf(10000f) }
    var isAutoSavePaycheckEnabled by remember { mutableStateOf(true) }
    var isRoundUpSavingsEnabled by remember { mutableStateOf(true) }
    var isSaved by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            BankingSectionHeader(
                title = "Standing Instructions & Smart Treasury",
                subtitle = "Algorithmic sweep instructions, automated paycheck yield splits, and round-up investment triggers.",
                icon = Icons.Default.AutoAwesome,
                color = CyberCyan
            )
        }

        // APPLICATION FORM: Treasury Rules
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("ACTIVE AUTOMATION & TREASURY SWEEP RULES", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Automatic Paycheck 20% Yield Sweep", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text("Routes 20% of direct deposits directly to 4.85% APY Vault", color = TextMuted, fontSize = 9.sp)
                        }
                        Switch(
                            checked = isAutoSavePaycheckEnabled,
                            onCheckedChange = { isAutoSavePaycheckEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Navy900, checkedTrackColor = CyberCyan)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Debit Card Purchase Round-Up Stash", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text("Rounds transactions to nearest dollar and saves spare change", color = TextMuted, fontSize = 9.sp)
                        }
                        Switch(
                            checked = isRoundUpSavingsEnabled,
                            onCheckedChange = { isRoundUpSavingsEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Navy900, checkedTrackColor = CyberCyan)
                        )
                    }

                    Text("Operating Balance Sweep Threshold: $%,d USD".format(sweepThreshold.toInt()), color = TextMuted, fontSize = 11.sp)
                    Slider(
                        value = sweepThreshold,
                        onValueChange = { sweepThreshold = it },
                        valueRange = 5000f..50000f,
                        colors = SliderDefaults.colors(thumbColor = CyberCyan, activeTrackColor = CyberCyan, inactiveTrackColor = Navy800)
                    )

                    if (isSaved) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Treasury standing instructions updated and deployed to smart vault!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isSaved = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Apply Standing Instructions", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 9. BENEFICIARY MANAGEMENT SCREEN
// -------------------------------------------------------------------------
@Composable
fun BeneficiaryManagementScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var beneficiaryName by remember { mutableStateOf("Marcus Sterling (Sterling Holdings LLC)") }
    var beneficiaryIban by remember { mutableStateOf("GB82 BARC 2004 1538 9201 22") }
    var transferNetwork by remember { mutableStateOf("SEPA Instant / SWIFT Global") }
    var isAdded by remember { mutableStateOf(false) }

    val payees = listOf(
        Triple("Elena Vance (Personal Trust)", "FedNow #01928301 • Whitelisted", EmeraldSuccess),
        Triple("Quantum AI Infrastructure Corp", "US Wire #9482019 • Whitelisted", CyberCyan),
        Triple("Zurich Wealth Multi-Family Office", "SWIFT UBSWCHZH • Cooling 24h", GoldAccent)
    )

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            BankingSectionHeader(
                title = "Beneficiary & Payee Whitelist",
                subtitle = "Cryptographically attested recipient registry with 24-hour security cooling periods.",
                icon = Icons.Default.Person,
                color = ElectricBlue
            )
        }

        // Stored Payees
        item {
            Text("WHITELISTED BENEFICIARY DIRECTORY", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                payees.forEach { (name, details, color) ->
                    Surface(
                        color = NavyCard,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(modifier = Modifier.size(34.dp).clip(CircleShape).background(color.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text(details, color = TextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                            }
                            Icon(Icons.Default.Lock, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // APPLICATION FORM: Add Beneficiary
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("ADD & WHITELIST NEW BENEFICIARY", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = beneficiaryName,
                        onValueChange = { beneficiaryName = it },
                        label = { Text("Legal Entity / Beneficiary Name", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = beneficiaryIban,
                        onValueChange = { beneficiaryIban = it },
                        label = { Text("Account Number / IBAN / DID", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = transferNetwork,
                        onValueChange = { transferNetwork = it },
                        label = { Text("Settlement Rail / Network", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (isAdded) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Beneficiary added to whitelist! 24h security cooling period active.", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isAdded = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Add & Sign Beneficiary Whitelist", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 10. ACCOUNT STATEMENTS SCREEN
// -------------------------------------------------------------------------
@Composable
fun AccountStatementsScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var statementPeriod by remember { mutableStateOf("July 2026 (Monthly Audited Statement)") }
    var exportFormat by remember { mutableStateOf("Encrypted PDF + PGP Signed XML") }
    var isDownloaded by remember { mutableStateOf(false) }

    val statementHistory = listOf(
        Triple("Statement - July 2026", "PDF • 2.4 MB • SHA256 Verified", EmeraldSuccess),
        Triple("Statement - June 2026", "PDF • 2.1 MB • SHA256 Verified", EmeraldSuccess),
        Triple("Statement - May 2026", "PDF • 1.9 MB • SHA256 Verified", CyberCyan),
        Triple("Annual Audit Pack 2025", "ZIP • 14.8 MB • Tax Form 1099-INT", GoldAccent)
    )

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            BankingSectionHeader(
                title = "Audited Account Statements & Tax Packs",
                subtitle = "Cryptographically signed monthly statements, tax year 1099-INT exports, and audit trails.",
                icon = Icons.Default.Description,
                color = EmeraldSuccess
            )
        }

        // Available Statements
        item {
            Text("HISTORICAL AUDITED STATEMENTS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                statementHistory.forEach { (name, meta, color) ->
                    Surface(
                        color = NavyCard,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(modifier = Modifier.size(34.dp).clip(CircleShape).background(color.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Receipt, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text(meta, color = TextMuted, fontSize = 9.sp)
                            }
                            Icon(Icons.Default.Download, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }

        // APPLICATION FORM: Generate Statement Export
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("GENERATE CUSTOM AUDIT STATEMENT", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = statementPeriod,
                        onValueChange = { statementPeriod = it },
                        label = { Text("Statement Billing Cycle", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(EmeraldSuccess),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = exportFormat,
                        onValueChange = { exportFormat = it },
                        label = { Text("Cryptographic Export Format", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = bankingTextFieldColors(EmeraldSuccess),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (isDownloaded) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Statement package compiled with RSA-4096 signature stamp!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isDownloaded = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Compile & Export Cryptographic Statement", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// REUSABLE BANKING HELPERS
// -------------------------------------------------------------------------
@Composable
private fun BankingSectionHeader(title: String, subtitle: String, icon: ImageVector, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(subtitle, color = TextMuted, fontSize = 10.sp, lineHeight = 13.sp)
            }
        }
    }
}

@Composable
private fun bankingTextFieldColors(activeColor: Color) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = activeColor,
    unfocusedBorderColor = Navy700,
    focusedTextColor = TextWhite,
    unfocusedTextColor = TextWhite,
    focusedLabelColor = activeColor,
    unfocusedLabelColor = TextMuted
)
