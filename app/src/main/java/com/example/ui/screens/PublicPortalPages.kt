package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
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
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.FlightTakeoff
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.Visibility
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
import androidx.compose.ui.graphics.Brush
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

// =========================================================================
// 1. PUBLIC LANDING PAGE
// =========================================================================
@Composable
fun LandingPageScreen(
    viewModel: BankViewModel,
    onNavigateToPublicPage: (String) -> Unit,
    onOpenAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // --- Hero Headline Banner ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth().testTag("landing_hero_card"),
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, CyberCyan.copy(alpha = 0.7f))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.verticalGradient(
                                listOf(CyberCyan.copy(alpha = 0.15f), NavyCard)
                            )
                        )
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Surface(
                            color = CyberCyan.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan)
                        ) {
                            Text(
                                text = "PFIN • GLOBAL FINANCIAL OS 2050–2077",
                                color = CyberCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 6.dp)
                    }

                    Text(
                        text = "The Operating System for the World's Financial Future",
                        color = TextWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        lineHeight = 28.sp
                    )

                    Text(
                        text = "A self-learning, AI-driven Planetary Financial Intelligence Network connecting people, enterprises, governments, smart cities, healthcare, climate systems, and interplanetary commerce.",
                        color = TextMuted,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onOpenAccount,
                            colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).testTag("landing_cta_get_started")
                        ) {
                            Text("Launch Sovereign ID", color = Navy900, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                        }
                        OutlinedButton(
                            onClick = { onNavigateToPublicPage("about_pfin") },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = TextWhite),
                            border = androidx.compose.foundation.BorderStroke(1.dp, Navy700),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier.weight(1f).testTag("landing_cta_learn_more")
                        ) {
                            Text("About PFIN", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // --- Live Global Telemetry Ticker ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    LandingMetricItem("Global Volume", "$14.8T / Day", EmeraldSuccess)
                    LandingMetricItem("AI Threat Intercepts", "99.999%", ElectricBlue)
                    LandingMetricItem("Connected Cities", "148 Smart Nodes", CyberCyan)
                    LandingMetricItem("Lattice Security", "Kyber-1024", GoldAccent)
                }
            }
        }

        // --- Quick Public Navigation Links ---
        item {
            Text("EXPLORE PFIN ECOSYSTEM", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.2.sp)
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LandingQuickCard("Vision & Mission", Icons.Default.Explore, PurpleTech, Modifier.weight(1f)) {
                    onNavigateToPublicPage("vision_mission")
                }
                LandingQuickCard("20 Features", Icons.Default.RocketLaunch, GoldAccent, Modifier.weight(1f)) {
                    onNavigateToPublicPage("features_overview")
                }
                LandingQuickCard("Solutions", Icons.Default.Handshake, ElectricBlue, Modifier.weight(1f)) {
                    onNavigateToPublicPage("solutions")
                }
            }
        }

        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                LandingQuickCard("Industries", Icons.Default.Apartment, EmeraldSuccess, Modifier.weight(1f)) {
                    onNavigateToPublicPage("industries")
                }
                LandingQuickCard("Pricing Plans", Icons.Default.TrendingUp, CyberCyan, Modifier.weight(1f)) {
                    onNavigateToPublicPage("pricing")
                }
                LandingQuickCard("Success Stories", Icons.Default.Star, AmberOrange, Modifier.weight(1f)) {
                    onNavigateToPublicPage("success_stories")
                }
            }
        }

        // --- Testimonial / Endorsement ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                        Text("GLOBAL REGULATORY & CENTRAL BANK ACCREDITATION", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(
                        text = "\"PFIN represents the definitive transition from static commercial banking to an autonomous, planetary-scale economic nervous system.\"",
                        color = TextWhite,
                        fontSize = 12.sp,
                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                    )
                    Text(
                        text = "— World Financial Technology Council & Global Sovereign Monetary Forum",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun LandingMetricItem(label: String, value: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(value, color = color, fontSize = 12.sp, fontWeight = FontWeight.ExtraBold)
        Text(label, color = TextMuted, fontSize = 9.sp)
    }
}

@Composable
private fun LandingQuickCard(
    title: String,
    icon: ImageVector,
    color: Color,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Surface(
        modifier = modifier.clickable { onClick() },
        color = NavyCard,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.35f))
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Box(
                modifier = Modifier.size(32.dp).clip(CircleShape).background(color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(18.dp))
            }
            Text(title, color = TextWhite, fontSize = 10.sp, fontWeight = FontWeight.Bold, maxLines = 1)
        }
    }
}

// =========================================================================
// 2. ABOUT PFIN SCREEN
// =========================================================================
@Composable
fun AboutPfinScreen(
    viewModel: BankViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            PublicPageHeader(
                title = "About PFIN",
                subtitle = "Planetary Financial Intelligence Network (Est. 2024 • Operating Horizon 2050–2077)",
                icon = Icons.Default.Public,
                color = CyberCyan,
                onBack = onNavigateBack
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("PIONEERING THE WORLD'S FINANCIAL OS", color = CyberCyan, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    Text(
                        text = "Founded by leading quantum cryptographers, macro-economists, and artificial general intelligence researchers, PFIN is built to unify fragmented global financial systems into one interoperable, ethical, and quantum-resistant network.",
                        color = TextWhite,
                        fontSize = 12.sp,
                        lineHeight = 16.sp
                    )
                    Text(
                        text = "From high-frequency algorithmic liquidity to disaster-relief distribution and interplanetary asset settlement, PFIN provides the foundational rails for the next half-century of human commerce.",
                        color = TextMuted,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        item {
            Text("PFIN EVOLUTIONARY TIMELINE", color = GoldAccent, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                TimelineItem("2024–2030", "Genesis of Zero-Trust AI Banking & Real-Time AML Graph Networks", CyberCyan)
                TimelineItem("2031–2040", "Integration of Smart City Municipal Wallets & Genomic Health Escrows", EmeraldSuccess)
                TimelineItem("2041–2050", "Autonomous Sovereign Treasuries, Digital Twin Macro Models & CBDC Bridges", PurpleTech)
                TimelineItem("2051–2077+", "Planetary Financial Network: Lunar Basecamp Nodes & Asteroid Mining Deeds", GoldAccent)
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Global Regulatory Governance", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text("• Fully licensed across US FinCEN, EU MiCA Tier-1, RBI Local Framework, and MAS Global Sandbox.", color = TextMuted, fontSize = 11.sp)
                    Text("• NIST Certified Post-Quantum Cryptography (Kyber-1024 / Dilithium-5).", color = TextMuted, fontSize = 11.sp)
                    Text("• Sovereign Zero-Knowledge Proof validation protecting 100% of user data rights.", color = TextMuted, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun TimelineItem(period: String, description: String, color: Color) {
    Surface(
        color = NavyCard,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.4f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                color = color.copy(alpha = 0.2f),
                shape = RoundedCornerShape(8.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, color)
            ) {
                Text(
                    text = period,
                    color = color,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 11.sp,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Text(description, color = TextWhite, fontSize = 11.sp, modifier = Modifier.weight(1f))
        }
    }
}

// =========================================================================
// 3. VISION & MISSION SCREEN
// =========================================================================
@Composable
fun VisionMissionScreen(
    viewModel: BankViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            PublicPageHeader(
                title = "Vision & Mission",
                subtitle = "The 2050–2077 Planetary Financial Operating System Manifesto",
                icon = Icons.Default.Explore,
                color = PurpleTech,
                onBack = onNavigateBack
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, PurpleTech.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("OUR GRAND VISION", color = PurpleTech, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    Text(
                        text = "To transform finance from an extractive transactional service into an intelligent, universal Operating System that shields humanity from systemic crises, democratizes sovereign wealth creation, and powers sustainable civilization on Earth and beyond.",
                        color = TextWhite,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        lineHeight = 18.sp
                    )
                }
            }
        }

        item {
            Text("THE 5 CORE PILLARS OF OUR MISSION", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                MissionCard(
                    number = "01",
                    title = "Autonomous Economic Protection",
                    body = "Predict recessions, asset bubbles, and climate shocks before wealth is destroyed, automating parametric buffers.",
                    color = CyberCyan
                )
                MissionCard(
                    number = "02",
                    title = "Universal Financial Inclusion",
                    body = "Give every human on Earth a zero-fee Sovereign World Digital Identity and algorithmic life-wealth advisor.",
                    color = EmeraldSuccess
                )
                MissionCard(
                    number = "03",
                    title = "Ecological & Climate Harmony",
                    body = "Direct global capital flows exclusively toward carbon-negative, regenerative, and clean energy infrastructures.",
                    color = GoldAccent
                )
                MissionCard(
                    number = "04",
                    title = "Post-Quantum Cryptographic Defense",
                    body = "Render all citizen and government assets permanently immune to quantum compute and adversarial attacks.",
                    color = ElectricBlue
                )
                MissionCard(
                    number = "05",
                    title = "Interplanetary Economic Continuity",
                    body = "Bridge asynchronous light-delay settlements between Earth, Lunar colonies, and Mars commercial nodes.",
                    color = PurpleTech
                )
            }
        }
    }
}

@Composable
private fun MissionCard(number: String, title: String, body: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Navy900),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.35f))
    ) {
        Row(modifier = Modifier.padding(14.dp), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(number, color = color, fontSize = 18.sp, fontWeight = FontWeight.Black)
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(body, color = TextMuted, fontSize = 11.sp, lineHeight = 14.sp)
            }
        }
    }
}

// =========================================================================
// 4. FEATURES OVERVIEW SCREEN
// =========================================================================
@Composable
fun FeaturesOverviewScreen(
    viewModel: BankViewModel,
    onNavigateBack: () -> Unit,
    onOpenPillar: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PublicPageHeader(
                title = "20 Next-Gen OS Pillars",
                subtitle = "Complete architectural directory of autonomous intelligence capabilities",
                icon = Icons.Default.RocketLaunch,
                color = GoldAccent,
                onBack = onNavigateBack
            )
        }

        val all20Features = listOf(
            Triple("1. AGI Financial Advisor", "Lifelong autonomous wealth strategist learning across decades", CyberCyan),
            Triple("2. Global Economic Engine", "Continuously predicts inflation, recessions & supply shocks", EmeraldSuccess),
            Triple("3. World Digital Identity", "One sovereign identity unifying banking, tax, health & credentials", ElectricBlue),
            Triple("4. AI Personal Life Planner", "Manages career, marriage, education & estate transitions", PurpleTech),
            Triple("5. Universal Wealth Engine", "Automated algorithmic yield hunting & expense optimization", GoldAccent),
            Triple("6. Global Crisis Intelligence", "Autonomous disaster relief, fraud freezes & sovereign coordination", CrimsonDanger),
            Triple("7. Climate Financial Intelligence", "Parametric crop insurance, flood risk & green energy bonds", EmeraldSuccess),
            Triple("8. Healthcare Financial Intelligence", "Genomic longevity HSA & automated zero-deductible clinic escrows", CrimsonDanger),
            Triple("9. Smart City Financial Network", "Unified municipal wallet for transit, EV charging & utilities", CyberCyan),
            Triple("10. Autonomous Business Banking", "Continuous payroll streaming, automated corporate tax & factoring", GoldAccent),
            Triple("11. AI Government Treasury", "Sovereign budget allocation, zero-leakage welfare & anti-evasion", PurpleTech),
            Triple("12. AI Startup Incubator", "TAM forecasting, VC matching & non-dilutive sovereign grant awards", GoldAccent),
            Triple("13. Digital Twin Economy", "100,000 parallel universe Monte Carlo macro simulations", CyberCyan),
            Triple("14. Quantum Banking", "Kyber-1024 lattice cryptography & entangled satellite key vaults", ElectricBlue),
            Triple("15. AI Financial Doctor", "Clinical diagnosis of debt addictions & prescriptive behavioral recovery", CrimsonDanger),
            Triple("16. Universal Reputation Score", "8-dimensional ethical trust rating beyond legacy credit scores", EmeraldSuccess),
            Triple("17. AI Marketplace", "Decentralized fintech app store for algorithms & trading bots", GoldAccent),
            Triple("18. AI Research Center", "Autonomous self-updating laboratory discovering novel protocols", CyberCyan),
            Triple("19. Autonomous Compliance", "Real-time automated AML, KYC, GDPR, RBI, SEC & MiCA clearance", ElectricBlue),
            Triple("20. Planetary Financial Network", "Interplanetary commerce: Moon basecamp, Mars & asteroid mining", PurpleTech)
        )

        items(all20Features) { (title, desc, color) ->
            FeatureRowCard(title = title, description = desc, color = color) {
                onOpenPillar("future_os")
            }
        }
    }
}

@Composable
private fun FeatureRowCard(title: String, description: String, color: Color, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(description, color = TextMuted, fontSize = 10.sp, lineHeight = 13.sp)
            }
            Icon(Icons.Default.ArrowForward, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
        }
    }
}

// =========================================================================
// 5. SOLUTIONS SCREEN (WITH INQUIRY / DEPLOYMENT APPLICATION FORM)
// =========================================================================
@Composable
fun SolutionsScreen(
    viewModel: BankViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showApplicationForm by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            PublicPageHeader(
                title = "Enterprise & Sovereign Solutions",
                subtitle = "Tailored deployment architectures for institutions, governments, and retail citizens",
                icon = Icons.Default.Handshake,
                color = ElectricBlue,
                onBack = onNavigateBack
            )
        }

        item {
            SolutionCategoryCard(
                category = "For Sovereign Governments & Central Banks",
                points = listOf(
                    "Programmable CBDC issuance with algorithmic inflation stabilizers",
                    "Zero-leakage direct welfare distribution to citizen digital wallets",
                    "Deep-learning sovereign tax fraud and offshore evasion shield"
                ),
                color = PurpleTech
            )
        }

        item {
            SolutionCategoryCard(
                category = "For Global Enterprises & Financial Institutions",
                points = listOf(
                    "Autonomous CFO continuous payroll and supplier factoring",
                    "Post-quantum lattice security migration for core banking databases",
                    "Multi-jurisdiction automated compliance engine (SEC, MiCA, RBI)"
                ),
                color = GoldAccent
            )
        }

        item {
            SolutionCategoryCard(
                category = "For Municipal Smart Cities",
                points = listOf(
                    "Unified civic wallet: Metro turnstiles, EV induction charging, smart utilities",
                    "Dynamic micro-tax accrual with zero lump-sum payment friction",
                    "Real-time municipal green bond issuance to local residents"
                ),
                color = CyberCyan
            )
        }

        item {
            SolutionCategoryCard(
                category = "For Space Agencies & Off-Earth Settlers",
                points = listOf(
                    "Asynchronous time-locked settlement tolerant of 4–24 min light delays",
                    "Tokenized asteroid mining claims backed by physical mineral reserves",
                    "SOLAR energy kilowatt-hour universal reserve unit"
                ),
                color = EmeraldSuccess
            )
        }

        // --- Interactive Solution Deployment Request Form ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, ElectricBlue.copy(alpha = 0.6f))
            ) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().clickable { showApplicationForm = !showApplicationForm }.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Request Custom Enterprise Architecture Deployment", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Submit institutional parameters for dedicated sandbox provisioning.", color = TextMuted, fontSize = 10.sp)
                        }
                        Surface(color = ElectricBlue.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text(
                                text = if (showApplicationForm) "Hide Form" else "Open Form",
                                color = ElectricBlue,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    AnimatedVisibility(visible = showApplicationForm) {
                        EnterpriseSolutionForm()
                    }
                }
            }
        }
    }
}

@Composable
private fun SolutionCategoryCard(category: String, points: List<String>, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(category, color = color, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            points.forEach { pt ->
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.Top) {
                    Text("•", color = color, fontSize = 12.sp)
                    Text(pt, color = TextWhite, fontSize = 11.sp, lineHeight = 14.sp)
                }
            }
        }
    }
}

@Composable
private fun EnterpriseSolutionForm() {
    var orgName by remember { mutableStateOf("Ministry of Finance / Sovereign Wealth Fund") }
    var userCount by remember { mutableFloatStateOf(50000f) }
    var formState by remember { mutableStateOf("DRAFT") }

    if (formState == "SCANNING") {
        LaunchedEffect(Unit) {
            delay(2100)
            formState = "APPROVED"
        }
    }

    Column(modifier = Modifier.background(Navy900).padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        when (formState) {
            "DRAFT" -> {
                OutlinedTextField(
                    value = orgName,
                    onValueChange = { orgName = it },
                    label = { Text("Institutional / Sovereign Entity Name", color = TextMuted, fontSize = 10.sp) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = ElectricBlue, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Target Citizen / Employee Userbase: ${userCount.toInt()}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                Slider(
                    value = userCount,
                    onValueChange = { userCount = it },
                    valueRange = 1000f..1000000f,
                    steps = 19,
                    colors = androidx.compose.material3.SliderDefaults.colors(thumbColor = ElectricBlue, activeTrackColor = ElectricBlue)
                )
                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_solution_request"),
                    colors = ButtonDefaults.buttonColors(containerColor = ElectricBlue),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Provision Dedicated Enterprise Sandbox Node", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = ElectricBlue)
                    Text("Synthesizing Sovereign VPC & Lattice Key Mesh...", color = ElectricBlue, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                    Text("SANDBOX CLUSTER ALLOCATED", color = ElectricBlue, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("Node #SOV-SANDBOX-8802 Deployed • Dedicated Lattice Gateway Active", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = ElectricBlue),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Configure Additional Parameters", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// =========================================================================
// 6. INDUSTRIES SCREEN (WITH PILOT APPLICATION FORM)
// =========================================================================
@Composable
fun IndustriesScreen(
    viewModel: BankViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var showPilotForm by remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            PublicPageHeader(
                title = "Vertical Industries",
                subtitle = "Customized economic intelligence tailored across 6 key global sectors",
                icon = Icons.Default.Apartment,
                color = EmeraldSuccess,
                onBack = onNavigateBack
            )
        }

        val industries = listOf(
            Triple("1. Banking, Fintech & DeFi", "High-frequency arbitrage, quantum vaults, real-time AML graph neural networks.", CyberCyan),
            Triple("2. Healthcare & Biotech", "Genomic actuarial modeling, zero-deductible clinical escrows & longevity HSAs.", CrimsonDanger),
            Triple("3. Renewable Energy & Climate", "Parametric catastrophe insurance, solar grid net-metering & ESG tokenization.", EmeraldSuccess),
            Triple("4. Real Estate & Infrastructure", "Fractionalized sovereign deed tokenization & predictive yield stress-tests.", GoldAccent),
            Triple("5. Municipal Smart Cities", "Automated turnstile NFC transit, smart meter utilities & dynamic micro-taxes.", ElectricBlue),
            Triple("6. Aerospace & Space Mining", "Asynchronous interplanetary settlement, lunar basecamp deeds & asteroid claim escrows.", PurpleTech)
        )

        items(industries) { (title, desc, color) ->
            IndustryCard(title = title, description = desc, color = color)
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
                        modifier = Modifier.fillMaxWidth().clickable { showPilotForm = !showPilotForm }.padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Apply for Industry Pilot Program ($0 Integration Cost)", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                            Text("Deploy PFIN in your sector sandbox with dedicated AI engineers.", color = TextMuted, fontSize = 10.sp)
                        }
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text(
                                text = if (showPilotForm) "Hide Form" else "Apply for Pilot",
                                color = EmeraldSuccess,
                                fontWeight = FontWeight.Bold,
                                fontSize = 10.sp,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    AnimatedVisibility(visible = showPilotForm) {
                        IndustryPilotApplicationForm()
                    }
                }
            }
        }
    }
}

@Composable
private fun IndustryCard(title: String, description: String, color: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.35f))
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, color = color, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            Text(description, color = TextWhite, fontSize = 11.sp, lineHeight = 15.sp)
        }
    }
}

@Composable
private fun IndustryPilotApplicationForm() {
    var selectedIndustry by remember { mutableStateOf("Biotech & Healthcare") }
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
                Text("Select Target Sector Pilot", color = TextMuted, fontSize = 11.sp)
                listOf("Biotech & Healthcare", "Renewable Energy Grid", "Aerospace Commerce").forEach { ind ->
                    val sel = selectedIndustry == ind
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = if (sel) EmeraldSuccess.copy(alpha = 0.25f) else Navy800,
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (sel) EmeraldSuccess else Navy700),
                        modifier = Modifier.fillMaxWidth().clickable { selectedIndustry = ind }
                    ) {
                        Text(
                            text = ind,
                            color = if (sel) EmeraldSuccess else TextMuted,
                            fontSize = 10.sp,
                            fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                        )
                    }
                }
                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_pilot_application"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Submit Pilot Application & Reserve Sandbox", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = EmeraldSuccess)
                    Text("Allocating Industry Sandbox Environment...", color = EmeraldSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                    Text("PILOT ADMISSION GRANTED", color = EmeraldSuccess, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("Sector: $selectedIndustry • Pilot Token #PLT-8891 Active", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldSuccess),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Apply for Another Vertical", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// =========================================================================
// REUSABLE PUBLIC PAGE HEADER
// =========================================================================
@Composable
fun PublicPageHeader(
    title: String,
    subtitle: String,
    icon: ImageVector,
    color: Color,
    onBack: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.5f))
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(icon, contentDescription = title, tint = color, modifier = Modifier.size(24.dp))
            }
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(title, color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                    PulsingStatusBadge(pulseColor = color, badgeSize = 5.dp)
                }
                Text(subtitle, color = TextMuted, fontSize = 10.sp, lineHeight = 13.sp)
            }
        }
    }
}
