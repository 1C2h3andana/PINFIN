package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import com.example.ui.viewmodel.BankViewModel

enum class DigitalTwinPageType(val title: String, val icon: ImageVector, val code: String) {
    DASHBOARD("Twin 360°", Icons.Default.Psychology, "TWIN"),
    SIMULATION("Monte Carlo", Icons.Default.AutoAwesome, "SIM"),
    FUTURE_WEALTH("Wealth Predictor", Icons.Default.TrendingUp, "WEALTH"),
    RETIREMENT("Retirement Path", Icons.Default.BeachAccess, "RETIRE"),
    LOAN_SIM("Loan Simulator", Icons.Default.Calculate, "LOAN"),
    EDUCATION_COST("Education Fund", Icons.Default.School, "EDU"),
    FAMILY_PLANNING("Family Life", Icons.Default.FamilyRestroom, "FAMILY"),
    EMERGENCY_PLAN("Emergency Vault", Icons.Default.HealthAndSafety, "EMERGENCY"),
    INVESTMENT_SIM("Alpha Optimizer", Icons.Default.RocketLaunch, "ALPHA")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AiDigitalTwinSuiteScreen(
    viewModel: BankViewModel,
    initialPage: DigitalTwinPageType = DigitalTwinPageType.DASHBOARD,
    onNavigateToPage: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedPage by remember { mutableStateOf(initialPage) }
    val totalBalance by viewModel.totalNetWorth.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Navy900)
    ) {
        // --- Top Header ---
        Card(
            colors = CardDefaults.cardColors(containerColor = Navy800),
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Brush.linearGradient(listOf(PurpleTech, CyberCyan))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Psychology, contentDescription = null, tint = Navy900, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "AI FINANCIAL DIGITAL TWIN",
                                color = CyberCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                "Autonomous Life Simulation",
                                color = TextWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    AssistChip(
                        onClick = {},
                        label = { Text("Active Model v4.2", fontSize = 11.sp, color = EmeraldSuccess, fontWeight = FontWeight.Bold) },
                        colors = AssistChipDefaults.assistChipColors(containerColor = EmeraldSuccess.copy(alpha = 0.15f)),
                        border = BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.4f))
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // --- Horizontal Selector Bar ---
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(DigitalTwinPageType.values()) { page ->
                        val isSelected = selectedPage == page
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedPage = page },
                            label = { Text(page.title, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            leadingIcon = {
                                Icon(
                                    page.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (isSelected) Navy900 else CyberCyan
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberCyan,
                                selectedLabelColor = Navy900,
                                containerColor = Navy700,
                                labelColor = TextWhite
                            ),
                            border = BorderStroke(1.dp, if (isSelected) CyberCyan else Navy700)
                        )
                    }
                }
            }
        }

        // --- Animated Body Content ---
        AnimatedContent(
            targetState = selectedPage,
            transitionSpec = {
                (slideInHorizontally { width -> width / 3 } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width / 3 } + fadeOut()
                )
            },
            label = "DigitalTwinPageTransition",
            modifier = Modifier.fillMaxSize()
        ) { targetPage ->
            when (targetPage) {
                DigitalTwinPageType.DASHBOARD -> DigitalTwinDashboardScreen(viewModel)
                DigitalTwinPageType.SIMULATION -> FinancialSimulationScreen(viewModel)
                DigitalTwinPageType.FUTURE_WEALTH -> FutureWealthPredictionScreen(viewModel)
                DigitalTwinPageType.RETIREMENT -> RetirementSimulationScreen(viewModel)
                DigitalTwinPageType.LOAN_SIM -> LoanSimulationScreen(viewModel)
                DigitalTwinPageType.EDUCATION_COST -> EducationCostSimulationScreen(viewModel)
                DigitalTwinPageType.FAMILY_PLANNING -> FamilyPlanningSimulationScreen(viewModel)
                DigitalTwinPageType.EMERGENCY_PLAN -> EmergencyPlanningScreen(viewModel)
                DigitalTwinPageType.INVESTMENT_SIM -> InvestmentSimulationScreen(viewModel)
            }
        }
    }
}

// -------------------------------------------------------------------------
// 1. DIGITAL TWIN DASHBOARD SCREEN
// -------------------------------------------------------------------------
@Composable
fun DigitalTwinDashboardScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val totalBalance by viewModel.totalNetWorth.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Digital Twin Fidelity Index", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("99.4% Synchronized", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        "Your synthetic twin runs 10,000 real-time Monte Carlo iterations against macro shocks, inflation, and rate cuts to safeguard your net worth of $%,.2f.".format(totalBalance),
                        color = TextWhite,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    LinearProgressIndicator(
                        progress = { 0.94f },
                        color = CyberCyan,
                        trackColor = Navy700,
                        modifier = Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(4.dp))
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                MetricCard("Life Longevity Horizon", "42.5 Yrs", CyberCyan, Modifier.weight(1f))
                MetricCard("Shock Resilience", "94 / 100", EmeraldSuccess, Modifier.weight(1f))
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Top Autonomous Recommendations", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    TwinActionItem("Hedge Rate Cut Cycle", "Shift $15k to 5-Yr High Yield Treasury Vault", "+$1,840 Est.")
                    TwinActionItem("Refinance Real Estate Loan", "Lock in 4.12% fixed rate before Q4 Fed meeting", "$340/mo saved")
                    TwinActionItem("Boost Emergency Staking", "Add $5k to instant liquidity smart pool", "0-Risk Shield")
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 2. FINANCIAL SIMULATION SCREEN (MONTE CARLO)
// -------------------------------------------------------------------------
@Composable
fun FinancialSimulationScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var inflationRate by remember { mutableStateOf(2.5f) }
    var annualReturn by remember { mutableStateOf(8.0f) }
    var yearsHorizon by remember { mutableStateOf(15f) }

    val projectedValue = remember(inflationRate, annualReturn, yearsHorizon) {
        val realReturn = (annualReturn - inflationRate) / 100.0
        val base = 100000.0
        base * Math.pow(1.0 + realReturn, yearsHorizon.toDouble())
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Interactive Monte Carlo Simulator", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    Text("Annual Portfolio Yield: ${"%.1f".format(annualReturn)}%", color = TextWhite, fontSize = 12.sp)
                    Slider(
                        value = annualReturn,
                        onValueChange = { annualReturn = it },
                        valueRange = 2f..20f,
                        colors = SliderDefaults.colors(thumbColor = CyberCyan, activeTrackColor = ElectricBlue)
                    )

                    Text("Macro Inflation Benchmark: ${"%.1f".format(inflationRate)}%", color = TextWhite, fontSize = 12.sp)
                    Slider(
                        value = inflationRate,
                        onValueChange = { inflationRate = it },
                        valueRange = 1f..10f,
                        colors = SliderDefaults.colors(thumbColor = AmberOrange, activeTrackColor = CrimsonDanger)
                    )

                    Text("Time Horizon: ${yearsHorizon.toInt()} Years", color = TextWhite, fontSize = 12.sp)
                    Slider(
                        value = yearsHorizon,
                        onValueChange = { yearsHorizon = it },
                        valueRange = 1f..40f,
                        colors = SliderDefaults.colors(thumbColor = EmeraldSuccess, activeTrackColor = CyberCyan)
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Navy700),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Simulated Real Purchasing Power (from $100,000 Base)", color = TextMuted, fontSize = 11.sp)
                            Text("$%,.2f USD".format(projectedValue), color = EmeraldSuccess, fontWeight = FontWeight.Black, fontSize = 22.sp)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 3. FUTURE WEALTH PREDICTION SCREEN
// -------------------------------------------------------------------------
@Composable
fun FutureWealthPredictionScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("AI Trajectory Predictions", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
        val milestones = listOf(
            Triple("Year 2028 (2 Yrs)", "$240,000.00", "99.1% Confidence"),
            Triple("Year 2031 (5 Yrs)", "$580,000.00", "94.8% Confidence"),
            Triple("Year 2036 (10 Yrs)", "$1,450,000.00", "88.2% Confidence"),
            Triple("Year 2046 (20 Yrs)", "$4,820,000.00", "81.0% Confidence")
        )
        items(milestones) { (year, valStr, conf) ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(year, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(conf, color = EmeraldSuccess, fontSize = 11.sp)
                    }
                    Text(valStr, color = CyberCyan, fontWeight = FontWeight.Black, fontSize = 16.sp)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 4. RETIREMENT SIMULATION SCREEN
// -------------------------------------------------------------------------
@Composable
fun RetirementSimulationScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var targetAge by remember { mutableStateOf(58f) }
    var monthlySpend by remember { mutableStateOf(6500f) }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Target Retirement Age: ${targetAge.toInt()} yrs", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Slider(
                        value = targetAge,
                        onValueChange = { targetAge = it },
                        valueRange = 45f..75f,
                        colors = SliderDefaults.colors(thumbColor = CyberCyan, activeTrackColor = ElectricBlue)
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Desired Post-Retirement Spend: $${monthlySpend.toInt()}/mo", color = TextWhite, fontSize = 14.sp)
                    Slider(
                        value = monthlySpend,
                        onValueChange = { monthlySpend = it },
                        valueRange = 2000f..25000f,
                        colors = SliderDefaults.colors(thumbColor = EmeraldSuccess, activeTrackColor = CyberCyan)
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Navy700),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            DetailItemRow("Required Nest Egg", "$%,.2f".format(monthlySpend * 12 * 25), EmeraldSuccess)
                            DetailItemRow("FIRE Runway Independence", "100% Fully Funded", CyberCyan)
                            DetailItemRow("Passive Dividend Income", "$%,.2f / mo".format(monthlySpend * 1.15), GoldAccent)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 5. LOAN SIMULATION SCREEN
// -------------------------------------------------------------------------
@Composable
fun LoanSimulationScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var principal by remember { mutableStateOf("250000") }
    var interestRate by remember { mutableStateOf("5.5") }
    var tenureYears by remember { mutableStateOf("20") }

    val p = principal.toDoubleOrNull() ?: 0.0
    val r = (interestRate.toDoubleOrNull() ?: 0.0) / 1200.0
    val n = (tenureYears.toDoubleOrNull() ?: 1.0) * 12.0
    val emi = if (r > 0 && n > 0) (p * r * Math.pow(1 + r, n)) / (Math.pow(1 + r, n) - 1) else 0.0

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("AI Real-Time EMI & Amortization Simulator", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = principal,
                        onValueChange = { principal = it },
                        label = { Text("Principal Amount ($)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, unfocusedBorderColor = Navy700, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = interestRate,
                        onValueChange = { interestRate = it },
                        label = { Text("Interest Rate (% APR)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, unfocusedBorderColor = Navy700, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = tenureYears,
                        onValueChange = { tenureYears = it },
                        label = { Text("Tenure (Years)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, unfocusedBorderColor = Navy700, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Navy700),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Estimated Monthly EMI Outflow", color = TextMuted, fontSize = 11.sp)
                            Text("$%,.2f / mo".format(emi), color = GoldAccent, fontSize = 24.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 6. EDUCATION COST SIMULATION SCREEN
// -------------------------------------------------------------------------
@Composable
fun EducationCostSimulationScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Higher Education & Ivy League Fund Planner", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    DetailItemRow("Target University Tier", "Top Tier Global Institution", TextWhite)
                    DetailItemRow("Target Matriculation Year", "2034 (8 Yrs Out)", CyberCyan)
                    DetailItemRow("Estimated Total Tuition + Living", "$285,000.00", GoldAccent)
                    DetailItemRow("Automated 529 Monthly SIP", "$1,450.00 / mo", EmeraldSuccess)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 7. FAMILY PLANNING SIMULATION SCREEN
// -------------------------------------------------------------------------
@Composable
fun FamilyPlanningSimulationScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Multi-Generational Family Planning Engine", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    DetailItemRow("Dependents Safeguarded", "3 Family Members", TextWhite)
                    DetailItemRow("Comprehensive Health Trust Pool", "$150,000.00", EmeraldSuccess)
                    DetailItemRow("Estate Transfer Tax Shield", "Zero-Knowledge Estate Trust", GoldAccent)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 8. EMERGENCY PLANNING SCREEN
// -------------------------------------------------------------------------
@Composable
fun EmergencyPlanningScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Autonomous Emergency Liquidity Vault", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    DetailItemRow("Monthly Fixed Living Outflow", "$5,800.00", TextWhite)
                    DetailItemRow("Recommended Safety Runway", "12 Months ($69,600.00)", CyberCyan)
                    DetailItemRow("Current Vault Liquidity", "$78,500.00", EmeraldSuccess)
                    DetailItemRow("Vault Security Status", "112% Funded (OPTIMAL)", EmeraldSuccess)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 9. INVESTMENT SIMULATION SCREEN (ALPHA OPTIMIZER)
// -------------------------------------------------------------------------
@Composable
fun InvestmentSimulationScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("AI Alpha Portfolio Optimization", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    SectorProgressRow("Equities & Index ETFs", 0.40f, CyberCyan, "40%")
                    SectorProgressRow("Govt Bonds & T-Bills", 0.30f, ElectricBlue, "30%")
                    SectorProgressRow("Real Estate REITs", 0.15f, GoldAccent, "15%")
                    SectorProgressRow("Crypto & Digital Assets", 0.15f, PurpleTech, "15%")
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailItemRow("Simulated Sharpe Ratio", "2.48 (Excellent)", EmeraldSuccess)
                    DetailItemRow("Max Drawdown Resistance", "-6.8% Max", GoldAccent)
                }
            }
        }
    }
}

@Composable
fun TwinActionItem(title: String, desc: String, tag: String) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Navy700),
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(desc, color = TextMuted, fontSize = 11.sp)
            }
            Text(tag, color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}
