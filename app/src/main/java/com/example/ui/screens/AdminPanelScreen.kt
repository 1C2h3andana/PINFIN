package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.SecurityEventType
import com.example.data.model.TransactionEntity
import com.example.data.model.UserRole
import com.example.ui.components.AdminOnlyGuard
import com.example.ui.components.HolographicGlassCard
import com.example.ui.components.HolographicTypingHeader
import com.example.ui.components.PulsingStatusBadge
import com.example.ui.components.SecurityClearanceLevel
import com.example.ui.theme.*
import com.example.ui.viewmodel.BankViewModel

@Composable
fun AdminPanelScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    // Verified via AdminOnlyGuard security barrier
    AdminOnlyGuard(
        viewModel = viewModel,
        modifier = modifier,
        requiredClearance = SecurityClearanceLevel.ROOT_ADMIN_LEVEL_5
    ) { securityContext ->
        AdminPanelContent(
            viewModel = viewModel,
            modifier = modifier
        )
    }
}

@Composable
private fun AdminPanelContent(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val currentUser = authState.currentUser

    val users by viewModel.users.collectAsStateWithLifecycle()
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val amlTransactions by viewModel.amlReviewTransactions.collectAsStateWithLifecycle()
    val securityLogs by viewModel.securityLogs.collectAsStateWithLifecycle()
    val fraudSensitivity by viewModel.fraudSensitivity.collectAsStateWithLifecycle()

    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("AML & Risk Queue (${amlTransactions.size})", "System Liquidity", "User Management", "Audit & Logs")

    val totalSystemDeposits = accounts.filter { it.type != com.example.data.model.AccountType.CREDIT_CARD }.sumOf { it.balance }
    val totalCreditUtilized = accounts.filter { it.type == com.example.data.model.AccountType.CREDIT_CARD }.sumOf { it.balance }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        item {
            HolographicTypingHeader(
                title = "ADMINISTRATIVE GOVERNANCE & AML",
                subtitle = "Central Security Operations & Quantum Compliance Enclave (Level 5 Security)",
                tag = "PFIN // SEC-OPS // ROOT",
                accentColor = AmberOrange
            )
        }

        // Active Admin Credentials Banner
        item {
            HolographicGlassCard(
                borderColor = AmberOrange,
                glowColor = AmberOrange.copy(alpha = 0.2f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(AmberOrange.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.AdminPanelSettings, contentDescription = null, tint = AmberOrange, modifier = Modifier.size(24.dp))
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 8.dp)
                            Text("ROOT ADMIN CLEARANCE GRANTED", color = TextWhite, fontWeight = FontWeight.Black, fontSize = 13.sp)
                        }
                        Text(
                            "Logged in as: ${currentUser?.email ?: "Super Administrator"} (Role: ${currentUser?.role?.name})",
                            color = CyberCyan,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                    }
                    Button(
                        onClick = {
                            viewModel.loginAsUser("alex.morgan@smartbank.ai")
                            viewModel.showMessage("Switched back to standard citizen user account")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Navy700, contentColor = TextWhite),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text("Exit Admin", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = NavyCard,
                contentColor = AmberOrange,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = AmberOrange,
                        height = 3.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                fontSize = 11.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) AmberOrange else TextMuted
                            )
                        },
                        modifier = Modifier.testTag("admin_tab_$index")
                    )
                }
            }
        }

        if (selectedTab == 0) {
            // TAB 0: AML QUEUE & FRAUD SENSITIVITY
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = NavyCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.Tune, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
                            Text("AI Fraud Engine Sensitivity", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            listOf("Balanced AI Model", "Aggressive Shield", "Permissive").forEach { mode ->
                                val isSelected = fraudSensitivity == mode
                                Surface(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { viewModel.setFraudSensitivity(mode) },
                                    color = if (isSelected) CyberCyan.copy(alpha = 0.2f) else Navy900,
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, if (isSelected) CyberCyan else Navy700)
                                ) {
                                    Text(
                                        text = mode,
                                        color = if (isSelected) CyberCyan else TextMuted,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        fontSize = 10.sp,
                                        modifier = Modifier.padding(vertical = 8.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }

            item {
                Text("Flagged & Suspicious Transactions Queue", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            if (amlTransactions.isEmpty()) {
                item {
                    Surface(color = Navy900, shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp)) {
                        Text(
                            text = "✓ Zero pending AML flags. All transactions cleared by automated shield.",
                            color = EmeraldSuccess,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(16.dp),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            } else {
                items(amlTransactions) { tx ->
                    AmlTransactionItem(
                        tx = tx,
                        onApprove = { viewModel.adminApproveTransaction(tx.id) },
                        onReject = { viewModel.adminRevertTransaction(tx.id) }
                    )
                }
            }
        } else if (selectedTab == 1) {
            // TAB 1: SYSTEM LIQUIDITY OVERVIEW
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = NavyCard),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Icon(Icons.Default.AccountBalance, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(20.dp))
                            Text("Macro System Liquidity Reserve", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                        }

                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Column {
                                Text("Total Citizen Deposits", color = TextMuted, fontSize = 11.sp)
                                Text("$${String.format("%,.2f", totalSystemDeposits)}", color = EmeraldSuccess, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                            }
                            Column(horizontalAlignment = Alignment.End) {
                                Text("Total Credit Extended", color = TextMuted, fontSize = 11.sp)
                                Text("$${String.format("%,.2f", totalCreditUtilized)}", color = AmberOrange, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                            }
                        }

                        val reserveRatio = if (totalCreditUtilized > 0) (totalSystemDeposits / (totalSystemDeposits + totalCreditUtilized) * 100).toInt() else 100
                        Surface(color = Navy900, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("Tier-1 Capital Adequacy Ratio (CAR)", color = TextWhite, fontSize = 12.sp)
                                Text("$reserveRatio% (Basel III Compliant)", color = if (reserveRatio > 80) EmeraldSuccess else AmberOrange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            }
                        }
                    }
                }
            }
        } else if (selectedTab == 2) {
            // TAB 2: USER MANAGEMENT & ROLE ASSIGNMENT
            item {
                Text("Registered Sovereign Users & Enclave RBAC", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            items(users) { user ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = NavyCard),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(user.fullName, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                Surface(
                                    color = if (user.role == UserRole.ADMIN) AmberOrange.copy(alpha = 0.2f) else CyberCyan.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = user.role.name,
                                        color = if (user.role == UserRole.ADMIN) AmberOrange else CyberCyan,
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                    )
                                }
                            }
                            Text(user.email, color = TextMuted, fontSize = 11.sp)
                        }

                        Button(
                            onClick = {
                                val nextRole = if (user.role == UserRole.ADMIN) UserRole.USER else UserRole.ADMIN
                                viewModel.updateUserRole(user.id, nextRole)
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (user.role == UserRole.ADMIN) Navy700 else AmberOrange.copy(alpha = 0.2f),
                                contentColor = if (user.role == UserRole.ADMIN) TextWhite else AmberOrange
                            ),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Text(if (user.role == UserRole.ADMIN) "Revoke Admin" else "Grant Admin", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        } else {
            // TAB 3: AUDIT & SECURITY LOGS
            item {
                Text("Immutable Security Logs & Access Trail", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            items(securityLogs.take(15)) { log ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Navy800),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(if (log.eventType == SecurityEventType.LOGIN_FAILED || log.eventType == SecurityEventType.SUSPICIOUS_TRANSFER_BLOCKED) CrimsonDanger else CyberCyan)
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(log.eventType.name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("${log.ipAddress} • Device: ${log.deviceName} • ${log.details}", color = TextMuted, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AmlTransactionItem(
    tx: TransactionEntity,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(1.dp, AmberOrange.copy(alpha = 0.3f))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(tx.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text("$${String.format("%,.2f", tx.amount)}", color = AmberOrange, fontWeight = FontWeight.ExtraBold, fontSize = 15.sp)
            }

            Text("Recipient: ${tx.recipient} • Account #${tx.accountId} • ${tx.timestamp}", color = TextMuted, fontSize = 11.sp)

            Surface(color = Navy900, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "⚠️ Risk Score: ${tx.riskScore}/100 • Reason: ${tx.riskReason}",
                    color = AmberOrange,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(8.dp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onApprove,
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(36.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = Navy900, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Approve & Release", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }

                Button(
                    onClick = onReject,
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonDanger),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(36.dp)
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, tint = TextWhite, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Reject & Freeze", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}
