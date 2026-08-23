package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AccountEntity
import com.example.data.model.BudgetEntity
import com.example.data.model.SavingsGoalEntity
import com.example.data.model.TransactionCategory
import com.example.data.model.TransactionEntity
import com.example.data.model.TransactionType
import com.example.ui.components.ExportReportDialog
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.CrimsonDanger
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyCardLight
import com.example.ui.theme.PurpleTech
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.BankViewModel

@Composable
fun AnalyticsScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val budgets by viewModel.budgets.collectAsStateWithLifecycle()
    val savingsGoals by viewModel.savingsGoals.collectAsStateWithLifecycle()
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()

    var showExportModal by remember { mutableStateOf(false) }
    var showAddGoalModal by remember { mutableStateOf(false) }
    var selectedGoalForDeposit by remember { mutableStateOf<SavingsGoalEntity?>(null) }

    val categoryColors = mapOf(
        TransactionCategory.FOOD to Color(0xFFF59E0B),
        TransactionCategory.SHOPPING to Color(0xFFEC4899),
        TransactionCategory.BILLS to Color(0xFF3B82F6),
        TransactionCategory.TRANSFER to Color(0xFF8B5CF6),
        TransactionCategory.ENTERTAINMENT to Color(0xFF10B981),
        TransactionCategory.HEALTHCARE to Color(0xFFEF4444),
        TransactionCategory.EDUCATION to Color(0xFF06B6D4)
    )

    val totalBudgetLimit = remember(budgets) { budgets.sumOf { it.monthlyLimit } }
    val totalBudgetSpent = remember(budgets) { budgets.sumOf { it.currentSpent } }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "FINANCIAL INTELLIGENCE & BUDGETING",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "Analytics & Goal Vaults",
                        color = TextWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = { showExportModal = true },
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(CyberCyan.copy(alpha = 0.15f))
                        .testTag("export_statement_btn")
                ) {
                    Icon(imageVector = Icons.Default.Download, contentDescription = "Export CSV", tint = CyberCyan)
                }
            }
        }

        // --- 1. Monthly Budget Health Card ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(18.dp)
            ) {
                Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("MONTHLY BUDGET SPENT", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "$${"%,.2f".format(totalBudgetSpent)} / $${"%,.2f".format(totalBudgetLimit)}",
                                color = TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp
                            )
                        }
                        Surface(
                            color = if (totalBudgetSpent > totalBudgetLimit) CrimsonDanger.copy(alpha = 0.2f) else EmeraldSuccess.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(
                                text = "${((totalBudgetSpent / totalBudgetLimit.coerceAtLeast(1.0)) * 100).toInt()}% Used",
                                color = if (totalBudgetSpent > totalBudgetLimit) CrimsonDanger else EmeraldSuccess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    LinearProgressIndicator(
                        progress = { (totalBudgetSpent / totalBudgetLimit.coerceAtLeast(1.0)).toFloat().coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = if (totalBudgetSpent > totalBudgetLimit) CrimsonDanger else CyberCyan,
                        trackColor = Navy900
                    )
                }
            }
        }

        // --- 2. Category Budget Breakdown ---
        item {
            Text("CATEGORY ALLOCATIONS & LIMITS", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
        }

        items(budgets, key = { it.id }) { budget ->
            val color = categoryColors[budget.category] ?: CyberCyan
            val isOverspent = budget.currentSpent > budget.monthlyLimit

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Box(modifier = Modifier.size(12.dp).clip(CircleShape).background(color))
                            Text(budget.category.name, color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                        }

                        Text(
                            text = "$${"%,.0f".format(budget.currentSpent)} of $${"%,.0f".format(budget.monthlyLimit)}",
                            color = if (isOverspent) CrimsonDanger else TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }

                    LinearProgressIndicator(
                        progress = { (budget.currentSpent / budget.monthlyLimit).toFloat().coerceIn(0f, 1f) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = if (isOverspent) CrimsonDanger else color,
                        trackColor = Navy900
                    )

                    if (isOverspent) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = CrimsonDanger, modifier = Modifier.size(14.dp))
                            Text("Over budget by $${"%.2f".format(budget.currentSpent - budget.monthlyLimit)}", color = CrimsonDanger, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // --- 3. Smart Cash Flow Prediction Card ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCardLight),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(CyberCyan.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.TrendingUp, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(24.dp))
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text("AI CASH FLOW FORECAST", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Text("Projected Net Savings: +$740.00", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Based on 30-day recurring velocity, you will comfortably meet your savings goals.", color = TextMuted, fontSize = 11.sp)
                    }
                }
            }
        }

        // --- 4. Savings Goals Section ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("GOAL SAVINGS VAULTS", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
                TextButton(
                    onClick = { showAddGoalModal = true },
                    modifier = Modifier.testTag("add_goal_btn")
                ) {
                    Icon(imageVector = Icons.Default.Add, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.size(4.dp))
                    Text("New Goal", color = CyberCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        items(savingsGoals, key = { it.id }) { goal ->
            val progress = (goal.currentAmount / goal.targetAmount).toFloat().coerceIn(0f, 1f)
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(14.dp)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldSuccess.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(imageVector = Icons.Default.Savings, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(20.dp))
                            }
                            Column {
                                Text(goal.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Text("Target: ${goal.targetDate}", color = TextMuted, fontSize = 11.sp)
                            }
                        }

                        Button(
                            onClick = { selectedGoalForDeposit = goal },
                            colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp).testTag("contribute_goal_${goal.id}")
                        ) {
                            Text("+ Add Funds", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }

                    LinearProgressIndicator(
                        progress = { progress },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = EmeraldSuccess,
                        trackColor = Navy900
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("$${"%,.2f".format(goal.currentAmount)} saved", color = EmeraldSuccess, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        Text("$${"%,.2f".format(goal.targetAmount)} target (${(progress * 100).toInt()}%)", color = TextMuted, fontSize = 12.sp)
                    }
                }
            }
        }
    }

    // --- Add Goal Modal ---
    if (showAddGoalModal) {
        AddGoalModal(
            onDismiss = { showAddGoalModal = false },
            onAdd = { title, target, date ->
                viewModel.addSavingsGoal(title, target, date, "SAVINGS")
                showAddGoalModal = false
            }
        )
    }

    // --- Contribute to Goal Modal ---
    selectedGoalForDeposit?.let { goal ->
        ContributeGoalModal(
            goal = goal,
            accounts = accounts,
            onDismiss = { selectedGoalForDeposit = null },
            onContribute = { amount, accId ->
                viewModel.contributeToGoal(goal.id, amount, accId)
                selectedGoalForDeposit = null
            }
        )
    }

    // --- Export Statement Modal ---
    ExportReportDialog(
        isOpen = showExportModal,
        reportContent = viewModel.exportStatementSummary(),
        onDismiss = { showExportModal = false }
    )
}

@Composable
private fun AddGoalModal(
    onDismiss: () -> Unit,
    onAdd: (String, Double, String) -> Unit
) {
    var title by remember { mutableStateOf("") }
    var targetText by remember { mutableStateOf("2000.00") }
    var dateText by remember { mutableStateOf("Dec 2026") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(imageVector = Icons.Default.Savings, contentDescription = null, tint = EmeraldSuccess)
                Text("Create Savings Vault Goal", color = TextWhite, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Goal Name (e.g. New Laptop)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("goal_title_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldSuccess,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )

                OutlinedTextField(
                    value = targetText,
                    onValueChange = { targetText = it },
                    label = { Text("Target Amount ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("goal_target_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldSuccess,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )

                OutlinedTextField(
                    value = dateText,
                    onValueChange = { dateText = it },
                    label = { Text("Target Completion Date") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldSuccess,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val target = targetText.toDoubleOrNull() ?: 0.0
                    if (title.isNotBlank() && target > 0) {
                        onAdd(title, target, dateText)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                modifier = Modifier.testTag("save_goal_btn")
            ) {
                Text("Create Goal", color = Navy900, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextMuted) }
        },
        containerColor = Navy800
    )
}

@Composable
private fun ContributeGoalModal(
    goal: SavingsGoalEntity,
    accounts: List<AccountEntity>,
    onDismiss: () -> Unit,
    onContribute: (Double, Long) -> Unit
) {
    var amountText by remember { mutableStateOf("100.00") }
    var selectedAccountId by remember { mutableStateOf(accounts.firstOrNull()?.id ?: 1L) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("Deposit to ${goal.title}", color = TextWhite, fontWeight = FontWeight.Bold)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Move liquid funds into this secured savings goal lock:", color = TextMuted, fontSize = 12.sp)

                OutlinedTextField(
                    value = amountText,
                    onValueChange = { amountText = it },
                    label = { Text("Contribution Amount ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("contribute_amount_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldSuccess,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amt = amountText.toDoubleOrNull() ?: 0.0
                    if (amt > 0) onContribute(amt, selectedAccountId)
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                modifier = Modifier.testTag("submit_contribute_btn")
            ) {
                Text("Transfer to Goal", color = Navy900, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel", color = TextMuted) }
        },
        containerColor = Navy800
    )
}
