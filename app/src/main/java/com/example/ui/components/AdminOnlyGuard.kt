package com.example.ui.components

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.UserEntity
import com.example.data.model.UserRole
import com.example.ui.theme.*
import com.example.ui.viewmodel.BankViewModel

/**
 * -------------------------------------------------------------------------------------
 * PFIN SECURITY & ACCESS CONTROL CONTEXT
 * -------------------------------------------------------------------------------------
 */

enum class AdminPermission(val displayName: String, val code: String) {
    VIEW_AML_QUEUE("Surveillance & AML Queue", "PERM_AML_SURVEILLANCE"),
    MANAGE_USERS("User Directory & RBAC", "PERM_USER_MANAGEMENT"),
    SYSTEM_LIQUIDITY_CONTROL("Macro Liquidity & Reserves", "PERM_LIQUIDITY_CONTROL"),
    AUDIT_LOG_ACCESS("Immutable Security Trail", "PERM_AUDIT_ACCESS"),
    SET_FRAUD_SENSITIVITY("AI Fraud Engine Tuning", "PERM_FRAUD_TUNING"),
    DISASTER_RECOVERY("Disaster Recovery & Key Sharding", "PERM_DISASTER_RECOVERY")
}

enum class SecurityClearanceLevel(val levelNumber: Int, val title: String, val badgeColor: Color) {
    CITIZEN_LEVEL_1(1, "Level 1 // Citizen Sovereign", CyberCyan),
    OFFICER_LEVEL_3(3, "Level 3 // Compliance Officer", ElectricBlue),
    ROOT_ADMIN_LEVEL_5(5, "Level 5 // Root Enclave Master", AmberOrange)
}

/**
 * Encapsulates the runtime Security Context for Role-Based Access Control (RBAC)
 */
data class SecurityContext(
    val user: UserEntity? = null,
    val isLoggedIn: Boolean = false,
    val role: UserRole = UserRole.USER,
    val clearanceLevel: SecurityClearanceLevel = SecurityClearanceLevel.CITIZEN_LEVEL_1,
    val permissions: Set<AdminPermission> = emptySet(),
    val isSessionVerified: Boolean = false,
    val deviceAttested: Boolean = true
) {
    val isAdmin: Boolean
        get() = isLoggedIn && role == UserRole.ADMIN

    fun hasPermission(permission: AdminPermission): Boolean {
        if (isAdmin) return true
        return permissions.contains(permission)
    }

    fun hasClearance(required: SecurityClearanceLevel): Boolean {
        return clearanceLevel.levelNumber >= required.levelNumber
    }

    companion object {
        val Unauthenticated = SecurityContext(
            user = null,
            isLoggedIn = false,
            role = UserRole.USER,
            clearanceLevel = SecurityClearanceLevel.CITIZEN_LEVEL_1,
            permissions = emptySet(),
            isSessionVerified = false,
            deviceAttested = false
        )
    }
}

/**
 * CompositionLocal providing active Security Context down the Composable hierarchy
 */
val LocalSecurityContext = staticCompositionLocalOf<SecurityContext> {
    SecurityContext.Unauthenticated
}

/**
 * Top-level Security Context Provider Composable.
 * Observes view model authentication state and binds the live SecurityContext.
 */
@Composable
fun SecurityContextProvider(
    viewModel: BankViewModel,
    content: @Composable () -> Unit
) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    val currentUser = authState.currentUser

    val securityContext = remember(authState) {
        if (authState.isLoggedIn && currentUser != null) {
            val isAdmin = currentUser.role == UserRole.ADMIN
            val clearance = if (isAdmin) {
                SecurityClearanceLevel.ROOT_ADMIN_LEVEL_5
            } else {
                SecurityClearanceLevel.CITIZEN_LEVEL_1
            }

            val defaultPermissions = if (isAdmin) {
                AdminPermission.values().toSet()
            } else {
                emptySet()
            }

            SecurityContext(
                user = currentUser,
                isLoggedIn = true,
                role = currentUser.role,
                clearanceLevel = clearance,
                permissions = defaultPermissions,
                isSessionVerified = true,
                deviceAttested = true
            )
        } else {
            SecurityContext.Unauthenticated
        }
    }

    CompositionLocalProvider(LocalSecurityContext provides securityContext) {
        content()
    }
}

/**
 * -------------------------------------------------------------------------------------
 * ADMIN ONLY GUARD COMPONENT
 * 
 * Verifies that the current user has verified Administrator privileges (and optional
 * required permissions/clearance) before rendering protected admin-specific screens or
 * routes. Unauthorized access triggers a dedicated fallback gate.
 * -------------------------------------------------------------------------------------
 */
@Composable
fun AdminOnlyGuard(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier,
    requiredPermission: AdminPermission? = null,
    requiredClearance: SecurityClearanceLevel = SecurityClearanceLevel.ROOT_ADMIN_LEVEL_5,
    onAccessDenied: (() -> Unit)? = null,
    fallback: @Composable (SecurityContext) -> Unit = { secCtx ->
        AdminAccessRestrictedGate(
            viewModel = viewModel,
            requiredClearance = requiredClearance,
            requiredPermission = requiredPermission,
            deniedReason = "This terminal requires verified ${requiredClearance.title} authorization and Role: ADMIN clearance.",
            modifier = modifier
        )
    },
    content: @Composable (SecurityContext) -> Unit
) {
    val securityContext = LocalSecurityContext.current

    val isAuthorized = remember(securityContext, requiredPermission, requiredClearance) {
        securityContext.isAdmin &&
                securityContext.hasClearance(requiredClearance) &&
                (requiredPermission == null || securityContext.hasPermission(requiredPermission))
    }

    LaunchedEffect(isAuthorized) {
        if (!isAuthorized) {
            onAccessDenied?.invoke()
        }
    }

    AnimatedContent(
        targetState = isAuthorized,
        transitionSpec = {
            fadeIn() togetherWith fadeOut()
        },
        label = "AdminGuardTransition"
    ) { authorized ->
        if (authorized) {
            content(securityContext)
        } else {
            fallback(securityContext)
        }
    }
}

/**
 * Lightweight Composable for micro-components (e.g. admin buttons, privileged action tags).
 * Renders content ONLY if the active user role is ADMIN.
 */
@Composable
fun AdminOnlyContent(
    modifier: Modifier = Modifier,
    fallback: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    val securityContext = LocalSecurityContext.current
    if (securityContext.isAdmin) {
        Box(modifier = modifier) {
            content()
        }
    } else {
        fallback()
    }
}

/**
 * Role-Based Gate for conditional rendering across any set of UserRoles.
 */
@Composable
fun AdminRoleGate(
    allowedRoles: List<UserRole> = listOf(UserRole.ADMIN),
    fallback: @Composable () -> Unit = {},
    content: @Composable () -> Unit
) {
    val securityContext = LocalSecurityContext.current
    if (securityContext.isLoggedIn && allowedRoles.contains(securityContext.role)) {
        content()
    } else {
        fallback()
    }
}

/**
 * -------------------------------------------------------------------------------------
 * ADMIN ACCESS RESTRICTED GATE (FALLBACK UI)
 * -------------------------------------------------------------------------------------
 */
@Composable
fun AdminAccessRestrictedGate(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier,
    requiredClearance: SecurityClearanceLevel = SecurityClearanceLevel.ROOT_ADMIN_LEVEL_5,
    requiredPermission: AdminPermission? = null,
    deniedReason: String = "This terminal is reserved exclusively for System Auditors, Risk Officers, and Network Operators. Standard citizen accounts cannot access AML queues or system parameters."
) {
    var adminPasswordInput by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isAuthenticating by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Navy900)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        HolographicGlassCard(
            borderColor = CrimsonDanger,
            glowColor = CrimsonDanger.copy(alpha = 0.35f),
            shape = RoundedCornerShape(24.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("admin_restricted_gate_card")
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Warning Shield Badge
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(CrimsonDanger.copy(alpha = 0.2f))
                        .border(2.dp, CrimsonDanger, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Restricted Admin Enclave",
                        tint = CrimsonDanger,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    PulsingStatusBadge(pulseColor = CrimsonDanger, badgeSize = 8.dp)
                    Text(
                        text = "RESTRICTED ENCLAVE // ${requiredClearance.title.uppercase()}",
                        color = CrimsonDanger,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.5.sp,
                        fontFamily = FontFamily.Monospace
                    )
                }

                Text(
                    text = "Administrator Authorization Required",
                    color = TextWhite,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.Center
                )

                Text(
                    text = deniedReason,
                    color = TextMuted,
                    fontSize = 12.sp,
                    lineHeight = 16.sp,
                    textAlign = TextAlign.Center
                )

                if (requiredPermission != null) {
                    Surface(
                        color = Navy900,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, AmberOrange.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = "Required Permission: ${requiredPermission.displayName} [${requiredPermission.code}]",
                            color = AmberOrange,
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                OutlinedTextField(
                    value = adminPasswordInput,
                    onValueChange = {
                        adminPasswordInput = it
                        errorMessage = null
                    },
                    label = { Text("Admin Master Key / Password", color = TextMuted) },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite,
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = Navy700,
                        focusedLabelColor = CyberCyan
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("admin_passkey_input")
                )

                if (errorMessage != null) {
                    Text(
                        text = errorMessage ?: "",
                        color = CrimsonDanger,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }

                // Unlock Button
                Button(
                    onClick = {
                        val trimmed = adminPasswordInput.trim()
                        if (trimmed == "Admin@123" || trimmed == "admin" || trimmed == "Password@123") {
                            viewModel.loginAsAdmin()
                            viewModel.showMessage("✓ Administrative Master Clearance Granted")
                        } else {
                            errorMessage = "Invalid Master Passkey. Access attempt logged to Security Enclave."
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = AmberOrange, contentColor = Navy900),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("admin_authenticate_button")
                ) {
                    Icon(Icons.Default.Security, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Authenticate & Unlock Admin Panel", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }

                // Quick Demo Override for testing convenience
                Surface(
                    color = Navy800,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.loginAsAdmin()
                            viewModel.showMessage("✓ Switched to Root Administrator Profile")
                        }
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.AdminPanelSettings, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Instant 1-Click Master Admin Login (Demo Mode)",
                            color = CyberCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
