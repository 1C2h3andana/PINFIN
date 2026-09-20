package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditScore
import androidx.compose.material.icons.filled.Insights
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
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
import com.example.data.model.AccountEntity
import com.example.data.model.BudgetGoal
import com.example.data.model.Transaction
import com.example.domain.ai.GeminiLoanPrediction
import com.example.ui.theme.CrimsonDanger
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.NavyCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.BankViewModel
import java.util.Locale

/**
 * Screen that uses the Gemini API (gemini-3.5-flash) to evaluate and predict loan eligibility
 * based on user financial accounts, transaction history, and savings goals stored in the local Room database.
 */
@Composable
fun GeminiLoanEligibilityScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val transactions by viewModel.transactionRecords.collectAsStateWithLifecycle()
    val budgetGoals by viewModel.budgetGoalsList.collectAsStateWithLifecycle()
    val creditScore by viewModel.userCreditScore.collectAsStateWithLifecycle()
    val monthlyIncome by viewModel.userMonthlyIncome.collectAsStateWithLifecycle()

    val geminiPrediction by viewModel.geminiLoanPrediction.collectAsStateWithLifecycle()
    val isEvaluating by viewModel.isEvaluatingLoanWithGemini.collectAsStateWithLifecycle()

    var requestedAmount by remember { mutableDoubleStateOf(35000.0) }
    var loanTenureMonths by remember { mutableIntStateOf(36) }
    var selectedPurpose by remember { mutableStateOf("Personal / Debt Consolidation") }

    val loanPurposes = listOf(
        "Personal / Debt Consolidation",
        "Home Improvement & Green Energy",
        "Small Business Capital",
        "Education & Certification",
        "Vehicle & Mobility"
    )

    // Automatically trigger initial prediction if null
    LaunchedEffect(Unit) {
        if (geminiPrediction == null) {
            viewModel.evaluateLoanEligibilityWithGemini(
                requestedAmount = requestedAmount,
                loanTenureMonths = loanTenureMonths,
                loanPurpose = selectedPurpose
            )
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        Navy800
                    )
                )
            )
            .testTag("gemini_loan_eligibility_screen"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Header
        item {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = GoldAccent,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "GEMINI CREDIT INTELLIGENCE",
                        color = GoldAccent,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    text = "Gemini AI Loan Underwriter",
                    color = TextWhite,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Autonomous credit modeling powered by Google Gemini and local Room data",
                    color = TextMuted,
                    fontSize = 12.sp,
                    lineHeight = 16.sp
                )
            }
        }

        // Database Financial Footprint Summary
        item {
            DatabaseFinancialSnapshotCard(
                accounts = accounts,
                transactions = transactions,
                budgetGoals = budgetGoals,
                creditScore = creditScore,
                monthlyIncome = monthlyIncome
            )
        }

        // Loan Application Parameters Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("loan_parameters_card"),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "LOAN APPLICATION PARAMETERS",
                            color = CyberCyan,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Icon(
                            Icons.Default.Calculate,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    // Purpose selector
                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text("Loan Purpose", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            loanPurposes.take(3).forEach { purpose ->
                                val isSelected = selectedPurpose == purpose
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedPurpose = purpose },
                                    label = {
                                        Text(
                                            purpose.substringBefore(" /").substringBefore(" &"),
                                            fontSize = 11.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
                                        selectedLabelColor = CyberCyan,
                                        containerColor = Navy700,
                                        labelColor = TextMuted
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        borderColor = if (isSelected) CyberCyan else Color.Transparent,
                                        selectedBorderColor = CyberCyan,
                                        enabled = true,
                                        selected = isSelected
                                    )
                                )
                            }
                        }
                    }

                    // Requested Amount Slider
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Requested Amount", color = TextWhite, fontSize = 13.sp)
                            Text(
                                "$${"%,.0f".format(Locale.US, requestedAmount)}",
                                color = GoldAccent,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Slider(
                            value = requestedAmount.toFloat(),
                            onValueChange = { requestedAmount = it.toDouble() },
                            valueRange = 1000f..150000f,
                            steps = 149,
                            colors = SliderDefaults.colors(
                                thumbColor = GoldAccent,
                                activeTrackColor = GoldAccent,
                                inactiveTrackColor = Navy700
                            ),
                            modifier = Modifier.testTag("loan_amount_slider")
                        )
                    }

                    // Requested Tenure Slider
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Repayment Tenure", color = TextWhite, fontSize = 13.sp)
                            Text(
                                "$loanTenureMonths Months (${loanTenureMonths / 12} yrs)",
                                color = CyberCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                        Slider(
                            value = loanTenureMonths.toFloat(),
                            onValueChange = { loanTenureMonths = it.toInt() },
                            valueRange = 12f..84f,
                            steps = 5,
                            colors = SliderDefaults.colors(
                                thumbColor = CyberCyan,
                                activeTrackColor = CyberCyan,
                                inactiveTrackColor = Navy700
                            ),
                            modifier = Modifier.testTag("loan_tenure_slider")
                        )
                    }

                    // Re-Evaluate with Gemini Button
                    Button(
                        onClick = {
                            viewModel.evaluateLoanEligibilityWithGemini(
                                requestedAmount = requestedAmount,
                                loanTenureMonths = loanTenureMonths,
                                loanPurpose = selectedPurpose
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("predict_with_gemini_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = ElectricBlue
                        ),
                        shape = RoundedCornerShape(12.dp),
                        enabled = !isEvaluating
                    ) {
                        if (isEvaluating) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(20.dp),
                                color = TextWhite,
                                strokeWidth = 2.dp
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text("Gemini is analyzing Room records...", color = TextWhite, fontWeight = FontWeight.Bold)
                        } else {
                            Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = GoldAccent)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                "Predict Loan Eligibility with Gemini AI",
                                color = TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        // Prediction Result Card
        item {
            AnimatedVisibility(visible = geminiPrediction != null) {
                geminiPrediction?.let { prediction ->
                    GeminiPredictionDisplayCard(prediction = prediction, requestedAmount = requestedAmount)
                }
            }
        }
    }
}

@Composable
private fun DatabaseFinancialSnapshotCard(
    accounts: List<AccountEntity>,
    transactions: List<Transaction>,
    budgetGoals: List<BudgetGoal>,
    creditScore: Int,
    monthlyIncome: Double
) {
    val totalBalance = accounts.sumOf { it.balance }
    val totalIncomeTxs = transactions.filter { it.type.equals("income", ignoreCase = true) }.sumOf { it.amount }
    val totalExpenseTxs = transactions.filter { it.type.equals("expense", ignoreCase = true) }.sumOf { it.amount }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("db_financial_snapshot_card"),
        colors = CardDefaults.cardColors(containerColor = Navy800),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Security,
                        contentDescription = null,
                        tint = EmeraldSuccess,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        "LOCAL ROOM DATABASE TELEMETRY",
                        color = EmeraldSuccess,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.5.sp
                    )
                }
                Text(
                    "${accounts.size} Accounts • ${transactions.size} Records",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                FinancialMetricItem(
                    label = "Total Liquidity",
                    value = "$${"%,.0f".format(Locale.US, totalBalance)}",
                    color = EmeraldSuccess
                )
                FinancialMetricItem(
                    label = "Monthly Stated",
                    value = "$${"%,.0f".format(Locale.US, monthlyIncome)}",
                    color = TextWhite
                )
                FinancialMetricItem(
                    label = "Credit Score",
                    value = "$creditScore",
                    color = GoldAccent
                )
                FinancialMetricItem(
                    label = "Goal Progress",
                    value = "${budgetGoals.size} Goals",
                    color = CyberCyan
                )
            }
        }
    }
}

@Composable
private fun FinancialMetricItem(
    label: String,
    value: String,
    color: Color
) {
    Column {
        Text(label, color = TextMuted, fontSize = 10.sp)
        Text(value, color = color, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun GeminiPredictionDisplayCard(
    prediction: GeminiLoanPrediction,
    requestedAmount: Double
) {
    val statusColor = if (prediction.isEligible) EmeraldSuccess else CrimsonDanger
    val statusText = if (prediction.isEligible) "PRE-APPROVED ELIGIBLE" else "MANUAL REVIEW / UNFAVORABLE"

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("gemini_prediction_result_card"),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(statusColor)
                    )
                    Text(
                        text = statusText,
                        color = statusColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        letterSpacing = 0.5.sp
                    )
                }

                Surface(
                    color = CyberCyan.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "${prediction.creditRiskTier} RISK TIER",
                        color = CyberCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Probability gauge
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        "Gemini Underwriting Confidence",
                        color = TextWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "${prediction.approvalProbabilityPercent}% Approval Probability",
                        color = if (prediction.approvalProbabilityPercent >= 75) EmeraldSuccess else WarningAmber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                }
                LinearProgressIndicator(
                    progress = { (prediction.approvalProbabilityPercent / 100f).coerceIn(0f, 1f) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .clip(RoundedCornerShape(4.dp)),
                    color = if (prediction.approvalProbabilityPercent >= 75) EmeraldSuccess else WarningAmber,
                    trackColor = Navy700
                )
            }

            // Key Financial Metrics Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                MetricColumn(
                    title = "MAX BORROWING CAP",
                    value = "$${"%,.0f".format(Locale.US, prediction.maxRecommendedLoanAmount)}",
                    valueColor = GoldAccent
                )
                MetricColumn(
                    title = "OFFERED APR",
                    value = "${"%.2f".format(Locale.US, prediction.recommendedApr)}%",
                    valueColor = CyberCyan
                )
                MetricColumn(
                    title = "MAX EMI CAPACITY",
                    value = "$${"%,.0f".format(Locale.US, prediction.maxMonthlyEmiCapacity)}/mo",
                    valueColor = TextWhite
                )
            }

            // Executive Summary
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            Icons.Default.Insights,
                            contentDescription = null,
                            tint = GoldAccent,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            "AI Executive Assessment",
                            color = GoldAccent,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = prediction.executiveSummary,
                        color = TextWhite,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            // Key Strengths
            if (prediction.keyStrengths.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "Underwriting Strengths Identified:",
                        color = EmeraldSuccess,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    prediction.keyStrengths.forEach { str ->
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldSuccess,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(str, color = TextWhite.copy(alpha = 0.9f), fontSize = 11.sp)
                        }
                    }
                }
            }

            // Risk Factors & Considerations
            if (prediction.riskFactors.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "Risk Factors & Guardrails:",
                        color = WarningAmber,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    prediction.riskFactors.forEach { risk ->
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = WarningAmber,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(risk, color = TextWhite.copy(alpha = 0.9f), fontSize = 11.sp)
                        }
                    }
                }
            }

            // Actionable Recommendations
            if (prediction.actionableRecommendations.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        "AI Recommendations to Optimize Terms:",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    prediction.actionableRecommendations.forEach { action ->
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(
                                Icons.Default.Bolt,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(14.dp)
                            )
                            Text(action, color = TextWhite.copy(alpha = 0.9f), fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricColumn(
    title: String,
    value: String,
    valueColor: Color
) {
    Column {
        Text(
            text = title,
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.5.sp
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            color = valueColor,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
