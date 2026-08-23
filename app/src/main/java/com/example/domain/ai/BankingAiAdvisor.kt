package com.example.domain.ai

import com.example.data.model.AccountEntity
import com.example.data.model.BudgetEntity
import com.example.data.model.TransactionEntity

object BankingAiAdvisor {

    data class AiChatMessage(
        val isUser: Boolean,
        val text: String,
        val timestamp: Long = System.currentTimeMillis(),
        val actionTag: String? = null
    )

    val PRESET_PROMPTS = listOf(
        "💡 Analyze my monthly spending",
        "🛡️ How do I spot UPI & QR code scams?",
        "📈 Best strategy to improve my credit score?",
        "💰 How much can I safely save this month?",
        "⚠️ Dispute an unauthorized transaction",
        "🎓 Student budget & money saving hacks"
    )

    fun generateResponse(
        prompt: String,
        accounts: List<AccountEntity>,
        budgets: List<BudgetEntity> = emptyList()
    ): String = generateResponse(prompt, accounts, emptyList(), budgets)

    fun generateResponse(
        prompt: String,
        accounts: List<AccountEntity>,
        transactions: List<TransactionEntity>,
        budgets: List<BudgetEntity>
    ): String {
        val lower = prompt.lowercase()

        val totalBalance = accounts.sumOf { it.balance }
        val recentDebits = transactions.filter { it.type == com.example.data.model.TransactionType.DEBIT }
        val totalSpent = recentDebits.sumOf { it.amount }
        val highRiskCount = transactions.count { it.riskScore >= 40 }

        return when {
            lower.contains("analyze") || lower.contains("spending") || lower.contains("budget") -> {
                val topCategory = recentDebits.groupBy { it.category }
                    .maxByOrNull { entry -> entry.value.sumOf { it.amount } }?.key?.name ?: "GENERAL"
                "📊 **Smart Spending Analysis**\n\n" +
                        "• Total Available Liquidity: **$${"%,.2f".format(totalBalance)}**\n" +
                        "• Recent Outflows: **$${"%,.2f".format(totalSpent)}** across ${recentDebits.size} transactions\n" +
                        "• Highest Expense Area: **$topCategory**\n\n" +
                        "💡 **AI Recommendation**: You are on track! We noticed $topCategory represents your primary expense. Setting a strict weekly cap can help allocate an extra $150 to your Emergency Fund goal."
            }

            lower.contains("scam") || lower.contains("upi") || lower.contains("phishing") || lower.contains("fraud") -> {
                "🛡️ **AI Cyber Defense Guide: Spotting Scams**\n\n" +
                        "1. **UPI PIN Rule**: You NEVER enter your UPI PIN to *receive* money, only to *send* money.\n" +
                        "2. **Fake KYC Alerts**: Banks never send SMS links with `.xyz` or `.top` domains asking for PAN or Aadhaar update.\n" +
                        "3. **Screen Sharing Apps**: Never install apps like AnyDesk or TeamViewer if requested by someone claiming to be bank support.\n" +
                        "4. **SmartBank Safety**: Use our built-in **Link/UPI Scanner** in the Security Center before approving unknown transfers."
            }

            lower.contains("credit") || lower.contains("score") || lower.contains("cibil") -> {
                "📈 **Proven Credit Score Optimization Plan**\n\n" +
                        "• **Keep Utilization < 30%**: If your credit limit is $5,000, keep revolving balance under $1,500.\n" +
                        "• **Automate Payments**: Enable Auto-Pay for minimum amounts on bills to avoid 30+ day delinquencies.\n" +
                        "• **Credit Age**: Avoid closing your oldest credit card account as it shortens your credit history.\n" +
                        "• **Limit Hard Inquiries**: Space out loan applications by at least 6 months."
            }

            lower.contains("save") || lower.contains("saving") || lower.contains("invest") -> {
                val suggestedSavings = (totalBalance * 0.15).coerceAtLeast(100.0)
                "💰 **Smart Savings Assessment**\n\n" +
                        "Based on your current cash reserves of **$${"%,.2f".format(totalBalance)}**, your recommended target monthly buffer is **$${"%,.2f".format(suggestedSavings)}**.\n\n" +
                        "• Automate a recurring deposit to your *High-Yield Savings* on payday (1st of every month).\n" +
                        "• Use round-up savings for micro-investments.\n" +
                        "• Keep at least 3 months of baseline expenses ($4,500) in an emergency liquid vault."
            }

            lower.contains("dispute") || lower.contains("unauthorized") || lower.contains("refund") -> {
                "⚠️ **Emergency Transaction Dispute Protocol**\n\n" +
                        "1. **Instant Freeze**: Tap 'Cards' on the home dashboard and toggle 'Freeze Card' immediately to stop further charges.\n" +
                        "2. **Mark Flagged**: Locate the transaction in your Transaction History and tap 'Flag as Fraud'.\n" +
                        "3. **Zero Liability Protection**: Under banking compliance regulations, unauthorized electronic transactions reported within 72 hours are covered by full reimbursement.\n" +
                        "4. A formal security incident ticket has been prepared for dispatch."
            }

            lower.contains("student") || lower.contains("college") || lower.contains("hack") -> {
                "🎓 **Student Financial Freedom Playbook**\n\n" +
                        "• **The 50/30/20 Student Rule**: 50% Essentials (tuition, books, rent), 30% Lifestyle (food out, movies), 20% Savings (emergency stash).\n" +
                        "• **Split Expenses**: Use the SmartBank Split Calculator to manage roommate bills and shared groceries without awkward reminders.\n" +
                        "• **Avoid High-Interest 'Buy Now Pay Later'**: Pay balances in full before the 30-day billing cycle ends.\n" +
                        "• **Student Banking Mode**: Enable 'Student Budget Limit' in our Hub to receive warnings when you exceed daily $30 discretionary spending."
            }

            else -> {
                "🤖 **SmartBank AI Assistant**\n\n" +
                        "I analyzed your account profile. You currently have **${accounts.size} active accounts** with **$${"%,.2f".format(totalBalance)}** total balance and **$highRiskCount security flags**.\n\n" +
                        "How can I assist you further today? You can ask me to calculate loan EMIs, scan a suspicious link, forecast next month's cash flow, or automate your utility payments!"
            }
        }
    }
}
