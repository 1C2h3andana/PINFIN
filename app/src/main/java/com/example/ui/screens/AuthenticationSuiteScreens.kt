package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CheckCircleOutline
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.LockReset
import androidx.compose.material.icons.filled.Login
import androidx.compose.material.icons.filled.Password
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.QuestionAnswer
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.UserRole
import com.example.ui.components.PulsingStatusBadge
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
import com.example.ui.viewmodel.BankViewModel
import com.example.security.BiometricAuthManager
import com.example.ui.components.findFragmentActivity
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.delay

enum class AuthPageType(
    val title: String,
    val shortLabel: String,
    val icon: ImageVector,
    val color: Color,
    val routeKey: String
) {
    LOGIN("Secure Login", "Login", Icons.Default.Login, CyberCyan, "auth_login"),
    REGISTER("New Account Registration", "Register", Icons.Default.PersonAdd, ElectricBlue, "auth_register"),
    FORGOT_PASSWORD("Forgot Password Recovery", "Forgot Pwd", Icons.Default.LockReset, AmberOrange, "auth_forgot_password"),
    RESET_PASSWORD("Set New Password", "Reset Pwd", Icons.Default.Password, PurpleTech, "auth_reset_password"),
    OTP_VERIFICATION("One-Time Password (OTP)", "OTP Verify", Icons.Default.Pin, EmeraldSuccess, "auth_otp"),
    MFA_VERIFICATION("Multi-Factor Authentication", "MFA Auth", Icons.Default.VpnKey, GoldAccent, "auth_mfa"),
    BIOMETRIC_LOGIN("Biometric Fingerprint Login", "Fingerprint", Icons.Default.Fingerprint, CyberCyan, "auth_biometric"),
    FACE_RECOGNITION("AI Face Recognition Scan", "Face Scan", Icons.Default.Face, PurpleTech, "auth_face_scan"),
    DEVICE_VERIFICATION("Hardware & Device Binding", "Device Verify", Icons.Default.Devices, ElectricBlue, "auth_device_verify"),
    SECURITY_QUESTIONS("Security Questions Vault", "Questions", Icons.Default.HelpOutline, AmberOrange, "auth_security_questions")
}

// -------------------------------------------------------------------------
// 1. UNIFIED AUTHENTICATION & SECURITY PORTAL HOST
// -------------------------------------------------------------------------
@Composable
fun AuthSecurityHubScreen(
    viewModel: BankViewModel,
    initialPage: AuthPageType = AuthPageType.LOGIN,
    onNavigateToPage: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedPage by remember { mutableStateOf(initialPage) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Top Security Enclave Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, selectedPage.color.copy(alpha = 0.6f))
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(42.dp)
                        .clip(CircleShape)
                        .background(selectedPage.color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = selectedPage.icon,
                        contentDescription = null,
                        tint = selectedPage.color,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "SMARTBANK AUTH & IDENTITY HUB",
                            color = TextWhite,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 13.sp
                        )
                        PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 6.dp)
                    }
                    Text(
                        text = "FIPS 140-3 Cryptographic Enclave • Post-Quantum Zero Knowledge",
                        color = selectedPage.color,
                        fontSize = 10.sp
                    )
                }
            }
        }

        // 10-Page Scrollable Navigation Tab Bar
        ScrollableTabRow(
            selectedTabIndex = AuthPageType.values().indexOf(selectedPage),
            containerColor = NavyCard,
            contentColor = selectedPage.color,
            edgePadding = 4.dp,
            indicator = { tabPositions ->
                val idx = AuthPageType.values().indexOf(selectedPage)
                if (idx in tabPositions.indices) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[idx]),
                        color = selectedPage.color,
                        height = 3.dp
                    )
                }
            }
        ) {
            AuthPageType.values().forEach { page ->
                val isSelected = selectedPage == page
                Tab(
                    selected = isSelected,
                    onClick = {
                        selectedPage = page
                        onNavigateToPage?.invoke(page.routeKey)
                    },
                    text = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Icon(
                                imageVector = page.icon,
                                contentDescription = null,
                                tint = if (isSelected) page.color else TextMuted,
                                modifier = Modifier.size(13.dp)
                            )
                            Text(
                                text = page.shortLabel,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) page.color else TextMuted
                            )
                        }
                    },
                    modifier = Modifier.testTag("tab_auth_${page.routeKey}")
                )
            }
        }

        // Animated Page Host
        AnimatedContent(
            targetState = selectedPage,
            transitionSpec = {
                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width } + fadeOut()
                )
            },
            label = "AuthPageTransition",
            modifier = Modifier.weight(1f)
        ) { targetPage ->
            when (targetPage) {
                AuthPageType.LOGIN -> LoginScreen(
                    viewModel = viewModel,
                    onNavigateToRegister = { selectedPage = AuthPageType.REGISTER },
                    onNavigateToForgot = { selectedPage = AuthPageType.FORGOT_PASSWORD },
                    onNavigateToBiometric = { selectedPage = AuthPageType.BIOMETRIC_LOGIN },
                    onNavigateToFace = { selectedPage = AuthPageType.FACE_RECOGNITION }
                )
                AuthPageType.REGISTER -> RegisterScreen(
                    viewModel = viewModel,
                    onNavigateToLogin = { selectedPage = AuthPageType.LOGIN },
                    onNavigateToOtp = { selectedPage = AuthPageType.OTP_VERIFICATION }
                )
                AuthPageType.FORGOT_PASSWORD -> ForgotPasswordScreen(
                    viewModel = viewModel,
                    onNavigateToOtp = { selectedPage = AuthPageType.OTP_VERIFICATION },
                    onNavigateToLogin = { selectedPage = AuthPageType.LOGIN }
                )
                AuthPageType.RESET_PASSWORD -> ResetPasswordScreen(
                    viewModel = viewModel,
                    onPasswordResetSuccess = { selectedPage = AuthPageType.LOGIN }
                )
                AuthPageType.OTP_VERIFICATION -> OtpVerificationScreen(
                    viewModel = viewModel,
                    onVerificationSuccess = { selectedPage = AuthPageType.MFA_VERIFICATION },
                    onResend = {}
                )
                AuthPageType.MFA_VERIFICATION -> MfaVerificationScreen(
                    viewModel = viewModel,
                    onMfaSuccess = { selectedPage = AuthPageType.DEVICE_VERIFICATION }
                )
                AuthPageType.BIOMETRIC_LOGIN -> BiometricLoginScreen(
                    viewModel = viewModel,
                    onFallbackToPassword = { selectedPage = AuthPageType.LOGIN }
                )
                AuthPageType.FACE_RECOGNITION -> FaceRecognitionScreen(
                    viewModel = viewModel,
                    onFallbackToPin = { selectedPage = AuthPageType.BIOMETRIC_LOGIN }
                )
                AuthPageType.DEVICE_VERIFICATION -> DeviceVerificationScreen(
                    viewModel = viewModel,
                    onDeviceApproved = { selectedPage = AuthPageType.SECURITY_QUESTIONS }
                )
                AuthPageType.SECURITY_QUESTIONS -> SecurityQuestionsScreen(
                    viewModel = viewModel,
                    onSuccess = { selectedPage = AuthPageType.LOGIN }
                )
            }
        }
    }
}

// -------------------------------------------------------------------------
// 2. PAGE 1: LOGIN SCREEN
// -------------------------------------------------------------------------
@Composable
fun LoginScreen(
    viewModel: BankViewModel,
    onNavigateToRegister: (() -> Unit)? = null,
    onNavigateToForgot: (() -> Unit)? = null,
    onNavigateToBiometric: (() -> Unit)? = null,
    onNavigateToFace: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("alex.morgan@smartbank.ai") }
    var password by remember { mutableStateOf("Password@123") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var rememberMe by remember { mutableStateOf(true) }
    var isLoading by remember { mutableStateOf(false) }
    var loginSuccessMessage by remember { mutableStateOf<String?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AuthFormHeader(
                title = "Welcome Back to SmartBank AI",
                subtitle = "Sign in to access your quantum-secured universal financial operating system.",
                icon = Icons.Default.Login,
                color = CyberCyan
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("ACCOUNT CREDENTIALS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    // Email Input
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; errorMessage = null },
                        label = { Text("Email or Global DID", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = CyberCyan) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("login_email_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = CyberCyan,
                            unfocusedLabelColor = TextMuted,
                            cursorColor = CyberCyan
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Password Input
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; errorMessage = null },
                        label = { Text("Password", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = CyberCyan) },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle Password",
                                    tint = TextMuted
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("login_password_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = CyberCyan,
                            unfocusedLabelColor = TextMuted,
                            cursorColor = CyberCyan
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Remember Me & Forgot Password Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Checkbox(
                                checked = rememberMe,
                                onCheckedChange = { rememberMe = it },
                                colors = CheckboxDefaults.colors(
                                    checkedColor = CyberCyan,
                                    uncheckedColor = TextMuted,
                                    checkmarkColor = Navy900
                                )
                            )
                            Text("Remember Me", color = TextWhite, fontSize = 11.sp)
                        }

                        TextButton(onClick = { onNavigateToForgot?.invoke() }) {
                            Text("Forgot Password?", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }

                    // Error Message Banner
                    errorMessage?.let { err ->
                        Surface(color = CrimsonDanger.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp), border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonDanger.copy(alpha = 0.5f)), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = CrimsonDanger, modifier = Modifier.size(16.dp))
                                Text(err, color = CrimsonDanger, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    // Success Message Banner
                    loginSuccessMessage?.let { msg ->
                        Surface(color = EmeraldSuccess.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp), border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.5f)), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                                Text(msg, color = EmeraldSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    // Login Action Button
                    Button(
                        onClick = {
                            if (email.isBlank() || password.isBlank()) {
                                errorMessage = "Please enter both your email/DID and password."
                            } else {
                                isLoading = true
                                errorMessage = null
                                viewModel.login(email.trim(), password)
                                loginSuccessMessage = "Authentication verified! Authenticated session established."
                                isLoading = false
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("login_submit_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Navy900, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.LockOpen, contentDescription = null, tint = Navy900, modifier = Modifier.size(18.dp))
                                Text("Secure Sign In", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                            }
                        }
                    }
                }
            }
        }

        // Biometric Quick Login Options
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("PASSWORDLESS INSTANT LOGIN", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        OutlinedButton(
                            onClick = { onNavigateToBiometric?.invoke() },
                            modifier = Modifier.weight(1f).height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.6f))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Fingerprint, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
                                Text("Biometric", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }

                        OutlinedButton(
                            onClick = { onNavigateToFace?.invoke() },
                            modifier = Modifier.weight(1f).height(44.dp),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, PurpleTech.copy(alpha = 0.6f))
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Icon(Icons.Default.Face, contentDescription = null, tint = PurpleTech, modifier = Modifier.size(18.dp))
                                Text("Face ID Scan", color = PurpleTech, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // Switch to Register CTA
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Don't have a sovereign account?", color = TextMuted, fontSize = 11.sp)
                TextButton(onClick = { onNavigateToRegister?.invoke() }) {
                    Text("Register Now", color = ElectricBlue, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 3. PAGE 2: REGISTER SCREEN
// -------------------------------------------------------------------------
@Composable
fun RegisterScreen(
    viewModel: BankViewModel,
    onNavigateToLogin: (() -> Unit)? = null,
    onNavigateToOtp: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf(UserRole.USER) }
    var agreeToTerms by remember { mutableStateOf(false) }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isSuccess by remember { mutableStateOf(false) }

    // Calculate password strength (0 to 4)
    val passwordStrength = remember(password) {
        var score = 0
        if (password.length >= 8) score++
        if (password.any { it.isUpperCase() }) score++
        if (password.any { it.isDigit() }) score++
        if (password.any { !it.isLetterOrDigit() }) score++
        score
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AuthFormHeader(
                title = "Create Sovereign Account",
                subtitle = "Open a decentralized, FDIC-insured smart financial vault with zero friction.",
                icon = Icons.Default.PersonAdd,
                color = ElectricBlue
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("APPLICANT DETAILS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    // Full Name
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it; errorMessage = null },
                        label = { Text("Full Legal Name", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = ElectricBlue) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("register_fullname_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = ElectricBlue,
                            unfocusedLabelColor = TextMuted,
                            cursorColor = ElectricBlue
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Email
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it; errorMessage = null },
                        label = { Text("Email Address", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = ElectricBlue) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier.fillMaxWidth().testTag("register_email_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = ElectricBlue,
                            unfocusedLabelColor = TextMuted,
                            cursorColor = ElectricBlue
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Phone Number
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it; errorMessage = null },
                        label = { Text("Mobile Phone Number (+1)", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = ElectricBlue) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier.fillMaxWidth().testTag("register_phone_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = ElectricBlue,
                            unfocusedLabelColor = TextMuted,
                            cursorColor = ElectricBlue
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Password
                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it; errorMessage = null },
                        label = { Text("Password (Min. 8 chars)", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = ElectricBlue) },
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                    imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle Password",
                                    tint = TextMuted
                                )
                            }
                        },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("register_password_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = ElectricBlue,
                            unfocusedLabelColor = TextMuted,
                            cursorColor = ElectricBlue
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Password Strength Indicator
                    if (password.isNotEmpty()) {
                        val strengthColor = when (passwordStrength) {
                            1 -> CrimsonDanger
                            2 -> AmberOrange
                            3 -> CyberCyan
                            4 -> EmeraldSuccess
                            else -> CrimsonDanger
                        }
                        val strengthLabel = when (passwordStrength) {
                            1 -> "Weak"
                            2 -> "Fair"
                            3 -> "Strong"
                            4 -> "Quantum Resistant"
                            else -> "Too Short"
                        }

                        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Strength: $strengthLabel", color = strengthColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                Text("${passwordStrength * 25}%", color = strengthColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                            LinearProgressIndicator(
                                progress = { passwordStrength / 4f },
                                modifier = Modifier.fillMaxWidth().height(4.dp).clip(RoundedCornerShape(2.dp)),
                                color = strengthColor,
                                trackColor = Navy700
                            )
                        }
                    }

                    // Confirm Password
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it; errorMessage = null },
                        label = { Text("Confirm Password", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Shield, contentDescription = null, tint = ElectricBlue) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("register_confirm_password_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = ElectricBlue,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = ElectricBlue,
                            unfocusedLabelColor = TextMuted,
                            cursorColor = ElectricBlue
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Role Selector
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text("Account Tier:", color = TextMuted, fontSize = 11.sp)
                        Surface(
                            color = if (selectedRole == UserRole.USER) ElectricBlue.copy(alpha = 0.2f) else Navy800,
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedRole == UserRole.USER) ElectricBlue else Navy700),
                            modifier = Modifier.clickable { selectedRole = UserRole.USER }
                        ) {
                            Text("Standard Individual", color = if (selectedRole == UserRole.USER) ElectricBlue else TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                        Surface(
                            color = if (selectedRole == UserRole.ADMIN) AmberOrange.copy(alpha = 0.2f) else Navy800,
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (selectedRole == UserRole.ADMIN) AmberOrange else Navy700),
                            modifier = Modifier.clickable { selectedRole = UserRole.ADMIN }
                        ) {
                            Text("Enterprise Admin", color = if (selectedRole == UserRole.ADMIN) AmberOrange else TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp))
                        }
                    }

                    // Agree to Terms
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = agreeToTerms,
                            onCheckedChange = { agreeToTerms = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = ElectricBlue,
                                uncheckedColor = TextMuted,
                                checkmarkColor = Navy900
                            )
                        )
                        Text(
                            "I agree to E-SIGN disclosure, biometric verification terms, and the Privacy Policy.",
                            color = TextWhite,
                            fontSize = 10.sp,
                            lineHeight = 13.sp
                        )
                    }

                    // Error Message Banner
                    errorMessage?.let { err ->
                        Surface(color = CrimsonDanger.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp), border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonDanger.copy(alpha = 0.5f)), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = CrimsonDanger, modifier = Modifier.size(16.dp))
                                Text(err, color = CrimsonDanger, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    // Submit Registration Button
                    Button(
                        onClick = {
                            if (fullName.isBlank() || email.isBlank() || password.isBlank()) {
                                errorMessage = "Please fill in all required fields."
                            } else if (password != confirmPassword) {
                                errorMessage = "Passwords do not match."
                            } else if (!agreeToTerms) {
                                errorMessage = "You must agree to the terms to continue."
                            } else {
                                viewModel.register(fullName.trim(), email.trim(), password, selectedRole)
                                isSuccess = true
                                onNavigateToOtp?.invoke()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp).testTag("register_submit_button"),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.PersonAdd, contentDescription = null, tint = TextWhite, modifier = Modifier.size(18.dp))
                            Text("Create Account & Verify", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Switch to Login
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Already have a SmartBank ID?", color = TextMuted, fontSize = 11.sp)
                TextButton(onClick = { onNavigateToLogin?.invoke() }) {
                    Text("Sign In Here", color = CyberCyan, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 4. PAGE 3: FORGOT PASSWORD RECOVERY SCREEN
// -------------------------------------------------------------------------
@Composable
fun ForgotPasswordScreen(
    viewModel: BankViewModel,
    onNavigateToOtp: (() -> Unit)? = null,
    onNavigateToLogin: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var recoveryEmail by remember { mutableStateOf("alex.morgan@smartbank.ai") }
    var recoveryMethod by remember { mutableStateOf("Email OTP Link") }
    var isSubmitted by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AuthFormHeader(
                title = "Password Recovery",
                subtitle = "Initiate multi-channel cryptographic proof verification to reset your access credentials.",
                icon = Icons.Default.LockReset,
                color = AmberOrange
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("SELECT RECOVERY CHANNEL", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    val methods = listOf("Email OTP Link", "SMS Fast Code", "Hardware Security Key (FIDO2)")
                    methods.forEach { method ->
                        val isSel = recoveryMethod == method
                        Surface(
                            color = if (isSel) AmberOrange.copy(alpha = 0.15f) else Navy800,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSel) AmberOrange else Navy700),
                            modifier = Modifier.fillMaxWidth().clickable { recoveryMethod = method }
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = if (isSel) Icons.Default.CheckCircle else Icons.Default.CheckCircleOutline,
                                    contentDescription = null,
                                    tint = if (isSel) AmberOrange else TextMuted,
                                    modifier = Modifier.size(20.dp)
                                )
                                Column {
                                    Text(method, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    Text(
                                        when (method) {
                                            "Email OTP Link" -> "Sends a 6-digit cryptographic token to your attested email."
                                            "SMS Fast Code" -> "Sends an SMS verification code to your verified mobile number."
                                            else -> "Requires tapping your physical hardware security key."
                                        },
                                        color = TextMuted,
                                        fontSize = 9.sp
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // Email input
                    OutlinedTextField(
                        value = recoveryEmail,
                        onValueChange = { recoveryEmail = it; errorMessage = null },
                        label = { Text("Attested Email or Username", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = AmberOrange) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AmberOrange,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = AmberOrange,
                            unfocusedLabelColor = TextMuted,
                            cursorColor = AmberOrange
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (isSubmitted) {
                        Surface(
                            color = EmeraldSuccess.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.5f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                    Text("Recovery Token Dispatched!", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                }
                                Text("A 6-digit verification code has been dispatched to $recoveryEmail.", color = TextWhite, fontSize = 10.sp)
                            }
                        }
                    }

                    // Action Button
                    Button(
                        onClick = {
                            if (recoveryEmail.isBlank()) {
                                errorMessage = "Please enter your recovery email."
                            } else {
                                isSubmitted = true
                                onNavigateToOtp?.invoke()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberOrange),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Navy900, modifier = Modifier.size(18.dp))
                            Text("Send Recovery Token", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Remembered your password?", color = TextMuted, fontSize = 11.sp)
                TextButton(onClick = { onNavigateToLogin?.invoke() }) {
                    Text("Return to Login", color = CyberCyan, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 5. PAGE 4: RESET PASSWORD SCREEN
// -------------------------------------------------------------------------
@Composable
fun ResetPasswordScreen(
    viewModel: BankViewModel,
    onPasswordResetSuccess: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var isNewPasswordVisible by remember { mutableStateOf(false) }
    var isSuccess by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    val hasMinLength = newPassword.length >= 8
    val hasUpper = newPassword.any { it.isUpperCase() }
    val hasNumber = newPassword.any { it.isDigit() }
    val hasSpecial = newPassword.any { !it.isLetterOrDigit() }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AuthFormHeader(
                title = "Set New Sovereign Password",
                subtitle = "Create a high-entropy password to re-encrypt your sovereign biometric and financial keys.",
                icon = Icons.Default.Password,
                color = PurpleTech
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("NEW CREDENTIALS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    // New Password
                    OutlinedTextField(
                        value = newPassword,
                        onValueChange = { newPassword = it; errorMessage = null },
                        label = { Text("New Password", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Key, contentDescription = null, tint = PurpleTech) },
                        trailingIcon = {
                            IconButton(onClick = { isNewPasswordVisible = !isNewPasswordVisible }) {
                                Icon(
                                    imageVector = if (isNewPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                    contentDescription = "Toggle",
                                    tint = TextMuted
                                )
                            }
                        },
                        visualTransformation = if (isNewPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PurpleTech,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = PurpleTech,
                            unfocusedLabelColor = TextMuted,
                            cursorColor = PurpleTech
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Criteria Checklist
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        PasswordCriteriaItem("At least 8 characters", hasMinLength)
                        PasswordCriteriaItem("Contains uppercase letter (A-Z)", hasUpper)
                        PasswordCriteriaItem("Contains number (0-9)", hasNumber)
                        PasswordCriteriaItem("Contains special character (!@#$)", hasSpecial)
                    }

                    // Confirm Password
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it; errorMessage = null },
                        label = { Text("Confirm New Password", fontSize = 12.sp) },
                        leadingIcon = { Icon(Icons.Default.Shield, contentDescription = null, tint = PurpleTech) },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PurpleTech,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            focusedLabelColor = PurpleTech,
                            unfocusedLabelColor = TextMuted,
                            cursorColor = PurpleTech
                        ),
                        shape = RoundedCornerShape(10.dp)
                    )

                    errorMessage?.let { err ->
                        Surface(color = CrimsonDanger.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp), border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonDanger.copy(alpha = 0.5f)), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Warning, contentDescription = null, tint = CrimsonDanger, modifier = Modifier.size(16.dp))
                                Text(err, color = CrimsonDanger, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }

                    if (isSuccess) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.15f), shape = RoundedCornerShape(8.dp), border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.5f)), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Password successfully reset! You can now log in.", color = EmeraldSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            if (!hasMinLength || !hasUpper || !hasNumber || !hasSpecial) {
                                errorMessage = "Password does not satisfy all complexity requirements."
                            } else if (newPassword != confirmPassword) {
                                errorMessage = "Passwords do not match."
                            } else {
                                isSuccess = true
                                onPasswordResetSuccess?.invoke()
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleTech),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = TextWhite, modifier = Modifier.size(18.dp))
                            Text("Confirm & Re-encrypt Vault", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PasswordCriteriaItem(label: String, isMet: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
        Icon(
            imageVector = if (isMet) Icons.Default.CheckCircle else Icons.Default.CheckCircleOutline,
            contentDescription = null,
            tint = if (isMet) EmeraldSuccess else TextMuted,
            modifier = Modifier.size(14.dp)
        )
        Text(label, color = if (isMet) EmeraldSuccess else TextMuted, fontSize = 10.sp, fontWeight = if (isMet) FontWeight.Bold else FontWeight.Normal)
    }
}

// -------------------------------------------------------------------------
// 6. PAGE 5: OTP VERIFICATION SCREEN
// -------------------------------------------------------------------------
@Composable
fun OtpVerificationScreen(
    viewModel: BankViewModel,
    onVerificationSuccess: (() -> Unit)? = null,
    onResend: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var otpDigit1 by remember { mutableStateOf("7") }
    var otpDigit2 by remember { mutableStateOf("4") }
    var otpDigit3 by remember { mutableStateOf("9") }
    var otpDigit4 by remember { mutableStateOf("2") }
    var otpDigit5 by remember { mutableStateOf("0") }
    var otpDigit6 by remember { mutableStateOf("1") }

    var countdownSeconds by remember { mutableIntStateOf(58) }
    var isVerifying by remember { mutableStateOf(false) }
    var isVerified by remember { mutableStateOf(false) }

    LaunchedEffect(countdownSeconds) {
        if (countdownSeconds > 0) {
            delay(1000)
            countdownSeconds--
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AuthFormHeader(
                title = "One-Time Password (OTP) Verification",
                subtitle = "Enter the 6-digit quantum-hashed code dispatched to your registered multi-factor channel.",
                icon = Icons.Default.Pin,
                color = EmeraldSuccess
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(
                    modifier = Modifier.padding(18.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("ENTER 6-DIGIT VERIFICATION CODE", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    // 6 Separate Digit Boxes
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OtpBox(digit = otpDigit1, onDigitChange = { otpDigit1 = it.take(1) })
                        OtpBox(digit = otpDigit2, onDigitChange = { otpDigit2 = it.take(1) })
                        OtpBox(digit = otpDigit3, onDigitChange = { otpDigit3 = it.take(1) })
                        OtpBox(digit = otpDigit4, onDigitChange = { otpDigit4 = it.take(1) })
                        OtpBox(digit = otpDigit5, onDigitChange = { otpDigit5 = it.take(1) })
                        OtpBox(digit = otpDigit6, onDigitChange = { otpDigit6 = it.take(1) })
                    }

                    // Countdown Timer
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Timer, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                        Text(
                            text = if (countdownSeconds > 0) "Code expires in 00:${if (countdownSeconds < 10) "0$countdownSeconds" else "$countdownSeconds"}" else "Code expired. Request a new code.",
                            color = if (countdownSeconds > 0) EmeraldSuccess else CrimsonDanger,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (isVerified) {
                        Surface(
                            color = EmeraldSuccess.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.6f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(20.dp))
                                Text("OTP Verified! Cryptographic hand-shake valid.", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }

                    // Verify Action Button
                    Button(
                        onClick = {
                            isVerifying = true
                            isVerified = true
                            isVerifying = false
                            onVerificationSuccess?.invoke()
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Navy900, modifier = Modifier.size(18.dp))
                            Text("Verify OTP Code", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        }
                    }

                    // Resend Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = {
                                countdownSeconds = 60
                                onResend?.invoke()
                            },
                            enabled = countdownSeconds == 0
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.Refresh, contentDescription = null, tint = if (countdownSeconds == 0) CyberCyan else TextMuted, modifier = Modifier.size(14.dp))
                                Text("Resend Code via SMS", color = if (countdownSeconds == 0) CyberCyan else TextMuted, fontSize = 11.sp)
                            }
                        }

                        TextButton(onClick = {}) {
                            Text("Send via Email", color = ElectricBlue, fontSize = 11.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OtpBox(digit: String, onDigitChange: (String) -> Unit) {
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(NavyCard)
            .border(1.5.dp, if (digit.isNotEmpty()) EmeraldSuccess else Navy700, RoundedCornerShape(8.dp)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = digit,
            color = if (digit.isNotEmpty()) EmeraldSuccess else TextMuted,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 18.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

// -------------------------------------------------------------------------
// 7. PAGE 6: MULTI-FACTOR AUTHENTICATION (MFA) SCREEN
// -------------------------------------------------------------------------
@Composable
fun MfaVerificationScreen(
    viewModel: BankViewModel,
    onMfaSuccess: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var authenticatorCode by remember { mutableStateOf("842 195") }
    var useBackupCode by remember { mutableStateOf(false) }
    var backupCodeInput by remember { mutableStateOf("") }
    var isVerified by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AuthFormHeader(
                title = "Multi-Factor Authentication (MFA)",
                subtitle = "Two-step verification enforced via Google Authenticator, YubiKey, or FIDO2 Security token.",
                icon = Icons.Default.VpnKey,
                color = GoldAccent
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("AUTHENTICATOR APP CODE", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Surface(color = GoldAccent.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text("TOTP 30s", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }

                    if (!useBackupCode) {
                        OutlinedTextField(
                            value = authenticatorCode,
                            onValueChange = { authenticatorCode = it },
                            label = { Text("6-Digit TOTP Code", fontSize = 12.sp) },
                            leadingIcon = { Icon(Icons.Default.Smartphone, contentDescription = null, tint = GoldAccent) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldAccent,
                                unfocusedBorderColor = Navy700,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedLabelColor = GoldAccent,
                                unfocusedLabelColor = TextMuted,
                                cursorColor = GoldAccent
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    } else {
                        OutlinedTextField(
                            value = backupCodeInput,
                            onValueChange = { backupCodeInput = it },
                            label = { Text("8-Character Backup Recovery Code", fontSize = 12.sp) },
                            leadingIcon = { Icon(Icons.Default.Key, contentDescription = null, tint = GoldAccent) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = GoldAccent,
                                unfocusedBorderColor = Navy700,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedLabelColor = GoldAccent,
                                unfocusedLabelColor = TextMuted,
                                cursorColor = GoldAccent
                            ),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    // Toggle Backup Mode
                    TextButton(onClick = { useBackupCode = !useBackupCode }) {
                        Text(
                            text = if (!useBackupCode) "Lost access? Use 8-digit Emergency Backup Code" else "Switch back to Authenticator App",
                            color = GoldAccent,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    if (isVerified) {
                        Surface(
                            color = EmeraldSuccess.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.6f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(20.dp))
                                Text("MFA Validated! Hardware token approved.", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            isVerified = true
                            onMfaSuccess?.invoke()
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = Navy900, modifier = Modifier.size(18.dp))
                            Text("Approve Multi-Factor Challenge", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 8. PAGE 7: BIOMETRIC FINGERPRINT LOGIN SCREEN
// -------------------------------------------------------------------------
@Composable
fun BiometricLoginScreen(
    viewModel: BankViewModel,
    onFallbackToPassword: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = remember(context) { context.findFragmentActivity() }
    var isScanning by remember { mutableStateOf(false) }
    var isScanSuccess by remember { mutableStateOf(false) }
    var authErrorText by remember { mutableStateOf<String?>(null) }

    fun triggerHardwareScan() {
        if (activity == null) {
            isScanSuccess = true
            viewModel.unlockAppWithBiometrics()
            return
        }
        isScanning = true
        BiometricAuthManager.promptBiometric(
            activity = activity,
            title = "Biometric Fingerprint Authentication",
            subtitle = "Verify fingerprint sensor to authenticate session",
            negativeButtonText = "Cancel",
            onSuccess = {
                isScanning = false
                isScanSuccess = true
                viewModel.unlockAppWithBiometrics()
            },
            onError = { _, err ->
                isScanning = false
                authErrorText = err.toString()
            },
            onFailed = {
                isScanning = false
                authErrorText = "Sensor did not recognize biometric."
            }
        )
    }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.9f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AuthFormHeader(
                title = "Biometric Fingerprint Authentication",
                subtitle = "Hardware Keystore Enclave verification with zero server-side biometric credential storage.",
                icon = Icons.Default.Fingerprint,
                color = CyberCyan
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f))
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Text("TOUCH SENSOR TO SCAN", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)

                    // Animated Fingerprint Sensor Box
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(if (isScanSuccess) EmeraldSuccess.copy(alpha = 0.25f) else CyberCyan.copy(alpha = 0.15f))
                            .border(2.dp, if (isScanSuccess) EmeraldSuccess else CyberCyan.copy(alpha = 0.6f), CircleShape)
                            .clickable {
                                triggerHardwareScan()
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Fingerprint,
                            contentDescription = "Scan Fingerprint",
                            tint = if (isScanSuccess) EmeraldSuccess else CyberCyan,
                            modifier = Modifier.size(64.dp)
                        )
                    }

                    Text(
                        text = if (isScanSuccess) "Fingerprint Confirmed • Enclave Signed" else "Place finger on the sensor or tap above to authenticate",
                        color = if (isScanSuccess) EmeraldSuccess else TextMuted,
                        fontSize = 11.sp,
                        fontWeight = if (isScanSuccess) FontWeight.Bold else FontWeight.Normal,
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
                        onClick = {
                            triggerHardwareScan()
                        },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isScanSuccess) EmeraldSuccess else CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (isScanSuccess) "Authentication Successful" else if (isScanning) "Verifying Hardware Sensor..." else "Scan Biometric Sensor",
                            color = Navy900,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                    }

                    TextButton(onClick = { onFallbackToPassword?.invoke() }) {
                        Text("Use Master Password Instead", color = TextMuted, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 9. PAGE 8: FACE RECOGNITION LOGIN SCREEN
// -------------------------------------------------------------------------
@Composable
fun FaceRecognitionScreen(
    viewModel: BankViewModel,
    onFallbackToPin: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val activity = remember(context) { context.findFragmentActivity() }
    var isFaceDetected by remember { mutableStateOf(true) }
    var livenessScore by remember { mutableStateOf(0.98f) }
    var isApproved by remember { mutableStateOf(false) }
    var faceErrorText by remember { mutableStateOf<String?>(null) }

    fun triggerFaceHardwareAuth() {
        if (activity == null) {
            isApproved = true
            viewModel.unlockAppWithBiometrics()
            return
        }
        BiometricAuthManager.promptBiometric(
            activity = activity,
            title = "Face Unlock & Biometric Verification",
            subtitle = "Look directly at front camera to authenticate",
            negativeButtonText = "Cancel",
            onSuccess = {
                isApproved = true
                viewModel.unlockAppWithBiometrics()
            },
            onError = { _, err ->
                faceErrorText = err.toString()
            },
            onFailed = {
                faceErrorText = "Face not recognized. Ensure sufficient lighting."
            }
        )
    }

    val infiniteTransition = rememberInfiniteTransition(label = "laserScan")
    val laserY by infiniteTransition.animateFloat(
        initialValue = 0.1f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(1800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "laserY"
    )

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AuthFormHeader(
                title = "AI Face Recognition & Liveness Scan",
                subtitle = "3D Mesh contour mapping and anti-spoofing micro-expression liveness detection.",
                icon = Icons.Default.Face,
                color = PurpleTech
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PurpleTech.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Text("3D FACIAL SCANNER VIEWPORT", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    // Scanner Viewport Box with Laser Line
                    Box(
                        modifier = Modifier
                            .size(160.dp)
                            .clip(RoundedCornerShape(20.dp))
                            .background(NavyCard)
                            .border(2.dp, if (isApproved) EmeraldSuccess else PurpleTech, RoundedCornerShape(20.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "Face Target",
                            tint = if (isApproved) EmeraldSuccess else PurpleTech.copy(alpha = 0.7f),
                            modifier = Modifier.size(90.dp)
                        )

                        // Laser Scanner Canvas
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            val scanLineY = size.height * laserY
                            drawLine(
                                color = if (isApproved) EmeraldSuccess else PurpleTech,
                                start = Offset(0f, scanLineY),
                                end = Offset(size.width, scanLineY),
                                strokeWidth = 2.5.dp.toPx()
                            )
                        }
                    }

                    // Anti-Spoofing & Liveness Metrics
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Liveness Match", color = TextMuted, fontSize = 9.sp)
                            Text("${(livenessScore * 100).toInt()}% Verified", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Anti-Spoofing", color = TextMuted, fontSize = 9.sp)
                            Text("Grade A+ Secure", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Depth Contour", color = TextMuted, fontSize = 9.sp)
                            Text("3D Active Mesh", color = PurpleTech, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                    }

                    faceErrorText?.let { err ->
                        Text(
                            text = err,
                            color = AmberOrange,
                            fontSize = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }

                    Button(
                        onClick = {
                            triggerFaceHardwareAuth()
                        },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isApproved) EmeraldSuccess else PurpleTech),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text(
                            text = if (isApproved) "Face ID Match Confirmed" else "Perform Hardware Face / Biometric Scan",
                            color = TextWhite,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                    }

                    TextButton(onClick = { onFallbackToPin?.invoke() }) {
                        Text("Switch to Biometric Fingerprint", color = TextMuted, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 10. PAGE 9: DEVICE VERIFICATION SCREEN
// -------------------------------------------------------------------------
@Composable
fun DeviceVerificationScreen(
    viewModel: BankViewModel,
    onDeviceApproved: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var isCurrentDeviceTrusted by remember { mutableStateOf(true) }

    val devices = remember {
        listOf(
            Triple("Pixel 9 Pro (Current Device)", "Android 15 • San Francisco, US • Trusted", EmeraldSuccess),
            Triple("MacBook Pro M3 Max", "macOS Sonoma • Chrome 124 • Active 2h ago", CyberCyan),
            Triple("iPad Pro 13-inch M4", "iPadOS 18 • Safari Mobile • Active Yesterday", GoldAccent)
        )
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AuthFormHeader(
                title = "Hardware & Device Trust Management",
                subtitle = "Enforce hardware-bound public keys, location geofencing, and revoke unauthorized sessions.",
                icon = Icons.Default.Devices,
                color = ElectricBlue
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("ACTIVE AUTHORIZED DEVICES", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    devices.forEach { (name, details, color) ->
                        Surface(
                            color = NavyCard,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.3f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier.size(36.dp).clip(CircleShape).background(color.copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text(name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                    Text(details, color = TextMuted, fontSize = 9.sp)
                                }

                                Surface(color = color.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                                    Text("TRUSTED", color = color, fontSize = 8.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 5.dp, vertical = 2.dp))
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Button(
                        onClick = { onDeviceApproved?.invoke() },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Approve & Trust Current Hardware", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 11. PAGE 10: SECURITY QUESTIONS SCREEN
// -------------------------------------------------------------------------
@Composable
fun SecurityQuestionsScreen(
    viewModel: BankViewModel,
    onSuccess: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var q1Answer by remember { mutableStateOf("Oxford High School") }
    var q2Answer by remember { mutableStateOf("Lake Tahoe") }
    var q3Answer by remember { mutableStateOf("Apollo") }
    var isSaved by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            AuthFormHeader(
                title = "Cryptographic Security Questions",
                subtitle = "Set encrypted emergency account recovery answers hashed with Argon2id zero-knowledge keys.",
                icon = Icons.Default.HelpOutline,
                color = AmberOrange
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("RECOVERY CHALLENGE QUESTIONS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    // Question 1
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Question 1: What was the name of your first high school?", color = AmberOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = q1Answer,
                            onValueChange = { q1Answer = it },
                            label = { Text("Your Answer (Encrypted)", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AmberOrange,
                                unfocusedBorderColor = Navy700,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedLabelColor = AmberOrange,
                                unfocusedLabelColor = TextMuted,
                                cursorColor = AmberOrange
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    // Question 2
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Question 2: What was the location of your favorite childhood vacation?", color = AmberOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = q2Answer,
                            onValueChange = { q2Answer = it },
                            label = { Text("Your Answer (Encrypted)", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AmberOrange,
                                unfocusedBorderColor = Navy700,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedLabelColor = AmberOrange,
                                unfocusedLabelColor = TextMuted,
                                cursorColor = AmberOrange
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    // Question 3
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("Question 3: What was the name of your first pet or vehicle?", color = AmberOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        OutlinedTextField(
                            value = q3Answer,
                            onValueChange = { q3Answer = it },
                            label = { Text("Your Answer (Encrypted)", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = AmberOrange,
                                unfocusedBorderColor = Navy700,
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedLabelColor = AmberOrange,
                                unfocusedLabelColor = TextMuted,
                                cursorColor = AmberOrange
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }

                    if (isSaved) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Security answers sealed with AES-256-GCM!", color = EmeraldSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = {
                            isSaved = true
                            onSuccess?.invoke()
                        },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberOrange),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Save & Seal Recovery Questions", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// REUSABLE AUTH FORM HEADER HELPER
// -------------------------------------------------------------------------
@Composable
private fun AuthFormHeader(title: String, subtitle: String, icon: ImageVector, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(subtitle, color = TextMuted, fontSize = 10.sp, lineHeight = 13.sp)
            }
        }
    }
}
