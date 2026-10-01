package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditScore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.domain.ai.FinancialProfilePreset
import com.example.domain.ai.PersonalizedLoanPrediction
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
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.LoanEligibilityUiState
import com.example.ui.viewmodel.LoanEligibilityViewModel
import java.util.Locale

/**
 * Screen that collects user financial inputs and uses the Gemini AI API (gemini-3.5-flash)
 * to deliver an intelligent, personalized loan eligibility prediction and underwriter report.
 */
@Composable
fun LoanEligibilityPredictionScreen(
    modifier: Modifier = Modifier,
    viewModel: LoanEligibilityViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colorScheme.background,
                        Navy900
                    )
                )
            )
            .testTag("loan_eligibility_prediction_screen"),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // --- 1. Top Header & AI Underwriter Badge ---
        item {
            LoanHeaderSection(onReset = { viewModel.resetForm() })
        }

        // --- 2. Quick Presets Bar ---
        item {
            ProfilePresetsSection(
                selectedPresetId = uiState.selectedPresetId,
                onSelectPreset = { viewModel.applyPreset(it) }
            )
        }

        // --- 3. Prediction Result Section (When available) ---
        uiState.prediction?.let { prediction ->
            item {
                PersonalizedPredictionResultCard(
                    prediction = prediction,
                    onDismiss = { viewModel.clearPrediction() }
                )
            }
        }

        // --- 4. Section 1: Income & Employment ---
        item {
            IncomeEmploymentInputCard(
                uiState = uiState,
                onIncomeChange = { viewModel.setMonthlyIncome(it) },
                onEmploymentChange = { viewModel.setEmploymentStatus(it) },
                onLiquidSavingsChange = { viewModel.setLiquidSavings(it) },
                onAdditionalIncomeChange = { viewModel.setAdditionalIncome(it) }
            )
        }

        // --- 5. Section 2: Credit & Debt Obligations ---
        item {
            CreditAndDebtInputCard(
                uiState = uiState,
                onCreditScoreChange = { viewModel.setCreditScore(it) },
                onExistingDebtChange = { viewModel.setExistingMonthlyDebt(it) }
            )
        }

        // --- 6. Section 3: Loan Request Parameters ---
        item {
            LoanRequestParametersCard(
                uiState = uiState,
                onAmountChange = { viewModel.setRequestedAmount(it) },
                onTenureChange = { viewModel.setTenureMonths(it) },
                onPurposeChange = { viewModel.setLoanPurpose(it) },
                onCollateralChange = { viewModel.setCollateralType(it) }
            )
        }

        // --- 7. Section 4: Live Pre-Analysis Gauge & Summary ---
        item {
            LiveImpactSummaryCard(uiState = uiState)
        }

        // --- 8. AI Analysis Trigger CTA Button ---
        item {
            AiUnderwritingActionButton(
                isLoading = uiState.isLoading,
                statusMessage = uiState.statusMessage,
                onEvaluate = { viewModel.evaluateEligibility() }
            )
        }

        // Error message if any
        uiState.errorMessage?.let { error ->
            item {
                Surface(
                    color = CrimsonDanger.copy(alpha = 0.15f),
                    border = BorderStroke(1.dp, CrimsonDanger),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().testTag("prediction_error_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.Warning, contentDescription = null, tint = CrimsonDanger)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(text = error, color = TextWhite, fontSize = 13.sp)
                    }
                }
            }
        }
    }
}

// -----------------------------------------------------------------------------
// SUB-COMPONENTS
// -----------------------------------------------------------------------------

@Composable
private fun LoanHeaderSection(onReset: () -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = GoldAccent,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "GEMINI CREDIT UNDERWRITING",
                    color = GoldAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 1.2.sp
                )
            }

            IconButton(
                onClick = onReset,
                modifier = Modifier.size(32.dp).testTag("btn_reset_loan_form")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Reset Form",
                    tint = CyberCyan,
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        Text(
            text = "Loan Eligibility Prediction",
            color = TextWhite,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Input your financial parameters to receive an instant AI assessment powered by ",
                color = TextMuted,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.weight(1f, fill = false)
            )
            Surface(
                color = CyberCyan.copy(alpha = 0.15f),
                shape = RoundedCornerShape(6.dp),
                border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f))
            ) {
                Text(
                    text = "gemini-3.5-flash",
                    color = CyberCyan,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfilePresetsSection(
    selectedPresetId: String?,
    onSelectPreset: (FinancialProfilePreset) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = "QUICK-FILL TEST PRESETS",
            color = TextMuted,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 1.sp
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FinancialProfilePreset.ALL.forEach { preset ->
                val isSelected = selectedPresetId == preset.id
                FilterChip(
                    selected = isSelected,
                    onClick = { onSelectPreset(preset) },
                    label = {
                        Text(
                            text = preset.label,
                            fontSize = 12.sp,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = ElectricBlue,
                        selectedLabelColor = TextWhite,
                        containerColor = NavyCard,
                        labelColor = TextMuted
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        borderColor = if (isSelected) ElectricBlue else Navy700,
                        selectedBorderColor = ElectricBlue,
                        enabled = true,
                        selected = isSelected
                    ),
                    modifier = Modifier.testTag("preset_chip_${preset.id}")
                )
            }
        }
    }
}

@Composable
private fun IncomeEmploymentInputCard(
    uiState: LoanEligibilityUiState,
    onIncomeChange: (Double) -> Unit,
    onEmploymentChange: (String) -> Unit,
    onLiquidSavingsChange: (Double) -> Unit,
    onAdditionalIncomeChange: (Double) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().testTag("card_income_employment")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.AccountBalance, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "INCOME & EMPLOYMENT PROFILE",
                    color = CyberCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            // Monthly Net Income
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Monthly Net Income", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Text(
                        "$${"%,.0f".format(Locale.US, uiState.monthlyIncome)}/mo",
                        color = EmeraldSuccess,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Slider(
                    value = uiState.monthlyIncome.toFloat(),
                    onValueChange = { onIncomeChange(it.toDouble()) },
                    valueRange = 1000f..30000f,
                    steps = 57,
                    colors = SliderDefaults.colors(
                        thumbColor = EmeraldSuccess,
                        activeTrackColor = EmeraldSuccess,
                        inactiveTrackColor = Navy700
                    ),
                    modifier = Modifier.testTag("slider_monthly_income")
                )
            }

            // Employment Status Chips
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Employment Status", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                val statuses = listOf(
                    "Full-Time Employed",
                    "Self-Employed / Business",
                    "Contract / Freelance",
                    "Retired / Passive"
                )
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    statuses.forEach { status ->
                        val isSelected = uiState.employmentStatus == status
                        FilterChip(
                            selected = isSelected,
                            onClick = { onEmploymentChange(status) },
                            label = {
                                Text(
                                    text = status.substringBefore(" /"),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberCyan.copy(alpha = 0.25f),
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

            // Liquid Savings and Emergency Funds
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Liquid Savings & Deposits", color = TextWhite, fontSize = 13.sp)
                    Text(
                        "$${"%,.0f".format(Locale.US, uiState.liquidSavings)}",
                        color = GoldAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
                Slider(
                    value = uiState.liquidSavings.toFloat(),
                    onValueChange = { onLiquidSavingsChange(it.toDouble()) },
                    valueRange = 0f..100000f,
                    steps = 99,
                    colors = SliderDefaults.colors(
                        thumbColor = GoldAccent,
                        activeTrackColor = GoldAccent,
                        inactiveTrackColor = Navy700
                    ),
                    modifier = Modifier.testTag("slider_liquid_savings")
                )
            }
        }
    }
}

@Composable
private fun CreditAndDebtInputCard(
    uiState: LoanEligibilityUiState,
    onCreditScoreChange: (Int) -> Unit,
    onExistingDebtChange: (Double) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().testTag("card_credit_debt")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CreditScore, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CREDIT STANDING & OBLIGATIONS",
                    color = GoldAccent,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            // Credit Score Slider
            val scoreColor = when {
                uiState.creditScore >= 750 -> EmeraldSuccess
                uiState.creditScore >= 700 -> CyberCyan
                uiState.creditScore >= 650 -> WarningAmber
                else -> CrimsonDanger
            }
            val scoreRating = when {
                uiState.creditScore >= 780 -> "Exceptional"
                uiState.creditScore >= 720 -> "Prime / Very Good"
                uiState.creditScore >= 670 -> "Good / Near-Prime"
                uiState.creditScore >= 620 -> "Fair"
                else -> "Subprime / Poor"
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("FICO Credit Score", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            color = scoreColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = scoreRating,
                                color = scoreColor,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${uiState.creditScore}",
                            color = scoreColor,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp
                        )
                    }
                }

                Slider(
                    value = uiState.creditScore.toFloat(),
                    onValueChange = { onCreditScoreChange(it.toInt()) },
                    valueRange = 350f..850f,
                    steps = 99,
                    colors = SliderDefaults.colors(
                        thumbColor = scoreColor,
                        activeTrackColor = scoreColor,
                        inactiveTrackColor = Navy700
                    ),
                    modifier = Modifier.testTag("slider_credit_score")
                )
            }

            // Existing Monthly Debt
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Existing Monthly Debt (Loans/Cards)", color = TextWhite, fontSize = 13.sp)
                    Text(
                        "$${"%,.0f".format(Locale.US, uiState.existingMonthlyDebt)}/mo",
                        color = WarningAmber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Slider(
                    value = uiState.existingMonthlyDebt.toFloat(),
                    onValueChange = { onExistingDebtChange(it.toDouble()) },
                    valueRange = 0f..5000f,
                    steps = 49,
                    colors = SliderDefaults.colors(
                        thumbColor = WarningAmber,
                        activeTrackColor = WarningAmber,
                        inactiveTrackColor = Navy700
                    ),
                    modifier = Modifier.testTag("slider_existing_debt")
                )
            }
        }
    }
}

@Composable
private fun LoanRequestParametersCard(
    uiState: LoanEligibilityUiState,
    onAmountChange: (Double) -> Unit,
    onTenureChange: (Int) -> Unit,
    onPurposeChange: (String) -> Unit,
    onCollateralChange: (String) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.fillMaxWidth().testTag("card_loan_parameters")
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Tune, contentDescription = null, tint = ElectricBlue, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "LOAN REQUEST SPECIFICATIONS",
                    color = CyberCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            // Loan Purpose Chips
            val purposes = listOf(
                "Home Improvement",
                "Debt Consolidation",
                "Small Business Expansion",
                "Vehicle & Mobility",
                "Education & Career",
                "Personal / Emergency"
            )
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Loan Purpose", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    purposes.forEach { purpose ->
                        val isSelected = uiState.loanPurpose == purpose
                        FilterChip(
                            selected = isSelected,
                            onClick = { onPurposeChange(purpose) },
                            label = {
                                Text(
                                    purpose,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricBlue.copy(alpha = 0.3f),
                                selectedLabelColor = TextWhite,
                                containerColor = Navy700,
                                labelColor = TextMuted
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                borderColor = if (isSelected) ElectricBlue else Color.Transparent,
                                selectedBorderColor = ElectricBlue,
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
                    Text("Requested Loan Amount", color = TextWhite, fontSize = 13.sp)
                    Text(
                        "$${"%,.0f".format(Locale.US, uiState.requestedAmount)}",
                        color = GoldAccent,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp
                    )
                }

                Slider(
                    value = uiState.requestedAmount.toFloat(),
                    onValueChange = { onAmountChange(it.toDouble()) },
                    valueRange = 1000f..150000f,
                    steps = 149,
                    colors = SliderDefaults.colors(
                        thumbColor = GoldAccent,
                        activeTrackColor = GoldAccent,
                        inactiveTrackColor = Navy700
                    ),
                    modifier = Modifier.testTag("slider_loan_amount")
                )
            }

            // Repayment Tenure
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Repayment Duration", color = TextWhite, fontSize = 13.sp)
                    Text(
                        "${uiState.tenureMonths} Months (${"%.1f".format(uiState.tenureMonths / 12.0)} yrs)",
                        color = CyberCyan,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }

                Slider(
                    value = uiState.tenureMonths.toFloat(),
                    onValueChange = { onTenureChange(it.toInt()) },
                    valueRange = 6f..84f,
                    steps = 12,
                    colors = SliderDefaults.colors(
                        thumbColor = CyberCyan,
                        activeTrackColor = CyberCyan,
                        inactiveTrackColor = Navy700
                    ),
                    modifier = Modifier.testTag("slider_loan_tenure")
                )
            }

            // Collateral Option
            val collaterals = listOf("Unsecured", "Vehicle / Auto Title", "Real Estate Equity", "Certificate of Deposit")
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Collateral Security", color = TextWhite, fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    collaterals.forEach { col ->
                        val isSelected = uiState.collateralType == col
                        FilterChip(
                            selected = isSelected,
                            onClick = { onCollateralChange(col) },
                            label = {
                                Text(
                                    col.substringBefore(" /"),
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberCyan.copy(alpha = 0.2f),
                                selectedLabelColor = CyberCyan,
                                containerColor = Navy700,
                                labelColor = TextMuted
                            )
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun LiveImpactSummaryCard(uiState: LoanEligibilityUiState) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Navy800),
        shape = RoundedCornerShape(16.dp),
        border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.25f)),
        modifier = Modifier.fillMaxWidth().testTag("card_live_impact_summary")
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
                    Icon(Icons.Default.Speed, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "REAL-TIME FINANCIAL IMPACT",
                        color = CyberCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
                Text(
                    text = "Pre-Assessment",
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Estimated EMI", color = TextMuted, fontSize = 11.sp)
                    Text(
                        "$${"%,.2f".format(Locale.US, uiState.liveEstimatedEmi)}/mo",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Current DTI", color = TextMuted, fontSize = 11.sp)
                    Text(
                        "${"%.1f".format(Locale.US, uiState.liveCurrentDti)}%",
                        color = if (uiState.liveCurrentDti <= 36.0) EmeraldSuccess else WarningAmber,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("Projected DTI", color = TextMuted, fontSize = 11.sp)
                    val dtiColor = when {
                        uiState.liveProjectedDti <= 36.0 -> EmeraldSuccess
                        uiState.liveProjectedDti <= 43.0 -> WarningAmber
                        else -> CrimsonDanger
                    }
                    Text(
                        "${"%.1f".format(Locale.US, uiState.liveProjectedDti)}%",
                        color = dtiColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }

            // Progress bar for DTI ratio
            val dtiProgress = (uiState.liveProjectedDti / 50.0).coerceIn(0.0, 1.0).toFloat()
            LinearProgressIndicator(
                progress = { dtiProgress },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = when {
                    uiState.liveProjectedDti <= 36.0 -> EmeraldSuccess
                    uiState.liveProjectedDti <= 43.0 -> WarningAmber
                    else -> CrimsonDanger
                },
                trackColor = Navy700
            )

            Text(
                text = if (uiState.liveProjectedDti <= 36.0) {
                    "✓ Projected DTI is well within conventional institutional approval guidelines (under 36%)."
                } else if (uiState.liveProjectedDti <= 43.0) {
                    "⚠ Projected DTI is moderate (36-43%). High credit score or collateral is recommended."
                } else {
                    "⚡ Projected DTI exceeds standard 43% cap. Consider extending tenure or reducing amount."
                },
                color = TextMuted,
                fontSize = 11.sp,
                lineHeight = 15.sp
            )
        }
    }
}

@Composable
private fun AiUnderwritingActionButton(
    isLoading: Boolean,
    statusMessage: String?,
    onEvaluate: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = onEvaluate,
            enabled = !isLoading,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("submit_loan_prediction_button"),
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ElectricBlue
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = TextWhite,
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = "Analyzing with Gemini AI...",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            } else {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = GoldAccent,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Predict Loan Eligibility with Gemini AI",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
        }

        statusMessage?.let { msg ->
            Text(
                text = msg,
                color = CyberCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun PersonalizedPredictionResultCard(
    prediction: PersonalizedLoanPrediction,
    onDismiss: () -> Unit
) {
    val isEligible = prediction.isEligible
    val verdictColor = when (prediction.decisionTier) {
        "PRE_APPROVED" -> EmeraldSuccess
        "CONDITIONAL_APPROVAL" -> CyberCyan
        "REVIEW_REQUIRED" -> WarningAmber
        else -> CrimsonDanger
    }

    Card(
        colors = CardDefaults.cardColors(
            containerColor = NavyCard
        ),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.5.dp, verdictColor),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("card_prediction_result")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header with Verdict Badge & Probability Meter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isEligible) Icons.Default.CheckCircle else Icons.Default.Warning,
                        contentDescription = null,
                        tint = verdictColor,
                        modifier = Modifier.size(26.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = prediction.decisionTier.replace("_", " "),
                            color = verdictColor,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = if (prediction.isAiPowered) "Gemini Underwriting Engine" else "Local Algorithm",
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                Surface(
                    color = verdictColor.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, verdictColor.copy(alpha = 0.4f)),
                    modifier = Modifier.testTag("badge_approval_probability")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${prediction.approvalProbabilityPercent}%",
                            color = verdictColor,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Approval", color = TextWhite, fontSize = 11.sp)
                    }
                }
            }

            // Executive Summary Callout
            Surface(
                color = Navy800,
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text(
                        text = "EXECUTIVE DECISION SUMMARY",
                        color = CyberCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = prediction.executiveSummary,
                        color = TextWhite,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }

            // Key Metrics Grid
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MetricTile(
                    title = "Max Eligible Cap",
                    value = "$${"%,.0f".format(Locale.US, prediction.maxRecommendedLoanAmount)}",
                    accent = GoldAccent,
                    modifier = Modifier.weight(1f)
                )
                MetricTile(
                    title = "Recommended APR",
                    value = "${"%.2f".format(Locale.US, prediction.recommendedApr)}%",
                    accent = CyberCyan,
                    modifier = Modifier.weight(1f)
                )
                MetricTile(
                    title = "Estimated EMI",
                    value = "$${"%,.0f".format(Locale.US, prediction.estimatedMonthlyPayment)}/mo",
                    accent = EmeraldSuccess,
                    modifier = Modifier.weight(1f)
                )
            }

            // Strengths Section
            if (prediction.keyStrengths.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "UNDERWRITING STRENGTHS",
                        color = EmeraldSuccess,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    prediction.keyStrengths.forEach { strength ->
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldSuccess,
                                modifier = Modifier.size(14.dp).padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = strength, color = TextWhite, fontSize = 12.sp, lineHeight = 16.sp)
                        }
                    }
                }
            }

            // Risk Factors Section
            if (prediction.riskFactors.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "RISK FACTORS & NOTATIONS",
                        color = WarningAmber,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    prediction.riskFactors.forEach { risk ->
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                Icons.Default.Warning,
                                contentDescription = null,
                                tint = WarningAmber,
                                modifier = Modifier.size(14.dp).padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = risk, color = TextWhite, fontSize = 12.sp, lineHeight = 16.sp)
                        }
                    }
                }
            }

            // Actionable Recommendations
            if (prediction.actionableRecommendations.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "AI RECOMMENDATIONS TO OPTIMIZE TERMS",
                        color = CyberCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    prediction.actionableRecommendations.forEach { action ->
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                Icons.Default.Lightbulb,
                                contentDescription = null,
                                tint = GoldAccent,
                                modifier = Modifier.size(14.dp).padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = action, color = TextWhite, fontSize = 12.sp, lineHeight = 16.sp)
                        }
                    }
                }
            }

            // Alternative Options
            if (prediction.alternativeOptions.isNotEmpty()) {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "STRUCTURED FINANCING ALTERNATIVES",
                        color = ElectricBlue,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    prediction.alternativeOptions.forEach { alt ->
                        Row(verticalAlignment = Alignment.Top) {
                            Icon(
                                Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = ElectricBlue,
                                modifier = Modifier.size(14.dp).padding(top = 2.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = alt, color = TextWhite, fontSize = 12.sp, lineHeight = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MetricTile(
    title: String,
    value: String,
    accent: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        color = Navy800,
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(text = title, color = TextMuted, fontSize = 10.sp, maxLines = 1)
            Text(
                text = value,
                color = accent,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 13.sp
            )
        }
    }
}
