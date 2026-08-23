package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

enum class UserRole {
    USER,
    ADMIN
}

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val fullName: String,
    val email: String,
    val passwordHash: String,
    val salt: String,
    val role: UserRole = UserRole.USER,
    val jwtToken: String = "",
    val isBiometricEnabled: Boolean = true,
    val pinCode: String = "1234",
    val isLocked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

enum class AccountType {
    CHECKING,
    SAVINGS,
    INVESTMENT,
    CREDIT_CARD
}

@Entity(tableName = "accounts")
data class AccountEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val accountNumber: String,
    val routingNumber: String,
    val balance: Double,
    val type: AccountType,
    val cardNumber: String,
    val expiry: String,
    val cvv: String,
    val isFrozen: Boolean = false,
    val dailyLimit: Double = 5000.0,
    val spentToday: Double = 0.0,
    val isContactlessEnabled: Boolean = true,
    val isInternationalEnabled: Boolean = false
)

enum class TransactionCategory {
    FOOD,
    SHOPPING,
    BILLS,
    TRANSFER,
    SALARY,
    INVESTMENT,
    ENTERTAINMENT,
    HEALTHCARE,
    EDUCATION,
    DEPOSIT,
    WITHDRAWAL,
    OTHER
}

enum class TransactionType {
    DEBIT,
    CREDIT
}

enum class RiskLevel {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

enum class TransactionStatus {
    COMPLETED,
    PENDING,
    BLOCKED_FRAUD,
    FLAGGED_REVIEW,
    REVERTED
}

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val accountId: Long,
    val title: String,
    val recipient: String,
    val upiOrHandle: String = "",
    val amount: Double,
    val category: TransactionCategory,
    val type: TransactionType,
    val timestamp: Long = System.currentTimeMillis(),
    val status: TransactionStatus = TransactionStatus.COMPLETED,
    val riskScore: Int = 5, // 0 - 100
    val riskLevel: RiskLevel = RiskLevel.LOW,
    val riskReason: String = "Normal legitimate transaction",
    val is2FaVerified: Boolean = false,
    val deviceOrigin: String = "Pixel 8 Pro (Current Device)",
    val location: String = "New York, USA",
    val note: String = ""
)

@Entity(tableName = "budgets")
data class BudgetEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val category: TransactionCategory,
    val monthlyLimit: Double,
    val currentSpent: Double = 0.0,
    val monthYear: String = "08-2026"
)

@Entity(tableName = "savings_goals")
data class SavingsGoalEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val targetAmount: Double,
    val currentAmount: Double,
    val targetDate: String,
    val iconKey: String = "SAVINGS",
    val isCompleted: Boolean = false
)

@Entity(tableName = "bills")
data class BillEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val billerName: String,
    val category: String,
    val amount: Double,
    val dueDate: String,
    val isAutoPay: Boolean = false,
    val isPaid: Boolean = false,
    val accountBilled: String = "Checking ...4829"
)

enum class AssetType {
    STOCK,
    CRYPTO,
    ETF,
    GOLD,
    MUTUAL_FUND
}

@Entity(tableName = "investments")
data class InvestmentEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val symbol: String,
    val name: String,
    val assetType: AssetType,
    val sharesOrUnits: Double,
    val avgBuyPrice: Double,
    val currentPrice: Double,
    val dailyChangePercent: Double,
    val allocationPercent: Double = 0.0
)

enum class InsuranceType {
    HEALTH,
    LIFE,
    AUTO,
    CYBER_SHIELD,
    HOME
}

enum class PolicyStatus {
    ACTIVE,
    CLAIM_PENDING,
    EXPIRED
}

@Entity(tableName = "insurance_policies")
data class InsurancePolicyEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val policyNumber: String,
    val type: InsuranceType,
    val planName: String,
    val provider: String,
    val coverageAmount: Double,
    val monthlyPremium: Double,
    val nextBillingDate: String,
    val status: PolicyStatus = PolicyStatus.ACTIVE,
    val keyFeatures: String = "Zero Deductible • 24/7 Claim Support"
)

enum class ClaimStatus {
    SUBMITTED,
    UNDER_REVIEW,
    APPROVED,
    REJECTED
}

@Entity(tableName = "insurance_claims")
data class InsuranceClaimEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val policyId: Long,
    val policyType: InsuranceType,
    val claimAmount: Double,
    val incidentDate: String,
    val description: String,
    val status: ClaimStatus = ClaimStatus.SUBMITTED,
    val timestamp: Long = System.currentTimeMillis()
)

enum class ApplicationType {
    LOAN,
    CREDIT_CARD,
    ACCOUNT_OPENING,
    INSURANCE_POLICY
}

enum class ApplicationStatus {
    SUBMITTED,
    AI_PRE_APPROVED,
    APPROVED,
    UNDER_REVIEW,
    REJECTED
}

@Entity(tableName = "application_forms")
data class ApplicationFormEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: ApplicationType,
    val applicantName: String,
    val email: String,
    val phone: String,
    val annualIncome: Double,
    val employmentType: String,
    val requestedAmountOrTier: String,
    val idProofType: String,
    val idProofNumber: String,
    val status: ApplicationStatus = ApplicationStatus.AI_PRE_APPROVED,
    val aiScore: Int = 88,
    val aiRemarks: String = "Automated AI Underwriting: Strong credit profile & low risk factors",
    val timestamp: Long = System.currentTimeMillis()
)

enum class NotificationType {
    SECURITY_ALERT,
    PAYMENT_DUE,
    TRANSACTION_ALERT,
    FRAUD_WARNING,
    APPLICATION_UPDATE,
    PROMO
}

@Entity(tableName = "notifications")
data class NotificationEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val message: String,
    val type: NotificationType,
    val timestamp: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val actionRoute: String = ""
)

enum class SecurityEventType {
    LOGIN_SUCCESS,
    LOGIN_FAILED,
    SUSPICIOUS_TRANSFER_BLOCKED,
    OTP_CHALLENGE_ISSUED,
    DEVICE_LINKED,
    CARD_FROZEN,
    PIN_CHANGED,
    SCAM_SCAN_PERFORMED,
    LIMIT_UPDATED,
    DEPOSIT_COMPLETED,
    WITHDRAWAL_COMPLETED,
    ADMIN_TRANSACTION_APPROVED,
    ADMIN_TRANSACTION_REVERTED
}

@Entity(tableName = "security_logs")
data class SecurityLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val eventType: SecurityEventType,
    val details: String,
    val ipAddress: String = "192.168.1.45",
    val deviceName: String = "Android Device",
    val location: String = "New York, US",
    val severity: RiskLevel = RiskLevel.LOW
)

enum class TicketStatus {
    OPEN,
    AI_RESOLVING,
    RESOLVED,
    ESCALATED_TO_AGENT
}

@Entity(tableName = "support_tickets")
data class SupportTicketEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val description: String,
    val category: String,
    val status: TicketStatus = TicketStatus.OPEN,
    val timestamp: Long = System.currentTimeMillis(),
    val aiResponse: String = ""
)

data class LoanCalculationResult(
    val monthlyEmi: Double,
    val totalInterest: Double,
    val totalAmountPayable: Double,
    val principalAmount: Double,
    val interestRate: Double,
    val tenureMonths: Int
)

data class LoanEligibilityResult(
    val isEligible: Boolean,
    val maxEligibleAmount: Double,
    val recommendedInterestRate: Double,
    val creditRiskTier: String,
    val debtToIncomeRatio: Double,
    val analysisNotes: List<String>
)

data class RiskEvaluation(
    val score: Int, // 0 - 100
    val level: RiskLevel,
    val requires2Fa: Boolean,
    val shouldBlock: Boolean,
    val flags: List<String>,
    val recommendation: String
)
