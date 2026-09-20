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
import android.os.Build
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.core.content.ContextCompat
import com.example.data.model.NotificationType
import com.example.service.BudgetAlertLevel
import com.example.service.BudgetAlertResult
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
// 12. GOAL TRACKING SCREEN (ROOM BUDGET GOAL SENTINEL & WORKER)
// -------------------------------------------------------------------------
@Composable
fun GoalTrackingScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val budgetGoals by viewModel.budgetGoalsList.collectAsStateWithLifecycle()
    val alertResults by viewModel.budgetAlertResults.collectAsStateWithLifecycle()

    var hasNotificationPermission by remember {
        mutableStateOf(
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ContextCompat.checkSelfPermission(
                    context,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            } else true
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        hasNotificationPermission = granted
        if (granted) {
            viewModel.showMessage("Notification permission granted.")
            viewModel.checkBudgetGoals(forceNotify = true)
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Sentinel & Background Worker Control Center Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth().testTag("budget_sentinel_panel")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                "Budget Sentinel & Worker",
                                color = CyberCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            Text(
                                "Checks Room BudgetGoals vs transactions and sends system notifications on approaching (≥80%) or exceeding (≥100%) targets.",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = { permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS) },
                            colors = ButtonDefaults.buttonColors(containerColor = AmberOrange),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("enable_notifications_btn")
                        ) {
                            Icon(Icons.Default.NotificationsActive, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Enable System Notifications", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                }
                                viewModel.triggerBudgetWorkerNow()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).testTag("check_budget_goals_button")
                        ) {
                            Icon(Icons.Default.Sync, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Run Worker", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        OutlinedButton(
                            onClick = {
                                if (!hasNotificationPermission && Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    permissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
                                }
                                viewModel.triggerBudgetServiceNow()
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                            border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).testTag("run_service_check_button")
                        ) {
                            Icon(Icons.Default.Sensors, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Run Service", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        item {
            Text(
                "Active Room Budget Targets (${budgetGoals.size})",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }

        if (budgetGoals.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Navy800),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp).fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("No Budget Goals stored in Room database.", color = TextMuted, fontSize = 13.sp)
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {
                                viewModel.addBudgetGoal("Food & Dining", 500.0, "End of Month", 420.0)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                        ) {
                            Text("Seed Food Budget Goal")
                        }
                    }
                }
            }
        }

        items(budgetGoals, key = { it.id }) { goal ->
            val ratio = if (goal.targetAmount > 0) (goal.currentProgress / goal.targetAmount).toFloat() else 0f
            val isExceeded = ratio >= 1.0f
            val isApproaching = ratio >= 0.80f && ratio < 1.0f

            val statusColor = when {
                isExceeded -> CrimsonDanger
                isApproaching -> AmberOrange
                else -> EmeraldSuccess
            }

            val statusLabel = when {
                isExceeded -> "🚨 EXCEEDED TARGET"
                isApproaching -> "⚠️ APPROACHING (≥80%)"
                else -> "✓ ON TRACK"
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, statusColor.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth().testTag("budget_goal_card_${goal.id}")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(goal.category, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Text("Deadline: ${goal.deadline}", color = TextMuted, fontSize = 11.sp)
                        }

                        Surface(
                            color = statusColor.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, statusColor.copy(alpha = 0.4f))
                        ) {
                            Text(
                                statusLabel,
                                color = statusColor,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    LinearProgressIndicator(
                        progress = { ratio.coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = statusColor,
                        trackColor = Navy900
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            "Spent: $${"%,.2f".format(goal.currentProgress)}",
                            color = statusColor,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                        Text(
                            "Target: $${"%,.2f".format(goal.targetAmount)} (${(ratio * 100).toInt()}%)",
                            color = TextMuted,
                            fontSize = 12.sp
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = Navy600.copy(alpha = 0.5f), thickness = 0.5.dp)
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        FilledTonalButton(
                            onClick = {
                                viewModel.recordExpenseForBudgetGoal(goal.id, goal.category, 50.0)
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).testTag("add_spend_50_${goal.id}")
                        ) {
                            Text("+ $50 Spend", fontSize = 11.sp)
                        }

                        FilledTonalButton(
                            onClick = {
                                viewModel.recordExpenseForBudgetGoal(goal.id, goal.category, 150.0)
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).testTag("add_spend_150_${goal.id}")
                        ) {
                            Text("+ $150 Spend", fontSize = 11.sp)
                        }
                    }
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
// 14. SPENDING ALERTS SCREEN (REAL-TIME SENTINEL ALERTS)
// -------------------------------------------------------------------------
@Composable
fun SpendingAlertsScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val alerts by viewModel.budgetAlertResults.collectAsStateWithLifecycle()
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()
    val transactionAlerts = notifications.filter { it.type == NotificationType.TRANSACTION_ALERT }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    "Real-Time Spending Velocity Alerts",
                    color = AmberOrange,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                IconButton(
                    onClick = { viewModel.checkBudgetGoals(forceNotify = true) },
                    modifier = Modifier.testTag("refresh_spending_alerts_button")
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh Alerts", tint = CyberCyan)
                }
            }
        }

        if (alerts.none { it.level != BudgetAlertLevel.NORMAL } && transactionAlerts.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Navy800),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(36.dp))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("All Goals Within Safe Limits", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("The background worker is actively monitoring your Room spending.", color = TextMuted, fontSize = 12.sp)
                    }
                }
            }
        }

        items(alerts.filter { it.level != BudgetAlertLevel.NORMAL }, key = { "alert_${it.goalId}" }) { alert ->
            val isExceeded = alert.level == BudgetAlertLevel.EXCEEDED
            val color = if (isExceeded) CrimsonDanger else AmberOrange

            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, color.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth().testTag("spending_alert_card_${alert.goalId}")
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            alert.alertTitle,
                            color = color,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                        if (alert.notificationSent) {
                            Surface(
                                color = EmeraldSuccess.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text("NOTIFIED", color = EmeraldSuccess, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(alert.alertMessage, color = TextMuted, fontSize = 12.sp)
                }
            }
        }

        if (transactionAlerts.isNotEmpty()) {
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Dispatched System Alerts History", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
            items(transactionAlerts.take(10), key = { "history_${it.id}" }) { item ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Navy800.copy(alpha = 0.7f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Text(item.title, color = AmberOrange, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(item.message, color = TextMuted, fontSize = 11.sp)
                    }
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
