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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import com.example.ui.viewmodel.BankViewModel

enum class MacroClimateHealthEduPageType(val title: String, val icon: ImageVector, val code: String) {
    // Module 18: Global Economic Intelligence
    GLOBAL_MACRO("Global Macro", Icons.Default.Public, "MACRO"),
    INFLATION_TRACKER("Inflation Radar", Icons.Default.TrendingUp, "INFLATION"),
    COMMODITY_OIL("Oil & Commodities", Icons.Default.LocalGasStation, "OIL"),
    INTEREST_RATES("Fed & Central Banks", Icons.Default.AccountBalance, "RATES"),

    // Module 19: Climate Financial Intelligence
    CLIMATE_DASHBOARD("Climate Hub", Icons.Default.Eco, "CLIMATE"),
    CARBON_SCORE("Carbon Footprint", Icons.Default.EnergySavingsLeaf, "CARBON"),
    FLOOD_DISASTER_RISK("Disaster Risk", Icons.Default.Flood, "DISASTER"),

    // Module 20: Healthcare Financial Intelligence
    HEALTH_EXPENSE_PLAN("Medical Planner", Icons.Default.MedicalServices, "HEALTH"),
    HOSPITAL_PREDICTION("Hospital AI Cost", Icons.Default.LocalHospital, "HOSPITAL"),
    HSA_SAVINGS("HSA Vault", Icons.Default.HealthAndSafety, "HSA"),

    // Module 21: Education Financial Intelligence
    EDU_PLANNER("College Planner", Icons.Default.School, "EDU"),
    SCHOLARSHIP_FINDER("Scholarships AI", Icons.Default.CardGiftcard, "SCHOLAR"),
    CAREER_INCOME_PREDICT("Career Trajectory", Icons.Default.Work, "CAREER")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MacroClimateHealthEducationSuiteScreen(
    viewModel: BankViewModel,
    initialPage: MacroClimateHealthEduPageType = MacroClimateHealthEduPageType.GLOBAL_MACRO,
    onNavigateToPage: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedPage by remember { mutableStateOf(initialPage) }

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
                                .background(Brush.linearGradient(listOf(CyberCyan, EmeraldSuccess))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Public, contentDescription = null, tint = Navy900, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "MACRO • CLIMATE • HEALTH • EDUCATION",
                                color = CyberCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                "Societal & Planetary Intelligence",
                                color = TextWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    AssistChip(
                        onClick = {},
                        label = { Text("Planetary Index: A+", fontSize = 11.sp, color = EmeraldSuccess, fontWeight = FontWeight.Bold) },
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
                    items(MacroClimateHealthEduPageType.values()) { page ->
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
            label = "MacroPageTransition",
            modifier = Modifier.fillMaxSize()
        ) { targetPage ->
            when (targetPage) {
                MacroClimateHealthEduPageType.GLOBAL_MACRO -> GlobalMacroScreen(viewModel)
                MacroClimateHealthEduPageType.INFLATION_TRACKER -> InflationTrackerScreen(viewModel)
                MacroClimateHealthEduPageType.COMMODITY_OIL -> CommodityOilScreen(viewModel)
                MacroClimateHealthEduPageType.INTEREST_RATES -> InterestRatesScreen(viewModel)
                MacroClimateHealthEduPageType.CLIMATE_DASHBOARD -> ClimateDashboardScreen(viewModel)
                MacroClimateHealthEduPageType.CARBON_SCORE -> CarbonScoreScreen(viewModel)
                MacroClimateHealthEduPageType.FLOOD_DISASTER_RISK -> FloodDisasterRiskScreen(viewModel)
                MacroClimateHealthEduPageType.HEALTH_EXPENSE_PLAN -> HealthExpensePlanScreen(viewModel)
                MacroClimateHealthEduPageType.HOSPITAL_PREDICTION -> HospitalPredictionScreen(viewModel)
                MacroClimateHealthEduPageType.HSA_SAVINGS -> HsaSavingsScreen(viewModel)
                MacroClimateHealthEduPageType.EDU_PLANNER -> EduPlannerScreen(viewModel)
                MacroClimateHealthEduPageType.SCHOLARSHIP_FINDER -> ScholarshipFinderScreen(viewModel)
                MacroClimateHealthEduPageType.CAREER_INCOME_PREDICT -> CareerIncomePredictScreen(viewModel)
            }
        }
    }
}

// -------------------------------------------------------------------------
// SCREENS IMPLEMENTATIONS
// -------------------------------------------------------------------------
@Composable
fun GlobalMacroScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Global Macroeconomic Vital Signs", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("US Real GDP Growth", "2.8% Annualized", EmeraldSuccess)
                    DetailItemRow("Eurozone Inflation (HICP)", "2.2% Target Stable", CyberCyan)
                    DetailItemRow("China Caixin Manufacturing PMI", "51.4 (Expansion)", GoldAccent)
                }
            }
        }
    }
}

@Composable
fun InflationTrackerScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Global Inflation & CPI Radar", color = AmberOrange, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("US Headline CPI", "2.6% YoY", EmeraldSuccess)
                    DetailItemRow("Core PCE Inflation", "2.5% YoY", CyberCyan)
                    DetailItemRow("Energy & Food Volatility", "Moderate (-1.2%)", EmeraldSuccess)
                }
            }
        }
    }
}

@Composable fun CommodityOilScreen(viewModel: BankViewModel) {
    val commodities = listOf(
        Triple("Brent Crude Oil", "$78.40 / bbl", "-0.8%"),
        Triple("Natural Gas (Henry Hub)", "$2.15 / MMBtu", "+1.2%"),
        Triple("Copper (COMEX)", "$4.20 / lb", "+2.4%"),
        Triple("Lithium Carbonate", "$14,200 / ton", "+3.1%")
    )
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Energy & Raw Materials Spot Market", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp) }
        items(commodities) { (item, price, chg) ->
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(item, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Column(horizontalAlignment = Alignment.End) {
                        Text(price, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(chg, color = if (chg.startsWith("+")) EmeraldSuccess else CrimsonDanger, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

@Composable fun InterestRatesScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Central Bank Benchmark Policy Rates", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Federal Reserve (Fed Funds)", "5.25% - 5.50%", TextWhite)
                    DetailItemRow("European Central Bank (ECB)", "3.75%", CyberCyan)
                    DetailItemRow("Bank of England (BoE)", "5.00%", GoldAccent)
                    DetailItemRow("Bank of Japan (BoJ)", "0.25%", PurpleTech)
                }
            }
        }
    }
}

@Composable fun ClimateDashboardScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Climate Risk & Carbon Credit Registry", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Personal Carbon Intensity", "3.2 Tons CO2/yr (Low)", EmeraldSuccess)
                    DetailItemRow("Automated Offset Subscriptions", "100% Net Zero Certified", CyberCyan)
                }
            }
        }
    }
}

@Composable fun CarbonScoreScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Spending Carbon Intensity Diagnostic", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    SectorProgressRow("Renewable Energy & EV", 0.95f, EmeraldSuccess, "0.1 t")
                    SectorProgressRow("Local Sustainable Food", 0.85f, CyberCyan, "0.4 t")
                    SectorProgressRow("Aviation & Commercial Flights", 0.40f, AmberOrange, "2.7 t")
                }
            }
        }
    }
}

@Composable fun FloodDisasterRiskScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Geospatial Disaster & Flood Risk Engine", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Primary Property Risk Tier", "Zone X (Minimal Flood Risk)", EmeraldSuccess)
                    DetailItemRow("Wildfire Resilience Index", "98 / 100", CyberCyan)
                }
            }
        }
    }
}

@Composable fun HealthExpensePlanScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Comprehensive Family Healthcare Plan", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Expected Annual Out-of-Pocket", "$2,400.00", TextWhite)
                    DetailItemRow("HSA Triple-Tax Advantage Reserve", "$18,500.00", EmeraldSuccess)
                }
            }
        }
    }
}

@Composable fun HospitalPredictionScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("AI Hospital Procedure Cost Estimator", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Transparent pre-negotiated in-network rate estimator across 1,200 hospitals nationwide.", color = TextMuted, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable fun HsaSavingsScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Invested HSA Stealth IRA Vault", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Invested in S&P 500 ETF", "$14,000.00", CyberCyan)
                    DetailItemRow("Liquid Cash for Immediate Rx", "$4,500.00", EmeraldSuccess)
                }
            }
        }
    }
}

@Composable fun EduPlannerScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("529 College Savings & Tuition Ladder", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Total 529 Vault Assets", "$68,400.00", EmeraldSuccess)
                    DetailItemRow("State Tax Deduction Captured", "$10,000 / yr", CyberCyan)
                }
            }
        }
    }
}

@Composable fun ScholarshipFinderScreen(viewModel: BankViewModel) {
    val scholarships = listOf(
        Pair("Global STEM Innovation Fellowship", "$25,000 Award • Deadline Nov 15"),
        Pair("National Merit Leadership Grant", "$10,000 Award • Deadline Dec 01"),
        Pair("Future FinTech AI Researcher Grant", "$15,000 Award • Deadline Jan 10")
    )
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("AI Scholarship & Grant Matcher", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp) }
        items(scholarships) { (title, info) ->
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(info, color = CyberCyan, fontSize = 11.sp)
                    }
                    Button(
                        onClick = { viewModel.showMessage("Applied with verified academic credentials!") },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = Navy900),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Apply", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable fun CareerIncomePredictScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Career Trajectory & Compensation AI", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Senior Principal Architect Market Comp", "$320,000 - $450,000", EmeraldSuccess)
                    DetailItemRow("Equity / Stock Grant Multiplier", "3.2x Upside Potential", CyberCyan)
                }
            }
        }
    }
}
