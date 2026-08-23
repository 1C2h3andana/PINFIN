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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy900
import com.example.ui.theme.NavyCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BankViewModel

@Composable
fun DepositWithdrawDialog(
    viewModel: BankViewModel,
    onDismiss: () -> Unit
) {
    val state by viewModel.depositWithdrawState.collectAsStateWithLifecycle()
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val selectedAccount = accounts.find { it.id == state.selectedAccountId } ?: accounts.firstOrNull()

    val depositMethods = listOf(
        "ACH Electronic Wire Transfer",
        "Mobile Instant Check Deposit",
        "UPI / Debit Card Top-Up",
        "FedNow Instant Clearing"
    )

    val withdrawMethods = listOf(
        "External Linked Bank (Chase ...9941)",
        "ATM Cash Pinless Withdrawal",
        "FedNow Immediate Wire",
        "Cryptocurrency Vault Transfer"
    )

    val presetAmounts = listOf("100", "500", "1000", "2500", "5000")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 20.dp)
                .testTag("deposit_withdraw_modal"),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (state.isDepositMode) EmeraldSuccess.copy(alpha = 0.4f) else AmberOrange.copy(alpha = 0.4f))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header Icon
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(if (state.isDepositMode) EmeraldSuccess.copy(alpha = 0.15f) else AmberOrange.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (state.isDepositMode) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                        contentDescription = null,
                        tint = if (state.isDepositMode) EmeraldSuccess else AmberOrange,
                        modifier = Modifier.size(26.dp)
                    )
                }

                Text(
                    text = if (state.isDepositMode) "Instant Fund Deposit" else "Secure Fund Withdrawal",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 19.sp
                )

                // Tabs: Deposit vs Withdraw
                TabRow(
                    selectedTabIndex = if (state.isDepositMode) 0 else 1,
                    containerColor = Navy900,
                    contentColor = if (state.isDepositMode) EmeraldSuccess else AmberOrange,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Navy700, RoundedCornerShape(12.dp)),
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[if (state.isDepositMode) 0 else 1]),
                            color = if (state.isDepositMode) EmeraldSuccess else AmberOrange,
                            height = 3.dp
                        )
                    }
                ) {
                    Tab(
                        selected = state.isDepositMode,
                        onClick = { viewModel.openDepositDialog(state.selectedAccountId) },
                        text = {
                            Text(
                                "Deposit (+)",
                                fontWeight = if (state.isDepositMode) FontWeight.Bold else FontWeight.Normal,
                                color = if (state.isDepositMode) EmeraldSuccess else TextMuted
                            )
                        },
                        modifier = Modifier.testTag("tab_mode_deposit")
                    )
                    Tab(
                        selected = !state.isDepositMode,
                        onClick = { viewModel.openWithdrawDialog(state.selectedAccountId) },
                        text = {
                            Text(
                                "Withdraw (-)",
                                fontWeight = if (!state.isDepositMode) FontWeight.Bold else FontWeight.Normal,
                                color = if (!state.isDepositMode) AmberOrange else TextMuted
                            )
                        },
                        modifier = Modifier.testTag("tab_mode_withdraw")
                    )
                }

                // Account Selection Carousel/Chips
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("Select Target Account:", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    accounts.forEach { acc ->
                        val isSelected = acc.id == state.selectedAccountId
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.updateDepositWithdrawAccount(acc.id) },
                            color = if (isSelected) Navy900 else NavyCard,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) CyberCyan else Navy700)
                        ) {
                            Row(
                                modifier = Modifier.padding(10.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(acc.name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                    Text("Account: ...${acc.accountNumber.takeLast(4)}", color = TextMuted, fontSize = 11.sp)
                                }
                                Text("$${"%,.2f".format(acc.balance)}", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            }
                        }
                    }
                }

                // Amount Input
                OutlinedTextField(
                    value = state.amountText,
                    onValueChange = { viewModel.updateDepositWithdrawAmount(it) },
                    label = { Text("Amount ($)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("deposit_withdraw_amount_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = if (state.isDepositMode) EmeraldSuccess else AmberOrange,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )

                // Fast Amount Chips
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    presetAmounts.forEach { amt ->
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.updateDepositWithdrawAmount(amt) },
                            color = Navy900,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
                        ) {
                            Text(
                                text = "+$$amt",
                                color = CyberCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 6.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                // Payment / Clearing Channel Selector
                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = if (state.isDepositMode) "Deposit Source Method:" else "Withdrawal Destination:",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    val methods = if (state.isDepositMode) depositMethods else withdrawMethods
                    methods.forEach { method ->
                        val isSelected = state.methodOrDestination == method
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { viewModel.updateDepositWithdrawMethod(method) },
                            color = if (isSelected) CyberCyan.copy(alpha = 0.15f) else Navy900,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) CyberCyan else Navy700)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(method, color = if (isSelected) CyberCyan else TextWhite, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                                if (isSelected) {
                                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }

                // Submit Button
                Button(
                    onClick = { viewModel.submitDepositWithdraw() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (state.isDepositMode) EmeraldSuccess else AmberOrange
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("submit_deposit_withdraw_btn")
                ) {
                    Text(
                        text = if (state.isDepositMode) "Confirm & Deposit Funds" else "Authorize & Withdraw Funds",
                        color = Navy900,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
