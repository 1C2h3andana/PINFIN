package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.AccountEntity
import com.example.data.model.ApplicationFormEntity
import com.example.data.model.ApplicationStatus
import com.example.data.model.BillEntity
import com.example.data.model.BudgetEntity
import com.example.data.model.InsuranceClaimEntity
import com.example.data.model.InsurancePolicyEntity
import com.example.data.model.InvestmentEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.SecurityLogEntity
import com.example.data.model.SupportTicketEntity
import com.example.data.model.Transaction
import com.example.data.model.TransactionCategory
import com.example.data.model.TransactionEntity
import com.example.data.model.TransactionStatus
import com.example.data.model.UserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BankDao {

    // --- Users & Auth ---
    @Query("SELECT * FROM users ORDER BY id ASC")
    fun getAllUsers(): Flow<List<UserEntity>>

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE id = :id LIMIT 1")
    suspend fun getUserById(id: Long): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(user: UserEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(users: List<UserEntity>)

    @Update
    suspend fun updateUser(user: UserEntity)

    // --- Accounts ---
    @Query("SELECT * FROM accounts ORDER BY id ASC")
    fun getAllAccounts(): Flow<List<AccountEntity>>

    @Query("SELECT * FROM accounts WHERE id = :id")
    suspend fun getAccountById(id: Long): AccountEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: AccountEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccounts(accounts: List<AccountEntity>)

    @Update
    suspend fun updateAccount(account: AccountEntity)

    @Query("UPDATE accounts SET balance = balance + :delta WHERE id = :id")
    suspend fun updateAccountBalance(id: Long, delta: Double)

    @Query("UPDATE accounts SET isFrozen = :frozen WHERE id = :id")
    suspend fun updateCardFrozen(id: Long, frozen: Boolean)

    @Query("UPDATE accounts SET isContactlessEnabled = :enabled WHERE id = :id")
    suspend fun updateContactless(id: Long, enabled: Boolean)

    @Query("UPDATE accounts SET isInternationalEnabled = :enabled WHERE id = :id")
    suspend fun updateInternational(id: Long, enabled: Boolean)

    @Query("UPDATE accounts SET dailyLimit = :limit WHERE id = :id")
    suspend fun updateDailyLimit(id: Long, limit: Double)

    // --- Transactions ---
    @Query("SELECT * FROM transactions ORDER BY timestamp DESC")
    fun getAllTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE accountId = :accountId ORDER BY timestamp DESC")
    fun getTransactionsForAccount(accountId: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE riskScore >= 50 ORDER BY timestamp DESC")
    fun getHighRiskTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE status = 'FLAGGED_REVIEW' OR status = 'BLOCKED_FRAUD' ORDER BY timestamp DESC")
    fun getAmlReviewTransactions(): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE timestamp >= :sinceTimestamp AND type = 'DEBIT' AND status != 'REVERTED' AND status != 'BLOCKED_FRAUD' ORDER BY timestamp ASC")
    fun getSpendingTransactionsSince(sinceTimestamp: Long): Flow<List<TransactionEntity>>

    @Query("SELECT * FROM transactions WHERE timestamp >= :sinceTimestamp ORDER BY timestamp ASC")
    fun getTransactionsSince(sinceTimestamp: Long): Flow<List<TransactionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransaction(transaction: TransactionEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactions(transactions: List<TransactionEntity>)

    @Update
    suspend fun updateTransaction(transaction: TransactionEntity)

    @Query("UPDATE transactions SET status = :newStatus WHERE id = :id")
    suspend fun updateTransactionStatus(id: Long, newStatus: TransactionStatus)

    // --- Transaction Records (Offline-First Transaction History) ---
    @Query("SELECT * FROM transaction_records ORDER BY date DESC")
    fun getAllTransactionRecords(): Flow<List<Transaction>>

    @Query("SELECT * FROM transaction_records WHERE id = :id LIMIT 1")
    suspend fun getTransactionRecordById(id: Long): Transaction?

    @Query("SELECT * FROM transaction_records WHERE type = :type ORDER BY date DESC")
    fun getTransactionsByType(type: String): Flow<List<Transaction>>

    @Query("SELECT * FROM transaction_records WHERE category = :category ORDER BY date DESC")
    fun getTransactionsByCategoryName(category: String): Flow<List<Transaction>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionRecord(transaction: Transaction): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransactionRecords(transactions: List<Transaction>)

    @Update
    suspend fun updateTransactionRecord(transaction: Transaction)

    @Query("DELETE FROM transaction_records WHERE id = :id")
    suspend fun deleteTransactionRecordById(id: Long)

    @Query("DELETE FROM transaction_records")
    suspend fun deleteAllTransactionRecords()

    // --- Investments ---
    @Query("SELECT * FROM investments ORDER BY id ASC")
    fun getAllInvestments(): Flow<List<InvestmentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvestments(investments: List<InvestmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInvestment(investment: InvestmentEntity): Long

    @Update
    suspend fun updateInvestment(investment: InvestmentEntity)

    @Query("SELECT * FROM investments WHERE symbol = :symbol LIMIT 1")
    suspend fun getInvestmentBySymbol(symbol: String): InvestmentEntity?

    // --- Insurance Policies & Claims ---
    @Query("SELECT * FROM insurance_policies ORDER BY id ASC")
    fun getAllInsurancePolicies(): Flow<List<InsurancePolicyEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInsurancePolicies(policies: List<InsurancePolicyEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInsurancePolicy(policy: InsurancePolicyEntity): Long

    @Query("SELECT * FROM insurance_claims ORDER BY timestamp DESC")
    fun getAllInsuranceClaims(): Flow<List<InsuranceClaimEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInsuranceClaim(claim: InsuranceClaimEntity): Long

    // --- Application Forms ---
    @Query("SELECT * FROM application_forms ORDER BY timestamp DESC")
    fun getAllApplications(): Flow<List<ApplicationFormEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplication(app: ApplicationFormEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertApplications(apps: List<ApplicationFormEntity>)

    @Query("UPDATE application_forms SET status = :status WHERE id = :id")
    suspend fun updateApplicationStatus(id: Long, status: ApplicationStatus)

    // --- Notifications ---
    @Query("SELECT * FROM notifications ORDER BY timestamp DESC")
    fun getAllNotifications(): Flow<List<NotificationEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotification(notification: NotificationEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNotifications(notifications: List<NotificationEntity>)

    @Query("UPDATE notifications SET isRead = 1 WHERE id = :id")
    suspend fun markNotificationRead(id: Long)

    @Query("UPDATE notifications SET isRead = 1")
    suspend fun markAllNotificationsRead()

    @Query("DELETE FROM notifications")
    suspend fun clearAllNotifications()

    // --- Budgets ---
    @Query("SELECT * FROM budgets")
    fun getAllBudgets(): Flow<List<BudgetEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBudgets(budgets: List<BudgetEntity>)

    @Update
    suspend fun updateBudget(budget: BudgetEntity)

    @Query("UPDATE budgets SET currentSpent = currentSpent + :amount WHERE category = :category")
    suspend fun addBudgetSpent(category: TransactionCategory, amount: Double)

    // --- Savings Goals ---
    @Query("SELECT * FROM savings_goals")
    fun getAllSavingsGoals(): Flow<List<SavingsGoalEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavingsGoal(goal: SavingsGoalEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSavingsGoals(goals: List<SavingsGoalEntity>)

    @Update
    suspend fun updateSavingsGoal(goal: SavingsGoalEntity)

    @Query("UPDATE savings_goals SET currentAmount = currentAmount + :amount WHERE id = :goalId")
    suspend fun depositToSavingsGoal(goalId: Long, amount: Double)

    // --- Bills ---
    @Query("SELECT * FROM bills ORDER BY isPaid ASC, id ASC")
    fun getAllBills(): Flow<List<BillEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: BillEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBills(bills: List<BillEntity>)

    @Update
    suspend fun updateBill(bill: BillEntity)

    @Query("UPDATE bills SET isPaid = 1 WHERE id = :billId")
    suspend fun markBillPaid(billId: Long)

    @Query("UPDATE bills SET isAutoPay = :autoPay WHERE id = :billId")
    suspend fun toggleBillAutoPay(billId: Long, autoPay: Boolean)

    // --- Security Logs ---
    @Query("SELECT * FROM security_logs ORDER BY timestamp DESC LIMIT 100")
    fun getAllSecurityLogs(): Flow<List<SecurityLogEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSecurityLog(log: SecurityLogEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSecurityLogs(logs: List<SecurityLogEntity>)

    // --- Support Tickets ---
    @Query("SELECT * FROM support_tickets ORDER BY timestamp DESC")
    fun getAllTickets(): Flow<List<SupportTicketEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTicket(ticket: SupportTicketEntity): Long

    @Update
    suspend fun updateTicket(ticket: SupportTicketEntity)
}
