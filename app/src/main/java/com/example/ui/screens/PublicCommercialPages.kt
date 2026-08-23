package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.AnimatedScannerBeam
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

// =========================================================================
// 7. PRICING & TIER PLANS (WITH DYNAMIC QUOTE & CHECKOUT FORM)
// =========================================================================
@Composable
fun PricingScreen(
    viewModel: BankViewModel,
    onNavigateBack: () -> Unit,
    onNavigateToPaidEdition: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTier by remember { mutableStateOf("Enterprise Pro") }
    var userVolume by remember { mutableFloatStateOf(2500f) }
    var showCheckoutForm by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            PublicPageHeader(
                title = "Transparent Sovereign Pricing",
                subtitle = "From free citizen wealth advisory to central bank planetary licenses",
                icon = Icons.Default.MonetizationOn,
                color = GoldAccent,
                onBack = onNavigateBack
            )
        }

        // --- Direct Paid Version & Download License Pass Banner ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, GoldAccent)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(GoldAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(26.dp))
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text("Looking for PFIN Paid Edition?", color = TextWhite, fontWeight = FontWeight.Black, fontSize = 13.sp)
                        Text("Download activation, monthly/annual passes & lifetime licenses ($9.99/mo to $199 lifetime).", color = TextMuted, fontSize = 10.sp)
                    }

                    Button(
                        onClick = onNavigateToPaidEdition,
                        colors = ButtonDefaults.buttonColors(containerColor = GoldAccent, contentColor = Navy900),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("pricing_to_paid_edition_btn")
                    ) {
                        Text("Get Pro", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }
        }

        val tiers = listOf(
            PricingTier("Retail Citizen", "$0", "Forever Free", listOf("AGI Financial Advisor", "World Digital ID", "Smart Budgeting", "Standard Vault Security"), CyberCyan),
            PricingTier("Enterprise Pro", "$499", "/ month", listOf("Autonomous CFO Payroll", "Supply Factoring", "Automated Corporate Tax", "Dedicated AI Underwriting"), GoldAccent),
            PricingTier("Central Bank / Sovereign", "$9,999", "/ month", listOf("Programmable CBDC Bridge", "Zero-Leakage Welfare Rail", "National Development Modeling", "Dedicated Quantum VPC"), PurpleTech),
            PricingTier("Interplanetary Node", "$25,000", "per orbital node", listOf("Time-Lock Light Latency Sync", "Asteroid Deed Tokenization", "Helium-3 Escrow Rails", "Radiation-Hardened Relay"), ElectricBlue)
        )

        items(tiers) { tier ->
            val isSelected = selectedTier == tier.name
            Card(
                modifier = Modifier.fillMaxWidth().clickable { selectedTier = tier.name },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = if (isSelected) NavyCard else Navy900),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, if (isSelected) tier.accentColor else Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(tier.name, color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(tier.price, color = tier.accentColor, fontWeight = FontWeight.Black, fontSize = 18.sp)
                            Text(" ${tier.period}", color = TextMuted, fontSize = 10.sp)
                        }
                    }
                    tier.features.forEach { feat ->
                        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = tier.accentColor, modifier = Modifier.size(13.dp))
                            Text(feat, color = TextWhite, fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // --- Interactive Dynamic Quotation & Checkout Form ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { showCheckoutForm = !showCheckoutForm }.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Custom Tier Quotation & Instant License Provisioning", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Calculate ROI and activate instant smart contract escrow license.", color = TextMuted, fontSize = 10.sp)
                        }
                        Surface(color = GoldAccent.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text(
                                text = if (showCheckoutForm) "Hide Form" else "Get Quote",
                                color = GoldAccent,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    AnimatedVisibility(visible = showCheckoutForm) {
                        PricingQuotationForm(selectedTier = selectedTier)
                    }
                }
            }
        }
    }
}

private data class PricingTier(
    val name: String,
    val price: String,
    val period: String,
    val features: List<String>,
    val accentColor: Color
)

@Composable
private fun PricingQuotationForm(selectedTier: String) {
    var volumeScale by remember { mutableFloatStateOf(5000f) }
    var billingCycle by remember { mutableStateOf("Annual (20% Sovereign Discount)") }
    var formState by remember { mutableStateOf("DRAFT") }

    if (formState == "SCANNING") {
        LaunchedEffect(Unit) {
            delay(1900)
            formState = "APPROVED"
        }
    }

    Column(modifier = Modifier.background(Navy900).padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        when (formState) {
            "DRAFT" -> {
                Text("Selected License Tier: $selectedTier", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text("Monthly Active Users / Transactions: ${volumeScale.toInt()}", color = TextWhite, fontSize = 11.sp)
                Slider(
                    value = volumeScale,
                    onValueChange = { volumeScale = it },
                    valueRange = 500f..50000f,
                    steps = 19,
                    colors = SliderDefaults.colors(thumbColor = GoldAccent, activeTrackColor = GoldAccent)
                )

                Text("Billing Cycle", color = TextMuted, fontSize = 10.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Annual (-20%)", "Monthly Standard").forEach { cycle ->
                        val sel = billingCycle.startsWith(cycle.substring(0, 4))
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (sel) GoldAccent.copy(alpha = 0.25f) else Navy800,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (sel) GoldAccent else Navy700),
                            modifier = Modifier.clickable { billingCycle = cycle }
                        ) {
                            Text(
                                text = cycle,
                                color = if (sel) GoldAccent else TextMuted,
                                fontSize = 10.sp,
                                fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_pricing_quote"),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Provision Autonomous License Smart Contract", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = GoldAccent)
                    Text("Generating Cryptographic Tier License & Escrow...", color = GoldAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().clip(CircleShape), color = GoldAccent, trackColor = Navy800)
                }
            }
            "APPROVED" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SuccessCelebrationParticles(modifier = Modifier.size(60.dp))
                    Text("LICENSE PROVISIONED SUCCESSFULLY", color = GoldAccent, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("License #LIC-7749 Active • Tier: $selectedTier • 99.999% SLA Guaranteed", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldAccent),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Re-calculate Quote", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// =========================================================================
// 8. SUCCESS STORIES & CASE STUDIES SCREEN
// =========================================================================
@Composable
fun SuccessStoriesScreen(
    viewModel: BankViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showCaseStudyInquiry by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            PublicPageHeader(
                title = "Global Case Studies",
                subtitle = "Real-world transformation across smart cities, central banks, and clean energy grids",
                icon = Icons.Default.Star,
                color = EmeraldSuccess,
                onBack = onNavigateBack
            )
        }

        val caseStudies = listOf(
            CaseStudyItem(
                title = "Metropolitan City of Neo-Tokyo",
                sector = "Smart City Transit & Utilities",
                metrics = listOf("12M Residents Connected", "0ms Turnstile Latency", "$42M Annual Tax Admin Saved"),
                description = "Replaced 6 disconnected civic ticketing and utility billing networks with one unified PFIN Municipal NFC Smart Pass.",
                color = CyberCyan
            ),
            CaseStudyItem(
                title = "Pacific Green Sovereign Climate Fund",
                sector = "Parametric Agriculture & Solar",
                metrics = listOf("$500M Green Bond Issued", "100% Autonomous Payout", "48hr Flood Claim Settlement"),
                description = "Deployed real-time satellite NDVI telemetry to automate parametric crop insurance payouts with zero adjusters required.",
                color = EmeraldSuccess
            ),
            CaseStudyItem(
                title = "Artemis Lunar Base Mining Consortium",
                sector = "Interplanetary Commerce",
                metrics = listOf("1.28s Sync Delay Managed", "10,000 SOLAR Kilowatt Units Escrowed", "Zero Ledger Forks"),
                description = "Established the first orbital light-latency tolerant escrow ledger for off-Earth mining equipment and solar power exchange.",
                color = PurpleTech
            )
        )

        items(caseStudies) { cs ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, cs.color.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(cs.title, color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                        Surface(color = cs.color.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text(cs.sector, color = cs.color, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                        }
                    }
                    Text(cs.description, color = TextMuted, fontSize = 11.sp, lineHeight = 14.sp)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        cs.metrics.forEach { m ->
                            Surface(color = Navy900, shape = RoundedCornerShape(6.dp), border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)) {
                                Text(m, color = cs.color, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 4.dp))
                            }
                        }
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { showCaseStudyInquiry = !showCaseStudyInquiry }.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Download Full Technical Whitepaper Case Studies", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Receive deep architectural reports on real-world deployments.", color = TextMuted, fontSize = 10.sp)
                        }
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text(
                                text = if (showCaseStudyInquiry) "Hide Form" else "Request Pack",
                                color = EmeraldSuccess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    AnimatedVisibility(visible = showCaseStudyInquiry) {
                        CaseStudyRequestForm()
                    }
                }
            }
        }
    }
}

private data class CaseStudyItem(
    val title: String,
    val sector: String,
    val metrics: List<String>,
    val description: String,
    val color: Color
)

@Composable
private fun CaseStudyRequestForm() {
    var email by remember { mutableStateOf("institution@globalgov.org") }
    var formState by remember { mutableStateOf("DRAFT") }

    if (formState == "SCANNING") {
        LaunchedEffect(Unit) {
            delay(1700)
            formState = "APPROVED"
        }
    }

    Column(modifier = Modifier.background(Navy900).padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        when (formState) {
            "DRAFT" -> {
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Institutional Email Address", color = TextMuted, fontSize = 10.sp) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldSuccess, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_case_study_request"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Instant Download 2050 Architecture Compendium", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = EmeraldSuccess)
                    Text("Compiling 32-Page Cryptographic Report...", color = EmeraldSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().clip(CircleShape), color = EmeraldSuccess, trackColor = Navy800)
                }
            }
            "APPROVED" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SuccessCelebrationParticles(modifier = Modifier.size(60.dp))
                    Text("REPORT DISPATCHED TO EMAIL", color = EmeraldSuccess, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("Secure PDF link dispatched to $email", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldSuccess),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Send to Another Email", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// =========================================================================
// 9. BLOG & PRESS INSIGHTS SCREEN
// =========================================================================
@Composable
fun BlogInsightsScreen(
    viewModel: BankViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PublicPageHeader(
                title = "PFIN Insights & Thought Leadership",
                subtitle = "Frontier research at the intersection of AGI, quantum security & planetary economics",
                icon = Icons.Default.MenuBook,
                color = CyberCyan,
                onBack = onNavigateBack
            )
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search articles, topics or whitepapers...", color = TextMuted, fontSize = 11.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(16.dp)) },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        val articles = listOf(
            BlogPost("Why Post-Quantum Cryptography is Non-Negotiable for 2050 Banking", "Security & Quantum Lab", "4 min read • Today", CyberCyan),
            BlogPost("Parametric Weather Insurance: How Satellites Replace Loss Adjusters", "Climate Intelligence Team", "6 min read • 2 days ago", EmeraldSuccess),
            BlogPost("The 4-Minute Mars Settlement Delay: Designing Interplanetary Ledgers", "Space Commerce Lab", "8 min read • 1 week ago", PurpleTech),
            BlogPost("Continuous Payroll: Why Bi-Weekly Paychecks are Obsolete in 2050", "Autonomous Business Group", "5 min read • 2 weeks ago", GoldAccent)
        )

        items(articles.filter { it.title.contains(searchQuery, ignoreCase = true) || it.category.contains(searchQuery, ignoreCase = true) }) { art ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, art.accentColor.copy(alpha = 0.35f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(art.category, color = art.accentColor, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                        Text(art.readTime, color = TextMuted, fontSize = 9.sp)
                    }
                    Text(art.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 16.sp)
                }
            }
        }
    }
}

private data class BlogPost(
    val title: String,
    val category: String,
    val readTime: String,
    val accentColor: Color
)

// =========================================================================
// 10. RESEARCH CENTER SCREEN (WITH R&D COMPUTE APPLICATION FORM)
// =========================================================================
@Composable
fun ResearchCenterScreen(
    viewModel: BankViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showGrantForm by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            PublicPageHeader(
                title = "Autonomous Research Center",
                subtitle = "PFIN Institute for Macro Intelligence, Cryptography & Deep Space Economies",
                icon = Icons.Default.Science,
                color = ElectricBlue,
                onBack = onNavigateBack
            )
        }

        val papers = listOf(
            ResearchPaperItem("PFIN-WP-2050-01", "Kyber-1024 Post-Quantum Defense Against Shor Decryption in Real-Time Banking", "NIST Lattice Level 5", ElectricBlue),
            ResearchPaperItem("PFIN-WP-2050-02", "Zero-Knowledge Sovereign Identity Verification across 42 Concurrent Central Banks", "ZK-SNARK / PLONK", PurpleTech),
            ResearchPaperItem("PFIN-WP-2050-03", "Asynchronous Light-Delay Time-Lock Consensus Protocols for Moon & Mars Settlements", "Interplanetary POS", GoldAccent)
        )

        items(papers) { paper ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, paper.color.copy(alpha = 0.4f))
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(paper.code, color = paper.color, fontWeight = FontWeight.ExtraBold, fontSize = 10.sp)
                        Surface(color = paper.color.copy(alpha = 0.2f), shape = RoundedCornerShape(4.dp)) {
                            Text(paper.standard, color = paper.color, fontSize = 9.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp))
                        }
                    }
                    Text(paper.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp, lineHeight = 15.sp)
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlue.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { showGrantForm = !showGrantForm }.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Apply for Academic QPU Quantum Sandbox Grant", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Access 10,000 QPU-hours for sovereign cryptographic research.", color = TextMuted, fontSize = 10.sp)
                        }
                        Surface(color = ElectricBlue.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text(
                                text = if (showGrantForm) "Hide Form" else "Apply for Grant",
                                color = ElectricBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    AnimatedVisibility(visible = showGrantForm) {
                        AcademicGrantForm()
                    }
                }
            }
        }
    }
}

private data class ResearchPaperItem(
    val code: String,
    val title: String,
    val standard: String,
    val color: Color
)

@Composable
private fun AcademicGrantForm() {
    var universityName by remember { mutableStateOf("MIT / ETH Zurich Quantum Lab") }
    var formState by remember { mutableStateOf("DRAFT") }

    if (formState == "SCANNING") {
        LaunchedEffect(Unit) {
            delay(1900)
            formState = "APPROVED"
        }
    }

    Column(modifier = Modifier.background(Navy900).padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        when (formState) {
            "DRAFT" -> {
                OutlinedTextField(
                    value = universityName,
                    onValueChange = { universityName = it },
                    label = { Text("University / Research Institute", color = TextMuted, fontSize = 10.sp) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlue, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_academic_grant"),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Submit Proposal for Autonomous Peer Review", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = ElectricBlue)
                    Text("Verifying Academic Credentials & Sandbox Limits...", color = ElectricBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth().clip(CircleShape), color = ElectricBlue, trackColor = Navy800)
                }
            }
            "APPROVED" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    SuccessCelebrationParticles(modifier = Modifier.size(60.dp))
                    Text("ACADEMIC GRANT APPROVED (10,000 QPU-HOURS)", color = ElectricBlue, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("Allocated to $universityName • Node #ETH-MIT-QPU Live", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Submit Another Proposal", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}
