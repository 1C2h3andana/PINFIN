package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.RiskLevel
import com.example.data.model.SecurityLogEntity
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

@Composable
fun SecurityCenterScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val is2FaEnabled by viewModel.is2FaGloballyEnabled.collectAsStateWithLifecycle()
    val isBiometricsEnabled by viewModel.isBiometricsEnabled.collectAsStateWithLifecycle()
    val isLoginAlertsEnabled by viewModel.isLoginAlertsEnabled.collectAsStateWithLifecycle()
    val isAmlEnabled by viewModel.isAmlMonitoringEnabled.collectAsStateWithLifecycle()
    val securityLogs by viewModel.securityLogs.collectAsStateWithLifecycle()
    val scamInput by viewModel.scamInspectorInput.collectAsStateWithLifecycle()
    val scamResult by viewModel.scamInspectionResult.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        item {
            Text(
                text = "CYBERSECURITY & DEFENSE CENTER",
                color = CyberCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "Account Security & Anti-Scam Shield",
                color = TextWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // --- 1. Security Health Score Card ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("security_score_card"),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(18.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "SECURITY HEALTH STATUS",
                            color = CyberCyan,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 11.sp
                        )
                        Text(
                            text = "Maximum Protection (98/100)",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        Text(
                            text = "AI Threat Intelligence, 2FA Biometrics, & Hardware Vault Encryption are active.",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }

                    Box(
                        modifier = Modifier.size(68.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            progress = { 0.98f },
                            modifier = Modifier.fillMaxSize(),
                            color = EmeraldSuccess,
                            strokeWidth = 6.dp,
                            trackColor = Navy700
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Shield,
                                contentDescription = null,
                                tint = EmeraldSuccess,
                                modifier = Modifier.size(22.dp)
                            )
                            Text("98%", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // --- 2. Live Phishing / Scam URL & Handle Inspector Tool ---
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("scam_inspector_card"),
                colors = CardDefaults.cardColors(containerColor = NavyCardLight),
                shape = RoundedCornerShape(18.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(imageVector = Icons.Default.QrCodeScanner, contentDescription = null, tint = CyberCyan)
                        Column {
                            Text(
                                text = "PHISHING & FAKE UPI SCANNER",
                                color = TextWhite,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                            Text(
                                text = "Paste any SMS link, URL, or UPI handle to scan for known scam signatures",
                                color = TextMuted,
                                fontSize = 11.sp
                            )
                        }
                    }

                    OutlinedTextField(
                        value = scamInput,
                        onValueChange = { viewModel.updateScamInspectorInput(it) },
                        placeholder = { Text("e.g. sbi-rewards-claim.xyz or win-crypto@free") },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("scam_scanner_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        ),
                        trailingIcon = {
                            if (scamInput.isNotEmpty()) {
                                IconButton(onClick = { viewModel.clearScamInspection() }) {
                                    Icon(imageVector = Icons.Default.Search, contentDescription = "Scan", tint = CyberCyan)
                                }
                            }
                        }
                    )

                    Button(
                        onClick = { viewModel.performScamInspection() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("run_scam_scan_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Analyze Link with AI Fraud Shield", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }

                    // Scan Result Box
                    scamResult?.let { result ->
                        Card(
                            colors = CardDefaults.cardColors(
                                containerColor = when (result.level) {
                                    RiskLevel.CRITICAL -> Color(0xFF450A0A)
                                    RiskLevel.HIGH -> Color(0xFF451A03)
                                    RiskLevel.MEDIUM -> Color(0xFF78350F).copy(alpha = 0.5f)
                                    RiskLevel.LOW -> Color(0xFF064E3B).copy(alpha = 0.6f)
                                }
                            ),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("Threat Assessment:", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    RiskBadge(level = result.level, score = result.score)
                                }
                                Text(
                                    text = result.recommendation,
                                    color = TextWhite,
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 12.sp
                                )
                                result.flags.forEach { f ->
                                    Text("• $f", color = TextWhite.copy(alpha = 0.85f), fontSize = 11.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- 3. Security Settings Toggles ---
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "DEFENSE CONTROLS & AUTHENTICATION",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = NavyCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        SecurityToggleRow(
                            icon = Icons.Default.VpnKey,
                            title = "Mandatory 2FA OTP for All Transfers",
                            description = "Require dynamic SMS/Authenticator OTP on every transfer",
                            checked = is2FaEnabled,
                            onCheckedChange = { viewModel.toggle2FaGlobal() },
                            testTag = "toggle_2fa"
                        )

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Navy700))

                        SecurityToggleRow(
                            icon = Icons.Default.Fingerprint,
                            title = "Biometric Passkey Authentication",
                            description = "Enable fingerprint / Face Unlock on device hardware",
                            checked = isBiometricsEnabled,
                            onCheckedChange = { viewModel.toggleBiometrics() },
                            testTag = "toggle_biometrics"
                        )

                        if (isBiometricsEnabled) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 6.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Test Biometric Lock Now", color = TextMuted, fontSize = 12.sp)
                                Button(
                                    onClick = { viewModel.lockAppBiometric() },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan.copy(alpha = 0.2f)),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f)),
                                    modifier = Modifier.testTag("lock_app_now_btn")
                                ) {
                                    Icon(Icons.Default.Lock, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Lock App Now", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Navy700))

                        SecurityToggleRow(
                            icon = Icons.Default.NotificationsActive,
                            title = "Real-Time Login & Device Alerts",
                            description = "Push notifications for new logins and unknown IPs",
                            checked = isLoginAlertsEnabled,
                            onCheckedChange = { viewModel.toggleLoginAlerts() },
                            testTag = "toggle_login_alerts"
                        )

                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Navy700))

                        SecurityToggleRow(
                            icon = Icons.Default.Security,
                            title = "AML Compliance Surveillance",
                            description = "Continuous anti-money laundering threshold monitoring",
                            checked = isAmlEnabled,
                            onCheckedChange = { viewModel.toggleAmlMonitoring() },
                            testTag = "toggle_aml"
                        )
                    }
                }
            }
        }

        // --- 4. Connected Devices ---
        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "ACTIVE TRUSTED DEVICES",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.2.sp
                )

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = NavyCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        DeviceRowItem("Pixel 8 Pro (Current Device)", "New York, USA • Active Now", isCurrent = true)
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(Navy700))
                        DeviceRowItem("MacBook Pro M3 (Web Client)", "New York, USA • Last active 2h ago", isCurrent = false)
                    }
                }
            }
        }

        // --- 5. Security Audit Log ---
        item {
            Text(
                text = "SECURITY INCIDENT & AUDIT TRAIL",
                color = TextMuted,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.2.sp
            )
        }

        if (securityLogs.isEmpty()) {
            item {
                Card(colors = CardDefaults.cardColors(containerColor = NavyCard)) {
                    Box(modifier = Modifier.fillMaxWidth().padding(20.dp), contentAlignment = Alignment.Center) {
                        Text("No security events logged", color = TextMuted)
                    }
                }
            }
        } else {
            items(securityLogs.take(10), key = { it.id }) { log ->
                SecurityLogRowItem(log)
            }
        }
    }
}

@Composable
private fun SecurityToggleRow(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    testTag: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
                    .size(36.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(CyberCyan.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
            }
            Column {
                Text(title, color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Text(description, color = TextMuted, fontSize = 11.sp)
            }
        }

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            modifier = Modifier.testTag(testTag),
            colors = SwitchDefaults.colors(
                checkedThumbColor = Navy900,
                checkedTrackColor = CyberCyan,
                uncheckedThumbColor = TextMuted,
                uncheckedTrackColor = Navy700
            )
        )
    }
}

@Composable
private fun DeviceRowItem(deviceName: String, locationTime: String, isCurrent: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(imageVector = Icons.Default.Devices, contentDescription = null, tint = if (isCurrent) CyberCyan else TextMuted)
            Column {
                Text(deviceName, color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 13.sp)
                Text(locationTime, color = TextMuted, fontSize = 11.sp)
            }
        }
        if (isCurrent) {
            Surface(
                color = EmeraldSuccess.copy(alpha = 0.2f),
                shape = RoundedCornerShape(6.dp)
            ) {
                Text(
                    text = "THIS DEVICE",
                    color = EmeraldSuccess,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp)
                )
            }
        }
    }
}

@Composable
private fun SecurityLogRowItem(log: SecurityLogEntity) {
    val formattedDate = remember(log.timestamp) {
        SimpleDateFormat("MMM dd, hh:mm a", Locale.US).format(Date(log.timestamp))
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Icon(
                imageVector = when (log.severity) {
                    RiskLevel.CRITICAL -> Icons.Default.Security
                    RiskLevel.HIGH -> Icons.Default.Warning
                    else -> Icons.Default.CheckCircle
                },
                contentDescription = null,
                tint = when (log.severity) {
                    RiskLevel.CRITICAL -> CrimsonDanger
                    RiskLevel.HIGH -> AmberOrange
                    else -> EmeraldSuccess
                },
                modifier = Modifier.size(20.dp)
            )

            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = log.eventType.name.replace("_", " "),
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(formattedDate, color = TextMuted, fontSize = 10.sp)
                }
                Text(log.details, color = TextWhite.copy(alpha = 0.8f), fontSize = 11.sp)
                Text("IP: ${log.ipAddress} • ${log.deviceName}", color = TextMuted, fontSize = 10.sp)
            }
        }
    }
}
