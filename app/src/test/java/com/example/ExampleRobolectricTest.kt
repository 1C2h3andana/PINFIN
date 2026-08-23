package com.example

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.example.data.dao.BankDao
import com.example.data.db.BankDatabase
import com.example.data.model.AccountEntity
import com.example.data.model.AccountType
import com.example.data.model.RiskLevel
import com.example.domain.engine.FraudDetectionEngine
import com.example.domain.loan.LoanCalculator
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
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

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, BankDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.bankDao()
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
            annualRate = 12.0,
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
            cardNumber = "4532 8921 0012 3456",
            expiry = "12/28",
            cvv = "921",
            type = AccountType.CHECKING,
            balance = 5000.0
        )
        dao.insertAccount(account)

        val retrieved = dao.getAccountById(1)
        assertEquals(5000.0, retrieved?.balance ?: 0.0, 0.01)

        dao.updateAccountBalance(1, 4200.0)
        val updated = dao.getAccountById(1)
        assertEquals(4200.0, updated?.balance ?: 0.0, 0.01)
    }
}
