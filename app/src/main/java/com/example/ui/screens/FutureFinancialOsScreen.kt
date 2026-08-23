package com.example.ui.screens

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Healing
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Rule
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
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
import com.example.ui.theme.PurpleTech
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BankViewModel

data class FuturePillarItem(
    val id: Int,
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val tag: String,
    val onClick: () -> Unit
)

@Composable
fun FutureFinancialOsScreen(
    viewModel: BankViewModel,
    onNavigateAdvisor: () -> Unit,
    onNavigateGlobalEconomy: () -> Unit,
    onNavigateWorldIdentity: () -> Unit,
    onNavigateLifePlanner: () -> Unit,
    onNavigateWealthEngine: () -> Unit,
    onNavigateCrisisIntelligence: () -> Unit,
    onNavigatePillarsPart1: (PillarSet1Page) -> Unit,
    onNavigatePillarsPart2: (PillarSet2Page) -> Unit,
    onNavigatePillarsPart3: (PillarSet3Page) -> Unit,
    modifier: Modifier = Modifier
) {
    val pillars = listOf(
        FuturePillarItem(1, "AGI Financial Advisor", "Lifelong autonomous partner learning habits over decades with predictive wealth modeling.", Icons.Default.AutoAwesome, GoldAccent, "os_pillar_1", onNavigateAdvisor),
        FuturePillarItem(2, "Global Economic Prediction Engine", "Macro forecasting: inflation, wars, oil, food, central bank rates & recession warnings.", Icons.Default.Public, CyberCyan, "os_pillar_2", onNavigateGlobalEconomy),
        FuturePillarItem(3, "World Digital Identity", "One sovereign identity linking banking, education, employment, tax, healthcare & assets.", Icons.Default.Fingerprint, ElectricBlue, "os_pillar_3", onNavigateWorldIdentity),
        FuturePillarItem(4, "AI Personal Life Planner", "360° life manager: Career roadmap, education ROI, healthcare, marriage, kids, FIRE & estate.", Icons.Default.Psychology, PurpleTech, "os_pillar_4", onNavigateLifePlanner),
        FuturePillarItem(5, "Universal Wealth Optimization Engine", "Autonomous autopilot finds better investments, loans, insurance, bills, savings, taxes & cashback.", Icons.Default.TrendingUp, EmeraldSuccess, "os_pillar_5", onNavigateWealthEngine),
        FuturePillarItem(6, "Global Crisis Intelligence", "Disaster AI: Freezes fraud, prioritizes payments, airdrops relief, predicts impact & shields SMBs.", Icons.Default.CrisisAlert, CrimsonDanger, "os_pillar_6", onNavigateCrisisIntelligence),
        FuturePillarItem(7, "Climate Financial Intelligence", "Predicts climate risk, crop failures, flood loss, ESG yields & green bond rebalancing.", Icons.Default.Park, EmeraldSuccess, "os_pillar_7") { onNavigatePillarsPart1(PillarSet1Page.CLIMATE) },
        FuturePillarItem(8, "Healthcare Financial Intelligence", "Genomic/biometric forecasting: medical expense modeling, insurance claims & emergency health escrow.", Icons.Default.HealthAndSafety, CrimsonDanger, "os_pillar_8") { onNavigatePillarsPart1(PillarSet1Page.HEALTHCARE) },
        FuturePillarItem(9, "Smart City Financial Network", "Unified city wallet: Autonomous metro turnstiles, EV charging, smart water & civic taxes.", Icons.Default.Apartment, CyberCyan, "os_pillar_9") { onNavigatePillarsPart1(PillarSet1Page.SMART_CITY) },
        FuturePillarItem(10, "Autonomous Business Banking", "Self-driving enterprise CFO: Streamed continuous payroll, automated tax escrows & profit AI.", Icons.Default.Business, GoldAccent, "os_pillar_10") { onNavigatePillarsPart1(PillarSet1Page.AUTONOMOUS_BIZ) },
        FuturePillarItem(11, "AI Government Treasury", "Sovereign treasury: Public spending optimization, direct welfare delivery & tax evasion shields.", Icons.Default.AccountBalance, PurpleTech, "os_pillar_11") { onNavigatePillarsPart1(PillarSet1Page.AI_TREASURY) },
        FuturePillarItem(12, "AI Startup Incubator", "Founder accelerator: TAM forecasting, non-dilutive grant matchmaking, VC sentiment & valuation models.", Icons.Default.RocketLaunch, GoldAccent, "os_pillar_12") { onNavigatePillarsPart2(PillarSet2Page.INCUBATOR) },
        FuturePillarItem(13, "Digital Twin Economy", "100k Monte Carlo parallel universe simulations before real money is committed to investments or debt.", Icons.Default.Timeline, CyberCyan, "os_pillar_13") { onNavigatePillarsPart2(PillarSet2Page.DIGITAL_TWIN) },
        FuturePillarItem(14, "Quantum Banking", "Post-quantum cryptography (Kyber/Dilithium), quantum key distribution & Shor algorithm defense.", Icons.Default.Memory, ElectricBlue, "os_pillar_14") { onNavigatePillarsPart2(PillarSet2Page.QUANTUM) },
        FuturePillarItem(15, "AI Financial Doctor", "Clinical diagnosis of spending addictions, debt stress, dopamine loops & prescriptive recovery regimens.", Icons.Default.Healing, CrimsonDanger, "os_pillar_15") { onNavigatePillarsPart2(PillarSet2Page.FINANCIAL_DOCTOR) },
        FuturePillarItem(16, "Universal Financial Reputation Score", "Holistic 8-dimensional rating: saving discipline, debt reliability, cybersecurity, ESG & tax integrity.", Icons.Default.Star, EmeraldSuccess, "os_pillar_16") { onNavigatePillarsPart2(PillarSet2Page.REPUTATION_SCORE) },
        FuturePillarItem(17, "AI Marketplace", "Decentralized fintech app store: Trading bots, expat tax plugins, yield harvesters & AI algorithms.", Icons.Default.Storefront, GoldAccent, "os_pillar_17") { onNavigatePillarsPart3(PillarSet3Page.MARKETPLACE) },
        FuturePillarItem(18, "AI Financial Research Center", "Autonomous R&D lab: Publishes live papers on quantum threats, CBDC velocity & zero-knowledge proofs.", Icons.Default.Science, CyberCyan, "os_pillar_18") { onNavigatePillarsPart3(PillarSet3Page.RESEARCH_CENTER) },
        FuturePillarItem(19, "Autonomous Compliance Engine", "Real-time global compliance: Automated AML, KYC, GDPR, RBI, SEC, MiCA & sanctions enforcement.", Icons.Default.Rule, ElectricBlue, "os_pillar_19") { onNavigatePillarsPart3(PillarSet3Page.COMPLIANCE_ENGINE) },
        FuturePillarItem(20, "Planetary Financial Network", "Interplanetary banking: Moon bases, Mars colonies, asteroid mining tokens & off-Earth solar energy currency.", Icons.Default.RocketLaunch, PurpleTech, "os_pillar_20") { onNavigatePillarsPart3(PillarSet3Page.PLANETARY_NETWORK) }
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 10.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // --- Master OS Hero Card ---
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.7f))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
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
                        Icon(
                            imageVector = Icons.Default.RocketLaunch,
                            contentDescription = "Financial OS 2077",
                            tint = GoldAccent,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "FINANCIAL OS (2050–2077)",
                                color = TextWhite,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )
                            PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 6.dp)
                        }
                        Text(
                            text = "The Operating System for the World's Financial Future • 20 Core Pillars",
                            color = GoldAccent,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 10.sp
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Navy900)
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("20 Sovereign AI Engines Online", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text("Zero-Knowledge • Quantum Safe", color = CyberCyan, fontSize = 10.sp)
                }
            }
        }

        // --- 20 Dedicated Pillars Grid List ---
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(pillars) { pillar ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { pillar.onClick() }
                        .testTag(pillar.tag),
                    colors = CardDefaults.cardColors(containerColor = NavyCard),
                    shape = RoundedCornerShape(14.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, pillar.color.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(pillar.color.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(pillar.icon, contentDescription = null, tint = pillar.color, modifier = Modifier.size(22.dp))
                        }

                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                                Surface(
                                    color = pillar.color.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(4.dp)
                                ) {
                                    Text(
                                        text = "#${pillar.id}",
                                        color = pillar.color,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                    )
                                }
                                Text(
                                    text = pillar.title,
                                    color = TextWhite,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = pillar.description,
                                color = TextMuted,
                                fontSize = 10.sp,
                                lineHeight = 13.sp
                            )
                        }

                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Open Pillar",
                            tint = pillar.color,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}
