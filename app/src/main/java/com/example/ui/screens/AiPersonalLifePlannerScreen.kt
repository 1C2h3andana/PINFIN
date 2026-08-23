package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BabyChangingStation
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.EscalatorWarning
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.HistoryEdu
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

enum class LifePlannerPage(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val tag: String
) {
    CAREER("Career Planning", Icons.Default.Work, CyberCyan, "tab_lp_career"),
    EDUCATION("Education", Icons.Default.School, PurpleTech, "tab_lp_education"),
    HEALTHCARE("Healthcare Expenses", Icons.Default.LocalHospital, CrimsonDanger, "tab_lp_healthcare"),
    MARRIAGE("Marriage Planning", Icons.Default.Favorite, AmberOrange, "tab_lp_marriage"),
    CHILDREN_EDU("Children's Education", Icons.Default.ChildCare, ElectricBlue, "tab_lp_children"),
    RETIREMENT("Retirement & FIRE", Icons.Default.Savings, EmeraldSuccess, "tab_lp_retirement"),
    ESTATE("Estate & Legacy", Icons.Default.HistoryEdu, GoldAccent, "tab_lp_estate")
}

@Composable
fun AiPersonalLifePlannerScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    var selectedPage by remember { mutableStateOf(LifePlannerPage.CAREER) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // --- Header Banner ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, PurpleTech.copy(alpha = 0.6f))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(PurpleTech.copy(alpha = 0.2f)),
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
                        Text(
                            text = "AI PERSONAL LIFE PLANNER",
                            color = TextWhite,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                        PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 6.dp)
                    }
                    Text(
                        text = "Holistic lifecycle management: Career, Health, Marriage, Kids, Retirement & Estate.",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }

        // --- 7 Separate Pages Scrollable Tab Navigation ---
        ScrollableTabRow(
            selectedTabIndex = LifePlannerPage.values().indexOf(selectedPage),
            containerColor = NavyCard,
            contentColor = PurpleTech,
            edgePadding = 4.dp,
            indicator = { tabPositions ->
                val idx = LifePlannerPage.values().indexOf(selectedPage)
                if (idx in tabPositions.indices) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[idx]),
                        color = selectedPage.color,
                        height = 3.dp
                    )
                }
            }
        ) {
            LifePlannerPage.values().forEach { page ->
                val isSelected = selectedPage == page
                Tab(
                    selected = isSelected,
                    onClick = { selectedPage = page },
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
                                text = page.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) page.color else TextMuted
                            )
                        }
                    },
                    modifier = Modifier.testTag(page.tag)
                )
            }
        }

        // --- Animated Page Host ---
        AnimatedContent(
            targetState = selectedPage,
            transitionSpec = {
                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width } + fadeOut()
                )
            },
            label = "LifePlannerPageTransition",
            modifier = Modifier.weight(1f)
        ) { targetPage ->
            when (targetPage) {
                LifePlannerPage.CAREER -> PageLpCareer()
                LifePlannerPage.EDUCATION -> PageLpEducation()
                LifePlannerPage.HEALTHCARE -> PageLpHealthcare()
                LifePlannerPage.MARRIAGE -> PageLpMarriage()
                LifePlannerPage.CHILDREN_EDU -> PageLpChildrenEducation()
                LifePlannerPage.RETIREMENT -> PageLpRetirement()
                LifePlannerPage.ESTATE -> PageLpEstate()
            }
        }
    }
}

// -------------------------------------------------------------
// PAGE 1: CAREER PLANNING
// -------------------------------------------------------------
@Composable
private fun PageLpCareer() {
    var targetSalary by remember { mutableFloatStateOf(280000f) }

    val careerMilestones = remember {
        listOf(
            RoadmapMilestone("Age 28", "2024", "Senior AI Systems Engineer", "Mastered distributed training and RAG architecture.", Icons.Default.CheckCircle, EmeraldSuccess, 1.0f, "Completed (100%)", "+$40k Base Salary"),
            RoadmapMilestone("Age 30", "2026", "Staff AI Infrastructure Lead", "Lead enterprise cross-cloud agentic orchestration.", Icons.Default.Work, CyberCyan, 0.72f, "In Progress (72%)", "+$65k Total Comp"),
            RoadmapMilestone("Age 33", "2029", "VP of Engineering & Autonomous Systems", "Scale 100+ engineer organization with P&L authority.", Icons.Default.TrendingUp, PurpleTech, 0.35f, "Target Track (35%)", "+$140k + Equity"),
            RoadmapMilestone("Age 36", "2032", "Chief Technology Officer / Co-Founder", "Lead global financial intelligence enterprise.", Icons.Default.MilitaryTech, GoldAccent, 0.15f, "Long-Range (15%)", "Equity Liquidity Event")
        )
    }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            PlannerSectionHeader(
                title = "AI Career & Leadership Roadmap",
                subtitle = "Cognitive market trajectory, high-value skill arbitrage, and promotion milestones.",
                icon = Icons.Default.Work,
                color = CyberCyan
            )
        }
        item {
            TimelineRoadmapCard(
                title = "Executive Career Milestone Timeline",
                milestones = careerMilestones
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Target Annual Compensation Projection", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Goal Compensation:", color = TextMuted, fontSize = 11.sp)
                        Text("$${targetSalary.toInt().formatWithCommas()} / yr", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Slider(
                        value = targetSalary,
                        onValueChange = { targetSalary = it },
                        valueRange = 120000f..500000f,
                        colors = SliderDefaults.colors(thumbColor = CyberCyan, activeTrackColor = CyberCyan)
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Text("OPTIMAL CAREER PIVOTS & MILESTONES", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)

                    MilestoneRow("Q3 2026", "Staff AI Infrastructure Engineer", "+$35k / yr Delta", EmeraldSuccess)
                    MilestoneRow("Q1 2028", "VP of Autonomous Systems", "+$85k / yr Delta", CyberCyan)
                    MilestoneRow("Q4 2030", "Chief Technology Officer / Founder", "+$160k / yr Delta + Equity", GoldAccent)
                }
            }
        }
        item {
            PlannerMetricCard(
                title = "High-Value Skill Gap Analysis",
                badge = "Top 1% Demand",
                badgeColor = EmeraldSuccess,
                items = listOf(
                    "Distributed LLM Inference Architecture" to "96% Match • Active Focus",
                    "Autonomous Agent Orchestration (LangGraph/K8s)" to "92% Match • Completed",
                    "C-Suite Board Governance & Enterprise P&L" to "74% Match • Recommended"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 2: EDUCATION
// -------------------------------------------------------------
@Composable
private fun PageLpEducation() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            PlannerSectionHeader(
                title = "Continuous Lifelong Learning & Upskilling",
                subtitle = "Executive programs, high-ROI certifications, and academic degree financing.",
                icon = Icons.Default.School,
                color = PurpleTech
            )
        }
        item {
            PlannerMetricCard(
                title = "Active Executive Learning Track",
                badge = "85% Funded by Employer",
                badgeColor = CyberCyan,
                items = listOf(
                    "Program" to "Executive MBA / Advanced AI Strategy (MIT Sloan)",
                    "Total Tuition" to "$92,000 (Employer Coverage: $78,200)",
                    "Out-of-Pocket Cost" to "$13,800 (Paid via Education Tax Deduction)",
                    "Projected Lifetime ROI" to "+$1,420,000 Lifetime Earnings"
                )
            )
        }
        item {
            PlannerMetricCard(
                title = "Quarterly Micro-Certifications",
                badge = "Next Exam: Nov 2026",
                badgeColor = EmeraldSuccess,
                items = listOf(
                    "AWS Certified Solutions Architect (Professional)" to "Active (Renewed 2025)",
                    "CFA Level III Candidate" to "Registered (Exam in 3 Months)",
                    "Quantum Computing Principles (IBM Q)" to "Completed • Grade: 98%"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 3: HEALTHCARE EXPENSES
// -------------------------------------------------------------
@Composable
private fun PageLpHealthcare() {
    var hsaContribution by remember { mutableFloatStateOf(4300f) }
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            PlannerSectionHeader(
                title = "Lifetime Healthcare & Longevity Fund",
                subtitle = "Tax-free HSA compound growth, critical illness buffers, and preventative care.",
                icon = Icons.Default.LocalHospital,
                color = CrimsonDanger
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Triple Tax-Advantaged HSA Optimizer", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Annual HSA Contribution:", color = TextMuted, fontSize = 11.sp)
                        Text("$${hsaContribution.toInt().formatWithCommas()} / yr (Max)", color = CrimsonDanger, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Slider(
                        value = hsaContribution,
                        onValueChange = { hsaContribution = it },
                        valueRange = 1000f..8300f,
                        colors = SliderDefaults.colors(thumbColor = CrimsonDanger, activeTrackColor = CrimsonDanger)
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Projected HSA Balance at Age 65:", color = TextMuted, fontSize = 11.sp)
                        val projectedHsa = (hsaContribution * 28 * 2.4).toInt()
                        Text("$$projectedHsa (Tax Free)", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
        item {
            PlannerMetricCard(
                title = "Medical Catastrophe & Longevity Reserves",
                badge = "100% Protected",
                badgeColor = EmeraldSuccess,
                items = listOf(
                    "Emergency Medical Out-of-Pocket Cap" to "$6,500 / yr (Fully Funded)",
                    "Long-Term Care Parametric Shield" to "$500,000 Benefit Pool",
                    "Preventative Diagnostics (Full Body MRI)" to "Annual Coverage Included"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 4: MARRIAGE PLANNING
// -------------------------------------------------------------
@Composable
private fun PageLpMarriage() {
    var weddingBudget by remember { mutableFloatStateOf(35000f) }
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            PlannerSectionHeader(
                title = "Marriage & Joint Financial Union",
                subtitle = "Wedding expense roadmap, joint liquidity merge, and prenuptial asset structure.",
                icon = Icons.Default.Favorite,
                color = AmberOrange
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Target Wedding & Honeymoon Allocation", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Allocated Budget:", color = TextMuted, fontSize = 11.sp)
                        Text("$${weddingBudget.toInt().formatWithCommas()}", color = AmberOrange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Slider(
                        value = weddingBudget,
                        onValueChange = { weddingBudget = it },
                        valueRange = 10000f..100000f,
                        colors = SliderDefaults.colors(thumbColor = AmberOrange, activeTrackColor = AmberOrange)
                    )

                    Text("MONTHLY SAVINGS REQUIRED (18 MONTH HORIZON)", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    val monthlyRequired = (weddingBudget / 18).toInt()
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Monthly Joint Contribution:", color = TextMuted, fontSize = 11.sp)
                        Text("$$monthlyRequired / mo", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
        item {
            PlannerMetricCard(
                title = "Dual-Income Household Optimization",
                badge = "Tax Optimized",
                badgeColor = CyberCyan,
                items = listOf(
                    "Combined Household Net Income" to "$345,000 / yr",
                    "Joint Tax Filing Benefit" to "+$4,800 Annual Tax Savings",
                    "Prenuptial Asset Segregation" to "Cryptographic Trust Active"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 5: CHILDREN'S EDUCATION
// -------------------------------------------------------------
@Composable
private fun PageLpChildrenEducation() {
    var monthly529 by remember { mutableFloatStateOf(650f) }
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            PlannerSectionHeader(
                title = "Children's 529 College & Private Education",
                subtitle = "Inflation-adjusted tuition projections, 529 compound investing, and early trust setup.",
                icon = Icons.Default.ChildCare,
                color = ElectricBlue
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("529 Education Savings Simulator", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Monthly Contribution / Child:", color = TextMuted, fontSize = 11.sp)
                        Text("$${monthly529.toInt().formatWithCommas()} / mo", color = ElectricBlue, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Slider(
                        value = monthly529,
                        onValueChange = { monthly529 = it },
                        valueRange = 200f..2500f,
                        colors = SliderDefaults.colors(thumbColor = ElectricBlue, activeTrackColor = ElectricBlue)
                    )

                    val projectedCollegeFund = (monthly529 * 12 * 18 * 2.1).toInt()
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Projected Fund at Age 18 (7% APY):", color = TextMuted, fontSize = 11.sp)
                        Text("$$projectedCollegeFund", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
        item {
            PlannerMetricCard(
                title = "Tuition Inflation Benchmark (Class of 2042)",
                badge = "4-Year In-State & Ivy League",
                badgeColor = PurpleTech,
                items = listOf(
                    "Top 20 Private University Estimated Cost" to "$280,000 (Covered: 88%)",
                    "Top Public In-State University" to "$125,000 (Covered: 100%)",
                    "Unused 529 Rollover to Roth IRA" to "$35,000 Lifetime Cap Eligible"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 6: RETIREMENT & FIRE
// -------------------------------------------------------------
@Composable
private fun PageLpRetirement() {
    var targetFireAge by remember { mutableFloatStateOf(52f) }

    val fireMilestones = remember {
        listOf(
            RoadmapMilestone("Age 35", "2031", "Coast FIRE Milestone ($750k)", "Compound interest covers base retirement expenses.", Icons.Default.Savings, CyberCyan, 1.0f, "Achieved (100%)", "$32,000 / yr Growth"),
            RoadmapMilestone("Age 42", "2038", "Lean FIRE Milestone ($1.5M)", "Covers all basic living expenses without earned income.", Icons.Default.TrendingUp, ElectricBlue, 0.65f, "On Track (65%)", "$60,000 / yr SWR"),
            RoadmapMilestone("Age 48", "2044", "Chubby FIRE Vault ($2.8M)", "Enables luxury travel, philanthropy, and family security.", Icons.Default.AccountBalance, PurpleTech, 0.38f, "Compounding (38%)", "$112,000 / yr SWR"),
            RoadmapMilestone("Age 52", "2048", "Fat FIRE & Generational Freedom ($4.2M)", "Full multi-generational trust endowment with perpetual liquidity.", Icons.Default.MilitaryTech, EmeraldSuccess, 0.20f, "Target Horizon (20%)", "$168,000 / yr SWR")
        )
    }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            PlannerSectionHeader(
                title = "Retirement & Financial Independence (FIRE)",
                subtitle = "Safe withdrawal rates (3.75%), multi-decade cashflow runway, and pension arbitrage.",
                icon = Icons.Default.Savings,
                color = EmeraldSuccess
            )
        }
        item {
            TimelineRoadmapCard(
                title = "Decadal FIRE Milestone Roadmap",
                milestones = fireMilestones
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("Target Retirement Age (FIRE)", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Target Age:", color = TextMuted, fontSize = 11.sp)
                        Text("Age ${targetFireAge.toInt()}", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Slider(
                        value = targetFireAge,
                        onValueChange = { targetFireAge = it },
                        valueRange = 40f..70f,
                        colors = SliderDefaults.colors(thumbColor = EmeraldSuccess, activeTrackColor = EmeraldSuccess)
                    )

                    val targetCorpus = 2850000 + (65 - targetFireAge.toInt()) * 95000
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Required FIRE Nest Egg:", color = TextMuted, fontSize = 11.sp)
                        Text("$${targetCorpus.formatWithCommas()}", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
        item {
            PlannerMetricCard(
                title = "Decadal Safe Withdrawal Rates",
                badge = "Monte Carlo 99.4%",
                badgeColor = EmeraldSuccess,
                items = listOf(
                    "Annual Passive Cashflow (3.75% SWR)" to "$106,875 / yr",
                    "Social Security Delay Arbitrage (Age 70)" to "+24% Boost ($4,120 / mo)",
                    "Post-Retirement Tax Bracket" to "12% Effective Rate"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 7: ESTATE & LEGACY
// -------------------------------------------------------------
@Composable
private fun PageLpEstate() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            PlannerSectionHeader(
                title = "Estate Planning & Generational Legacy",
                subtitle = "Revocable living trusts, probate avoidance, digital asset wills, and tax shielding.",
                icon = Icons.Default.HistoryEdu,
                color = GoldAccent
            )
        }
        item {
            PlannerMetricCard(
                title = "Revocable Living Trust & Probate Shield",
                badge = "100% Legally Sealed",
                badgeColor = EmeraldSuccess,
                items = listOf(
                    "Trust Structure" to "The Sovereign Family Living Trust (2026)",
                    "Probate Avoidance" to "Immediate 0-Day Asset Transfer",
                    "Protected Estate Value" to "$3,150,000 Total Assets",
                    "Primary Beneficiaries" to "2 Named Heirs (Equal 50/50 Allocation)"
                )
            )
        }
        item {
            PlannerMetricCard(
                title = "Autonomous Digital Asset Inheritance",
                badge = "Multi-Sig Protected",
                badgeColor = CyberCyan,
                items = listOf(
                    "Dead Man's Switch Trigger" to "180 Days Inactivity + Biometric Check",
                    "Hardware Keys Custody" to "3-of-5 Distributed Key Shares",
                    "Estate Tax Exemption Optimization" to "Zero Federal Estate Tax Liability"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// REUSABLE HELPER COMPONENTS
// -------------------------------------------------------------
@Composable
private fun PlannerSectionHeader(title: String, subtitle: String, icon: ImageVector, color: Color) {
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
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(subtitle, color = TextMuted, fontSize = 10.sp, lineHeight = 14.sp)
            }
        }
    }
}

@Composable
private fun PlannerMetricCard(
    title: String,
    badge: String,
    badgeColor: Color,
    items: List<Pair<String, String>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Navy900),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Surface(color = badgeColor.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                    Text(badge, color = badgeColor, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            items.forEach { (label, value) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(label, color = TextMuted, fontSize = 10.sp)
                    Text(value, color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
private fun MilestoneRow(date: String, title: String, delta: String, deltaColor: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(NavyCard)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(color = Navy700, shape = RoundedCornerShape(4.dp)) {
                Text(date, color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
            }
            Text(title, color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
        }
        Text(delta, color = deltaColor, fontWeight = FontWeight.Bold, fontSize = 10.sp)
    }
}

data class RoadmapMilestone(
    val age: String,
    val year: String,
    val title: String,
    val subtitle: String,
    val icon: ImageVector,
    val color: Color,
    val progress: Float, // 0.0 to 1.0
    val status: String,
    val financialImpact: String
)

@Composable
fun TimelineRoadmapCard(
    title: String,
    milestones: List<RoadmapMilestone>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Navy900),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, PurpleTech.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Timeline, contentDescription = null, tint = PurpleTech, modifier = Modifier.size(18.dp))
                    Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
                Surface(color = PurpleTech.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                    Text("Interactive Timeline", color = PurpleTech, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }

            milestones.forEachIndexed { index, milestone ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Left Timeline Column with Line and Node
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(32.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(milestone.color.copy(alpha = 0.2f))
                                .border(1.5.dp, milestone.color, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(milestone.icon, contentDescription = null, tint = milestone.color, modifier = Modifier.size(14.dp))
                        }
                        if (index < milestones.size - 1) {
                            Box(
                                modifier = Modifier
                                    .width(2.dp)
                                    .height(55.dp)
                                    .background(Navy700)
                            )
                        }
                    }

                    // Right Content Card
                    Surface(
                        color = NavyCard,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, milestone.color.copy(alpha = 0.3f)),
                        modifier = Modifier.weight(1f).padding(bottom = if (index < milestones.size - 1) 8.dp else 0.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(milestone.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Surface(color = milestone.color.copy(alpha = 0.15f), shape = RoundedCornerShape(4.dp)) {
                                    Text(
                                        "${milestone.age} • ${milestone.year}",
                                        color = milestone.color,
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(milestone.subtitle, color = TextMuted, fontSize = 9.sp, lineHeight = 12.sp)

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                LinearProgressIndicator(
                                    progress = { milestone.progress },
                                    modifier = Modifier.weight(1f).height(4.dp).clip(RoundedCornerShape(2.dp)),
                                    color = milestone.color,
                                    trackColor = Navy700
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    milestone.status,
                                    color = milestone.color,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 8.sp
                                )
                            }
                            Text(
                                "Impact: ${milestone.financialImpact}",
                                color = GoldAccent,
                                fontSize = 8.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun Int.formatWithCommas(): String {
    return "%,d".format(this)
}
