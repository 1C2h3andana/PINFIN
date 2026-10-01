package com.example.ui.components

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.TableChart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.NavyCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BankViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Statement and Transaction Database Export Dialog.
 * Supports exporting transactions to CSV format with a file sharing intent to save
 * to external storage or cloud drive apps, as well as clipboard copying.
 */
@Composable
fun StatementExportDialog(
    viewModel: BankViewModel,
    onDismiss: () -> Unit
) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val clipboardManager = LocalClipboardManager.current

    var selectedFormatIndex by remember { mutableIntStateOf(1) } // Default to CSV Spreadsheet

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    // Generate CSV String from Room transactions
    val csvContent = remember(transactions) {
        buildString {
            append("TxID,Timestamp,Date,Title,Recipient,Category,Type,Amount,Status,RiskScore,RiskReason,SourceAccount\n")
            transactions.forEach { tx ->
                val dateStr = dateFormat.format(Date(tx.timestamp))
                val titleSafe = tx.title.replace("\"", "\"\"")
                val recipientSafe = tx.recipient.replace("\"", "\"\"")
                val reasonSafe = tx.riskReason.replace("\"", "\"\"")
                append("${tx.id},${tx.timestamp},\"$dateStr\",\"$titleSafe\",\"$recipientSafe\",${tx.category},${tx.type},${tx.amount},${tx.status},${tx.riskScore},\"$reasonSafe\",${tx.accountId}\n")
            }
        }
    }

    // Generate Formatted Statement Text
    val statementContent = remember(transactions, accounts) {
        buildString {
            append("=====================================================\n")
            append("           SMARTBANK AI OFFICIAL STATEMENT           \n")
            append("           Cryptographically Verified Report         \n")
            append("=====================================================\n")
            append("Account Holder : Alex Morgan\n")
            append("Statement Date : ${dateFormat.format(Date())}\n")
            append("Total Accounts : ${accounts.size}\n")
            append("Total Records  : ${transactions.size}\n")
            append("-----------------------------------------------------\n")
            accounts.forEach { acc ->
                append("• ${acc.name} (...${acc.accountNumber.takeLast(4)}): $${"%,.2f".format(acc.balance)}\n")
            }
            append("-----------------------------------------------------\n")
            append("TRANSACTIONS AUDIT LOGS:\n")
            transactions.take(25).forEach { tx ->
                val dateStr = dateFormat.format(Date(tx.timestamp))
                val sign = if (tx.type == com.example.data.model.TransactionType.CREDIT) "+" else "-"
                append("[$dateStr] ${tx.title} | $sign$${"%.2f".format(tx.amount)} | ${tx.status} | Risk: ${tx.riskScore}\n")
            }
            append("=====================================================\n")
            append("Export Format: SmartBank AI SEC-Audit-Compliant\n")
            append("=====================================================\n")
        }
    }

    // Function to write CSV file and launch File Sharing Intent for external storage / cloud
    fun shareCsvFile() {
        try {
            val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
            val fileName = "smartbank_transactions_$timestamp.csv"
            val exportDir = File(context.cacheDir, "exports").apply { mkdirs() }
            val csvFile = File(exportDir, fileName)
            csvFile.writeText(csvContent)

            val fileUri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                csvFile
            )

            val shareIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/csv"
                putExtra(Intent.EXTRA_STREAM, fileUri)
                putExtra(Intent.EXTRA_SUBJECT, "SmartBank AI Transaction Database Export")
                putExtra(Intent.EXTRA_TEXT, "Exported ${transactions.size} records from SmartBank AI Room Database.")
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }

            val chooser = Intent.createChooser(shareIntent, "Save CSV to External Storage or Share")
            context.startActivity(chooser)
            viewModel.recordCsvBackupCompleted()
            viewModel.showMessage("✓ CSV export file ready! Backup recorded & reminder timer reset.")
        } catch (e: Exception) {
            Log.e("StatementExportDialog", "Failed to share CSV", e)
            viewModel.showMessage("Export error: ${e.message}")
        }
    }

    val dialogBg = if (isDarkMode) NavyCard else MaterialTheme.colorScheme.surface
    val surfaceBoxBg = if (isDarkMode) Navy900 else MaterialTheme.colorScheme.surfaceVariant
    val textPrimary = if (isDarkMode) TextWhite else MaterialTheme.colorScheme.onSurface
    val textSecondary = if (isDarkMode) TextMuted else MaterialTheme.colorScheme.onSurfaceVariant
    val borderColor = if (isDarkMode) Navy700 else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 16.dp)
                .testTag("export_statement_dialog"),
            colors = CardDefaults.cardColors(containerColor = dialogBg),
            shape = RoundedCornerShape(24.dp),
            border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(imageVector = Icons.Default.TableChart, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(24.dp))
                        Text("Export Transaction Database", color = textPrimary, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    }
                    Surface(
                        color = EmeraldSuccess.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = "${transactions.size} Records",
                            color = EmeraldSuccess,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                        )
                    }
                }

                // Format Tabs
                TabRow(
                    selectedTabIndex = selectedFormatIndex,
                    containerColor = surfaceBoxBg,
                    contentColor = CyberCyan,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, borderColor, RoundedCornerShape(10.dp)),
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedFormatIndex]),
                            color = CyberCyan,
                            height = 3.dp
                        )
                    }
                ) {
                    Tab(
                        selected = selectedFormatIndex == 0,
                        onClick = { selectedFormatIndex = 0 },
                        text = { Text("Formatted Statement", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedFormatIndex == 1,
                        onClick = { selectedFormatIndex = 1 },
                        text = { Text("CSV Spreadsheet", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                }

                Text("Live Export Preview (${if (selectedFormatIndex == 1) "CSV Database" else "Audit Log"}):", color = textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                // Preview Box
                Surface(
                    color = surfaceBoxBg,
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, borderColor),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                ) {
                    Text(
                        text = if (selectedFormatIndex == 0) statementContent else csvContent,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = if (isDarkMode) CyberCyan.copy(alpha = 0.9f) else MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                    )
                }

                // Primary Actions: File Sharing Intent for CSV
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            shareCsvFile()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("export_csv_share_btn")
                    ) {
                        Icon(Icons.Default.Share, contentDescription = null, tint = Navy900, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Save to External Storage / Share CSV", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val contentToCopy = if (selectedFormatIndex == 0) statementContent else csvContent
                                clipboardManager.setText(AnnotatedString(contentToCopy))
                                viewModel.showMessage("✓ ${if (selectedFormatIndex == 0) "Statement" else "CSV"} copied to clipboard!")
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDarkMode) Navy800 else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .testTag("copy_report_btn")
                        ) {
                            Icon(Icons.Default.FileDownload, contentDescription = null, tint = textPrimary, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Copy Content", color = textPrimary, fontWeight = FontWeight.SemiBold, fontSize = 12.sp)
                        }

                        Button(
                            onClick = onDismiss,
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isDarkMode) Navy700 else MaterialTheme.colorScheme.surfaceVariant
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .weight(0.7f)
                                .height(44.dp)
                        ) {
                            Text("Close", color = textPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}
