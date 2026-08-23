package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.BankDao
import com.example.data.model.AccountEntity
import com.example.data.model.ApplicationFormEntity
import com.example.data.model.BillEntity
import com.example.data.model.BudgetEntity
import com.example.data.model.InsuranceClaimEntity
import com.example.data.model.InsurancePolicyEntity
import com.example.data.model.InvestmentEntity
import com.example.data.model.NotificationEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.SecurityLogEntity
import com.example.data.model.SupportTicketEntity
import com.example.data.model.TransactionEntity
import com.example.data.model.UserEntity

@Database(
    entities = [
        UserEntity::class,
        AccountEntity::class,
        TransactionEntity::class,
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
    version = 2,
    exportSchema = false
)
abstract class BankDatabase : RoomDatabase() {
    abstract fun bankDao(): BankDao

    companion object {
        @Volatile
        private var INSTANCE: BankDatabase? = null

        fun getDatabase(context: Context): BankDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    BankDatabase::class.java,
                    "smart_bank_ai.db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
