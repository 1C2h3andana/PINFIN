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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.data.model.TransactionEntity
import com.example.data.model.TransactionStatus
import com.example.data.model.TransactionType
import com.example.data.model.UserRole
import com.example.ui.components.PulsingStatusBadge
import com.example.ui.components.RiskBadge
import com.example.ui.components.TransactionRowItem
import com.example.ui.components.VirtualCardItem
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

    var selectedTxForDetails by remember { mutableStateOf<TransactionEntity?>(null) }

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

                Surface(
                    color = NavyCard,
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Navy700),
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

        // --- 2. Top Hero Net Worth & Security Shield Banner ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(22.dp))
                    .testTag("net_worth_hero_card"),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(NavyCardLight, NavyCard)
                            )
                        )
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_bank_hero),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .align(Alignment.BottomEnd),
                        contentScale = ContentScale.Crop,
                        alpha = 0.15f
                    )
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Header row with Shield status
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                val animatedNetWorth by animateFloatAsState(
                                    targetValue = totalNetWorth.toFloat(),
                                    animationSpec = tween(durationMillis = 900),
                                    label = "netWorthAnim"
                                )
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Text(
                                        text = "TOTAL CONSOLIDATED NET WORTH",
                                        color = CyberCyan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.5.sp
                                    )
                                    PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 6.dp)
                                }
                                Text(
                                    text = "$${"%,.2f".format(animatedNetWorth.toDouble())}",
                                    color = TextWhite,
                                    fontSize = 32.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Surface(
                                color = if (blockedScamsCount > 0) CrimsonDanger.copy(alpha = 0.2f) else EmeraldSuccess.copy(alpha = 0.2f),
                                shape = RoundedCornerShape(20.dp),
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (blockedScamsCount > 0) CrimsonDanger.copy(alpha = 0.5f) else EmeraldSuccess.copy(alpha = 0.5f)
                                ),
                                modifier = Modifier.clickable { onNavigateSecurity() }
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Shield,
                                        contentDescription = "Shield Active",
                                        tint = if (blockedScamsCount > 0) CrimsonDanger else EmeraldSuccess,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = if (blockedScamsCount > 0) "$blockedScamsCount Threats Blocked" else "AI Shield Active",
                                        color = if (blockedScamsCount > 0) CrimsonDanger else EmeraldSuccess,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        // Income / Outflow summary bar
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(14.dp))
                                .background(Navy900.copy(alpha = 0.6f))
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceAround,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(EmeraldSuccess.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowDownward,
                                        contentDescription = "Income",
                                        tint = EmeraldSuccess,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column {
                                    Text("Cash Flow In", color = TextMuted, fontSize = 11.sp)
                                    Text("+$${"%,.2f".format(totalIncomeMonth)}", color = EmeraldSuccess, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }

                            Box(
                                modifier = Modifier
                                    .width(1.dp)
                                    .height(30.dp)
                                    .background(Navy700)
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(CyberCyan.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ArrowUpward,
                                        contentDescription = "Spent",
                                        tint = CyberCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                                Column {
                                    Text("Outflow", color = TextMuted, fontSize = 11.sp)
                                    Text("-$${"%,.2f".format(totalSpentMonth)}", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }
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
            }
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

        // --- 6. Recent Transactions with Risk Scores & Statements Button ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "LIVE TRANSACTION AUDIT FEED",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )
                Text(
                    text = "📄 Export PDF / CSV",
                    color = CyberCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onOpenStatements() }
                )
            }
        }

        if (transactions.isEmpty()) {
            item {
                Surface(
                    color = Navy900,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "No recent transactions found.",
                        color = TextMuted,
                        fontSize = 13.sp,
                        modifier = Modifier.padding(20.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        } else {
            items(transactions.take(8)) { tx ->
                TransactionRowItem(
                    tx = tx,
                    onFlagFraud = { viewModel.flagTransactionAsFraud(tx.id) },
                    onClick = { selectedTxForDetails = tx }
                )
            }
        }
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
