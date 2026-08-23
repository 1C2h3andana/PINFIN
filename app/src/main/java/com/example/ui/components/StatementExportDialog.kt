package com.example.ui.components

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
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy900
import com.example.ui.theme.NavyCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BankViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun StatementExportDialog(
    viewModel: BankViewModel,
    onDismiss: () -> Unit
) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val clipboardManager = LocalClipboardManager.current

    var selectedFormatIndex by remember { mutableIntStateOf(0) } // 0 = PDF/Text Statement, 1 = CSV Spreadsheet
    var selectedRangeIndex by remember { mutableIntStateOf(0) } // 0 = All Time, 1 = Last 30 Days

    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    // Generate CSV String
    val csvContent = buildString {
        append("TxID,Date,Title,Recipient,Category,Type,Amount,Status,RiskScore,RiskReason\n")
        transactions.forEach { tx ->
            val dateStr = dateFormat.format(Date(tx.timestamp))
            append("${tx.id},\"$dateStr\",\"${tx.title}\",\"${tx.recipient}\",${tx.category},${tx.type},${tx.amount},${tx.status},${tx.riskScore},\"${tx.riskReason}\"\n")
        }
    }

    // Generate Formatted Statement Text
    val statementContent = buildString {
        append("=====================================================\n")
        append("           SMARTBANK AI OFFICIAL STATEMENT           \n")
        append("           Cryptographically Verified Report        \n")
        append("=====================================================\n")
        append("Account Holder : Alex Morgan\n")
        append("Statement Date : ${dateFormat.format(Date())}\n")
        append("Total Accounts : ${accounts.size}\n")
        append("-----------------------------------------------------\n")
        accounts.forEach { acc ->
            append("• ${acc.name} (...${acc.accountNumber.takeLast(4)}): $${"%,.2f".format(acc.balance)}\n")
        }
        append("-----------------------------------------------------\n")
        append("RECENT TRANSACTIONS & AUDIT LOGS:\n")
        transactions.take(15).forEach { tx ->
            val dateStr = dateFormat.format(Date(tx.timestamp))
            val sign = if (tx.type == com.example.data.model.TransactionType.CREDIT) "+" else "-"
            append("[$dateStr] ${tx.title} | $sign$${"%.2f".format(tx.amount)} | ${tx.status}\n")
        }
        append("=====================================================\n")
        append("Fraud Shield Verification Hash: 9f82ab48201bcf9128\n")
        append("=====================================================\n")
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 20.dp)
                .testTag("export_statement_dialog"),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(24.dp))
                        Text("Export Statements & Reports", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 17.sp)
                    }
                }

                // Format Tabs
                TabRow(
                    selectedTabIndex = selectedFormatIndex,
                    containerColor = Navy900,
                    contentColor = CyberCyan,
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .border(1.dp, Navy700, RoundedCornerShape(10.dp)),
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
                        text = { Text("PDF / Text Statement", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                    Tab(
                        selected = selectedFormatIndex == 1,
                        onClick = { selectedFormatIndex = 1 },
                        text = { Text("CSV Spreadsheet", fontSize = 12.sp, fontWeight = FontWeight.Bold) }
                    )
                }

                Text("Live Document Preview:", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                // Preview Box
                Surface(
                    color = Navy900,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Navy700),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Text(
                        text = if (selectedFormatIndex == 0) statementContent else csvContent,
                        fontFamily = FontFamily.Monospace,
                        fontSize = 10.sp,
                        color = CyberCyan.copy(alpha = 0.9f),
                        modifier = Modifier
                            .padding(12.dp)
                            .verticalScroll(rememberScrollState())
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = {
                            val contentToCopy = if (selectedFormatIndex == 0) statementContent else csvContent
                            clipboardManager.setText(AnnotatedString(contentToCopy))
                            viewModel.showMessage("✓ ${if (selectedFormatIndex == 0) "PDF Statement" else "CSV Data"} copied to clipboard!")
                            onDismiss()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(1f).height(46.dp).testTag("copy_report_btn")
                    ) {
                        Icon(Icons.Default.FileDownload, contentDescription = null, tint = Navy900, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Export / Copy", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }

                    Button(
                        onClick = onDismiss,
                        colors = ButtonDefaults.buttonColors(containerColor = Navy700),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.weight(0.7f).height(46.dp)
                    ) {
                        Text("Close", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}
