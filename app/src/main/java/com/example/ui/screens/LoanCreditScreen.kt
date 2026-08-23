package com.example.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditScore
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.BankViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoanCreditScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("EMI Calculator", "AI Loan Eligibility", "Credit Score")

    val loanPrincipal by viewModel.loanPrincipal.collectAsStateWithLifecycle()
    val loanRate by viewModel.loanRate.collectAsStateWithLifecycle()
    val loanTenureMonths by viewModel.loanTenureMonths.collectAsStateWithLifecycle()
    val emiResult by viewModel.emiResult.collectAsStateWithLifecycle()

    val userMonthlyIncome by viewModel.userMonthlyIncome.collectAsStateWithLifecycle()
    val userExistingEmis by viewModel.userExistingEmis.collectAsStateWithLifecycle()
    val userCreditScore by viewModel.userCreditScore.collectAsStateWithLifecycle()
    val selectedLoanType by viewModel.selectedLoanType.collectAsStateWithLifecycle()
    val eligibilityResult by viewModel.eligibilityResult.collectAsStateWithLifecycle()

    var loanTypeDropdownExpanded by remember { mutableStateOf(false) }
    val loanTypeOptions = listOf("Personal Loan", "Home Loan", "Vehicle Loan", "Education Loan")

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        item {
            Text(
                text = "CREDIT & LOAN INTELLIGENCE",
                color = CyberCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "Loan Prediction & EMI Analytics",
                color = TextWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Tab Row
        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = NavyCard,
                contentColor = CyberCyan,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = CyberCyan,
                        height = 3.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) CyberCyan else TextMuted
                            )
                        },
                        modifier = Modifier.testTag("loan_tab_$index")
                    )
                }
            }
        }

        when (selectedTab) {
            0 -> {
                // --- TAB 1: EMI CALCULATOR ---
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("emi_result_card"),
                        colors = CardDefaults.cardColors(containerColor = NavyCard),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text("MONTHLY INSTALLMENT (EMI)", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "$${"%,.2f".format(emiResult.monthlyEmi)}",
                                color = CyberCyan,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 32.sp
                            )

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(Navy900)
                                    .padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceAround
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Principal", color = TextMuted, fontSize = 11.sp)
                                    Text("$${"%,.0f".format(emiResult.principalAmount)}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Navy700))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Total Interest", color = TextMuted, fontSize = 11.sp)
                                    Text("$${"%,.0f".format(emiResult.totalInterest)}", color = AmberOrange, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Box(modifier = Modifier.width(1.dp).height(24.dp).background(Navy700))
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("Total Payable", color = TextMuted, fontSize = 11.sp)
                                    Text("$${"%,.0f".format(emiResult.totalAmountPayable)}", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }

                // Interactive Sliders
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = NavyCard),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            // Loan Amount Slider
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Loan Amount", color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    Text("$${"%,.0f".format(loanPrincipal)}", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                Slider(
                                    value = loanPrincipal.toFloat(),
                                    onValueChange = { viewModel.setLoanPrincipal(it.toDouble()) },
                                    valueRange = 1000f..100000f,
                                    steps = 99,
                                    colors = SliderDefaults.colors(thumbColor = CyberCyan, activeTrackColor = CyberCyan, inactiveTrackColor = Navy700),
                                    modifier = Modifier.testTag("loan_amount_slider")
                                )
                            }

                            // Interest Rate Slider
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Annual Interest Rate", color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    Text("${"%.1f".format(loanRate)}% APR", color = AmberOrange, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                Slider(
                                    value = loanRate.toFloat(),
                                    onValueChange = { viewModel.setLoanRate(it.toDouble()) },
                                    valueRange = 5f..24f,
                                    steps = 38,
                                    colors = SliderDefaults.colors(thumbColor = AmberOrange, activeTrackColor = AmberOrange, inactiveTrackColor = Navy700),
                                    modifier = Modifier.testTag("loan_rate_slider")
                                )
                            }

                            // Tenure Slider
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("Tenure / Duration", color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                                    Text("$loanTenureMonths Months (${loanTenureMonths / 12} yrs)", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                }
                                Slider(
                                    value = loanTenureMonths.toFloat(),
                                    onValueChange = { viewModel.setLoanTenureMonths(it.toInt()) },
                                    valueRange = 6f..120f,
                                    steps = 19,
                                    colors = SliderDefaults.colors(thumbColor = EmeraldSuccess, activeTrackColor = EmeraldSuccess, inactiveTrackColor = Navy700),
                                    modifier = Modifier.testTag("loan_tenure_slider")
                                )
                            }
                        }
                    }
                }
            }

            1 -> {
                // --- TAB 2: AI LOAN ELIGIBILITY PREDICTION ---
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("loan_eligibility_card"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (eligibilityResult.isEligible) Color(0xFF064E3B).copy(alpha = 0.5f) else Color(0xFF450A0A)
                        ),
                        shape = RoundedCornerShape(18.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (eligibilityResult.isEligible) EmeraldSuccess else CrimsonDanger
                        )
                    ) {
                        Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(
                                        imageVector = if (eligibilityResult.isEligible) Icons.Default.CheckCircle else Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = if (eligibilityResult.isEligible) EmeraldSuccess else CrimsonDanger
                                    )
                                    Text(
                                        text = if (eligibilityResult.isEligible) "AI PRE-APPROVED" else "REQUIRES IMPROVEMENT",
                                        color = if (eligibilityResult.isEligible) EmeraldSuccess else CrimsonDanger,
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp
                                    )
                                }
                                Surface(
                                    color = Navy900,
                                    shape = RoundedCornerShape(6.dp)
                                ) {
                                    Text(
                                        text = eligibilityResult.creditRiskTier,
                                        color = CyberCyan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }

                            if (eligibilityResult.isEligible) {
                                Text(
                                    text = "Max Approved Amount: $${"%,.0f".format(eligibilityResult.maxEligibleAmount)}",
                                    color = TextWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                )
                                Text(
                                    text = "Recommended Prime Rate: ${"%.2f".format(eligibilityResult.recommendedInterestRate)}% APR",
                                    color = CyberCyan,
                                    fontSize = 13.sp
                                )
                            }

                            Text(
                                text = "Debt-to-Income (DTI): ${"%.1f".format(eligibilityResult.debtToIncomeRatio)}% (Safe Limit: 50%)",
                                color = TextMuted,
                                fontSize = 12.sp
                            )

                            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                for (note in eligibilityResult.analysisNotes) {
                                    Text("• $note", color = TextWhite.copy(alpha = 0.9f), fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }

                // Profile Tuning Controls
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = NavyCard),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                            Text("CUSTOMIZE APPLICANT PROFILE", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                            // Loan Type Selection Row
                            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text("Selected Product", color = TextWhite, fontSize = 12.sp)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    loanTypeOptions.forEach { opt ->
                                        val isSel = selectedLoanType == opt
                                        Surface(
                                            color = if (isSel) CyberCyan else Navy800,
                                            shape = RoundedCornerShape(8.dp),
                                            border = if (!isSel) androidx.compose.foundation.BorderStroke(1.dp, Navy700) else null,
                                            modifier = Modifier
                                                .weight(1f)
                                                .clickable { viewModel.setSelectedLoanType(opt) }
                                        ) {
                                            Text(
                                                text = opt.replace(" Loan", ""),
                                                color = if (isSel) Navy900 else TextWhite,
                                                fontSize = 10.sp,
                                                fontWeight = if (isSel) FontWeight.Bold else FontWeight.Normal,
                                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                                modifier = Modifier.padding(vertical = 8.dp)
                                            )
                                        }
                                    }
                                }
                            }

                            // Monthly Income Slider
                            Column {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Monthly Gross Income", color = TextWhite, fontSize = 12.sp)
                                    Text("$${"%,.0f".format(userMonthlyIncome)}/mo", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Slider(
                                    value = userMonthlyIncome.toFloat(),
                                    onValueChange = { viewModel.setUserMonthlyIncome(it.toDouble()) },
                                    valueRange = 1000f..25000f,
                                    steps = 47,
                                    colors = SliderDefaults.colors(thumbColor = EmeraldSuccess, activeTrackColor = EmeraldSuccess, inactiveTrackColor = Navy700),
                                    modifier = Modifier.testTag("income_slider")
                                )
                            }

                            // Existing EMIs Slider
                            Column {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Existing Monthly Debt / EMIs", color = TextWhite, fontSize = 12.sp)
                                    Text("$${"%,.0f".format(userExistingEmis)}/mo", color = AmberOrange, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Slider(
                                    value = userExistingEmis.toFloat(),
                                    onValueChange = { viewModel.setUserExistingEmis(it.toDouble()) },
                                    valueRange = 0f..5000f,
                                    steps = 50,
                                    colors = SliderDefaults.colors(thumbColor = AmberOrange, activeTrackColor = AmberOrange, inactiveTrackColor = Navy700),
                                    modifier = Modifier.testTag("existing_emi_slider")
                                )
                            }

                            // Credit Score Slider
                            Column {
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Applicant Credit Score", color = TextWhite, fontSize = 12.sp)
                                    Text("$userCreditScore", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                                Slider(
                                    value = userCreditScore.toFloat(),
                                    onValueChange = { viewModel.setUserCreditScore(it.toInt()) },
                                    valueRange = 300f..850f,
                                    steps = 55,
                                    colors = SliderDefaults.colors(thumbColor = CyberCyan, activeTrackColor = CyberCyan, inactiveTrackColor = Navy700),
                                    modifier = Modifier.testTag("credit_score_slider")
                                )
                            }
                        }
                    }
                }
            }

            2 -> {
                // --- TAB 3: CREDIT SCORE ANALYZER ---
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth().testTag("credit_score_hub_card"),
                        colors = CardDefaults.cardColors(containerColor = NavyCard),
                        shape = RoundedCornerShape(18.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text("FICO / EXPERIAN CREDIT SCORE", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)

                            Box(modifier = Modifier.size(110.dp), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(
                                    progress = { (userCreditScore - 300) / 550f },
                                    modifier = Modifier.fillMaxSize(),
                                    color = if (userCreditScore >= 750) EmeraldSuccess else if (userCreditScore >= 670) CyberCyan else AmberOrange,
                                    strokeWidth = 10.dp,
                                    trackColor = Navy900
                                )
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text("$userCreditScore", color = TextWhite, fontSize = 26.sp, fontWeight = FontWeight.ExtraBold)
                                    Text(
                                        text = if (userCreditScore >= 750) "EXCELLENT" else if (userCreditScore >= 670) "GOOD" else "FAIR",
                                        color = if (userCreditScore >= 750) EmeraldSuccess else CyberCyan,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            Text("Score Range: 300 (Poor) - 850 (Exceptional)", color = TextMuted, fontSize = 11.sp)
                        }
                    }
                }

                // Key Credit Factors
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = NavyCard),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Text("SCORE INFLUENCING FACTORS", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                            ScoreFactorRow("Payment History", "100% On-Time", "Impact: High", EmeraldSuccess)
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Navy700))
                            ScoreFactorRow("Credit Card Utilization", "18.4% (< 30% Target)", "Impact: High", EmeraldSuccess)
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Navy700))
                            ScoreFactorRow("Credit Age History", "4.8 Years", "Impact: Medium", CyberCyan)
                            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Navy700))
                            ScoreFactorRow("Hard Inquiries (Last 12mo)", "1 Inquiry", "Impact: Low", EmeraldSuccess)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun ScoreFactorRow(title: String, value: String, impact: String, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(title, color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
            Text(impact, color = TextMuted, fontSize = 10.sp)
        }
        Text(value, color = valueColor, fontWeight = FontWeight.Bold, fontSize = 12.sp)
    }
}
