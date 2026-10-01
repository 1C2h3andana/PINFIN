package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.BankDatabase
import com.example.data.model.AccountEntity
import com.example.domain.ai.FinancialInputProfile
import com.example.domain.ai.FinancialProfilePreset
import com.example.domain.ai.PersonalizedLoanEligibilityService
import com.example.domain.ai.PersonalizedLoanPrediction
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale

/**
 * UI State holding user inputs, calculated real-time ratios, and Gemini AI prediction results.
 */
data class LoanEligibilityUiState(
    val monthlyIncome: Double = 7500.0,
    val employmentStatus: String = "Full-Time Employed",
    val creditScore: Int = 740,
    val existingMonthlyDebt: Double = 650.0,
    val requestedAmount: Double = 35000.0,
    val tenureMonths: Int = 36,
    val loanPurpose: String = "Home Improvement",
    val liquidSavings: Double = 18000.0,
    val collateralType: String = "Unsecured",
    val additionalIncome: Double = 0.0,

    // Real-time live computed metrics
    val liveCurrentDti: Double = 8.67,
    val liveEstimatedEmi: Double = 1068.50,
    val liveProjectedDti: Double = 22.91,

    // Async Evaluation State
    val isLoading: Boolean = false,
    val statusMessage: String? = null,
    val prediction: PersonalizedLoanPrediction? = null,
    val errorMessage: String? = null,
    val selectedPresetId: String? = null
)

/**
 * ViewModel responsible for managing financial user inputs and delegating
 * personalized loan underwriting predictions to the Gemini AI API.
 */
class LoanEligibilityViewModel(application: Application) : AndroidViewModel(application) {

    private val _uiState = MutableStateFlow(LoanEligibilityUiState())
    val uiState: StateFlow<LoanEligibilityUiState> = _uiState.asStateFlow()

    init {
        // Compute initial metrics
        recalculateLiveMetrics()

        // Optionally read user profile defaults from local Room database
        viewModelScope.launch {
            try {
                val db = BankDatabase.getDatabase(getApplication())
                val accounts: List<AccountEntity>? = db.bankDao().getAllAccounts().firstOrNull()
                if (!accounts.isNullOrEmpty()) {
                    val totalBalance = accounts.sumOf { it.balance }
                    if (totalBalance > 0.0) {
                        _uiState.update { it.copy(liquidSavings = totalBalance) }
                        recalculateLiveMetrics()
                    }
                }
            } catch (e: Exception) {
                // Room defaults optional, proceed with default values
            }
        }
    }

    fun setMonthlyIncome(income: Double) {
        _uiState.update { it.copy(monthlyIncome = income.coerceAtLeast(500.0), selectedPresetId = null) }
        recalculateLiveMetrics()
    }

    fun setEmploymentStatus(status: String) {
        _uiState.update { it.copy(employmentStatus = status, selectedPresetId = null) }
        recalculateLiveMetrics()
    }

    fun setCreditScore(score: Int) {
        _uiState.update { it.copy(creditScore = score.coerceIn(300, 850), selectedPresetId = null) }
        recalculateLiveMetrics()
    }

    fun setExistingMonthlyDebt(debt: Double) {
        _uiState.update { it.copy(existingMonthlyDebt = debt.coerceAtLeast(0.0), selectedPresetId = null) }
        recalculateLiveMetrics()
    }

    fun setRequestedAmount(amount: Double) {
        _uiState.update { it.copy(requestedAmount = amount.coerceAtLeast(1000.0), selectedPresetId = null) }
        recalculateLiveMetrics()
    }

    fun setTenureMonths(tenure: Int) {
        _uiState.update { it.copy(tenureMonths = tenure.coerceIn(6, 84), selectedPresetId = null) }
        recalculateLiveMetrics()
    }

    fun setLoanPurpose(purpose: String) {
        _uiState.update { it.copy(loanPurpose = purpose, selectedPresetId = null) }
    }

    fun setLiquidSavings(savings: Double) {
        _uiState.update { it.copy(liquidSavings = savings.coerceAtLeast(0.0), selectedPresetId = null) }
    }

    fun setCollateralType(collateral: String) {
        _uiState.update { it.copy(collateralType = collateral, selectedPresetId = null) }
        recalculateLiveMetrics()
    }

    fun setAdditionalIncome(income: Double) {
        _uiState.update { it.copy(additionalIncome = income.coerceAtLeast(0.0), selectedPresetId = null) }
        recalculateLiveMetrics()
    }

    fun applyPreset(preset: FinancialProfilePreset) {
        _uiState.update { current ->
            current.copy(
                monthlyIncome = preset.profile.monthlyIncome,
                employmentStatus = preset.profile.employmentStatus,
                creditScore = preset.profile.creditScore,
                existingMonthlyDebt = preset.profile.existingMonthlyDebt,
                requestedAmount = preset.profile.requestedAmount,
                tenureMonths = preset.profile.tenureMonths,
                loanPurpose = preset.profile.loanPurpose,
                liquidSavings = preset.profile.liquidSavings,
                collateralType = preset.profile.collateralType,
                selectedPresetId = preset.id,
                prediction = null,
                errorMessage = null
            )
        }
        recalculateLiveMetrics()
    }

    fun resetForm() {
        _uiState.value = LoanEligibilityUiState()
        recalculateLiveMetrics()
    }

    fun clearPrediction() {
        _uiState.update { it.copy(prediction = null, errorMessage = null) }
    }

    /**
     * Executes the Gemini AI loan eligibility prediction using the current inputs.
     */
    fun evaluateEligibility() {
        val currentState = _uiState.value
        val input = FinancialInputProfile(
            monthlyIncome = currentState.monthlyIncome,
            employmentStatus = currentState.employmentStatus,
            creditScore = currentState.creditScore,
            existingMonthlyDebt = currentState.existingMonthlyDebt,
            requestedAmount = currentState.requestedAmount,
            tenureMonths = currentState.tenureMonths,
            loanPurpose = currentState.loanPurpose,
            liquidSavings = currentState.liquidSavings,
            collateralType = currentState.collateralType,
            additionalHouseholdIncome = currentState.additionalIncome
        )

        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isLoading = true,
                    statusMessage = "Connecting to Gemini AI underwriting model...",
                    errorMessage = null
                )
            }

            try {
                _uiState.update { it.copy(statusMessage = "Gemini 3.5 Flash is analyzing credit risk, DTI ratios, and liquidity...") }
                val prediction = PersonalizedLoanEligibilityService.predictEligibility(input)

                _uiState.update {
                    it.copy(
                        isLoading = false,
                        statusMessage = null,
                        prediction = prediction,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        statusMessage = null,
                        errorMessage = "Evaluation encountered an issue: ${e.localizedMessage ?: "Unknown error"}"
                    )
                }
            }
        }
    }

    /**
     * Dynamically updates live DTI and estimated EMI as the user tweaks parameters.
     */
    private fun recalculateLiveMetrics() {
        val state = _uiState.value
        val totalIncome = state.monthlyIncome + (state.additionalIncome / 12.0)
        val currentDti = if (totalIncome > 0) (state.existingMonthlyDebt / totalIncome) * 100.0 else 0.0

        val estimatedApr = when {
            state.creditScore >= 780 -> 5.25
            state.creditScore >= 720 -> 6.85
            state.creditScore >= 670 -> 9.75
            state.creditScore >= 620 -> 14.50
            else -> 19.99
        }
        val monthlyRate = (estimatedApr / 100.0) / 12.0
        val n = state.tenureMonths.coerceAtLeast(1)

        val liveEmi = if (monthlyRate > 0.0) {
            val factor = Math.pow(1.0 + monthlyRate, n.toDouble())
            if (factor > 1.0) (state.requestedAmount * monthlyRate * factor) / (factor - 1.0)
            else state.requestedAmount / n
        } else {
            state.requestedAmount / n
        }

        val projectedTotalDebt = state.existingMonthlyDebt + liveEmi
        val projectedDti = if (totalIncome > 0) (projectedTotalDebt / totalIncome) * 100.0 else 0.0

        _uiState.update {
            it.copy(
                liveCurrentDti = currentDti,
                liveEstimatedEmi = liveEmi,
                liveProjectedDti = projectedDti
            )
        }
    }
}
