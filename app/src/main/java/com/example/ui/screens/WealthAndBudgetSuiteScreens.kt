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

enum class WealthBudgetPageType(val title: String, val icon: ImageVector, val code: String) {
    // Module 10: AI Wealth Management (8)
    WEALTH_DASHBOARD("Wealth 360°", Icons.Default.AccountBalanceWallet, "WEALTH"),
    SAVINGS_PLANNER("Savings Planner", Icons.Default.Savings, "SAVINGS"),
    INVESTMENT_PLANNER("Investment Planner", Icons.Default.TrendingUp, "INVEST"),
    RETIREMENT_PLANNER("Retirement", Icons.Default.BeachAccess, "RETIRE"),
    TAX_OPTIMIZATION("Tax Optimizer", Icons.Default.Policy, "TAX"),
    EXPENSE_OPTIMIZER("Expense AI", Icons.Default.AutoAwesome, "EXPENSE"),
    PASSIVE_INCOME("Passive Income", Icons.Default.AttachMoney, "PASSIVE"),
    NET_WORTH_CALC("Net Worth Calc", Icons.Default.Calculate, "NETWORTH"),

    // Module 11: Budget Management (7)
    BUDGET_DASHBOARD("Budget Hub", Icons.Default.PieChart, "BUDGET"),
    MONTHLY_BUDGET("Monthly Ledger", Icons.Default.CalendarMonth, "MONTHLY"),
    EXPENSE_CATEGORIES("Categories", Icons.Default.Category, "CAT"),
    GOAL_TRACKING("Goal Tracker", Icons.Default.Flag, "GOALS"),
    SAVINGS_GOALS("Savings Targets", Icons.Default.Star, "TARGETS"),
    SPENDING_ALERTS("Spending Alerts", Icons.Default.NotificationsActive, "ALERTS"),
    SMART_RECOMMENDATIONS("Smart Tips", Icons.Default.Lightbulb, "TIPS")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WealthAndBudgetSuiteScreen(
    viewModel: BankViewModel,
    initialPage: WealthBudgetPageType = WealthBudgetPageType.WEALTH_DASHBOARD,
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
                                .background(Brush.linearGradient(listOf(GoldAccent, AmberOrange))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.AccountBalanceWallet, contentDescription = null, tint = Navy900, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "WEALTH & BUDGET ENGINE",
                                color = GoldAccent,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                "Autonomous Capital Allocation",
                                color = TextWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    AssistChip(
                        onClick = {},
                        label = { Text("$%,.0f".format(totalBalance), fontSize = 11.sp, color = GoldAccent, fontWeight = FontWeight.Bold) },
                        colors = AssistChipDefaults.assistChipColors(containerColor = GoldAccent.copy(alpha = 0.15f)),
                        border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.4f))
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // --- Horizontal Selector Bar ---
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(WealthBudgetPageType.values()) { page ->
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
                                    tint = if (isSelected) Navy900 else GoldAccent
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = GoldAccent,
                                selectedLabelColor = Navy900,
                                containerColor = Navy700,
                                labelColor = TextWhite
                            ),
                            border = BorderStroke(1.dp, if (isSelected) GoldAccent else Navy700)
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
            label = "WealthBudgetPageTransition",
            modifier = Modifier.fillMaxSize()
        ) { targetPage ->
            when (targetPage) {
                WealthBudgetPageType.WEALTH_DASHBOARD -> WealthDashboardScreen(viewModel)
                WealthBudgetPageType.SAVINGS_PLANNER -> SavingsPlannerScreen(viewModel)
                WealthBudgetPageType.INVESTMENT_PLANNER -> InvestmentPlannerScreen(viewModel)
                WealthBudgetPageType.RETIREMENT_PLANNER -> RetirementPlannerScreen(viewModel)
                WealthBudgetPageType.TAX_OPTIMIZATION -> TaxOptimizationScreen(viewModel)
                WealthBudgetPageType.EXPENSE_OPTIMIZER -> ExpenseOptimizerScreen(viewModel)
                WealthBudgetPageType.PASSIVE_INCOME -> PassiveIncomePlannerScreen(viewModel)
                WealthBudgetPageType.NET_WORTH_CALC -> NetWorthCalculatorScreen(viewModel)
                WealthBudgetPageType.BUDGET_DASHBOARD -> BudgetDashboardScreen(viewModel)
                WealthBudgetPageType.MONTHLY_BUDGET -> MonthlyBudgetScreen(viewModel)
                WealthBudgetPageType.EXPENSE_CATEGORIES -> BudgetExpenseCategoriesScreen(viewModel)
                WealthBudgetPageType.GOAL_TRACKING -> GoalTrackingScreen(viewModel)
                WealthBudgetPageType.SAVINGS_GOALS -> SavingsGoalsScreen(viewModel)
                WealthBudgetPageType.SPENDING_ALERTS -> SpendingAlertsScreen(viewModel)
                WealthBudgetPageType.SMART_RECOMMENDATIONS -> SmartBudgetRecommendationsScreen(viewModel)
            }
        }
    }
}

// -------------------------------------------------------------------------
// 1. WEALTH DASHBOARD SCREEN
// -------------------------------------------------------------------------
@Composable
fun WealthDashboardScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val totalBalance by viewModel.totalNetWorth.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, GoldAccent.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Consolidated Net Worth Engine", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text("$%,.2f USD".format(totalBalance), color = TextWhite, fontSize = 32.sp, fontWeight = FontWeight.Black)
                    Text("+$4,820.00 (+3.4%) past 30 days", color = EmeraldSuccess, fontSize = 12.sp, fontWeight = FontWeight.Bold)

                    Spacer(modifier = Modifier.height(16.dp))
                    SectorProgressRow("Liquid Cash & Yield Vaults (35%)", 0.35f, CyberCyan, "$175,000")
                    SectorProgressRow("Equities & Index Funds (40%)", 0.40f, ElectricBlue, "$200,000")
                    SectorProgressRow("Real Estate Equity (15%)", 0.15f, GoldAccent, "$75,000")
                    SectorProgressRow("Digital Assets & Gold (10%)", 0.10f, PurpleTech, "$50,000")
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 2. SAVINGS PLANNER SCREEN
// -------------------------------------------------------------------------
@Composable
fun SavingsPlannerScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var monthlySave by remember { mutableStateOf("1500") }
    val annualSave = (monthlySave.toDoubleOrNull() ?: 0.0) * 12

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
                    Text("Autonomous High-Yield Savings Planner", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = monthlySave,
                        onValueChange = { monthlySave = it },
                        label = { Text("Target Monthly Savings Contribution ($)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, unfocusedBorderColor = Navy700, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                    )

                    Spacer(modifier = Modifier.height(14.dp))
                    DetailItemRow("Annualized Direct Savings", "$%,.2f".format(annualSave), EmeraldSuccess)
                    DetailItemRow("Estimated 4.85% APY Compound", "$%,.2f".format(annualSave * 1.0485), GoldAccent)
                    DetailItemRow("5-Year Compounded Vault Value", "$%,.2f".format(annualSave * 5.75), CyberCyan)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 3. INVESTMENT PLANNER SCREEN
// -------------------------------------------------------------------------
@Composable
fun InvestmentPlannerScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
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
                    Text("Automated Dollar-Cost Averaging (DCA)", color = ElectricBlue, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    DetailItemRow("S&P 500 Index ETF (VOO)", "$800 / mo", CyberCyan)
                    DetailItemRow("All-World Ex-US (VXUS)", "$400 / mo", ElectricBlue)
                    DetailItemRow("US Treasury 10Y Bonds", "$300 / mo", GoldAccent)
                    DetailItemRow("Bitcoin / Ethereum Bluechip", "$250 / mo", PurpleTech)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 4. RETIREMENT PLANNER SCREEN
// -------------------------------------------------------------------------
@Composable
fun RetirementPlannerScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
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
                    Text("401(k), IRA & Mega Backdoor Roth Planner", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    DetailItemRow("Employer 401(k) Match", "100% Match up to 6%", EmeraldSuccess)
                    DetailItemRow("Annual Roth IRA Contribution", "$7,000 (Maxed)", CyberCyan)
                    DetailItemRow("Projected Retirement Nest Egg", "$3,850,000.00", GoldAccent)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 5. TAX OPTIMIZATION SCREEN
// -------------------------------------------------------------------------
@Composable
fun TaxOptimizationScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
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
                    Text("Automated Tax-Loss Harvesting & Shelters", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    DetailItemRow("Harvestable Capital Losses", "$3,450.00", EmeraldSuccess)
                    DetailItemRow("HSA Triple-Tax Advantage", "$4,150.00 Maxed", CyberCyan)
                    DetailItemRow("Estimated Tax Saved This Year", "$8,920.00", GoldAccent)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 6. EXPENSE OPTIMIZER SCREEN
// -------------------------------------------------------------------------
@Composable
fun ExpenseOptimizerScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
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
                    Text("AI Subscription & Recurring Expense Optimizer", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    TwinActionItem("Cancel Unused Streaming", "2 unused streaming services detected", "Save $38/mo")
                    TwinActionItem("Auto-Negotiate Telecom Bill", "Lower fiber optic internet tier", "Save $45/mo")
                    TwinActionItem("Switch Insurance Underwriter", "Identified identical coverage tier", "Save $85/mo")
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 7. PASSIVE INCOME PLANNER SCREEN
// -------------------------------------------------------------------------
@Composable
fun PassiveIncomePlannerScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
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
                    Text("Passive Income Cashflow Engine", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("$4,250.00 / month", color = EmeraldSuccess, fontSize = 28.sp, fontWeight = FontWeight.Black)

                    Spacer(modifier = Modifier.height(14.dp))
                    DetailItemRow("Dividend Aristocrats Portfolio", "$1,650 / mo", CyberCyan)
                    DetailItemRow("Treasury Staking & Yield Farms", "$1,400 / mo", GoldAccent)
                    DetailItemRow("Fractional Real Estate Syndication", "$1,200 / mo", ElectricBlue)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 8. NET WORTH CALCULATOR SCREEN
// -------------------------------------------------------------------------
@Composable
fun NetWorthCalculatorScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var assetsInput by remember { mutableStateOf("620000") }
    var liabilitiesInput by remember { mutableStateOf("120000") }

    val a = assetsInput.toDoubleOrNull() ?: 0.0
    val l = liabilitiesInput.toDoubleOrNull() ?: 0.0
    val netWorth = a - l

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
                    Text("Interactive Net Worth Calculator", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedTextField(
                        value = assetsInput,
                        onValueChange = { assetsInput = it },
                        label = { Text("Total Assets ($)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldSuccess, unfocusedBorderColor = Navy700, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = liabilitiesInput,
                        onValueChange = { liabilitiesInput = it },
                        label = { Text("Total Liabilities ($)") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CrimsonDanger, unfocusedBorderColor = Navy700, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Navy700),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Calculated Total Net Worth", color = TextMuted, fontSize = 11.sp)
                            Text("$%,.2f USD".format(netWorth), color = GoldAccent, fontSize = 24.sp, fontWeight = FontWeight.Black)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 9. BUDGET DASHBOARD SCREEN
// -------------------------------------------------------------------------
@Composable
fun BudgetDashboardScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
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
                    Text("50/30/20 Rule Health Check", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    SectorProgressRow("Needs (Target 50% | Actual 42%)", 0.42f, EmeraldSuccess, "$4,200 / $5,000")
                    SectorProgressRow("Wants (Target 30% | Actual 24%)", 0.24f, CyberCyan, "$2,400 / $3,000")
                    SectorProgressRow("Savings & Investments (Target 20% | Actual 34%)", 0.34f, GoldAccent, "$3,400 / $2,000")
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 10. MONTHLY BUDGET SCREEN
// -------------------------------------------------------------------------
@Composable
fun MonthlyBudgetScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Monthly Budget Allocations", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
        val items = listOf(
            Triple("Housing & Mortgage", "$2,800.00 Spent of $2,800.00", EmeraldSuccess),
            Triple("Food & Fine Dining", "$950.00 Spent of $1,400.00", CyberCyan),
            Triple("Transportation & Fuel", "$320.00 Spent of $600.00", EmeraldSuccess),
            Triple("Entertainment & Tech", "$420.00 Spent of $800.00", ElectricBlue)
        )
        items(items) { (cat, budget, col) ->
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
                    Text(cat, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(budget, color = col, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 11. EXPENSE CATEGORIES SCREEN
// -------------------------------------------------------------------------
@Composable
fun BudgetExpenseCategoriesScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Active Expense Rules & Caps", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
        items(listOf("Essential Housing", "Health & Wellness", "Travel & Aviation", "Software & Cloud", "Education & Books")) { name ->
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
                    Text(name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("Active Rule", color = EmeraldSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 12. GOAL TRACKING SCREEN
// -------------------------------------------------------------------------
@Composable
fun GoalTrackingScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
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
                    Text("Strategic Financial Goals", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    SectorProgressRow("Primary Home Downpayment ($100k)", 0.85f, GoldAccent, "$85,000 (85%)")
                    SectorProgressRow("Private Angel Investment Pool ($50k)", 0.60f, CyberCyan, "$30,000 (60%)")
                    SectorProgressRow("New EV Purchase ($45k)", 0.95f, EmeraldSuccess, "$42,750 (95%)")
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 13. SAVINGS GOALS SCREEN
// -------------------------------------------------------------------------
@Composable
fun SavingsGoalsScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
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
                    Text("Savings Goal Vaults", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    DetailItemRow("Emergency Runway Vault", "$50,000 (100% Complete)", EmeraldSuccess)
                    DetailItemRow("World Travel Sabbatical", "$18,500 of $20,000", CyberCyan)
                    DetailItemRow("Crypto Bull Run Dry Powder", "$35,000 of $40,000", GoldAccent)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 14. SPENDING ALERTS SCREEN
// -------------------------------------------------------------------------
@Composable
fun SpendingAlertsScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Real-Time Spending Velocity Alerts", color = AmberOrange, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
        val alerts = listOf(
            Pair("Dining Out Budget Exceeded 80%", "You have spent $800 of your $1,000 monthly allotment."),
            Pair("Unusual High Cloud Computing Charge", "AWS billed $420.00 today (normally $150.00)."),
            Pair("Annual Subscription Renewal Due", "Domain & Hosting renewals occurring in 3 days.")
        )
        items(alerts) { (title, desc) ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Text(title, color = AmberOrange, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(desc, color = TextMuted, fontSize = 12.sp)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 15. SMART BUDGET RECOMMENDATIONS SCREEN
// -------------------------------------------------------------------------
@Composable
fun SmartBudgetRecommendationsScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
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
                    Text("AI Autonomous Budget Optimizations", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    TwinActionItem("Rebalance 5% from Cash to Bond ETF", "Capture 5.2% locking yield before rate cuts", "+$480 Annual")
                    TwinActionItem("Consolidate Grocery Subscriptions", "Bundle delivery services for bulk rebate", "+$25/mo saved")
                    TwinActionItem("Activate Round-Up Staking", "Round up spare cents to Bitcoin vault", "+$65/mo DCA")
                }
            }
        }
    }
}
