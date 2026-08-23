package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Token
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

enum class WorldIdentityPage(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val tag: String
) {
    BANKING("Banking & Liquidity", Icons.Default.AccountBalance, CyberCyan, "tab_wid_banking"),
    EDUCATION("Education Credentials", Icons.Default.School, PurpleTech, "tab_wid_education"),
    EMPLOYMENT("Employment Ledger", Icons.Default.Work, EmeraldSuccess, "tab_wid_employment"),
    TAX("Tax & Compliance", Icons.Default.ReceiptLong, GoldAccent, "tab_wid_tax"),
    HEALTHCARE("Healthcare & Biometrics", Icons.Default.LocalHospital, CrimsonDanger, "tab_wid_healthcare"),
    GOVERNMENT_IDS("Government IDs", Icons.Default.Badge, ElectricBlue, "tab_wid_gov_ids"),
    DIGITAL_SIGNATURES("Digital Signatures", Icons.Default.Draw, CyberCyan, "tab_wid_signatures"),
    INSURANCE("Insurance Shields", Icons.Default.Policy, EmeraldSuccess, "tab_wid_insurance"),
    ASSETS("Assets & Real Estate", Icons.Default.Home, GoldAccent, "tab_wid_assets")
}

@Composable
fun WorldDigitalIdentityScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    var currentPage by remember { mutableStateOf(WorldIdentityPage.BANKING) }
    var showQrModal by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // --- Top Sovereign Passport Header ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.6f))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(CyberCyan.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Fingerprint,
                                contentDescription = "World ID",
                                tint = CyberCyan,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Text(
                                    text = "WORLD DIGITAL FINANCIAL IDENTITY",
                                    color = TextWhite,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 12.sp
                                )
                                PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 6.dp)
                            }
                            Text(
                                text = "Zero-Knowledge Sovereign Identity • W3C DID Standard",
                                color = CyberCyan,
                                fontSize = 10.sp
                            )
                        }
                    }

                    IconButton(
                        onClick = { showQrModal = true },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Navy900)
                            .border(1.dp, CyberCyan.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .testTag("open_world_id_qr_btn")
                    ) {
                        Icon(
                            imageVector = Icons.Default.QrCode,
                            contentDescription = "Share World ID",
                            tint = CyberCyan,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                // Global Sovereign Identifier Chip
                Surface(
                    color = Navy900,
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Navy700),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("SOVEREIGN GLOBAL DID", color = TextMuted, fontSize = 8.sp, fontWeight = FontWeight.Bold)
                            Text(
                                text = "did:w3c:global-id#7849-0291-5582-X9Q",
                                color = GoldAccent,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            )
                        }
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text("ZK Verified", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                }
            }
        }

        // --- Multi-Page Sub-Navigation Bar (9 Separate Hubs) ---
        ScrollableTabRow(
            selectedTabIndex = WorldIdentityPage.values().indexOf(currentPage),
            containerColor = NavyCard,
            contentColor = CyberCyan,
            edgePadding = 4.dp,
            indicator = { tabPositions ->
                val idx = WorldIdentityPage.values().indexOf(currentPage)
                if (idx in tabPositions.indices) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[idx]),
                        color = currentPage.color,
                        height = 3.dp
                    )
                }
            }
        ) {
            WorldIdentityPage.values().forEach { page ->
                val isSelected = currentPage == page
                Tab(
                    selected = isSelected,
                    onClick = { currentPage = page },
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
                                text = page.title,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) page.color else TextMuted
                            )
                        }
                    },
                    modifier = Modifier.testTag(page.tag)
                )
            }
        }

        // --- Animated Page Host ---
        AnimatedContent(
            targetState = currentPage,
            transitionSpec = {
                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width } + fadeOut()
                )
            },
            label = "WorldIdPageTransition",
            modifier = Modifier.weight(1f)
        ) { targetPage ->
            when (targetPage) {
                WorldIdentityPage.BANKING -> PageWidBanking()
                WorldIdentityPage.EDUCATION -> PageWidEducation()
                WorldIdentityPage.EMPLOYMENT -> PageWidEmployment()
                WorldIdentityPage.TAX -> PageWidTax()
                WorldIdentityPage.HEALTHCARE -> PageWidHealthcare()
                WorldIdentityPage.GOVERNMENT_IDS -> PageWidGovernmentIds()
                WorldIdentityPage.DIGITAL_SIGNATURES -> PageWidDigitalSignatures()
                WorldIdentityPage.INSURANCE -> PageWidInsurance()
                WorldIdentityPage.ASSETS -> PageWidAssets()
            }
        }
    }

    if (showQrModal) {
        WorldIdQrModal(onDismiss = { showQrModal = false })
    }
}

// -------------------------------------------------------------
// PAGE 1: BANKING & LIQUIDITY VAULTS
// -------------------------------------------------------------
@Composable
private fun PageWidBanking() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            IdentitySectionHeader("Decentralized Sovereign Banking", "Multi-currency digital cash rails and liquidity pools replacing traditional siloed checking accounts.", Icons.Default.AccountBalance, CyberCyan)
        }
        item {
            CredentialCard(
                title = "Global Sovereign CBDC & Multi-Fiat Clearing",
                issuer = "Bank for International Settlements (Project Agora)",
                status = "Active & Attested",
                statusColor = EmeraldSuccess,
                details = listOf(
                    "Universal Account ID" to "US-FED-7739-1092",
                    "Available Sovereign Liquidity" to "$142,500.00 USD Eqv.",
                    "Active Currencies" to "USD, EUR, GBP, JPY, SGD",
                    "Instant Settlement Rail" to "ISO 20022 Cross-Border Realtime"
                )
            )
        }
        item {
            CredentialCard(
                title = "Automated High-Yield Treasury Vault",
                issuer = "Decentralized Liquidity Protocol v4",
                status = "Compounding (5.25% APY)",
                statusColor = GoldAccent,
                details = listOf(
                    "Locked Collateral" to "$80,000.00",
                    "Monthly Accrued Yield" to "+$350.00 / mo",
                    "Withdrawal Friction" to "0.00s Instant Liquidity"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 2: EDUCATION CREDENTIALS
// -------------------------------------------------------------
@Composable
private fun PageWidEducation() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            IdentitySectionHeader("Cryptographic Academic Diplomas", "Verifiable tamper-proof degrees, micro-certifications, and academic attestations on zero-knowledge ledgers.", Icons.Default.School, PurpleTech)
        }
        item {
            CredentialCard(
                title = "Master of Science in Computer Science (AI & Robotics)",
                issuer = "Stanford University (ZK-Academic Registrar)",
                status = "Cryptographically Verified",
                statusColor = EmeraldSuccess,
                details = listOf(
                    "Conferred Date" to "June 2022",
                    "Honors" to "Summa Cum Laude (GPA 3.96)",
                    "ZK Hash" to "0x7f9a...3c82d",
                    "W3C Verifiable Credential" to "vc:edu:stanford#88390"
                )
            )
        }
        item {
            CredentialCard(
                title = "Chartered Financial Analyst (CFA® Charterholder)",
                issuer = "CFA Institute Global Registrar",
                status = "Active Membership",
                statusColor = CyberCyan,
                details = listOf(
                    "Credential ID" to "CFA-GL-993812",
                    "Specialization" to "Quantitative Asset Allocation & Risk",
                    "Continuing Education" to "100% Compliant 2026"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 3: EMPLOYMENT & LABOR LEDGER
// -------------------------------------------------------------
@Composable
private fun PageWidEmployment() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            IdentitySectionHeader("Verified Labor & Income Ledger", "Tamper-proof employment verification, salary provenance, and cross-border work authorizations.", Icons.Default.Work, EmeraldSuccess)
        }
        item {
            CredentialCard(
                title = "Principal AI Infrastructure Architect",
                issuer = "Global Quantum Systems Inc.",
                status = "Current Active Role",
                statusColor = EmeraldSuccess,
                details = listOf(
                    "Tenure" to "Aug 2023 - Present (3 Years)",
                    "Verified Base Compensation" to "$210,000 / yr (ZK-Attested)",
                    "Security Clearance" to "Zero-Trust Tier 3 Sovereign Access",
                    "Work Jurisdiction" to "Remote Global / US Resident"
                )
            )
        }
        item {
            CredentialCard(
                title = "Senior Distributed Systems Engineer",
                issuer = "Apex Cloud Technologies Ltd.",
                status = "Past Verified Tenure",
                statusColor = CyberCyan,
                details = listOf(
                    "Tenure" to "Jan 2020 - July 2023",
                    "Employer Attestation" to "Signed by CEO & HR Multi-Sig",
                    "Separation Standing" to "Excellent / Rehire Eligible"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 4: TAX & SOVEREIGN COMPLIANCE
// -------------------------------------------------------------
@Composable
private fun PageWidTax() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            IdentitySectionHeader("Automated Sovereign Tax Ledger", "Continuous W-8BEN/W-2 zero-knowledge compliance, instant withholding settlements, and global reporting.", Icons.Default.ReceiptLong, GoldAccent)
        }
        item {
            CredentialCard(
                title = "Global Unified Tax Identity (IRS & OECD FATCA)",
                issuer = "Sovereign Revenue Bureau (Automated Clearing)",
                status = "100% Tax Compliant",
                statusColor = EmeraldSuccess,
                details = listOf(
                    "Global Tax ID (TIN)" to "XXX-XX-9412 (ZK Masked)",
                    "2025 Tax Obligation" to "Settled & Certified",
                    "Automated Tax-Loss Offsets" to "$3,450.00 Harvested",
                    "Cross-Border Treaty Status" to "Double Tax Relief Active (US-EU)"
                )
            )
        }
        item {
            CredentialCard(
                title = "Instant Autonomous Withholding Reconciliation",
                issuer = "Digital Tax Smart Contract Engine",
                status = "Real-Time Balance: $0.00 Owed",
                statusColor = CyberCyan,
                details = listOf(
                    "Estimated 2026 Refund" to "+$1,840.00 (Projected)",
                    "Deductions Provenance" to "HSA, 401(k), Green Energy Capex"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 5: HEALTHCARE & BIOMETRICS
// -------------------------------------------------------------
@Composable
private fun PageWidHealthcare() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            IdentitySectionHeader("Encrypted Genomic & Health Records", "Decentralized electronic health records (EHR), emergency medical access keys, and biometric identity.", Icons.Default.LocalHospital, CrimsonDanger)
        }
        item {
            CredentialCard(
                title = "Universal Global Emergency Health Passport",
                issuer = "World Health Interoperability Alliance",
                status = "Encrypted & Biometric Gated",
                statusColor = EmeraldSuccess,
                details = listOf(
                    "Blood Type & RH" to "O-Positive (Universal Compatible)",
                    "Critical Drug Allergies" to "Penicillin (Verified Warning)",
                    "Emergency Access Key" to "Paramedic QR Scanner Token",
                    "Organ Donor Status" to "Registered Universal Donor"
                )
            )
        }
        item {
            CredentialCard(
                title = "Full Genomic Profile & Pharmacogenomics",
                issuer = "NextGen Genomic Vault (HIPAA/GDPR Sealed)",
                status = "Self-Sovereign Encryption",
                statusColor = PurpleTech,
                details = listOf(
                    "Genetic Risk Analysis" to "Zero High-Risk Cardiovascular Markers",
                    "Metabolic Medication Fit" to "Optimal Response Profile (Tier 1)"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 6: GOVERNMENT IDS & PASSPORTS
// -------------------------------------------------------------
@Composable
private fun PageWidGovernmentIds() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            IdentitySectionHeader("Sovereign Digital Passports & IDs", "ICAO-compliant biometric e-passports, borderless global transit, and sovereign driver credentials.", Icons.Default.Badge, ElectricBlue)
        }
        item {
            CredentialCard(
                title = "Sovereign Biometric E-Passport",
                issuer = "United States Department of State (Digital Bureau)",
                status = "Valid (Expires 2034)",
                statusColor = EmeraldSuccess,
                details = listOf(
                    "Passport Number" to "US-E-99482103 (NFC Chip Sync)",
                    "Global Transit Clearance" to "Global Entry & TSA Pre-Check Active",
                    "Facial & Iris Hash" to "NIST FRVT 99.98% Match Rate",
                    "Visa-Free Access" to "186 Jurisdictions"
                )
            )
        }
        item {
            CredentialCard(
                title = "Real-ID Sovereign Digital Driver's License",
                issuer = "Department of Motor Vehicles (Mobile ID Rail)",
                status = "Active Real-ID Tier 1",
                statusColor = CyberCyan,
                details = listOf(
                    "License Class" to "Class C (Commercial & Passenger)",
                    "Organ Donor" to "Yes",
                    "Zero Driving Infractions" to "Clean 10-Year Record"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 7: DIGITAL SIGNATURES & SMART CONTRACTS
// -------------------------------------------------------------
@Composable
private fun PageWidDigitalSignatures() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            IdentitySectionHeader("Cryptographic Power of Attorney & Keys", "Ed25519 multi-signature contracts, verifiable notary seals, and autonomous legal authority.", Icons.Default.Draw, CyberCyan)
        }
        item {
            CredentialCard(
                title = "Master Ed25519 Sovereign Signing Key",
                issuer = "Hardware Security Module (Secure Enclave)",
                status = "Hardware Protected (Biometric Auth)",
                statusColor = EmeraldSuccess,
                details = listOf(
                    "Public Key" to "ed25519:8a3f9e...2201b",
                    "Signed Transactions" to "1,248 Legal Smart Contracts",
                    "eIDAS Legal Equivalent" to "Qualified Electronic Signature (QES)",
                    "Revocation Ledger" to "Zero Revocations / Clean Key"
                )
            )
        }
        item {
            CredentialCard(
                title = "Revocable Living Trust & Multi-Sig Will",
                issuer = "Sovereign Digital Notary Association",
                status = "Active & Legally Sealed",
                statusColor = GoldAccent,
                details = listOf(
                    "Trust Beneficiaries" to "2 Named Heirs (Encrypted ZK Proofs)",
                    "Probate Bypass" to "100% Autonomous Asset Transfer",
                    "Threshold Quorum" to "2-of-3 Hardware Signatures"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 8: INSURANCE SHIELDS
// -------------------------------------------------------------
@Composable
private fun PageWidInsurance() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            IdentitySectionHeader("Autonomous Parametric Insurance", "Instant zero-claim auto-settling insurance policies covering health, life, property, and cyber liabilities.", Icons.Default.Policy, EmeraldSuccess)
        }
        item {
            CredentialCard(
                title = "Global Comprehensive Health & Catastrophe Shield",
                issuer = "Swiss Re Sovereign Risk Pool",
                status = "Active Full Coverage",
                statusColor = EmeraldSuccess,
                details = listOf(
                    "Coverage Limit" to "$2,000,000.00 Worldwide",
                    "Deductible" to "$500.00 / yr",
                    "Hospital Network" to "Direct Settlement at 45,000 Hospitals",
                    "Medevac Evacuation" to "100% Covered Worldwide"
                )
            )
        }
        item {
            CredentialCard(
                title = "Zero-Knowledge Cyber Identity & Asset Shield",
                issuer = "SmartBank Cyber Underwriters",
                status = "Active ($1M Policy)",
                statusColor = CyberCyan,
                details = listOf(
                    "Fraud Protection" to "100% Instant Reimbursement Guarantee",
                    "Ransomware & SIM-Swap" to "Automated Forensic Containment"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 9: ASSETS & REAL ESTATE LEDGER
// -------------------------------------------------------------
@Composable
private fun PageWidAssets() {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            IdentitySectionHeader("Tokenized Real Estate & Physical Assets", "Blockchain land registry deeds, precious metals custody receipts, and vehicle ownership titles.", Icons.Default.Home, GoldAccent)
        }
        item {
            CredentialCard(
                title = "Primary Sovereign Residence (Deed & Title)",
                issuer = "County Land Records Registry (Immutable Ledger)",
                status = "100% Verified Ownership",
                statusColor = EmeraldSuccess,
                details = listOf(
                    "Property Address" to "742 Evergreen Terrace, Palo Alto, CA",
                    "Assessed Market Value" to "$1,450,000.00",
                    "Mortgage Encumbrance" to "$620,000.00 (Fixed 5.8%)",
                    "Equity Stake" to "$830,000.00 (57.2% Net Equity)"
                )
            )
        }
        item {
            CredentialCard(
                title = "Allocated Physical Gold Bullion Vault",
                issuer = "Zurich FreePort Custodial Vaults",
                status = "Audited & Allocated",
                statusColor = GoldAccent,
                details = listOf(
                    "Bar Serial Numbers" to "CH-GLD-99014 through 99018",
                    "Weight & Purity" to "250.00 Troy Oz (99.99% Fine Gold)",
                    "Custody Value" to "$595,000.00 USD"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// HELPER COMPOSABLES & MODAL
// -------------------------------------------------------------
@Composable
private fun IdentitySectionHeader(title: String, subtitle: String, icon: ImageVector, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(22.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                Text(subtitle, color = TextMuted, fontSize = 10.sp, lineHeight = 14.sp)
            }
        }
    }
}

@Composable
private fun CredentialCard(
    title: String,
    issuer: String,
    status: String,
    statusColor: Color,
    details: List<Pair<String, String>>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Navy900),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text("Issuer: $issuer", color = TextMuted, fontSize = 10.sp)
                }
                Surface(color = statusColor.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                    Text(status, color = statusColor, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

            details.forEach { (k, v) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(k, color = TextMuted, fontSize = 10.sp)
                    Text(v, color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
private fun WorldIdQrModal(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Icon(Icons.Default.Fingerprint, contentDescription = null, tint = CyberCyan)
                Text("Share World Digital ID", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
            }
        },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(160.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White)
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.QrCode,
                        contentDescription = "QR Code",
                        tint = Navy900,
                        modifier = Modifier.size(136.dp)
                    )
                }

                Text(
                    text = "did:w3c:global-id#7849-0291-5582-X9Q",
                    color = GoldAccent,
                    fontFamily = FontFamily.Monospace,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Scanning this QR provides Zero-Knowledge selective disclosure to banks, immigration border controls, employers, or hospitals.",
                    color = TextMuted,
                    fontSize = 10.sp,
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }
        },
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan)
            ) {
                Text("Close", color = Navy900, fontWeight = FontWeight.Bold)
            }
        },
        containerColor = Navy800
    )
}
