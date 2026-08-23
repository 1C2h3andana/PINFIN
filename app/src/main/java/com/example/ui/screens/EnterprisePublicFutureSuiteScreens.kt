package com.example.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import com.example.ui.viewmodel.BankViewModel

enum class EnterpriseFuturePageType(val title: String, val icon: ImageVector, val code: String) {
    // Module 22: Business Intelligence
    BUSINESS_HUB("Business 360°", Icons.Default.Business, "BI"),
    BUSINESS_PAYROLL("Payroll Engine", Icons.Default.Payments, "PAYROLL"),
    BUSINESS_INVOICES("Invoices & AP", Icons.Default.Receipt, "INVOICE"),

    // Module 23: Smart City Finance
    SMART_CITY("Smart City Hub", Icons.Default.LocationCity, "CITY"),
    CITY_UTILITIES("EV & Utilities", Icons.Default.ElectricBolt, "EV"),

    // Module 24 & 25: Government & Disaster Management
    GOVERNMENT_PORTAL("Govt Portal", Icons.Default.AccountBalance, "GOV"),
    DISASTER_MANAGEMENT("Crisis Relief", Icons.Default.CrisisAlert, "DISASTER"),

    // Module 26 & 27: Humanitarian & Sustainability
    HUMANITARIAN_NGO("Humanitarian NGO", Icons.Default.VolunteerActivism, "NGO"),
    SUSTAINABILITY_CENTER("ESG Center", Icons.Default.Eco, "ESG"),

    // Module 28 & 29: Research & Marketplace
    RESEARCH_INNOVATION("Research Lab", Icons.Default.Science, "RESEARCH"),
    AI_MARKETPLACE("AI Marketplace", Icons.Default.Storefront, "MARKET"),

    // Module 30 & 32: Analytics & Developer Center
    EXECUTIVE_ANALYTICS("Exec KPIs", Icons.Default.Analytics, "KPI"),
    DEVELOPER_CENTER("Developer APIs", Icons.Default.Code, "DEV"),

    // Module 33 & 38: Future Tech & Space Economy
    FUTURE_TECH_HUB("Future Tech Lab", Icons.Default.Biotech, "TECH"),
    SPACE_LUNAR_ECONOMY("Space & Lunar", Icons.Default.RocketLaunch, "SPACE"),

    // Module 34, 35, 36: Notifications, Settings, Help
    NOTIFICATION_CENTER("Alerts Hub", Icons.Default.Notifications, "ALERTS"),
    SETTINGS_CENTER("Master Settings", Icons.Default.Settings, "SETTINGS"),
    HELP_SUPPORT("24/7 AI Support", Icons.Default.SupportAgent, "HELP")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnterprisePublicFutureSuiteScreen(
    viewModel: BankViewModel,
    initialPage: EnterpriseFuturePageType = EnterpriseFuturePageType.BUSINESS_HUB,
    onNavigateToPage: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedPage by remember { mutableStateOf(initialPage) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Navy900)
    ) {
        // --- Top Header ---
        Card(
            colors = CardDefaults.cardColors(containerColor = Navy800),
            shape = RoundedCornerShape(0.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(Brush.linearGradient(listOf(PurpleTech, ElectricBlue))),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.CorporateFare, contentDescription = null, tint = TextWhite, modifier = Modifier.size(24.dp))
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                "ENTERPRISE • GOVT • FUTURE ECONOMY",
                                color = PurpleTech,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Text(
                                "Planetary Infrastructure Suite",
                                color = TextWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    AssistChip(
                        onClick = {},
                        label = { Text("Grid Status: 100%", fontSize = 11.sp, color = EmeraldSuccess, fontWeight = FontWeight.Bold) },
                        colors = AssistChipDefaults.assistChipColors(containerColor = EmeraldSuccess.copy(alpha = 0.15f)),
                        border = BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.4f))
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // --- Horizontal Selector Bar ---
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(EnterpriseFuturePageType.values()) { page ->
                        val isSelected = selectedPage == page
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedPage = page },
                            label = { Text(page.title, fontSize = 12.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                            leadingIcon = {
                                Icon(
                                    page.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp),
                                    tint = if (isSelected) Navy900 else PurpleTech
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PurpleTech,
                                selectedLabelColor = TextWhite,
                                containerColor = Navy700,
                                labelColor = TextWhite
                            ),
                            border = BorderStroke(1.dp, if (isSelected) PurpleTech else Navy700)
                        )
                    }
                }
            }
        }

        // --- Animated Body Content ---
        AnimatedContent(
            targetState = selectedPage,
            transitionSpec = {
                (slideInHorizontally { width -> width / 3 } + fadeIn()).togetherWith(
                    slideOutHorizontally { width -> -width / 3 } + fadeOut()
                )
            },
            label = "EnterpriseFuturePageTransition",
            modifier = Modifier.fillMaxSize()
        ) { targetPage ->
            when (targetPage) {
                EnterpriseFuturePageType.BUSINESS_HUB -> BusinessHubScreen(viewModel)
                EnterpriseFuturePageType.BUSINESS_PAYROLL -> BusinessPayrollScreen(viewModel)
                EnterpriseFuturePageType.BUSINESS_INVOICES -> BusinessInvoicesScreen(viewModel)
                EnterpriseFuturePageType.SMART_CITY -> SmartCityScreen(viewModel)
                EnterpriseFuturePageType.CITY_UTILITIES -> CityUtilitiesScreen(viewModel)
                EnterpriseFuturePageType.GOVERNMENT_PORTAL -> GovernmentPortalScreen(viewModel)
                EnterpriseFuturePageType.DISASTER_MANAGEMENT -> DisasterManagementScreen(viewModel)
                EnterpriseFuturePageType.HUMANITARIAN_NGO -> HumanitarianNgoScreen(viewModel)
                EnterpriseFuturePageType.SUSTAINABILITY_CENTER -> SustainabilityCenterScreen(viewModel)
                EnterpriseFuturePageType.RESEARCH_INNOVATION -> ResearchInnovationScreen(viewModel)
                EnterpriseFuturePageType.AI_MARKETPLACE -> AiMarketplaceScreen(viewModel)
                EnterpriseFuturePageType.EXECUTIVE_ANALYTICS -> ExecutiveAnalyticsScreen(viewModel)
                EnterpriseFuturePageType.DEVELOPER_CENTER -> DeveloperCenterScreen(viewModel)
                EnterpriseFuturePageType.FUTURE_TECH_HUB -> FutureTechHubScreen(viewModel)
                EnterpriseFuturePageType.SPACE_LUNAR_ECONOMY -> SpaceLunarEconomyScreen(viewModel)
                EnterpriseFuturePageType.NOTIFICATION_CENTER -> NotificationCenterScreen(viewModel)
                EnterpriseFuturePageType.SETTINGS_CENTER -> SettingsCenterScreen(viewModel)
                EnterpriseFuturePageType.HELP_SUPPORT -> HelpSupportScreen(viewModel)
            }
        }
    }
}

// -------------------------------------------------------------------------
// SCREENS IMPLEMENTATIONS
// -------------------------------------------------------------------------
@Composable
fun BusinessHubScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(14.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Enterprise Commercial Treasury & Cash Flow", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Operating Cash Runway", "18.4 Months", EmeraldSuccess)
                    DetailItemRow("Accounts Receivable (AR)", "$420,000.00 Outstanding", CyberCyan)
                    DetailItemRow("Accounts Payable (AP)", "$118,000.00 Due 30d", GoldAccent)
                }
            }
        }
    }
}

@Composable
fun BusinessPayrollScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Automated Multi-Jurisdiction Payroll", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Next Payroll Batch", "Aug 31, 2026 ($98,500.00)", TextWhite)
                    DetailItemRow("Employees Across 8 Countries", "42 Active Staff", CyberCyan)
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = { viewModel.showMessage("✓ Next payroll run locked and pre-funded with automated tax withholdings.") },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess, contentColor = Navy900),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(44.dp)
                    ) {
                        Text("Authorize Batch Payroll", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun BusinessInvoicesScreen(viewModel: BankViewModel) {
    val invoices = listOf(
        Triple("INV-2026-081", "Global Cloud Provider LLC", "$12,450.00 • DUE AUG 28"),
        Triple("INV-2026-080", "Silicon Valley Law Group", "$6,800.00 • PAID"),
        Triple("INV-2026-079", "AI Compute Datacenter Cluster", "$34,200.00 • PAID")
    )
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Invoices & Supplier Payables", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp) }
        items(invoices) { (num, vendor, status) ->
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column {
                        Text(num, color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(vendor, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Text(status, color = if (status.contains("PAID")) EmeraldSuccess else AmberOrange, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable fun SmartCityScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Municipal Smart City Services", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Autonomous EV Toll Pass", "Active • Zero Congestion", EmeraldSuccess)
                    DetailItemRow("Municipal Property Tax", "Current ($4,200.00 Paid)", CyberCyan)
                }
            }
        }
    }
}

@Composable fun CityUtilitiesScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Smart Meter EV Charging & Green Grid", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Solar Net Metering Credit", "+$48.20 Grid Inflow", EmeraldSuccess)
                    DetailItemRow("Off-Peak EV Supercharging Rate", "$0.08 / kWh", CyberCyan)
                }
            }
        }
    }
}

@Composable fun GovernmentPortalScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Sovereign Digital Government Gateway", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Direct Tax Settlement", "Zero-Knowledge Filing Synchronized", EmeraldSuccess)
                    DetailItemRow("Verified Citizen DID Status", "Level 4 Sovereign Citizen", GoldAccent)
                }
            }
        }
    }
}

@Composable fun DisasterManagementScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Emergency Relief & Micro-Disbursement Grid", color = CrimsonDanger, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Instant Crisis Aid Rail", "Sub-second biometric cash grant", EmeraldSuccess)
                    DetailItemRow("Disaster Reserve Pool", "$500,000,000.00 Liquid", CyberCyan)
                }
            }
        }
    }
}

@Composable fun HumanitarianNgoScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Transparent On-Chain Aid & Charity", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Aid Tracking Traceability", "100% On-Chain Proof", EmeraldSuccess)
                    DetailItemRow("Tax-Exempt 501(c)(3) Receipts", "Instant PDF Generation", CyberCyan)
                }
            }
        }
    }
}

@Composable fun SustainabilityCenterScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Planetary ESG Score & Green Bonds", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Corporate ESG Rating", "AAA (Top Tier)", EmeraldSuccess)
                    DetailItemRow("Reforestation Hectares Funded", "450 Hectares", CyberCyan)
                }
            }
        }
    }
}

@Composable fun ResearchInnovationScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Quantum & Autonomous FinTech R&D Lab", color = PurpleTech, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("ZKP Rollup Consensus", "Active Testnet Beta", CyberCyan)
                    DetailItemRow("Autonomous Agent Arbitrage", "99.4% Efficiency", EmeraldSuccess)
                }
            }
        }
    }
}

@Composable fun AiMarketplaceScreen(viewModel: BankViewModel) {
    val plugins = listOf(
        Pair("TaxBot Pro 2026", "Automated Form 1040/8886 Filing Plugin"),
        Pair("High-Frequency FX Arbitrage Bot", "Sub-millisecond currency spread optimizer"),
        Pair("Sovereign Estate Trustee", "Smart contract automated inheritance engine")
    )
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("AI Plugin & Integration Store", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp) }
        items(plugins) { (name, desc) ->
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                Row(modifier = Modifier.fillMaxWidth().padding(12.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(desc, color = TextMuted, fontSize = 11.sp)
                    }
                    Button(
                        onClick = { viewModel.showMessage("Plugin '$name' installed into your AI financial stack.") },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = Navy900),
                        shape = RoundedCornerShape(6.dp),
                        modifier = Modifier.height(34.dp)
                    ) {
                        Text("Install", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable fun ExecutiveAnalyticsScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Executive Board & KPI Intelligence", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Gross Margin", "78.4%", EmeraldSuccess)
                    DetailItemRow("Customer Lifetime Value (LTV)", "$14,800.00", CyberCyan)
                    DetailItemRow("CAC Payback Period", "4.2 Months", GoldAccent)
                }
            }
        }
    }
}

@Composable fun DeveloperCenterScreen(viewModel: BankViewModel) {
    var apiKey by remember { mutableStateOf("pk_live_998472910482_f91a") }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Developer APIs & Webhook Subscriptions", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Text("Active Production API Key:", color = TextMuted, fontSize = 11.sp)
                    Text(apiKey, color = CyberCyan, fontFamily = FontFamily.Monospace, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = {
                            apiKey = "pk_live_" + (10000000..99999999).random() + "_q2x"
                            viewModel.showMessage("✓ Generated new rotated production API key!")
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = Navy900),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(42.dp)
                    ) {
                        Text("Rotate API Key", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable fun FutureTechHubScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("CBDC, DeFi & IoT Streaming Micropayments", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Federal Reserve FedNow CBDC", "Connected & Audited", EmeraldSuccess)
                    DetailItemRow("Machine-to-Machine IoT Stream", "0.0001 USD / sec data stream", CyberCyan)
                }
            }
        }
    }
}

@Composable fun SpaceLunarEconomyScreen(viewModel: BankViewModel) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Lunar, Orbital & Mars Interplanetary Settlement", color = PurpleTech, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    DetailItemRow("Lagrangian L1 Relay Latency", "1.28 sec Roundtrip", CyberCyan)
                    DetailItemRow("Lunar Gateway Mining Rights Bond", "$1,200,000 Par Value", GoldAccent)
                    DetailItemRow("Autonomous Orbital Satellite Insurance", "Active Shield", EmeraldSuccess)
                }
            }
        }
    }
}

@Composable fun NotificationCenterScreen(viewModel: BankViewModel) {
    val notifs = listOf(
        Pair("Yield Payout Settled", "Received $28.40 from Treasury Staking (10 mins ago)"),
        Pair("Security Key Attestation Complete", "YubiKey 5C successfully validated (1 hr ago)"),
        Pair("Monthly Budget Report Ready", "August financial statement pack generated (4 hrs ago)")
    )
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        item { Text("Notification & Alert Stream", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp) }
        items(notifs) { (title, body) ->
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Text(body, color = TextMuted, fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable fun SettingsCenterScreen(viewModel: BankViewModel) {
    var darkMode by remember { mutableStateOf(true) }
    var biometricLogin by remember { mutableStateOf(true) }
    var instantAlerts by remember { mutableStateOf(true) }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("System Preferences & Security Settings", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Dark Futuristic Mode", color = TextWhite, fontSize = 13.sp)
                        Switch(checked = darkMode, onCheckedChange = { darkMode = it }, colors = SwitchDefaults.colors(checkedThumbColor = CyberCyan, checkedTrackColor = ElectricBlue))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Biometric Hardware Key 2FA", color = TextWhite, fontSize = 13.sp)
                        Switch(checked = biometricLogin, onCheckedChange = { biometricLogin = it }, colors = SwitchDefaults.colors(checkedThumbColor = CyberCyan, checkedTrackColor = ElectricBlue))
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text("Instant Push & Telegram Wire Alerts", color = TextWhite, fontSize = 13.sp)
                        Switch(checked = instantAlerts, onCheckedChange = { instantAlerts = it }, colors = SwitchDefaults.colors(checkedThumbColor = CyberCyan, checkedTrackColor = ElectricBlue))
                    }
                }
            }
        }
    }
}

@Composable fun HelpSupportScreen(viewModel: BankViewModel) {
    var ticketText by remember { mutableStateOf("") }
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
        item {
            Card(colors = CardDefaults.cardColors(containerColor = Navy800), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("24/7 Global Concierge & Support", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Average response time: 42 seconds (AI Concierge) or 2 mins (Private Banker).", color = TextMuted, fontSize = 12.sp)
                    Spacer(modifier = Modifier.height(10.dp))
                    OutlinedTextField(
                        value = ticketText,
                        onValueChange = { ticketText = it },
                        label = { Text("How can our treasury desk assist you?") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, unfocusedBorderColor = Navy700, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Button(
                        onClick = {
                            viewModel.showMessage("✓ Support ticket opened. Dedicated banker assigned.")
                            ticketText = ""
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = Navy900),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.fillMaxWidth().height(42.dp)
                    ) {
                        Text("Send Support Request", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}
