package com.example.domain.ai

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.AccountEntity
import com.example.data.model.BudgetGoal
import com.example.data.model.Transaction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.OutputStreamWriter
import java.net.HttpURLConnection
import java.net.URL
import java.util.Locale

/**
 * Result model for Gemini-powered loan eligibility prediction.
 */
data class GeminiLoanPrediction(
    val isEligible: Boolean,
    val approvalProbabilityPercent: Int,
    val maxRecommendedLoanAmount: Double,
    val recommendedApr: Double,
    val creditRiskTier: String, // "PRIME", "NEAR-PRIME", "SUBPRIME", "EXCEPTIONAL"
    val maxMonthlyEmiCapacity: Double,
    val keyStrengths: List<String>,
    val riskFactors: List<String>,
    val actionableRecommendations: List<String>,
    val executiveSummary: String,
    val isAiPowered: Boolean = true
)

/**
 * Service that connects the user's Room database financial profiles (Accounts, Transactions, Budget Goals)
 * to Google Gemini API (gemini-3.5-flash) to evaluate loan eligibility and terms.
 */
object GeminiLoanAdvisorService {

    private const val TAG = "GeminiLoanAdvisor"
    private const val MODEL_NAME = "gemini-3.5-flash"
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta"

    suspend fun predictLoanEligibility(
        requestedAmount: Double,
        loanTenureMonths: Int,
        loanPurpose: String,
        creditScore: Int,
        statedMonthlyIncome: Double,
        accounts: List<AccountEntity>,
        transactions: List<Transaction>,
        budgetGoals: List<BudgetGoal>
    ): GeminiLoanPrediction = withContext(Dispatchers.IO) {
        val apiKey = BuildConfig.GEMINI_API_KEY

        // Compute aggregate financial metrics from Room database
        val totalLiquidBalance = accounts.sumOf { it.balance }
        val recentInflows = transactions.filter { it.type.equals("income", ignoreCase = true) }.sumOf { it.amount }
        val recentOutflows = transactions.filter { it.type.equals("expense", ignoreCase = true) }.sumOf { it.amount }
        val netCashFlow = recentInflows - recentOutflows
        val completedGoalsCount = budgetGoals.count { it.isCompleted }
        val activeGoalsProgress = if (budgetGoals.isNotEmpty()) {
            budgetGoals.map { it.progressPercentage }.average().toInt()
        } else 50

        // If no API key or placeholder, provide comprehensive algorithmic fallback with instant analysis
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            return@withContext computeDeterministicPrediction(
                requestedAmount = requestedAmount,
                loanTenureMonths = loanTenureMonths,
                loanPurpose = loanPurpose,
                creditScore = creditScore,
                statedMonthlyIncome = statedMonthlyIncome,
                totalLiquidBalance = totalLiquidBalance,
                recentInflows = recentInflows,
                recentOutflows = recentOutflows,
                budgetGoalDiscipline = activeGoalsProgress
            )
        }

        try {
            val endpoint = "$BASE_URL/models/$MODEL_NAME:generateContent?key=$apiKey"
            val url = URL(endpoint)
            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "POST"
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8")
            conn.doOutput = true
            conn.connectTimeout = 20000
            conn.readTimeout = 20000

            val promptText = """
                You are a senior credit underwriting algorithm and loan evaluation AI for SmartBank.
                Analyze the following financial data stored in the local banking database:
                
                - Requested Loan Amount: $$requestedAmount
                - Requested Tenure: $loanTenureMonths months
                - Loan Purpose: $loanPurpose
                - Credit Score: $creditScore / 850
                - Stated Monthly Income: $$statedMonthlyIncome
                - Total Liquid Account Balances: $$totalLiquidBalance across ${accounts.size} accounts
                - Total Recorded Income Inflows: $$recentInflows
                - Total Recorded Expenses Outflows: $$recentOutflows
                - Net Tracked Cash Flow: $$netCashFlow
                - Active Budget Goals Count: ${budgetGoals.size} (Avg completion: $activeGoalsProgress%)
                
                Predict loan eligibility and respond ONLY with a raw JSON object with NO markdown formatting:
                {
                  "isEligible": true,
                  "approvalProbabilityPercent": 88,
                  "maxRecommendedLoanAmount": 75000.0,
                  "recommendedApr": 5.75,
                  "creditRiskTier": "PRIME",
                  "maxMonthlyEmiCapacity": 1800.0,
                  "keyStrengths": ["Low DTI ratio under 28%", "Consistent liquid cash reserve covering >6 months"],
                  "riskFactors": ["Tenure of 60 months increases total interest charge"],
                  "actionableRecommendations": ["Set up automatic deduction to qualify for an extra 0.25% APR rate discount"],
                  "executiveSummary": "Applicant displays prime credit health with strong liquidity reserves."
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

                    // Clean code fence if Gemini returned markdown
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

                    return@withContext GeminiLoanPrediction(
                        isEligible = parsed.optBoolean("isEligible", true),
                        approvalProbabilityPercent = parsed.optInt("approvalProbabilityPercent", 85),
                        maxRecommendedLoanAmount = parsed.optDouble("maxRecommendedLoanAmount", requestedAmount * 1.2),
                        recommendedApr = parsed.optDouble("recommendedApr", 6.25),
                        creditRiskTier = parsed.optString("creditRiskTier", "PRIME"),
                        maxMonthlyEmiCapacity = parsed.optDouble("maxMonthlyEmiCapacity", statedMonthlyIncome * 0.4),
                        keyStrengths = if (strengths.isEmpty()) listOf("Healthy liquid balance", "Active budget discipline") else strengths,
                        riskFactors = if (risks.isEmpty()) listOf("Macro interest rate fluctuations") else risks,
                        actionableRecommendations = if (actions.isEmpty()) listOf("Keep credit utilization below 30%") else actions,
                        executiveSummary = parsed.optString("executiveSummary", "Application pre-approved based on Gemini financial modeling."),
                        isAiPowered = true
                    )
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Gemini loan prediction call failed, using deterministic fallback", e)
        }

        // Fallback calculation if network or API error occurs
        return@withContext computeDeterministicPrediction(
            requestedAmount = requestedAmount,
            loanTenureMonths = loanTenureMonths,
            loanPurpose = loanPurpose,
            creditScore = creditScore,
            statedMonthlyIncome = statedMonthlyIncome,
            totalLiquidBalance = totalLiquidBalance,
            recentInflows = recentInflows,
            recentOutflows = recentOutflows,
            budgetGoalDiscipline = activeGoalsProgress
        )
    }

    private fun computeDeterministicPrediction(
        requestedAmount: Double,
        loanTenureMonths: Int,
        loanPurpose: String,
        creditScore: Int,
        statedMonthlyIncome: Double,
        totalLiquidBalance: Double,
        recentInflows: Double,
        recentOutflows: Double,
        budgetGoalDiscipline: Int
    ): GeminiLoanPrediction {
        val effectiveIncome = if (statedMonthlyIncome > 0) statedMonthlyIncome else 6500.0
        val maxAllowableEmi = effectiveIncome * 0.45
        val baseApr = when {
            creditScore >= 780 -> 4.99
            creditScore >= 720 -> 6.49
            creditScore >= 660 -> 9.25
            else -> 13.99
        }

        val monthlyRate = (baseApr / 12.0) / 100.0
        val requestedEmi = if (loanTenureMonths > 0) {
            val factor = Math.pow(1.0 + monthlyRate, loanTenureMonths.toDouble())
            if (factor > 1.0) (requestedAmount * monthlyRate * factor) / (factor - 1.0) else (requestedAmount / loanTenureMonths)
        } else requestedAmount

        val isEligible = creditScore >= 620 && requestedEmi <= maxAllowableEmi
        val approvalProb = when {
            creditScore >= 780 && requestedEmi <= maxAllowableEmi * 0.7 -> 94
            creditScore >= 720 && requestedEmi <= maxAllowableEmi -> 85
            isEligible -> 70
            else -> 38
        }

        val maxCap = (maxAllowableEmi * loanTenureMonths * 0.85).coerceAtLeast(10000.0)
        val tier = when {
            creditScore >= 780 -> "EXCEPTIONAL"
            creditScore >= 720 -> "PRIME"
            creditScore >= 660 -> "NEAR-PRIME"
            else -> "SUBPRIME"
        }

        val strengths = mutableListOf<String>()
        val risks = mutableListOf<String>()
        val actions = mutableListOf<String>()

        if (creditScore >= 720) strengths.add("Strong credit score ($creditScore) qualifies for prime tier rates")
        if (totalLiquidBalance > requestedEmi * 6) strengths.add("Solid liquid reserves ($${"%,.0f".format(Locale.US, totalLiquidBalance)}) provide a 6+ month safety buffer")
        if (budgetGoalDiscipline >= 50) strengths.add("Good savings discipline ($budgetGoalDiscipline% completion rate on goals)")

        if (requestedEmi > maxAllowableEmi * 0.8) risks.add("Monthly installment ($${"%,.2f".format(Locale.US, requestedEmi)}) approaches 45% DTI threshold")
        if (creditScore < 680) risks.add("Credit score under 680 subjects loan to increased risk-weighting")
        if (loanTenureMonths > 48) risks.add("Extended duration ($loanTenureMonths mos) accrues cumulative interest")

        actions.add("Setting up autopay will lower interest by an additional 0.25%")
        actions.add("Maintaining a debt-to-income ratio below 35% ensures seamless instant disbursement")

        val summary = if (isEligible) {
            "Applicant is highly eligible for $loanPurpose with a $approvalProb% predictive approval confidence. Estimated monthly installment is $${"%,.2f".format(Locale.US, requestedEmi)} at ${"%.2f".format(Locale.US, baseApr)}% APR."
        } else {
            "Application requires additional collateral or a co-applicant because requested EMI exceeds safe Debt-to-Income parameters."
        }

        return GeminiLoanPrediction(
            isEligible = isEligible,
            approvalProbabilityPercent = approvalProb,
            maxRecommendedLoanAmount = maxCap,
            recommendedApr = baseApr,
            creditRiskTier = tier,
            maxMonthlyEmiCapacity = maxAllowableEmi,
            keyStrengths = strengths,
            riskFactors = risks,
            actionableRecommendations = actions,
            executiveSummary = summary,
            isAiPowered = false
        )
    }
}
