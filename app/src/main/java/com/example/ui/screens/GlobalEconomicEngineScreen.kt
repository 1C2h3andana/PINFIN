package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.Agriculture
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudQueue
import androidx.compose.material.icons.filled.CurrencyExchange
import androidx.compose.material.icons.filled.DirectionsBoat
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.HowToVote
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.LocalGasStation
import androidx.compose.material.icons.filled.LocalMall
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
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
import androidx.compose.runtime.mutableDoubleStateOf
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
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

enum class EconomyTabCategory(val label: String) {
    ANALYSIS("Macro Continuous Signals (10)"),
    PREDICTIONS("AI Predictive Engines (4)")
}

enum class EconomicEnginePage(
    val title: String,
    val icon: ImageVector,
    val isPredictive: Boolean,
    val tag: String
) {
    // 10 Continuous Macro Analysis Pages
    INFLATION("Inflation", Icons.Default.TrendingUp, false, "tab_inflation"),
    WARS("Wars & Geopolitics", Icons.Default.Shield, false, "tab_wars"),
    CLIMATE("Climate Change", Icons.Default.Thermostat, false, "tab_climate"),
    ELECTIONS("Elections & Policy", Icons.Default.HowToVote, false, "tab_elections"),
    OIL_PRICES("Oil & Energy", Icons.Default.LocalGasStation, false, "tab_oil"),
    FOOD_PRICES("Food & Agritech", Icons.Default.Agriculture, false, "tab_food"),
    SUPPLY_CHAINS("Supply Chains", Icons.Default.LocalShipping, false, "tab_supply"),
    EMPLOYMENT("Labor & Jobs", Icons.Default.Work, false, "tab_employment"),
    CURRENCY("Currency & FX", Icons.Default.CurrencyExchange, false, "tab_currency"),
    STOCK_MARKETS("Global Equities", Icons.Default.ShowChart, false, "tab_stocks"),

    // 4 Future Predictions Engines
    FUTURE_RECESSION("Recession Forecaster", Icons.Default.TrendingDown, true, "tab_recession"),
    INVESTMENT_RISKS("Investment Risk Engine", Icons.Default.Warning, true, "tab_risks"),
    PERSONAL_IMPACT("Personal Financial Impact", Icons.Default.Person, true, "tab_personal_impact"),
    BUSINESS_OPPORTUNITIES("Venture Opportunities", Icons.Default.Lightbulb, true, "tab_opportunities")
}

@Composable
fun GlobalEconomicEngineScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    var currentPage by remember { mutableStateOf(EconomicEnginePage.FUTURE_RECESSION) }
    var selectedCategory by remember { mutableStateOf(EconomyTabCategory.PREDICTIONS) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // --- Header Section ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(CyberCyan.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Public,
                        contentDescription = "Global Engine",
                        tint = CyberCyan,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "GLOBAL ECONOMIC PREDICTOR",
                            color = TextWhite,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                        PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 6.dp)
                    }
                    Text(
                        text = "10 Macro Signals • 4 AI Predictive Engines",
                        color = CyberCyan,
                        fontSize = 10.sp
                    )
                }
            }

            Surface(
                color = NavyCardLight,
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(Icons.Default.Speed, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(13.dp))
                    Text("Live Feed", color = GoldAccent, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // --- Category Toggle Filter ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            EconomyTabCategory.values().forEach { cat ->
                val isSelected = selectedCategory == cat
                Surface(
                    color = if (isSelected) CyberCyan.copy(alpha = 0.25f) else NavyCard,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) CyberCyan else Navy700),
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedCategory = cat
                            if (cat == EconomyTabCategory.PREDICTIONS && !currentPage.isPredictive) {
                                currentPage = EconomicEnginePage.FUTURE_RECESSION
                            } else if (cat == EconomyTabCategory.ANALYSIS && currentPage.isPredictive) {
                                currentPage = EconomicEnginePage.INFLATION
                            }
                        }
                        .testTag("cat_tab_${cat.name}")
                ) {
                    Text(
                        text = cat.label,
                        color = if (isSelected) CyberCyan else TextMuted,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.padding(vertical = 8.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )
                }
            }
        }

        // --- Filtered Scrollable Tabs ---
        val displayedPages = EconomicEnginePage.values().filter {
            if (selectedCategory == EconomyTabCategory.PREDICTIONS) it.isPredictive else !it.isPredictive
        }

        ScrollableTabRow(
            selectedTabIndex = displayedPages.indexOf(currentPage).coerceAtLeast(0),
            containerColor = NavyCard,
            contentColor = CyberCyan,
            edgePadding = 4.dp,
            indicator = { tabPositions ->
                val idx = displayedPages.indexOf(currentPage)
                if (idx in tabPositions.indices) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[idx]),
                        color = if (currentPage.isPredictive) GoldAccent else CyberCyan,
                        height = 3.dp
                    )
                }
            }
        ) {
            displayedPages.forEach { page ->
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
                                tint = if (isSelected) (if (page.isPredictive) GoldAccent else CyberCyan) else TextMuted,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = page.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) (if (page.isPredictive) GoldAccent else CyberCyan) else TextMuted
                            )
                        }
                    },
                    modifier = Modifier.testTag(page.tag)
                )
            }
        }

        // --- Animated Page Body ---
        AnimatedContent(
            targetState = currentPage,
            transitionSpec = {
                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width } + fadeOut()
                )
            },
            label = "EconomicPageTransition",
            modifier = Modifier.weight(1f)
        ) { targetPage ->
            when (targetPage) {
                // 10 Continuous Analysis Pages
                EconomicEnginePage.INFLATION -> PageInflationAnalysis()
                EconomicEnginePage.WARS -> PageWarsGeopolitics()
                EconomicEnginePage.CLIMATE -> PageClimateChange()
                EconomicEnginePage.ELECTIONS -> PageElectionsPolicy()
                EconomicEnginePage.OIL_PRICES -> PageOilEnergy()
                EconomicEnginePage.FOOD_PRICES -> PageFoodAgritech()
                EconomicEnginePage.SUPPLY_CHAINS -> PageSupplyChains()
                EconomicEnginePage.EMPLOYMENT -> PageEmploymentLabor()
                EconomicEnginePage.CURRENCY -> PageCurrencyFx()
                EconomicEnginePage.STOCK_MARKETS -> PageStockMarkets()

                // 4 Predictive Output Engines
                EconomicEnginePage.FUTURE_RECESSION -> PageFutureRecessionPrediction()
                EconomicEnginePage.INVESTMENT_RISKS -> PageInvestmentRisksForecast()
                EconomicEnginePage.PERSONAL_IMPACT -> PagePersonalFinancialImpact()
                EconomicEnginePage.BUSINESS_OPPORTUNITIES -> PageBusinessOpportunities()
            }
        }
    }
}

// -------------------------------------------------------------
// ANALYSIS PAGE 1: INFLATION
// -------------------------------------------------------------
@Composable
private fun PageInflationAnalysis() {
    val inflationHistory = remember {
        listOf(
            ChartPoint("Jan", 6.4f, 5.6f),
            ChartPoint("Mar", 5.0f, 5.6f),
            ChartPoint("May", 4.0f, 5.3f),
            ChartPoint("Jul", 3.2f, 4.7f),
            ChartPoint("Sep", 3.7f, 4.1f),
            ChartPoint("Nov", 3.1f, 4.0f),
            ChartPoint("Now", 2.9f, 3.2f),
            ChartPoint("Q4*", 2.4f, 2.6f)
        )
    }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            MacroHeaderCard("Global Inflation & Central Bank Rates", "Headline CPI, sticky services inflation, and policy rate expectations.", Icons.Default.TrendingUp, CyberCyan)
        }
        item {
            RechartsAreaChart(
                title = "CPI vs Core Inflation Trend (12-Mo + AI Forecast)",
                data = inflationHistory,
                lineColor1 = CyberCyan,
                lineColor2 = AmberOrange,
                legend1 = "Headline CPI (YoY)",
                legend2 = "Core Services (YoY)"
            )
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Navy900), shape = RoundedCornerShape(14.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MacroMetricRow("US Headline CPI (YoY)", "3.1%", "-0.2% MoM", EmeraldSuccess)
                    MacroMetricRow("Eurozone Core HICP", "2.8%", "Steady", CyberCyan)
                    MacroMetricRow("Fed Funds Target Rate", "5.25% - 5.50%", "Peak Rate", GoldAccent)
                    MacroMetricRow("Sticky Wage Growth", "4.1% YoY", "Softening", AmberOrange)
                }
            }
        }
        item {
            RechartsBarChart(
                title = "Global Central Bank Interest Rates vs Inflation",
                bars = listOf(
                    BarGroup("US", 5.33f, 3.1f),
                    BarGroup("EU", 3.75f, 2.8f),
                    BarGroup("UK", 5.25f, 3.4f),
                    BarGroup("JP", 0.25f, 2.5f),
                    BarGroup("EM", 8.50f, 4.2f)
                ),
                color1 = GoldAccent,
                color2 = CyberCyan,
                label1 = "Policy Rate %",
                label2 = "Inflation %"
            )
        }
        item {
            InsightCard("AI Inflation Outlook", "Central bank rate cuts are priced for late Q3. Core shelter components are easing, lowering systemic stagflation risks from 42% to 18%.", EmeraldSuccess)
        }
    }
}

// -------------------------------------------------------------
// ANALYSIS PAGE 2: WARS & GEOPOLITICS
// -------------------------------------------------------------
@Composable
private fun PageWarsGeopolitics() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            MacroHeaderCard("Geopolitical Conflict & Sovereign Risk", "Active conflict chokepoints, shipping route sanctions, and defense capex.", Icons.Default.Shield, CrimsonDanger)
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Navy900), shape = RoundedCornerShape(14.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MacroMetricRow("Red Sea Shipping Disruption", "Elevated Risk", "+48% Freight Cost", CrimsonDanger)
                    MacroMetricRow("Eastern European Corridor", "Active Friction", "Sanctions Locked", AmberOrange)
                    MacroMetricRow("Global Defense Outlays", "$2.44 Trillion", "+6.8% YoY Surge", PurpleTech)
                    MacroMetricRow("Safe-Haven Gold Inflows", "$2,380 / oz", "+12.4% 6-Mo", GoldAccent)
                }
            }
        }
        item {
            InsightCard("Geopolitical Risk Multiplier", "Maritime rerouting around the Cape of Good Hope adds 10-14 transit days, creating temporary supply buffers in European retail inventories.", AmberOrange)
        }
    }
}

// -------------------------------------------------------------
// ANALYSIS PAGE 3: CLIMATE CHANGE
// -------------------------------------------------------------
@Composable
private fun PageClimateChange() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            MacroHeaderCard("Climate Change & Energy Transition", "Extreme weather economic drag, carbon credits, and green grid investments.", Icons.Default.Thermostat, EmeraldSuccess)
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Navy900), shape = RoundedCornerShape(14.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MacroMetricRow("Global Carbon Credit Index", "€68.50 / ton", "+5.4% YoY", EmeraldSuccess)
                    MacroMetricRow("Hydroelectric Drought Drag", "14% Output Drop", "S. America & Asia", AmberOrange)
                    MacroMetricRow("Renewable Capex Velocity", "$1.8 Trillion", "Record High", CyberCyan)
                    MacroMetricRow("Extreme Weather Loss Buffer", "$210 Billion/yr", "Insured Losses", CrimsonDanger)
                }
            }
        }
        item {
            InsightCard("Clean Energy Substitution", "Solar and battery storage installation costs dropped 18% YoY, accelerating commercial grid parity across 34 countries.", EmeraldSuccess)
        }
    }
}

// -------------------------------------------------------------
// ANALYSIS PAGE 4: ELECTIONS & POLICY
// -------------------------------------------------------------
@Composable
private fun PageElectionsPolicy() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            MacroHeaderCard("Elections & Sovereign Policy Shifts", "Global democratic election cycles, fiscal deficits, and trade tariff risks.", Icons.Default.HowToVote, PurpleTech)
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Navy900), shape = RoundedCornerShape(14.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MacroMetricRow("G20 Global Elections Scope", "64 Countries", "4.2B Population", PurpleTech)
                    MacroMetricRow("Tariff Escalation Probability", "35% Risk", "Cross-Border Trade", AmberOrange)
                    MacroMetricRow("US Fiscal Deficit / GDP", "6.2%", "Treasury Issuance", GoldAccent)
                    MacroMetricRow("Antitrust Regulatory Pressure", "High", "Big Tech & AI", CyberCyan)
                }
            }
        }
        item {
            InsightCard("Policy Impact Matrix", "Fiscal deficits remain expansive across major economies, providing structural liquidity support to sovereign debt markets while limiting deep recession downside.", PurpleTech)
        }
    }
}

// -------------------------------------------------------------
// ANALYSIS PAGE 5: OIL & ENERGY
// -------------------------------------------------------------
@Composable
private fun PageOilEnergy() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            MacroHeaderCard("Oil & Global Energy Markets", "Brent crude, WTI benchmark, OPEC+ spare capacity, and LNG flows.", Icons.Default.LocalGasStation, GoldAccent)
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Navy900), shape = RoundedCornerShape(14.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MacroMetricRow("Brent Crude Spot", "$82.40 / bbl", "+1.2% Daily", GoldAccent)
                    MacroMetricRow("WTI Crude Benchmark", "$78.10 / bbl", "Tight Range", CyberCyan)
                    MacroMetricRow("OPEC+ Spare Buffer", "3.2M bpd", "Production Curbs", AmberOrange)
                    MacroMetricRow("US Strategic Petroleum Reserve", "375M Barrels", "Replenishing", EmeraldSuccess)
                }
            }
        }
        item {
            InsightCard("Energy Balance", "Non-OPEC production growth (US, Guyana, Brazil) provides supply resilience, capping upside oil shock risks below $95/bbl.", EmeraldSuccess)
        }
    }
}

// -------------------------------------------------------------
// ANALYSIS PAGE 6: FOOD & AGRICULTURE
// -------------------------------------------------------------
@Composable
private fun PageFoodAgritech() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            MacroHeaderCard("Food Prices & Agricultural Yields", "UN FAO food price index, grain corridors, fertilizer inputs, and livestock.", Icons.Default.Agriculture, AmberOrange)
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Navy900), shape = RoundedCornerShape(14.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MacroMetricRow("FAO Food Price Index", "118.3 pts", "-4.2% YoY", EmeraldSuccess)
                    MacroMetricRow("Wheat & Corn Futures", "$5.85 / bushel", "Normal Range", CyberCyan)
                    MacroMetricRow("Nitrogen Fertilizer Index", "$340 / ton", "Stabilized", AmberOrange)
                    MacroMetricRow("Cocoa / Sugar Volatility", "Elevated Highs", "Crop Dryness", CrimsonDanger)
                }
            }
        }
        item {
            InsightCard("Food Inflation Outlook", "Broad agricultural commodities are well below 2022 peaks, removing food cost pressure from core consumer inflation indices.", EmeraldSuccess)
        }
    }
}

// -------------------------------------------------------------
// ANALYSIS PAGE 7: SUPPLY CHAINS
// -------------------------------------------------------------
@Composable
private fun PageSupplyChains() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            MacroHeaderCard("Global Supply Chains & Logistics", "Baltic dry index, container spot rates, and semiconductor lead times.", Icons.Default.LocalShipping, CyberCyan)
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Navy900), shape = RoundedCornerShape(14.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MacroMetricRow("Baltic Dry Shipping Index", "1,840 pts", "+8.2% MoM", CyberCyan)
                    MacroMetricRow("40ft Container Spot Rate", "$3,250", "Manageable", EmeraldSuccess)
                    MacroMetricRow("Semiconductor Delivery Lag", "14.2 Weeks", "Normalizing", PurpleTech)
                    MacroMetricRow("Nearshoring Investment Shift", "$140 Billion", "Mexico & SE Asia", GoldAccent)
                }
            }
        }
        item {
            InsightCard("Supply Chain Resilience", "Companies have built diversified multi-country supplier networks, eliminating single-source vulnerability across electronics and auto assembly.", CyberCyan)
        }
    }
}

// -------------------------------------------------------------
// ANALYSIS PAGE 8: EMPLOYMENT & LABOR
// -------------------------------------------------------------
@Composable
private fun PageEmploymentLabor() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            MacroHeaderCard("Employment & Labor Market Dynamics", "Job openings (JOLTS), unemployment rate, real wage growth, and AI automation.", Icons.Default.Work, EmeraldSuccess)
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Navy900), shape = RoundedCornerShape(14.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MacroMetricRow("US Unemployment Rate", "3.9%", "Near 50-Yr Lows", EmeraldSuccess)
                    MacroMetricRow("Job Openings / Unemployed", "1.35 Ratio", "Balancing", CyberCyan)
                    MacroMetricRow("Real Disposable Income", "+2.1% YoY", "Positive Growth", GoldAccent)
                    MacroMetricRow("Tech Automation Displacement", "Low Net Impact", "Upskilling Surge", PurpleTech)
                }
            }
        }
        item {
            InsightCard("Labor Health", "The labor market exhibits rare 'soft landing' conditions: job openings cooled without mass layoffs, preserving consumer spending resilience.", EmeraldSuccess)
        }
    }
}

// -------------------------------------------------------------
// ANALYSIS PAGE 9: CURRENCY & FX
// -------------------------------------------------------------
@Composable
private fun PageCurrencyFx() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            MacroHeaderCard("Currency Markets & Foreign Exchange", "US Dollar Index (DXY), EUR/USD, Yen carry trade, and central bank reserves.", Icons.Default.CurrencyExchange, GoldAccent)
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Navy900), shape = RoundedCornerShape(14.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MacroMetricRow("US Dollar Index (DXY)", "104.2", "Stable Range", GoldAccent)
                    MacroMetricRow("EUR / USD Exchange Rate", "1.086", "Rangebound", CyberCyan)
                    MacroMetricRow("USD / JPY (Yen Carry Flow)", "154.2", "BOJ Intervention Watch", AmberOrange)
                    MacroMetricRow("Cross-Border Digital CBDC", "38 Pilot Rails", "+64% Volume", PurpleTech)
                }
            }
        }
        item {
            InsightCard("FX Stability", "DXY strength remains anchored by US productivity gains. Foreign central banks maintain comfortable reserve liquidity cushions.", GoldAccent)
        }
    }
}

// -------------------------------------------------------------
// ANALYSIS PAGE 10: GLOBAL STOCK MARKETS
// -------------------------------------------------------------
@Composable
private fun PageStockMarkets() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            MacroHeaderCard("Global Stock & Equity Markets", "S&P 500, Nasdaq 100, Nikkei 225, MSCI World, and Equity Risk Premiums.", Icons.Default.ShowChart, CyberCyan)
        }
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Navy900), shape = RoundedCornerShape(14.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    MacroMetricRow("S&P 500 Index (US)", "5,320 pts", "+14.8% YTD", EmeraldSuccess)
                    MacroMetricRow("Nasdaq Composite (Tech)", "16,840 pts", "+18.2% YTD", CyberCyan)
                    MacroMetricRow("Nikkei 225 (Japan)", "38,900 pts", "All-Time Highs", GoldAccent)
                    MacroMetricRow("Schiller Cyclical P/E", "31.4x", "Premium Valuation", AmberOrange)
                }
            }
        }
        item {
            InsightCard("Equity Momentum", "Earnings growth driven by enterprise AI infrastructure and resilient consumer balance sheets sustains market multiple expansion.", CyberCyan)
        }
    }
}

// -------------------------------------------------------------
// PREDICTIVE ENGINE 1: FUTURE RECESSION PREDICTOR
// -------------------------------------------------------------
@Composable
private fun PageFutureRecessionPrediction() {
    var simulatedRecessionProbability by remember { mutableStateOf(24f) }
    val yieldCurveSpread = "+0.18%" // Uninverting
    val sahmRuleTrigger = "0.33% (Below 0.50% Alert)"

    val recessionHistory = remember {
        listOf(
            ChartPoint("2020", 85.0f, 65.0f),
            ChartPoint("2021", 15.0f, 20.0f),
            ChartPoint("2022", 45.0f, 50.0f),
            ChartPoint("2023", 65.0f, 60.0f),
            ChartPoint("2024", 35.0f, 30.0f),
            ChartPoint("2025*", 24.0f, 22.0f),
            ChartPoint("2026*", 18.0f, 16.0f)
        )
    }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.TrendingDown, contentDescription = null, tint = GoldAccent)
                            Text("12-Month Recession Probability Engine", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Surface(
                            color = if (simulatedRecessionProbability < 30f) EmeraldSuccess.copy(alpha = 0.2f) else if (simulatedRecessionProbability < 60f) AmberOrange.copy(alpha = 0.2f) else CrimsonDanger.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                "Risk: ${if (simulatedRecessionProbability < 30f) "LOW" else if (simulatedRecessionProbability < 60f) "MODERATE" else "ELEVATED"} (${simulatedRecessionProbability.toInt()}%)",
                                color = if (simulatedRecessionProbability < 30f) EmeraldSuccess else if (simulatedRecessionProbability < 60f) AmberOrange else CrimsonDanger,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    // Interactive Gauge Dial
                    RechartsRecessionGauge(
                        probability = simulatedRecessionProbability,
                        onProbabilityChange = { simulatedRecessionProbability = it }
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Soft Landing Base: ${(100 - simulatedRecessionProbability).toInt()}%", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("Contraction Probability: ${simulatedRecessionProbability.toInt()}%", color = AmberOrange, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            RechartsAreaChart(
                title = "Historical Recession Risk vs Monetary Tightening",
                data = recessionHistory,
                lineColor1 = CrimsonDanger,
                lineColor2 = GoldAccent,
                legend1 = "Recession Probability %",
                legend2 = "Policy Rate Stress Index"
            )
        }

        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Navy900), shape = RoundedCornerShape(14.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("KEY RECESSION LEADING INDICATORS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    MacroMetricRow("10Y - 2Y Yield Curve Spread", yieldCurveSpread, "Disinverting", EmeraldSuccess)
                    MacroMetricRow("Sahm Rule Recession Meter", sahmRuleTrigger, "Safe Zone", EmeraldSuccess)
                    MacroMetricRow("Conference Board CLI", "102.4 pts", "Inflecting Up", CyberCyan)
                    MacroMetricRow("High-Yield Credit Spreads", "320 bps", "Tight / No Stress", GoldAccent)
                }
            }
        }

        item {
            InsightCard(
                "Recession Forecast Verdict",
                "The global economy has entered a sustainable mid-cycle expansion. Strong household balance sheets and generative AI productivity tailwinds have insulated output against tight monetary conditions.",
                EmeraldSuccess
            )
        }
    }
}

// -------------------------------------------------------------
// PREDICTIVE ENGINE 2: INVESTMENT RISKS FORECAST
// -------------------------------------------------------------
@Composable
private fun PageInvestmentRisksForecast() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, AmberOrange.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Warning, contentDescription = null, tint = AmberOrange)
                            Text("Asset Class Risk Forecaster", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Surface(color = AmberOrange.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text("Moderate Volatility", color = AmberOrange, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Text("Predicts 6-12 month maximum drawdown risks across sectors and asset classes:", color = TextMuted, fontSize = 11.sp)
                }
            }
        }

        item {
            RiskSectorCard("Commercial Real Estate (Offices)", "High Drawdown Risk", "18% - 24% Exposure", CrimsonDanger, "Refinancing cliff at elevated cap rates.")
        }
        item {
            RiskSectorCard("High-Yield Corporate Credit", "Moderate Risk", "4% - 7% Drawdown Buffer", AmberOrange, "Default rates contained below 3.5%.")
        }
        item {
            RiskSectorCard("Mega-Cap Tech & AI Platforms", "Low Structural Risk", "High Free Cashflow Moat", EmeraldSuccess, "Supported by $150B+ annual R&D investment.")
        }
        item {
            RiskSectorCard("Sovereign Short-Term Treasuries", "Zero Default Risk", "5.2% Locked Yield", CyberCyan, "Risk-free cash equivalent liquidity harbor.")
        }
    }
}

@Composable
private fun RiskSectorCard(sector: String, riskLevel: String, exposure: String, color: Color, note: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Navy900),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(sector, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Surface(color = color.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                    Text(riskLevel, color = color, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(note, color = TextMuted, fontSize = 10.sp, modifier = Modifier.weight(1f))
                Text(exposure, color = color, fontWeight = FontWeight.Bold, fontSize = 10.sp)
            }
        }
    }
}

// -------------------------------------------------------------
// PREDICTIVE ENGINE 3: PERSONAL FINANCIAL IMPACT PREDICTOR
// -------------------------------------------------------------
@Composable
private fun PagePersonalFinancialImpact() {
    var monthlySpend by remember { mutableDoubleStateOf(4200.0) }
    val groceryInflationDelta = monthlySpend * 0.28 * 0.032
    val energyInflationDelta = monthlySpend * 0.12 * 0.018
    val projectedYieldGain = monthlySpend * 1.5 * 0.052 / 12.0

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Person, contentDescription = null, tint = CyberCyan)
                            Text("Personal Financial Impact Solver", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text("Net Positive: +$22/mo", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Text("Translates global macro changes directly into your monthly household balance sheet:", color = TextMuted, fontSize = 11.sp)
                }
            }
        }

        // Interactive Spending Dial
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Navy900), shape = RoundedCornerShape(14.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Current Monthly Household Spend", color = TextMuted, fontSize = 10.sp)
                        Text("$${"%,.0f".format(monthlySpend)}/mo", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                    Slider(
                        value = monthlySpend.toFloat(),
                        onValueChange = { monthlySpend = it.toDouble() },
                        valueRange = 1500f..15000f,
                        steps = 27,
                        colors = SliderDefaults.colors(thumbColor = CyberCyan, activeTrackColor = CyberCyan, inactiveTrackColor = Navy700)
                    )
                }
            }
        }

        // Predicted Net Impact Cards
        item {
            Card(modifier = Modifier.fillMaxWidth(), colors = CardDefaults.cardColors(containerColor = Navy900), shape = RoundedCornerShape(14.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("PREDICTED MONTHLY IMPACT BREAKDOWN", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    MacroMetricRow("Grocery & Food Inflation Drag", "+$${"%.0f".format(groceryInflationDelta)}/mo", "Moderate", AmberOrange)
                    MacroMetricRow("Energy & Utility Surcharge", "+$${"%.0f".format(energyInflationDelta)}/mo", "Mild", AmberOrange)
                    MacroMetricRow("High-Yield Cash Vault Surplus", "+$${"%.0f".format(projectedYieldGain)}/mo", "Outpacing Inflation", EmeraldSuccess)
                }
            }
        }

        item {
            InsightCard(
                "Autonomous Household Shield",
                "Your SmartBank 5.2% APY savings vault generates +$${"%.0f".format(projectedYieldGain)}/mo in passive cash yield, completely neutralizing the +$${"%.0f".format(groceryInflationDelta + energyInflationDelta)}/mo total inflation drag.",
                EmeraldSuccess
            )
        }
    }
}

// -------------------------------------------------------------
// PREDICTIVE ENGINE 4: BUSINESS OPPORTUNITIES
// -------------------------------------------------------------
@Composable
private fun PageBusinessOpportunities() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Lightbulb, contentDescription = null, tint = EmeraldSuccess)
                            Text("Macro Business & Venture Opportunities", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text("4 High-Conviction Plays", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 10.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Text("High-upside commercial ventures capitalizing on emerging macroeconomic shifts:", color = TextMuted, fontSize = 11.sp)
                }
            }
        }

        item {
            OpportunityCard(
                title = "AI Enterprise Workflow Automation",
                marketSize = "$1.3T TAM by 2032",
                roiProjection = "3.8x - 6.2x Return",
                theme = "Productivity Arbitrage",
                statusColor = EmeraldSuccess,
                summary = "Automating legacy banking, legal, and compliance pipelines to extract 60% operating margin improvements."
            )
        }

        item {
            OpportunityCard(
                title = "Nearshoring & Cross-Border Logistics Hubs",
                marketSize = "$320B Shift",
                roiProjection = "18% - 24% Unlevered IRR",
                theme = "Supply Chain Re-engineering",
                statusColor = CyberCyan,
                summary = "Industrial warehousing and cold-chain infrastructure along US-Mexico and ASEAN manufacturing belts."
            )
        }

        item {
            OpportunityCard(
                title = "Decentralized Clean Grid & Micro-Storage",
                marketSize = "$850B Market",
                roiProjection = "14% - 19% Long-Term Yield",
                theme = "Climate Transition",
                statusColor = GoldAccent,
                summary = "Commercial solar + sodium-ion battery microgrids shielding data centers from grid volatility."
            )
        }

        item {
            OpportunityCard(
                title = "Distressed Commercial Asset Repurposing",
                marketSize = "$120B Secondary Pool",
                roiProjection = "2.4x Multiple on Capital",
                theme = "Counter-Cyclical Arbitrage",
                statusColor = PurpleTech,
                summary = "Acquiring marked-down suburban office properties for conversion to life sciences and residential trusts."
            )
        }
    }
}

@Composable
private fun OpportunityCard(title: String, marketSize: String, roiProjection: String, theme: String, statusColor: Color, summary: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Navy900),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Surface(color = statusColor.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                    Text(theme, color = statusColor, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Market: $marketSize", color = TextMuted, fontSize = 10.sp)
                Text("ROI: $roiProjection", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 10.sp)
            }
            Text(summary, color = TextWhite.copy(alpha = 0.85f), fontSize = 10.sp, lineHeight = 14.sp)
        }
    }
}

// -------------------------------------------------------------
// REUSABLE HELPER UI COMPONENTS
// -------------------------------------------------------------
@Composable
private fun MacroHeaderCard(title: String, subtitle: String, icon: ImageVector, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier.size(36.dp).clip(CircleShape).background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(subtitle, color = TextMuted, fontSize = 10.sp)
            }
        }
    }
}

@Composable
private fun MacroMetricRow(label: String, value: String, status: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = TextWhite.copy(alpha = 0.9f), fontSize = 11.sp)
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(value, color = color, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            Surface(color = color.copy(alpha = 0.15f), shape = RoundedCornerShape(4.dp)) {
                Text(status, color = color, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
            }
        }
    }
}

@Composable
private fun InsightCard(title: String, text: String, color: Color) {
    Surface(
        color = NavyCard,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = color, modifier = Modifier.size(14.dp))
                Text(title, color = color, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
            Text(text, color = TextWhite.copy(alpha = 0.85f), fontSize = 10.sp, lineHeight = 14.sp)
        }
    }
}

// -------------------------------------------------------------
// RECHARTS-STYLE CUSTOM COMPOSE VISUALIZERS
// -------------------------------------------------------------

data class ChartPoint(
    val label: String,
    val value1: Float,
    val value2: Float
)

data class BarGroup(
    val region: String,
    val val1: Float,
    val val2: Float
)

@Composable
fun RechartsAreaChart(
    title: String,
    data: List<ChartPoint>,
    lineColor1: Color,
    lineColor2: Color,
    legend1: String,
    legend2: String,
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableStateOf<Int?>(data.size - 2) }

    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Navy900),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp), verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(lineColor1))
                        Text(legend1, color = TextMuted, fontSize = 9.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(lineColor2))
                        Text(legend2, color = TextMuted, fontSize = 9.sp)
                    }
                }
            }

            // Interactive Canvas Area Chart
            Box(modifier = Modifier.fillMaxWidth().height(160.dp)) {
                val maxVal = remember(data) {
                    val max1 = data.maxOfOrNull { it.value1 } ?: 10f
                    val max2 = data.maxOfOrNull { it.value2 } ?: 10f
                    maxOf(max1, max2) * 1.15f
                }

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    val w = size.width
                    val h = size.height
                    val stepX = if (data.size > 1) w / (data.size - 1) else w

                    // Draw 3 horizontal dotted grid lines
                    for (i in 0..3) {
                        val y = h - (h * (i / 3f))
                        drawLine(
                            color = Navy700.copy(alpha = 0.6f),
                            start = Offset(0f, y),
                            end = Offset(w, y),
                            strokeWidth = 1.dp.toPx()
                        )
                    }

                    // Build Paths for Line 1
                    val path1 = Path()
                    val areaPath1 = Path()
                    areaPath1.moveTo(0f, h)

                    data.forEachIndexed { index, pt ->
                        val x = index * stepX
                        val y = h - ((pt.value1 / maxVal) * h)
                        if (index == 0) {
                            path1.moveTo(x, y)
                            areaPath1.lineTo(x, y)
                        } else {
                            val prevX = (index - 1) * stepX
                            val prevY = h - ((data[index - 1].value1 / maxVal) * h)
                            val cX1 = prevX + (x - prevX) / 2
                            val cX2 = prevX + (x - prevX) / 2
                            path1.cubicTo(cX1, prevY, cX2, y, x, y)
                            areaPath1.cubicTo(cX1, prevY, cX2, y, x, y)
                        }
                    }
                    areaPath1.lineTo((data.size - 1) * stepX, h)
                    areaPath1.close()

                    // Draw Area Gradient 1
                    drawPath(
                        path = areaPath1,
                        brush = Brush.verticalGradient(
                            colors = listOf(lineColor1.copy(alpha = 0.35f), Color.Transparent),
                            startY = 0f,
                            endY = h
                        )
                    )
                    drawPath(
                        path = path1,
                        color = lineColor1,
                        style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Build Paths for Line 2
                    val path2 = Path()
                    data.forEachIndexed { index, pt ->
                        val x = index * stepX
                        val y = h - ((pt.value2 / maxVal) * h)
                        if (index == 0) {
                            path2.moveTo(x, y)
                        } else {
                            val prevX = (index - 1) * stepX
                            val prevY = h - ((data[index - 1].value2 / maxVal) * h)
                            val cX1 = prevX + (x - prevX) / 2
                            val cX2 = prevX + (x - prevX) / 2
                            path2.cubicTo(cX1, prevY, cX2, y, x, y)
                        }
                    }
                    drawPath(
                        path = path2,
                        color = lineColor2,
                        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Draw data nodes and highlight line if selected
                    selectedIndex?.let { selIdx ->
                        if (selIdx in data.indices) {
                            val selX = selIdx * stepX
                            drawLine(
                                color = TextWhite.copy(alpha = 0.5f),
                                start = Offset(selX, 0f),
                                end = Offset(selX, h),
                                strokeWidth = 1.dp.toPx()
                            )

                            val y1 = h - ((data[selIdx].value1 / maxVal) * h)
                            drawCircle(color = TextWhite, radius = 5.dp.toPx(), center = Offset(selX, y1))
                            drawCircle(color = lineColor1, radius = 3.dp.toPx(), center = Offset(selX, y1))

                            val y2 = h - ((data[selIdx].value2 / maxVal) * h)
                            drawCircle(color = TextWhite, radius = 5.dp.toPx(), center = Offset(selX, y2))
                            drawCircle(color = lineColor2, radius = 3.dp.toPx(), center = Offset(selX, y2))
                        }
                    }
                }
            }

            // X-Axis Labels row (clickable nodes)
            Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                data.forEachIndexed { idx, pt ->
                    val isSel = selectedIndex == idx
                    Text(
                        text = pt.label,
                        color = if (isSel) CyberCyan else TextMuted,
                        fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                        fontSize = 10.sp,
                        modifier = Modifier.clickable { selectedIndex = idx }
                    )
                }
            }

            // Tooltip Summary Bar
            selectedIndex?.let { idx ->
                if (idx in data.indices) {
                    val pt = data[idx]
                    Surface(
                        color = NavyCard,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Node: ${pt.label}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Text("$legend1: ${pt.value1}%", color = lineColor1, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                                Text("$legend2: ${pt.value2}%", color = lineColor2, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RechartsBarChart(
    title: String,
    bars: List<BarGroup>,
    color1: Color,
    color2: Color,
    label1: String,
    label2: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Navy900),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(2.dp)).background(color1))
                        Text(label1, color = TextMuted, fontSize = 9.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                        Box(modifier = Modifier.size(8.dp).clip(RoundedCornerShape(2.dp)).background(color2))
                        Text(label2, color = TextMuted, fontSize = 9.sp)
                    }
                }
            }

            val maxVal = remember(bars) {
                val m1 = bars.maxOfOrNull { it.val1 } ?: 10f
                val m2 = bars.maxOfOrNull { it.val2 } ?: 10f
                maxOf(m1, m2) * 1.2f
            }

            Box(modifier = Modifier.fillMaxWidth().height(140.dp)) {
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 8.dp, vertical = 8.dp)
                ) {
                    val w = size.width
                    val h = size.height
                    val groupWidth = w / bars.size
                    val barWidth = 10.dp.toPx()

                    bars.forEachIndexed { index, group ->
                        val groupCenterX = (index * groupWidth) + (groupWidth / 2)
                        val bar1X = groupCenterX - barWidth - 2.dp.toPx()
                        val bar2X = groupCenterX + 2.dp.toPx()

                        val h1 = (group.val1 / maxVal) * h
                        val h2 = (group.val2 / maxVal) * h

                        // Bar 1
                        drawRoundRect(
                            color = color1,
                            topLeft = Offset(bar1X, h - h1),
                            size = Size(barWidth, h1),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx(), 3.dp.toPx())
                        )

                        // Bar 2
                        drawRoundRect(
                            color = color2,
                            topLeft = Offset(bar2X, h - h2),
                            size = Size(barWidth, h2),
                            cornerRadius = androidx.compose.ui.geometry.CornerRadius(3.dp.toPx(), 3.dp.toPx())
                        )
                    }
                }
            }

            // Region Labels Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                bars.forEach { b ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(b.region, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        Text("${b.val1}% / ${b.val2}%", color = TextMuted, fontSize = 8.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun RechartsRecessionGauge(
    probability: Float,
    onProbabilityChange: (Float) -> Unit,
    modifier: Modifier = Modifier
) {
    val animatedProb by animateFloatAsState(
        targetValue = probability,
        animationSpec = tween(durationMillis = 800),
        label = "probAnim"
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Box(
            modifier = Modifier.size(200.dp, 100.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height
                val radius = w / 2f - 16.dp.toPx()
                val center = Offset(w / 2f, h)

                // Background arc (180 degrees)
                drawArc(
                    color = Navy700,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                )

                // Active Gradient arc based on probability
                val sweep = (animatedProb / 100f) * 180f
                val arcColor = if (animatedProb < 30f) EmeraldSuccess else if (animatedProb < 60f) AmberOrange else CrimsonDanger

                drawArc(
                    color = arcColor,
                    startAngle = 180f,
                    sweepAngle = sweep,
                    useCenter = false,
                    topLeft = Offset(center.x - radius, center.y - radius),
                    size = Size(radius * 2, radius * 2),
                    style = Stroke(width = 14.dp.toPx(), cap = StrokeCap.Round)
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(0.dp)
            ) {
                Text(
                    text = "${animatedProb.toInt()}%",
                    color = if (animatedProb < 30f) EmeraldSuccess else if (animatedProb < 60f) AmberOrange else CrimsonDanger,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 24.sp
                )
                Text(
                    text = "CONTRACTION PROBABILITY",
                    color = TextMuted,
                    fontSize = 8.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        // Slider to simulate shock stress test
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text("Simulate Shock:", color = TextMuted, fontSize = 10.sp)
            Slider(
                value = probability,
                onValueChange = onProbabilityChange,
                valueRange = 5f..95f,
                modifier = Modifier.weight(1f),
                colors = SliderDefaults.colors(
                    thumbColor = GoldAccent,
                    activeTrackColor = if (probability < 30f) EmeraldSuccess else if (probability < 60f) AmberOrange else CrimsonDanger
                )
            )
        }
    }
}

