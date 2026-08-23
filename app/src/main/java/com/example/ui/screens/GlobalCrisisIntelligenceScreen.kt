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
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Coronavirus
import androidx.compose.material.icons.filled.CrisisAlert
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Handshake
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.Landslide
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.MilitaryTech
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.ReportProblem
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDamage
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
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

enum class CrisisType(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val description: String
) {
    EARTHQUAKE("Earthquake (7.8 Mw)", Icons.Default.Landslide, AmberOrange, "High-intensity seismic event triggering infrastructure loss"),
    FLOOD("Catastrophic Flood", Icons.Default.WaterDamage, CyberCyan, "Coastal storm surge & river overflow in metro zones"),
    PANDEMIC("Global Pandemic", Icons.Default.Coronavirus, CrimsonDanger, "Biological contagion triggering lockdown & healthcare strain"),
    WAR("Geopolitical Conflict", Icons.Default.MilitaryTech, GoldAccent, "Cross-border airspace disruption & sanctions enforcement"),
    ECONOMIC_CRASH("Economic Flash Crash", Icons.Default.TrendingDown, PurpleTech, "Sovereign liquidity freeze & banking run circuit breaker")
}

enum class CrisisIntelligencePage(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val tag: String
) {
    FREEZE_FRAUD("Freezes Fraud", Icons.Default.Lock, CrimsonDanger, "tab_gci_freeze_fraud"),
    EMERGENCY_PAYMENTS("Emergency Payments", Icons.Default.FlashOn, GoldAccent, "tab_gci_emergency_payments"),
    RELIEF_MONEY("Sends Relief Money", Icons.Default.VolunteerActivism, EmeraldSuccess, "tab_gci_relief_money"),
    PREDICT_USERS("Predicts Affected Users", Icons.Default.Psychology, CyberCyan, "tab_gci_predict_users"),
    CONNECT_GOVERNMENTS("Connects Governments", Icons.Default.Public, ElectricBlue, "tab_gci_connect_gov"),
    PROTECT_BUSINESSES("Protects Businesses", Icons.Default.Business, PurpleTech, "tab_gci_protect_biz")
}

@Composable
fun GlobalCrisisIntelligenceScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    var selectedCrisis by remember { mutableStateOf(CrisisType.EARTHQUAKE) }
    var selectedPage by remember { mutableStateOf(CrisisIntelligencePage.FREEZE_FRAUD) }

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
            border = androidx.compose.foundation.BorderStroke(1.dp, CrimsonDanger.copy(alpha = 0.6f))
        ) {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(CrimsonDanger.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CrisisAlert,
                            contentDescription = "Global Crisis Intelligence",
                            tint = CrimsonDanger,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            Text(
                                text = "GLOBAL CRISIS INTELLIGENCE",
                                color = TextWhite,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp
                            )
                            PulsingStatusBadge(pulseColor = CrimsonDanger, badgeSize = 6.dp)
                        }
                        Text(
                            text = "Autonomous rapid defense during Earthquakes, Floods, Pandemics, Wars & Crashes.",
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }
                }

                // Crisis Simulator Pills
                Text("ACTIVE CRISIS TELEMETRY & SIMULATION", color = TextMuted, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(CrisisType.values()) { crisis ->
                        val isSelected = selectedCrisis == crisis
                        Surface(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .clickable { selectedCrisis = crisis }
                                .border(
                                    1.dp,
                                    if (isSelected) crisis.color else Navy700,
                                    RoundedCornerShape(8.dp)
                                ),
                            color = if (isSelected) crisis.color.copy(alpha = 0.2f) else Navy900
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                Icon(crisis.icon, contentDescription = null, tint = if (isSelected) crisis.color else TextMuted, modifier = Modifier.size(13.dp))
                                Text(
                                    text = crisis.title,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) crisis.color else TextMuted
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- 6 Separate Pages Scrollable Tab Navigation ---
        ScrollableTabRow(
            selectedTabIndex = CrisisIntelligencePage.values().indexOf(selectedPage),
            containerColor = NavyCard,
            contentColor = CrimsonDanger,
            edgePadding = 4.dp,
            indicator = { tabPositions ->
                val idx = CrisisIntelligencePage.values().indexOf(selectedPage)
                if (idx in tabPositions.indices) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[idx]),
                        color = selectedPage.color,
                        height = 3.dp
                    )
                }
            }
        ) {
            CrisisIntelligencePage.values().forEach { page ->
                val isSelected = selectedPage == page
                Tab(
                    selected = isSelected,
                    onClick = { selectedPage = page },
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
            targetState = selectedPage,
            transitionSpec = {
                (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width } + fadeOut()
                )
            },
            label = "CrisisPageTransition",
            modifier = Modifier.weight(1f)
        ) { targetPage ->
            when (targetPage) {
                CrisisIntelligencePage.FREEZE_FRAUD -> PageGciFreezeFraud(selectedCrisis)
                CrisisIntelligencePage.EMERGENCY_PAYMENTS -> PageGciEmergencyPayments(selectedCrisis)
                CrisisIntelligencePage.RELIEF_MONEY -> PageGciReliefMoney(selectedCrisis)
                CrisisIntelligencePage.PREDICT_USERS -> PageGciPredictUsers(selectedCrisis)
                CrisisIntelligencePage.CONNECT_GOVERNMENTS -> PageGciConnectGov(selectedCrisis)
                CrisisIntelligencePage.PROTECT_BUSINESSES -> PageGciProtectBiz(selectedCrisis)
            }
        }
    }
}

// -------------------------------------------------------------
// PAGE 1: FREEZES FRAUD
// -------------------------------------------------------------
@Composable
private fun PageGciFreezeFraud(crisis: CrisisType) {
    var lockdownActive by remember { mutableStateOf(true) }
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            CrisisSectionHeader(
                title = "Autonomous Disaster Fraud Freeze Engine",
                subtitle = "Instantly intercepts SIM swaps, impersonation scams, extortion bots, and looting exploits during ${crisis.title}.",
                icon = Icons.Default.Lock,
                color = CrimsonDanger
            )
        }
        item {
            CrisisMetricCard(
                title = "Geo-Perimeter Fraud Lockdown",
                badge = if (lockdownActive) "Shield Armed" else "Shield Paused",
                badgeColor = if (lockdownActive) EmeraldSuccess else AmberOrange,
                items = listOf(
                    "Impact Zone Intercept Rate" to "99.98% Malicious Exploits Blocked",
                    "Simulated Attack Vectors" to "Phishing SMS, Impersonation Relief Portals, ATM Jacking",
                    "Autonomous Action" to "All high-risk cross-border wire transfers placed in 24h biometric hold",
                    "Affected Accounts Protected" to "14,820 Sovereign Accounts Shielded"
                )
            )
        }
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("CRISIS ATTACK DEFENSE PROTOCOLS", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    DefenseRow("Biometric Sovereign Step-Up", "Passkey / FaceID required for transfers >$250", EmeraldSuccess)
                    DefenseRow("SIM-Swap Immunity Lock", "Carrier port requests frozen during disaster window", CyberCyan)
                    DefenseRow("Fake Charity Blacklist", "1,290 rogue crypto/fiat donation addresses blocked", CrimsonDanger)
                }
            }
        }
    }
}

// -------------------------------------------------------------
// PAGE 2: PRIORITIZES EMERGENCY PAYMENTS
// -------------------------------------------------------------
@Composable
private fun PageGciEmergencyPayments(crisis: CrisisType) {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            CrisisSectionHeader(
                title = "Life-Critical Emergency Payment Fast-Lane",
                subtitle = "Bypasses standard settlement delays to grant instant zero-latency clearance for hospitals, groceries, fuel, and shelter.",
                icon = Icons.Default.FlashOn,
                color = GoldAccent
            )
        }
        item {
            CrisisMetricCard(
                title = "Life-Critical Priority Routing Status",
                badge = "0.00ms Settlement Latency",
                badgeColor = EmeraldSuccess,
                items = listOf(
                    "Medical & Hospital Invoices" to "Instant Auto-Approval (Zero Cap)",
                    "Evacuation Fuel & Flights" to "Priority Channel Active",
                    "Overdraft & Penalty Fee Shield" to "100% Fee Immunity Imposed",
                    "Offline Mesh Transaction Sync" to "Active via Bluetooth / Starlink Relay"
                )
            )
        }
        item {
            CrisisMetricCard(
                title = "Critical Merchant Category Codes (MCC)",
                badge = "Elevated Priority 1",
                badgeColor = CyberCyan,
                items = listOf(
                    "MCC 8062 (Hospitals & Emergency Care)" to "Fast-Lane Enabled • 0% Merchant Surcharge",
                    "MCC 5411 (Grocery Stores & Water)" to "Instant Ledger Clearance",
                    "MCC 5541 (Service Stations / Gas)" to "Rationed Zero-Friction Tap to Pay"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 3: SENDS RELIEF MONEY
// -------------------------------------------------------------
@Composable
private fun PageGciReliefMoney(crisis: CrisisType) {
    var airdropTriggered by remember { mutableStateOf(false) }
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            CrisisSectionHeader(
                title = "Automated Parametric Relief Disbursal",
                subtitle = "Triggers automatic disaster cash grants directly into victims' digital wallets upon verified satellite event threshold.",
                icon = Icons.Default.VolunteerActivism,
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
                            Text("Parametric Disaster Grant Pool", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("Instant $1,500 Direct Emergency Airdrop / Citizen", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        }
                        Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                            Text(if (airdropTriggered) "Disbursed to 42,000 Users" else "Ready to Disburse", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp))
                        }
                    }

                    LinearProgressIndicator(
                        progress = { if (airdropTriggered) 1.0f else 0.45f },
                        modifier = Modifier.fillMaxWidth().height(6.dp).clip(CircleShape),
                        color = EmeraldSuccess,
                        trackColor = Navy700
                    )

                    Button(
                        onClick = { airdropTriggered = true },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Icon(Icons.Default.VolunteerActivism, contentDescription = null, tint = Navy900, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (airdropTriggered) "Relief Grants Disbursed Successfully" else "Trigger Automated Parametric Airdrop", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }
        item {
            CrisisMetricCard(
                title = "Global Humanitarian Aid Partners",
                badge = "Direct API Integration",
                badgeColor = CyberCyan,
                items = listOf(
                    "Red Cross / Red Crescent" to "Coordinated Emergency Payout Gateway",
                    "UN World Food Programme" to "Direct Digital Voucher Exchange",
                    "FEMA / Sovereign Emergency Pool" to "Automated Tax-Free Wire Clearance"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 4: PREDICTS AFFECTED USERS
// -------------------------------------------------------------
@Composable
private fun PageGciPredictUsers(crisis: CrisisType) {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            CrisisSectionHeader(
                title = "Satellite AI Geospatial Risk Prediction",
                subtitle = "Models seismic fault lines, flood plain levels, contagion vectors, and war zones to pinpoint affected users ahead of time.",
                icon = Icons.Default.Psychology,
                color = CyberCyan
            )
        }
        item {
            CrisisMetricCard(
                title = "Geospatial Impact Radius Telemetry",
                badge = "Confidence: 99.4%",
                badgeColor = EmeraldSuccess,
                items = listOf(
                    "Identified High-Risk Zone" to "Metro Epicenter & 50km Peripheral Radius",
                    "Predicted Impacted Citizens" to "142,800 Registered Users",
                    "Proactive Alert Delivered" to "Push notification, SMS & Mesh beacon broadcasted",
                    "Autonomous Pre-Funding" to "$250 Pre-Emergency Liquidity Added to Wallets"
                )
            )
        }
        item {
            CrisisMetricCard(
                title = "Multi-Source Sensor Feed Integration",
                badge = "Real-Time Telemetry",
                badgeColor = GoldAccent,
                items = listOf(
                    "USGS Seismic Sensors" to "Real-time P-wave & S-wave accelerometer feeds",
                    "Sentinel-2 SAR Satellite Imagery" to "Sub-meter flood water mapping",
                    "Cell Tower & GPS Density Map" to "Evacuation bottlenecks predicted 3 hours in advance"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 5: CONNECTS GOVERNMENTS
// -------------------------------------------------------------
@Composable
private fun PageGciConnectGov(crisis: CrisisType) {
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            CrisisSectionHeader(
                title = "Sovereign Government & Emergency Agency Bridge",
                subtitle = "Interlinks central banks, municipal first responders, and sovereign agencies for coordinated disaster relief.",
                icon = Icons.Default.Public,
                color = ElectricBlue
            )
        }
        item {
            CrisisMetricCard(
                title = "Inter-Agency Disaster Gateway (IADG)",
                badge = "Zero-Trust Encrypted",
                badgeColor = EmeraldSuccess,
                items = listOf(
                    "Central Bank Emergency Line" to "Unlimited Repo & Liquidity Window Opened",
                    "Ministry of Interior / Civil Defense" to "Live Citizen Shelter & Evacuation Registry",
                    "Customs & Airspace Authority" to "Emergency Medical Freight Fast-Tracked"
                )
            )
        }
        item {
            CrisisMetricCard(
                title = "Sovereign Compliance & Tax Waivers",
                badge = "Executive Order Enforced",
                badgeColor = PurpleTech,
                items = listOf(
                    "Disaster Tax Relief" to "Automatic 90-Day Extension on All Filings",
                    "Import Duty Suspension" to "0% Tariff on Rescue Equipment & Medicine",
                    "Cross-Border Sovereign Swap Lines" to "Instant FX Liquidity Guaranteed"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// PAGE 6: PROTECTS BUSINESSES
// -------------------------------------------------------------
@Composable
private fun PageGciProtectBiz(crisis: CrisisType) {
    var payrollShieldActive by remember { mutableStateOf(true) }
    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            CrisisSectionHeader(
                title = "Autonomous SMB & Enterprise Continuity Shield",
                subtitle = "Guarantees payroll continuity, supply chain re-routing, business interruption claim fast-tracks, and emergency credit.",
                icon = Icons.Default.Business,
                color = PurpleTech
            )
        }
        item {
            CrisisMetricCard(
                title = "Emergency Payroll Continuity Shield",
                badge = if (payrollShieldActive) "100% Payroll Guaranteed" else "Paused",
                badgeColor = EmeraldSuccess,
                items = listOf(
                    "Protected Small Businesses" to "3,840 Local Employers",
                    "Covered Employees" to "48,200 Salaried Workers Guaranteed Pay",
                    "0% Emergency Business Loans" to "Pre-approved up to $100,000 per Merchant",
                    "Insurance Claim Settlement" to "Auto-approved via satellite property verification in <10 mins"
                )
            )
        }
        item {
            CrisisMetricCard(
                title = "Supply Chain Autonomous Re-Routing",
                badge = "Active Dynamic Reroute",
                badgeColor = CyberCyan,
                items = listOf(
                    "Disrupted Freight Corridors" to "3 Ports & 2 Rail Corridors Flagged Closed",
                    "Alternative Sourcing Activated" to "14 Pre-vetted Secondary Suppliers Dispatched",
                    "Working Capital Buffer" to "Instant $250,000 Invoice Factoring Line"
                )
            )
        }
    }
}

// -------------------------------------------------------------
// REUSABLE HELPER COMPONENTS
// -------------------------------------------------------------
@Composable
private fun CrisisSectionHeader(title: String, subtitle: String, icon: ImageVector, color: Color) {
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
private fun CrisisMetricCard(
    title: String,
    badge: String,
    badgeColor: Color,
    items: List<Pair<String, String>>
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
                Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Surface(color = badgeColor.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                    Text(badge, color = badgeColor, fontWeight = FontWeight.Bold, fontSize = 9.sp, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                }
            }

            Spacer(modifier = Modifier.height(2.dp))

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
private fun DefenseRow(title: String, detail: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(NavyCard)
            .padding(horizontal = 10.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 11.sp)
            Text(detail, color = TextMuted, fontSize = 9.sp)
        }
        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
    }
}
