package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditScore
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.domain.loan.LoanCalculator
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
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BankViewModel

/**
 * Loan prediction input form that takes user financial data (gross income, current EMIs/debts,
 * credit score, requested principal) and uses a local logic layer ([LoanCalculator]) to estimate
 * loan eligibility, maximum approved borrowing capacity, and monthly EMI based on Debt-to-Income (DTI) ratio.
 */
@Composable
fun LoanPredictionFormCard(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

    var monthlyIncomeInput by remember { mutableStateOf("7500") }
    var existingEmisInput by remember { mutableStateOf("1200") }
    var requestedAmountInput by remember { mutableStateOf("25000") }
    var creditScore by remember { mutableIntStateOf(740) }
    var selectedLoanType by remember { mutableStateOf("Personal Loan") }
    var tenureMonths by remember { mutableIntStateOf(36) }

    val income = monthlyIncomeInput.toDoubleOrNull() ?: 0.0
    val existingEmis = existingEmisInput.toDoubleOrNull() ?: 0.0
    val requestedAmount = requestedAmountInput.toDoubleOrNull() ?: 0.0

    // Local Logic Layer Evaluation based on Debt-to-Income Ratio
    val eligibilityResult = remember(income, existingEmis, creditScore, selectedLoanType) {
        LoanCalculator.evaluateEligibility(
            monthlyIncome = income,
            existingEmis = existingEmis,
            creditScore = creditScore,
            employmentType = "Salaried Full-Time",
            loanType = selectedLoanType
        )
    }

    // Local EMI Calculation for requested loan
    val emiResult = remember(requestedAmount, eligibilityResult.recommendedInterestRate, tenureMonths) {
        LoanCalculator.calculateEmi(
            principal = requestedAmount,
            annualRatePercent = eligibilityResult.recommendedInterestRate,
            tenureMonths = tenureMonths
        )
    }

    val dtiRatio = eligibilityResult.debtToIncomeRatio
    val isDtiSafe = dtiRatio <= 45.0
    val dtiColor = when {
        dtiRatio <= 35.0 -> EmeraldSuccess
        dtiRatio <= 50.0 -> AmberOrange
        else -> CrimsonDanger
    }

    val cardBg = if (isDarkMode) NavyCard else MaterialTheme.colorScheme.surfaceVariant
    val inputBg = if (isDarkMode) Navy900 else MaterialTheme.colorScheme.surface
    val textPrimary = if (isDarkMode) TextWhite else MaterialTheme.colorScheme.onSurface
    val textSecondary = if (isDarkMode) TextMuted else MaterialTheme.colorScheme.onSurfaceVariant
    val borderColor = if (isDarkMode) Navy700 else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("loan_prediction_input_form"),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.dp, borderColor)
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Surface(
                        color = CyberCyan.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Calculate,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier
                                .padding(6.dp)
                                .size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Loan Eligibility Predictor",
                            color = textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Text(
                            text = "Instant evaluation via Local Debt-to-Income Engine",
                            color = textSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                Surface(
                    color = (if (isDarkMode) Navy800 else MaterialTheme.colorScheme.surface),
                    shape = RoundedCornerShape(6.dp),
                    border = BorderStroke(1.dp, borderColor)
                ) {
                    Text(
                        text = "Local Logic",
                        color = CyberCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                    )
                }
            }

            // Loan Product Type Selector
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text("Select Loan Product", color = textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Personal Loan", "Home Loan", "Vehicle Loan", "Education Loan").forEach { type ->
                        val isSelected = selectedLoanType == type
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedLoanType = type },
                            color = if (isSelected) CyberCyan else (if (isDarkMode) Navy800 else MaterialTheme.colorScheme.surface),
                            border = if (!isSelected) BorderStroke(1.dp, borderColor) else null,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = type.replace(" Loan", ""),
                                color = if (isSelected) Navy900 else textPrimary,
                                fontSize = 10.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                modifier = Modifier.padding(vertical = 7.dp)
                            )
                        }
                    }
                }
            }

            // Input Fields Row 1: Monthly Income & Existing Debts (EMIs)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = monthlyIncomeInput,
                    onValueChange = { monthlyIncomeInput = it.filter { ch -> ch.isDigit() || ch == '.' } },
                    label = { Text("Monthly Income ($)", fontSize = 11.sp) },
                    placeholder = { Text("7500") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("loan_input_income"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textPrimary,
                        unfocusedTextColor = textPrimary,
                        focusedContainerColor = inputBg,
                        unfocusedContainerColor = inputBg,
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = borderColor
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                OutlinedTextField(
                    value = existingEmisInput,
                    onValueChange = { existingEmisInput = it.filter { ch -> ch.isDigit() || ch == '.' } },
                    label = { Text("Existing Debts ($)", fontSize = 11.sp) },
                    placeholder = { Text("1200") },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("loan_input_existing_debts"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textPrimary,
                        unfocusedTextColor = textPrimary,
                        focusedContainerColor = inputBg,
                        unfocusedContainerColor = inputBg,
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = borderColor
                    ),
                    shape = RoundedCornerShape(10.dp)
                )
            }

            // Input Fields Row 2: Requested Loan Principal & Tenure
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedTextField(
                    value = requestedAmountInput,
                    onValueChange = { requestedAmountInput = it.filter { ch -> ch.isDigit() || ch == '.' } },
                    label = { Text("Requested Loan ($)", fontSize = 11.sp) },
                    placeholder = { Text("25000") },
                    modifier = Modifier
                        .weight(1.2f)
                        .testTag("loan_input_requested_amount"),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textPrimary,
                        unfocusedTextColor = textPrimary,
                        focusedContainerColor = inputBg,
                        unfocusedContainerColor = inputBg,
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = borderColor
                    ),
                    shape = RoundedCornerShape(10.dp)
                )

                // Tenure Selector
                Surface(
                    modifier = Modifier
                        .weight(0.8f)
                        .height(56.dp),
                    color = inputBg,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, borderColor)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text("Tenure", color = textSecondary, fontSize = 10.sp)
                        Text("$tenureMonths Months", color = textPrimary, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }
            }

            // Credit Score Slider
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Credit Score (FICO)", color = textSecondary, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                    Text("$creditScore", color = if (creditScore >= 720) EmeraldSuccess else AmberOrange, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Slider(
                    value = creditScore.toFloat(),
                    onValueChange = { creditScore = it.toInt() },
                    valueRange = 350f..850f,
                    steps = 50,
                    colors = SliderDefaults.colors(
                        thumbColor = CyberCyan,
                        activeTrackColor = CyberCyan,
                        inactiveTrackColor = if (isDarkMode) Navy700 else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.testTag("loan_input_credit_score_slider")
                )
            }

            // --- REAL-TIME ESTIMATION RESULTS CARD ---
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("loan_prediction_result_box"),
                colors = CardDefaults.cardColors(
                    containerColor = if (isDarkMode) Navy900 else MaterialTheme.colorScheme.surface
                ),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, if (eligibilityResult.isEligible) EmeraldSuccess.copy(alpha = 0.5f) else CrimsonDanger.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Status Badge & Risk Tier
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(
                                imageVector = if (eligibilityResult.isEligible) Icons.Default.CheckCircle else Icons.Default.Warning,
                                contentDescription = null,
                                tint = if (eligibilityResult.isEligible) EmeraldSuccess else CrimsonDanger,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = if (eligibilityResult.isEligible) "ELIGIBLE / PRE-APPROVED" else "REQUIRES ADJUSTMENT",
                                color = if (eligibilityResult.isEligible) EmeraldSuccess else CrimsonDanger,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )
                        }

                        Surface(
                            color = (if (isDarkMode) Navy800 else MaterialTheme.colorScheme.surfaceVariant),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = eligibilityResult.creditRiskTier,
                                color = CyberCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                            )
                        }
                    }

                    // Debt-to-Income (DTI) Ratio Indicator Bar
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "Debt-to-Income (DTI) Ratio",
                                color = textSecondary,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "${"%.1f".format(dtiRatio)}%  (Safe: ≤50%)",
                                color = dtiColor,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                        }
                        LinearProgressIndicator(
                            progress = { (dtiRatio / 100.0).toFloat().coerceIn(0f, 1f) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = dtiColor,
                            trackColor = if (isDarkMode) Navy800 else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f)
                        )
                    }

                    // Key Calculated Figures (Max Capacity, Monthly EMI, Recommended APR)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier.weight(1f),
                            color = if (isDarkMode) Navy800 else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("MAX CAPACITY", color = textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "$${"%,.0f".format(eligibilityResult.maxEligibleAmount)}",
                                    color = textPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            color = if (isDarkMode) Navy800 else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("ESTIMATED EMI", color = textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "$${"%,.0f".format(emiResult.monthlyEmi)}/mo",
                                    color = CyberCyan,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        Surface(
                            modifier = Modifier.weight(1f),
                            color = if (isDarkMode) Navy800 else MaterialTheme.colorScheme.surfaceVariant,
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                Text("ESTIMATED APR", color = textSecondary, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                                Text(
                                    text = "${"%.1f".format(eligibilityResult.recommendedInterestRate)}%",
                                    color = AmberOrange,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    // Local Logic Notes
                    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                        for (note in eligibilityResult.analysisNotes) {
                            Text(
                                text = "• $note",
                                color = textPrimary.copy(alpha = 0.85f),
                                fontSize = 11.sp,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
