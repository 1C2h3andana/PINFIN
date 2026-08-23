package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AccountEntity
import com.example.data.model.RiskLevel
import com.example.data.model.TransactionCategory
import com.example.ui.components.OtpVerificationDialog
import com.example.ui.components.RiskBadge
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.CrimsonDanger
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyCardLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.BankViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransferScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val transferState by viewModel.transferState.collectAsStateWithLifecycle()
    val is2FaEnabled by viewModel.is2FaGloballyEnabled.collectAsStateWithLifecycle()

    val frequentContacts = listOf(
        Pair("Sarah Jenkins", "sarah.j@okhdfcbank"),
        Pair("Amazon AWS", "billing@aws.amazon.com"),
        Pair("Whole Foods", "pos.wf829@amex"),
        Pair("Campus Tuition", "tuition.finance@sit.edu")
    )

    val selectedAccount = accounts.find { it.id == transferState.selectedAccountId } ?: accounts.firstOrNull()

    var categoryDropdownExpanded by remember { mutableStateOf(false) }
    var accountDropdownExpanded by remember { mutableStateOf(false) }

    if (transferState.isTransferSuccess && transferState.lastCompletedTx != null) {
        // --- Success Receipt View ---
        TransferReceiptView(
            tx = transferState.lastCompletedTx!!,
            onDone = { viewModel.resetTransferForm() }
        )
        return
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        item {
            Text(
                text = "SECURE MONEY TRANSFER",
                color = CyberCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "Real-Time AI-Protected P2P & Wire",
                color = TextWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // --- 1. Funding Account Selector ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("DEBIT FROM ACCOUNT", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)

                    ExposedDropdownMenuBox(
                        expanded = accountDropdownExpanded,
                        onExpandedChange = { accountDropdownExpanded = !accountDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = "${selectedAccount?.name ?: "Select Account"} — $${"%,.2f".format(selectedAccount?.balance ?: 0.0)}",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = accountDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                                .testTag("select_account_dropdown"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = Navy700,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = accountDropdownExpanded,
                            onDismissRequest = { accountDropdownExpanded = false },
                            modifier = Modifier.background(Navy800)
                        ) {
                            accounts.forEach { acc ->
                                DropdownMenuItem(
                                    text = {
                                        Column {
                                            Text(acc.name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                            Text("Balance: $${"%,.2f".format(acc.balance)} • ${acc.accountNumber}", color = TextMuted, fontSize = 11.sp)
                                        }
                                    },
                                    onClick = {
                                        viewModel.updateTransferAccount(acc.id)
                                        accountDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 2. Frequent Beneficiary Chips ---
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("FREQUENT BENEFICIARIES", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(frequentContacts) { contact ->
                        Surface(
                            color = NavyCard,
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Navy700),
                            modifier = Modifier.clickable {
                                viewModel.updateTransferRecipient(contact.first)
                                viewModel.updateTransferUpi(contact.second)
                            }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(24.dp)
                                        .clip(CircleShape)
                                        .background(CyberCyan.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(contact.first.take(1), color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                                Text(contact.first, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }
        }

        // --- 3. Recipient & Amount Form ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedTextField(
                        value = transferState.recipient,
                        onValueChange = { viewModel.updateTransferRecipient(it) },
                        label = { Text("Recipient Full Name / Organization") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("transfer_recipient_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    OutlinedTextField(
                        value = transferState.upiOrHandle,
                        onValueChange = { viewModel.updateTransferUpi(it) },
                        label = { Text("UPI ID / Account Number / IBAN (e.g. user@bank)") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("transfer_handle_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    OutlinedTextField(
                        value = transferState.amountText,
                        onValueChange = { viewModel.updateTransferAmount(it) },
                        label = { Text("Amount ($)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("transfer_amount_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    // Category Selector
                    ExposedDropdownMenuBox(
                        expanded = categoryDropdownExpanded,
                        onExpandedChange = { categoryDropdownExpanded = !categoryDropdownExpanded }
                    ) {
                        OutlinedTextField(
                            value = "Category: ${transferState.category.name}",
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = Navy700,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite
                            )
                        )

                        ExposedDropdownMenu(
                            expanded = categoryDropdownExpanded,
                            onDismissRequest = { categoryDropdownExpanded = false },
                            modifier = Modifier.background(Navy800)
                        ) {
                            TransactionCategory.values().forEach { cat ->
                                DropdownMenuItem(
                                    text = { Text(cat.name, color = TextWhite) },
                                    onClick = {
                                        viewModel.updateTransferCategory(cat)
                                        categoryDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = transferState.note,
                        onValueChange = { viewModel.updateTransferNote(it) },
                        label = { Text("Purpose / Memo (Optional)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )
                }
            }
        }

        // --- 4. REAL-TIME AI RISK MONITORING CARD ---
        item {
            val liveRisk = transferState.liveRisk
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = when {
                        liveRisk == null -> NavyCardLight
                        liveRisk.score >= 70 -> Color(0xFF450A0A)
                        liveRisk.score >= 40 -> Color(0xFF451A03)
                        else -> Color(0xFF064E3B).copy(alpha = 0.5f)
                    }
                ),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    when {
                        liveRisk == null -> Navy700
                        liveRisk.score >= 70 -> CrimsonDanger
                        liveRisk.score >= 40 -> AmberOrange
                        else -> EmeraldSuccess
                    }
                )
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = null,
                                tint = when {
                                    liveRisk == null -> CyberCyan
                                    liveRisk.score >= 70 -> CrimsonDanger
                                    liveRisk.score >= 40 -> AmberOrange
                                    else -> EmeraldSuccess
                                }
                            )
                            Text(
                                text = "REAL-TIME AI FRAUD RADAR",
                                color = TextWhite,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 12.sp,
                                letterSpacing = 1.sp
                            )
                        }

                        if (liveRisk != null) {
                            RiskBadge(level = liveRisk.level, score = liveRisk.score)
                        } else {
                            Text("Awaiting Input", color = TextMuted, fontSize = 11.sp)
                        }
                    }

                    if (liveRisk != null) {
                        LinearProgressIndicator(
                            progress = { liveRisk.score / 100f },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(6.dp)
                                .clip(RoundedCornerShape(3.dp)),
                            color = when {
                                liveRisk.score >= 70 -> CrimsonDanger
                                liveRisk.score >= 40 -> AmberOrange
                                else -> EmeraldSuccess
                            },
                            trackColor = Navy900
                        )

                        Text(
                            text = liveRisk.recommendation,
                            color = TextWhite,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )

                        if (liveRisk.flags.isNotEmpty()) {
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                liveRisk.flags.forEach { flag ->
                                    Text("• $flag", color = TextWhite.copy(alpha = 0.8f), fontSize = 11.sp)
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "SmartBank AI actively screens recipient handles, amounts, frequency, and scam databases as you type.",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }

        // --- Error Alert ---
        transferState.errorMessage?.let { error ->
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF7F1D1D)),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Warning, contentDescription = null, tint = TextWhite)
                        Text(error, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // --- Submit Action Button ---
        item {
            Button(
                onClick = { viewModel.initiateTransfer() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("submit_transfer_button"),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (transferState.liveRisk?.shouldBlock == true) CrimsonDanger else CyberCyan
                ),
                shape = RoundedCornerShape(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (transferState.liveRisk?.shouldBlock == true) Icons.Default.Lock else Icons.Default.Send,
                        contentDescription = null,
                        tint = Navy900
                    )
                    Text(
                        text = if (transferState.liveRisk?.shouldBlock == true) "Blocked by Security Shield"
                        else if (transferState.liveRisk?.requires2Fa == true || is2FaEnabled) "Authorize with 2FA OTP ($${transferState.amountText.ifBlank { "0.00" }})"
                        else "Send Money Instantly ($${transferState.amountText.ifBlank { "0.00" }})",
                        color = Navy900,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }

    // --- 2FA OTP Modal ---
    OtpVerificationDialog(
        isOpen = transferState.isOtpPromptOpen,
        enteredOtp = transferState.enteredOtp,
        onOtpChange = { viewModel.setEnteredOtp(it) },
        onConfirm = { viewModel.submitOtpAndCompleteTransfer() },
        onDismiss = { viewModel.resetTransferForm() },
        amount = transferState.amountText,
        risk = transferState.liveRisk
    )
}

@Composable
private fun TransferReceiptView(
    tx: com.example.data.model.TransactionEntity,
    onDone: () -> Unit
) {
    val clipboardManager = LocalClipboardManager.current
    val formattedDate = remember(tx.timestamp) {
        SimpleDateFormat("MMMM dd, yyyy - hh:mm:ss a", Locale.US).format(Date(tx.timestamp))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(EmeraldSuccess.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Success",
                tint = EmeraldSuccess,
                modifier = Modifier.size(44.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Transfer Authorized & Cleared",
            color = TextWhite,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Text(
            text = "$${"%,.2f".format(tx.amount)}",
            color = CyberCyan,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                ReceiptRow("Transaction ID", "#TX-${tx.id}-SMARTBANK")
                ReceiptRow("Recipient", tx.recipient)
                if (tx.upiOrHandle.isNotBlank()) {
                    ReceiptRow("VPA / Handle", tx.upiOrHandle)
                }
                ReceiptRow("Category", tx.category.name)
                ReceiptRow("Time", formattedDate)
                ReceiptRow("Security Clearance", "HSM 2FA Verified (Risk Score ${tx.riskScore}/100)")
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(
                onClick = {
                    clipboardManager.setText(AnnotatedString("SmartBank Transfer Receipt:\nTX #${tx.id}\nAmount: $${tx.amount}\nTo: ${tx.recipient}\nDate: $formattedDate"))
                },
                modifier = Modifier.weight(1f)
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = CyberCyan)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Copy Receipt", color = CyberCyan)
            }

            Button(
                onClick = onDone,
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                modifier = Modifier
                    .weight(1f)
                    .testTag("transfer_receipt_done_btn")
            ) {
                Text("Done", color = Navy900, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
private fun ReceiptRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, color = TextMuted, fontSize = 12.sp)
        Text(value, color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}
