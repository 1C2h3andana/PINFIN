package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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

enum class DigitalIdentityPageType(
    val title: String,
    val shortLabel: String,
    val icon: ImageVector,
    val color: Color,
    val routeKey: String
) {
    UNIVERSAL_ID("Universal Digital Identity", "Universal DID", Icons.Default.Fingerprint, CyberCyan, "did_universal"),
    IDENTITY_VERIFICATION("Identity Verification", "Verification", Icons.Default.VerifiedUser, EmeraldSuccess, "did_verification"),
    DIGITAL_SIGNATURE("Digital Signature Suite", "Signature", Icons.Default.Draw, PurpleTech, "did_signature"),
    PASSPORT_VERIFY("Passport Verification (eMRZ)", "Passport", Icons.Default.Badge, GoldAccent, "did_passport"),
    DRIVER_LICENSE("Driver License (AAMVA)", "Driver Lic", Icons.Default.Description, ElectricBlue, "did_driver_license"),
    NATIONAL_ID("National ID & zk-Proof", "National ID", Icons.Default.Shield, CyberCyan, "did_national_id"),
    KYC_VERIFICATION("Comprehensive KYC / AML", "KYC / AML", Icons.Default.Security, AmberOrange, "did_kyc"),
    FACE_MATCHING("3D Liveness & Face Matching", "Face Match", Icons.Default.Face, EmeraldSuccess, "did_face_matching"),
    IDENTITY_HISTORY("Immutable Identity Audit Log", "Audit History", Icons.Default.History, PurpleTech, "did_history")
}

// -------------------------------------------------------------------------
// MASTER DIGITAL IDENTITY HUB (9-Page Switcher)
// -------------------------------------------------------------------------
@Composable
fun DigitalIdentityHubScreen(
    viewModel: BankViewModel,
    initialPage: DigitalIdentityPageType = DigitalIdentityPageType.UNIVERSAL_ID,
    onNavigateToPage: ((String) -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var selectedPage by remember { mutableStateOf(initialPage) }
    val authState by viewModel.authState.collectAsStateWithLifecycle()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // Master Digital Identity Card
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
                        .size(46.dp)
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
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = authState.currentUser?.fullName ?: "Sovereign Citizen",
                            color = TextWhite,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                        PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 6.dp)
                    }
                    Text(
                        text = "W3C Universal DID • did:ion:smartbank:${authState.currentUser?.id ?: 1} • Level 3 KYC",
                        color = selectedPage.color,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // 9-Tab Scrollable Bar
        ScrollableTabRow(
            selectedTabIndex = DigitalIdentityPageType.values().indexOf(selectedPage),
            containerColor = NavyCard,
            contentColor = selectedPage.color,
            edgePadding = 4.dp,
            indicator = { tabPositions ->
                val idx = DigitalIdentityPageType.values().indexOf(selectedPage)
                if (idx in tabPositions.indices) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[idx]),
                        color = selectedPage.color,
                        height = 3.dp
                    )
                }
            }
        ) {
            DigitalIdentityPageType.values().forEach { page ->
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
                    modifier = Modifier.testTag("tab_did_${page.routeKey}")
                )
            }
        }

        // Animated Screen Content Switcher
        AnimatedContent(
            targetState = selectedPage,
            transitionSpec = {
                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width } + fadeOut()
                )
            },
            label = "DigitalIdPageTransition",
            modifier = Modifier.weight(1f)
        ) { targetPage ->
            when (targetPage) {
                DigitalIdentityPageType.UNIVERSAL_ID -> UniversalDigitalIdentityScreen(viewModel = viewModel)
                DigitalIdentityPageType.IDENTITY_VERIFICATION -> IdentityVerificationCenterScreen(viewModel = viewModel)
                DigitalIdentityPageType.DIGITAL_SIGNATURE -> DigitalSignatureSuiteScreen(viewModel = viewModel)
                DigitalIdentityPageType.PASSPORT_VERIFY -> PassportVerificationScreen(viewModel = viewModel)
                DigitalIdentityPageType.DRIVER_LICENSE -> DriverLicenseVerificationScreen(viewModel = viewModel)
                DigitalIdentityPageType.NATIONAL_ID -> NationalIdVerificationScreen(viewModel = viewModel)
                DigitalIdentityPageType.KYC_VERIFICATION -> KycVerificationFlowScreen(viewModel = viewModel)
                DigitalIdentityPageType.FACE_MATCHING -> FaceMatchingBiometricScreen(viewModel = viewModel)
                DigitalIdentityPageType.IDENTITY_HISTORY -> IdentityHistoryAuditScreen(viewModel = viewModel)
            }
        }
    }
}

// -------------------------------------------------------------------------
// 1. UNIVERSAL DIGITAL IDENTITY SCREEN
// -------------------------------------------------------------------------
@Composable
fun UniversalDigitalIdentityScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    var didMethod by remember { mutableStateOf("did:ion:EiA902k4...vL91QuantumSecp256k1") }
    var sovereignAlias by remember { mutableStateOf("alex.vance.vault.id") }
    var recoveryKeyHash by remember { mutableStateOf("0x9F42...88AC4B1D9902E") }
    var isKeyRotated by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            DidSectionHeader(
                title = "Universal Sovereign Digital Identity (DID)",
                subtitle = "Decentralized identifier anchored on cryptographic ledger, verifiable credentials & biometric root of trust.",
                icon = Icons.Default.Fingerprint,
                color = CyberCyan
            )
        }

        // Active DID Specifications Card
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("ACTIVE W3C DID DOCUMENT", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(didMethod, color = CyberCyan, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = FontWeight.Bold)
                    
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Root Key: Ed25519 / Secp256k1", color = TextWhite, fontSize = 10.sp)
                        Text("Ledger: Sovereign Quantum DAG", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        // APPLICATION FORM: DID Management & Key Rotation
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("SOVEREIGN DID REGISTRATION & KEY ROTATION", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = sovereignAlias,
                        onValueChange = { sovereignAlias = it },
                        label = { Text("Human-Readable DID Alias (.vault.id)", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = didTextFieldColors(CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = recoveryKeyHash,
                        onValueChange = { recoveryKeyHash = it },
                        label = { Text("Social Recovery Key Fingerprint", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = didTextFieldColors(CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (isKeyRotated) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Universal DID document rotated & signed with hardware enclave root key!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isKeyRotated = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Rotate Master Sovereign Root Key", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 2. IDENTITY VERIFICATION SCREEN
// -------------------------------------------------------------------------
@Composable
fun IdentityVerificationCenterScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var verificationTier by remember { mutableStateOf("Tier 3 - Sovereign Institutional Attested") }
    var auditorOrg by remember { mutableStateOf("KPMG / ISO-27001 Cryptographic Identity Registry") }
    var isReverified by remember { mutableStateOf(false) }

    val tiers = listOf(
        Triple("Tier 1: Basic Electronic KYC", "Phone, Email & Biometric Hash Verified", EmeraldSuccess),
        Triple("Tier 2: Government RealID & AML", "Passport & DMV OCR Attested ($100k Limit)", EmeraldSuccess),
        Triple("Tier 3: Sovereign Institutional", "Quantum Zero-Knowledge Multi-Sig (Unlimited)", CyberCyan)
    )

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            DidSectionHeader(
                title = "Identity Verification Center",
                subtitle = "Real-time compliance attestation, AML/CTF check levels, and tiered transaction clearances.",
                icon = Icons.Default.VerifiedUser,
                color = EmeraldSuccess
            )
        }

        // Tier Levels
        item {
            Text("VERIFICATION LEVELS & CLEARANCES", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                tiers.forEach { (title, subtitle, color) ->
                    Surface(
                        color = NavyCard,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(modifier = Modifier.size(34.dp).clip(CircleShape).background(color.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text(subtitle, color = TextMuted, fontSize = 9.sp)
                            }
                        }
                    }
                }
            }
        }

        // APPLICATION FORM: Request Verification Level Upgrade
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("REQUEST CLEARANCE LEVEL UPGRADE", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = verificationTier,
                        onValueChange = { verificationTier = it },
                        label = { Text("Target Clearance Tier", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = didTextFieldColors(EmeraldSuccess),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = auditorOrg,
                        onValueChange = { auditorOrg = it },
                        label = { Text("Designated Audit Authority", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = didTextFieldColors(EmeraldSuccess),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (isReverified) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Verification dossier compiled and sent to institutional attestation node!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isReverified = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Submit Identity Verification Dossier", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 3. DIGITAL SIGNATURE SUITE SCREEN
// -------------------------------------------------------------------------
@Composable
fun DigitalSignatureSuiteScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var documentTitle by remember { mutableStateOf("Global Wire Transfer Authorization #8492-QT") }
    var signatureAlgorithm by remember { mutableStateOf("Post-Quantum Dilithium-5 / Ed25519") }
    var signerMemo by remember { mutableStateOf("Authorized by Sovereign Vault Controller Vance") }
    var isSigned by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            DidSectionHeader(
                title = "Cryptographic Digital Signature Suite",
                subtitle = "FIPS 204 Post-Quantum Dilithium & RSA-4096 signing engine with verifiable timestamping.",
                icon = Icons.Default.Draw,
                color = PurpleTech
            )
        }

        // APPLICATION FORM: Digital Document Signing
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("SIGN ARTIFACT WITH SECURE HARDWARE ENCLAVE", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = documentTitle,
                        onValueChange = { documentTitle = it },
                        label = { Text("Document / Transaction Subject", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = didTextFieldColors(PurpleTech),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = signatureAlgorithm,
                        onValueChange = { signatureAlgorithm = it },
                        label = { Text("Signature Cryptographic Standard", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = didTextFieldColors(PurpleTech),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = signerMemo,
                        onValueChange = { signerMemo = it },
                        label = { Text("Signer Statement Memo", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = didTextFieldColors(PurpleTech),
                        shape = RoundedCornerShape(10.dp)
                    )

                    // Simulated Signature Visualizer Canvas
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(Navy800)
                            .border(1.dp, PurpleTech.copy(alpha = 0.5f), RoundedCornerShape(10.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isSigned) "✓ SIGNED: SHA256(0x9482...A94E) • Dilithium-5 Enclave Proof" else "Hardware Enclave Signature Pad Ready",
                            color = if (isSigned) EmeraldSuccess else TextMuted,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (isSigned) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Cryptographic signature generated and anchored to immutable audit log!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isSigned = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleTech),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Sign Document with Hardware Enclave Key", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 4. PASSPORT VERIFICATION SCREEN
// -------------------------------------------------------------------------
@Composable
fun PassportVerificationScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var passportNumber by remember { mutableStateOf("P982348102") }
    var issuingCountry by remember { mutableStateOf("USA - United States of America") }
    var mrzLine1 by remember { mutableStateOf("P<USAVANCE<<ALEXANDER<<<<<<<<<<<<<<<<<<") }
    var mrzLine2 by remember { mutableStateOf("P9823481023USA9206154M3211208<<<<<<<4") }
    var isPassportVerified by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            DidSectionHeader(
                title = "ICAO 9303 ePassport Verification",
                subtitle = "Biometric passport chip reading, Machine Readable Zone (MRZ) OCR, and Public Key Directory (PKD) checks.",
                icon = Icons.Default.Badge,
                color = GoldAccent
            )
        }

        // APPLICATION FORM: ePassport Scanning & Verification
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("EPASSPORT MRZ & BIOMETRIC DATA", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = passportNumber,
                            onValueChange = { passportNumber = it },
                            label = { Text("Passport Number", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = didTextFieldColors(GoldAccent),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = issuingCountry,
                            onValueChange = { issuingCountry = it },
                            label = { Text("Issuing Jurisdiction", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1.2f),
                            colors = didTextFieldColors(GoldAccent),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    OutlinedTextField(
                        value = mrzLine1,
                        onValueChange = { mrzLine1 = it },
                        label = { Text("MRZ Machine Line 1", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = didTextFieldColors(GoldAccent),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = mrzLine2,
                        onValueChange = { mrzLine2 = it },
                        label = { Text("MRZ Machine Line 2", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = didTextFieldColors(GoldAccent),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (isPassportVerified) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("ePassport PKD cryptographic check passed! Biometric NFC valid.", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isPassportVerified = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Verify ePassport with ICAO PKD Master List", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 5. DRIVER LICENSE VERIFICATION SCREEN
// -------------------------------------------------------------------------
@Composable
fun DriverLicenseVerificationScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var licenseNumber by remember { mutableStateOf("DL-9482-1092-NY") }
    var stateCode by remember { mutableStateOf("New York (NY)") }
    var isRealIdCompliant by remember { mutableStateOf(true) }
    var isLicenseVerified by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            DidSectionHeader(
                title = "Driver's License & RealID Verification",
                subtitle = "AAMVA PDF417 barcode decoding, DMV state database lookups, and RealID gold star compliance.",
                icon = Icons.Default.Description,
                color = ElectricBlue
            )
        }

        // APPLICATION FORM: Driver License Verification
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("STATE DMV & AAMVA REALID DETAILS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = licenseNumber,
                        onValueChange = { licenseNumber = it },
                        label = { Text("Driver License Identification Number", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = didTextFieldColors(ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = stateCode,
                        onValueChange = { stateCode = it },
                        label = { Text("Issuing State / Jurisdiction", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = didTextFieldColors(ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("RealID Gold Star Compliant", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text("Federal TSA & high-tier bank clearance recognized", color = TextMuted, fontSize = 9.sp)
                        }
                        Switch(
                            checked = isRealIdCompliant,
                            onCheckedChange = { isRealIdCompliant = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Navy900, checkedTrackColor = ElectricBlue)
                        )
                    }

                    if (isLicenseVerified) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("DMV RealID valid! Address and full legal name matched.", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isLicenseVerified = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Validate Driver License with State Registry", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 6. NATIONAL ID VERIFICATION SCREEN
// -------------------------------------------------------------------------
@Composable
fun NationalIdVerificationScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var nationalIdType by remember { mutableStateOf("Social Security Number (SSN) / EU eID") }
    var nationalIdNumber by remember { mutableStateOf("•••-••-8492") }
    var isZkProofGenerated by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            DidSectionHeader(
                title = "National ID & Zero-Knowledge Verification",
                subtitle = "Cryptographic identity verification with zero plaintext disclosure of raw social security numbers.",
                icon = Icons.Default.Shield,
                color = CyberCyan
            )
        }

        // APPLICATION FORM: National ID ZK Proof
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("NATIONAL REGISTRY & ZKP CREDENTIAL", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = nationalIdType,
                        onValueChange = { nationalIdType = it },
                        label = { Text("National Identification System", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = didTextFieldColors(CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = nationalIdNumber,
                        onValueChange = { nationalIdNumber = it },
                        label = { Text("National ID / Tax Token", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = didTextFieldColors(CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (isZkProofGenerated) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("ZKP Proof verified! IRS/SSA validation confirmed without storing raw SSN.", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isZkProofGenerated = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Generate Zero-Knowledge National ID Attestation", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 7. KYC VERIFICATION SCREEN
// -------------------------------------------------------------------------
@Composable
fun KycVerificationFlowScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var pepStatus by remember { mutableStateOf("Non-PEP (Politically Exposed Person: No)") }
    var sourceOfFunds by remember { mutableStateOf("Employment Compensation & Venture Investments") }
    var isKycCompleted by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            DidSectionHeader(
                title = "Comprehensive KYC & AML Screening",
                subtitle = "Automated OFAC sanction checks, PEP screening, and source-of-wealth compliance dossiers.",
                icon = Icons.Default.Security,
                color = AmberOrange
            )
        }

        // APPLICATION FORM: KYC Verification
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("AML / CTF RISK PROFILING", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = pepStatus,
                        onValueChange = { pepStatus = it },
                        label = { Text("PEP Screening Declaration", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = didTextFieldColors(AmberOrange),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = sourceOfFunds,
                        onValueChange = { sourceOfFunds = it },
                        label = { Text("Primary Source of Wealth / Funds", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = didTextFieldColors(AmberOrange),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (isKycCompleted) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Global OFAC & PEP check passed: Risk score 0.02 (Ultra Low Risk)!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isKycCompleted = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberOrange),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Execute Real-Time KYC / AML Clearance", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 8. FACE MATCHING SCREEN
// -------------------------------------------------------------------------
@Composable
fun FaceMatchingBiometricScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var matchConfidence by remember { mutableFloatStateOf(99.8f) }
    var isScanRunning by remember { mutableStateOf(false) }
    var isMatched by remember { mutableStateOf(true) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            DidSectionHeader(
                title = "3D Liveness & Biometric Face Matching",
                subtitle = "128-point neural vector facial matching against government passport and driver's license photos.",
                icon = Icons.Default.Face,
                color = EmeraldSuccess
            )
        }

        // Biometric Scanner Radar Box
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.5f))
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(120.dp)
                            .clip(CircleShape)
                            .background(EmeraldSuccess.copy(alpha = 0.15f))
                            .border(2.dp, EmeraldSuccess, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Face, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(64.dp))
                    }

                    Text("3D LIVENESS: PASS (128 VECTORS MATCHED)", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    Text("Passport Face Vector vs Live Hardware Scan: 99.84% Confidence", color = EmeraldSuccess, fontSize = 10.sp)

                    Button(
                        onClick = { isMatched = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Re-Run 3D Facial Liveness Detection", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 9. IDENTITY HISTORY AUDIT SCREEN
// -------------------------------------------------------------------------
@Composable
fun IdentityHistoryAuditScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val auditLogs = listOf(
        Triple("Zero-Knowledge Age Verification", "Verifier: Quantum Securities • Proof Verified", EmeraldSuccess),
        Triple("ICAO ePassport Master PKD Check", "Status: Pass • PKD Version 2026.08", GoldAccent),
        Triple("FIDO2 Hardware Key Rotation", "Rotated root Secp256k1 enclave key", CyberCyan),
        Triple("DMV RealID Barcode Attestation", "Matched state DL record with 100% parity", ElectricBlue),
        Triple("KYC Tier 3 Clearance Granted", "Institutional unlimited wire clearance authorized", EmeraldSuccess)
    )

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            DidSectionHeader(
                title = "Immutable Identity History & Audit Log",
                subtitle = "Cryptographically verifiable ledger of all identity presentations, audits, and key assertions.",
                icon = Icons.Default.History,
                color = PurpleTech
            )
        }

        item {
            Text("SOVEREIGN AUDIT TRAIL", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                auditLogs.forEach { (action, detail, color) ->
                    Surface(
                        color = NavyCard,
                        shape = RoundedCornerShape(10.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(modifier = Modifier.size(34.dp).clip(CircleShape).background(color.copy(alpha = 0.2f)), contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(action, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text(detail, color = TextMuted, fontSize = 9.sp)
                            }
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// REUSABLE DID HELPERS
// -------------------------------------------------------------------------
@Composable
private fun DidSectionHeader(title: String, subtitle: String, icon: ImageVector, color: Color) {
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

@Composable
private fun didTextFieldColors(activeColor: Color) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = activeColor,
    unfocusedBorderColor = Navy700,
    focusedTextColor = TextWhite,
    unfocusedTextColor = TextWhite,
    focusedLabelColor = activeColor,
    unfocusedLabelColor = TextMuted
)
