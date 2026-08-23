package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.BusinessCenter
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DeveloperBoard
import androidx.compose.material.icons.filled.EmojiObjects
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AnimatedScannerBeam
import com.example.ui.components.PulsingStatusBadge
import com.example.ui.components.SuccessCelebrationParticles
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
import com.example.ui.theme.PurpleTech
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BankViewModel
import kotlinx.coroutines.delay

enum class PillarSet2Page(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val subtitle: String,
    val tag: String
) {
    INCUBATOR("12. AI Startup Incubator", Icons.Default.RocketLaunch, GoldAccent, "Validates ideas, forecasts TAM, matches VCs & secures non-dilutive grants", "tab_p2_incubator"),
    DIGITAL_TWIN("13. Digital Twin Economy", Icons.Default.Timeline, CyberCyan, "Simulates investments, recessions & corporate moves before spending real capital", "tab_p2_digitaltwin"),
    QUANTUM("14. Quantum Banking", Icons.Default.Memory, ElectricBlue, "Lattice-based post-quantum encryption, QKD security & Shor algorithm defense", "tab_p2_quantum"),
    FINANCIAL_DOCTOR("15. AI Financial Doctor", Icons.Default.Healing, CrimsonDanger, "Clinical diagnosis of debt addictions, stress markers & prescriptive financial therapy", "tab_p2_doctor"),
    REPUTATION_SCORE("16. Universal Reputation", Icons.Default.Star, EmeraldSuccess, "Beyond credit scores: 8-dimensional discipline, cybersecurity & ESG ethics rating", "tab_p2_reputation")
}

@Composable
fun FuturePillarsSet2Screen(
    viewModel: BankViewModel,
    initialPillar: PillarSet2Page = PillarSet2Page.INCUBATOR,
    modifier: Modifier = Modifier
) {
    var selectedPillar by remember { mutableStateOf(initialPillar) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        // --- Header Banner ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, selectedPillar.color.copy(alpha = 0.6f))
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
                        .background(selectedPillar.color.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = selectedPillar.icon,
                        contentDescription = selectedPillar.title,
                        tint = selectedPillar.color,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = selectedPillar.title.uppercase(),
                            color = TextWhite,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 12.sp
                        )
                        PulsingStatusBadge(pulseColor = selectedPillar.color, badgeSize = 6.dp)
                    }
                    Text(
                        text = selectedPillar.subtitle,
                        color = TextMuted,
                        fontSize = 10.sp,
                        lineHeight = 13.sp
                    )
                }
            }
        }

        // --- 5 Dedicated Pillar Tabs ---
        ScrollableTabRow(
            selectedTabIndex = PillarSet2Page.values().indexOf(selectedPillar),
            containerColor = NavyCard,
            contentColor = selectedPillar.color,
            edgePadding = 4.dp,
            indicator = { tabPositions ->
                val idx = PillarSet2Page.values().indexOf(selectedPillar)
                if (idx in tabPositions.indices) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[idx]),
                        color = selectedPillar.color,
                        height = 3.dp
                    )
                }
            }
        ) {
            PillarSet2Page.values().forEach { page ->
                val isSelected = selectedPillar == page
                Tab(
                    selected = isSelected,
                    onClick = { selectedPillar = page },
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

        // --- Animated Sub-Page Host ---
        AnimatedContent(
            targetState = selectedPillar,
            transitionSpec = {
                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width } + fadeOut()
                )
            },
            label = "PillarSet2Transition",
            modifier = Modifier.weight(1f)
        ) { targetPillar ->
            when (targetPillar) {
                PillarSet2Page.INCUBATOR -> PillarPageIncubator()
                PillarSet2Page.DIGITAL_TWIN -> PillarPageDigitalTwin()
                PillarSet2Page.QUANTUM -> PillarPageQuantum()
                PillarSet2Page.FINANCIAL_DOCTOR -> PillarPageFinancialDoctor()
                PillarSet2Page.REPUTATION_SCORE -> PillarPageReputationScore()
            }
        }
    }
}

// -------------------------------------------------------------
// PILLAR 12: AI STARTUP INCUBATOR & APPLICATION FORM
// -------------------------------------------------------------
@Composable
private fun PillarPageIncubator() {
    var pitchOptimized by remember { mutableStateOf(false) }
    var showApplicationForm by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Pillar2InfoCard(
                title = "Autonomous Founder & Venture Accelerator",
                subtitle = "Algorithmic co-founder: market TAM analysis, investor sentiment mapping, non-dilutive grant applications & growth modeling.",
                icon = Icons.Default.RocketLaunch,
                color = GoldAccent
            )
        }
        item {
            Pillar2MetricsGrid(
                title = "Venture Viability & Market Fit Analysis",
                items = listOf(
                    "Total Addressable Market (TAM)" to "$14.8B (34.2% Compound Annual Growth)",
                    "Matched Sovereign & Angel Grants" to "$250,000 Zero-Equity Grant Pre-Approved",
                    "Tier-1 VC Alignment Match" to "96.4% Compatibility (Founders Fund, Sequoia)",
                    "Predicted 24-Month Survival Odds" to "88.7% (vs 12% Industry Average)"
                )
            )
        }
        item {
            Pillar2ActionCard(
                title = "Automated Pitch & Cap-Table Optimizer",
                description = "AI auto-generates dynamic valuation models, SAFE notes, and data room documents.",
                status = if (pitchOptimized) "Venture Data Room Live" else "Draft Ready",
                actionLabel = if (pitchOptimized) "Data Room Published" else "Generate & Publish Pitch Deck",
                onClick = { pitchOptimized = true },
                color = GoldAccent
            )
        }
        item {
            Pillar2ApplicationSection(
                formTitle = "Apply for $250,000 Zero-Equity Sovereign Startup Grant",
                formSubtitle = "Submit your venture thesis for instant TAM scoring, competitive landscape synthesis, and automated SAFE term sheet issuance.",
                color = GoldAccent,
                isOpen = showApplicationForm,
                onToggle = { showApplicationForm = !showApplicationForm }
            ) {
                StartupGrantApplicationForm()
            }
        }
    }
}

@Composable
private fun StartupGrantApplicationForm() {
    var ventureName by remember { mutableStateOf("NeuralQuantum DeFi Protocol") }
    var targetTAM by remember { mutableFloatStateOf(150000f) }
    var sectorCategory by remember { mutableStateOf("AI & Quantum Fintech") }
    var formState by remember { mutableStateOf("DRAFT") }

    if (formState == "SCANNING") {
        LaunchedEffect(Unit) {
            delay(2200)
            formState = "APPROVED"
        }
    }

    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        when (formState) {
            "DRAFT" -> {
                OutlinedTextField(
                    value = ventureName,
                    onValueChange = { ventureName = it },
                    label = { Text("Startup Project / Thesis Name", color = TextMuted, fontSize = 10.sp) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Requested Non-Dilutive Grant: $${targetTAM.toInt()}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Slider(
                    value = targetTAM,
                    onValueChange = { targetTAM = it },
                    valueRange = 25000f..500000f,
                    steps = 19,
                    colors = SliderDefaults.colors(thumbColor = GoldAccent, activeTrackColor = GoldAccent)
                )
                Text("Venture Sector", color = TextMuted, fontSize = 11.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("AI Fintech", "Space Commerce", "Clean Energy Grid").forEach { sector ->
                        val sel = sectorCategory == sector
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (sel) GoldAccent.copy(alpha = 0.25f) else Navy800,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (sel) GoldAccent else Navy700),
                            modifier = Modifier.clickable { sectorCategory = sector }
                        ) {
                            Text(
                                text = sector,
                                color = if (sel) GoldAccent else TextMuted,
                                fontSize = 10.sp,
                                fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_startup_application"),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Submit for Autonomous VC Due Diligence", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = GoldAccent)
                    Text("Synthesizing TAM Graph, Patent Cross-References & Term Sheet...", color = GoldAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().clip(CircleShape), color = GoldAccent, trackColor = Navy800)
                }
            }
            "APPROVED" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SuccessCelebrationParticles(modifier = Modifier.size(60.dp))
                    Text("NON-DILUTIVE SEED GRANT APPROVED", color = GoldAccent, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("$${targetTAM.toInt()} Sovereign Grant Allocated • SAFE Note Escrow Ready", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldAccent),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Submit Another Venture Application", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// PILLAR 13: DIGITAL TWIN ECONOMY & SIMULATION STUDIO FORM
// -------------------------------------------------------------
@Composable
private fun PillarPageDigitalTwin() {
    var showApplicationForm by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Pillar2InfoCard(
                title = "High-Fidelity Macro Financial Digital Twin",
                subtitle = "Runs 100,000 Monte Carlo parallel universe simulations before any real capital is committed to investments, mortgages, or expansion.",
                icon = Icons.Default.Timeline,
                color = CyberCyan
            )
        }
        item {
            Pillar2MetricsGrid(
                title = "Parallel Universe Stress-Test Telemetry",
                items = listOf(
                    "User Net Worth Twin (2035 Horizon)" to "$3,840,000 (95% Confidence Interval)",
                    "Global Stagflation Scenario Impact" to "-4.2% Net Drawdown (Fully Hedged)",
                    "30-Year Mortgage Early Payoff Sim" to "+$148,000 Added Wealth vs Investing Difference",
                    "Company Expansion Failure Risk" to "Reduced from 41% to 6.2% via AI Sim"
                )
            )
        }
        item {
            Pillar2ApplicationSection(
                formTitle = "Launch 100k Multi-Universe Simulation Wizard",
                formSubtitle = "Configure simultaneous job transition, 30% real estate crash, and crypto volatility to calculate probability of financial freedom.",
                color = CyberCyan,
                isOpen = showApplicationForm,
                onToggle = { showApplicationForm = !showApplicationForm }
            ) {
                DigitalTwinSimulationForm()
            }
        }
    }
}

@Composable
private fun DigitalTwinSimulationForm() {
    var shockScenario by remember { mutableStateOf("Global Hyperinflation & 40% Rate Spike") }
    var capitalAtRisk by remember { mutableFloatStateOf(100000f) }
    var formState by remember { mutableStateOf("DRAFT") }

    if (formState == "SCANNING") {
        LaunchedEffect(Unit) {
            delay(2400)
            formState = "APPROVED"
        }
    }

    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        when (formState) {
            "DRAFT" -> {
                Text("Capital Subject to Monte Carlo Simulation: $${capitalAtRisk.toInt()}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Slider(
                    value = capitalAtRisk,
                    onValueChange = { capitalAtRisk = it },
                    valueRange = 10000f..1000000f,
                    steps = 19,
                    colors = SliderDefaults.colors(thumbColor = CyberCyan, activeTrackColor = CyberCyan)
                )
                Text("Macro Shock Stress Scenario", color = TextMuted, fontSize = 11.sp)
                listOf("Stagflation & Rate Hike", "Tech Sector Layoff Wave", "Cryptocurrency Supercycle").forEach { sc ->
                    val sel = shockScenario == sc
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (sel) CyberCyan.copy(alpha = 0.25f) else Navy800,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (sel) CyberCyan else Navy700),
                        modifier = Modifier.fillMaxWidth().clickable { shockScenario = sc }
                    ) {
                        Text(
                            text = sc,
                            color = if (sel) CyberCyan else TextMuted,
                            fontSize = 10.sp,
                            fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                        )
                    }
                }
                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_sim_application"),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Execute 100,000 Universe Monte Carlo Twin", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = CyberCyan)
                    Text("Calculating Probability Distribution across 100,000 Universes...", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().clip(CircleShape), color = CyberCyan, trackColor = Navy800)
                }
            }
            "APPROVED" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SuccessCelebrationParticles(modifier = Modifier.size(60.dp))
                    Text("SIMULATION COMPLETE: 99.4% SURVIVAL ODDS", color = CyberCyan, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("Identified Optimal Hedge: +14% Treasury TIPS Allocation Prevents Capital Drawdown", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Configure New Simulation Parameters", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// PILLAR 14: QUANTUM BANKING & VAULT MIGRATION FORM
// -------------------------------------------------------------
@Composable
private fun PillarPageQuantum() {
    var showApplicationForm by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Pillar2InfoCard(
                title = "Post-Quantum Cryptography & Quantum Key Vault",
                subtitle = "Guarantees 100-year future-proof defense against Shor's algorithm, quantum decryption supercomputers, and entanglement spoofing.",
                icon = Icons.Default.Memory,
                color = ElectricBlue
            )
        }
        item {
            Pillar2MetricsGrid(
                title = "Quantum Cryptographic Shielding",
                items = listOf(
                    "Primary Encryption Standard" to "Kyber-1024 & Dilithium-5 (NIST Approved)",
                    "Quantum Key Distribution (QKD)" to "Entangled Photonic Satellite Sync Active",
                    "Decryption Vulnerability" to "0.00% (Immune to 100,000 Qubit Attacks)",
                    "Zero-Knowledge Quantum Proofs" to "Verified in 0.8ms per sovereign signature"
                )
            )
        }
        item {
            Pillar2MetricsGrid(
                title = "Autonomous Quantum Threat Hunting",
                items = listOf(
                    "Harvest Now, Decrypt Later Defense" to "All historical archives quantum-re-encrypted",
                    "Quantum Random Number Generator (QRNG)" to "True hardware entropy via vacuum fluctuations",
                    "Inter-Bank Quantum Mesh" to "Connected to Federal Reserve Quantum Fiber Grid"
                )
            )
        }
        item {
            Pillar2ApplicationSection(
                formTitle = "Request Post-Quantum Lattice Vault Migration",
                formSubtitle = "Migrate RSA/ECDSA legacy keys to NIST-standard Kyber-1024 lattice keys with photon-entangled satellite key backups.",
                color = ElectricBlue,
                isOpen = showApplicationForm,
                onToggle = { showApplicationForm = !showApplicationForm }
            ) {
                QuantumVaultMigrationForm()
            }
        }
    }
}

@Composable
private fun QuantumVaultMigrationForm() {
    var latticeStandard by remember { mutableStateOf("Kyber-1024 (Lattice-Based NIST Grade)") }
    var formState by remember { mutableStateOf("DRAFT") }

    if (formState == "SCANNING") {
        LaunchedEffect(Unit) {
            delay(2100)
            formState = "APPROVED"
        }
    }

    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        when (formState) {
            "DRAFT" -> {
                Text("Select Quantum Cryptographic Standard", color = TextMuted, fontSize = 11.sp)
                listOf("Kyber-1024 (Lattice-Based NIST)", "Dilithium-5 (Signature Shield)", "Falcon-1024 (Zero-Latency)").forEach { std ->
                    val sel = latticeStandard.startsWith(std.substring(0, 5))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (sel) ElectricBlue.copy(alpha = 0.25f) else Navy800,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (sel) ElectricBlue else Navy700),
                        modifier = Modifier.fillMaxWidth().clickable { latticeStandard = std }
                    ) {
                        Text(
                            text = std,
                            color = if (sel) ElectricBlue else TextMuted,
                            fontSize = 10.sp,
                            fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                        )
                    }
                }
                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_quantum_application"),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Seal Assets with Quantum Entangled Photons", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = ElectricBlue)
                    Text("Harvesting QRNG Hardware Vacuum Entropy & Generating Lattice Shield...", color = ElectricBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().clip(CircleShape), color = ElectricBlue, trackColor = Navy800)
                }
            }
            "APPROVED" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SuccessCelebrationParticles(modifier = Modifier.size(60.dp))
                    Text("QUANTUM VAULT ENCRYPTION SEALED", color = ElectricBlue, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("Lattice Key #QK-99201 Active • Immune to 1,000,000 Qubit Quantum Supercomputers", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Re-verify Quantum Entanglement", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// PILLAR 15: AI FINANCIAL DOCTOR & PRESCRIPTION FORM
// -------------------------------------------------------------
@Composable
private fun PillarPageFinancialDoctor() {
    var showApplicationForm by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Pillar2InfoCard(
                title = "Clinical AI Financial Diagnostic & Recovery Clinic",
                subtitle = "Biopsies spending impulses, debt dopamine loops, bankruptcy vectors, and prescribes automated financial wellness regimens.",
                icon = Icons.Default.Healing,
                color = CrimsonDanger
            )
        }
        item {
            Pillar2MetricsGrid(
                title = "Financial Health Pathology & Diagnosis",
                items = listOf(
                    "Overall Financial Vitality Score" to "94 / 100 (Optimal Health)",
                    "Dopamine Impulse Spending Risk" to "Low (2 incidents detected this month)",
                    "Debt-to-Income Blood Pressure" to "11.4% (Healthy < 30%)",
                    "Financial Cortisol Stress Index" to "Low (4.1/10 — Fully automated security)"
                )
            )
        }
        item {
            Pillar2ApplicationSection(
                formTitle = "Request Clinical AI Financial Biopsy & Prescription",
                formSubtitle = "Automate behavioral spending lockouts, dopamine impulse cooling-off buffers, and high-interest debt eradication therapy.",
                color = CrimsonDanger,
                isOpen = showApplicationForm,
                onToggle = { showApplicationForm = !showApplicationForm }
            ) {
                FinancialTherapyForm()
            }
        }
    }
}

@Composable
private fun FinancialTherapyForm() {
    var primarySymptom by remember { mutableStateOf("Late-Night Impulse Dopamine Shopping") }
    var formState by remember { mutableStateOf("DRAFT") }

    if (formState == "SCANNING") {
        LaunchedEffect(Unit) {
            delay(1900)
            formState = "APPROVED"
        }
    }

    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        when (formState) {
            "DRAFT" -> {
                Text("Select Chief Financial Pathology Symptom", color = TextMuted, fontSize = 11.sp)
                listOf("Late-Night Impulse Shopping", "High-Interest Debt Anxiety", "Lifestyle Creep & Low Savings Rate").forEach { symptom ->
                    val sel = primarySymptom == symptom
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (sel) CrimsonDanger.copy(alpha = 0.25f) else Navy800,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (sel) CrimsonDanger else Navy700),
                        modifier = Modifier.fillMaxWidth().clickable { primarySymptom = symptom }
                    ) {
                        Text(
                            text = symptom,
                            color = if (sel) CrimsonDanger else TextMuted,
                            fontSize = 10.sp,
                            fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                        )
                    }
                }
                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_doctor_application"),
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonDanger),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Generate Clinical Prescription Regimen", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = CrimsonDanger)
                    Text("Biopsying Transaction Timestamp Clusters & Dopamine Triggers...", color = CrimsonDanger, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().clip(CircleShape), color = CrimsonDanger, trackColor = Navy800)
                }
            }
            "APPROVED" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SuccessCelebrationParticles(modifier = Modifier.size(60.dp))
                    Text("PRESCRIPTION Rx #DOC-881 ISSUED", color = CrimsonDanger, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("Enforced: 12-Hour Shopping Cooldown & Auto-Sweep $20/day into Low-Cost Index Yield", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonDanger),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Re-assess Financial Vitality", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// PILLAR 16: UNIVERSAL REPUTATION SCORE & AUDIT FORM
// -------------------------------------------------------------
@Composable
private fun PillarPageReputationScore() {
    var showApplicationForm by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Pillar2InfoCard(
                title = "Holistic 8-Dimensional Financial Reputation Score",
                subtitle = "Replaces legacy credit scores with a 360-degree ethical rating: saving discipline, cyber defense, sustainability, and tax honor.",
                icon = Icons.Default.Star,
                color = EmeraldSuccess
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("Sovereign Reputation Rating", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Text("Elite Global Tier (Tier 1 Sovereign Citizen)", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                        Text("962 / 1000", color = EmeraldSuccess, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                    }
                    LinearProgressIndicator(
                        progress = { 0.962f },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                        color = EmeraldSuccess,
                        trackColor = Navy700
                    )
                }
            }
        }
        item {
            Pillar2MetricsGrid(
                title = "8-Dimensional Reputation Vectors",
                items = listOf(
                    "1. Saving & Investing Discipline" to "98 / 100 (Consistent 32% savings rate)",
                    "2. Debt Repayment Reliability" to "100 / 100 (0 late payments lifetime)",
                    "3. Portfolio Quality & Diversification" to "94 / 100 (Zero unhedged junk exposure)",
                    "4. Cybersecurity Awareness" to "99 / 100 (Hardware passkeys + 0 breaches)",
                    "5. Tax Compliance & Accuracy" to "100 / 100 (Audit-proof clean ledger)",
                    "6. ESG & Environmental Impact" to "91 / 100 (Carbon negative lifestyle)",
                    "7. Philanthropic & Community Giving" to "88 / 100 (3.5% automated tithe)",
                    "8. Anti-Fraud Integrity Record" to "100 / 100 (Zero malicious chargebacks)"
                )
            )
        }
        item {
            Pillar2ApplicationSection(
                formTitle = "Apply for Global Tier-1 Sovereign Citizen Upgrade",
                formSubtitle = "Zero-Knowledge verification of tax records, green carbon offsets, and hardware security keys for global 0.25% loan discounts.",
                color = EmeraldSuccess,
                isOpen = showApplicationForm,
                onToggle = { showApplicationForm = !showApplicationForm }
            ) {
                ReputationAuditForm()
            }
        }
    }
}

@Composable
private fun ReputationAuditForm() {
    var formState by remember { mutableStateOf("DRAFT") }

    if (formState == "SCANNING") {
        LaunchedEffect(Unit) {
            delay(2000)
            formState = "APPROVED"
        }
    }

    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        when (formState) {
            "DRAFT" -> {
                Text("Submit Zero-Knowledge Proofs for 8-Dimensional Reputation Audit", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text("Includes: Tax audit clearance, carbon token offsets, and 0-fraud authentication history.", color = TextMuted, fontSize = 10.sp)
                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_reputation_application"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Execute Autonomous 8D Trust Audit", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = EmeraldSuccess)
                    Text("Verifying Cryptographic 8D Trust Ledger & Sovereign Certifications...", color = EmeraldSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().clip(CircleShape), color = EmeraldSuccess, trackColor = Navy800)
                }
            }
            "APPROVED" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SuccessCelebrationParticles(modifier = Modifier.size(60.dp))
                    Text("TIER-1 SOVEREIGN STATUS CONFIRMED (962/1000)", color = EmeraldSuccess, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("Unlocked Global Prime Interest Rates & Instant Sovereign Passkey Settlement", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldSuccess),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Export Sovereign Trust Certificate", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// REUSABLE HELPER COMPONENTS
// -------------------------------------------------------------
@Composable
private fun Pillar2ApplicationSection(
    formTitle: String,
    formSubtitle: String,
    color: Color,
    isOpen: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = if (isOpen) 0.8f else 0.4f))
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(formTitle, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        PulsingStatusBadge(pulseColor = color, badgeSize = 5.dp)
                    }
                    Text(formSubtitle, color = TextMuted, fontSize = 10.sp, lineHeight = 13.sp)
                }
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = color.copy(alpha = 0.2f),
                    border = androidx.compose.foundation.BorderStroke(1.dp, color)
                ) {
                    Text(
                        text = if (isOpen) "Hide Form" else "Open Form",
                        color = color,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
            AnimatedVisibility(visible = isOpen) {
                Box(modifier = Modifier.fillMaxWidth().background(Navy900)) {
                    content()
                }
            }
        }
    }
}

@Composable
private fun Pillar2InfoCard(title: String, subtitle: String, icon: ImageVector, color: Color) {
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
private fun Pillar2MetricsGrid(title: String, items: List<Pair<String, String>>) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Navy900),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            items.forEach { (label, value) ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(label, color = TextMuted, fontSize = 10.sp)
                    Text(value, color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 10.sp)
                }
            }
        }
    }
}

@Composable
private fun Pillar2ActionCard(
    title: String,
    description: String,
    status: String,
    actionLabel: String,
    onClick: () -> Unit,
    color: Color
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Navy900),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(description, color = TextMuted, fontSize = 10.sp)
                }
                Surface(color = color.copy(alpha = 0.15f), shape = RoundedCornerShape(6.dp)) {
                    Text(status, color = color, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }
            Button(
                onClick = onClick,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = color),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(actionLabel, color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
            }
        }
    }
}
