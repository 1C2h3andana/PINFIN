package com.example.domain.ai

import android.util.Log
import com.example.BuildConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

/**
 * Encapsulates the user's financial inputs collected for loan underwriting.
 */
data class FinancialInputProfile(
    val monthlyIncome: Double = 7500.0,
    val employmentStatus: String = "Full-Time Employed",
    val creditScore: Int = 740,
    val existingMonthlyDebt: Double = 650.0,
    val requestedAmount: Double = 35000.0,
    val tenureMonths: Int = 36,
    val loanPurpose: String = "Home Improvement",
    val liquidSavings: Double = 18000.0,
    val collateralType: String = "Unsecured",
    val additionalHouseholdIncome: Double = 0.0
)

/**
 * Result model returned by Gemini AI or the deterministic credit underwriting engine.
 */
data class PersonalizedLoanPrediction(
    val isEligible: Boolean,
    val decisionTier: String, // "PRE_APPROVED", "CONDITIONAL_APPROVAL", "REVIEW_REQUIRED", "DECLINED"
    val approvalProbabilityPercent: Int,
    val maxRecommendedLoanAmount: Double,
    val recommendedApr: Double,
    val estimatedMonthlyPayment: Double,
    val maxMonthlyEmiCapacity: Double,
    val currentDtiPercent: Double,
    val projectedDtiPercent: Double,
    val keyStrengths: List<String>,
    val riskFactors: List<String>,
    val actionableRecommendations: List<String>,
    val executiveSummary: String,
    val alternativeOptions: List<String>,
    val isAiPowered: Boolean = true,
    val modelName: String = "gemini-3.5-flash",
    val evaluatedAtMillis: Long = System.currentTimeMillis()
)

/**
 * Pre-configured financial presets for user testing and quick demonstration.
 */
data class FinancialProfilePreset(
    val id: String,
    val label: String,
    val description: String,
    val profile: FinancialInputProfile
) {
    companion object {
        val ALL = listOf(
            FinancialProfilePreset(
                id = "prime_tech",
                label = "Prime Professional",
                description = "High income, excellent credit, strong liquid savings",
                profile = FinancialInputProfile(
                    monthlyIncome = 11500.0,
                    employmentStatus = "Full-Time Employed",
                    creditScore = 785,
                    existingMonthlyDebt = 550.0,
                    requestedAmount = 50000.0,
                    tenureMonths = 36,
                    loanPurpose = "Home Improvement",
                    liquidSavings = 42000.0,
                    collateralType = "Unsecured"
                )
            ),
            FinancialProfilePreset(
                id = "small_biz",
                label = "Small Business Owner",
                description = "Variable revenue, moderate debt, business expansion needs",
                profile = FinancialInputProfile(
                    monthlyIncome = 14000.0,
                    employmentStatus = "Self-Employed / Business",
                    creditScore = 720,
                    existingMonthlyDebt = 1800.0,
                    requestedAmount = 75000.0,
                    tenureMonths = 48,
                    loanPurpose = "Small Business Expansion",
                    liquidSavings = 25000.0,
                    collateralType = "Real Estate Equity"
                )
            ),
            FinancialProfilePreset(
                id = "debt_consolidation",
                label = "Debt Consolidator",
                description = "Moderate income, high revolving debt, improving liquidity",
                profile = FinancialInputProfile(
                    monthlyIncome = 6200.0,
                    employmentStatus = "Full-Time Employed",
                    creditScore = 675,
                    existingMonthlyDebt = 1950.0,
                    requestedAmount = 28000.0,
                    tenureMonths = 36,
                    loanPurpose = "Debt Consolidation",
                    liquidSavings = 5500.0,
                    collateralType = "Unsecured"
                )
            ),
            FinancialProfilePreset(
                id = "young_starter",
                label = "First-Time Applicant",
                description = "Entry-level career, clean payment record, modest loan request",
                profile = FinancialInputProfile(
                    monthlyIncome = 4800.0,
                    employmentStatus = "Full-Time Employed",
                    creditScore = 710,
                    existingMonthlyDebt = 350.0,
                    requestedAmount = 15000.0,
                    tenureMonths = 24,
                    loanPurpose = "Vehicle & Mobility",
                    liquidSavings = 9000.0,
                    collateralType = "Vehicle / Auto Title"
                )
            )
        )
    }
}

/**
 * Service that connects user financial input to Google Gemini (gemini-3.5-flash)
 * using REST API calls with 60-second timeouts, and provides an intelligent fallback.
 */
object PersonalizedLoanEligibilityService {

    private const val TAG = "LoanPredictService"
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta"

    suspend fun predictEligibility(input: FinancialInputProfile): PersonalizedLoanPrediction = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        // Calculate basic financial metrics
        val totalMonthlyIncome = input.monthlyIncome + (input.additionalHouseholdIncome / 12.0)
        val currentDti = if (totalMonthlyIncome > 0) {
            (input.existingMonthlyDebt / totalMonthlyIncome) * 100.0
        } else 0.0

        // If no API key or default placeholder, use comprehensive deterministic underwriting engine
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.d(TAG, "Using deterministic underwriting engine (no active Gemini API key)")
            return@withContext computeDeterministicUnderwriting(input, currentDti)
        }

        try {
            val endpoint = "$BASE_URL/models/$MODEL_NAME:generateContent?key=$apiKey"
            val url = URL(endpoint)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            conn.doOutput = true
            // OkHttp / Network 60-second timeout per Gemini API skill guidelines
            conn.connectTimeout = 60000
            conn.readTimeout = 60000

            val promptText = """
                You are a senior credit underwriting algorithm and loan decision AI for SmartBank.
                Analyze the applicant's financial profile:
                - Gross Monthly Income: $${input.monthlyIncome}
                - Employment Status: ${input.employmentStatus}
                - Credit Score: ${input.creditScore} / 850
                - Existing Monthly Debt Payments: $${input.existingMonthlyDebt}
                - Requested Loan Amount: $${input.requestedAmount}
                - Requested Tenure: ${input.tenureMonths} months
                - Stated Loan Purpose: ${input.loanPurpose}
                - Liquid Savings / Emergency Reserve: $${input.liquidSavings}
                - Collateral Offered: ${input.collateralType}
                - Current Debt-To-Income (DTI): ${"%.1f".format(Locale.US, currentDti)}%

                Evaluate eligibility rigorously based on banking standards (max 43% back-end DTI, credit tiers, liquidity cushion).
                Respond ONLY with a valid raw JSON object, without markdown ticks or backticks:
                {
                  "isEligible": true,
                  "decisionTier": "PRE_APPROVED",
                  "approvalProbabilityPercent": 88,
                  "maxRecommendedLoanAmount": 55000.0,
                  "recommendedApr": 6.25,
                  "estimatedMonthlyPayment": 1068.50,
                  "maxMonthlyEmiCapacity": 2200.0,
                  "projectedDtiPercent": 28.5,
                  "keyStrengths": ["DTI well below the 36% threshold", "Ample liquid reserves covering >6 months of payments"],
                  "riskFactors": ["Tenure of 36 months leaves modest monthly cushion"],
                  "actionableRecommendations": ["Enroll in automatic debit for a 0.25% APR rate reduction", "Keep credit card balances below 20% limit"],
                  "executiveSummary": "Applicant demonstrates strong liquidity and debt management capability, easily qualifying for prime rates.",
                  "alternativeOptions": ["Opt for a 48-month tenure to reduce monthly payment to $840", "Pledge certificate of deposit to lower APR by 1.0%"]
                }
            """.trimIndent()

            val rootJson = JSONObject().apply {
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("role", "user")
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply { put("text", promptText) })
                        })
                    })
                })
            }

            OutputStreamWriter(conn.outputStream, "UTF-8").use { writer ->
                writer.write(rootJson.toString())
                writer.flush()
            }

            val responseCode = conn.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
                val responseBody = conn.inputStream.bufferedReader().use { it.readText() }
                val respJson = JSONObject(responseBody)
                val candidates = respJson.optJSONArray("candidates")
                if (candidates != null && candidates.length() > 0) {
                    val contentObj = candidates.getJSONObject(0).getJSONObject("content")
                    val parts = contentObj.getJSONArray("parts")
                    val rawAiText = parts.getJSONObject(0).getString("text")

                    // Strip potential markdown code fence
                    val cleanJson = rawAiText.replace("```json", "").replace("```", "").trim()
                    val parsed = JSONObject(cleanJson)

                    val strengths = mutableListOf<String>()
                    val sArr = parsed.optJSONArray("keyStrengths")
                    if (sArr != null) {
                        for (i in 0 until sArr.length()) strengths.add(sArr.getString(i))
                    }

                    val risks = mutableListOf<String>()
                    val rArr = parsed.optJSONArray("riskFactors")
                    if (rArr != null) {
                        for (i in 0 until rArr.length()) risks.add(rArr.getString(i))
                    }

                    val actions = mutableListOf<String>()
                    val aArr = parsed.optJSONArray("actionableRecommendations")
                    if (aArr != null) {
                        for (i in 0 until aArr.length()) actions.add(aArr.getString(i))
                    }

                    val alternatives = mutableListOf<String>()
                    val altArr = parsed.optJSONArray("alternativeOptions")
                    if (altArr != null) {
                        for (i in 0 until altArr.length()) alternatives.add(altArr.getString(i))
                    }

                    return@withContext PersonalizedLoanPrediction(
                        isEligible = parsed.optBoolean("isEligible", true),
                        decisionTier = parsed.optString("decisionTier", "PRE_APPROVED"),
                        approvalProbabilityPercent = parsed.optInt("approvalProbabilityPercent", 85),
                        maxRecommendedLoanAmount = parsed.optDouble("maxRecommendedLoanAmount", input.requestedAmount * 1.15),
                        recommendedApr = parsed.optDouble("recommendedApr", 6.75),
                        estimatedMonthlyPayment = parsed.optDouble("estimatedMonthlyPayment", input.requestedAmount / input.tenureMonths),
                        maxMonthlyEmiCapacity = parsed.optDouble("maxMonthlyEmiCapacity", totalMonthlyIncome * 0.43),
                        currentDtiPercent = currentDti,
                        projectedDtiPercent = parsed.optDouble("projectedDtiPercent", currentDti + 12.0),
                        keyStrengths = if (strengths.isEmpty()) listOf("Solid credit rating", "Verified income stability") else strengths,
                        riskFactors = if (risks.isEmpty()) listOf("Macro rate variability") else risks,
                        actionableRecommendations = if (actions.isEmpty()) listOf("Maintain debt payments on time") else actions,
                        executiveSummary = parsed.optString("executiveSummary", "Pre-qualification calculated via Gemini credit underwriting."),
                        alternativeOptions = if (alternatives.isEmpty()) listOf("Consider shorter tenure for reduced interest expense") else alternatives,
                        isAiPowered = true,
                        modelName = MODEL_NAME
                    )
                }
            } else {
                Log.w(TAG, "Gemini API returned HTTP $responseCode, falling back to deterministic underwriting.")
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gemini API call failed, falling back to deterministic underwriting", e)
        }

        return@withContext computeDeterministicUnderwriting(input, currentDti)
    }

    /**
     * High-precision local credit underwriting engine used when offline or without Gemini API key.
     */
    fun computeDeterministicUnderwriting(
        input: FinancialInputProfile,
        currentDti: Double
    ): PersonalizedLoanPrediction {
        val totalMonthlyIncome = (input.monthlyIncome + (input.additionalHouseholdIncome / 12.0)).coerceAtLeast(1000.0)
        val maxAllowableEmiCapacity = (totalMonthlyIncome * 0.43) - input.existingMonthlyDebt

        // Base APR from Credit Score
        var baseApr = when {
            input.creditScore >= 780 -> 5.25
            input.creditScore >= 720 -> 6.85
            input.creditScore >= 670 -> 9.75
            input.creditScore >= 620 -> 14.50
            else -> 19.99
        }

        // Employment and Collateral Adjustments
        if (input.collateralType != "Unsecured") {
            baseApr = (baseApr - 0.75).coerceAtLeast(4.25)
        }
        if (input.employmentStatus == "Self-Employed / Business") {
            baseApr += 0.40
        }

        // Standard Amortization Formula: P * [ r(1+r)^n ] / [ (1+r)^n - 1 ]
        val monthlyRate = (baseApr / 100.0) / 12.0
        val n = input.tenureMonths.coerceAtLeast(1)
        val monthlyPayment = if (monthlyRate > 0.0) {
            val factor = Math.pow(1.0 + monthlyRate, n.toDouble())
            if (factor > 1.0) (input.requestedAmount * monthlyRate * factor) / (factor - 1.0)
            else input.requestedAmount / n
        } else {
            input.requestedAmount / n
        }

        val projectedTotalDebtMonthly = input.existingMonthlyDebt + monthlyPayment
        val projectedDti = (projectedTotalDebtMonthly / totalMonthlyIncome) * 100.0

        val isEligible = input.creditScore >= 600 && projectedDti <= 45.0 && monthlyPayment <= maxAllowableEmiCapacity

        val decisionTier = when {
            input.creditScore >= 750 && projectedDti <= 35.0 -> "PRE_APPROVED"
            input.creditScore >= 680 && projectedDti <= 42.0 -> "CONDITIONAL_APPROVAL"
            input.creditScore >= 600 && projectedDti <= 50.0 -> "REVIEW_REQUIRED"
            else -> "DECLINED"
        }

        val approvalProb = when (decisionTier) {
            "PRE_APPROVED" -> 92
            "CONDITIONAL_APPROVAL" -> 78
            "REVIEW_REQUIRED" -> 54
            else -> 22
        }

        val maxBorrowingCap = (maxAllowableEmiCapacity.coerceAtLeast(100.0) * n * 0.88).coerceAtLeast(5000.0)

        val strengths = mutableListOf<String>()
        val risks = mutableListOf<String>()
        val actions = mutableListOf<String>()
        val alternatives = mutableListOf<String>()

        if (input.creditScore >= 720) strengths.add("Credit score of ${input.creditScore} qualifies for premier low-APR risk tier")
        if (currentDti <= 28.0) strengths.add("Current debt-to-income (${"%.1f".format(Locale.US, currentDti)}%) demonstrates prudent leverage")
        if (input.liquidSavings >= monthlyPayment * 6) strengths.add("Emergency reserves ($${"%,.0f".format(Locale.US, input.liquidSavings)}) provide a 6+ month payment safety buffer")
        if (input.collateralType != "Unsecured") strengths.add("Secured collateral (${input.collateralType}) lowers institutional lender risk")

        if (projectedDti > 40.0) risks.add("Projected DTI (${"%.1f".format(Locale.US, projectedDti)}%) approaches the 43% traditional underwriting ceiling")
        if (input.creditScore < 680) risks.add("Credit score below 680 subjects borrowing to elevated risk premiums")
        if (input.tenureMonths >= 60) risks.add("Extended tenure (${input.tenureMonths} mos) accrues higher cumulative interest over time")
        if (input.existingMonthlyDebt > totalMonthlyIncome * 0.35) risks.add("High existing debt payments ($${"%,.0f".format(Locale.US, input.existingMonthlyDebt)}/mo) constrain liquidity")

        actions.add("Enroll in automatic bank payments to receive a standard 0.25% APR rate discount")
        actions.add("Paying down existing credit balances by $1,000 before applying can boost credit score by 10-15 points")
        if (projectedDti > 36.0) actions.add("Consider adding a co-signer or adding secondary income to lower overall debt ratio")

        if (input.tenureMonths < 60) {
            val altMonths = input.tenureMonths + 12
            val altRate = (baseApr / 100.0) / 12.0
            val altFactor = Math.pow(1.0 + altRate, altMonths.toDouble())
            val altMonthly = if (altFactor > 1.0) (input.requestedAmount * altRate * altFactor) / (altFactor - 1.0) else input.requestedAmount / altMonths
            alternatives.add("Extend tenure to $altMonths months to lower monthly installment to $${"%,.2f".format(Locale.US, altMonthly)}")
        }
        if (input.collateralType == "Unsecured") {
            alternatives.add("Pledging a vehicle or CD account can reduce your interest rate by 0.75% to 1.25%")
        }
        alternatives.add("Borrowing $${"%,.0f".format(Locale.US, input.requestedAmount * 0.8)} reduces total monthly interest burden by 20%")

        val summary = if (isEligible) {
            "Applicant is $decisionTier for $${"%,.0f".format(Locale.US, input.requestedAmount)} (${input.loanPurpose}) with a $approvalProb% approval confidence. Estimated monthly installment is $${"%,.2f".format(Locale.US, monthlyPayment)} at ${"%.2f".format(Locale.US, baseApr)}% APR."
        } else {
            "Applicant's projected debt-to-income ratio (${"%.1f".format(Locale.US, projectedDti)}%) or credit score (${input.creditScore}) exceeds standard risk parameters. A lower loan amount or collateral is advised."
        }

        return PersonalizedLoanPrediction(
            isEligible = isEligible,
            decisionTier = decisionTier,
            approvalProbabilityPercent = approvalProb,
            maxRecommendedLoanAmount = maxBorrowingCap,
            recommendedApr = baseApr,
            estimatedMonthlyPayment = monthlyPayment,
            maxMonthlyEmiCapacity = maxAllowableEmiCapacity.coerceAtLeast(0.0),
            currentDtiPercent = currentDti,
            projectedDtiPercent = projectedDti,
            keyStrengths = if (strengths.isEmpty()) listOf("Steady documented income") else strengths,
            riskFactors = if (risks.isEmpty()) listOf("General macroeconomic rate fluctuations") else risks,
            actionableRecommendations = actions,
            executiveSummary = summary,
            alternativeOptions = alternatives,
            isAiPowered = false,
            modelName = "Deterministic Underwriting Engine"
        )
    }
}
