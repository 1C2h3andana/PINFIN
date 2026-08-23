package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DirectionsRun
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LockClock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.ModelTraining
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.R
import com.example.domain.ai.BankingAiAdvisor
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
import com.example.ui.viewmodel.ChatMessage
import com.example.ui.viewmodel.MessageSender
import kotlin.math.pow

enum class AgiAdvisorPage(val title: String, val icon: ImageVector, val tag: String) {
    DECADES("Decadal Behavior", Icons.Default.Memory, "page_decades"),
    GOALS("Life Goals Engine", Icons.Default.Flag, "page_goals"),
    PREDICTOR("Future Predictor", Icons.Default.Calculate, "page_predictor"),
    CAREER("Career Arbitrage", Icons.Default.Work, "page_career"),
    RETIREMENT("Retirement & FIRE", Icons.Default.LockClock, "page_retirement"),
    WEALTH("Wealth Strategies", Icons.Default.TrendingUp, "page_wealth"),
    CONTINUOUS_LEARNING("Neural Learning", Icons.Default.ModelTraining, "page_learning"),
    AI_CHAT("Cognitive Chat", Icons.Default.AutoAwesome, "page_chat")
}

@Composable
fun AiAdvisorScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    var currentPage by remember { mutableStateOf(AgiAdvisorPage.DECADES) }
    var showTicketModal by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // --- Top AGI Identity Header ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_ai_advisor),
                    contentDescription = "AGI Advisor Avatar",
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "AGI FINANCIAL PARTNER",
                            color = TextWhite,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                        PulsingStatusBadge(pulseColor = CyberCyan, badgeSize = 6.dp)
                    }
                    Text(
                        text = "Autonomous Cognitive Wealth Ecosystem",
                        color = CyberCyan,
                        fontSize = 10.sp
                    )
                }
            }

            Surface(
                color = NavyCardLight,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f)),
                modifier = Modifier.clickable { showTicketModal = true }.testTag("open_support_ticket_btn")
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.SupportAgent,
                        contentDescription = null,
                        tint = CyberCyan,
                        modifier = Modifier.size(15.dp)
                    )
                    Text("Ticket", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // --- Multi-Page Sub-Navigation Bar ---
        ScrollableTabRow(
            selectedTabIndex = AgiAdvisorPage.values().indexOf(currentPage),
            containerColor = NavyCard,
            contentColor = CyberCyan,
            edgePadding = 4.dp,
            indicator = { tabPositions ->
                val currentIndex = AgiAdvisorPage.values().indexOf(currentPage)
                if (currentIndex in tabPositions.indices) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[currentIndex]),
                        color = CyberCyan,
                        height = 3.dp
                    )
                }
            }
        ) {
            AgiAdvisorPage.values().forEach { page ->
                val isSelected = currentPage == page
                Tab(
                    selected = isSelected,
                    onClick = { currentPage = page },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = page.icon,
                                contentDescription = null,
                                tint = if (isSelected) CyberCyan else TextMuted,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(
                                text = page.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) CyberCyan else TextMuted
                            )
                        }
                    },
                    modifier = Modifier.testTag(page.tag)
                )
            }
        }

        // --- Animated Sub-Page Host ---
        AnimatedContent(
            targetState = currentPage,
            transitionSpec = {
                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width } + fadeOut()
                )
            },
            label = "AgiPageTransition",
            modifier = Modifier.weight(1f)
        ) { targetPage ->
            when (targetPage) {
                AgiAdvisorPage.DECADES -> PageDecadalBehavior()
                AgiAdvisorPage.GOALS -> PageLifeGoalsEngine()
                AgiAdvisorPage.PREDICTOR -> PageFuturePredictor()
                AgiAdvisorPage.CAREER -> PageCareerArbitrage()
                AgiAdvisorPage.RETIREMENT -> PageRetirementAndFire()
                AgiAdvisorPage.WEALTH -> PageWealthStrategies()
                AgiAdvisorPage.CONTINUOUS_LEARNING -> PageContinuousLearning()
                AgiAdvisorPage.AI_CHAT -> PageCognitiveChat(viewModel = viewModel)
            }
        }
    }

    if (showTicketModal) {
        CreateSupportTicketModal(
            onDismiss = { showTicketModal = false },
            onSubmit = { title, desc, cat ->
                viewModel.submitSupportTicket(title, desc, cat)
                showTicketModal = false
            }
        )
    }
}

// -------------------------------------------------------------
// PAGE 1: LEARN USER BEHAVIOR OVER DECADES (Decadal Behavioral Learner)
// -------------------------------------------------------------
@Composable
private fun PageDecadalBehavior() {
    var selectedDecadeIndex by remember { mutableIntStateOf(1) } // 0: 20s, 1: 30s (Current), 2: 40s, 3: 50s, 4: 60s+
    val decades = listOf("20s Early Career", "30s Accumulation (Active)", "40s Peak Earnings", "50s Financial Freedom", "60s+ Legacy & SWR")

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Memory, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(22.dp))
                            Text("Decadal Behavioral Modeling", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Surface(color = GoldAccent.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text("Decade Model Active", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Text(
                        text = "Tracks behavioral financial evolution across a 40+ year lifecycle, analyzing saving discipline, risk tolerance drift, and generational wealth compounding.",
                        color = TextWhite.copy(alpha = 0.85f),
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        // Decadal Selector
        item {
            Text("LIFELONG DECADE HORIZON", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(4.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(decades.size) { idx ->
                    val isSelected = selectedDecadeIndex == idx
                    Surface(
                        modifier = Modifier
                            .clickable { selectedDecadeIndex = idx }
                            .testTag("decade_chip_$idx"),
                        color = if (isSelected) CyberCyan.copy(alpha = 0.2f) else Navy900,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) CyberCyan else Navy700)
                    ) {
                        Text(
                            text = decades[idx],
                            color = if (isSelected) CyberCyan else TextWhite,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }
            }
        }

        // Detailed Decade Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    when (selectedDecadeIndex) {
                        0 -> {
                            DecadeItem("Early Career Foundations", "Eliminate student debts, establish a 6-month liquid buffer, and initiate $500/mo index compounding.", "$45,000 Target", EmeraldSuccess)
                            DecadeItem("High Risk-Tolerance Growth", "80% Equity / 20% Liquid allocation capturing early career compounding velocity.", "8.8% Expected Return", CyberCyan)
                        }
                        1 -> {
                            DecadeItem("Active Prime Wealth Accumulation", "Income scaling to $165k+; maxing 401(k), Backdoor Roth IRA, and HSA compounding.", "$340,000 Target", EmeraldSuccess)
                            DecadeItem("First Sovereign Property Equity", "Acquired 20% equity stake in primary property at 5.8% fixed rate.", "On Schedule", PurpleTech)
                            DecadeItem("Comprehensive Family Shield", "$1.5M Term Life & Umbrella liability policies active.", "100% Insured", GoldAccent)
                        }
                        2 -> {
                            DecadeItem("Executive & Venture Leadership", "Transitioning to equity-heavy consultancy or enterprise leadership.", "$1.25M Target", EmeraldSuccess)
                            DecadeItem("Children Education Trust 529", "Fully funded tax-free tuition vaults for university funding.", "96% Funded", CyberCyan)
                        }
                        3 -> {
                            DecadeItem("Financial Independence Transition", "Passive dividend & yield cashflow covers 100% of baseline living costs.", "$2.85M Target", GoldAccent)
                            DecadeItem("Healthcare Pre-Funding Vault", "$150,000 liquid buffer mitigating unexpected medical inflation.", "Protected", AmberOrange)
                        }
                        else -> {
                            DecadeItem("Generational Trust & SWR", "Operating on a 3.75% Safe Withdrawal Rate (SWR) with capital preservation.", "$4.6M Multi-Gen Vault", EmeraldSuccess)
                            DecadeItem("Philanthropic & Estate Sealing", "Living revocable trusts with zero estate probate latency.", "Legally Sealed", PurpleTech)
                        }
                    }
                }
            }
        }

        // Long-term Behavioral Drift Metric
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("LEARNED BEHAVIOR PATTERNS (PAST 10 YEARS)", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    BehaviorMetricRow("Monthly Savings Discipline", "38.2% surplus rate (Top 5% peer benchmark)", EmeraldSuccess)
                    BehaviorMetricRow("Impulse Spending Suppression", "94% resistance to non-essential late-night transactions", CyberCyan)
                    BehaviorMetricRow("Inflation Drag Resistance", "+4.2% real yield outperforming national CPI", GoldAccent)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// PAGE 2: UNDERSTAND LIFE GOALS (Life Goals Cognitive Engine)
// -------------------------------------------------------------
@Composable
private fun PageLifeGoalsEngine() {
    var showAddGoalDialog by remember { mutableStateOf(false) }

    data class LifeGoalItem(val title: String, val category: String, val targetAmount: Double, val currentSaved: Double, val targetYear: Int, val icon: ImageVector, val color: Color)
    val goals = remember {
        mutableStateListOf(
            LifeGoalItem("Primary Residence Down Payment", "Real Estate", 85000.0, 62000.0, 2027, Icons.Default.AccountBalance, CyberCyan),
            LifeGoalItem("Children Ivy League Education", "Education Trust", 180000.0, 115000.0, 2033, Icons.Default.School, PurpleTech),
            LifeGoalItem("Financial Independence (FIRE Vault)", "Retirement", 2500000.0, 680000.0, 2040, Icons.Default.LockClock, EmeraldSuccess),
            LifeGoalItem("Global Sabbatical & World Travel", "Lifestyle", 35000.0, 28000.0, 2026, Icons.Default.DirectionsRun, GoldAccent)
        )
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Flag, contentDescription = null, tint = CyberCyan)
                            Text("Life Goals Intelligence", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Button(
                            onClick = { showAddGoalDialog = true },
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("add_life_goal_btn")
                        ) {
                            Text("+ New Goal", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        }
                    }
                    Text(
                        text = "The AGI continuously balances competing life priorities, dynamically reallocating surplus cashflow to guarantee critical milestones are reached without financial strain.",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
            }
        }

        items(goals) { goal ->
            val progress = (goal.currentSaved / goal.targetAmount).toFloat().coerceIn(0f, 1f)
            val monthlyNeeded = ((goal.targetAmount - goal.currentSaved) / ((goal.targetYear - 2026).coerceAtLeast(1) * 12)).coerceAtLeast(0.0)

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, goal.color.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(
                                modifier = Modifier.size(32.dp).clip(CircleShape).background(goal.color.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(goal.icon, contentDescription = null, tint = goal.color, modifier = Modifier.size(18.dp))
                            }
                            Column {
                                Text(goal.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                Text("${goal.category} • Target Year: ${goal.targetYear}", color = TextMuted, fontSize = 10.sp)
                            }
                        }
                        Surface(color = goal.color.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text("${(progress * 100).toInt()}% Funded", color = goal.color, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                        }
                    }

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                        color = goal.color,
                        trackColor = Navy700
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("$${"%,.0f".format(goal.currentSaved)} / $${"%,.0f".format(goal.targetAmount)}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Text("Required: $${"%,.0f".format(monthlyNeeded)}/mo", color = CyberCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    if (showAddGoalDialog) {
        AlertDialog(
            onDismissRequest = { showAddGoalDialog = false },
            title = { Text("Add New Life Milestone", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp) },
            text = {
                Text("Select milestone category to connect to your AGI autonomous funding ledger (e.g. Sovereign Property, Venture Seed, Education Vault).", color = TextMuted, fontSize = 12.sp)
            },
            confirmButton = {
                Button(
                    onClick = { showAddGoalDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
                ) {
                    Text("Add Milestone", color = Navy900, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showAddGoalDialog = false }) { Text("Cancel", color = TextMuted) }
            },
            containerColor = Navy800
        )
    }
}

// -------------------------------------------------------------
// PAGE 3: PREDICT FUTURE FINANCIAL SITUATIONS (Predictor & Simulator)
// -------------------------------------------------------------
@Composable
private fun PageFuturePredictor() {
    var monthlySavings by remember { mutableDoubleStateOf(1400.0) }
    var expectedReturn by remember { mutableDoubleStateOf(9.2) }
    var inflationRate by remember { mutableDoubleStateOf(3.4) }
    var stressScenarioIndex by remember { mutableIntStateOf(0) }

    val stressScenarios = listOf(
        "Baseline Normal Growth",
        "Macro Stagflation (+6% CPI)",
        "Severe Market Drawdown (-25%)",
        "Healthcare Emergency ($15k Shock)"
    )

    val realRate = (expectedReturn - inflationRate).coerceAtLeast(0.5) / 100.0 / 12.0
    val fiveYearTotal = monthlySavings * (((1 + realRate).pow(60.0) - 1) / realRate)
    val tenYearTotal = monthlySavings * (((1 + realRate).pow(120.0) - 1) / realRate)
    val twentyYearTotal = monthlySavings * (((1 + realRate).pow(240.0) - 1) / realRate)

    val solvencyScore = ((monthlySavings / 1600.0) * 85 + (realRate * 2000)).coerceIn(40.0, 99.0).toInt()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PurpleTech.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Calculate, contentDescription = null, tint = PurpleTech)
                            Text("Predictive Future Intelligence", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Surface(
                            color = if (solvencyScore >= 80) EmeraldSuccess.copy(alpha = 0.2f) else AmberOrange.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text("Solvency: $solvencyScore/100", color = if (solvencyScore >= 80) EmeraldSuccess else AmberOrange, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Text("Simulate multi-decade financial trajectories and stress test your wealth runway under changing macroeconomic factors.", color = TextMuted, fontSize = 11.sp)
                }
            }
        }

        // Stress Scenario Chips
        item {
            Text("SELECT STRESS CONDITION", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(4.dp))
            LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                items(stressScenarios.size) { idx ->
                    val isSelected = stressScenarioIndex == idx
                    Surface(
                        modifier = Modifier.clickable { stressScenarioIndex = idx },
                        color = if (isSelected) PurpleTech.copy(alpha = 0.2f) else Navy900,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) PurpleTech else Navy700)
                    ) {
                        Text(stressScenarios[idx], color = if (isSelected) PurpleTech else TextWhite, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                    }
                }
            }
        }

        // Interactive Sliders
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("SIMULATION PARAMETERS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Monthly Savings Allocation", color = TextMuted, fontSize = 10.sp)
                            Text("$${"%,.0f".format(monthlySavings)}/mo", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                        Slider(
                            value = monthlySavings.toFloat(),
                            onValueChange = { monthlySavings = it.toDouble() },
                            valueRange = 300f..10000f,
                            steps = 38,
                            colors = SliderDefaults.colors(thumbColor = CyberCyan, activeTrackColor = CyberCyan, inactiveTrackColor = Navy700)
                        )
                    }

                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Portfolio Annual Real APY", color = TextMuted, fontSize = 10.sp)
                            Text("${"%.1f".format(expectedReturn)}%", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                        Slider(
                            value = expectedReturn.toFloat(),
                            onValueChange = { expectedReturn = it.toDouble() },
                            valueRange = 3f..18f,
                            steps = 30,
                            colors = SliderDefaults.colors(thumbColor = EmeraldSuccess, activeTrackColor = EmeraldSuccess, inactiveTrackColor = Navy700)
                        )
                    }
                }
            }
        }

        // Projected Multi-Horizon Outlays
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                FutureCard("5-YEAR", "$${"%,.0f".format(fiveYearTotal)}", CyberCyan, Modifier.weight(1f))
                FutureCard("10-YEAR", "$${"%,.0f".format(tenYearTotal)}", EmeraldSuccess, Modifier.weight(1f))
                FutureCard("20-YEAR", "$${"%,.0f".format(twentyYearTotal)}", GoldAccent, Modifier.weight(1f))
            }
        }
    }
}

@Composable
private fun FutureCard(horizon: String, amount: String, color: Color, modifier: Modifier) {
    Surface(
        color = Navy900,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.6f)),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(horizon, color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text(amount, color = color, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
            Text("Real Power", color = TextMuted, fontSize = 8.sp)
        }
    }
}

// -------------------------------------------------------------
// PAGE 4: RECOMMEND CAREER CHANGES (Autonomous Career Arbitrage)
// -------------------------------------------------------------
@Composable
private fun PageCareerArbitrage() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Work, contentDescription = null, tint = EmeraldSuccess)
                            Text("AGI Career & Labor Arbitrage", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text("High ROI Identified", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Text("The AGI evaluates global talent markets and recommends strategic career pivots that maximize your 20-year net worth.", color = TextMuted, fontSize = 11.sp)
                }
            }
        }

        item {
            CareerRecommendationCard(
                title = "Principal AI Systems Architect",
                compensationDelta = "+$68,000 / year",
                timeToTransition = "6 - 9 Months",
                skillFit = "94% Compatibility",
                lifetimeUpside = "+$940,000 (20-Year Compounded)",
                statusColor = EmeraldSuccess,
                requiredSkills = listOf("Distributed LLM Orchestration", "Rust/C++ Kernel Optimization", "Zero-Trust Infrastructure")
            )
        }

        item {
            CareerRecommendationCard(
                title = "Fintech Co-Founder / Equity Lead",
                compensationDelta = "+$150,000 / year Upside",
                timeToTransition = "12 - 18 Months",
                skillFit = "88% Compatibility",
                lifetimeUpside = "+$2.4M (Equity Realization)",
                statusColor = GoldAccent,
                requiredSkills = listOf("Sovereign Treasury Management", "Regulatory Compliance", "Institutional Fundraising")
            )
        }

        item {
            CareerRecommendationCard(
                title = "Senior Quantitative Strategy Lead",
                compensationDelta = "+$55,000 / year",
                timeToTransition = "3 - 6 Months",
                skillFit = "91% Compatibility",
                lifetimeUpside = "+$780,000 (20-Year Compounded)",
                statusColor = CyberCyan,
                requiredSkills = listOf("High-Frequency Execution", "Bayesian Risk Modeling", "Order Flow Analytics")
            )
        }
    }
}

@Composable
private fun CareerRecommendationCard(
    title: String,
    compensationDelta: String,
    timeToTransition: String,
    skillFit: String,
    lifetimeUpside: String,
    statusColor: Color,
    requiredSkills: List<String>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Navy900),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Surface(color = statusColor.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                    Text(compensationDelta, color = statusColor, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Transition Time: $timeToTransition", color = TextMuted, fontSize = 10.sp)
                Text(skillFit, color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 10.sp)
            }
            Text("20-Year Compounded Upside: $lifetimeUpside", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 11.sp)

            Text("Key Focus Skills:", color = TextMuted, fontSize = 10.sp)
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                requiredSkills.forEach { skill ->
                    Surface(color = NavyCard, shape = RoundedCornerShape(6.dp)) {
                        Text(skill, color = TextWhite.copy(alpha = 0.8f), fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// PAGE 5: PREDICT RETIREMENT NEEDS (Retirement & FIRE Forecaster)
// -------------------------------------------------------------
@Composable
private fun PageRetirementAndFire() {
    var targetRetirementAge by remember { mutableIntStateOf(52) }
    var desiredMonthlySpend by remember { mutableDoubleStateOf(7500.0) }
    var currentAge by remember { mutableIntStateOf(32) }

    val yearsToRetire = (targetRetirementAge - currentAge).coerceAtLeast(1)
    val annualSpend = desiredMonthlySpend * 12.0
    // 3.8% SWR (Safe Withdrawal Rate)
    val requiredNestEgg = annualSpend / 0.038

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.LockClock, contentDescription = null, tint = GoldAccent)
                            Text("Retirement & FIRE Solver", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Surface(color = GoldAccent.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text("Target: Age $targetRetirementAge", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Text("Calculates precise capital required for perpetual financial independence, incorporating healthcare buffers and dynamic safe withdrawal rates.", color = TextMuted, fontSize = 11.sp)
                }
            }
        }

        // Summary Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("REQUIRED PERPETUAL FIRE NEST EGG", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("$${"%,.0f".format(requiredNestEgg)}", color = GoldAccent, fontWeight = FontWeight.ExtraBold, fontSize = 24.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Years Remaining: $yearsToRetire yrs", color = TextWhite, fontSize = 11.sp)
                        Text("Safe Withdrawal Rate: 3.8% SWR", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }

        // Dials
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("RETIREMENT CONFIGURATION", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Target Retirement Age", color = TextMuted, fontSize = 10.sp)
                            Text("Age $targetRetirementAge", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                        Slider(
                            value = targetRetirementAge.toFloat(),
                            onValueChange = { targetRetirementAge = it.toInt() },
                            valueRange = 40f..70f,
                            steps = 30,
                            colors = SliderDefaults.colors(thumbColor = CyberCyan, activeTrackColor = CyberCyan, inactiveTrackColor = Navy700)
                        )
                    }

                    Column {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Desired Post-Retirement Monthly Cashflow", color = TextMuted, fontSize = 10.sp)
                            Text("$${"%,.0f".format(desiredMonthlySpend)}/mo", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                        Slider(
                            value = desiredMonthlySpend.toFloat(),
                            onValueChange = { desiredMonthlySpend = it.toDouble() },
                            valueRange = 3000f..25000f,
                            steps = 44,
                            colors = SliderDefaults.colors(thumbColor = EmeraldSuccess, activeTrackColor = EmeraldSuccess, inactiveTrackColor = Navy700)
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// PAGE 6: BUILD PERSONALIZED WEALTH STRATEGIES
// -------------------------------------------------------------
@Composable
private fun PageWealthStrategies() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = CyberCyan)
                            Text("Personalized Wealth Blueprint", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Surface(color = CyberCyan.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text("Auto-Rebalancing ON", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Text("AI-optimized multi-asset allocation tailored to your risk tolerance, liquidity needs, and tax-loss harvesting opportunities.", color = TextMuted, fontSize = 11.sp)
                }
            }
        }

        // Asset Allocation Pillars
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("OPTIMAL PORTFOLIO ALLOCATION", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    AllocationRow("Global Total Market Equities (VT/VTI)", "45%", EmeraldSuccess)
                    AllocationRow("High-Yield ESG & Treasury Vaults", "25%", CyberCyan)
                    AllocationRow("Tokenized Sovereign Real Estate", "15%", PurpleTech)
                    AllocationRow("High-Growth AI & Clean Energy Ventures", "10%", GoldAccent)
                    AllocationRow("Liquid Emergency Buffer (5.2% APY)", "5%", AmberOrange)
                }
            }
        }

        // Tax Harvesting & Yield Compounding
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                        Text("Automated Tax-Loss Harvesting", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Text("The engine automatically offsets short-term capital gains by harvesting non-correlated asset drawdowns, saving an estimated $3,450 annually in tax drag.", color = TextMuted, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun AllocationRow(title: String, percent: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(color))
            Text(title, color = TextWhite, fontSize = 11.sp)
        }
        Text(percent, color = color, fontWeight = FontWeight.Bold, fontSize = 11.sp)
    }
}

// -------------------------------------------------------------
// PAGE 7: LEARN CONTINUOUSLY (Neural Learning & Feedback)
// -------------------------------------------------------------
@Composable
private fun PageContinuousLearning() {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(bottom = 20.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.ModelTraining, contentDescription = null, tint = CyberCyan)
                            Text("Continuous On-Device Neural Learning", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Surface(color = CyberCyan.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text("Federated Learning Active", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Text("The AI continuously trains on your private financial transactions without exposing sensitive data, continuously refining budgeting, debt reduction, and investment timing.", color = TextMuted, fontSize = 11.sp)
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("REAL-TIME LEARNING LOG", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    LearningLogEntry("Behavior Reinforcement", "Detected consistent 40% surplus savings; increased index vault compounding allocation.", "2h ago", EmeraldSuccess)
                    LearningLogEntry("Impulse Risk Mitigation", "Identified high-velocity shopping spikes on weekend evenings; enabled friction prompt.", "1d ago", AmberOrange)
                    LearningLogEntry("Macro Inflation Sync", "Calibrated living expense models with latest 3.1% national CPI data.", "3d ago", CyberCyan)
                    LearningLogEntry("Tax Arbitrage Adaptation", "Indexed updated IRS 401(k) and IRA limits into future projection engines.", "5d ago", PurpleTech)
                }
            }
        }
    }
}

@Composable
private fun LearningLogEntry(title: String, desc: String, timestamp: String, color: Color) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(title, color = color, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            Text(timestamp, color = TextMuted, fontSize = 9.sp)
        }
        Text(desc, color = TextWhite.copy(alpha = 0.85f), fontSize = 10.sp)
    }
}

// -------------------------------------------------------------
// PAGE 8: COGNITIVE CONCIERGE & CHAT
// -------------------------------------------------------------
@Composable
private fun PageCognitiveChat(viewModel: BankViewModel) {
    val chatMessages by viewModel.chatMessages.collectAsStateWithLifecycle()
    var inputText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Preset Prompts
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            items(BankingAiAdvisor.PRESET_PROMPTS) { prompt ->
                Surface(
                    color = NavyCard,
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Navy700),
                    modifier = Modifier
                        .clickable { viewModel.sendChatMessage(prompt) }
                        .testTag("preset_prompt_${prompt.take(10)}")
                ) {
                    Text(
                        text = prompt,
                        color = TextWhite,
                        fontSize = 11.sp,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Messages List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            contentPadding = PaddingValues(vertical = 6.dp)
        ) {
            items(chatMessages) { message ->
                ChatMessageBubble(message = message)
            }
        }

        // Input Box
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = { Text("Ask your AGI Lifelong Partner anything...", color = TextMuted, fontSize = 12.sp) },
                modifier = Modifier
                    .weight(1f)
                    .testTag("ai_chat_input_field"),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = Navy700,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                ),
                shape = RoundedCornerShape(14.dp),
                singleLine = true
            )

            IconButton(
                onClick = {
                    if (inputText.isNotBlank()) {
                        viewModel.sendChatMessage(inputText)
                        inputText = ""
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(CyberCyan)
                    .testTag("send_ai_prompt_btn")
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = "Send", tint = Navy900)
            }
        }
    }
}

// -------------------------------------------------------------
// HELPER COMPOSABLES
// -------------------------------------------------------------
@Composable
private fun DecadeItem(title: String, desc: String, tag: String, tagColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Text(desc, color = TextMuted, fontSize = 10.sp)
        }
        Surface(color = tagColor.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
            Text(tag, color = tagColor, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
        }
    }
}

@Composable
private fun BehaviorMetricRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = TextMuted, fontSize = 11.sp)
        Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 11.sp)
    }
}

@Composable
private fun ChatMessageBubble(message: ChatMessage) {
    val isUser = message.sender == MessageSender.USER
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!isUser) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(CyberCyan.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.width(8.dp))
        }

        Surface(
            color = if (isUser) CyberCyan else NavyCard,
            shape = RoundedCornerShape(
                topStart = 16.dp,
                topEnd = 16.dp,
                bottomStart = if (isUser) 16.dp else 4.dp,
                bottomEnd = if (isUser) 4.dp else 16.dp
            ),
            border = if (!isUser) androidx.compose.foundation.BorderStroke(1.dp, Navy700) else null,
            modifier = Modifier.fillMaxWidth(0.85f)
        ) {
            Text(
                text = message.text,
                color = if (isUser) Navy900 else TextWhite,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}

@Composable
private fun CreateSupportTicketModal(
    onDismiss: () -> Unit,
    onSubmit: (String, String, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("Fraud & Security") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(imageVector = Icons.Default.SupportAgent, contentDescription = null, tint = CyberCyan)
                Text("Open Priority Support Ticket", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Submit an incident or inquiry directly to banking compliance specialists:", color = TextMuted, fontSize = 11.sp)

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Issue Title") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("ticket_title_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )

                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Detailed Description") },
                    modifier = Modifier.fillMaxWidth().height(100.dp).testTag("ticket_desc_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (title.isNotBlank() && description.isNotBlank()) {
                        onSubmit(title, description, category)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                modifier = Modifier.testTag("submit_ticket_btn")
            ) {
                Text("Submit Ticket", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextMuted, fontSize = 12.sp) }
        },
        containerColor = Navy800
    )
}
