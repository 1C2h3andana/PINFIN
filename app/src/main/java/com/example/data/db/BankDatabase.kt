package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.dao.BankDao
import com.example.data.dao.BudgetGoalDao
import com.example.data.dao.FraudAlertDao
import com.example.data.dao.RecurringTransactionDao
import com.example.data.dao.TransactionDao
import com.example.data.model.AccountEntity
import com.example.data.model.ApplicationFormEntity
import com.example.data.model.BillEntity
import com.example.data.model.BudgetEntity
import com.example.data.model.BudgetGoal
import com.example.data.model.FraudAlert
import com.example.data.model.InsuranceClaimEntity
import com.example.data.model.InsurancePolicyEntity
import com.example.data.model.InvestmentEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.RecurringTransactionEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.SecurityLogEntity
import com.example.data.model.SupportTicketEntity
import com.example.data.model.Transaction
import com.example.data.model.TransactionEntity
import com.example.data.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        AccountEntity::class,
        TransactionEntity::class,
        Transaction::class,
        BudgetGoal::class,
        FraudAlert::class,
        RecurringTransactionEntity::class,
        BudgetEntity::class,
        SavingsGoalEntity::class,
        BillEntity::class,
        InvestmentEntity::class,
        InsurancePolicyEntity::class,
        InsuranceClaimEntity::class,
        ApplicationFormEntity::class,
        NotificationEntity::class,
        SecurityLogEntity::class,
        SupportTicketEntity::class
    ],
    version = 6,
    exportSchema = false
)
abstract class BankDatabase : RoomDatabase() {
    abstract fun bankDao(): BankDao
    abstract fun transactionDao(): TransactionDao
    abstract fun budgetGoalDao(): BudgetGoalDao
    abstract fun fraudAlertDao(): FraudAlertDao
    abstract fun recurringTransactionDao(): RecurringTransactionDao

    companion object {
        private const val DATABASE_NAME = "smart_bank_ai.db"

        @Volatile
        private var INSTANCE: BankDatabase? = null

        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Version 1 to 2 migrations if needed
            }
        }

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `transaction_records` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `amount` REAL NOT NULL,
                        `category` TEXT NOT NULL,
                        `date` INTEGER NOT NULL,
                        `type` TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `budget_goals` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `category` TEXT NOT NULL,
                        `targetAmount` REAL NOT NULL,
                        `currentProgress` REAL NOT NULL,
                        `deadline` TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `fraud_alerts` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `description` TEXT NOT NULL,
                        `severityLevel` TEXT NOT NULL,
                        `timestamp` INTEGER NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        val MIGRATION_5_6 = object : Migration(5, 6) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE IF NOT EXISTS `recurring_transactions` (
                        `id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        `title` TEXT NOT NULL,
                        `payee` TEXT NOT NULL,
                        `amount` REAL NOT NULL,
                        `category` TEXT NOT NULL,
                        `frequency` TEXT NOT NULL,
                        `intervalMillis` INTEGER NOT NULL,
                        `nextDueTimestamp` INTEGER NOT NULL,
                        `lastExecutedTimestamp` INTEGER,
                        `accountId` INTEGER NOT NULL,
                        `accountName` TEXT NOT NULL,
                        `isActive` INTEGER NOT NULL,
                        `executionCount` INTEGER NOT NULL,
                        `note` TEXT NOT NULL
                    )
                    """.trimIndent()
                )
            }
        }

        fun getDatabase(context: Context): BankDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BankDatabase::class.java,
                    DATABASE_NAME
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4, MIGRATION_4_5, MIGRATION_5_6)
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
