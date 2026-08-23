package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.BillEntity
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.NavyCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.theme.WarningAmber
import com.example.ui.viewmodel.BankViewModel

@Composable
fun BillsScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val bills by viewModel.bills.collectAsStateWithLifecycle()
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val primaryAccount = accounts.firstOrNull()

    val pendingBills = remember(bills) { bills.filter { !it.isPaid } }
    val totalPendingAmount = remember(pendingBills) { pendingBills.sumOf { it.amount } }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        item {
            Text(
                text = "AUTOMATED BILLS & UTILITIES",
                color = CyberCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "Bill Reminders & Auto-Pay",
                color = TextWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Summary Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("bills_summary_card"),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("TOTAL UPCOMING DUE", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text("$${"%,.2f".format(totalPendingAmount)}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 24.sp)
                        Text("${pendingBills.size} pending invoices this cycle", color = CyberCyan, fontSize = 11.sp)
                    }

                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(WarningAmber.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Schedule, contentDescription = null, tint = WarningAmber, modifier = Modifier.size(28.dp))
                    }
                }
            }
        }

        item {
            Text("UPCOMING UTILITY & SUBSCRIPTION INVOICES", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.2.sp)
        }

        items(bills, key = { it.id }) { bill ->
            BillItemCard(
                bill = bill,
                onPay = { primaryAccount?.let { acc -> viewModel.payBill(bill.id, acc.id) } },
                onToggleAutoPay = { viewModel.toggleBillAutoPay(bill.id, bill.isAutoPay) }
            )
        }
    }
}

@Composable
private fun BillItemCard(
    bill: BillEntity,
    onPay: () -> Unit,
    onToggleAutoPay: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("bill_item_${bill.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (bill.isPaid) Navy800 else NavyCard
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (bill.isPaid) EmeraldSuccess.copy(alpha = 0.2f) else CyberCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (bill.isPaid) Icons.Default.CheckCircle else Icons.Default.Receipt,
                            contentDescription = null,
                            tint = if (bill.isPaid) EmeraldSuccess else CyberCyan,
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(bill.billerName, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("${bill.category} • ${bill.dueDate}", color = TextMuted, fontSize = 11.sp)
                    }
                }

                Text(
                    text = "$${"%,.2f".format(bill.amount)}",
                    color = if (bill.isPaid) EmeraldSuccess else TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Navy700))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("Auto-Debit", color = TextWhite, fontSize = 12.sp)
                    Switch(
                        checked = bill.isAutoPay,
                        onCheckedChange = { onToggleAutoPay() },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = Navy900,
                            checkedTrackColor = CyberCyan,
                            uncheckedThumbColor = TextMuted,
                            uncheckedTrackColor = Navy700
                        ),
                        modifier = Modifier.testTag("bill_autopay_toggle_${bill.id}")
                    )
                }

                if (!bill.isPaid) {
                    Button(
                        onClick = onPay,
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(34.dp).testTag("pay_bill_btn_${bill.id}")
                    ) {
                        Text("Pay Now", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                } else {
                    Surface(
                        color = EmeraldSuccess.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text("PAID", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 11.sp, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                    }
                }
            }
        }
    }
}
