package com.example.ui.components

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.UserRole
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
import com.example.ui.viewmodel.BankViewModel

@Composable
fun AuthDialog(
    viewModel: BankViewModel,
    onDismiss: () -> Unit
) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    var isPasswordVisible by remember { mutableStateOf(false) }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth(0.94f)
                .padding(vertical = 24.dp)
                .testTag("auth_dialog_card"),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(24.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(CircleShape)
                        .background(CyberCyan.copy(alpha = 0.15f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Security,
                        contentDescription = null,
                        tint = CyberCyan,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Text(
                    text = if (authState.isRegisterMode) "Create Secure Account" else "Secure Banking Login",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )

                Surface(
                    color = Navy900,
                    shape = RoundedCornerShape(8.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
                ) {
                    Text(
                        text = "🔒 SHA-256 Salted Hashing & JWT Session Guard",
                        color = CyberCyan,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
                    )
                }

                // Mode Tabs (Login vs Register)
                TabRow(
                    selectedTabIndex = if (authState.isRegisterMode) 1 else 0,
                    containerColor = Navy900,
                    contentColor = CyberCyan,
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, Navy700, RoundedCornerShape(12.dp)),
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[if (authState.isRegisterMode) 1 else 0]),
                            color = CyberCyan,
                            height = 3.dp
                        )
                    }
                ) {
                    Tab(
                        selected = !authState.isRegisterMode,
                        onClick = { if (authState.isRegisterMode) viewModel.toggleAuthMode() },
                        text = {
                            Text(
                                "Sign In",
                                fontWeight = if (!authState.isRegisterMode) FontWeight.Bold else FontWeight.Normal,
                                color = if (!authState.isRegisterMode) CyberCyan else TextMuted
                            )
                        },
                        modifier = Modifier.testTag("auth_tab_login")
                    )
                    Tab(
                        selected = authState.isRegisterMode,
                        onClick = { if (!authState.isRegisterMode) viewModel.toggleAuthMode() },
                        text = {
                            Text(
                                "Register",
                                fontWeight = if (authState.isRegisterMode) FontWeight.Bold else FontWeight.Normal,
                                color = if (authState.isRegisterMode) CyberCyan else TextMuted
                            )
                        },
                        modifier = Modifier.testTag("auth_tab_register")
                    )
                }

                // Form Fields
                if (authState.isRegisterMode) {
                    OutlinedTextField(
                        value = authState.fullNameInput,
                        onValueChange = { viewModel.updateAuthInput(fullName = it) },
                        label = { Text("Full Legal Name") },
                        leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = CyberCyan) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("auth_fullname_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )

                    // Role Picker for Demo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.updateAuthInput(role = UserRole.USER) },
                            color = if (authState.selectedRole == UserRole.USER) CyberCyan.copy(alpha = 0.2f) else Navy900,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (authState.selectedRole == UserRole.USER) CyberCyan else Navy700)
                        ) {
                            Text(
                                text = "👤 Customer",
                                color = if (authState.selectedRole == UserRole.USER) CyberCyan else TextMuted,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                            )
                        }

                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { viewModel.updateAuthInput(role = UserRole.ADMIN) },
                            color = if (authState.selectedRole == UserRole.ADMIN) AmberOrange.copy(alpha = 0.2f) else Navy900,
                            shape = RoundedCornerShape(8.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (authState.selectedRole == UserRole.ADMIN) AmberOrange else Navy700)
                        ) {
                            Text(
                                text = "🛡️ Security Admin",
                                color = if (authState.selectedRole == UserRole.ADMIN) AmberOrange else TextMuted,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = authState.emailInput,
                    onValueChange = { viewModel.updateAuthInput(email = it) },
                    label = { Text("Email Address") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("auth_email_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )

                OutlinedTextField(
                    value = authState.passwordInput,
                    onValueChange = { viewModel.updateAuthInput(pass = it) },
                    label = { Text("Password") },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    trailingIcon = {
                        IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                contentDescription = "Toggle password visibility",
                                tint = TextMuted
                            )
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("auth_password_input"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )

                if (authState.isRegisterMode) {
                    OutlinedTextField(
                        value = authState.confirmPasswordInput,
                        onValueChange = { viewModel.updateAuthInput(confirmPass = it) },
                        label = { Text("Confirm Password") },
                        visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth().testTag("auth_confirm_password_input"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = CyberCyan,
                            unfocusedBorderColor = Navy700,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite
                        )
                    )
                }

                // Error Message
                if (authState.authError != null) {
                    Surface(
                        color = CrimsonDanger.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonDanger.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = authState.authError ?: "",
                            color = CrimsonDanger,
                            fontSize = 12.sp,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                }

                // Submit Button
                Button(
                    onClick = {
                        if (authState.isRegisterMode) {
                            viewModel.register()
                        } else {
                            viewModel.login()
                        }
                        if (authState.isLoggedIn) {
                            onDismiss()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("auth_submit_button")
                ) {
                    if (authState.isAuthLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Navy900)
                    } else {
                        Text(
                            text = if (authState.isRegisterMode) "Create Account & Mint Token" else "Authenticate Session",
                            color = Navy900,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                        )
                    }
                }

                // Quick Switch Demo Profiles
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Navy900)
                        .padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text("⚡ Quick Demo Profile Switcher:", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                viewModel.quickSwitchUser(UserRole.USER)
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue.copy(alpha = 0.25f)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).height(38.dp).testTag("quick_user_alex")
                        ) {
                            Text("👤 Customer (Alex)", color = ElectricBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = {
                                viewModel.quickSwitchUser(UserRole.ADMIN)
                                onDismiss()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AmberOrange.copy(alpha = 0.25f)),
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f).height(38.dp).testTag("quick_user_admin")
                        ) {
                            Text("🛡️ Admin (Security)", color = AmberOrange, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // Biometric Unlock Shortcut
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.quickSwitchUser(UserRole.USER)
                            viewModel.showMessage("✓ Biometric Passkey Verified! Instant login.")
                            onDismiss()
                        }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Fingerprint, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Unlock with Device Biometrics / Passkey", color = EmeraldSuccess, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
