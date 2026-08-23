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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.TransactionCategory
import com.example.data.model.TransactionType
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

enum class UserDashboardPageType(
    val title: String,
    val shortLabel: String,
    val icon: ImageVector,
    val color: Color,
    val routeKey: String
) {
    GLOBAL("Global Planetary Dashboard", "Global", Icons.Default.Public, CyberCyan, "dashboard_global"),
    PERSONAL("Personal Sovereign Dashboard", "Personal", Icons.Default.Person, ElectricBlue, "dashboard_personal"),
    FINANCIAL_HEALTH("Financial Health & Score", "Health Score", Icons.Default.HealthAndSafety, EmeraldSuccess, "dashboard_health"),
    AI_INSIGHTS("AGI Predictive Insights", "AI Insights", Icons.Default.AutoAwesome, PurpleTech, "dashboard_insights"),
    NOTIFICATIONS("Alerts & Command Center", "Notifications", Icons.Default.Notifications, AmberOrange, "dashboard_notifications"),
    RECENT_ACTIVITIES("Audit Ledger & Activities", "Activities", Icons.Default.History, CyberCyan, "dashboard_activities"),
    RECOMMENDATIONS("Personalized AI Directives", "AI Advise", Icons.Default.Lightbulb, GoldAccent, "dashboard_recommendations")
}

// -------------------------------------------------------------------------
// 1. MASTER USER DASHBOARD HUB SCREEN (Multi-Page Host)
// -------------------------------------------------------------------------
@Composable
fun UserDashboardHubScreen(
    viewModel: BankViewModel,
    initialPage: UserDashboardPageType = UserDashboardPageType.GLOBAL,
    onNavigateToPage: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedPage by remember { mutableStateOf(initialPage) }
    val totalNetWorth by viewModel.totalNetWorth.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Top Master Telemetry Card
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
                        .size(44.dp)
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
                            text = "SMARTBANK 360° DASHBOARD SUITE",
                            color = TextWhite,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                        PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 6.dp)
                    }
                    Text(
                        text = "Planetary Liquidity $${"%,.2f".format(totalNetWorth)} • Real-Time AI Autonomous Feed",
                        color = selectedPage.color,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // 7-Page Scrollable Navigation Tab Bar
        ScrollableTabRow(
            selectedTabIndex = UserDashboardPageType.values().indexOf(selectedPage),
            containerColor = NavyCard,
            contentColor = selectedPage.color,
            edgePadding = 4.dp,
            indicator = { tabPositions ->
                val idx = UserDashboardPageType.values().indexOf(selectedPage)
                if (idx in tabPositions.indices) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[idx]),
                        color = selectedPage.color,
                        height = 3.dp
                    )
                }
            }
        ) {
            UserDashboardPageType.values().forEach { page ->
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
                    modifier = Modifier.testTag("tab_dashboard_${page.routeKey}")
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
            label = "DashboardPageTransition",
            modifier = Modifier.weight(1f)
        ) { targetPage ->
            when (targetPage) {
                UserDashboardPageType.GLOBAL -> GlobalDashboardScreen(viewModel = viewModel)
                UserDashboardPageType.PERSONAL -> PersonalDashboardScreen(viewModel = viewModel)
                UserDashboardPageType.FINANCIAL_HEALTH -> FinancialHealthDashboardScreen(viewModel = viewModel)
                UserDashboardPageType.AI_INSIGHTS -> AiInsightsDashboardScreen(viewModel = viewModel)
                UserDashboardPageType.NOTIFICATIONS -> NotificationsDashboardScreen(viewModel = viewModel)
                UserDashboardPageType.RECENT_ACTIVITIES -> RecentActivitiesDashboardScreen(viewModel = viewModel)
                UserDashboardPageType.RECOMMENDATIONS -> PersonalizedRecommendationsDashboardScreen(viewModel = viewModel)
            }
        }
    }
}

// -------------------------------------------------------------------------
// 2. PAGE 1: GLOBAL DASHBOARD SCREEN
// -------------------------------------------------------------------------
@Composable
fun GlobalDashboardScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val totalNetWorth by viewModel.totalNetWorth.collectAsStateWithLifecycle()
    var selectedCurrency by remember { mutableStateOf("USD") }
    var transferAmount by remember { mutableStateOf("5000") }
    var destinationRegion by remember { mutableStateOf("European Union (SEPA Instant)") }
    var isHedgingEnabled by remember { mutableStateOf(true) }
    var isSubmitted by remember { mutableStateOf(false) }

    val regions = listOf(
        "North America (FedNow)" to "$48,290.50",
        "European Union (SEPA Instant)" to "€32,150.00",
        "Asia-Pacific (Cross-Border QR)" to "¥184,300.00",
        "Global Quantum Staking Nodes" to "14.85 ETH"
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            DashboardSectionHeader(
                title = "Global Planetary Dashboard",
                subtitle = "Universal multi-currency liquidity grid, sovereign nodes, and cross-border settlement rails.",
                icon = Icons.Default.Public,
                color = CyberCyan
            )
        }

        // Global Planetary Liquidity Visualizer
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("WORLD CONSOLIDATED LIQUIDITY", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Surface(color = CyberCyan.copy(alpha = 0.15f), shape = RoundedCornerShape(6.dp)) {
                            Text("ISO 20022 READY", color = CyberCyan, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }

                    Text(
                        text = "$${"%,.2f".format(totalNetWorth)}",
                        color = TextWhite,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        fontFamily = FontFamily.Monospace
                    )

                    // Global Node Allocations
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        regions.forEach { (region, balance) ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(NavyCard)
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(CyberCyan))
                                    Text(region, color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                                }
                                Text(balance, color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
                            }
                        }
                    }
                }
            }
        }

        // APPLICATION FORM: Cross-Border Liquidity Transfer & Currency Hedging
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("GLOBAL CROSS-BORDER DISPATCH & FX HEDGE", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    // Target Region Selector
                    Text("Destination Region & Settlement Rail:", color = TextMuted, fontSize = 11.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        listOf("SEPA Instant (EU)", "FedNow (USA)", "APAC Mesh").forEach { reg ->
                            val isSel = destinationRegion.startsWith(reg.take(4))
                            Surface(
                                color = if (isSel) CyberCyan.copy(alpha = 0.2f) else Navy800,
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) CyberCyan else Navy700),
                                modifier = Modifier.weight(1f).clickable { destinationRegion = reg }
                            ) {
                                Text(
                                    text = reg,
                                    color = if (isSel) CyberCyan else TextMuted,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 6.dp, horizontal = 2.dp)
                                )
                            }
                        }
                    }

                    // Dispatch Amount
                    OutlinedTextField(
                        value = transferAmount,
                        onValueChange = { transferAmount = it },
                        label = { Text("Transfer Amount ($USD)", fontSize = 11.sp) },
                        leadingIcon = { Icon(Icons.Default.Paid, contentDescription = null, tint = CyberCyan) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = CyberCyan,
                            unfocusedLabelColor = TextMuted
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // AI Automated Currency Hedging Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Real-Time FX Hedging Shield", color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
                            Text("Locks live spot exchange rate against macro currency volatility", color = TextMuted, fontSize = 9.sp)
                        }
                        Switch(
                            checked = isHedgingEnabled,
                            onCheckedChange = { isHedgingEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Navy900,
                                checkedTrackColor = CyberCyan,
                                uncheckedThumbColor = TextMuted,
                                uncheckedTrackColor = Navy800
                            )
                        )
                    }

                    if (isSubmitted) {
                        Surface(
                            color = EmeraldSuccess.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Cross-Border Wire Dispatched! Hedged via SEPA Instant Node #782.", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isSubmitted = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Send, contentDescription = null, tint = Navy900, modifier = Modifier.size(16.dp))
                            Text("Dispatch Hedged Global Liquidity", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 3. PAGE 2: PERSONAL DASHBOARD SCREEN
// -------------------------------------------------------------------------
@Composable
fun PersonalDashboardScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    var goalName by remember { mutableStateOf("Emergency Stash (6 Months)") }
    var targetAmount by remember { mutableStateOf("25000") }
    var monthlyContribution by remember { mutableStateOf("750") }
    var autoDeduct by remember { mutableStateOf(true) }
    var isGoalCreated by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            DashboardSectionHeader(
                title = "Personal Sovereign Dashboard",
                subtitle = "Direct access to checking vaults, high-yield vaults, smart savings goals, and virtual cards.",
                icon = Icons.Default.Person,
                color = ElectricBlue
            )
        }

        // Sovereign Account Cards
        item {
            Text("YOUR ACTIVE VAULTS & ACCOUNTS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(top = 6.dp)) {
                accounts.forEach { account ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Navy900),
                        shape = RoundedCornerShape(14.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (account.isFrozen) CrimsonDanger.copy(alpha = 0.6f) else ElectricBlue.copy(alpha = 0.3f))
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(40.dp).clip(CircleShape).background(ElectricBlue.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(20.dp))
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(account.name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    if (account.isFrozen) {
                                        Surface(color = CrimsonDanger.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                                            Text("FROZEN", color = CrimsonDanger, fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                        }
                                    }
                                }
                                Text("•••• ${account.accountNumber.takeLast(4)} • ${account.type.name}", color = TextMuted, fontSize = 10.sp)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    "$${"%,.2f".format(account.balance)}",
                                    color = if (account.isFrozen) CrimsonDanger else EmeraldSuccess,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text("APY 4.85%", color = GoldAccent, fontSize = 9.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }
            }
        }

        // APPLICATION FORM: Instant Smart Savings Goal Creator
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("CREATE NEW SMART SAVINGS GOAL", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = goalName,
                        onValueChange = { goalName = it },
                        label = { Text("Savings Goal Title", fontSize = 11.sp) },
                        leadingIcon = { Icon(Icons.Default.Savings, contentDescription = null, tint = ElectricBlue) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = ElectricBlue,
                            unfocusedLabelColor = TextMuted
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = targetAmount,
                            onValueChange = { targetAmount = it },
                            label = { Text("Target ($USD)", fontSize = 11.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBlue,
                                unfocusedBorderColor = Navy700,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedLabelColor = ElectricBlue,
                                unfocusedLabelColor = TextMuted
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )

                        OutlinedTextField(
                            value = monthlyContribution,
                            onValueChange = { monthlyContribution = it },
                            label = { Text("Monthly ($USD)", fontSize = 11.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = ElectricBlue,
                                unfocusedBorderColor = Navy700,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedLabelColor = ElectricBlue,
                                unfocusedLabelColor = TextMuted
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = autoDeduct,
                            onCheckedChange = { autoDeduct = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = ElectricBlue,
                                uncheckedColor = TextMuted,
                                checkmarkColor = Navy900
                            )
                        )
                        Text(
                            "Enable AI Autonomous Micro-Savings Roundups on every transaction",
                            color = TextWhite,
                            fontSize = 10.sp
                        )
                    }

                    if (isGoalCreated) {
                        Surface(
                            color = EmeraldSuccess.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Smart Goal Created! Goal '$goalName' active with APY 5.15% lock.", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isGoalCreated = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = TextWhite, modifier = Modifier.size(16.dp))
                            Text("Activate Smart Savings Goal", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 4. PAGE 3: FINANCIAL HEALTH DASHBOARD SCREEN
// -------------------------------------------------------------------------
@Composable
fun FinancialHealthDashboardScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    var healthScore by remember { mutableIntStateOf(885) }
    var emergencyRunwayMonths by remember { mutableIntStateOf(14) }
    var savingsRatePercent by remember { mutableIntStateOf(42) }
    var debtToIncomePercent by remember { mutableIntStateOf(12) }

    var extraMonthlyPaydown by remember { mutableStateOf("500") }
    var simulationResultMonths by remember { mutableStateOf("14 months to 0 Debt") }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            DashboardSectionHeader(
                title = "Financial Health & Score Diagnostic",
                subtitle = "Holistic 360-degree liquidity score, debt-to-income analysis, and emergency runway metrics.",
                icon = Icons.Default.HealthAndSafety,
                color = EmeraldSuccess
            )
        }

        // Health Score Speedometer Gauge
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text("SMARTBANK UNIVERSAL HEALTH SCORE", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)

                    Box(contentAlignment = Alignment.Center, modifier = Modifier.size(140.dp)) {
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            drawArc(
                                color = NavyCard,
                                startAngle = 135f,
                                sweepAngle = 270f,
                                useCenter = false,
                                style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                            )
                            val sweep = (healthScore / 1000f) * 270f
                            drawArc(
                                color = EmeraldSuccess,
                                startAngle = 135f,
                                sweepAngle = sweep,
                                useCenter = false,
                                style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                            )
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "$healthScore",
                                color = EmeraldSuccess,
                                fontWeight = FontWeight.Black,
                                fontSize = 32.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text("SUPER PRIME", color = TextWhite, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    // Key Health Metrics Grid
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        HealthMetricBadge(label = "Emergency Runway", value = "$emergencyRunwayMonths Mo", color = EmeraldSuccess)
                        HealthMetricBadge(label = "Savings Rate", value = "$savingsRatePercent%", color = CyberCyan)
                        HealthMetricBadge(label = "Debt-to-Income", value = "$debtToIncomePercent%", color = GoldAccent)
                    }
                }
            }
        }

        // APPLICATION FORM: Financial Health Stress-Test & Payoff Simulator
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("DEBT ACCELERATION & STRESS TEST SIMULATOR", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    Text("Simulate allocating extra monthly capital to eliminate existing debts and raise health score to 950+:", color = TextMuted, fontSize = 10.sp)

                    OutlinedTextField(
                        value = extraMonthlyPaydown,
                        onValueChange = {
                            extraMonthlyPaydown = it
                            val extra = it.toDoubleOrNull() ?: 0.0
                            val monthsLeft = if (extra > 0) (14000.0 / (750 + extra)).toInt() else 18
                            simulationResultMonths = "$monthsLeft months to debt-free ($${(extra * 12).toInt()} saved in interest)"
                        },
                        label = { Text("Extra Monthly Allocation ($USD)", fontSize = 11.sp) },
                        leadingIcon = { Icon(Icons.Default.Speed, contentDescription = null, tint = EmeraldSuccess) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldSuccess,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = EmeraldSuccess,
                            unfocusedLabelColor = TextMuted
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Surface(
                        color = NavyCard,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                                Text("AI SIMULATION FORECAST:", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            Text(simulationResultMonths, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
                        }
                    }

                    Button(
                        onClick = { healthScore = 915 },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Apply AI Paydown Plan to Vault", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

@Composable
private fun HealthMetricBadge(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, color = TextMuted, fontSize = 9.sp)
        Text(value, color = color, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
    }
}

// -------------------------------------------------------------------------
// 5. PAGE 4: AI INSIGHTS DASHBOARD SCREEN
// -------------------------------------------------------------------------
@Composable
fun AiInsightsDashboardScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    var minConfidenceThreshold by remember { mutableFloatStateOf(0.85f) }
    var autoRebalance by remember { mutableStateOf(true) }
    var recurringAuditEnabled by remember { mutableStateOf(true) }
    var isRuleSaved by remember { mutableStateOf(false) }

    val insights = listOf(
        Triple("Subscription Anomaly Detected", "You have 2 unused streaming services ($38/mo). Auto-cancellation ready.", CrimsonDanger),
        Triple("Yield Arbitrage Opportunity", "Moving $12,500 from Checking to Quantum Vault earns +$645/yr in APY.", GoldAccent),
        Triple("Tax-Loss Harvesting Alert", "Harvesting $1,200 loss in Solar Index offsets capital gains tax liability.", CyberCyan),
        Triple("Spending Velocity Projection", "Predicted end-of-month surplus: +$2,340 (+18% vs last month).", EmeraldSuccess)
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            DashboardSectionHeader(
                title = "AGI Predictive Insights & Engine",
                subtitle = "Autonomous telemetry, cash flow forecasting, yield arbitrage, and subscription leak defense.",
                icon = Icons.Default.AutoAwesome,
                color = PurpleTech
            )
        }

        // Active AI Insights Feed
        item {
            Text("LIVE AUTONOMOUS DIRECTIVES", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 6.dp)) {
                insights.forEach { (title, desc, color) ->
                    Surface(
                        color = NavyCard,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(34.dp).clip(CircleShape).background(color.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Bolt, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text(desc, color = TextMuted, fontSize = 9.sp, lineHeight = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // APPLICATION FORM: Custom AI Autonomous Rule Engine
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("CONFIGURE AUTONOMOUS AGI RULES", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    Text("Minimum AI Confidence Threshold for Auto-Execution: ${(minConfidenceThreshold * 100).toInt()}%", color = TextMuted, fontSize = 10.sp)
                    Slider(
                        value = minConfidenceThreshold,
                        onValueChange = { minConfidenceThreshold = it },
                        valueRange = 0.70f..0.99f,
                        colors = SliderDefaults.colors(
                            thumbColor = PurpleTech,
                            activeTrackColor = PurpleTech,
                            inactiveTrackColor = Navy800
                        )
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Auto-Rebalance High Yield Vaults", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text("Transfers surplus cash exceeding $5k to 5.15% APY yield vault", color = TextMuted, fontSize = 9.sp)
                        }
                        Switch(
                            checked = autoRebalance,
                            onCheckedChange = { autoRebalance = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Navy900,
                                checkedTrackColor = PurpleTech
                            )
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Continuous Subscription Audit", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text("Alerts and halts price-creep on recurring monthly bills", color = TextMuted, fontSize = 9.sp)
                        }
                        Switch(
                            checked = recurringAuditEnabled,
                            onCheckedChange = { recurringAuditEnabled = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Navy900,
                                checkedTrackColor = PurpleTech
                            )
                        )
                    }

                    if (isRuleSaved) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                                Text("AGI Directives saved and committed to local enclave!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isRuleSaved = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleTech),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Deploy Autonomous Rules", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 6. PAGE 5: NOTIFICATIONS DASHBOARD SCREEN
// -------------------------------------------------------------------------
@Composable
fun NotificationsDashboardScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    var alertThresholdAmount by remember { mutableStateOf("500") }
    var isPushEnabled by remember { mutableStateOf(true) }
    var isSmsEnabled by remember { mutableStateOf(true) }
    var isEmailEncrypted by remember { mutableStateOf(true) }
    var isFilterSaved by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            DashboardSectionHeader(
                title = "Notifications & Real-Time Alert Center",
                subtitle = "Direct telemetry stream for security alarms, liquidity events, and compliance notices.",
                icon = Icons.Default.Notifications,
                color = AmberOrange
            )
        }

        // Real-Time Notification Stream
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("LIVE ALERT STREAM", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                TextButton(onClick = { viewModel.markAllNotificationsRead() }) {
                    Text("Mark All Read", color = AmberOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                if (notifications.isEmpty()) {
                    Text("No active notifications in the ledger.", color = TextMuted, fontSize = 11.sp)
                } else {
                    notifications.forEach { notif ->
                        Surface(
                            color = if (notif.isRead) Navy800 else NavyCard,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (!notif.isRead) AmberOrange.copy(alpha = 0.5f) else Navy700),
                            modifier = Modifier.fillMaxWidth().clickable { viewModel.markNotificationRead(notif.id) }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier.size(32.dp).clip(CircleShape).background(AmberOrange.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = AmberOrange, modifier = Modifier.size(16.dp))
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(notif.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    Text(notif.message, color = TextMuted, fontSize = 9.sp)
                                }
                                if (!notif.isRead) {
                                    Box(modifier = Modifier.size(6.dp).clip(CircleShape).background(AmberOrange))
                                }
                            }
                        }
                    }
                }
            }
        }

        // APPLICATION FORM: Notification Filtering & Channel Preferences
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("NOTIFICATION PREFERENCES & THRESHOLDS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = alertThresholdAmount,
                        onValueChange = { alertThresholdAmount = it },
                        label = { Text("Instant Alert Transaction Threshold ($USD)", fontSize = 11.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmberOrange,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = AmberOrange,
                            unfocusedLabelColor = TextMuted
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = isPushEnabled, onCheckedChange = { isPushEnabled = it }, colors = CheckboxDefaults.colors(checkedColor = AmberOrange))
                            Text("Push", color = TextWhite, fontSize = 11.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = isSmsEnabled, onCheckedChange = { isSmsEnabled = it }, colors = CheckboxDefaults.colors(checkedColor = AmberOrange))
                            Text("SMS", color = TextWhite, fontSize = 11.sp)
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(checked = isEmailEncrypted, onCheckedChange = { isEmailEncrypted = it }, colors = CheckboxDefaults.colors(checkedColor = AmberOrange))
                            Text("PGP Email", color = TextWhite, fontSize = 11.sp)
                        }
                    }

                    if (isFilterSaved) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(8.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                                Text("Preferences synchronized across all devices.", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isFilterSaved = true },
                        modifier = Modifier.fillMaxWidth().height(44.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberOrange),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Save Notification Settings", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 7. PAGE 6: RECENT ACTIVITIES DASHBOARD SCREEN
// -------------------------------------------------------------------------
@Composable
fun RecentActivitiesDashboardScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    var disputeTxId by remember { mutableStateOf("TX-884920") }
    var disputeReason by remember { mutableStateOf("Unauthorized Card Charge / Suspected Fraud") }
    var disputeNotes by remember { mutableStateOf("") }
    var isDisputeSubmitted by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            DashboardSectionHeader(
                title = "Recent Activities & Audit Ledger",
                subtitle = "Cryptographically signed immutable transaction history with zero-knowledge fraud audit checks.",
                icon = Icons.Default.History,
                color = CyberCyan
            )
        }

        // Ledger List
        item {
            Text("RECENT SETTLED TRANSACTIONS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                transactions.take(6).forEach { tx ->
                    val isIncome = tx.type == TransactionType.CREDIT
                    val dateFormatted = remember(tx.timestamp) {
                        java.text.SimpleDateFormat("MMM dd, yyyy • HH:mm", java.util.Locale.US).format(java.util.Date(tx.timestamp))
                    }
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
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(if (isIncome) EmeraldSuccess.copy(alpha = 0.2f) else CrimsonDanger.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = if (isIncome) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                    contentDescription = null,
                                    tint = if (isIncome) EmeraldSuccess else CrimsonDanger,
                                    modifier = Modifier.size(16.dp)
                                )
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(tx.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text("${tx.recipient} • $dateFormatted", color = TextMuted, fontSize = 9.sp)
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${if (isIncome) "+" else "-"}$${"%,.2f".format(tx.amount)}",
                                    color = if (isIncome) EmeraldSuccess else CrimsonDanger,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                                Text(tx.category.name, color = CyberCyan, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        // APPLICATION FORM: Transaction Dispute & Chargeback Claim
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("FILE TRANSACTION DISPUTE & CHARGEBACK", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = disputeTxId,
                        onValueChange = { disputeTxId = it },
                        label = { Text("Transaction ID / Reference", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = CyberCyan,
                            unfocusedLabelColor = TextMuted
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = disputeReason,
                        onValueChange = { disputeReason = it },
                        label = { Text("Dispute Category / Reason", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = CyberCyan,
                            unfocusedLabelColor = TextMuted
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = disputeNotes,
                        onValueChange = { disputeNotes = it },
                        label = { Text("Detailed Explanation / Evidence Notes", fontSize = 11.sp) },
                        maxLines = 3,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = CyberCyan,
                            unfocusedLabelColor = TextMuted
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (isDisputeSubmitted) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                                Text("Dispute #DSP-904 filed! Instant provisional credit applied to your vault.", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isDisputeSubmitted = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Submit Dispute to AI Fraud Arbitrator", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 8. PAGE 7: PERSONALIZED RECOMMENDATIONS DASHBOARD SCREEN
// -------------------------------------------------------------------------
@Composable
fun PersonalizedRecommendationsDashboardScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    var selectedPlan by remember { mutableStateOf("Quantum High-Yield CD Ladder") }
    var allocationAmount by remember { mutableStateOf("10000") }
    var isPlanExecuted by remember { mutableStateOf(false) }

    val recommendations = listOf(
        Triple("Quantum High-Yield CD Ladder", "Allocate to 6-mo & 12-mo ladder locking 5.45% APY with quarterly liquidity.", GoldAccent),
        Triple("Automated Debt Consolidation", "Merge 2 high-interest balances into a 6.2% prime rate loan to save $340/mo.", ElectricBlue),
        Triple("Clean Energy ESG Tax Credit", "Direct $5k to solar energy fund eligible for 30% IRS Section 48 credit.", EmeraldSuccess),
        Triple("Parametric Travel Insurance Shield", "Auto-activate flight delay cover for your upcoming Tokyo itinerary.", PurpleTech)
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            DashboardSectionHeader(
                title = "Personalized AGI Recommendations",
                subtitle = "Tailored wealth optimization strategies matching your risk profile and long-term liquidity targets.",
                icon = Icons.Default.Lightbulb,
                color = GoldAccent
            )
        }

        // Recommendations List
        item {
            Text("MATCHED WEALTH DIRECTIVES", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                recommendations.forEach { (title, desc, color) ->
                    val isSel = selectedPlan == title
                    Surface(
                        color = if (isSel) color.copy(alpha = 0.15f) else NavyCard,
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) color else Navy700),
                        modifier = Modifier.fillMaxWidth().clickable { selectedPlan = title }
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier.size(34.dp).clip(CircleShape).background(color.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.ThumbUp, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    Surface(color = color.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                                        Text("98% MATCH", color = color, fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                                    }
                                }
                                Text(desc, color = TextMuted, fontSize = 9.sp, lineHeight = 12.sp)
                            }
                        }
                    }
                }
            }
        }

        // APPLICATION FORM: Execute Recommendation Strategy
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("EXECUTE STRATEGY: $selectedPlan", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = allocationAmount,
                        onValueChange = { allocationAmount = it },
                        label = { Text("Capital Allocation Amount ($USD)", fontSize = 11.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = GoldAccent,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = GoldAccent,
                            unfocusedLabelColor = TextMuted
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Surface(
                        color = NavyCard,
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Verified, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(18.dp))
                            Text("Projected 12-Month Net Alpha: +$785.00 (+7.85% ROI)", color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (isPlanExecuted) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Strategy locked and initiated! Vault allocations updated.", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isPlanExecuted = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("1-Click Execute Strategy", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// REUSABLE DASHBOARD HEADER HELPER
// -------------------------------------------------------------------------
@Composable
private fun DashboardSectionHeader(title: String, subtitle: String, icon: ImageVector, color: Color) {
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

private const val USD = "USD"
