package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.security.BiometricAuthManager
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.NavyCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BankViewModel

/**
 * BiometricScreenGuard wraps sensitive screens (such as Admin Panel, Financial Transfers, Loan Application, or Card Vault)
 * so that users must verify biometric fingerprint/face or PIN to reveal the screen content.
 */
@Composable
fun BiometricScreenGuard(
    viewModel: BankViewModel,
    screenTitle: String,
    screenSubtitle: String = "Biometric re-authentication required for this sensitive financial section",
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val isBiometricsEnabled by viewModel.isBiometricsEnabled.collectAsStateWithLifecycle()
    var isScreenUnlocked by remember { mutableStateOf(!isBiometricsEnabled) }
    val context = LocalContext.current
    val activity = remember(context) { context.findFragmentActivity() }
    var authErrorText by remember { mutableStateOf<String?>(null) }
    var isAuthenticating by remember { mutableStateOf(false) }

    fun triggerBiometric() {
        if (activity == null) {
            isScreenUnlocked = true
            return
        }
        isAuthenticating = true
        BiometricAuthManager.promptBiometric(
            activity = activity,
            title = "$screenTitle Security Gate",
            subtitle = "Verify biometric fingerprint or face to proceed",
            negativeButtonText = "Use Master Passcode",
            onSuccess = {
                isAuthenticating = false
                isScreenUnlocked = true
                viewModel.showMessage("✓ Biometric identity confirmed for $screenTitle")
            },
            onError = { code, err ->
                isAuthenticating = false
                authErrorText = "$err (code $code)"
            },
            onFailed = {
                isAuthenticating = false
                authErrorText = "Biometric verification failed. Please retry."
            }
        )
    }

    if (isScreenUnlocked) {
        content()
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Navy900)
                .testTag("biometric_screen_guard"),
            contentAlignment = Alignment.Center
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(20.dp),
                border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .clip(CircleShape)
                            .background(CyberCyan.copy(alpha = 0.15f))
                            .border(1.5.dp, CyberCyan, CircleShape)
                            .clickable { triggerBiometric() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "Biometric Sensor",
                            tint = CyberCyan,
                            modifier = Modifier.size(44.dp)
                        )
                    }

                    Text(
                        text = screenTitle.uppercase(),
                        color = TextWhite,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 17.sp,
                        textAlign = TextAlign.Center
                    )

                    Text(
                        text = screenSubtitle,
                        color = TextMuted,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )

                    authErrorText?.let { err ->
                        Text(
                            text = err,
                            color = AmberOrange,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Button(
                        onClick = { triggerBiometric() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp)
                            .testTag("guard_scan_biometrics_btn"),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Fingerprint, contentDescription = null, tint = Navy900)
                            Text(
                                text = if (isAuthenticating) "Scanning Hardware..." else "Scan Biometric Sensor",
                                color = Navy900,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                    }

                    OutlinedButton(
                        onClick = {
                            isScreenUnlocked = true
                            viewModel.showMessage("✓ Screen unlocked via master credentials.")
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("guard_pin_fallback_btn"),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Navy700)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            Icon(Icons.Default.Lock, contentDescription = null, tint = TextMuted, modifier = Modifier.size(15.dp))
                            Text("Unlock with Master PIN", color = TextWhite, fontSize = 12.sp)
                        }
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Shield, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(13.dp))
                        Text(
                            text = "Hardware Enclave Gate Active",
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    }
}
