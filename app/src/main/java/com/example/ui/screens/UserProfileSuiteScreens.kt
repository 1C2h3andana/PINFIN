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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddModerator
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloudUpload
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Devices
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.FamilyRestroom
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.UploadFile
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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

enum class UserProfilePageType(
    val title: String,
    val shortLabel: String,
    val icon: ImageVector,
    val color: Color,
    val routeKey: String
) {
    PERSONAL_INFO("Personal Information", "Personal", Icons.Default.AccountCircle, CyberCyan, "profile_personal"),
    DIGITAL_IDENTITY("Digital Sovereign Identity (DID)", "Digital ID", Icons.Default.Fingerprint, PurpleTech, "profile_digital_id"),
    DOCUMENTS("Verified Documents & KYC", "Documents", Icons.Default.Badge, EmeraldSuccess, "profile_documents"),
    EMPLOYMENT("Employment & Income Proof", "Employment", Icons.Default.Work, ElectricBlue, "profile_employment"),
    EDUCATION("Academic & Credentials", "Education", Icons.Default.School, GoldAccent, "profile_education"),
    FAMILY("Family & Beneficiaries", "Family", Icons.Default.FamilyRestroom, AmberOrange, "profile_family"),
    ADDRESS("Address Management", "Addresses", Icons.Default.Home, CyberCyan, "profile_address"),
    CONTACT("Contact Channels & PGP", "Contact", Icons.Default.Phone, ElectricBlue, "profile_contact"),
    TRUSTED_DEVICES("Trusted Hardware & Keys", "Devices", Icons.Default.Devices, PurpleTech, "profile_devices"),
    PRIVACY_SETTINGS("Privacy & ZK Disclosures", "Privacy", Icons.Default.Shield, CrimsonDanger, "profile_privacy")
}

// -------------------------------------------------------------------------
// MASTER USER PROFILE HUB (10-Page Switcher)
// -------------------------------------------------------------------------
@Composable
fun UserProfileHubScreen(
    viewModel: BankViewModel,
    initialPage: UserProfilePageType = UserProfilePageType.PERSONAL_INFO,
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
        // Master Profile Summary Card
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
                            text = authState.currentUser?.fullName ?: "Sovereign Vault Citizen",
                            color = TextWhite,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 14.sp
                        )
                        PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 6.dp)
                    }
                    Text(
                        text = "KYC Level 3 Attested • DID: did:ion:smartbank:${authState.currentUser?.id ?: 1}",
                        color = selectedPage.color,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        // 10-Tab Scrollable Bar
        ScrollableTabRow(
            selectedTabIndex = UserProfilePageType.values().indexOf(selectedPage),
            containerColor = NavyCard,
            contentColor = selectedPage.color,
            edgePadding = 4.dp,
            indicator = { tabPositions ->
                val idx = UserProfilePageType.values().indexOf(selectedPage)
                if (idx in tabPositions.indices) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[idx]),
                        color = selectedPage.color,
                        height = 3.dp
                    )
                }
            }
        ) {
            UserProfilePageType.values().forEach { page ->
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
                    modifier = Modifier.testTag("tab_profile_${page.routeKey}")
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
            label = "ProfilePageTransition",
            modifier = Modifier.weight(1f)
        ) { targetPage ->
            when (targetPage) {
                UserProfilePageType.PERSONAL_INFO -> PersonalInformationScreen(viewModel = viewModel)
                UserProfilePageType.DIGITAL_IDENTITY -> DigitalIdentityProfileScreen(viewModel = viewModel)
                UserProfilePageType.DOCUMENTS -> DocumentsVaultScreen(viewModel = viewModel)
                UserProfilePageType.EMPLOYMENT -> EmploymentInformationScreen(viewModel = viewModel)
                UserProfilePageType.EDUCATION -> EducationProfileScreen(viewModel = viewModel)
                UserProfilePageType.FAMILY -> FamilyInformationScreen(viewModel = viewModel)
                UserProfilePageType.ADDRESS -> AddressManagementScreen(viewModel = viewModel)
                UserProfilePageType.CONTACT -> ContactInformationScreen(viewModel = viewModel)
                UserProfilePageType.TRUSTED_DEVICES -> TrustedDevicesScreen(viewModel = viewModel)
                UserProfilePageType.PRIVACY_SETTINGS -> PrivacySettingsScreen(viewModel = viewModel)
            }
        }
    }
}

// -------------------------------------------------------------------------
// 1. PERSONAL INFORMATION SCREEN
// -------------------------------------------------------------------------
@Composable
fun PersonalInformationScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    var fullName by remember { mutableStateOf(authState.currentUser?.fullName ?: "Alexander Vance") }
    var preferredName by remember { mutableStateOf("Alex") }
    var dob by remember { mutableStateOf("1992-06-15") }
    var nationality by remember { mutableStateOf("United States / EU Sovereign") }
    var taxId by remember { mutableStateOf("•••-••-8492") }
    var bio by remember { mutableStateOf("FinTech Architect & Quantum Liquidity Researcher.") }
    var isSaved by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            ProfileSectionHeader(
                title = "Personal Sovereign Identity",
                subtitle = "Cryptographically bound personal profile, legal identity verification, and tax residency.",
                icon = Icons.Default.AccountCircle,
                color = CyberCyan
            )
        }

        // APPLICATION FORM: Personal Information
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("LEGAL IDENTITY & BIOGRAPHICAL DATA", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Legal Name", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = preferredName,
                            onValueChange = { preferredName = it },
                            label = { Text("Preferred Name", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = profileTextFieldColors(CyberCyan),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = dob,
                            onValueChange = { dob = it },
                            label = { Text("Date of Birth", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = profileTextFieldColors(CyberCyan),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    OutlinedTextField(
                        value = nationality,
                        onValueChange = { nationality = it },
                        label = { Text("Nationality & Primary Jurisdiction", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = taxId,
                        onValueChange = { taxId = it },
                        label = { Text("Tax Identification Number (TIN / SSN)", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = bio,
                        onValueChange = { bio = it },
                        label = { Text("Personal Bio / Memo", fontSize = 11.sp) },
                        maxLines = 2,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (isSaved) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Personal identity updated and attested to local enclave!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isSaved = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Save & Cryptographically Sign Profile", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 2. DIGITAL IDENTITY PROFILE SCREEN
// -------------------------------------------------------------------------
@Composable
fun DigitalIdentityProfileScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    var didUri by remember { mutableStateOf("did:ion:EiD8947...vX90qSmartBankVault") }
    var zkProofSelected by remember { mutableStateOf("Proof of Age (Over 21) & Net Worth (>$50k)") }
    var isProofIssued by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            ProfileSectionHeader(
                title = "Digital Sovereign Identity (DID)",
                subtitle = "Self-Sovereign Identity (SSI), W3C Verifiable Credentials, and Zero-Knowledge proofs.",
                icon = Icons.Default.Fingerprint,
                color = PurpleTech
            )
        }

        // Active DIDs and Attestations
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, PurpleTech.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("ACTIVE W3C VERIFIABLE CREDENTIALS", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)

                    listOf(
                        "W3C DID Enclave Key" to "did:ion:EiD8947_q982_smartbank_secp256k1",
                        "zk-KYC Zero-Knowledge Proof" to "Attested by ISO-27001 Sovereign CA",
                        "WebAuthn Passkey (Hardware)" to "Bound to Secure Enclave (FIDO2 L3)"
                    ).forEach { (name, detail) ->
                        Surface(color = NavyCard, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.Verified, contentDescription = null, tint = PurpleTech, modifier = Modifier.size(16.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(name, color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    Text(detail, color = TextMuted, fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                                }
                            }
                        }
                    }
                }
            }
        }

        // APPLICATION FORM: Issue Zero-Knowledge Attestation
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("GENERATE NEW ZERO-KNOWLEDGE PROOF (ZKP)", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = didUri,
                        onValueChange = { didUri = it },
                        label = { Text("Issuer DID URI", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(PurpleTech),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = zkProofSelected,
                        onValueChange = { zkProofSelected = it },
                        label = { Text("Proof Claim Attribute", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(PurpleTech),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (isProofIssued) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("ZKP Claim minted! Verifiable without revealing raw personal data.", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isProofIssued = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleTech),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Mint Zero-Knowledge Attestation", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 3. DOCUMENTS VAULT SCREEN
// -------------------------------------------------------------------------
@Composable
fun DocumentsVaultScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var docType by remember { mutableStateOf("International Passport") }
    var docNumber by remember { mutableStateOf("P982348102") }
    var expiryDate by remember { mutableStateOf("2032-11-20") }
    var isUploaded by remember { mutableStateOf(false) }

    val docs = listOf(
        Triple("Passport (USA)", "Verified • Expires Nov 2032", EmeraldSuccess),
        Triple("Driver's License (NY State)", "Verified • RealID Gold Star", EmeraldSuccess),
        Triple("IRS Form W-9 / Tax Certificate", "Verified • Tax Year 2025/2026", CyberCyan),
        Triple("Utility Bill (Residence Proof)", "Verified • Issued Jan 2026", GoldAccent)
    )

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            ProfileSectionHeader(
                title = "Verified Documents & KYC Vault",
                subtitle = "AES-256 encrypted storage for government IDs, biometric records, and tax filings.",
                icon = Icons.Default.Badge,
                color = EmeraldSuccess
            )
        }

        // Document List
        item {
            Text("STORED ENCRYPTED CREDENTIALS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                docs.forEach { (title, status, color) ->
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
                                Icon(Icons.Default.Badge, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text(status, color = TextMuted, fontSize = 9.sp)
                            }
                            Icon(Icons.Default.Lock, contentDescription = null, tint = TextMuted, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }

        // APPLICATION FORM: Upload & Verify New Document
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("UPLOAD & ATTEST NEW DOCUMENT", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = docType,
                        onValueChange = { docType = it },
                        label = { Text("Document Classification", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(EmeraldSuccess),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = docNumber,
                            onValueChange = { docNumber = it },
                            label = { Text("Doc Number", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = profileTextFieldColors(EmeraldSuccess),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = expiryDate,
                            onValueChange = { expiryDate = it },
                            label = { Text("Expiry (YYYY-MM)", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = profileTextFieldColors(EmeraldSuccess),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    if (isUploaded) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Document encrypted and submitted for instant AI OCR verification!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isUploaded = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Icon(Icons.Default.CloudUpload, contentDescription = null, tint = Navy900, modifier = Modifier.size(16.dp))
                            Text("Encrypt & Upload to Secure Enclave", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 4. EMPLOYMENT INFORMATION SCREEN
// -------------------------------------------------------------------------
@Composable
fun EmploymentInformationScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var employerName by remember { mutableStateOf("Quantum Financial Labs Inc.") }
    var jobTitle by remember { mutableStateOf("Lead Distributed Systems Architect") }
    var industry by remember { mutableStateOf("Financial Technology & Quantum Computing") }
    var annualSalary by remember { mutableStateOf("185000") }
    var payrollRouting by remember { mutableStateOf("Direct Deposit Active (Vault Checking)") }
    var isSubmitted by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            ProfileSectionHeader(
                title = "Employment & Income Verification",
                subtitle = "Corporate payroll links, credit-underwriting income verification, and tax withholding profiles.",
                icon = Icons.Default.Work,
                color = ElectricBlue
            )
        }

        // APPLICATION FORM: Employment Information
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("CURRENT OCCUPATION & INCOME PROFILE", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = employerName,
                        onValueChange = { employerName = it },
                        label = { Text("Employer / Company Name", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = jobTitle,
                        onValueChange = { jobTitle = it },
                        label = { Text("Job Title / Position", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = industry,
                        onValueChange = { industry = it },
                        label = { Text("Industry Sector", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = annualSalary,
                            onValueChange = { annualSalary = it },
                            label = { Text("Annual Gross ($USD)", fontSize = 11.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = profileTextFieldColors(ElectricBlue),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = payrollRouting,
                            onValueChange = { payrollRouting = it },
                            label = { Text("Payroll Status", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = profileTextFieldColors(ElectricBlue),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    if (isSubmitted) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Employment verified! Credit line eligibility updated automatically.", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isSubmitted = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Submit Employment Verification", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 5. EDUCATION PROFILE SCREEN
// -------------------------------------------------------------------------
@Composable
fun EducationProfileScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var degree by remember { mutableStateOf("Master of Science in Distributed Computing") }
    var university by remember { mutableStateOf("Massachusetts Institute of Technology") }
    var gradYear by remember { mutableStateOf("2018") }
    var fieldOfStudy by remember { mutableStateOf("Computer Science & Cryptography") }
    var isSubmitted by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            ProfileSectionHeader(
                title = "Academic & Professional Credentials",
                subtitle = "Verifiable academic degrees, professional certifications, and student loan relief links.",
                icon = Icons.Default.School,
                color = GoldAccent
            )
        }

        // APPLICATION FORM: Education Information
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("HIGHEST DEGREE & ACADEMIC INSTITUTION", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = degree,
                        onValueChange = { degree = it },
                        label = { Text("Degree / Certification Title", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(GoldAccent),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = university,
                        onValueChange = { university = it },
                        label = { Text("University / Institution Name", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(GoldAccent),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = fieldOfStudy,
                            onValueChange = { fieldOfStudy = it },
                            label = { Text("Major / Specialization", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1.5f),
                            colors = profileTextFieldColors(GoldAccent),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = gradYear,
                            onValueChange = { gradYear = it },
                            label = { Text("Grad Year", fontSize = 11.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = profileTextFieldColors(GoldAccent),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    if (isSubmitted) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Degree credential verified! Student alumni discounts unlocked.", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isSubmitted = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Attest Academic Credential", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 6. FAMILY INFORMATION SCREEN
// -------------------------------------------------------------------------
@Composable
fun FamilyInformationScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var beneficiaryName by remember { mutableStateOf("Elena Vance") }
    var relationship by remember { mutableStateOf("Spouse & Primary Beneficiary") }
    var allocationPercent by remember { mutableFloatStateOf(100f) }
    var emergencyContactPhone by remember { mutableStateOf("+1 (555) 392-8190") }
    var isSaved by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            ProfileSectionHeader(
                title = "Family & Vault Beneficiaries",
                subtitle = "Sovereign inheritance smart contract rules, emergency kin contacts, and dependent allowances.",
                icon = Icons.Default.FamilyRestroom,
                color = AmberOrange
            )
        }

        // APPLICATION FORM: Family & Beneficiary Allocation
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("ADD / UPDATE VAULT BENEFICIARY", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = beneficiaryName,
                        onValueChange = { beneficiaryName = it },
                        label = { Text("Beneficiary Full Legal Name", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(AmberOrange),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = relationship,
                        onValueChange = { relationship = it },
                        label = { Text("Relationship", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(AmberOrange),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = emergencyContactPhone,
                        onValueChange = { emergencyContactPhone = it },
                        label = { Text("Emergency Contact Phone", fontSize = 11.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(AmberOrange),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Text("Estate & Vault Liquidity Allocation: ${allocationPercent.toInt()}%", color = TextMuted, fontSize = 11.sp)
                    Slider(
                        value = allocationPercent,
                        onValueChange = { allocationPercent = it },
                        valueRange = 10f..100f,
                        colors = SliderDefaults.colors(thumbColor = AmberOrange, activeTrackColor = AmberOrange, inactiveTrackColor = Navy800)
                    )

                    if (isSaved) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Beneficiary contract updated with ${allocationPercent.toInt()}% allocation rule.", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isSaved = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AmberOrange),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Save Beneficiary Smart Allocation", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 7. ADDRESS MANAGEMENT SCREEN
// -------------------------------------------------------------------------
@Composable
fun AddressManagementScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var streetAddress by remember { mutableStateOf("750 Financial Core Parkway, Suite 400") }
    var city by remember { mutableStateOf("New York") }
    var stateProvince by remember { mutableStateOf("NY") }
    var postalCode by remember { mutableStateOf("10005") }
    var country by remember { mutableStateOf("United States") }
    var isAddressSaved by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            ProfileSectionHeader(
                title = "Address & Residency Management",
                subtitle = "Primary tax residence, physical debit card delivery destinations, and offshore compliance.",
                icon = Icons.Default.Home,
                color = CyberCyan
            )
        }

        // APPLICATION FORM: Address Management
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("PRIMARY RESIDENTIAL & MAILING DESTINATION", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = streetAddress,
                        onValueChange = { streetAddress = it },
                        label = { Text("Street Address Line", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        OutlinedTextField(
                            value = city,
                            onValueChange = { city = it },
                            label = { Text("City", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(1f),
                            colors = profileTextFieldColors(CyberCyan),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = stateProvince,
                            onValueChange = { stateProvince = it },
                            label = { Text("State/Prov", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(0.7f),
                            colors = profileTextFieldColors(CyberCyan),
                            shape = RoundedCornerShape(10.dp)
                        )
                        OutlinedTextField(
                            value = postalCode,
                            onValueChange = { postalCode = it },
                            label = { Text("Postal Code", fontSize = 11.sp) },
                            singleLine = true,
                            modifier = Modifier.weight(0.9f),
                            colors = profileTextFieldColors(CyberCyan),
                            shape = RoundedCornerShape(10.dp)
                        )
                    }

                    OutlinedTextField(
                        value = country,
                        onValueChange = { country = it },
                        label = { Text("Country", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (isAddressSaved) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Address verified via postal database! Physical card orders synced.", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isAddressSaved = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Update Verified Residential Address", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 8. CONTACT INFORMATION SCREEN
// -------------------------------------------------------------------------
@Composable
fun ContactInformationScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val authState by viewModel.authState.collectAsStateWithLifecycle()
    var email by remember { mutableStateOf(authState.currentUser?.email ?: "alex.vance@smartbank.quantum") }
    var phone by remember { mutableStateOf("+1 (555) 839-2041") }
    var secureHandle by remember { mutableStateOf("@alex.vance:matrix.org") }
    var isEmailPgpEnabled by remember { mutableStateOf(true) }
    var isSaved by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            ProfileSectionHeader(
                title = "Contact Information & PGP Channels",
                subtitle = "Attested communication channels, PGP encrypted notifications, and emergency broadcast lines.",
                icon = Icons.Default.Phone,
                color = ElectricBlue
            )
        }

        // APPLICATION FORM: Contact Channels
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("VERIFIED COMMUNICATION ENDPOINTS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Primary Vault Email Address", fontSize = 11.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Mobile Phone Number (SMS/Voice 2FA)", fontSize = 11.sp) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    )

                    OutlinedTextField(
                        value = secureHandle,
                        onValueChange = { secureHandle = it },
                        label = { Text("Encrypted Matrix / Signal Handle", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Enforce PGP Encryption on All Statements", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text("Encrypts sensitive audit logs using your public key", color = TextMuted, fontSize = 9.sp)
                        }
                        Switch(
                            checked = isEmailPgpEnabled,
                            onCheckedChange = { isEmailPgpEnabled = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Navy900, checkedTrackColor = ElectricBlue)
                        )
                    }

                    if (isSaved) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("Contact endpoints saved & verified with SMS OTP challenge.", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isSaved = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Save & Verify Communication Channels", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 9. TRUSTED DEVICES SCREEN
// -------------------------------------------------------------------------
@Composable
fun TrustedDevicesScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var newDeviceName by remember { mutableStateOf("Pixel 9 Pro / YubiKey 5C NFC") }
    var isEnrolled by remember { mutableStateOf(false) }

    val devices = listOf(
        Triple("Pixel 8 Pro (This Device)", "Authorized Enclave Key • Active Now", EmeraldSuccess),
        Triple("MacBook Pro M3 Max (Silicon)", "Passkey FIDO2 • Last active 2h ago", CyberCyan),
        Triple("YubiKey 5C NFC (Hardware Key)", "Hardware Security Token #84920", PurpleTech)
    )

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            ProfileSectionHeader(
                title = "Trusted Hardware & Security Tokens",
                subtitle = "Hardware-bound cryptographic keys, biometric enclaves, and session revocation.",
                icon = Icons.Default.Devices,
                color = PurpleTech
            )
        }

        // Device List
        item {
            Text("AUTHORIZED HARDWARE SESSIONS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.padding(top = 4.dp)) {
                devices.forEach { (name, detail, color) ->
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
                                Icon(Icons.Default.Smartphone, contentDescription = null, tint = color, modifier = Modifier.size(18.dp))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                                Text(detail, color = TextMuted, fontSize = 9.sp)
                            }
                        }
                    }
                }
            }
        }

        // APPLICATION FORM: Enroll New Hardware Key
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("PAIR NEW HARDWARE SECURITY KEY / TOKEN", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    OutlinedTextField(
                        value = newDeviceName,
                        onValueChange = { newDeviceName = it },
                        label = { Text("Device / Security Key Model", fontSize = 11.sp) },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = profileTextFieldColors(PurpleTech),
                        shape = RoundedCornerShape(10.dp)
                    )

                    if (isEnrolled) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("New FIDO2 Hardware Key paired to vault enclave!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isEnrolled = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PurpleTech),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Initiate Hardware Key Attestation", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 10. PRIVACY SETTINGS SCREEN
// -------------------------------------------------------------------------
@Composable
fun PrivacySettingsScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var isZkDisclosuresOnly by remember { mutableStateOf(true) }
    var isAiTrainingOptOut by remember { mutableStateOf(true) }
    var isMarketingZeroData by remember { mutableStateOf(true) }
    var isGdprExportTriggered by remember { mutableStateOf(false) }

    LazyColumn(modifier = modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
        item {
            ProfileSectionHeader(
                title = "Privacy & Zero-Knowledge Disclosures",
                subtitle = "GDPR Article 17 Right-to-be-Forgotten, CCPA compliance, and zero-knowledge data sharing.",
                icon = Icons.Default.Shield,
                color = CrimsonDanger
            )
        }

        // APPLICATION FORM: Privacy Preferences
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text("GRANULAR PRIVACY & ANONYMIZATION CONTROLS", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Enforce Zero-Knowledge (ZKP) Proofs Only", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text("Never expose plaintext name or birthdate to third-party APIs", color = TextMuted, fontSize = 9.sp)
                        }
                        Switch(
                            checked = isZkDisclosuresOnly,
                            onCheckedChange = { isZkDisclosuresOnly = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Navy900, checkedTrackColor = CrimsonDanger)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Strict Opt-Out of AI Training", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text("Ensures transaction history is processed only in local enclave", color = TextMuted, fontSize = 9.sp)
                        }
                        Switch(
                            checked = isAiTrainingOptOut,
                            onCheckedChange = { isAiTrainingOptOut = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Navy900, checkedTrackColor = CrimsonDanger)
                        )
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Zero Commercial Data Sharing", color = TextWhite, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            Text("Blocks advertising trackers and affiliate brokers", color = TextMuted, fontSize = 9.sp)
                        }
                        Switch(
                            checked = isMarketingZeroData,
                            onCheckedChange = { isMarketingZeroData = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = Navy900, checkedTrackColor = CrimsonDanger)
                        )
                    }

                    if (isGdprExportTriggered) {
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                            Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(18.dp))
                                Text("GDPR Sovereign Data Package generated and PGP signed!", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    Button(
                        onClick = { isGdprExportTriggered = true },
                        modifier = Modifier.fillMaxWidth().height(46.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = CrimsonDanger),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Export Full GDPR Archive / Update Disclosures", color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// REUSABLE PROFILE HELPERS
// -------------------------------------------------------------------------
@Composable
private fun ProfileSectionHeader(title: String, subtitle: String, icon: ImageVector, color: Color) {
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
private fun profileTextFieldColors(activeColor: Color) = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = activeColor,
    unfocusedBorderColor = Navy700,
    focusedTextColor = TextWhite,
    unfocusedTextColor = TextWhite,
    focusedLabelColor = activeColor,
    unfocusedLabelColor = TextMuted
)

private const val USD = "USD"
