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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.Navy900
import com.example.ui.theme.NavyCard
import com.example.ui.theme.PurpleTech
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BankViewModel

enum class WealthEnginePage(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val tag: String
) {
    INVESTMENTS("Better Investments", Icons.Default.TrendingUp, CyberCyan, "tab_we_investments"),
    LOANS("Better Loans", Icons.Default.CreditCard, CrimsonDanger, "tab_we_loans"),
    INSURANCE("Better Insurance", Icons.Default.HealthAndSafety, ElectricBlue, "tab_we_insurance"),
    BILLS("Lower Bills", Icons.Default.ReceiptLong, AmberOrange, "tab_we_bills"),
    SAVINGS("Better Savings", Icons.Default.Savings, EmeraldSuccess, "tab_we_savings"),
    TAXES("Lower Taxes", Icons.Default.Percent, PurpleTech, "tab_we_taxes"),
    CASHBACK("Cashback & Rewards", Icons.Default.CardGiftcard, GoldAccent, "tab_we_cashback")
}

@Composable
fun UniversalWealthEngineScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    var selectedPage by remember { mutableStateOf(WealthEnginePage.INVESTMENTS) }
    var autoPilotEnabled by remember { mutableStateOf(true) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // --- Universal Wealth Engine Hero Banner ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.6f))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(GoldAccent.copy(alpha = 0.2f)),
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
                            Text(
                                text = "UNIVERSAL WEALTH ENGINE",
                                color = TextWhite,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )
                            PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 6.dp)
                        }
                        Text(
                            text = "Autonomous AI scans & captures multi-market yield, discounts, rebates & tax savings 24/7.",
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                // Auto-Pilot Execution Switch
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Navy900)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Speed, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                        Column {
                            Text("Autonomous Auto-Pilot Optimization", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(if (autoPilotEnabled) "AI executes approved threshold rebalances instantly" else "Manual review required for each action", color = TextMuted, fontSize = 9.sp)
                        }
                    }
                    Switch(
                        checked = autoPilotEnabled,
                        onCheckedChange = { autoPilotEnabled = it },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = TextWhite,
                            checkedTrackColor = EmeraldSuccess,
                            uncheckedThumbColor = TextMuted,
                            uncheckedTrackColor = Navy700
                        ),
                        modifier = Modifier.testTag("wealth_engine_autopilot_switch")
                    )
                }
            }
        }

        // --- 7 Separate Pages Scrollable Tab Navigation ---
        ScrollableTabRow(
            selectedTabIndex = WealthEnginePage.values().indexOf(selectedPage),
            containerColor = NavyCard,
            contentColor = GoldAccent,
            edgePadding = 4.dp,
            indicator = { tabPositions ->
                val idx = WealthEnginePage.values().indexOf(selectedPage)
                if (idx in tabPositions.indices) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[idx]),
                        color = selectedPage.color,
                        height = 3.dp
                    )
                }
            }
        ) {
            WealthEnginePage.values().forEach { page ->
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
            label = "WealthEnginePageTransition",
            modifier = Modifier.weight(1f)
        ) { targetPage ->
            when (targetPage) {
                WealthEnginePage.INVESTMENTS -> PageWeInvestments()
                WealthEnginePage.LOANS -> PageWeLoans()
                WealthEnginePage.INSURANCE -> PageWeInsurance()
                WealthEnginePage.BILLS -> PageWeBills()
                WealthEnginePage.SAVINGS -> PageWeSavings()
                WealthEnginePage.TAXES -> PageWeTaxes()
                WealthEnginePage.CASHBACK -> PageWeCashback()
            }
        }
    }
}

// -------------------------------------------------------------
// PAGE 1: BETTER INVESTMENTS
// -------------------------------------------------------------
@Composable
private fun PageWeInvestments() {
    var appliedUpgrade by remember { mutableStateOf(false) }
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            EngineHeaderCard(
                title = "AI Investment Yield & Arbitrage Optimizer",
                subtitle = "Detects fee drag, low Sharpe ratios, and auto-routes capital into higher risk-adjusted alpha.",
                icon = Icons.Default.TrendingUp,
                color = CyberCyan
            )
        }
        item {
            EngineOpportunityCard(
                title = "Expense Ratio Drag Reduction",
                impactText = "+$2,840 / yr Added Return",
                impactColor = EmeraldSuccess,
                status = if (appliedUpgrade) "Executed Automatically" else "Ready to Switch",
                isExecuted = appliedUpgrade,
                onExecute = { appliedUpgrade = true },
                rows = listOf(
                    "Current Asset" to "Legacy Mutual Fund (1.18% MER)",
                    "AI Recommendation" to "Direct-Indexed Zero-Fee Core ETF (0.03% MER)",
                    "Projected 10-Yr Compounded Boost" to "+$41,890 Net Gains",
                    "Execution Speed" to "Instant (Tax-Free 1035/Exchange)"
                )
            )
        }
        item {
            EngineOpportunityCard(
                title = "Private Credit & Sovereign Treasury Spread",
                impactText = "+3.85% APY Spread",
                impactColor = CyberCyan,
                status = "Auto-Allocated 15%",
                isExecuted = true,
                onExecute = {},
                rows = listOf(
                    "Asset Class" to "Tokenized Senior Secured Corporate Debt",
                    "Collateral Ratio" to "142% Real Estate & Receivables Backed",
                    "Net Yield" to "9.45% APY (Monthly Auto-Distribution)"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 2: BETTER LOANS
// -------------------------------------------------------------
@Composable
private fun PageWeLoans() {
    var loanRefiDone by remember { mutableStateOf(false) }
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            EngineHeaderCard(
                title = "Autonomous Loan & Debt Refinancing",
                subtitle = "Continuous cross-lender rate matching to lower APR, slash monthly debt service, and eliminate fees.",
                icon = Icons.Default.CreditCard,
                color = CrimsonDanger
            )
        }
        item {
            EngineOpportunityCard(
                title = "Primary Mortgage Rate Compression",
                impactText = "Save $482 / month ($115,680 Total)",
                impactColor = EmeraldSuccess,
                status = if (loanRefiDone) "Refinance Application Locked" else "Pre-Approved 0-Fee Refinance",
                isExecuted = loanRefiDone,
                onExecute = { loanRefiDone = true },
                rows = listOf(
                    "Current Rate" to "6.85% APR (30-Year Fixed)",
                    "AI Negotiated Rate" to "5.15% APR (Zero Closing Costs)",
                    "Lender" to "Apex Sovereign Wholesale Lending",
                    "Lock Period" to "60 Days Floating Cap Guarantee"
                )
            )
        }
        item {
            EngineOpportunityCard(
                title = "Credit Card Debt Consolidation",
                impactText = "Save $1,920 in Interest",
                impactColor = GoldAccent,
                status = "Zero Interest Active",
                isExecuted = true,
                onExecute = {},
                rows = listOf(
                    "Mechanism" to "0% APR 21-Month Balance Transfer Window",
                    "Transferred Balance" to "$14,500 at 0.0% APR (Fee: 0%)",
                    "Payoff Timeline" to "14 Months Ahead of Schedule"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 3: BETTER INSURANCE
// -------------------------------------------------------------
@Composable
private fun PageWeInsurance() {
    var insuranceOptimized by remember { mutableStateOf(false) }
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            EngineHeaderCard(
                title = "Algorithmic Insurance Premium Shopper",
                subtitle = "Scans 48 tier-1 underwriters to eliminate duplicate riders, expand umbrella limits, and slash premiums.",
                icon = Icons.Default.HealthAndSafety,
                color = ElectricBlue
            )
        }
        item {
            EngineOpportunityCard(
                title = "Home & Auto Comprehensive Bundle",
                impactText = "Save $1,140 / yr Premium",
                impactColor = EmeraldSuccess,
                status = if (insuranceOptimized) "Policy Bound & Swapped" else "Instant Policy Swap Available",
                isExecuted = insuranceOptimized,
                onExecute = { insuranceOptimized = true },
                rows = listOf(
                    "Current Underwriter" to "Legacy Mutual Insurance ($3,420/yr)",
                    "AI Optimized Underwriter" to "Aegis Global Risk ($2,280/yr)",
                    "Coverage Change" to "+$1,000,000 Extra Umbrella Liability",
                    "Deductible Match" to "Identical $500 Deductible"
                )
            )
        }
        item {
            EngineOpportunityCard(
                title = "Parametric Cyber & Identity Theft Shield",
                impactText = "Zero Deductible Included",
                impactColor = CyberCyan,
                status = "Active Policy",
                isExecuted = true,
                onExecute = {},
                rows = listOf(
                    "Coverage Pool" to "$1,000,000 Zero-Friction Ransom & Fraud Shield",
                    "Underwriting" to "Integrated with World Digital ID Biometrics",
                    "Annual Cost" to "$0 (Complimentary Tier Member)"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 4: LOWER BILLS
// -------------------------------------------------------------
@Composable
private fun PageWeBills() {
    var billsNegotiated by remember { mutableStateOf(false) }
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            EngineHeaderCard(
                title = "Autonomous Recurring Bill Negotiator",
                subtitle = "AI negotiation bot contacts telecom, internet, SaaS, and utilities to match secret retention promo rates.",
                icon = Icons.Default.ReceiptLong,
                color = AmberOrange
            )
        }
        item {
            EngineOpportunityCard(
                title = "Fiber Gigabit Internet & Mobile Plan",
                impactText = "Saved $75 / mo ($900 / yr)",
                impactColor = EmeraldSuccess,
                status = if (billsNegotiated) "Retention Promo Activated" else "Negotiation Ready (AI Bot)",
                isExecuted = billsNegotiated,
                onExecute = { billsNegotiated = true },
                rows = listOf(
                    "Provider" to "Metro Fiber & Ultra 5G Wireless",
                    "Previous Cost" to "$195.00 / month",
                    "New Renegotiated Cost" to "$120.00 / month (Locked 24 Mo)",
                    "Action Taken" to "AI Bot invoked competitor match clause"
                )
            )
        }
        item {
            EngineOpportunityCard(
                title = "Zombie SaaS & Streaming Cleanup",
                impactText = "Saved $64 / mo ($768 / yr)",
                impactColor = GoldAccent,
                status = "4 Subscriptions Cancelled",
                isExecuted = true,
                onExecute = {},
                rows = listOf(
                    "Detected Unused Services" to "Cloud Storage Pro, 2x Streaming Apps, Fitness Pass",
                    "Avg Inactivity" to "114 Days Without Login",
                    "Annual Savings" to "$768.00 Re-Routed to Investment Yield"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 5: BETTER SAVINGS ACCOUNTS
// -------------------------------------------------------------
@Composable
private fun PageWeSavings() {
    var sweepEnabled by remember { mutableStateOf(true) }
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            EngineHeaderCard(
                title = "Global HYSA Sweep & Yield Maximizer",
                subtitle = "Dynamic multi-bank liquidity sweep routing cash to the highest FDIC/SIPC-insured yield in real time.",
                icon = Icons.Default.Savings,
                color = EmeraldSuccess
            )
        }
        item {
            EngineOpportunityCard(
                title = "High-Yield Cash Sweep Network",
                impactText = "5.45% APY (vs 0.01% Big Banks)",
                impactColor = EmeraldSuccess,
                status = if (sweepEnabled) "Active Dynamic Sweep" else "Sweep Paused",
                isExecuted = sweepEnabled,
                onExecute = { sweepEnabled = !sweepEnabled },
                rows = listOf(
                    "Current National Average" to "0.45% APY",
                    "AI Dynamic Sweep APY" to "5.45% APY (Paid Monthly)",
                    "FDIC Insurance Coverage" to "$5,000,000 via 20 Insured Partner Banks",
                    "Estimated Annual Interest" to "+$4,632.50 on Idle Cash"
                )
            )
        }
        item {
            EngineOpportunityCard(
                title = "Autonomous CD Ladder Arbitrage",
                impactText = "5.70% APY Guaranteed",
                impactColor = CyberCyan,
                status = "Auto-Rolling 3-Mo Tranches",
                isExecuted = true,
                onExecute = {},
                rows = listOf(
                    "Strategy" to "4-Quarter Rolling High-Yield Certificates",
                    "Principal Allocated" to "$50,000 Cash Buffer",
                    "Liquidity Access" to "Quarterly Tranche Maturity Schedule"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 6: LOWER TAXES
// -------------------------------------------------------------
@Composable
private fun PageWeTaxes() {
    var harvestExecuted by remember { mutableStateOf(false) }
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            EngineHeaderCard(
                title = "Autonomous Tax-Loss Harvesting & Shielding",
                subtitle = "Continuous 365-day algorithmic tax optimization across capital gains, depreciation, and bracket arbitrage.",
                icon = Icons.Default.Percent,
                color = PurpleTech
            )
        }
        item {
            EngineOpportunityCard(
                title = "Real-Time Tax-Loss Harvesting Engine",
                impactText = "Harvested $4,850 in Losses",
                impactColor = EmeraldSuccess,
                status = if (harvestExecuted) "Loss Harvested & Replaced" else "Ready to Harvest",
                isExecuted = harvestExecuted,
                onExecute = { harvestExecuted = true },
                rows = listOf(
                    "Loss Identified" to "Short-term equity dip in Global Semiconductor ETF",
                    "Tax Savings" to "$1,794 Offset against Ordinary & Capital Gains",
                    "Wash-Sale Avoidance" to "Instantly swapped into correlated Tech Alpha Index",
                    "Market Exposure Kept" to "100% Continuous Market Beta"
                )
            )
        }
        item {
            EngineOpportunityCard(
                title = "Mega-Backdoor Roth & Solo 401(k) Shield",
                impactText = "Tax Savings: $14,200 / yr",
                impactColor = GoldAccent,
                status = "Fully Optimized",
                isExecuted = true,
                onExecute = {},
                rows = listOf(
                    "Total Sheltered Capital" to "$69,000 Tax-Advantaged Space Utilized",
                    "Effective Tax Bracket Drop" to "From 32% to 24%",
                    "Lifetime Tax Compound Savings" to "+$380,000 at Retirement"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 7: CASHBACK & REWARDS
// -------------------------------------------------------------
@Composable
private fun PageWeCashback() {
    var autoRewardBoost by remember { mutableStateOf(true) }
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            EngineHeaderCard(
                title = "Universal Cashback & Reward Stacker",
                subtitle = "Auto-selects optimal payment card per merchant MCC, stacks wholesale merchant rebates, and boosts points.",
                icon = Icons.Default.CardGiftcard,
                color = GoldAccent
            )
        }
        item {
            EngineOpportunityCard(
                title = "Smart Merchant MCC Card Router",
                impactText = "Average 4.8% Back on All Spend",
                impactColor = EmeraldSuccess,
                status = if (autoRewardBoost) "Smart Card Routing Enabled" else "Routing Disabled",
                isExecuted = autoRewardBoost,
                onExecute = { autoRewardBoost = !autoRewardBoost },
                rows = listOf(
                    "Dining & Entertainment" to "Auto-routed to 5% Unlimited Cash Card",
                    "Travel & Flights" to "Auto-routed to 10x Points Elite Card",
                    "Groceries & Supermarkets" to "Auto-routed to 6% Preferred Card",
                    "Annual Estimated Cashback" to "+$3,480.00 Direct to Brokerage"
                )
            )
        }
        item {
            EngineOpportunityCard(
                title = "Stackable AI Merchant Rebates",
                impactText = "+$412.50 Pending Rebate",
                impactColor = CyberCyan,
                status = "Auto-Claimed on 12 Purchases",
                isExecuted = true,
                onExecute = {},
                rows = listOf(
                    "Recent Captured Rebates" to "Apple Store (4%), Delta Airlines (5%), Nike (8%)",
                    "Frictionless Claim" to "Zero affiliate clicks required; AI auto-matches receipt ledger",
                    "Payout Destination" to "Auto-invested in S&P 500 Index"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// REUSABLE HELPER COMPONENTS
// -------------------------------------------------------------
@Composable
private fun EngineHeaderCard(title: String, subtitle: String, icon: ImageVector, color: Color) {
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
private fun EngineOpportunityCard(
    title: String,
    impactText: String,
    impactColor: Color,
    status: String,
    isExecuted: Boolean,
    onExecute: () -> Unit,
    rows: List<Pair<String, String>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Navy900),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isExecuted) EmeraldSuccess.copy(alpha = 0.4f) else Navy700)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(impactText, color = impactColor, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                }
                Surface(
                    color = if (isExecuted) EmeraldSuccess.copy(alpha = 0.15f) else GoldAccent.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        if (isExecuted) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(11.dp))
                        }
                        Text(
                            text = status,
                            color = if (isExecuted) EmeraldSuccess else GoldAccent,
                            fontWeight = FontWeight.Bold,
                            fontSize = 10.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                rows.forEach { (label, value) ->
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

            if (!isExecuted) {
                Button(
                    onClick = onExecute,
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Navy900, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Auto-Optimize & Execute Now", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}
