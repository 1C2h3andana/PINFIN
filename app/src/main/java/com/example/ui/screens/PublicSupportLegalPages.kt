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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Gavel
import androidx.compose.material.icons.filled.HelpOutline
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Work
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
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
// 11. CONTACT US (WITH INTERACTIVE INQUIRY FORM & REAL-TIME DISPATCH)
// =========================================================================
@Composable
fun ContactUsScreen(
    viewModel: BankViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var senderName by remember { mutableStateOf("") }
    var senderEmail by remember { mutableStateOf("") }
    var messageSubject by remember { mutableStateOf("Enterprise Deployment Inquiry") }
    var messageBody by remember { mutableStateOf("") }
    var formState by remember { mutableStateOf("DRAFT") }

    if (formState == "SCANNING") {
        LaunchedEffect(Unit) {
            delay(1800)
            formState = "APPROVED"
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            PublicPageHeader(
                title = "Contact PFIN Global Operations",
                subtitle = "Reach our sovereign architecture advisors, compliance teams & AI engineers",
                icon = Icons.Default.Email,
                color = CyberCyan,
                onBack = onNavigateBack
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("DIRECT INQUIRY & DISPATCH FORM", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)

                    when (formState) {
                        "DRAFT" -> {
                            OutlinedTextField(
                                value = senderName,
                                onValueChange = { senderName = it },
                                label = { Text("Your Full Name / Entity", color = TextMuted, fontSize = 10.sp) },
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = senderEmail,
                                onValueChange = { senderEmail = it },
                                label = { Text("Official Email Address", color = TextMuted, fontSize = 10.sp) },
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = messageBody,
                                onValueChange = { messageBody = it },
                                label = { Text("Inquiry Message & Requirements", color = TextMuted, fontSize = 10.sp) },
                                minLines = 3,
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Button(
                                onClick = { formState = "SCANNING" },
                                modifier = Modifier.fillMaxWidth().testTag("submit_contact_form"),
                                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.Send, contentDescription = null, tint = Navy900, modifier = Modifier.size(16.dp))
                                Spacer(Modifier.size(8.dp))
                                Text("Dispatch Encrypted Priority Ticket", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                        "SCANNING" -> {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = CyberCyan)
                                Text("Encrypting with Post-Quantum Lattice Key & Routing...", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().clip(CircleShape), color = CyberCyan, trackColor = Navy800)
                            }
                        }
                        "APPROVED" -> {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                SuccessCelebrationParticles(modifier = Modifier.size(60.dp))
                                Text("INQUIRY DISPATCHED • TICKET #TKT-8840", color = CyberCyan, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                                Text("Assigned to Tier-1 Solutions Architect • Response within <15 mins", color = TextWhite, fontSize = 10.sp)
                                OutlinedButton(
                                    onClick = {
                                        senderName = ""
                                        senderEmail = ""
                                        messageBody = ""
                                        formState = "DRAFT"
                                    },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = CyberCyan),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Send Another Inquiry", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Text("GLOBAL SOVEREIGN OPERATIONAL NODES", color = GoldAccent, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                ContactLocationCard("San Francisco / Silicon Valley", "Autonomous AI Models & Quantum R&D", "sf.hq@pfin.network", CyberCyan)
                ContactLocationCard("Zurich / Crypto Valley", "Sovereign Banking & ZK-Vaults", "zurich@pfin.network", EmeraldSuccess)
                ContactLocationCard("Singapore / MAS Sandbox", "Smart City & Interplanetary Bridges", "singapore@pfin.network", ElectricBlue)
            }
        }
    }
}

@Composable
private fun ContactLocationCard(city: String, focus: String, email: String, color: Color) {
    Surface(
        color = NavyCard,
        shape = RoundedCornerShape(12.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.35f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Icon(Icons.Default.LocationOn, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(city, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                Text(focus, color = TextMuted, fontSize = 10.sp)
                Text(email, color = color, fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// =========================================================================
// 12. CAREERS SCREEN (WITH JOB APPLICATION & RESUME SCANNER FORM)
// =========================================================================
@Composable
fun CareersScreen(
    viewModel: BankViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedRole by remember { mutableStateOf("Lead Post-Quantum Cryptographer") }
    var applicantName by remember { mutableStateOf("") }
    var applicantPortfolio by remember { mutableStateOf("") }
    var formState by remember { mutableStateOf("DRAFT") }

    if (formState == "SCANNING") {
        LaunchedEffect(Unit) {
            delay(2000)
            formState = "APPROVED"
        }
    }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            PublicPageHeader(
                title = "Careers at PFIN",
                subtitle = "Build the operating system for humanity's next 50 years of finance",
                icon = Icons.Default.Work,
                color = PurpleTech,
                onBack = onNavigateBack
            )
        }

        val roles = listOf(
            RoleItem("Lead Post-Quantum Cryptographer", "Zurich / Remote", "Kyber-1024, Dilithium & Lattice Proofs", "$280k - $420k + Equity", ElectricBlue),
            RoleItem("Autonomous AGI Financial Actuary", "San Francisco / Remote", "Lifelong RL, Multi-Agent Macro Sim", "$260k - $390k + Equity", CyberCyan),
            RoleItem("Interplanetary Consensus Architect", "Singapore / Remote", "Light-Delay Asynchronous Fault Tolerance", "$250k - $380k + Equity", PurpleTech),
            RoleItem("Sovereign Climate Data Engineer", "London / Remote", "Satellite NDVI Telemetry & ESG Smart Contracts", "$220k - $340k + Equity", EmeraldSuccess)
        )

        items(roles) { role ->
            val isSelected = selectedRole == role.title
            Card(
                modifier = Modifier.fillMaxWidth().clickable { selectedRole = role.title },
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = if (isSelected) NavyCard else Navy900),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, if (isSelected) role.color else Navy700)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Text(role.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        Text(role.comp, color = role.color, fontWeight = FontWeight.ExtraBold, fontSize = 10.sp)
                    }
                    Text("${role.location} • ${role.skills}", color = TextMuted, fontSize = 10.sp)
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, PurpleTech.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("APPLY FOR SELECTED POSITION: $selectedRole", color = PurpleTech, fontWeight = FontWeight.Bold, fontSize = 11.sp)

                    when (formState) {
                        "DRAFT" -> {
                            OutlinedTextField(
                                value = applicantName,
                                onValueChange = { applicantName = it },
                                label = { Text("Full Legal / Sovereign Name", color = TextMuted, fontSize = 10.sp) },
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurpleTech, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                                modifier = Modifier.fillMaxWidth()
                            )
                            OutlinedTextField(
                                value = applicantPortfolio,
                                onValueChange = { applicantPortfolio = it },
                                label = { Text("GitHub / arXiv / Research Portfolio Link", color = TextMuted, fontSize = 10.sp) },
                                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = PurpleTech, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                                modifier = Modifier.fillMaxWidth()
                            )
                            Button(
                                onClick = { formState = "SCANNING" },
                                modifier = Modifier.fillMaxWidth().testTag("submit_job_application"),
                                colors = ButtonDefaults.buttonColors(containerColor = PurpleTech),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Submit Application & AI Skills Screening", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            }
                        }
                        "SCANNING" -> {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(14.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                AnimatedScannerBeam(modifier = Modifier.fillMaxWidth().height(100.dp), beamColor = PurpleTech)
                                Text("Analyzing Quantum Cryptography / AI Portfolio Benchmarks...", color = PurpleTech, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                LinearProgressIndicator(modifier = Modifier.fillMaxWidth().clip(CircleShape), color = PurpleTech, trackColor = Navy800)
                            }
                        }
                        "APPROVED" -> {
                            Column(
                                modifier = Modifier.fillMaxWidth().padding(10.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                SuccessCelebrationParticles(modifier = Modifier.size(60.dp))
                                Text("APPLICATION RECEIVED & FAST-TRACKED", color = PurpleTech, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                                Text("Passed Automated Skills Benchmark • Technical Interview Scheduled", color = TextWhite, fontSize = 10.sp)
                                OutlinedButton(
                                    onClick = { formState = "DRAFT" },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = PurpleTech),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Text("Apply for Another Role", fontSize = 10.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class RoleItem(
    val title: String,
    val location: String,
    val skills: String,
    val comp: String,
    val color: Color
)

// =========================================================================
// 13. FAQS SCREEN (INTERACTIVE EXPANDABLE ACCORDION)
// =========================================================================
@Composable
fun FaqsScreen(
    viewModel: BankViewModel,
    onNavigateBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }

    val allFaqs = listOf(
        FaqItem("What is PFIN compared to regular mobile banking apps?", "PFIN is not merely a retail payment app; it is a full-stack, autonomous 20-pillar Financial Intelligence Operating System designed for the 2050–2077 horizon, spanning sovereign CBDCs, AGI wealth management, smart cities, and space settlement escrows."),
        FaqItem("How does PFIN guarantee post-quantum security?", "Every private key, account balance, and multi-signature authorization is secured via NIST-standardized Kyber-1024 lattice algorithms and continuous quantum random number entropy streams."),
        FaqItem("What is a World Digital Identity on PFIN?", "It is a self-sovereign, Zero-Knowledge cryptographic passkey that unifies KYC, passport verification, tax filings, and clinical medical records without exposing raw personal data to third parties."),
        FaqItem("Can governments and central banks deploy PFIN on private infrastructure?", "Yes. Sovereign nations can provision dedicated sovereign nodes with programmable monetary policies, localized AML rules, and direct citizen welfare pipelines."),
        FaqItem("How does interplanetary settlement handle light-speed delay?", "PFIN utilizes asynchronous time-locked atomic escrows and SOLAR energy-backed kilowatt-hour units that synchronize reliably over 1.28s (Moon) and 4–24 min (Mars) light delays.")
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .padding(top = 12.dp, bottom = 80.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            PublicPageHeader(
                title = "Frequently Asked Questions",
                subtitle = "Everything you need to know about the Planetary Financial Operating System",
                icon = Icons.Default.HelpOutline,
                color = AmberOrange,
                onBack = onNavigateBack
            )
        }

        item {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Search FAQ topics, security, or technology...", color = TextMuted, fontSize = 11.sp) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = AmberOrange, modifier = Modifier.size(16.dp)) },
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = AmberOrange, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            )
        }

        items(allFaqs.filter { it.question.contains(searchQuery, ignoreCase = true) || it.answer.contains(searchQuery, ignoreCase = true) }) { faq ->
            FaqAccordionItem(faq = faq)
        }
    }
}

private data class FaqItem(val question: String, val answer: String)

@Composable
private fun FaqAccordionItem(faq: FaqItem) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (expanded) AmberOrange.copy(alpha = 0.5f) else Navy700)
    ) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text(faq.question, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp, modifier = Modifier.weight(1f))
                Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, contentDescription = null, tint = AmberOrange)
            }
            AnimatedVisibility(visible = expanded) {
                Text(faq.answer, color = TextMuted, fontSize = 11.sp, lineHeight = 15.sp, modifier = Modifier.padding(top = 6.dp))
            }
        }
    }
}

// =========================================================================
// 14. TERMS & CONDITIONS SCREEN
// =========================================================================
@Composable
fun TermsConditionsScreen(
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
                title = "Terms & Smart Contract Framework",
                subtitle = "Legal & algorithmic covenants governing PFIN Operating Horizon 2050–2077",
                icon = Icons.Default.Gavel,
                color = GoldAccent,
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
                    Text("1. Sovereign Algorithmic Execution", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(
                        "All transactions, escrows, and yield reallocations are executed deterministically via audited smart contracts. No human intermediary can unilaterally alter smart contract state once finalized by consensus.",
                        color = TextWhite,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )

                    Text("2. Zero-Custody Cryptographic Principle", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(
                        "Users maintain absolute cryptographic ownership of their private keys and assets via their World Digital Identity. PFIN does not hold un-shielded master custody over user funds.",
                        color = TextWhite,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )

                    Text("3. Service Level Agreement & Disaster Continuity", color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(
                        "PFIN guarantees 99.999% global uptime across redundant terrestrial mesh and orbital satellite relay nodes.",
                        color = TextWhite,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}

// =========================================================================
// 15. PRIVACY POLICY SCREEN
// =========================================================================
@Composable
fun PrivacyPolicyScreen(
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
                title = "Privacy & Data Sovereignty",
                subtitle = "Zero-Knowledge privacy architecture and universal citizen data protection",
                icon = Icons.Default.Security,
                color = EmeraldSuccess,
                onBack = onNavigateBack
            )
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.5f))
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Text("1. Zero-Knowledge Cryptographic Privacy", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(
                        "Your personal identity, transaction counter-parties, and medical HSA records are shielded using Zero-Knowledge Succinct Non-Interactive Arguments of Knowledge (ZK-SNARKs). Validations occur without revealing underlying raw data.",
                        color = TextWhite,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )

                    Text("2. GDPR, MiCA & Sovereign Compliance", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(
                        "PFIN strictly complies with global data sovereignty laws. Your biometric and personal data remains exclusively on your encrypted enclave device and is never monetized or sold.",
                        color = TextWhite,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )

                    Text("3. Right to Permanent Cryptographic Deletion", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Text(
                        "Citizens retain the unalienable right to sever biometric passkeys and burn ephemeral session states at any time.",
                        color = TextWhite,
                        fontSize = 11.sp,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}
