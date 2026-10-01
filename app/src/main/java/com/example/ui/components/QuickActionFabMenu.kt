package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.CrimsonDanger
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.NavyCard
import com.example.ui.theme.PurpleTech
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite

/**
 * Expandable Floating Action Button (FAB) Speed-Dial Menu.
 * Provides quick 1-tap access to common core banking actions:
 * - 'Add Transaction': Launch instant deposit or funds movement
 * - 'Check Eligibility': Launch AI Loan Prediction & Debt-to-Income Underwriting
 * - 'View Alerts': Open Real-Time Security Intelligence and Budget Alerts
 * - 'Export CSV': Export Room Database transaction logs to CSV with sharing
 */
@Composable
fun QuickActionFabMenu(
    onAddTransaction: () -> Unit,
    onCheckEligibility: () -> Unit,
    onViewAlerts: () -> Unit,
    onExportCsv: () -> Unit,
    onLockVault: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isExpanded by remember { mutableStateOf(false) }

    // Smooth rotation animation for FAB icon (+ into x)
    val rotationAngle by animateFloatAsState(
        targetValue = if (isExpanded) 135f else 0f,
        animationSpec = tween(durationMillis = 280, easing = FastOutSlowInEasing),
        label = "fab_rotation"
    )

    // Action items stack and Primary Trigger FAB
    Column(
        horizontalAlignment = Alignment.End,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = modifier
    ) {
        // Animated list of quick actions
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn(tween(200)) + slideInVertically(
                initialOffsetY = { it / 2 },
                animationSpec = tween(220, easing = FastOutSlowInEasing)
            ),
            exit = fadeOut(tween(150)) + slideOutVertically(
                targetOffsetY = { it / 2 },
                animationSpec = tween(180, easing = FastOutSlowInEasing)
            )
        ) {
                Column(
                    horizontalAlignment = Alignment.End,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Item 1: Add Transaction
                    FabSpeedDialItem(
                        icon = Icons.Default.Add,
                        label = "Add Transaction",
                        subtitle = "Instant Deposit & Funds Transfer",
                        containerColor = EmeraldSuccess,
                        contentColor = Navy900,
                        testTag = "fab_add_transaction",
                        onClick = {
                            isExpanded = false
                            onAddTransaction()
                        }
                    )

                    // Item 2: Check Eligibility (Loan Prediction)
                    FabSpeedDialItem(
                        icon = Icons.Default.Calculate,
                        label = "Check Eligibility",
                        subtitle = "AI Loan Underwriting & DTI Calculator",
                        containerColor = CyberCyan,
                        contentColor = Navy900,
                        testTag = "fab_check_eligibility",
                        onClick = {
                            isExpanded = false
                            onCheckEligibility()
                        }
                    )

                    // Item 3: View Alerts
                    FabSpeedDialItem(
                        icon = Icons.Default.NotificationsActive,
                        label = "View Alerts",
                        subtitle = "Security & Budget Threshold Warnings",
                        containerColor = AmberOrange,
                        contentColor = Navy900,
                        testTag = "fab_view_alerts",
                        onClick = {
                            isExpanded = false
                            onViewAlerts()
                        }
                    )

                    // Item 4: Export CSV
                    FabSpeedDialItem(
                        icon = Icons.Default.FileDownload,
                        label = "Export CSV",
                        subtitle = "Download Room Database Audit Logs",
                        containerColor = PurpleTech,
                        contentColor = TextWhite,
                        testTag = "fab_export_csv",
                        onClick = {
                            isExpanded = false
                            onExportCsv()
                        }
                    )

                    // Item 5: Lock App Vault (Biometric Security)
                    if (onLockVault != null) {
                        FabSpeedDialItem(
                            icon = Icons.Default.Fingerprint,
                            label = "Lock Vault",
                            subtitle = "Secure SmartBank with Biometrics",
                            containerColor = EmeraldSuccess,
                            contentColor = Navy900,
                            testTag = "fab_lock_vault",
                            onClick = {
                                isExpanded = false
                                onLockVault()
                            }
                        )
                    }
                }
            }

            // Main Primary FAB Button
            FloatingActionButton(
                onClick = { isExpanded = !isExpanded },
                containerColor = CyberCyan,
                contentColor = Navy900,
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 8.dp,
                    pressedElevation = 12.dp
                ),
                shape = CircleShape,
                modifier = Modifier
                    .size(58.dp)
                    .testTag("fab_main_toggle")
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = if (isExpanded) "Close Quick Actions Menu" else "Open Quick Actions Menu",
                    modifier = Modifier
                        .size(28.dp)
                        .rotate(rotationAngle),
                    tint = Navy900
                )
            }
        }
}

@Composable
private fun FabSpeedDialItem(
    icon: ImageVector,
    label: String,
    subtitle: String,
    containerColor: Color,
    contentColor: Color,
    testTag: String,
    onClick: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = Modifier
            .clickable { onClick() }
            .testTag(testTag)
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        // Label badge card
        Surface(
            color = NavyCard,
            shape = RoundedCornerShape(12.dp),
            shadowElevation = 4.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, Navy800)
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = label,
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
                Text(
                    text = subtitle,
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }
        }

        // Mini Circular Action Button (48dp touch target)
        Box(
            modifier = Modifier
                .size(48.dp)
                .shadow(6.dp, CircleShape)
                .clip(CircleShape)
                .background(containerColor),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}
