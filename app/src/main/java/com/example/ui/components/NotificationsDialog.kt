package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.NotificationEntity
import com.example.data.model.NotificationType
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.CrimsonDanger
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
fun NotificationsDialog(
    viewModel: BankViewModel,
    onDismiss: () -> Unit
) {
    val notifications by viewModel.notifications.collectAsStateWithLifecycle()

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .padding(vertical = 24.dp)
                .testTag("notifications_dialog_card"),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(imageVector = Icons.Default.Notifications, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(24.dp))
                        Text("Notifications & Alerts", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }

                    Row {
                        IconButton(onClick = { viewModel.markAllNotificationsRead() }) {
                            Icon(Icons.Default.DoneAll, contentDescription = "Mark all read", tint = CyberCyan)
                        }
                        IconButton(onClick = { viewModel.clearAllNotifications() }) {
                            Icon(Icons.Default.ClearAll, contentDescription = "Clear all", tint = TextMuted)
                        }
                    }
                }

                if (notifications.isEmpty()) {
                    Surface(
                        color = Navy900,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp)
                    ) {
                        Text(
                            text = "You're all caught up! No active alerts.",
                            color = TextMuted,
                            fontSize = 13.sp,
                            modifier = Modifier.padding(20.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(380.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        items(notifications) { notif ->
                            NotificationItemCard(
                                notif = notif,
                                onClick = { viewModel.markNotificationRead(notif.id) }
                            )
                        }
                    }
                }

                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(containerColor = Navy700),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(42.dp)
                ) {
                    Text("Close Feed", color = TextWhite, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun NotificationItemCard(
    notif: NotificationEntity,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        colors = CardDefaults.cardColors(
            containerColor = if (!notif.isRead) Navy900 else NavyCard
        ),
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (!notif.isRead) CyberCyan.copy(alpha = 0.5f) else Navy700)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(CircleShape)
                    .background(
                        when (notif.type) {
                            NotificationType.FRAUD_WARNING -> CrimsonDanger.copy(alpha = 0.2f)
                            NotificationType.SECURITY_ALERT -> CyberCyan.copy(alpha = 0.2f)
                            NotificationType.PAYMENT_DUE -> AmberOrange.copy(alpha = 0.2f)
                            NotificationType.TRANSACTION_ALERT -> EmeraldSuccess.copy(alpha = 0.2f)
                            else -> ElectricBlue.copy(alpha = 0.2f)
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = when (notif.type) {
                        NotificationType.FRAUD_WARNING -> Icons.Default.Warning
                        NotificationType.SECURITY_ALERT -> Icons.Default.Security
                        NotificationType.PAYMENT_DUE -> Icons.Default.Payment
                        else -> Icons.Default.Notifications
                    },
                    contentDescription = null,
                    tint = when (notif.type) {
                        NotificationType.FRAUD_WARNING -> CrimsonDanger
                        NotificationType.SECURITY_ALERT -> CyberCyan
                        NotificationType.PAYMENT_DUE -> AmberOrange
                        else -> EmeraldSuccess
                    },
                    modifier = Modifier.size(18.dp)
                )
            }

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(notif.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    if (!notif.isRead) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(CyberCyan))
                    }
                }
                Text(notif.message, color = TextMuted, fontSize = 11.sp)
            }
        }
    }
}
