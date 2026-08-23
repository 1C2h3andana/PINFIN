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

enum class FinancialIntelPageType(val title: String, val icon: ImageVector, val code: String) {
    INTEL_SCORE("Intel Score", Icons.Default.AutoAwesome, "INTEL"),
    HEALTH_SCORE("Health Score", Icons.Default.HealthAndSafety, "HEALTH"),
    CREDIT_HEALTH("Credit Health", Icons.Default.Speed, "CREDIT"),
    DEBT_ANALYSIS("Debt Analysis", Icons.Default.MoneyOff, "DEBT"),
    INCOME_ANALYSIS("Income Streams", Icons.Default.TrendingUp, "INCOME"),
    SPENDING_ANALYSIS("Spending Deepdive", Icons.Default.PieChart, "SPEND"),
    WEALTH_GROWTH("Wealth Growth", Icons.Default.ShowChart, "GROWTH"),
    FINANCIAL_REPORTS("AI Reports", Icons.Default.Description, "REPORTS")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FinancialIntelligenceSuiteScreen(
    viewModel: BankViewModel,
    initialPage: FinancialIntelPageType = FinancialIntelPageType.INTEL_SCORE,
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
                                .background(Brush.linearGradient(listOf(CyberCyan, ElectricBlue))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Insights, contentDescription = null, tint = Navy900, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "FINANCIAL INTELLIGENCE",
                                color = CyberCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                "AI Score & Macro Diagnostics",
                                color = TextWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    AssistChip(
                        onClick = {},
                        label = { Text("Score: 885 / 1000", fontSize = 11.sp, color = EmeraldSuccess, fontWeight = FontWeight.Bold) },
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
                    items(FinancialIntelPageType.values()) { page ->
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
            label = "FinancialIntelPageTransition",
            modifier = Modifier.fillMaxSize()
        ) { targetPage ->
            when (targetPage) {
                FinancialIntelPageType.INTEL_SCORE -> IntelScoreScreen(viewModel)
                FinancialIntelPageType.HEALTH_SCORE -> HealthScoreScreen(viewModel)
                FinancialIntelPageType.CREDIT_HEALTH -> CreditHealthScreen(viewModel)
                FinancialIntelPageType.DEBT_ANALYSIS -> DebtAnalysisScreen(viewModel)
                FinancialIntelPageType.INCOME_ANALYSIS -> IncomeAnalysisScreen(viewModel)
                FinancialIntelPageType.SPENDING_ANALYSIS -> SpendingAnalysisScreen(viewModel)
                FinancialIntelPageType.WEALTH_GROWTH -> WealthGrowthScreen(viewModel)
                FinancialIntelPageType.FINANCIAL_REPORTS -> FinancialReportsScreen(viewModel)
            }
        }
    }
}

// -------------------------------------------------------------------------
// 1. INTEL SCORE SCREEN
// -------------------------------------------------------------------------
@Composable
fun IntelScoreScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
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
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("PFIN Global Financial IQ", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("885", color = EmeraldSuccess, fontSize = 48.sp, fontWeight = FontWeight.Black)
                    Text("Top 1.5% Global Financial Literacy & Resilience", color = TextMuted, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(16.dp))
                    SectorProgressRow("Liquidity Efficiency", 0.92f, CyberCyan, "92/100")
                    SectorProgressRow("Debt Optimization", 0.88f, EmeraldSuccess, "88/100")
                    SectorProgressRow("Diversification Quotient", 0.85f, GoldAccent, "85/100")
                    SectorProgressRow("Tax Shield Alpha", 0.89f, PurpleTech, "89/100")
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 2. HEALTH SCORE SCREEN
// -------------------------------------------------------------------------
@Composable
fun HealthScoreScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
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
                    Text("Holistic Health Diagnostic", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    DetailItemRow("Emergency Runway", "14.2 Months (Optimal)", EmeraldSuccess)
                    DetailItemRow("Savings-to-Income Ratio", "34.5% (High)", CyberCyan)
                    DetailItemRow("Net Cashflow Velocity", "+$6,200.00 / mo", EmeraldSuccess)
                    DetailItemRow("Systemic Vulnerability Index", "Low (0.04)", GoldAccent)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 3. CREDIT HEALTH SCREEN
// -------------------------------------------------------------------------
@Composable
fun CreditHealthScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
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
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Decentralized & FICO Credit Score", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("EXCELLENT", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("815", color = TextWhite, fontSize = 36.sp, fontWeight = FontWeight.Black)

                    Spacer(modifier = Modifier.height(14.dp))
                    DetailItemRow("Credit Utilization", "8.2% (Below 10% benchmark)", EmeraldSuccess)
                    DetailItemRow("On-Time Payment Record", "100% (48 consecutive months)", CyberCyan)
                    DetailItemRow("Credit History Age", "9.4 Years", TextWhite)
                    DetailItemRow("Hard Inquiries (Last 12mo)", "0 Inquiries", EmeraldSuccess)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 4. DEBT ANALYSIS SCREEN
// -------------------------------------------------------------------------
@Composable
fun DebtAnalysisScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
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
                    Text("Debt-to-Income (DTI) Portfolio", color = AmberOrange, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    DetailItemRow("Total Outstanding Liabilities", "$124,000.00", TextWhite)
                    DetailItemRow("DTI Ratio", "14.2% (Safely under 36%)", EmeraldSuccess)
                    DetailItemRow("Weighted Average APR", "3.85%", CyberCyan)
                    DetailItemRow("AI Debt Snowball Payoff Date", "June 2029 (Accelerated)", GoldAccent)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 5. INCOME ANALYSIS SCREEN
// -------------------------------------------------------------------------
@Composable
fun IncomeAnalysisScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Multi-Stream Revenue Breakdown", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
        val streams = listOf(
            Triple("Primary Tech & Consulting Salary", "$14,500.00 / mo", EmeraldSuccess),
            Triple("Treasury Staking & Bond Dividends", "$2,150.00 / mo", CyberCyan),
            Triple("Real Estate Rental Inflow", "$3,400.00 / mo", GoldAccent),
            Triple("IP & Digital Royalties", "$850.00 / mo", PurpleTech)
        )
        items(streams) { (title, amt, col) ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(amt, color = col, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 6. SPENDING ANALYSIS SCREEN
// -------------------------------------------------------------------------
@Composable
fun SpendingAnalysisScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
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
                    Text("30-Day Expense Velocity", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    SectorProgressRow("Housing & Utilities", 0.32f, CyberCyan, "$3,200")
                    SectorProgressRow("Food & Groceries", 0.18f, ElectricBlue, "$1,800")
                    SectorProgressRow("Mobility & Travel", 0.12f, GoldAccent, "$1,200")
                    SectorProgressRow("Discretionary & Leisure", 0.14f, AmberOrange, "$1,400")
                    SectorProgressRow("Automated Investments", 0.24f, EmeraldSuccess, "$2,400")
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 7. WEALTH GROWTH SCREEN
// -------------------------------------------------------------------------
@Composable
fun WealthGrowthScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
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
                    Text("Compound Annual Growth Rate (CAGR)", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("16.4% CAGR", color = TextWhite, fontSize = 32.sp, fontWeight = FontWeight.Black)
                    Text("Outperforming S&P 500 benchmark by +5.2%", color = CyberCyan, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(14.dp))
                    DetailItemRow("1-Year Growth", "+$34,800.00 (+18.2%)", EmeraldSuccess)
                    DetailItemRow("3-Year Growth", "+$112,400.00 (+54.6%)", CyberCyan)
                    DetailItemRow("All-Time Inception", "+$310,000.00 (+142.1%)", GoldAccent)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 8. FINANCIAL REPORTS SCREEN
// -------------------------------------------------------------------------
@Composable
fun FinancialReportsScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val reports = listOf(
        Pair("Q3 Comprehensive Wealth Audit", "Aug 2026 • 24 Pages • Signed"),
        Pair("Annual Tax Alpha & Loss Harvesting Pack", "Jul 2026 • 12 Pages • Form 1040"),
        Pair("Global Macro Vulnerability Assessment", "Jun 2026 • 18 Pages • ISO Certified")
    )

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Automated Certified AI Reports", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
        items(reports) { (title, meta) ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(meta, color = TextMuted, fontSize = 11.sp)
                    }
                    IconButton(onClick = { viewModel.showMessage("Downloading $title...") }) {
                        Icon(Icons.Default.Download, contentDescription = null, tint = CyberCyan)
                    }
                }
            }
        }
    }
}
