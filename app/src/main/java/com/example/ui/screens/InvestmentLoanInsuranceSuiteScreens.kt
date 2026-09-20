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

enum class InvestLoanInsurancePageType(val title: String, val icon: ImageVector, val category: String) {
    // Module 12: Investment Intelligence
    INVEST_PORTFOLIO("Portfolio", Icons.Default.TrendingUp, "INVEST"),
    INVEST_STOCKS("Stocks", Icons.Default.ShowChart, "INVEST"),
    INVEST_CRYPTO("Crypto Assets", Icons.Default.CurrencyBitcoin, "INVEST"),
    INVEST_BONDS("Treasury Bonds", Icons.Default.Security, "INVEST"),
    INVEST_GOLD("Gold & Commodities", Icons.Default.Diamond, "INVEST"),
    INVEST_REALESTATE("Real Estate", Icons.Default.Apartment, "INVEST"),
    INVEST_ESG("ESG Green Fund", Icons.Default.Eco, "INVEST"),
    INVEST_RISK("Risk Analysis", Icons.Default.Speed, "INVEST"),

    // Module 13: Loan Intelligence
    LOAN_DASHBOARD("Loan Hub", Icons.Default.Calculate, "LOANS"),
    LOAN_ELIGIBILITY("Eligibility Checker", Icons.Default.CheckCircle, "LOANS"),
    LOAN_EMI_CALC("EMI Calculator", Icons.Default.Payments, "LOANS"),
    LOAN_AI_ADVISOR("Loan Advisor", Icons.Default.Psychology, "LOANS"),
    LOAN_REPAYMENT("Repayment Planner", Icons.Default.Schedule, "LOANS"),

    // Module 14: Insurance
    INSURANCE_DASHBOARD("Insurance 360°", Icons.Default.Policy, "INSURANCE"),
    INSURANCE_HEALTH("Health Policy", Icons.Default.HealthAndSafety, "INSURANCE"),
    INSURANCE_VEHICLE("Vehicle Shield", Icons.Default.DirectionsCar, "INSURANCE"),
    INSURANCE_PROPERTY("Property Cover", Icons.Default.Home, "INSURANCE"),
    INSURANCE_CLAIMS("Claims Management", Icons.Default.Assignment, "INSURANCE"),

    // Module 15: AI Financial Coach
    COACH_ASSISTANT("Personal AI Coach", Icons.Default.AutoAwesome, "COACH"),
    COACH_DAILY_INSIGHTS("Daily Insights", Icons.Default.Lightbulb, "COACH"),
    COACH_WEEKLY_REPORTS("Weekly Digest", Icons.Default.Description, "COACH"),
    COACH_EDUCATION("Financial Academy", Icons.Default.School, "COACH")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvestmentLoanInsuranceSuiteScreen(
    viewModel: BankViewModel,
    initialPage: InvestLoanInsurancePageType = InvestLoanInsurancePageType.INVEST_PORTFOLIO,
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
                                .background(Brush.linearGradient(listOf(EmeraldSuccess, CyberCyan))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.TrendingUp, contentDescription = null, tint = Navy900, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "INVESTMENTS • LOANS • INSURANCE • COACH",
                                color = EmeraldSuccess,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                "Capital & Risk Suite",
                                color = TextWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    AssistChip(
                        onClick = {},
                        label = { Text("Active Yield 12.8%", fontSize = 11.sp, color = EmeraldSuccess, fontWeight = FontWeight.Bold) },
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
                    items(InvestLoanInsurancePageType.values()) { page ->
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
                                    tint = if (isSelected) Navy900 else EmeraldSuccess
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldSuccess,
                                selectedLabelColor = Navy900,
                                containerColor = Navy700,
                                labelColor = TextWhite
                            ),
                            border = BorderStroke(1.dp, if (isSelected) EmeraldSuccess else Navy700)
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
            label = "InvestLoanPageTransition",
            modifier = Modifier.fillMaxSize()
        ) { targetPage ->
            when (targetPage) {
                InvestLoanInsurancePageType.INVEST_PORTFOLIO -> InvestPortfolioScreen(viewModel)
                InvestLoanInsurancePageType.INVEST_STOCKS -> InvestStocksScreen(viewModel)
                InvestLoanInsurancePageType.INVEST_CRYPTO -> InvestCryptoScreen(viewModel)
                InvestLoanInsurancePageType.INVEST_BONDS -> InvestBondsScreen(viewModel)
                InvestLoanInsurancePageType.INVEST_GOLD -> InvestGoldScreen(viewModel)
                InvestLoanInsurancePageType.INVEST_REALESTATE -> InvestRealEstateScreen(viewModel)
                InvestLoanInsurancePageType.INVEST_ESG -> InvestEsgScreen(viewModel)
                InvestLoanInsurancePageType.INVEST_RISK -> InvestRiskScreen(viewModel)
                InvestLoanInsurancePageType.LOAN_DASHBOARD -> LoanDashboardScreen(viewModel)
                InvestLoanInsurancePageType.LOAN_ELIGIBILITY -> LoanEligibilityScreen(viewModel)
                InvestLoanInsurancePageType.LOAN_EMI_CALC -> LoanEmiCalcScreen(viewModel)
                InvestLoanInsurancePageType.LOAN_AI_ADVISOR -> LoanAiAdvisorScreen(viewModel)
                InvestLoanInsurancePageType.LOAN_REPAYMENT -> LoanRepaymentScreen(viewModel)
                InvestLoanInsurancePageType.INSURANCE_DASHBOARD -> InsuranceDashboardScreen(viewModel)
                InvestLoanInsurancePageType.INSURANCE_HEALTH -> InsuranceHealthScreen(viewModel)
                InvestLoanInsurancePageType.INSURANCE_VEHICLE -> InsuranceVehicleScreen(viewModel)
                InvestLoanInsurancePageType.INSURANCE_PROPERTY -> InsurancePropertyScreen(viewModel)
                InvestLoanInsurancePageType.INSURANCE_CLAIMS -> InsuranceClaimsScreen(viewModel)
                InvestLoanInsurancePageType.COACH_ASSISTANT -> CoachAssistantScreen(viewModel)
                InvestLoanInsurancePageType.COACH_DAILY_INSIGHTS -> CoachDailyInsightsScreen(viewModel)
                InvestLoanInsurancePageType.COACH_WEEKLY_REPORTS -> CoachWeeklyReportsScreen(viewModel)
                InvestLoanInsurancePageType.COACH_EDUCATION -> CoachEducationScreen(viewModel)
            }
        }
    }
}

// -------------------------------------------------------------------------
// SCREENS IMPLEMENTATIONS
// -------------------------------------------------------------------------
@Composable
fun InvestPortfolioScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Total Portfolio Value", color = CyberCyan, fontSize = 12.sp)
                    Text("$485,250.00 USD", color = TextWhite, fontSize = 28.sp, fontWeight = FontWeight.Black)
                    Text("+$54,120.00 (+12.8% YTD)", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(14.dp))
                    SectorProgressRow("US Equities & Mega-Cap Tech", 0.45f, CyberCyan, "$218,362")
                    SectorProgressRow("Crypto & Web3 Liquidity", 0.25f, PurpleTech, "$121,312")
                    SectorProgressRow("US Treasury 5.2% Bonds", 0.20f, ElectricBlue, "$97,050")
                    SectorProgressRow("Physical Gold & Silver", 0.10f, GoldAccent, "$48,525")
                }
            }
        }
    }
}

@Composable
fun InvestStocksScreen(viewModel: BankViewModel) {
    val stocks = listOf(
        Triple("AAPL - Apple Inc.", "$224.50", "+1.8%"),
        Triple("NVDA - NVIDIA Corp.", "$128.40", "+4.2%"),
        Triple("MSFT - Microsoft Corp.", "$448.20", "+0.9%"),
        Triple("AMZN - Amazon.com Inc.", "$182.10", "+2.1%")
    )
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Top Tech Equities", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp) }
        items(stocks) { (sym, price, change) ->
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(sym, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Column(horizontalAlignment = Alignment.End) {
                        Text(price, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(change, color = EmeraldSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun InvestCryptoScreen(viewModel: BankViewModel) {
    val cryptos = listOf(
        Triple("Bitcoin (BTC)", "$64,250.00", "+3.8%"),
        Triple("Ethereum (ETH)", "$3,450.00", "+2.4%"),
        Triple("Solana (SOL)", "$148.20", "+6.1%"),
        Triple("Chainlink (LINK)", "$18.90", "+5.4%")
    )
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Bluechip Digital Assets", color = PurpleTech, fontWeight = FontWeight.Bold, fontSize = 15.sp) }
        items(cryptos) { (name, price, change) ->
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Column(horizontalAlignment = Alignment.End) {
                        Text(price, color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text(change, color = EmeraldSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable fun InvestBondsScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("US Treasury Yield Curve", color = ElectricBlue, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("4-Week T-Bill Yield", "5.28% APY", EmeraldSuccess)
                    DetailItemRow("2-Year Treasury Note", "4.62% APY", CyberCyan)
                    DetailItemRow("10-Year Benchmark Bond", "4.15% APY", GoldAccent)
                }
            }
        }
    }
}

@Composable fun InvestGoldScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Physical Gold & Rare Metals", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Spot Gold (XAU/USD)", "$2,480.50 / oz", GoldAccent)
                    DetailItemRow("Spot Silver (XAG/USD)", "$31.40 / oz", CyberCyan)
                    DetailItemRow("Allocated Swiss Vault Bar", "100.00% Fully Audited", EmeraldSuccess)
                }
            }
        }
    }
}

@Composable fun InvestRealEstateScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Fractional Real Estate Syndications", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Austin Tech Commercial Hub", "8.4% Cap Rate", EmeraldSuccess)
                    DetailItemRow("Miami Luxury Multi-Family", "7.9% Net Yield", CyberCyan)
                    DetailItemRow("Quarterly Rental Distributions", "$3,450.00 Paid", GoldAccent)
                }
            }
        }
    }
}

@Composable fun InvestEsgScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("ESG Clean Tech & Carbon Offsets", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Global Solar & Wind Fund", "+14.2% YTD", EmeraldSuccess)
                    DetailItemRow("Verified Carbon Credits", "120 Metric Tons Retired", CyberCyan)
                }
            }
        }
    }
}

@Composable fun InvestRiskScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Portfolio Value at Risk (VaR)", color = AmberOrange, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("99% Confidence 1-Day VaR", "-1.45% ($7,036 Max)", TextWhite)
                    DetailItemRow("Beta vs S&P 500", "0.82 (Defensive Alpha)", EmeraldSuccess)
                }
            }
        }
    }
}

@Composable fun LoanDashboardScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Active Loans & Collateralized Lines", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Fixed 30-Yr Mortgage", "$184,000 Remaining", TextWhite)
                    DetailItemRow("DeFi Crypto-Backed Credit Line", "$0 / $50,000 Drawn", EmeraldSuccess)
                }
            }
        }
    }
}

@Composable fun LoanEligibilityScreen(viewModel: BankViewModel) {
    GeminiLoanEligibilityScreen(viewModel = viewModel)
}

@Composable fun LoanEmiCalcScreen(viewModel: BankViewModel) {
    LoanSimulationScreen(viewModel)
}

@Composable fun LoanAiAdvisorScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("AI Mortgage Refinance Strategy", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("By refinancing your current mortgage at 3.95% fixed, you will save $385.00/month in interest, freeing up $4,620 annually for high-yield treasury reinvestment.", color = TextMuted, fontSize = 12.sp, lineHeight = 18.sp)
                }
            }
        }
    }
}

@Composable fun LoanRepaymentScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Accelerated Bi-Weekly Amortization", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Time Shaved Off Loan", "4 Years & 8 Months Early", EmeraldSuccess)
                    DetailItemRow("Total Interest Saved", "$42,800.00", GoldAccent)
                }
            }
        }
    }
}

@Composable fun InsuranceDashboardScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Global Umbrella Protection Coverage", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Total Policy Coverage Pool", "$2,500,000.00", EmeraldSuccess)
                    DetailItemRow("Annual Premium Optimized", "$2,150.00 / yr", CyberCyan)
                }
            }
        }
    }
}

@Composable fun InsuranceHealthScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Platinum Worldwide Medical Policy", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Deductible", "$1,500 Individual", TextWhite)
                    DetailItemRow("Global Evacuation & Trauma", "100% Covered", CyberCyan)
                }
            }
        }
    }
}

@Composable fun InsuranceVehicleScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Autonomous EV Comprehensive Shield", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Collision & Liability", "$1,000,000 Limit", EmeraldSuccess)
                    DetailItemRow("Telematics Safe Driving Discount", "-22% Premium Discount", GoldAccent)
                }
            }
        }
    }
}

@Composable fun InsurancePropertyScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Real Estate Hazard & Flood Cover", color = ElectricBlue, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Dwelling Replacement", "$750,000.00", TextWhite)
                    DetailItemRow("Valuable Personal Property Floater", "$100,000 Included", CyberCyan)
                }
            }
        }
    }
}

@Composable fun InsuranceClaimsScreen(viewModel: BankViewModel) {
    var claimDesc by remember { mutableStateOf("") }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Instant Smart Contract Claims Filing", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = claimDesc,
                        onValueChange = { claimDesc = it },
                        label = { Text("Incident Description & Policy ID") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, unfocusedBorderColor = Navy700, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { viewModel.showMessage("✓ Claim submitted with automated AI biometric verification.") },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = Navy900),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text("Submit Instant Claim", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable fun CoachAssistantScreen(viewModel: BankViewModel) {
    var userPrompt by remember { mutableStateOf("") }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("AI Financial Coach & Concierge", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Ask questions regarding multi-currency allocations, tax loss harvesting, mortgage interest deductions, or pension rollovers.", color = TextMuted, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = userPrompt,
                        onValueChange = { userPrompt = it },
                        label = { Text("Ask your financial coach...") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, unfocusedBorderColor = Navy700, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            viewModel.showMessage("AI Coach: Analyzing optimal treasury allocation for '$userPrompt'...")
                            userPrompt = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = Navy900),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text("Get Personalized Strategy", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable fun CoachDailyInsightsScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Today's Financial Actionables", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp) }
        items(listOf("Fed rate expectations shifted 25 bps. Rebalanced bond ladder.", "Earned $14.20 in overnight liquidity yield.", "Zero anomalous charges detected in last 24 hours.")) { insight ->
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                Text(insight, color = TextWhite, fontSize = 12.sp, modifier = Modifier.padding(14.dp))
            }
        }
    }
}

@Composable fun CoachWeeklyReportsScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Weekly Wealth Velocity Summary", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Weekly Net Cashflow", "+$1,420.00", EmeraldSuccess)
                    DetailItemRow("Portfolio Performance", "+1.2% ($5,820.00)", CyberCyan)
                }
            }
        }
    }
}

@Composable fun CoachEducationScreen(viewModel: BankViewModel) {
    val modules = listOf(
        Pair("Advanced Zero-Coupon Bond Yield Curve Mechanics", "Master Treasury Arbitrage • 15 min"),
        Pair("Cross-Border Foreign Exchange Hedging", "Derivatives & Forwards • 20 min"),
        Pair("Zero-Knowledge Cryptographic Auditing", "ZKP Financial Privacy • 12 min")
    )
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Financial Mastery Academy", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp) }
        items(modules) { (title, subtitle) ->
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(subtitle, color = CyberCyan, fontSize = 11.sp)
                }
            }
        }
    }
}
