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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Co2
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.EvStation
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.Grass
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalParking
import androidx.compose.material.icons.filled.MedicalInformation
import androidx.compose.material.icons.filled.MoneyOff
import androidx.compose.material.icons.filled.NaturePeople
import androidx.compose.material.icons.filled.Paid
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Train
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WaterDrop
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
import androidx.compose.ui.text.input.KeyboardType
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

enum class PillarSet1Page(
    val title: String,
    val icon: ImageVector,
    val color: Color,
    val subtitle: String,
    val tag: String
) {
    CLIMATE("7. Climate Intelligence", Icons.Default.Park, EmeraldSuccess, "Predicts climate risk, crop yield, flood losses & green investments", "tab_p1_climate"),
    HEALTHCARE("8. Healthcare Financial", Icons.Default.HealthAndSafety, CrimsonDanger, "Predicts medical expenses, claims, health risks & emergency vaults", "tab_p1_health"),
    SMART_CITY("9. Smart City Network", Icons.Default.Apartment, CyberCyan, "Unified city wallet: Metro, EV charging, utilities & public services", "tab_p1_smartcity"),
    AUTONOMOUS_BIZ("10. Autonomous Business", Icons.Default.Business, GoldAccent, "AI handles payroll, taxes, suppliers, cashflow & profit models", "tab_p1_biz"),
    AI_TREASURY("11. AI Gov Treasury", Icons.Default.AccountBalance, PurpleTech, "Budget planning, sovereign spending, tax fraud & economic development", "tab_p1_treasury")
}

@Composable
fun FuturePillarsSet1Screen(
    viewModel: BankViewModel,
    initialPillar: PillarSet1Page = PillarSet1Page.CLIMATE,
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
            selectedTabIndex = PillarSet1Page.values().indexOf(selectedPillar),
            containerColor = NavyCard,
            contentColor = selectedPillar.color,
            edgePadding = 4.dp,
            indicator = { tabPositions ->
                val idx = PillarSet1Page.values().indexOf(selectedPillar)
                if (idx in tabPositions.indices) {
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[idx]),
                        color = selectedPillar.color,
                        height = 3.dp
                    )
                }
            }
        ) {
            PillarSet1Page.values().forEach { page ->
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
            label = "PillarSet1Transition",
            modifier = Modifier.weight(1f)
        ) { targetPillar ->
            when (targetPillar) {
                PillarSet1Page.CLIMATE -> PillarPageClimate()
                PillarSet1Page.HEALTHCARE -> PillarPageHealthcare()
                PillarSet1Page.SMART_CITY -> PillarPageSmartCity()
                PillarSet1Page.AUTONOMOUS_BIZ -> PillarPageAutonomousBiz()
                PillarSet1Page.AI_TREASURY -> PillarPageAiTreasury()
            }
        }
    }
}

// -------------------------------------------------------------
// PILLAR 7: CLIMATE FINANCIAL INTELLIGENCE & APPLICATION FORM
// -------------------------------------------------------------
@Composable
private fun PillarPageClimate() {
    var greenSwapActive by remember { mutableStateOf(true) }
    var showApplicationForm by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            PillarInfoCard(
                title = "Predictive Climate Risk & Parametric Underwriting",
                subtitle = "Pre-disaster capital allocation: models 30-year sea-level rise, grid heat stress, and agricultural yield loss.",
                icon = Icons.Default.NaturePeople,
                color = EmeraldSuccess
            )
        }

        item {
            PillarMetricsGrid(
                title = "Live Climate Risk Telemetry",
                items = listOf(
                    "Crop Failure Vulnerability Index" to "Low (8.2% drought risk / 10-yr)",
                    "Property 100-Year Flood Risk" to "0.02% (Elevated zone grade A+)",
                    "Parametric Catastrophe Cover" to "$500,000 Zero-Adjuster Instant Payout",
                    "Carbon Offset Token Yield" to "+4.2% APY from Direct Air Capture credits"
                )
            )
        }

        item {
            PillarActionCard(
                title = "Autonomous Green Investment Rebalancing",
                description = "Auto-replaces fossil-fuel exposed debt with ESG Triple-A sovereign green bonds.",
                status = if (greenSwapActive) "100% Green Portfolio Active" else "Standard Allocation",
                actionLabel = "Toggle Green Optimization",
                onClick = { greenSwapActive = !greenSwapActive },
                color = EmeraldSuccess
            )
        }

        // --- Interactive Animated Application Form for Green Bond / Parametric Insurance ---
        item {
            PillarApplicationSection(
                formTitle = "Apply for Sovereign Green Bond & Climate Shield",
                formSubtitle = "Instant AI underwriting for eco-restoration grants, rooftop solar micro-financing, and parametric crop catastrophe protection.",
                color = EmeraldSuccess,
                isOpen = showApplicationForm,
                onToggle = { showApplicationForm = !showApplicationForm }
            ) {
                ClimateBondApplicationForm()
            }
        }
    }
}

@Composable
private fun ClimateBondApplicationForm() {
    var bondAmount by remember { mutableFloatStateOf(25000f) }
    var locationZone by remember { mutableStateOf("Pacific Northwest Agricultural Grid #4") }
    var selectedProject by remember { mutableStateOf("Rooftop Solar & Microgrid Battery") }
    var formState by remember { mutableStateOf("DRAFT") } // DRAFT, SCANNING, APPROVED

    if (formState == "SCANNING") {
        LaunchedEffect(Unit) {
            delay(2200)
            formState = "APPROVED"
        }
    }

    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        when (formState) {
            "DRAFT" -> {
                Text("Requested Green Micro-Capital: $${bondAmount.toInt()}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Slider(
                    value = bondAmount,
                    onValueChange = { bondAmount = it },
                    valueRange = 5000f..100000f,
                    steps = 18,
                    colors = SliderDefaults.colors(thumbColor = EmeraldSuccess, activeTrackColor = EmeraldSuccess)
                )

                Text("Ecological Project Type", color = TextMuted, fontSize = 11.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Solar Microgrid", "Direct Air Capture", "Parametric Crops").forEach { type ->
                        val sel = selectedProject == type
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (sel) EmeraldSuccess.copy(alpha = 0.25f) else Navy800,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (sel) EmeraldSuccess else Navy700),
                            modifier = Modifier.clickable { selectedProject = type }
                        ) {
                            Text(
                                text = type,
                                color = if (sel) EmeraldSuccess else TextMuted,
                                fontSize = 10.sp,
                                fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                OutlinedTextField(
                    value = locationZone,
                    onValueChange = { locationZone = it },
                    label = { Text("Geospatial Climate Mesh Zone", color = TextMuted, fontSize = 10.sp) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = EmeraldSuccess, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_climate_application"),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Submit for Instant Autonomous AI Underwriting", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = EmeraldSuccess)
                    Text("Analyzing Satellite NDVI & 100-Year Climate Telemetry...", color = EmeraldSuccess, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                    Text("GREEN BOND ALLOCATION APPROVED", color = EmeraldSuccess, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("Contract #ESG-77492 Issued • $${bondAmount.toInt()} Escrowed at 1.8% Subsidized Yield", color = TextWhite, fontSize = 10.sp)
                    Surface(color = EmeraldSuccess.copy(alpha = 0.15f), shape = RoundedCornerShape(6.dp)) {
                        Text("Zero-Carbon Verified • Smart Satellite Parametric Shield Active", color = EmeraldSuccess, fontSize = 9.sp, modifier = Modifier.padding(6.dp))
                    }
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = EmeraldSuccess),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Apply for Another Climate Facility", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// PILLAR 8: HEALTHCARE FINANCIAL INTELLIGENCE & APPLICATION FORM
// -------------------------------------------------------------
@Composable
private fun PillarPageHealthcare() {
    var showApplicationForm by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            PillarInfoCard(
                title = "Genomic & Longevity Financial Integration",
                subtitle = "Connects permitted biometric data with lifetime medical expense forecasts and auto-expanding emergency reserves.",
                icon = Icons.Default.MedicalInformation,
                color = CrimsonDanger
            )
        }

        item {
            PillarMetricsGrid(
                title = "Predictive Health Expense Forecast",
                items = listOf(
                    "Projected Lifetime Out-of-Pocket" to "$82,400 (Top 10% Lowest Risk)",
                    "Triple Tax-Advantaged HSA Compound" to "$214,500 projected at Age 65",
                    "Permitted Biometric Discount" to "-24% Insurance Premium Reduction",
                    "Automated Hospital Claim Settlement" to "100% Zero-Friction Direct Pay"
                )
            )
        }

        item {
            PillarMetricsGrid(
                title = "Autonomous Critical Illness Buffer",
                items = listOf(
                    "Dedicated Emergency Health Vault" to "$45,000 Locked in Sovereign Yield",
                    "Auto-Escrow Medical Lane" to "Instant Pre-Authorization at 12,000 Global Clinics",
                    "Rare Disease Experimental Trial Shield" to "$2,000,000 Global Coverage"
                )
            )
        }

        item {
            PillarApplicationSection(
                formTitle = "Enroll in Genomic Longevity HSA & Escrow Vault",
                formSubtitle = "Connect encrypted biometric health tokens to unlock 0% medical expense loans and automatic emergency clinic authorizations.",
                color = CrimsonDanger,
                isOpen = showApplicationForm,
                onToggle = { showApplicationForm = !showApplicationForm }
            ) {
                HealthcareEnrollmentForm()
            }
        }
    }
}

@Composable
private fun HealthcareEnrollmentForm() {
    var coverageTarget by remember { mutableFloatStateOf(50000f) }
    var bloodPressureTier by remember { mutableStateOf("Optimal (<120/80)") }
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
                Text("Requested Emergency Medical Escrow: $${coverageTarget.toInt()}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Slider(
                    value = coverageTarget,
                    onValueChange = { coverageTarget = it },
                    valueRange = 10000f..250000f,
                    steps = 23,
                    colors = SliderDefaults.colors(thumbColor = CrimsonDanger, activeTrackColor = CrimsonDanger)
                )

                Text("Biometric Verification Consent", color = TextMuted, fontSize = 11.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Optimal (<120/80)", "Standard", "Continuous CGM").forEach { tier ->
                        val sel = bloodPressureTier == tier
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (sel) CrimsonDanger.copy(alpha = 0.25f) else Navy800,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (sel) CrimsonDanger else Navy700),
                            modifier = Modifier.clickable { bloodPressureTier = tier }
                        ) {
                            Text(
                                text = tier,
                                color = if (sel) CrimsonDanger else TextMuted,
                                fontSize = 10.sp,
                                fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_healthcare_application"),
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonDanger),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Verify Zero-Knowledge Health Signature & Activate Vault", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = CrimsonDanger)
                    Text("Verifying Encrypted Health Data & Actuarial Longevity Curve...", color = CrimsonDanger, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                    Text("LONGEVITY EMERGENCY VAULT ACTIVE", color = CrimsonDanger, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("$${coverageTarget.toInt()} Escrowed • Zero-Deductible Clinic Pre-Auth Enabled", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CrimsonDanger),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Manage Escrow Allocation", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// PILLAR 9: SMART CITY FINANCIAL NETWORK & APPLICATION FORM
// -------------------------------------------------------------
@Composable
private fun PillarPageSmartCity() {
    var showApplicationForm by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            PillarInfoCard(
                title = "One Unified Sovereign City Wallet",
                subtitle = "Autonomous micro-transactions power municipal transport, EV induction charging, grid water & civic amenities.",
                icon = Icons.Default.Apartment,
                color = CyberCyan
            )
        }

        item {
            PillarMetricsGrid(
                title = "Connected Municipal Utilities & Transit",
                items = listOf(
                    "Autonomous Metro Tap & Go" to "0ms NFC / Biometric Turnstile Clearance",
                    "Dynamic EV Induction Charging" to "Auto-paid per kilowatt ($0.08/kWh off-peak)",
                    "Smart Grid Water & Solar" to "Net-metering revenue credited daily ($14.20/mo)",
                    "Automated Dynamic Parking" to "Camera LPR auto-billed with 20% municipal rebate"
                )
            )
        }

        item {
            PillarMetricsGrid(
                title = "Civic Tax & Public Service Integration",
                items = listOf(
                    "Municipal Property Tax" to "Micro-accrued daily ($4.10/day) — Zero lump sum shock",
                    "Public Library & Recreation Passes" to "All-inclusive digital resident pass",
                    "City Carbon Credit Dividend" to "+$38.50 / mo clean commute incentive"
                )
            )
        }

        item {
            PillarApplicationSection(
                formTitle = "Apply for Smart City Resident Pass & Auto-EV Induction",
                formSubtitle = "Link vehicle LPR, biometric turnstile credentials, and dynamic municipal water/electricity net metering.",
                color = CyberCyan,
                isOpen = showApplicationForm,
                onToggle = { showApplicationForm = !showApplicationForm }
            ) {
                SmartCityPassApplicationForm()
            }
        }
    }
}

@Composable
private fun SmartCityPassApplicationForm() {
    var municipalCity by remember { mutableStateOf("Neo-Tokyo Metro Zone 7") }
    var evVehicleVin by remember { mutableStateOf("VIN-8842-CYBER-EV") }
    var formState by remember { mutableStateOf("DRAFT") }

    if (formState == "SCANNING") {
        LaunchedEffect(Unit) {
            delay(1800)
            formState = "APPROVED"
        }
    }

    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        when (formState) {
            "DRAFT" -> {
                OutlinedTextField(
                    value = municipalCity,
                    onValueChange = { municipalCity = it },
                    label = { Text("Smart City Jurisdiction", color = TextMuted, fontSize = 10.sp) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = evVehicleVin,
                    onValueChange = { evVehicleVin = it },
                    label = { Text("EV Vehicle Plate / Autonomous ID", color = TextMuted, fontSize = 10.sp) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth()
                )
                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_smartcity_application"),
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Provision Smart City Universal NFC & Induction Pass", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = CyberCyan)
                    Text("Registering Municipal Zero-Knowledge Turnstile Credential...", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                    Text("SMART CITY DIGITAL PASS ISSUED", color = CyberCyan, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("Linked to $municipalCity • Automated EV Charging & Metro Pass Active", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Add Another City Pass", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// PILLAR 10: AUTONOMOUS BUSINESS BANKING & APPLICATION FORM
// -------------------------------------------------------------
@Composable
private fun PillarPageAutonomousBiz() {
    var showApplicationForm by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            PillarInfoCard(
                title = "Enterprise Autonomous Financial CFO",
                subtitle = "Self-driving B2B engine: auto-reconciles payroll, tax withholdings, supply chain factoring, and profit forecasts.",
                icon = Icons.Default.Business,
                color = GoldAccent
            )
        }

        item {
            PillarMetricsGrid(
                title = "Autonomous Corporate Operations",
                items = listOf(
                    "Real-Time Smart Payroll" to "Streamed continuous wage payments to 48 staff",
                    "Algorithmic Tax Reserve Shield" to "Exact 21% corporate tax auto-escrowed per invoice",
                    "Just-In-Time Supplier Factoring" to "Early payment discounts captured (+2.8% margin)",
                    "365-Day Predictive Profit Engine" to "Forecast: $1.42M Net (+18.4% YoY)"
                )
            )
        }

        item {
            PillarMetricsGrid(
                title = "Dynamic Risk & Liquidity Management",
                items = listOf(
                    "Working Capital Revolver" to "$500,000 dynamic credit line (Prime - 1.2%)",
                    "Accounts Receivable Recovery Bot" to "Average collection cycle reduced to 4.2 days",
                    "Foreign Exchange Currency Hedging" to "Automated EUR/USD options hedge against FX swings"
                )
            )
        }

        item {
            PillarApplicationSection(
                formTitle = "Onboard Corporate Business Entity to Autonomous CFO",
                formSubtitle = "Enable per-second continuous payroll streaming, instant automated tax escrows, and algorithmic supply chain factoring.",
                color = GoldAccent,
                isOpen = showApplicationForm,
                onToggle = { showApplicationForm = !showApplicationForm }
            ) {
                AutonomousBizOnboardingForm()
            }
        }
    }
}

@Composable
private fun AutonomousBizOnboardingForm() {
    var companyName by remember { mutableStateOf("HyperScale Quantum AI Corp") }
    var employeeCount by remember { mutableFloatStateOf(48f) }
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
                    value = companyName,
                    onValueChange = { companyName = it },
                    label = { Text("Corporate Entity Name & EIN", color = TextMuted, fontSize = 10.sp) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = GoldAccent, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Number of Employees / Contractors: ${employeeCount.toInt()}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Slider(
                    value = employeeCount,
                    onValueChange = { employeeCount = it },
                    valueRange = 1f..500f,
                    steps = 49,
                    colors = SliderDefaults.colors(thumbColor = GoldAccent, activeTrackColor = GoldAccent)
                )
                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_biz_application"),
                    colors = ButtonDefaults.buttonColors(containerColor = GoldAccent),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Activate Autonomous Enterprise CFO Engine", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = GoldAccent)
                    Text("Synthesizing Continuous Payroll & Tax Escrow Smart Contracts...", color = GoldAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                    Text("AUTONOMOUS B2B CFO ONLINE", color = GoldAccent, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("$companyName • Real-Time Payroll & Tax Automation Configured", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = GoldAccent),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Edit Corporate Parameters", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// PILLAR 11: AI GOVERNMENT TREASURY & APPLICATION FORM
// -------------------------------------------------------------
@Composable
private fun PillarPageAiTreasury() {
    var showApplicationForm by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            PillarInfoCard(
                title = "Sovereign Treasury & Public Wealth Optimization",
                subtitle = "AI public finance architecture: real-time national balance sheets, automated welfare distribution, and tax evasion shields.",
                icon = Icons.Default.AccountBalance,
                color = PurpleTech
            )
        }

        item {
            PillarMetricsGrid(
                title = "National Treasury Operations",
                items = listOf(
                    "Sovereign Budget Optimization" to "Real-time AI reallocation to high-multiplier infrastructure",
                    "Programmable Welfare Delivery" to "Zero leakage direct citizen transfer ($0 administrative overhead)",
                    "Deep-Learning Tax Evasion Scanner" to "99.94% offshore loophole & shell entity detection",
                    "National Development 2050 Index" to "Target GDP growth +4.8% via AI green energy grid"
                )
            )
        }

        item {
            PillarMetricsGrid(
                title = "Central Bank Digital Currency (CBDC) Stability",
                items = listOf(
                    "Algorithmic Velocity of Money Control" to "Stabilized inflation target (1.98% annualized)",
                    "Cross-Border Sovereign Liquidity Hub" to "T+0 instant settlement with 42 central banks",
                    "Public Infrastructure Bond Auctions" to "Micro-fractionalized directly to retail citizens"
                )
            )
        }

        item {
            PillarApplicationSection(
                formTitle = "Apply for Citizen Sovereign Welfare & Micro-Bond Allocation",
                formSubtitle = "Direct zero-leakage government stimulus, sovereign infrastructure yield bonds, and national universal basic income stipend.",
                color = PurpleTech,
                isOpen = showApplicationForm,
                onToggle = { showApplicationForm = !showApplicationForm }
            ) {
                CitizenWelfareApplicationForm()
            }
        }
    }
}

@Composable
private fun CitizenWelfareApplicationForm() {
    var citizenId by remember { mutableStateOf("SOV-ID-2050-US-9918") }
    var stipendType by remember { mutableStateOf("Infrastructure Yield Micro-Bond") }
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
                OutlinedTextField(
                    value = citizenId,
                    onValueChange = { citizenId = it },
                    label = { Text("Sovereign World Citizen ID", color = TextMuted, fontSize = 10.sp) },
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurpleTech, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    modifier = Modifier.fillMaxWidth()
                )
                Text("Select Program", color = TextMuted, fontSize = 11.sp)
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    listOf("Infrastructure Bond", "Direct UBI Stipend", "Green Dividend").forEach { type ->
                        val sel = stipendType == type
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (sel) PurpleTech.copy(alpha = 0.25f) else Navy800,
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (sel) PurpleTech else Navy700),
                            modifier = Modifier.clickable { stipendType = type }
                        ) {
                            Text(
                                text = type,
                                color = if (sel) PurpleTech else TextMuted,
                                fontSize = 10.sp,
                                fontWeight = if (sel) FontWeight.Bold else FontWeight.Normal,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                            )
                        }
                    }
                }
                Button(
                    onClick = { formState = "SCANNING" },
                    modifier = Modifier.fillMaxWidth().testTag("submit_treasury_application"),
                    colors = ButtonDefaults.buttonColors(containerColor = PurpleTech),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Verify Zero-Knowledge Citizen Eligibility", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
            "SCANNING" -> {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = PurpleTech)
                    Text("Verifying National Treasury Blockchain & ZK Identity...", color = PurpleTech, fontSize = 11.sp, fontWeight = FontWeight.Bold)
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
                    Text("SOVEREIGN PROGRAM ALLOCATION GRANTED", color = PurpleTech, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                    Text("$stipendType Activated • $500/mo Credited to Sovereign Wallet", color = TextWhite, fontSize = 10.sp)
                    OutlinedButton(
                        onClick = { formState = "DRAFT" },
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = PurpleTech),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Submit Another Treasury Request", fontSize = 10.sp)
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
private fun PillarApplicationSection(
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
private fun PillarInfoCard(title: String, subtitle: String, icon: ImageVector, color: Color) {
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
private fun PillarMetricsGrid(title: String, items: List<Pair<String, String>>) {
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
private fun PillarActionCard(
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
