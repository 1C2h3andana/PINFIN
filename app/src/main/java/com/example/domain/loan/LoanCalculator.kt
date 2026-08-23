package com.example.domain.loan

import com.example.data.model.LoanCalculationResult
import com.example.data.model.LoanEligibilityResult
import kotlin.math.pow

object LoanCalculator {

    /**
     * Calculates monthly EMI using the standard banking formula:
     * EMI = [P x R x (1+R)^N] / [(1+R)^N - 1]
     */
    fun calculateEmi(principal: Double, annualRatePercent: Double, tenureMonths: Int): LoanCalculationResult {
        if (principal <= 0 || annualRatePercent <= 0 || tenureMonths <= 0) {
            return LoanCalculationResult(
                monthlyEmi = 0.0,
                totalInterest = 0.0,
                totalAmountPayable = 0.0,
                principalAmount = principal,
                interestRate = annualRatePercent,
                tenureMonths = tenureMonths
            )
        }

        val monthlyRate = (annualRatePercent / 12.0) / 100.0
        val numerator = principal * monthlyRate * (1.0 + monthlyRate).pow(tenureMonths.toDouble())
        val denominator = (1.0 + monthlyRate).pow(tenureMonths.toDouble()) - 1.0

        val emi = if (denominator != 0.0) numerator / denominator else 0.0
        val totalPayable = emi * tenureMonths
        val totalInterest = (totalPayable - principal).coerceAtLeast(0.0)

        return LoanCalculationResult(
            monthlyEmi = emi,
            totalInterest = totalInterest,
            totalAmountPayable = totalPayable,
            principalAmount = principal,
            interestRate = annualRatePercent,
            tenureMonths = tenureMonths
        )
    }

    /**
     * Predicts loan eligibility based on monthly income, existing EMIs, credit score, and requested loan type.
     */
    fun evaluateEligibility(
        monthlyIncome: Double,
        existingEmis: Double,
        creditScore: Int,
        employmentType: String = "Salaried Full-Time",
        loanType: String = "Personal Loan"
    ): LoanEligibilityResult {
        val notes = mutableListOf<String>()

        // 1. Debt-to-Income (DTI) check
        val currentDti = if (monthlyIncome > 0) (existingEmis / monthlyIncome) * 100.0 else 100.0
        val maxAllowableDti = 50.0 // 50% max allowable EMI burden

        val availableMonthlyCapacity = (monthlyIncome * (maxAllowableDti / 100.0) - existingEmis).coerceAtLeast(0.0)

        // 2. Base interest rate depending on credit score
        val baseRate = when {
            creditScore >= 780 -> 8.5
            creditScore >= 720 -> 10.2
            creditScore >= 660 -> 12.8
            creditScore >= 600 -> 15.5
            else -> 18.0
        }

        // Adjust rate for loan type
        val rateAdjustment = when (loanType.lowercase()) {
            "home loan" -> -1.5
            "education loan" -> -0.8
            "vehicle loan" -> 0.0
            else -> +1.2 // personal loan
        }
        val finalRate = (baseRate + rateAdjustment).coerceIn(6.5, 24.0)

        // 3. Max loan capacity (assuming standard 60-month loan for capacity calculation)
        val defaultTenureMonths = if (loanType.lowercase().contains("home")) 240 else 60
        val r = (finalRate / 12.0) / 100.0
        val denom = r * (1.0 + r).pow(defaultTenureMonths.toDouble())
        val factor = if (denom > 0) ((1.0 + r).pow(defaultTenureMonths.toDouble()) - 1.0) / denom else 0.0

        val maxLoanAmount = (availableMonthlyCapacity * factor).coerceAtLeast(0.0)

        // Credit Risk Tier
        val tier = when {
            creditScore >= 750 && currentDti < 35 -> "Prime (Tier 1 - Instant Approval)"
            creditScore >= 670 && currentDti < 45 -> "Near-Prime (Tier 2 - Low Risk)"
            creditScore >= 600 && currentDti < 50 -> "Standard (Tier 3 - Moderate Risk)"
            else -> "Subprime (Tier 4 - High Risk)"
        }

        val isEligible = creditScore >= 600 && currentDti <= 50.0 && monthlyIncome >= 1500.0

        if (creditScore >= 750) {
            notes.add("Excellent Credit Score ($creditScore) qualifies for lowest prime interest rates.")
        } else if (creditScore < 650) {
            notes.add("Credit score ($creditScore) increases interest rate. Boost score by lowering credit card balances.")
        }

        if (currentDti <= 30) {
            notes.add("Healthy Debt-to-Income ratio (${"%.1f".format(currentDti)}%) provides high borrowing headroom.")
        } else if (currentDti > 45) {
            notes.add("High existing debt obligation (${"%.1f".format(currentDti)}% DTI). Consider consolidating existing loans.")
        }

        if (isEligible) {
            notes.add("AI Recommendation: Pre-approved for up to $${"%,.0f".format(maxLoanAmount)} with ${"%.2f".format(finalRate)}% APR.")
        } else {
            notes.add("Application needs improvement: Increase down-payment or clear existing EMIs to meet eligibility criteria.")
        }

        return LoanEligibilityResult(
            isEligible = isEligible,
            maxEligibleAmount = maxLoanAmount,
            recommendedInterestRate = finalRate,
            creditRiskTier = tier,
            debtToIncomeRatio = currentDti,
            analysisNotes = notes
        )
    }
}
