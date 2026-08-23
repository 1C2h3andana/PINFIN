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
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Biotech
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CloudDownload
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Extension
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.SatelliteAlt
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Shop
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.TrackChanges
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

enum class PillarSet3Page(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val subtitle: String,
    val tag: String
) {
    MARKETPLACE("17. AI Marketplace", Icons.Default.Storefront, GoldAccent, "Global app store for financial AI plugins, trading bots & wealth algorithms", "tab_p3_marketplace"),
    RESEARCH_CENTER("18. AI Research Lab", Icons.Default.Science, CyberCyan, "Autonomous financial R&D: discovery of emerging threats, macro dynamics & CBDCs", "tab_p3_research"),
    COMPLIANCE_ENGINE("19. Autonomous Compliance", Icons.Default.Rule, ElectricBlue, "Real-time AML, KYC, GDPR, RBI, SEC & Cross-Border Sovereign legal enforcement", "tab_p3_compliance"),
    PLANETARY_NETWORK("20. Planetary Network", Icons.Default.RocketLaunch, PurpleTech, "Interplanetary banking: Moon bases, Mars settlements, asteroid mining & space commerce", "tab_p3_planetary")
}

@Composable
fun FuturePillarsSet3Screen(
    viewModel: BankViewModel,
    initialPillar: PillarSet3Page = PillarSet3Page.MARKETPLACE,
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

        // --- 4 Dedicated Pillar Tabs ---
        ScrollableTabRow(
            selectedTabIndex = PillarSet3Page.values().indexOf(selectedPillar),
            containerColor = NavyCard,
            contentColor = selectedPillar.color,
            edgePadding = 4.dp,
            indicator = { tabPositions ->
                val idx = PillarSet3Page.values().indexOf(selectedPillar)
                if (idx in tabPositions.indices) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[idx]),
                        color = selectedPillar.color,
                        height = 3.dp
                    )
                }
            }
        ) {
            PillarSet3Page.values().forEach { page ->
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
            label = "PillarSet3Transition",
            modifier = Modifier.weight(1f)
        ) { targetPillar ->
            when (targetPillar) {
                PillarSet3Page.MARKETPLACE -> PillarPageMarketplace()
                PillarSet3Page.RESEARCH_CENTER -> PillarPageResearchCenter()
                PillarSet3Page.COMPLIANCE_ENGINE -> PillarPageComplianceEngine()
                PillarSet3Page.PLANETARY_NETWORK -> PillarPagePlanetaryNetwork()
            }
        }
    }
}

// -------------------------------------------------------------
// PILLAR 17: AI MARKETPLACE & PLUGIN PUBLISHING APPLICATION FORM
// -------------------------------------------------------------
@Composable
private fun PillarPageMarketplace() {
    var installedPlugin by remember { mutableStateOf(false) }
    var showApplicationForm by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Pillar3InfoCard(
                title = "Global Decentralized Financial App Store",
                subtitle = "Ecosystem where fintech engineers publish specialized AI models, tax modules, and automated arbitrage algorithms.",
                icon = Icons.Default.Storefront,
                color = GoldAccent
            )
        }
        item {
            Pillar3PluginCard(
                name = "DeepAlpha Arbitrage Bot v4.2",
                author = "QuantLabs Zurich • 4.9 ★ (18,400 Installs)",
                desc = "Autonomous cross-exchange yield harvester with built-in flash-loan slippage protection.",
                price = "Free Tier Included",
                installed = installedPlugin,
                onInstall = { installedPlugin = !installedPlugin }
            )
        }
        item {
            Pillar3PluginCard(
                name = "Cross-Border Tax Shield Plugin",
                author = "Deloitte Sovereign AI • 4.95 ★",
                desc = "Real-time dual-citizenship expat tax credits and foreign earned income exclusion (FEIE) optimizer.",
                price = "Active (Verified)",
                installed = true,
                onInstall = {}
            )
        }
        item {
            Pillar3ApplicationSection(
                formTitle = "Publish AI Financial Plugin to Global FinOS Marketplace",
                formSubtitle = "Submit your algorithmic trading model, budget bot, or tax optimizer for automated sandbox backtesting and smart contract royalties.",
                color = GoldAccent,
                isOpen = showApplicationForm,
                onToggle = { showApplicationForm = !showApplicationForm }
            ) {
                PublishPluginForm()
            }
        }
    }
}

@Composable
private fun PublishPluginForm() {
    var pluginName by remember { mutableStateOf("HyperYield Flash-Loan Harvester") }
    var revenueSplit by remember { mutableFloatStateOf(85f) }
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
                OutlinedTextField(
                    value = pluginName,
                    onValueChange = { pluginName = it },
                    label = { Text("Plugin / AI Algorithm Name", color = TextMuted, fontSize = 10.sp) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Developer Royalty Payout Split: ${revenueSplit.toInt()}%", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Slider(
                    value = revenueSplit,
                    onValueChange = { revenueSplit = it },
                    valueRange = 70f..95f,
                    steps = 5,
                    colors = SliderDefaults.colors(thumbColor = GoldAccent, activeTrackColor = GoldAccent)
                )
                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_plugin_application"),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Submit for Automated Sandbox Security Audit & Publish", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = GoldAccent)
                    Text("Running Zero-Knowledge Code Audit & 10-Year Backtest...", color = GoldAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                    Text("PLUGIN VERIFIED & PUBLISHED TO STORE", color = GoldAccent, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("Royalty Smart Contract #SPL-4481 Deployed • Live for 500M Global Users", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldAccent),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Publish Another Module", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// PILLAR 18: AI RESEARCH CENTER & R&D GRANT APPLICATION FORM
// -------------------------------------------------------------
@Composable
private fun PillarPageResearchCenter() {
    var showApplicationForm by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Pillar3InfoCard(
                title = "Autonomous Financial Laboratory & R&D Center",
                subtitle = "Self-updating intelligence engine synthesizing real-time whitepapers, novel fraud topologies, and tokenized CBDC dynamics.",
                icon = Icons.Default.Science,
                color = CyberCyan
            )
        }
        item {
            Pillar3MetricsGrid(
                title = "Active Autonomous Research Streams",
                items = listOf(
                    "Paper #842 (Published 2h ago)" to "Post-Quantum Smart Contract Attack Vectors & Lattice Shields",
                    "Paper #841 (Published 12h ago)" to "Zero-Knowledge Proofs in Multi-Jurisdiction Sovereign Settlement",
                    "Paper #840 (Published 1d ago)" to "Algorithmic CBDC Velocity Modeling & Inflation Control",
                    "Platform Auto-Update Status" to "Core algorithms patched 48 seconds ago"
                )
            )
        }
        item {
            Pillar3MetricsGrid(
                title = "Laboratory Sandbox & Stress Testing",
                items = listOf(
                    "Simulated Quantum Attacks" to "1,000,000 Penetration tests completed (0 Breaches)",
                    "Macro Shocks Tested" to "Hyperinflation, oil embargo, semiconductor grid freeze",
                    "AI Model Self-Correction Rate" to "99.98% Autonomous Model Tuning"
                )
            )
        }
        item {
            Pillar3ApplicationSection(
                formTitle = "Request R&D Compute Grant & Quantum Sandbox Access",
                formSubtitle = "Apply for sovereign supercomputing allocations to simulate macro economic policies, novel consensus, and zero-knowledge financial cryptography.",
                color = CyberCyan,
                isOpen = showApplicationForm,
                onToggle = { showApplicationForm = !showApplicationForm }
            ) {
                ResearchGrantApplicationForm()
            }
        }
    }
}

@Composable
private fun ResearchGrantApplicationForm() {
    var researchField by remember { mutableStateOf("Zero-Knowledge Sovereign Proofs") }
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
                Text("Select Autonomous Research Discipline", color = TextMuted, fontSize = 11.sp)
                listOf("Zero-Knowledge Sovereign Proofs", "Algorithmic CBDC Velocity", "Post-Quantum Smart Contracts").forEach { field ->
                    val sel = researchField == field
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (sel) CyberCyan.copy(alpha = 0.25f) else Navy800,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (sel) CyberCyan else Navy700),
                        modifier = Modifier.fillMaxWidth().clickable { researchField = field }
                    ) {
                        Text(
                            text = field,
                            color = if (sel) CyberCyan else TextMuted,
                            fontSize = 10.sp,
                            fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                        )
                    }
                }
                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_research_application"),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Allocate 10,000 QPU-Hours Compute Grant", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = CyberCyan)
                    Text("Provisioning Quantum Supercomputer Cluster & Research Node...", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                    Text("RESEARCH COMPUTING GRANT GRANTED", color = CyberCyan, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("Node #LAB-992-QPU Ready • Autonomous Peer Review Pipeline Enabled", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Submit Another Research Proposal", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// PILLAR 19: AUTONOMOUS COMPLIANCE ENGINE & FAST-TRACK FORM
// -------------------------------------------------------------
@Composable
private fun PillarPageComplianceEngine() {
    var showApplicationForm by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Pillar3InfoCard(
                title = "Continuous Sovereign Regulatory Compliance Engine",
                subtitle = "Automated zero-trust enforcement across AML, KYC, GDPR, RBI, SEC, MiCA, and FATF cross-border rules.",
                icon = Icons.Default.Rule,
                color = ElectricBlue
            )
        }
        item {
            Pillar3MetricsGrid(
                title = "Multi-Jurisdictional Regulatory Audits",
                items = listOf(
                    "Anti-Money Laundering (AML)" to "Real-time Graph Neural Network (0.00% False Flag Rate)",
                    "Know Your Customer (KYC / KYB)" to "100% Zero-Knowledge Biometric Proof of Personhood",
                    "Reserve Bank of India (RBI) Stance" to "Full Local Data Localization & UPI 2.0 Compliance",
                    "European Union GDPR & MiCA" to "Cryptographic Right to be Forgotten & MiCA Tier-1 Regulated",
                    "SEC / FINRA Disclosures" to "Audit-Proof Autonomous Daily Filing Logs"
                )
            )
        }
        item {
            Pillar3MetricsGrid(
                title = "Sanctions & Geopolitical Guardrails",
                items = listOf(
                    "OFAC / UN Sanctions List Sync" to "Live Sub-Second Real-Time Webhook Feed",
                    "Cross-Border Sovereign Tax Treaty" to "Automated CRS & FATCA XML generation",
                    "Compliance Overhead Cost" to "$0 (100% Autonomous Software Execution)"
                )
            )
        }
        item {
            Pillar3ApplicationSection(
                formTitle = "Apply for Global Fast-Track Regulatory Exemption Sandbox",
                formSubtitle = "Instantly clear compliance across 42 jurisdictions simultaneously using zero-knowledge automated AML proofs.",
                color = ElectricBlue,
                isOpen = showApplicationForm,
                onToggle = { showApplicationForm = !showApplicationForm }
            ) {
                ComplianceFastTrackForm()
            }
        }
    }
}

@Composable
private fun ComplianceFastTrackForm() {
    var targetJurisdiction by remember { mutableStateOf("Global Multi-Jurisdiction (US + EU + RBI + MAS)") }
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
                OutlinedTextField(
                    value = targetJurisdiction,
                    onValueChange = { targetJurisdiction = it },
                    label = { Text("Regulatory Sandbox Frameworks", color = TextMuted, fontSize = 10.sp) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlue, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_compliance_application"),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Execute Real-Time GNN AML & Sanctions Audit", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = ElectricBlue)
                    Text("Scanning 500,000 Sanctions Watchlists & FATF Cross-Border Rules...", color = ElectricBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                    Text("GLOBAL REGULATORY CLEARANCE ISSUED", color = ElectricBlue, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("Passkey #CMP-9901 Active • 0 Violations across SEC, RBI, MiCA & FinCEN", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Export Audit Certificate", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// PILLAR 20: PLANETARY FINANCIAL NETWORK & SPACE COMMERCE FORM
// -------------------------------------------------------------
@Composable
private fun PillarPagePlanetaryNetwork() {
    var showApplicationForm by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Pillar3InfoCard(
                title = "Interplanetary Economy & Space Commerce Network",
                subtitle = "Banking beyond Earth: handles light-delay multi-planetary settlement, lunar settlements, orbital helium-3 & asteroid mining.",
                icon = Icons.Default.RocketLaunch,
                color = PurpleTech
            )
        }
        item {
            Pillar3MetricsGrid(
                title = "Planetary Nodes & Orbital Asset Ledgers",
                items = listOf(
                    "Moon Artemis Base Node" to "1.28-Second Light Latency Proof-of-Stake Sync",
                    "Mars Colony One Gateway" to "4–24 Minute Asynchronous Time-Lock Consensus",
                    "Asteroid Mining Claims (Psyche 16)" to "Tokenized Rare Earth & Platinum Reserves ($10B+ Asset Backed)",
                    "Space Tourism Orbital Pass" to "Universal Zero-G Escrow Booking Protocol"
                )
            )
        }
        item {
            Pillar3MetricsGrid(
                title = "Interplanetary Reserve Currency (SOLAR)",
                items = listOf(
                    "Standard Monetary Unit" to "1.0 SOLAR = 1 Kilowatt-Hour of Orbital Solar Energy",
                    "Solar Flare Cosmic Ray Shielding" to "Triple-Redundant Deep Space Radiation Proof Nodes",
                    "Lunar Helium-3 Energy Credits" to "+14.8% Annualized Off-Earth Trade Growth"
                )
            )
        }
        item {
            Pillar3ApplicationSection(
                formTitle = "Apply for Artemis Lunar Base & Orbital Solar Node Escrow",
                formSubtitle = "Tokenize orbital satellite assets, open off-Earth SOLAR currency accounts, and claim asteroid mining fractional deeds.",
                color = PurpleTech,
                isOpen = showApplicationForm,
                onToggle = { showApplicationForm = !showApplicationForm }
            ) {
                PlanetaryEscrowApplicationForm()
            }
        }
    }
}

@Composable
private fun PlanetaryEscrowApplicationForm() {
    var planetaryDestination by remember { mutableStateOf("Moon Artemis Base Basecamp 1") }
    var solarCreditAmount by remember { mutableFloatStateOf(5000f) }
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
                Text("Select Planetary Node Destination", color = TextMuted, fontSize = 11.sp)
                listOf("Moon Artemis Base Camp", "Mars Colony One Gateway", "Asteroid Psyche 16 Mining Claim").forEach { dest ->
                    val sel = planetaryDestination.startsWith(dest.substring(0, 4))
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (sel) PurpleTech.copy(alpha = 0.25f) else Navy800,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (sel) PurpleTech else Navy700),
                        modifier = Modifier.fillMaxWidth().clickable { planetaryDestination = dest }
                    ) {
                        Text(
                            text = dest,
                            color = if (sel) PurpleTech else TextMuted,
                            fontSize = 10.sp,
                            fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                        )
                    }
                }
                Text("Orbital Solar Energy Currency: ${solarCreditAmount.toInt()} SOLAR (kWh)", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Slider(
                    value = solarCreditAmount,
                    onValueChange = { solarCreditAmount = it },
                    valueRange = 1000f..50000f,
                    steps = 49,
                    colors = SliderDefaults.colors(thumbColor = PurpleTech, activeTrackColor = PurpleTech)
                )
                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_space_application"),
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleTech),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Transmit Interplanetary Asynchronous Escrow Proof", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = PurpleTech)
                    Text("Transmitting Deep Space Optical Laser Sync (1.28s Light Delay)...", color = PurpleTech, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().clip(CircleShape), color = PurpleTech, trackColor = Navy800)
                }
            }
            "APPROVED" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SuccessCelebrationParticles(modifier = Modifier.size(60.dp))
                    Text("PLANETARY ESCROW VAULT ACTIVE", color = PurpleTech, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("${solarCreditAmount.toInt()} SOLAR Units Escrowed at $planetaryDestination • Time-Lock Verified", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PurpleTech),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Add Interplanetary Asset", fontSize = 10.sp)
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
private fun Pillar3ApplicationSection(
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
private fun Pillar3InfoCard(title: String, subtitle: String, icon: ImageVector, color: Color) {
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
private fun Pillar3MetricsGrid(title: String, items: List<Pair<String, String>>) {
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
private fun Pillar3PluginCard(
    name: String,
    author: String,
    desc: String,
    price: String,
    installed: Boolean,
    onInstall: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Navy900),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (installed) EmeraldSuccess.copy(alpha = 0.4f) else Navy700)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(author, color = GoldAccent, fontSize = 10.sp)
                }
                Surface(
                    color = if (installed) EmeraldSuccess.copy(alpha = 0.15f) else GoldAccent.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = if (installed) "Active" else price,
                        color = if (installed) EmeraldSuccess else GoldAccent,
                        fontWeight = FontWeight.Bold,
                        fontSize = 10.sp,
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
            Text(desc, color = TextMuted, fontSize = 10.sp)
            Button(
                onClick = onInstall,
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = if (installed) NavyCard else GoldAccent),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = if (installed) "Uninstall Plugin" else "Install & Activate in OS",
                    color = if (installed) TextWhite else Navy900,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }
    }
}
