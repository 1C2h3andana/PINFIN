package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.components.HolographicGlassCard
import com.example.ui.components.PulsingStatusBadge
import com.example.ui.components.SuccessCelebrationParticles
import com.example.ui.theme.*
import com.example.ui.viewmodel.BankViewModel
import com.example.ui.viewmodel.MembershipTier
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

enum class BillingCycle(val label: String, val badge: String?) {
    MONTHLY("Monthly", null),
    ANNUAL("Annual Pass", "SAVE 25% + 2 MO FREE"),
    LIFETIME("Lifetime License", "BEST VALUE // PAY ONCE")
}

enum class PaymentRail(val title: String, val icon: ImageVector, val tag: String) {
    GOOGLE_PLAY("Google Play Billing", Icons.Default.ShoppingBag, "1-TAP PAY"),
    CREDIT_CARD("Credit / Debit (Stripe)", Icons.Default.CreditCard, "INSTANT"),
    CRYPTO_WEB3("Crypto Web3 (USDC / SOL)", Icons.Default.MonetizationOn, "ZERO FEE"),
    BANK_WIRE("FedNow / SWIFT Wire", Icons.Default.AccountBalance, "DIRECT")
}

@Composable
fun PaidEditionScreen(
    viewModel: BankViewModel,
    onNavigateBack: () -> Unit,
    onOpenTerms: () -> Unit = {},
    onOpenPrivacy: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val activeTier by viewModel.activeMembershipTier.collectAsStateWithLifecycle()
    val isProUnlocked by viewModel.isPaidVersionUnlocked.collectAsStateWithLifecycle()
    val licenseKey by viewModel.activeLicenseKey.collectAsStateWithLifecycle()
    val renewalDate by viewModel.subscriptionRenewalDate.collectAsStateWithLifecycle()

    var selectedCycle by remember { mutableStateOf(BillingCycle.ANNUAL) }
    var selectedTierChoice by remember { mutableStateOf(MembershipTier.PRO_PLANETARY) }
    var selectedPaymentRail by remember { mutableStateOf(PaymentRail.GOOGLE_PLAY) }

    var promoInput by remember { mutableStateOf("") }
    var isProcessingPayment by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showLicenseCopied by remember { mutableStateOf(false) }

    val primaryGradient = Brush.verticalGradient(
        colors = listOf(Navy900, NavyCard, Navy900)
    )

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(primaryGradient)
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 84.dp)
    ) {
        // --- Header ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .size(40.dp)
                        .background(Navy800, CircleShape)
                        .border(1.dp, Navy700, CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = TextWhite)
                }

                Surface(
                    color = AmberOrange.copy(alpha = 0.15f),
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, AmberOrange.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = AmberOrange, modifier = Modifier.size(14.dp))
                        Text(
                            text = "PAID EDITION & LICENSING",
                            color = AmberOrange,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 1.sp
                        )
                    }
                }

                TextButton(
                    onClick = { viewModel.restorePurchases() }
                ) {
                    Text("Restore", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        // --- Active License Status Banner ---
        item {
            HolographicGlassCard(
                borderColor = if (isProUnlocked) GoldAccent else CrimsonDanger,
                glowColor = if (isProUnlocked) GoldAccent.copy(alpha = 0.3f) else CrimsonDanger.copy(alpha = 0.3f),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape)
                            .background(if (isProUnlocked) GoldAccent.copy(alpha = 0.2f) else Navy900)
                            .border(1.5.dp, if (isProUnlocked) GoldAccent else CrimsonDanger, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (isProUnlocked) Icons.Default.Star else Icons.Default.Lock,
                            contentDescription = null,
                            tint = if (isProUnlocked) GoldAccent else CrimsonDanger,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                            PulsingStatusBadge(pulseColor = if (isProUnlocked) EmeraldSuccess else AmberOrange, badgeSize = 8.dp)
                            Text(
                                text = if (isProUnlocked) "ACTIVE LICENSE // ${activeTier.name}" else "TRIAL / STANDARD ACCESS",
                                color = TextWhite,
                                fontWeight = FontWeight.Black,
                                fontSize = 12.sp
                            )
                        }
                        Text(
                            text = "Key: $licenseKey",
                            color = CyberCyan,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace
                        )
                        Text(
                            text = "Status: $renewalDate",
                            color = TextMuted,
                            fontSize = 10.sp
                        )
                    }

                    IconButton(
                        onClick = {
                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("PFIN License Key", licenseKey)
                            clipboard.setPrimaryClip(clip)
                            viewModel.showMessage("✓ License Key copied to clipboard")
                        },
                        modifier = Modifier.size(36.dp).background(Navy800, CircleShape)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy Key", tint = CyberCyan, modifier = Modifier.size(16.dp))
                    }
                }
            }
        }

        // --- Hero Headline ---
        item {
            Column(
                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = "Unlock the Entire Planetary Financial Network",
                    color = TextWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    lineHeight = 26.sp
                )
                Text(
                    text = "Gain full unrestricted access to autonomous AGI Wealth advisory, Kyber-1024 Quantum vault, Digital Twin simulations, and space commerce rails.",
                    color = TextMuted,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(horizontal = 12.dp)
                )
            }
        }

        // --- Billing Cycle Switcher ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, Navy700)
            ) {
                Row(
                    modifier = Modifier.padding(6.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    BillingCycle.values().forEach { cycle ->
                        val isSelected = selectedCycle == cycle
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { selectedCycle = cycle },
                            color = if (isSelected) CyberCyan.copy(alpha = 0.2f) else Color.Transparent,
                            shape = RoundedCornerShape(12.dp),
                            border = BorderStroke(1.dp, if (isSelected) CyberCyan else Color.Transparent)
                        ) {
                            Column(
                                modifier = Modifier.padding(vertical = 8.dp, horizontal = 4.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                Text(
                                    text = cycle.label,
                                    color = if (isSelected) TextWhite else TextMuted,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp
                                )
                                if (cycle.badge != null) {
                                    Surface(
                                        color = if (isSelected) GoldAccent else Navy800,
                                        shape = RoundedCornerShape(4.dp)
                                    ) {
                                        Text(
                                            text = cycle.badge,
                                            color = if (isSelected) Navy900 else GoldAccent,
                                            fontSize = 7.sp,
                                            fontWeight = FontWeight.Black,
                                            modifier = Modifier.padding(horizontal = 4.dp, vertical = 1.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // --- Tier Cards ---
        val availableTiers = listOf(
            TierDisplayItem(
                tier = MembershipTier.FREE_CITIZEN,
                tag = "BASIC TIER",
                tagColor = TextMuted,
                price = "$0",
                period = "Free Forever",
                description = "Standard ledger, community AI assistant, and basic domestic transfers.",
                features = listOf(
                    "Standard Multi-Currency Account",
                    "Basic Domestic Money Transfers",
                    "Community AI Financial Q&A",
                    "Standard 2FA & PIN Security"
                ),
                isPopular = false,
                borderColor = Navy700
            ),
            TierDisplayItem(
                tier = MembershipTier.PRO_PLANETARY,
                tag = "MOST POPULAR",
                tagColor = CyberCyan,
                price = when (selectedCycle) {
                    BillingCycle.MONTHLY -> "$9.99"
                    BillingCycle.ANNUAL -> "$89.99"
                    BillingCycle.LIFETIME -> "$199"
                },
                period = when (selectedCycle) {
                    BillingCycle.MONTHLY -> "/ month"
                    BillingCycle.ANNUAL -> "/ year ($7.49/mo)"
                    BillingCycle.LIFETIME -> "one-time payment"
                },
                description = "Complete sovereign access for individuals, investors, and families.",
                features = listOf(
                    "24/7 Autonomous AGI Financial Concierge",
                    "Digital Twin 100,000 Monte Carlo Simulation",
                    "Zero-Fee SWIFT / ISO 20022 Global Wires",
                    "Smart City Unified Municipal Utilities",
                    "Real-Time High-Frequency AI Fraud Radar",
                    "Certified PDF/CSV Statement Exports"
                ),
                isPopular = true,
                borderColor = CyberCyan
            ),
            TierDisplayItem(
                tier = MembershipTier.QUANTUM_SOVEREIGN,
                tag = "SOVEREIGN LIFETIME VIP",
                tagColor = GoldAccent,
                price = when (selectedCycle) {
                    BillingCycle.MONTHLY -> "$29.99"
                    BillingCycle.ANNUAL -> "$249"
                    BillingCycle.LIFETIME -> "$499"
                },
                period = when (selectedCycle) {
                    BillingCycle.MONTHLY -> "/ month"
                    BillingCycle.ANNUAL -> "/ year"
                    BillingCycle.LIFETIME -> "lifetime master key"
                },
                description = "Military-grade post-quantum defense and space commerce connectivity.",
                features = listOf(
                    "Kyber-1024 NIST Lattice Cryptographic Vault",
                    "Interplanetary Moon/Mars Settlement Nodes",
                    "Direct Central Bank Programmable CBDC Rails",
                    "Zero-Slippage Multi-Asset Staking & Yields",
                    "Dedicated Human + Quantum Concierge VIP Line",
                    "Unrestricted Device Attestation (Up to 20 Devices)"
                ),
                isPopular = false,
                borderColor = GoldAccent
            )
        )

        items(availableTiers) { item ->
            val isSelected = selectedTierChoice == item.tier
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { selectedTierChoice = item.tier }
                    .testTag("tier_card_${item.tier.name}"),
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = if (isSelected) NavyCard else Navy900),
                border = BorderStroke(if (isSelected) 2.dp else 1.dp, if (isSelected) item.borderColor else Navy700)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = item.tagColor.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp),
                            border = BorderStroke(1.dp, item.tagColor.copy(alpha = 0.5f))
                        ) {
                            Text(
                                text = item.tag,
                                color = item.tagColor,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Black,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                            )
                        }

                        if (isSelected) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = item.borderColor, modifier = Modifier.size(16.dp))
                                Text("Selected Plan", color = item.borderColor, fontWeight = FontWeight.Bold, fontSize = 10.sp)
                            }
                        }
                    }

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Bottom
                    ) {
                        Column {
                            Text(item.tier.displayName, color = TextWhite, fontWeight = FontWeight.Black, fontSize = 16.sp)
                            Text(item.description, color = TextMuted, fontSize = 11.sp, lineHeight = 14.sp)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(item.price, color = item.borderColor, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp)
                            Text(item.period, color = TextMuted, fontSize = 10.sp)
                        }
                    }

                    Divider(color = Navy700.copy(alpha = 0.5f))

                    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        item.features.forEach { feat ->
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                Icon(Icons.Default.Check, contentDescription = null, tint = item.borderColor, modifier = Modifier.size(14.dp))
                                Text(feat, color = TextWhite, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }
        }

        // --- Payment Rails Selection ---
        item {
            Text("SELECT SECURE PAYMENT METHOD", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold, letterSpacing = 1.sp)
        }

        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                PaymentRail.values().forEach { rail ->
                    val isSelected = selectedPaymentRail == rail
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { selectedPaymentRail = rail },
                        color = if (isSelected) Navy800 else Navy900,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (isSelected) CyberCyan else Navy700)
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .background(CyberCyan.copy(alpha = 0.15f), CircleShape),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(rail.icon, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(18.dp))
                                }
                                Column {
                                    Text(rail.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                                    Text("Encrypted checkout with fraud guarantee", color = TextMuted, fontSize = 10.sp)
                                }
                            }

                            Surface(
                                color = if (isSelected) CyberCyan.copy(alpha = 0.2f) else Navy700,
                                shape = RoundedCornerShape(4.dp)
                            ) {
                                Text(
                                    text = rail.tag,
                                    color = if (isSelected) CyberCyan else TextMuted,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        // --- Promo / Institutional Grant Code ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Have an Academic / Institutional Grant or Promo Code?", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = promoInput,
                            onValueChange = { promoInput = it },
                            placeholder = { Text("e.g. PLANET2026, EARLYBIRD", color = TextMuted, fontSize = 11.sp) },
                            singleLine = true,
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedTextColor = TextWhite,
                                unfocusedTextColor = TextWhite,
                                focusedBorderColor = CyberCyan,
                                unfocusedBorderColor = Navy700
                            ),
                            modifier = Modifier.weight(1f).height(48.dp)
                        )
                        Button(
                            onClick = {
                                if (promoInput.isNotBlank()) {
                                    val success = viewModel.redeemPromoCode(promoInput)
                                    if (success) promoInput = ""
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = PurpleTech, contentColor = TextWhite),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.height(48.dp)
                        ) {
                            Text("Apply", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // --- Checkout Action CTA Button ---
        item {
            Button(
                onClick = {
                    isProcessingPayment = true
                    coroutineScope.launch {
                        delay(1600)
                        isProcessingPayment = false
                        viewModel.activatePaidPlan(
                            tier = selectedTierChoice,
                            paymentMethod = selectedPaymentRail.title
                        )
                        showSuccessDialog = true
                    }
                },
                enabled = !isProcessingPayment,
                colors = ButtonDefaults.buttonColors(
                    containerColor = when (selectedTierChoice) {
                        MembershipTier.QUANTUM_SOVEREIGN -> GoldAccent
                        MembershipTier.PRO_PLANETARY -> CyberCyan
                        else -> ElectricBlue
                    },
                    contentColor = Navy900
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("activate_paid_edition_button")
            ) {
                if (isProcessingPayment) {
                    CircularProgressIndicator(color = Navy900, modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Minting Quantum License Token...", fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                } else {
                    Icon(Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Activate ${selectedTierChoice.displayName} // Instant Access",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 13.sp
                    )
                }
            }
        }

        // --- Trust Badges & Guarantee ---
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Navy700)
            ) {
                Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TrustBadge(icon = Icons.Default.Shield, title = "30-Day Money Back", subtitle = "No questions asked")
                        TrustBadge(icon = Icons.Default.Lock, title = "256-Bit SSL Secured", subtitle = "Kyber-1024 Lattice")
                        TrustBadge(icon = Icons.Default.Autorenew, title = "Cancel Anytime", subtitle = "1-Click in Settings")
                    }

                    HorizontalDivider(color = Navy700)

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(onClick = onOpenTerms) {
                            Text("Terms & Licensing", color = TextMuted, fontSize = 10.sp)
                        }
                        Text("•", color = TextMuted)
                        TextButton(onClick = onOpenPrivacy) {
                            Text("Privacy Policy", color = TextMuted, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }

    // --- Success Dialog ---
    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { showSuccessDialog = false },
            containerColor = NavyCard,
            icon = {
                Box(
                    modifier = Modifier
                        .size(56.dp)
                        .background(EmeraldSuccess.copy(alpha = 0.2f), CircleShape)
                        .border(2.dp, EmeraldSuccess, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(32.dp))
                }
            },
            title = {
                Text(
                    text = "License Activated Successfully!",
                    color = TextWhite,
                    fontWeight = FontWeight.Black,
                    fontSize = 18.sp,
                    textAlign = TextAlign.Center
                )
            },
            text = {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Welcome to the Planetary Financial Network. Your sovereign digital license token has been minted and bound to your device.",
                        color = TextMuted,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    Surface(
                        color = Navy900,
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f))
                    ) {
                        Text(
                            text = licenseKey,
                            color = CyberCyan,
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showSuccessDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = Navy900),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text("Enter Planetary OS", fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

@Composable
private fun TrustBadge(icon: ImageVector, title: String, subtitle: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        Icon(icon, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(20.dp))
        Text(title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 10.sp)
        Text(subtitle, color = TextMuted, fontSize = 8.sp)
    }
}

private data class TierDisplayItem(
    val tier: MembershipTier,
    val tag: String,
    val tagColor: Color,
    val price: String,
    val period: String,
    val description: String,
    val features: List<String>,
    val isPopular: Boolean,
    val borderColor: Color
)
