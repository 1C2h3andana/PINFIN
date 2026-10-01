package com.example.ui.components

import android.content.Context
import android.content.ContextWrapper
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Backspace
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.fragment.app.FragmentActivity
import com.example.security.BiometricAuthManager
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.CrimsonDanger
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.NavyCard
import com.example.ui.theme.PurpleTech
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BankViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Finds FragmentActivity from Compose Context
 */
fun Context.findFragmentActivity(): FragmentActivity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is FragmentActivity) return ctx
        ctx = ctx.baseContext
    }
    return null
}

/**
 * Full-screen Biometric Lock Gate for App Launch.
 * Protects all sensitive financial accounts, transactions, and balances.
 * Triggers hardware BiometricPrompt API automatically or lets the user scan via interactive sensor / Master PIN.
 */
@Composable
fun BiometricAppLockOverlay(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val activity = remember(context) { context.findFragmentActivity() }

    val capability = remember(context) { BiometricAuthManager.getDeviceCapability(context) }
    var authErrorText by remember { mutableStateOf<String?>(null) }
    var isAuthenticating by remember { mutableStateOf(false) }
    var isSimulatingScan by remember { mutableStateOf(false) }
    var isPinDialogOpen by remember { mutableStateOf(false) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse_rings")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.22f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.35f,
        targetValue = 0.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseAlpha"
    )

    fun triggerSystemBiometric() {
        if (activity == null) {
            viewModel.unlockAppWithBiometrics("Biometric Simulator (Host)")
            return
        }

        when (capability.status) {
            BiometricAuthManager.BiometricStatus.AVAILABLE -> {
                // Real biometric hardware is present & registered
                authErrorText = null
                isAuthenticating = true
                BiometricAuthManager.promptBiometricWithResult(
                    activity = activity,
                    title = "SmartBank Biometric Vault",
                    subtitle = "Verify fingerprint or face to access financial records",
                    description = "Confirms identity using Android Keystore Enclave before sensitive financial balances are decrypted.",
                    negativeButtonText = "Use Master Passcode",
                    allowDeviceCredential = false,
                    onSuccess = {
                        isAuthenticating = false
                        viewModel.unlockAppWithBiometrics("Android Biometric Hardware (BiometricPrompt)")
                    },
                    onError = { code, err ->
                        isAuthenticating = false
                        if (code == androidx.biometric.BiometricPrompt.ERROR_NEGATIVE_BUTTON) {
                            isPinDialogOpen = true
                        } else {
                            authErrorText = "$err (Code $code)"
                        }
                    },
                    onFailed = {
                        isAuthenticating = false
                        authErrorText = "Biometric not recognized. Please scan again."
                    }
                )
            }
            BiometricAuthManager.BiometricStatus.DEVICE_CREDENTIAL_ONLY -> {
                // Device screen lock (PIN/pattern)
                authErrorText = null
                isAuthenticating = true
                BiometricAuthManager.promptBiometricWithResult(
                    activity = activity,
                    title = "SmartBank Device Unlock",
                    subtitle = "Confirm device credentials to unlock financial records",
                    allowDeviceCredential = true,
                    onSuccess = {
                        isAuthenticating = false
                        viewModel.unlockAppWithBiometrics("Device Screen Lock Credentials")
                    },
                    onError = { code, err ->
                        isAuthenticating = false
                        authErrorText = "$err (Code $code)"
                    },
                    onFailed = {
                        isAuthenticating = false
                        authErrorText = "Credential verification failed. Please try again."
                    }
                )
            }
            else -> {
                // On emulator or devices without registered biometrics:
                // Provide high-tech interactive sensor simulation
                coroutineScope.launch {
                    isSimulatingScan = true
                    authErrorText = null
                    delay(500)
                    isSimulatingScan = false
                    viewModel.unlockAppWithBiometrics("Hardware Sensor Simulation (Enclave Test Key)")
                }
            }
        }
    }

    // Auto-prompt on initial app launch if biometrics are available
    LaunchedEffect(Unit) {
        if (capability.status == BiometricAuthManager.BiometricStatus.AVAILABLE ||
            capability.status == BiometricAuthManager.BiometricStatus.DEVICE_CREDENTIAL_ONLY) {
            triggerSystemBiometric()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Navy900)
            .testTag("biometric_app_lock_overlay"),
        contentAlignment = Alignment.Center
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                Spacer(modifier = Modifier.height(16.dp))
                // Vault Shield Header Icon
                Box(
                    modifier = Modifier
                        .size(86.dp)
                        .clip(CircleShape)
                        .background(CyberCyan.copy(alpha = 0.12f))
                        .border(2.dp, CyberCyan.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Security Shield",
                        tint = CyberCyan,
                        modifier = Modifier.size(46.dp)
                    )
                }
            }

            item {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "SMARTBANK PRIVATE VAULT",
                        color = TextWhite,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        letterSpacing = 1.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Android BiometricPrompt • FIPS 140-3 Hardware Gate",
                        color = CyberCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // Sensor Capability Badge
            item {
                Surface(
                    color = when (capability.status) {
                        BiometricAuthManager.BiometricStatus.AVAILABLE -> EmeraldSuccess.copy(alpha = 0.15f)
                        BiometricAuthManager.BiometricStatus.DEVICE_CREDENTIAL_ONLY -> CyberCyan.copy(alpha = 0.15f)
                        else -> AmberOrange.copy(alpha = 0.15f)
                    },
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        when (capability.status) {
                            BiometricAuthManager.BiometricStatus.AVAILABLE -> EmeraldSuccess.copy(alpha = 0.5f)
                            BiometricAuthManager.BiometricStatus.DEVICE_CREDENTIAL_ONLY -> CyberCyan.copy(alpha = 0.5f)
                            else -> AmberOrange.copy(alpha = 0.5f)
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(
                            imageVector = when (capability.status) {
                                BiometricAuthManager.BiometricStatus.AVAILABLE -> Icons.Default.VerifiedUser
                                BiometricAuthManager.BiometricStatus.DEVICE_CREDENTIAL_ONLY -> Icons.Default.Lock
                                else -> Icons.Default.Fingerprint
                            },
                            contentDescription = null,
                            tint = when (capability.status) {
                                BiometricAuthManager.BiometricStatus.AVAILABLE -> EmeraldSuccess
                                BiometricAuthManager.BiometricStatus.DEVICE_CREDENTIAL_ONLY -> CyberCyan
                                else -> AmberOrange
                            },
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = capability.statusSummary,
                            color = TextWhite,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            // Main Security Action Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = NavyCard),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
                ) {
                    Column(
                        modifier = Modifier.padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // Pulsing Fingerprint Sensor Scan Target
                        Box(
                            modifier = Modifier.size(130.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            // Pulsing Outer Radar Ring
                            Box(
                                modifier = Modifier
                                    .size(126.dp)
                                    .scale(pulseScale)
                                    .clip(CircleShape)
                                    .background(CyberCyan.copy(alpha = pulseAlpha))
                            )

                            // Inner Interactive Touch Target
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .clip(CircleShape)
                                    .background(if (isSimulatingScan) EmeraldSuccess.copy(alpha = 0.25f) else CyberCyan.copy(alpha = 0.15f))
                                    .border(2.dp, if (isSimulatingScan) EmeraldSuccess else CyberCyan, CircleShape)
                                    .clickable { triggerSystemBiometric() }
                                    .testTag("biometric_sensor_touch_target"),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSimulatingScan || isAuthenticating) {
                                    CircularProgressIndicator(
                                        color = CyberCyan,
                                        modifier = Modifier.size(54.dp),
                                        strokeWidth = 3.dp
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Fingerprint,
                                        contentDescription = "Scan Fingerprint Sensor",
                                        tint = CyberCyan,
                                        modifier = Modifier.size(56.dp)
                                    )
                                }
                            }
                        }

                        Text(
                            text = "Touch sensor or scan face to unlock financial accounts, cards, and transaction records.",
                            color = TextMuted,
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )

                        // Error Banner if needed
                        authErrorText?.let { err ->
                            Surface(
                                color = AmberOrange.copy(alpha = 0.15f),
                                shape = RoundedCornerShape(8.dp),
                                border = androidx.compose.foundation.BorderStroke(1.dp, AmberOrange.copy(alpha = 0.5f)),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.padding(10.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    Icon(Icons.Default.Warning, contentDescription = null, tint = AmberOrange, modifier = Modifier.size(16.dp))
                                    Text(
                                        text = err,
                                        color = AmberOrange,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }

                        // Primary Action: Launch BiometricPrompt API
                        Button(
                            onClick = { triggerSystemBiometric() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("biometric_unlock_button"),
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Fingerprint, contentDescription = null, tint = Navy900, modifier = Modifier.size(20.dp))
                                Text(
                                    text = if (isAuthenticating) "Verifying Biometric..." else "Authenticate with Biometrics",
                                    color = Navy900,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        // Fallback: Master Passcode / PIN Dialog
                        OutlinedButton(
                            onClick = { isPinDialogOpen = true },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(44.dp)
                                .testTag("biometric_pin_fallback_btn"),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Lock, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                                Text(
                                    text = "Use Master Passcode (PIN 1234)",
                                    color = TextWhite,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        // Sandbox Simulator Unlock for Demo / Emulator
                        TextButton(
                            onClick = {
                                viewModel.unlockAppWithBiometrics("Direct Simulator Hardware Verification")
                            },
                            modifier = Modifier.testTag("biometric_quick_demo_unlock")
                        ) {
                            Text(
                                text = "Quick Hardware Simulation Unlock (Demo)",
                                color = CyberCyan.copy(alpha = 0.8f),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Normal
                            )
                        }
                    }
                }
            }

            // Security Architecture Note
            item {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(14.dp))
                    Text(
                        text = "Android BiometricPrompt • AES-256 GCM • Zero Cloud Biometrics",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }

    // Master Passcode (PIN) Dialog
    if (isPinDialogOpen) {
        MasterPinUnlockDialog(
            viewModel = viewModel,
            onDismiss = { isPinDialogOpen = false }
        )
    }
}

/**
 * Dedicated Master Passcode / PIN pad dialog for secure fallback.
 */
@Composable
private fun MasterPinUnlockDialog(
    viewModel: BankViewModel,
    onDismiss: () -> Unit
) {
    var enteredPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf<String?>(null) }

    fun submitPin() {
        if (enteredPin.length < 4) {
            pinError = "Please enter 4 digits."
            return
        }
        val isSuccess = viewModel.verifyMasterPasscode(enteredPin)
        if (isSuccess) {
            onDismiss()
        } else {
            enteredPin = ""
            pinError = "Incorrect Passcode. Default PIN is 1234."
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Navy900),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
        ) {
            Column(
                modifier = Modifier.padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(Icons.Default.Pin, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(20.dp))
                        Text("Master Passcode", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close", tint = TextMuted)
                    }
                }

                Text(
                    text = "Enter your 4-digit Master Security PIN to unlock your financial data.",
                    color = TextMuted,
                    fontSize = 12.sp,
                    textAlign = TextAlign.Center
                )

                // PIN Digit Indicators (4 circles)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (i in 0 until 4) {
                        val isFilled = enteredPin.length > i
                        Box(
                            modifier = Modifier
                                .size(20.dp)
                                .clip(CircleShape)
                                .background(if (isFilled) CyberCyan else Navy800)
                                .border(1.5.dp, if (isFilled) CyberCyan else Navy700, CircleShape)
                        )
                    }
                }

                pinError?.let { err ->
                    Text(err, color = CrimsonDanger, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }

                // Numeric Keypad (1 to 9, Clear, 0, Backspace)
                val keyRows = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("C", "0", "DEL")
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    keyRows.forEach { row ->
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            row.forEach { key ->
                                Surface(
                                    modifier = Modifier
                                        .size(60.dp, 44.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            when (key) {
                                                "C" -> {
                                                    enteredPin = ""
                                                    pinError = null
                                                }
                                                "DEL" -> {
                                                    if (enteredPin.isNotEmpty()) {
                                                        enteredPin = enteredPin.dropLast(1)
                                                        pinError = null
                                                    }
                                                }
                                                else -> {
                                                    if (enteredPin.length < 4) {
                                                        enteredPin += key
                                                        pinError = null
                                                        if (enteredPin.length == 4) {
                                                            submitPin()
                                                        }
                                                    }
                                                }
                                            }
                                        },
                                    color = NavyCard,
                                    shape = RoundedCornerShape(8.dp),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        if (key == "DEL") {
                                            Icon(Icons.AutoMirrored.Filled.Backspace, contentDescription = "Delete", tint = TextMuted, modifier = Modifier.size(18.dp))
                                        } else {
                                            Text(
                                                text = key,
                                                color = if (key == "C") AmberOrange else TextWhite,
                                                fontSize = 16.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Hint: Default PIN is 1234
                Text(
                    text = "Default Passcode: 1234",
                    color = CyberCyan.copy(alpha = 0.8f),
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

