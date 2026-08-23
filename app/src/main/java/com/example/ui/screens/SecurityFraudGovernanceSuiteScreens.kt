package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.AdminOnlyGuard
import com.example.ui.components.SecurityClearanceLevel
import com.example.ui.theme.*
import com.example.ui.viewmodel.BankViewModel

enum class SecurityFraudPageType(val title: String, val icon: ImageVector, val code: String) {
    // Module 16: Fraud Detection Center
    FRAUD_DASHBOARD("Fraud Hub", Icons.Default.GppBad, "FRAUD"),
    LIVE_MONITORING("Live Threat Feed", Icons.Default.Radar, "MONITOR"),
    SUSPICIOUS_TX("Suspicious TX", Icons.Default.Warning, "SUSP"),
    FRAUD_PREDICTION("AI Prediction", Icons.Default.Psychology, "PREDICT"),

    // Module 17: Cyber Security Center
    CYBER_DASHBOARD("Cyber Hub", Icons.Default.Security, "CYBER"),
    LOGIN_HISTORY("Login History", Icons.Default.History, "LOGINS"),
    DEVICE_MGMT("Trusted Devices", Icons.Default.Devices, "DEVICES"),
    THREAT_DETECTION("Threat Detection", Icons.Default.Shield, "THREAT"),

    // Module 31: Administration
    ADMIN_DASHBOARD("Admin 360°", Icons.Default.AdminPanelSettings, "ADMIN"),
    USER_MGMT("User Directory", Icons.Default.People, "USERS"),
    ROLE_PERMISSIONS("RBAC & Permissions", Icons.Default.Key, "ROLES"),
    AUDIT_LOGS("System Audit Logs", Icons.Default.ReceiptLong, "AUDIT"),
    BACKUP_RECOVERY("Backup & Disaster", Icons.Default.Backup, "BACKUP"),

    // Module 37: Audit & Compliance
    COMPLIANCE_DASHBOARD("Compliance Hub", Icons.Default.Policy, "COMPLY"),
    AML_MONITORING("AML Watchlist", Icons.Default.Search, "AML"),
    KYC_MONITORING("KYC Tier Registry", Icons.Default.VerifiedUser, "KYC"),
    REGULATORY_REPORTS("ISO/Basel Reports", Icons.Default.Description, "REG")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SecurityFraudGovernanceSuiteScreen(
    viewModel: BankViewModel,
    initialPage: SecurityFraudPageType = SecurityFraudPageType.FRAUD_DASHBOARD,
    onNavigateToPage: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedPage by remember { mutableStateOf(initialPage) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Navy900)
    ) {
        // --- Top Header ---
        Card(
            colors = CardDefaults.cardColors(containerColor = Navy800),
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Brush.linearGradient(listOf(CrimsonDanger, AmberOrange))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.Security, contentDescription = null, tint = TextWhite, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "FRAUD • SECURITY • ADMIN • COMPLIANCE",
                                color = CrimsonDanger,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                "Zero-Trust Fortress Hub",
                                color = TextWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    AssistChip(
                        onClick = {},
                        label = { Text("Quantum Shield ACTIVE", fontSize = 11.sp, color = EmeraldSuccess, fontWeight = FontWeight.Bold) },
                        colors = AssistChipDefaults.assistChipColors(containerColor = EmeraldSuccess.copy(alpha = 0.15f)),
                        border = BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.4f))
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // --- Horizontal Selector Bar ---
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(SecurityFraudPageType.values()) { page ->
                        val isSelected = selectedPage == page
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedPage = page },
                            label = { Text(page.title, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            leadingIcon = {
                                Icon(
                                    page.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (isSelected) Navy900 else CrimsonDanger
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CrimsonDanger,
                                selectedLabelColor = TextWhite,
                                containerColor = Navy700,
                                labelColor = TextWhite
                            ),
                            border = BorderStroke(1.dp, if (isSelected) CrimsonDanger else Navy700)
                        )
                    }
                }
            }
        }

        // --- Animated Body Content ---
        AnimatedContent(
            targetState = selectedPage,
            transitionSpec = {
                (slideInHorizontally { width -> width / 3 } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width / 3 } + fadeOut()
                )
            },
            label = "SecurityPageTransition",
            modifier = Modifier.fillMaxSize()
        ) { targetPage ->
            when (targetPage) {
                SecurityFraudPageType.FRAUD_DASHBOARD -> FraudDashboardScreen(viewModel)
                SecurityFraudPageType.LIVE_MONITORING -> LiveMonitoringScreen(viewModel)
                SecurityFraudPageType.SUSPICIOUS_TX -> SuspiciousTxScreen(viewModel)
                SecurityFraudPageType.FRAUD_PREDICTION -> FraudPredictionScreen(viewModel)
                SecurityFraudPageType.CYBER_DASHBOARD -> CyberDashboardScreen(viewModel)
                SecurityFraudPageType.LOGIN_HISTORY -> LoginHistoryScreen(viewModel)
                SecurityFraudPageType.DEVICE_MGMT -> DeviceMgmtScreen(viewModel)
                SecurityFraudPageType.THREAT_DETECTION -> ThreatDetectionScreen(viewModel)
                SecurityFraudPageType.ADMIN_DASHBOARD -> AdminOnlyGuard(viewModel = viewModel, requiredClearance = SecurityClearanceLevel.ROOT_ADMIN_LEVEL_5) { AdminDashboardScreen(viewModel) }
                SecurityFraudPageType.USER_MGMT -> AdminOnlyGuard(viewModel = viewModel, requiredClearance = SecurityClearanceLevel.ROOT_ADMIN_LEVEL_5) { UserMgmtScreen(viewModel) }
                SecurityFraudPageType.ROLE_PERMISSIONS -> AdminOnlyGuard(viewModel = viewModel, requiredClearance = SecurityClearanceLevel.ROOT_ADMIN_LEVEL_5) { RolePermissionsScreen(viewModel) }
                SecurityFraudPageType.AUDIT_LOGS -> AdminOnlyGuard(viewModel = viewModel, requiredClearance = SecurityClearanceLevel.ROOT_ADMIN_LEVEL_5) { AuditLogsScreen(viewModel) }
                SecurityFraudPageType.BACKUP_RECOVERY -> AdminOnlyGuard(viewModel = viewModel, requiredClearance = SecurityClearanceLevel.ROOT_ADMIN_LEVEL_5) { BackupRecoveryScreen(viewModel) }
                SecurityFraudPageType.COMPLIANCE_DASHBOARD -> ComplianceDashboardScreen(viewModel)
                SecurityFraudPageType.AML_MONITORING -> AmlMonitoringScreen(viewModel)
                SecurityFraudPageType.KYC_MONITORING -> KycMonitoringScreen(viewModel)
                SecurityFraudPageType.REGULATORY_REPORTS -> RegulatoryReportsScreen(viewModel)
            }
        }
    }
}

// -------------------------------------------------------------------------
// SCREENS IMPLEMENTATIONS
// -------------------------------------------------------------------------
@Composable
fun FraudDashboardScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Autonomous AI Fraud Interceptor", color = CrimsonDanger, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("0 Active Breaches • 99.98% Anomaly Precision", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(12.dp))
                    DetailItemRow("Transactions Scanned (24h)", "1,240,580 TXs", CyberCyan)
                    DetailItemRow("Velocity Blocks Enforced", "14 Blocked", CrimsonDanger)
                    DetailItemRow("Synthetic Identity Interceptions", "3 Blocked", AmberOrange)
                }
            }
        }
    }
}

@Composable
fun LiveMonitoringScreen(viewModel: BankViewModel) {
    val liveFeeds = listOf(
        Triple("IP 194.26.29.112", "Brute force credential attempt blocked (Kyiv Geo)", "BLOCKED"),
        Triple("Card Token ...9912", "Velocity spike: 5 POS swipes in 30 seconds", "CHALLENGED"),
        Triple("SWIFT Wire #8812", "Sanction list screening clean", "CLEARED")
    )
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Live Security Event Stream", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp) }
        items(liveFeeds) { (ip, desc, status) ->
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(ip, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(desc, color = TextMuted, fontSize = 11.sp)
                    }
                    Text(status, color = if (status == "CLEARED") EmeraldSuccess else CrimsonDanger, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable fun SuspiciousTxScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Suspicious Activity Reports (SAR Queue)", color = AmberOrange, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("0 Flagged Transactions Requiring Manual Underwriter Review.", color = TextMuted, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable fun FraudPredictionScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Predictive Risk Scoring Graph", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Account Risk Index", "0.01 / 1.00 (Extremely Safe)", EmeraldSuccess)
                    DetailItemRow("Graph Network Similarity", "Clean Ledger Topology", CyberCyan)
                }
            }
        }
    }
}

@Composable fun CyberDashboardScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Post-Quantum Cryptography & Enclave", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Key Exchange", "Kyber-1024 Post-Quantum Active", EmeraldSuccess)
                    DetailItemRow("Signature Scheme", "Dilithium5 Post-Quantum Verified", CyberCyan)
                    DetailItemRow("Hardware Security Module", "FIPS 140-3 Level 4 Enclave", GoldAccent)
                }
            }
        }
    }
}

@Composable fun LoginHistoryScreen(viewModel: BankViewModel) {
    val logins = listOf(
        Triple("Android Pixel 9 Pro (This Device)", "San Francisco, CA • Today 15:45", "ACTIVE"),
        Triple("MacBook Pro M3 Max", "San Francisco, CA • Yesterday 09:12", "AUTHENTICATED"),
        Triple("Linux Enclave Server Node", "Zurich, CH • Aug 19, 2026", "AUTHENTICATED")
    )
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Active Session & Login History", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp) }
        items(logins) { (dev, loc, st) ->
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(dev, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(loc, color = TextMuted, fontSize = 11.sp)
                    }
                    Text(st, color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable fun DeviceMgmtScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Cryptographic Hardware Tokens & Devices", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("YubiKey 5C NFC Token", "Enrolled • Hardware Key #1", EmeraldSuccess)
                    DetailItemRow("Android StrongBox KeyStore", "Enrolled • Biometrics Key #2", CyberCyan)
                }
            }
        }
    }
}

@Composable fun ThreatDetectionScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Malware, Keylogger & Memory Shield", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("All runtime memory spaces attested with zero unauthorized injected hooks.", color = TextMuted, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable fun AdminDashboardScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Global System Administration Hub", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("System Uptime", "99.999% High Availability", EmeraldSuccess)
                    DetailItemRow("Active User Nodes", "1,420,800 Connected", CyberCyan)
                    DetailItemRow("Consensus Block Latency", "120 ms Settlement", GoldAccent)
                }
            }
        }
    }
}

@Composable fun UserMgmtScreen(viewModel: BankViewModel) {
    var newUserEmail by remember { mutableStateOf("") }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Invite Enterprise User & Set RBAC", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = newUserEmail,
                        onValueChange = { newUserEmail = it },
                        label = { Text("User Corporate Email") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, unfocusedBorderColor = Navy700, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            viewModel.showMessage("✓ Cryptographic invitation sent to $newUserEmail")
                            newUserEmail = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = Navy900),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text("Grant Role & Provision Keys", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable fun RolePermissionsScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Role-Based Access Control (RBAC)", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp) }
        items(listOf("Super Admin (Full Root Multi-Sig)", "Treasury Officer (Wire Authority >$1M)", "Compliance Auditor (Read-Only Signed Logs)", "DevOps Operator (Infrastructure Enclave)")) { role ->
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().padding(14.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Text(role, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("ACTIVE", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable fun AuditLogsScreen(viewModel: BankViewModel) {
    val logs = listOf(
        Triple("AUD-8891", "Treasury Sweep Rule modified by Admin #1", "14:20"),
        Triple("AUD-8890", "Biometric enrollment verified for DID:0x49f", "12:15"),
        Triple("AUD-8889", "Post-Quantum key rotation cycle completed", "08:00")
    )
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Immutable System Audit Trail", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp) }
        items(logs) { (id, log, time) ->
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(id, color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Text(log, color = TextWhite, fontSize = 12.sp)
                    }
                    Text(time, color = TextMuted, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable fun BackupRecoveryScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Disaster Recovery & Sharded Backups", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Shamir Secret Sharing", "3-of-5 Geographic Custodians", EmeraldSuccess)
                    DetailItemRow("Cold Storage Replication", "Swiss Alps & Arctic Vaults", CyberCyan)
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { viewModel.showMessage("✓ Initiated automated dry-run recovery drill.") },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = Navy900),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text("Trigger Recovery Simulation", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable fun ComplianceDashboardScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Regulatory Compliance (Basel III & ISO 20022)", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Tier 1 Capital Ratio (CET1)", "18.4% (Exceeds 8.0% Req)", EmeraldSuccess)
                    DetailItemRow("Liquidity Coverage Ratio (LCR)", "162% (Exceeds 100% Req)", CyberCyan)
                }
            }
        }
    }
}

@Composable fun AmlMonitoringScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Global Anti-Money Laundering (AML) Screening", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("OFAC Sanctions List", "Synchronized (Real-Time)", EmeraldSuccess)
                    DetailItemRow("PEP (Politically Exposed Persons)", "Automated Graph Filter Active", CyberCyan)
                }
            }
        }
    }
}

@Composable fun KycMonitoringScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Zero-Knowledge KYC Tier Status", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Current User Tier", "Tier 3 Sovereign Enterprise", EmeraldSuccess)
                    DetailItemRow("Daily Wire Limit", "Unlimited (Multi-Sig Attested)", GoldAccent)
                }
            }
        }
    }
}

@Composable fun RegulatoryReportsScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Generate Regulatory Filings Pack", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("One-click automated creation of FinCEN CTR, SAR, and MiCA compliance packages.", color = TextMuted, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { viewModel.showMessage("✓ Generated and cryptographically signed regulatory reporting pack.") },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = Navy900),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text("Export Compliance Package", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
