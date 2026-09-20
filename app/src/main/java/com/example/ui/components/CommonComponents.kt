package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Nfc
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AccountEntity
import com.example.data.model.AccountType
import com.example.data.model.RiskEvaluation
import com.example.data.model.RiskLevel
import com.example.data.model.TransactionEntity
import com.example.data.model.TransactionStatus
import com.example.data.model.TransactionType
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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun RiskBadge(
    level: RiskLevel,
    score: Int,
    modifier: Modifier = Modifier
) {
    val (bgColor, textColor, label, icon) = when (level) {
        RiskLevel.LOW -> Quadruple(
            Color(0xFF064E3B).copy(alpha = 0.8f),
            EmeraldSuccess,
            "Low Risk ($score/100)",
            Icons.Default.CheckCircle
        )
        RiskLevel.MEDIUM -> Quadruple(
            Color(0xFF78350F).copy(alpha = 0.8f),
            WarningAmber,
            "Moderate Risk ($score/100)",
            Icons.Default.Warning
        )
        RiskLevel.HIGH -> Quadruple(
            Color(0xFF7F1D1D).copy(alpha = 0.8f),
            AmberOrange,
            "High Risk ($score/100)",
            Icons.Default.Warning
        )
        RiskLevel.CRITICAL -> Quadruple(
            Color(0xFF881337).copy(alpha = 0.9f),
            CrimsonDanger,
            "CRITICAL RISK ($score/100)",
            Icons.Default.Security
        )
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .border(1.dp, textColor.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = textColor,
            modifier = Modifier.size(14.dp)
        )
        Text(
            text = label,
            color = textColor,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

private data class Quadruple<A, B, C, D>(val first: A, val second: B, val third: C, val fourth: D)

@Composable
fun VirtualCardItem(
    account: AccountEntity,
    onFreezeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showNumbers by remember { mutableStateOf(false) }

    val gradient = when (account.type) {
        AccountType.CREDIT_CARD -> Brush.linearGradient(
            colors = listOf(Color(0xFF1F2937), Color(0xFF111827), Color(0xFF374151))
        )
        AccountType.SAVINGS -> Brush.linearGradient(
            colors = listOf(Color(0xFF064E3B), Color(0xFF065F46), Color(0xFF047857))
        )
        AccountType.INVESTMENT -> Brush.linearGradient(
            colors = listOf(Color(0xFF4C1D95), Color(0xFF5B21B6), Color(0xFF6D28D9))
        )
        AccountType.CHECKING -> Brush.linearGradient(
            colors = listOf(Color(0xFF0C4A6E), Color(0xFF0369A1), Color(0xFF0284C7))
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .height(210.dp)
            .testTag("virtual_card_${account.id}"),
        shape = RoundedCornerShape(20.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradient)
                .padding(20.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Top row: Brand & NFC / Status
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(CyberCyan.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Security,
                                contentDescription = "Bank logo",
                                tint = CyberCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Column {
                            Text(
                                text = "SMARTBANK AI",
                                color = TextWhite,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                letterSpacing = 1.5.sp
                            )
                            Text(
                                text = account.name,
                                color = TextWhite.copy(alpha = 0.7f),
                                fontSize = 11.sp
                            )
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (account.isContactlessEnabled) {
                            Icon(
                                imageVector = Icons.Default.Nfc,
                                contentDescription = "Contactless Active",
                                tint = TextWhite.copy(alpha = 0.8f),
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        IconButton(
                            onClick = onFreezeToggle,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(
                                    if (account.isFrozen) CrimsonDanger.copy(alpha = 0.4f)
                                    else TextWhite.copy(alpha = 0.15f)
                                )
                                .testTag("card_freeze_btn_${account.id}")
                        ) {
                            Icon(
                                imageVector = if (account.isFrozen) Icons.Default.Lock else Icons.Default.LockOpen,
                                contentDescription = if (account.isFrozen) "Unfreeze Card" else "Freeze Card",
                                tint = if (account.isFrozen) CrimsonDanger else TextWhite,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Card EMV Chip Simulation & Balance
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .width(36.dp)
                            .height(26.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(GoldAccent.copy(alpha = 0.9f))
                            .border(1.dp, Color(0xFFD97706), RoundedCornerShape(4.dp))
                    )

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = if (account.type == AccountType.CREDIT_CARD) "Utilized Balance" else "Available Balance",
                            color = TextWhite.copy(alpha = 0.7f),
                            fontSize = 11.sp
                        )
                        Text(
                            text = "$${"%,.2f".format(account.balance)}",
                            color = TextWhite,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Card Number + Expiry + CVV
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Text(
                                text = if (showNumbers) account.cardNumber else "•••• •••• •••• ${account.cardNumber.takeLast(4)}",
                                color = TextWhite,
                                fontSize = 15.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.SemiBold,
                                letterSpacing = 2.sp
                            )
                            IconButton(
                                onClick = { showNumbers = !showNumbers },
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = if (showNumbers) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle card number visibility",
                                    tint = TextWhite.copy(alpha = 0.7f),
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Text(
                                text = "EXP: ${account.expiry}",
                                color = TextWhite.copy(alpha = 0.8f),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                            Text(
                                text = "CVV: ${if (showNumbers) account.cvv else "•••"}",
                                color = TextWhite.copy(alpha = 0.8f),
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace
                            )
                        }
                    }

                    // Card Tier Badge
                    Surface(
                        color = Color.Black.copy(alpha = 0.3f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = account.type.name,
                            color = CyberCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            // Frozen Overlay
            if (account.isFrozen) {
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(Color.Black.copy(alpha = 0.75f))
                        .clip(RoundedCornerShape(20.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "Card Frozen",
                            tint = CrimsonDanger,
                            modifier = Modifier.size(36.dp)
                        )
                        Text(
                            text = "CARD CURRENTLY FROZEN",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                        Text(
                            text = "Tap unlock icon to reactivate card transactions",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TransactionRowItem(
    tx: TransactionEntity,
    onFlagFraud: () -> Unit,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isDebit = tx.type == TransactionType.DEBIT
    val amountColor = if (isDebit) TextWhite else EmeraldSuccess
    val prefix = if (isDebit) "-$" else "+$"

    val formattedDate = remember(tx.timestamp) {
        SimpleDateFormat("MMM dd, hh:mm a", Locale.US).format(Date(tx.timestamp))
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("tx_item_${tx.id}"),
        colors = CardDefaults.cardColors(
            containerColor = if (tx.status == TransactionStatus.BLOCKED_FRAUD)
                Color(0xFF450A0A).copy(alpha = 0.5f)
            else NavyCard
        ),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            when {
                                tx.status == TransactionStatus.BLOCKED_FRAUD -> CrimsonDanger.copy(alpha = 0.2f)
                                tx.riskScore >= 50 -> AmberOrange.copy(alpha = 0.2f)
                                isDebit -> CyberCyan.copy(alpha = 0.15f)
                                else -> EmeraldSuccess.copy(alpha = 0.15f)
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = when {
                            tx.status == TransactionStatus.BLOCKED_FRAUD -> Icons.Default.Security
                            tx.riskScore >= 50 -> Icons.Default.Warning
                            isDebit -> Icons.Default.CreditCard
                            else -> Icons.Default.CheckCircle
                        },
                        contentDescription = null,
                        tint = when {
                            tx.status == TransactionStatus.BLOCKED_FRAUD -> CrimsonDanger
                            tx.riskScore >= 50 -> AmberOrange
                            isDebit -> CyberCyan
                            else -> EmeraldSuccess
                        },
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column {
                    Text(
                        text = tx.title,
                        color = TextWhite,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "${tx.recipient} • $formattedDate",
                        color = TextMuted,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (tx.riskScore >= 30) {
                        Spacer(modifier = Modifier.height(4.dp))
                        RiskBadge(level = tx.riskLevel, score = tx.riskScore)
                    }
                }
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "$prefix${"%,.2f".format(tx.amount)}",
                    color = amountColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
                Text(
                    text = when (tx.status) {
                        TransactionStatus.BLOCKED_FRAUD -> "BLOCKED"
                        TransactionStatus.FLAGGED_REVIEW -> "FLAGGED"
                        TransactionStatus.PENDING -> "PENDING"
                        else -> "CLEARED"
                    },
                    color = when (tx.status) {
                        TransactionStatus.BLOCKED_FRAUD -> CrimsonDanger
                        TransactionStatus.FLAGGED_REVIEW -> AmberOrange
                        else -> EmeraldSuccess
                    },
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun OtpVerificationDialog(
    isOpen: Boolean,
    enteredOtp: String,
    onOtpChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    amount: String,
    risk: RiskEvaluation?
) {
    if (!isOpen) return

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Security,
                    contentDescription = null,
                    tint = CyberCyan
                )
                Text("2FA Security Verification", color = TextWhite, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text(
                    text = "A high-security OTP was dispatched to your registered authenticator & device for authorization of transfer of $$amount.",
                    color = TextMuted,
                    fontSize = 13.sp
                )

                if (risk != null && risk.score > 25) {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = Navy700),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(10.dp)) {
                            Text(
                                text = "🛡️ AI Risk Assessment: ${risk.level} (${risk.score}/100)",
                                color = if (risk.score >= 50) AmberOrange else CyberCyan,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp
                            )
                            Text(
                                text = risk.flags.joinToString("\n• ", prefix = "• "),
                                color = TextWhite.copy(alpha = 0.85f),
                                fontSize = 11.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = enteredOtp,
                    onValueChange = { if (it.length <= 6) onOtpChange(it) },
                    label = { Text("Enter 6-Digit OTP (e.g. 748291)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("otp_input_field"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )

                // Biometric shortcut
                OutlinedButton(
                    onClick = {
                        onOtpChange("1234")
                        onConfirm()
                    },
                    modifier = Modifier.fillMaxWidth().height(42.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Fingerprint, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
                        Text("Instant Biometric Passkey Approval", color = CyberCyan, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Text(
                    text = "Tip: Tap Biometric Approval or type demo OTP: 1234.",
                    color = CyberCyan.copy(alpha = 0.7f),
                    fontSize = 11.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                modifier = Modifier.testTag("confirm_otp_btn")
            ) {
                Text("Authorize & Send", color = Navy900, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextMuted)
            }
        },
        containerColor = Navy800
    )
}

@Composable
fun ExportReportDialog(
    isOpen: Boolean,
    reportContent: String,
    onDismiss: () -> Unit
) {
    if (!isOpen) return
    val clipboardManager = LocalClipboardManager.current
    var isCopied by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(imageVector = Icons.Default.Share, contentDescription = null, tint = CyberCyan)
                Text("Official Statement & CSV Export", color = TextWhite, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Encrypted digital bank report ready for download/compliance review:",
                    color = TextMuted,
                    fontSize = 12.sp
                )
                Surface(
                    color = Navy900,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                ) {
                    Text(
                        text = reportContent,
                        color = CyberCyan,
                        fontSize = 10.sp,
                        fontFamily = FontFamily.Monospace,
                        modifier = Modifier.padding(10.dp)
                    )
                }
                if (isCopied) {
                    Text("✓ Statement copied to clipboard!", color = EmeraldSuccess, fontSize = 12.sp)
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    clipboardManager.setText(AnnotatedString(reportContent))
                    isCopied = true
                },
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                modifier = Modifier.testTag("copy_statement_btn")
            ) {
                Text("Copy Statement / CSV", color = Navy900, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Close", color = TextMuted)
            }
        },
        containerColor = Navy800
    )
}
