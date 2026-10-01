package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.TransactionCategory
import com.example.service.CategoryThresholdEngine
import com.example.service.ThresholdStatus
import com.example.ui.theme.*
import com.example.ui.viewmodel.BankViewModel

/**
 * Dialog allowing users to configure custom monthly spending thresholds per category.
 * Displays real-time progress against 80% warning and 100% exceeded thresholds,
 * and allows immediate testing of local notifications.
 */
@Composable
fun CategorySpendingThresholdDialog(
    viewModel: BankViewModel,
    initialCategory: TransactionCategory? = null,
    onDismiss: () -> Unit
) {
    val budgets by viewModel.budgets.collectAsStateWithLifecycle()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

    var selectedCategory by remember {
        mutableStateOf(initialCategory ?: TransactionCategory.FOOD)
    }

    val currentBudget = budgets.find { it.category == selectedCategory }
    val currentLimit = currentBudget?.monthlyLimit ?: 800.0
    val currentSpent = currentBudget?.currentSpent ?: 0.0

    var limitInput by remember(selectedCategory, currentLimit) {
        mutableStateOf("%.0f".format(currentLimit))
    }

    val parsedLimit = limitInput.toDoubleOrNull() ?: currentLimit
    val ratio = if (parsedLimit > 0.0) (currentSpent / parsedLimit).toFloat() else 0f
    val percentInt = (ratio * 100f).toInt()

    val statusColor = when {
        ratio >= 1.0f -> CrimsonError
        ratio >= 0.80f -> AmberWarning
        else -> EmeraldSuccess
    }

    val statusText = when {
        ratio >= 1.0f -> "🚨 100% Exceeded ($percentInt%)"
        ratio >= 0.80f -> "⚠️ 80% Threshold Warning ($percentInt%)"
        else -> "✓ Within Safe Budget ($percentInt%)"
    }

    val dialogBg = if (isDarkMode) NavyCard else MaterialTheme.colorScheme.surface
    val surfaceVariant = if (isDarkMode) Navy900 else MaterialTheme.colorScheme.surfaceVariant
    val textPrimary = if (isDarkMode) TextWhite else MaterialTheme.colorScheme.onSurface
    val textSecondary = if (isDarkMode) TextMuted else MaterialTheme.colorScheme.onSurfaceVariant

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("category_threshold_dialog"),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = dialogBg),
            elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(statusColor.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.NotificationsActive,
                                contentDescription = null,
                                tint = statusColor,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Monthly Spending Limits",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Text(
                                text = "Trigger local alerts at 80% & 100%",
                                style = MaterialTheme.typography.bodySmall,
                                color = textSecondary
                            )
                        }
                    }

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Close",
                            tint = textSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Category selector chips
                Text(
                    text = "Select Category to Configure",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = textSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                val categories = listOf(
                    TransactionCategory.FOOD,
                    TransactionCategory.SHOPPING,
                    TransactionCategory.BILLS,
                    TransactionCategory.ENTERTAINMENT,
                    TransactionCategory.HEALTHCARE,
                    TransactionCategory.EDUCATION,
                    TransactionCategory.OTHER
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Scrollable or wrapping chips
                    Box(modifier = Modifier.weight(1f)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            categories.take(4).forEach { cat ->
                                val isSelected = cat == selectedCategory
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedCategory = cat },
                                    label = {
                                        Text(
                                            cat.name.take(4),
                                            fontSize = 12.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = ElectricBlue,
                                        selectedLabelColor = Color.White
                                    ),
                                    modifier = Modifier.testTag("chip_category_${cat.name}")
                                )
                            }
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    categories.drop(4).forEach { cat ->
                        val isSelected = cat == selectedCategory
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedCategory = cat },
                            label = {
                                Text(
                                    cat.name.take(6),
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = ElectricBlue,
                                selectedLabelColor = Color.White
                            ),
                            modifier = Modifier.testTag("chip_category_${cat.name}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Current Category Card
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = surfaceVariant)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = CategoryThresholdEngine.formatCategoryName(selectedCategory),
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = statusColor.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = statusText,
                                    color = statusColor,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Amount stats
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("Current Spent", fontSize = 11.sp, color = textSecondary)
                                Text(
                                    text = "$${"%,.2f".format(currentSpent)}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textPrimary
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text("Monthly Limit", fontSize = 11.sp, color = textSecondary)
                                Text(
                                    text = "$${"%,.2f".format(parsedLimit)}",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = ElectricBlue
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Visual progress bar with 80% marker
                        Box(modifier = Modifier.fillMaxWidth()) {
                            // Progress bar
                            LinearProgressIndicator(
                                progress = { ratio.coerceIn(0f, 1f) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(10.dp)
                                    .clip(CircleShape),
                                color = statusColor,
                                trackColor = if (isDarkMode) Navy800 else Color.LightGray.copy(alpha = 0.5f)
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("0%", fontSize = 10.sp, color = textSecondary)
                            Text("⚠️ 80% Warning", fontSize = 10.sp, color = AmberWarning, fontWeight = FontWeight.Bold)
                            Text("🚨 100% Limit", fontSize = 10.sp, color = CrimsonError, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Custom Limit Input Field
                OutlinedTextField(
                    value = limitInput,
                    onValueChange = { limitInput = it },
                    label = { Text("Set Monthly Limit ($)") },
                    leadingIcon = {
                        Text(
                            "$",
                            fontWeight = FontWeight.Bold,
                            color = textSecondary,
                            modifier = Modifier.padding(start = 12.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_category_limit")
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quick presets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    listOf(500.0, 800.0, 1200.0, 2000.0).forEach { preset ->
                        OutlinedButton(
                            onClick = { limitInput = "%.0f".format(preset) },
                            modifier = Modifier.weight(1f),
                            contentPadding = PaddingValues(horizontal = 4.dp, vertical = 6.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("$$preset", fontSize = 11.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Save button
                Button(
                    onClick = {
                        val limitToSave = limitInput.toDoubleOrNull() ?: currentLimit
                        if (limitToSave > 0.0) {
                            viewModel.setCategorySpendingLimit(selectedCategory, limitToSave)
                            onDismiss()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_save_category_limit"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue)
                ) {
                    Icon(imageVector = Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Save Limit & Arm Notifications", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Test notification buttons
                Text(
                    text = "Verify System Notifications",
                    style = MaterialTheme.typography.labelSmall,
                    color = textSecondary,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedButton(
                        onClick = {
                            viewModel.simulateCategoryThresholdAlert(selectedCategory, isExceeded = false)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_test_80_alert"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = AmberWarning)
                    ) {
                        Text("Test 80% Warning", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            viewModel.simulateCategoryThresholdAlert(selectedCategory, isExceeded = true)
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("btn_test_100_alert"),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonError)
                    ) {
                        Text("Test 100% Exceeded", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
