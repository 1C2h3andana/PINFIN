package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.dao.BankDao
import com.example.data.dao.BudgetGoalDao
import com.example.data.dao.TransactionDao
import com.example.data.db.BankDatabase
import com.example.data.model.AccountEntity
import com.example.data.model.AccountType
import com.example.data.model.BudgetGoal
import com.example.data.model.RiskLevel
import com.example.data.model.Transaction
import com.example.data.repository.TransactionRepository
import com.example.domain.engine.FraudDetectionEngine
import com.example.domain.loan.LoanCalculator
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class ExampleRobolectricTest {

    private lateinit var db: BankDatabase
    private lateinit var dao: BankDao
    private lateinit var transactionDao: TransactionDao
    private lateinit var budgetGoalDao: BudgetGoalDao
    private lateinit var recurringTransactionDao: com.example.data.dao.RecurringTransactionDao
    private lateinit var transactionRepository: TransactionRepository

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, BankDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.bankDao()
        transactionDao = db.transactionDao()
        budgetGoalDao = db.budgetGoalDao()
        recurringTransactionDao = db.recurringTransactionDao()
        transactionRepository = TransactionRepository(transactionDao, budgetGoalDao, recurringTransactionDao)
    }

    @After
    fun closeDb() {
        db.close()
    }

    @Test
    fun `test app name resource`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("SmartBank AI", appName)
    }

    @Test
    fun `test fraud detection detects scam keywords and block threshold`() {
        val eval = FraudDetectionEngine.evaluateTransaction(
            amount = 9000.0,
            recipient = "Fake Lottery Reward",
            upiOrHandle = "scam.winner@paytm.xyz",
            currentBalance = 10000.0,
            note = "lottery prize claim"
        )
        assertTrue(eval.score >= 70)
        assertEquals(RiskLevel.CRITICAL, eval.level)
        assertTrue(eval.shouldBlock)
    }

    @Test
    fun `test loan emi calculation accuracy`() {
        val res = LoanCalculator.calculateEmi(
            principal = 10000.0,
            annualRatePercent = 12.0,
            tenureMonths = 12
        )
        // EMI for 10000 at 12% for 12 months is ~888.49
        assertEquals(888.49, res.monthlyEmi, 1.0)
        assertTrue(res.totalAmountPayable > 10000.0)
    }

    @Test
    fun `test room dao account balance manipulation`() = runBlocking {
        val account = AccountEntity(
            id = 1,
            name = "Test Primary Checking",
            accountNumber = "9876543210",
            routingNumber = "021000021",
            cardNumber = "4532 8921 0012 3456",
            expiry = "12/28",
            cvv = "921",
            type = AccountType.CHECKING,
            balance = 5000.0
        )
        dao.insertAccount(account)

        val retrieved = dao.getAccountById(1)
        assertEquals(5000.0, retrieved?.balance ?: 0.0, 0.01)

        dao.updateAccountBalance(1, -800.0)
        val updated = dao.getAccountById(1)
        assertEquals(4200.0, updated?.balance ?: 0.0, 0.01)
    }

    @Test
    fun `test transaction dao and repository CRUD operations`() = runBlocking {
        val tx1 = Transaction(
            amount = 150.0,
            category = "Groceries",
            date = System.currentTimeMillis(),
            type = "expense"
        )
        val txId = transactionRepository.insertTransaction(tx1)
        assertTrue(txId > 0)

        val retrieved = transactionRepository.getTransactionById(txId)
        assertNotNull(retrieved)
        assertEquals(150.0, retrieved!!.amount, 0.001)
        assertEquals("Groceries", retrieved.category)
        assertEquals("expense", retrieved.type)

        val list = transactionRepository.allTransactions.first()
        assertEquals(1, list.size)

        val updatedTx = retrieved.copy(amount = 200.0)
        transactionRepository.updateTransaction(updatedTx)
        val afterUpdate = transactionRepository.getTransactionById(txId)
        assertEquals(200.0, afterUpdate?.amount ?: 0.0, 0.001)

        transactionRepository.deleteTransactionById(txId)
        val afterDelete = transactionRepository.getTransactionById(txId)
        assertNull(afterDelete)
    }

    @Test
    fun `test budget goal entity and repository CRUD operations`() = runBlocking {
        val goal = BudgetGoal(
            category = "Vacation Fund",
            targetAmount = 5000.0,
            currentProgress = 1200.0,
            deadline = "Dec 2026"
        )
        val goalId = transactionRepository.insertBudgetGoal(goal)
        assertTrue(goalId > 0)

        val retrieved = transactionRepository.getBudgetGoalById(goalId)
        assertNotNull(retrieved)
        assertEquals("Vacation Fund", retrieved!!.category)
        assertEquals(5000.0, retrieved.targetAmount, 0.001)
        assertEquals(1200.0, retrieved.currentProgress, 0.001)
        assertEquals("Dec 2026", retrieved.deadline)
        assertEquals(24.0f, retrieved.progressPercentage, 0.1f)
        assertEquals(3800.0, retrieved.remainingAmount, 0.001)
        assertFalse(retrieved.isCompleted)

        transactionRepository.updateBudgetProgress(goalId, 3800.0)
        val updated = transactionRepository.getBudgetGoalById(goalId)
        assertEquals(5000.0, updated?.currentProgress ?: 0.0, 0.001)
        assertTrue(updated!!.isCompleted)
    }

    @Test
    fun `test fraud alert entity and dao operations`() = runBlocking {
        val fraudDao = db.fraudAlertDao()
        val alert = com.example.data.model.FraudAlert(
            description = "Unusual overseas ATM withdrawal attempt",
            severityLevel = "HIGH",
            timestamp = System.currentTimeMillis()
        )
        val alertId = fraudDao.insertFraudAlert(alert)
        assertTrue(alertId > 0)

        val retrieved = fraudDao.getFraudAlertById(alertId)
        assertNotNull(retrieved)
        assertEquals("Unusual overseas ATM withdrawal attempt", retrieved!!.description)
        assertEquals("HIGH", retrieved.severityLevel)

        val allAlerts = fraudDao.getAllFraudAlerts().first()
        assertEquals(1, allAlerts.size)

        fraudDao.deleteFraudAlertById(alertId)
        val afterDelete = fraudDao.getFraudAlertById(alertId)
        assertNull(afterDelete)
    }

    @Test
    fun `test gemini loan eligibility prediction logic with room data`() = runBlocking {
        val accounts = listOf(
            AccountEntity(
                id = 1,
                name = "Primary Checking",
                accountNumber = "ACC-01",
                routingNumber = "12200049",
                balance = 18500.0,
                type = com.example.data.model.AccountType.CHECKING,
                cardNumber = "4532-XXXX-XXXX-9901",
                expiry = "12/28",
                cvv = "842"
            )
        )
        val transactions = listOf(
            Transaction(id = 1, amount = 4200.0, category = "Direct Deposit", date = System.currentTimeMillis(), type = "income"),
            Transaction(id = 2, amount = 1100.0, category = "Rent", date = System.currentTimeMillis(), type = "expense")
        )
        val budgetGoals = listOf(
            com.example.data.model.BudgetGoal(id = 1, category = "Downpayment", targetAmount = 10000.0, currentProgress = 6500.0)
        )

        val prediction = com.example.domain.ai.GeminiLoanAdvisorService.predictLoanEligibility(
            requestedAmount = 25000.0,
            loanTenureMonths = 36,
            loanPurpose = "Vehicle & Mobility",
            creditScore = 760,
            statedMonthlyIncome = 6500.0,
            accounts = accounts,
            transactions = transactions,
            budgetGoals = budgetGoals
        )

        assertNotNull(prediction)
        assertTrue(prediction.approvalProbabilityPercent > 50)
        assertTrue(prediction.maxRecommendedLoanAmount > 0)
        assertTrue(prediction.recommendedApr in 2.0..25.0)
        assertFalse(prediction.executiveSummary.isBlank())
    }

    @Test
    fun testBiometricStatusManager() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val status = com.example.security.BiometricAuthManager.checkBiometricStatus(context)
        // Verify that status is one of the valid BiometricStatus enum constants
        assertNotNull(status)
        assertTrue(
            status == com.example.security.BiometricAuthManager.BiometricStatus.AVAILABLE ||
            status == com.example.security.BiometricAuthManager.BiometricStatus.NO_HARDWARE ||
            status == com.example.security.BiometricAuthManager.BiometricStatus.HARDWARE_UNAVAILABLE ||
            status == com.example.security.BiometricAuthManager.BiometricStatus.NONE_ENROLLED ||
            status == com.example.security.BiometricAuthManager.BiometricStatus.SECURITY_UPDATE_REQUIRED ||
            status == com.example.security.BiometricAuthManager.BiometricStatus.UNSUPPORTED
        )
    }

    @Test
    fun testRoomDatabaseSpendingTrendsLast30Days() = runBlocking {
        val now = System.currentTimeMillis()
        val dayMs = 86_400_000L

        // Insert sample debit transactions over the past 30 days
        val tx1 = com.example.data.model.TransactionEntity(
            id = 101,
            accountId = 1,
            title = "Groceries Supermarket",
            recipient = "Whole Foods",
            amount = 125.50,
            category = com.example.data.model.TransactionCategory.FOOD,
            type = com.example.data.model.TransactionType.DEBIT,
            timestamp = now - (2 * dayMs),
            status = com.example.data.model.TransactionStatus.COMPLETED
        )
        val tx2 = com.example.data.model.TransactionEntity(
            id = 102,
            accountId = 1,
            title = "Electric Utility Bill",
            recipient = "ConEd",
            amount = 85.20,
            category = com.example.data.model.TransactionCategory.BILLS,
            type = com.example.data.model.TransactionType.DEBIT,
            timestamp = now - (5 * dayMs),
            status = com.example.data.model.TransactionStatus.COMPLETED
        )
        val tx3 = com.example.data.model.TransactionEntity(
            id = 103,
            accountId = 1,
            title = "Salary Deposit",
            recipient = "Tech Corp",
            amount = 4500.00,
            category = com.example.data.model.TransactionCategory.SALARY,
            type = com.example.data.model.TransactionType.CREDIT,
            timestamp = now - (3 * dayMs),
            status = com.example.data.model.TransactionStatus.COMPLETED
        )

        dao.insertTransactions(listOf(tx1, tx2, tx3))

        // Query spending transactions from Room DAO
        val thirtyDaysAgo = now - (30 * dayMs)
        val spendingTxs = dao.getSpendingTransactionsSince(thirtyDaysAgo).first()

        // CREDIT transaction should be excluded; only DEBIT transactions included
        assertEquals(2, spendingTxs.size)
        assertTrue(spendingTxs.all { it.type == com.example.data.model.TransactionType.DEBIT })

        // Calculate 30-day spending points for D3 line chart
        val points = com.example.data.repository.BankRepository.calculateDailySpending(spendingTxs, 30)

        assertEquals(30, points.size)
        val totalSpent = points.sumOf { it.amount }
        assertEquals(210.70, totalSpent, 0.01)
        assertTrue(points.any { it.amount == 125.50 })
        assertTrue(points.any { it.amount == 85.20 })
    }

    @Test
    fun `test recurring transaction DAO insert and queries`() = runBlocking {
        val now = System.currentTimeMillis()
        val item = com.example.data.model.RecurringTransactionEntity(
            title = "Netflix 4K Premium",
            payee = "Netflix Inc.",
            amount = 22.99,
            category = "Entertainment",
            frequency = "MONTHLY",
            intervalMillis = 30L * 24 * 60 * 60 * 1000,
            nextDueTimestamp = now - 1000, // Due now
            accountId = 1,
            accountName = "Premier Checking",
            isActive = true
        )

        val id = recurringTransactionDao.insertRecurringTransaction(item)
        assertTrue(id > 0)

        val retrieved = recurringTransactionDao.getRecurringTransactionById(id)
        assertNotNull(retrieved)
        assertEquals("Netflix 4K Premium", retrieved?.title)
        assertEquals(22.99, retrieved?.amount ?: 0.0, 0.001)
        assertTrue(retrieved?.isActive == true)

        val activeList = recurringTransactionDao.getActiveRecurringTransactions().first()
        assertEquals(1, activeList.size)

        val dueList = recurringTransactionDao.getDueRecurringTransactions(now)
        assertEquals(1, dueList.size)

        // Deactivate
        recurringTransactionDao.setRecurringTransactionActive(id, false)
        val activeAfter = recurringTransactionDao.getActiveRecurringTransactions().first()
        assertTrue(activeAfter.isEmpty())
    }

    @Test
    fun `test recurring transaction execution inserts record into Room and updates budget`() = runBlocking {
        val now = System.currentTimeMillis()

        // 1. Seed account
        val account = AccountEntity(
            id = 1,
            userId = 1,
            accountNumber = "CH-4829",
            type = AccountType.CHECKING,
            balance = 5000.0,
            currency = "USD",
            name = "Premier Checking"
        )
        dao.insertAccounts(listOf(account))

        // 2. Seed budget goal for Entertainment
        val budgetGoal = BudgetGoal(
            id = 1,
            category = "Entertainment",
            targetAmount = 100.0,
            currentProgress = 20.0,
            deadline = "End of Month"
        )
        budgetGoalDao.insertBudgetGoals(listOf(budgetGoal))

        // 3. Seed due recurring transaction
        val subscription = com.example.data.model.RecurringTransactionEntity(
            id = 10,
            title = "Spotify Family",
            payee = "Spotify AB",
            amount = 16.99,
            category = "Entertainment",
            frequency = "MONTHLY",
            intervalMillis = 30L * 24 * 60 * 60 * 1000,
            nextDueTimestamp = now - 500, // Due
            accountId = 1,
            accountName = "Premier Checking",
            isActive = true
        )
        recurringTransactionDao.insertRecurringTransaction(subscription)

        // 4. Execute due processing logic
        val context = ApplicationProvider.getApplicationContext<Context>()
        val result = com.example.service.RecurringTransactionManager.processDueTransactionsWithDaos(
            bankDao = dao,
            transactionDao = transactionDao,
            budgetGoalDao = budgetGoalDao,
            recurringDao = recurringTransactionDao,
            context = context,
            forceAll = false
        )

        assertEquals(1, result.processedCount)
        assertEquals(16.99, result.totalAmount, 0.01)

        // 5. Verify transaction was inserted into Room
        val insertedTransactions = transactionDao.getAllTransactions().first()
        assertTrue(insertedTransactions.any { it.description.contains("Spotify Family") && it.amount == 16.99 })

        // 6. Verify account balance was debited
        val updatedAccount = dao.getAccountById(1)
        assertEquals(5000.0 - 16.99, updatedAccount?.balance ?: 0.0, 0.01)

        // 7. Verify budget goal progress was incremented
        val updatedGoal = budgetGoalDao.getGoalById(1)
        assertEquals(20.0 + 16.99, updatedGoal?.currentProgress ?: 0.0, 0.01)

        // 8. Verify recurring subscription next due date was advanced
        val updatedSub = recurringTransactionDao.getRecurringTransactionById(10)
        assertTrue((updatedSub?.nextDueTimestamp ?: 0) > now)
        assertEquals(1, updatedSub?.executionCount)
    }
}

