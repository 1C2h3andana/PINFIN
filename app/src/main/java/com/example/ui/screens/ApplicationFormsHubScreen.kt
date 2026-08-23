package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.Apartment
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Draw
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Fingerprint
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Policy
import androidx.compose.material.icons.filled.RocketLaunch
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.UploadFile
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
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.ApplicationFormEntity
import com.example.data.model.ApplicationStatus
import com.example.data.model.ApplicationType
import com.example.ui.components.AnimatedScannerBeam
import com.example.ui.components.CyberRadarScan
import com.example.ui.components.PulsingStatusBadge
import com.example.ui.components.SuccessCelebrationParticles
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.CrimsonDanger
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.GoldAccent
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy900
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyCardLight
import com.example.ui.theme.PurpleTech
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BankViewModel
import kotlinx.coroutines.delay

@Composable
fun ApplicationFormsHubScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val applications by viewModel.applications.collectAsStateWithLifecycle()
    var selectedTopTab by remember { mutableIntStateOf(0) }
    val topTabs = listOf("New Application Wizard", "Live Tracker (${applications.size})")

    // Wizard Step State (1: Product, 2: Financials, 3: OCR & Biometrics, 4: AI Underwriting, 5: Instant Decision)
    var currentStep by remember { mutableIntStateOf(1) }

    // Application Form inputs
    var selectedAppType by remember { mutableStateOf(ApplicationType.LOAN) }
    var selectedProductTitle by remember { mutableStateOf("Low-APR Personal Loan ($35,000)") }
    var requestedAmount by remember { mutableDoubleStateOf(35000.0) }

    var applicantName by remember { mutableStateOf("Alex Morgan") }
    var email by remember { mutableStateOf("alex.morgan@smartbank.ai") }
    var phone by remember { mutableStateOf("+1 (555) 982-1049") }
    var annualIncome by remember { mutableDoubleStateOf(145000.0) }
    var existingMonthlyDebt by remember { mutableDoubleStateOf(1200.0) }
    var employmentType by remember { mutableStateOf("Salaried Full-Time (Tech)") }

    var idProofType by remember { mutableStateOf("US Passport / RealID") }
    var idProofNumber by remember { mutableStateOf("US-P9823481") }
    var isDocumentScanned by remember { mutableStateOf(false) }
    var isFaceMatchVerified by remember { mutableStateOf(false) }
    var isSignatureCompleted by remember { mutableStateOf(false) }

    // AI Underwriting simulation states
    var isUnderwritingInProgress by remember { mutableStateOf(false) }
    var underwritingProgress by remember { mutableFloatStateOf(0f) }
    var computedAiScore by remember { mutableIntStateOf(92) }
    var isApproved by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        // --- Screen Header ---
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "AI DIGITAL ONBOARDING & UNDERWRITING",
                            color = CyberCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.4.sp
                        )
                        PulsingStatusBadge(pulseColor = EmeraldSuccess, badgeSize = 8.dp)
                    }
                    Text(
                        text = "Smart Application Center",
                        color = TextWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // --- Top Tabs ---
        item {
            TabRow(
                selectedTabIndex = selectedTopTab,
                containerColor = NavyCard,
                contentColor = CyberCyan,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTopTab]),
                        color = CyberCyan,
                        height = 3.dp
                    )
                }
            ) {
                topTabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTopTab == index,
                        onClick = { selectedTopTab = index },
                        text = {
                            Text(
                                title,
                                fontSize = 13.sp,
                                fontWeight = if (selectedTopTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTopTab == index) CyberCyan else TextMuted
                            )
                        }
                    )
                }
            }
        }

        if (selectedTopTab == 0) {
            // --- Step Progress Wizard Header ---
            item {
                WizardStepIndicator(currentStep = currentStep)
            }

            // --- Step Content ---
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("application_wizard_card"),
                    colors = CardDefaults.cardColors(containerColor = NavyCard),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
                ) {
                    Column(modifier = Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {

                        when (currentStep) {
                            1 -> {
                                // STEP 1: PRODUCT & TIER SELECTION
                                StepOneProductSelection(
                                    selectedAppType = selectedAppType,
                                    selectedProductTitle = selectedProductTitle,
                                    onTypeSelected = { type, title, defaultAmount ->
                                        selectedAppType = type
                                        selectedProductTitle = title
                                        requestedAmount = defaultAmount
                                    }
                                )
                            }

                            2 -> {
                                // STEP 2: APPLICANT PROFILE & AFFORDABILITY
                                StepTwoFinancialProfile(
                                    applicantName = applicantName,
                                    onNameChange = { applicantName = it },
                                    email = email,
                                    onEmailChange = { email = it },
                                    phone = phone,
                                    onPhoneChange = { phone = it },
                                    annualIncome = annualIncome,
                                    onIncomeChange = { annualIncome = it },
                                    existingMonthlyDebt = existingMonthlyDebt,
                                    onDebtChange = { existingMonthlyDebt = it },
                                    employmentType = employmentType,
                                    onEmploymentChange = { employmentType = it }
                                )
                            }

                            3 -> {
                                // STEP 3: OCR DOCUMENT SCANNER & BIOMETRIC VERIFICATION
                                StepThreeDocumentOcrScanner(
                                    idProofType = idProofType,
                                    onIdTypeChange = { idProofType = it },
                                    idProofNumber = idProofNumber,
                                    onIdNumberChange = { idProofNumber = it },
                                    isDocumentScanned = isDocumentScanned,
                                    onScanTrigger = {
                                        isDocumentScanned = true
                                        viewModel.showMessage("✓ OCR Verification Complete: Identity & Income verified")
                                    },
                                    isFaceMatchVerified = isFaceMatchVerified,
                                    onFaceMatchTrigger = {
                                        isFaceMatchVerified = true
                                        viewModel.showMessage("✓ Biometric Passkey Linked: 99.8% Liveness Match")
                                    }
                                )
                            }

                            4 -> {
                                // STEP 4: AI UNDERWRITING NEURAL ANALYSIS
                                StepFourAiUnderwriting(
                                    isProcessing = isUnderwritingInProgress,
                                    progress = underwritingProgress,
                                    aiScore = computedAiScore,
                                    applicantIncome = annualIncome,
                                    dtiRatio = ((existingMonthlyDebt / (annualIncome / 12)) * 100),
                                    onStartUnderwriting = {
                                        isUnderwritingInProgress = true
                                    }
                                )

                                LaunchedEffect(currentStep) {
                                    if (currentStep == 4) {
                                        isUnderwritingInProgress = true
                                        underwritingProgress = 0f
                                        for (p in 1..10) {
                                            delay(200)
                                            underwritingProgress = p / 10f
                                        }
                                        val dti = (existingMonthlyDebt / (annualIncome / 12)) * 100
                                        computedAiScore = if (dti < 35) 94 else if (dti < 50) 86 else 72
                                        isApproved = computedAiScore >= 80
                                        isUnderwritingInProgress = false
                                    }
                                }
                            }

                            5 -> {
                                // STEP 5: INSTANT DECISION & ACTIVATION
                                StepFiveInstantApproval(
                                    isApproved = isApproved,
                                    appType = selectedAppType,
                                    productTitle = selectedProductTitle,
                                    requestedAmount = requestedAmount,
                                    aiScore = computedAiScore,
                                    applicantName = applicantName,
                                    isSignatureCompleted = isSignatureCompleted,
                                    onSign = {
                                        isSignatureCompleted = true
                                        viewModel.showMessage("✓ Digital Signature Stamped & Encrypted on Ledger")
                                    },
                                    onFinalizeAndDisburse = {
                                        viewModel.submitApplicationForm(
                                            type = selectedAppType,
                                            applicantName = applicantName,
                                            email = email,
                                            phone = phone,
                                            annualIncome = annualIncome,
                                            employmentType = employmentType,
                                            requestedAmountOrTier = selectedProductTitle,
                                            idProofType = idProofType,
                                            idProofNumber = idProofNumber
                                        )
                                        viewModel.showMessage("✓ Application Approved & Virtual Assets Provisioned!")
                                        selectedTopTab = 1
                                        currentStep = 1
                                    }
                                )
                            }
                        }

                        // --- Wizard Navigation Buttons ---
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            if (currentStep > 1 && currentStep < 5) {
                                OutlinedButton(
                                    onClick = { currentStep-- },
                                    colors = ButtonDefaults.outlinedButtonColors(contentColor = TextMuted),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Icon(Icons.Default.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text("Back", fontSize = 12.sp)
                                }
                            } else {
                                Spacer(modifier = Modifier.width(10.dp))
                            }

                            if (currentStep < 5) {
                                Button(
                                    onClick = {
                                        if (currentStep == 3 && (!isDocumentScanned || !isFaceMatchVerified)) {
                                            isDocumentScanned = true
                                            isFaceMatchVerified = true
                                        }
                                        currentStep++
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = CyberCyan),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.testTag("wizard_next_btn")
                                ) {
                                    Text(
                                        text = if (currentStep == 4) "View Decision" else "Next Step",
                                        color = Navy900,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Icon(Icons.Default.ArrowForward, contentDescription = null, tint = Navy900, modifier = Modifier.size(16.dp))
                                }
                            }
                        }
                    }
                }
            }
        } else {
            // --- TAB 2: LIVE APPLICATION TRACKER ---
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Live Underwriting Status (${applications.size})", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Surface(
                        color = NavyCardLight,
                        shape = RoundedCornerShape(8.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
                    ) {
                        Text(
                            text = "Auto-Synced",
                            color = EmeraldSuccess,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }

            items(applications, key = { it.id }) { app ->
                ApplicationTrackingCard(app = app)
            }
        }
    }
}

// -------------------------------------------------------------
// STEP 1: PRODUCT & TIER SELECTION
// -------------------------------------------------------------
@Composable
private fun StepOneProductSelection(
    selectedAppType: ApplicationType,
    selectedProductTitle: String,
    onTypeSelected: (ApplicationType, String, Double) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("STEP 1: CHOOSE FINANCIAL PRODUCT", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)
        Text("Select the financial product and credit tier you wish to apply for with instant AI pre-qualification:", color = TextMuted, fontSize = 12.sp)

        val products = listOf(
            ProductOption(
                type = ApplicationType.LOAN,
                title = "Personal Liquidity Loan ($35,000)",
                rateText = "6.99% APR • 36-Month Term",
                icon = Icons.Default.MonetizationOn,
                badge = "Instant Payout",
                color = CyberCyan,
                amount = 35000.0
            ),
            ProductOption(
                type = ApplicationType.LOAN,
                title = "Green Solar & Eco Home Loan ($75,000)",
                rateText = "4.25% APR • Carbon-Neutral Rebate",
                icon = Icons.Default.Eco,
                badge = "ESG Certified",
                color = EmeraldSuccess,
                amount = 75000.0
            ),
            ProductOption(
                type = ApplicationType.CREDIT_CARD,
                title = "Apex Titanium Infinite ($25,000 Limit)",
                rateText = "4x Travel Points • $0 Foreign Fee",
                icon = Icons.Default.CreditCard,
                badge = "Premium Tier",
                color = PurpleTech,
                amount = 25000.0
            ),
            ProductOption(
                type = ApplicationType.ACCOUNT_OPENING,
                title = "Global Multi-Currency Vault (5.2% APY)",
                rateText = "Instant USD/EUR/GBP FX Swaps",
                icon = Icons.Default.AccountBalance,
                badge = "FDIC Insured",
                color = ElectricBlue,
                amount = 10000.0
            ),
            ProductOption(
                type = ApplicationType.INSURANCE_POLICY,
                title = "CyberShield Identity & Fraud Defense ($100k)",
                rateText = "24/7 Dark Web Monitoring • $19/mo",
                icon = Icons.Default.Shield,
                badge = "Zero Deductible",
                color = GoldAccent,
                amount = 100000.0
            ),
            ProductOption(
                type = ApplicationType.ACCOUNT_OPENING,
                title = "Quantum Kyber-1024 Vault Account",
                rateText = "NIST Lattice-Protected • Entangled Key Backup",
                icon = Icons.Default.Memory,
                badge = "Quantum Safe",
                color = ElectricBlue,
                amount = 50000.0
            ),
            ProductOption(
                type = ApplicationType.LOAN,
                title = "Planetary & Space Commerce Facility ($250k)",
                rateText = "1.8% Subsidized APR • Lunar/Orbital Escrow",
                icon = Icons.Default.RocketLaunch,
                badge = "Interplanetary",
                color = PurpleTech,
                amount = 250000.0
            )
        )

        products.forEach { opt ->
            val isSelected = selectedProductTitle == opt.title
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onTypeSelected(opt.type, opt.title, opt.amount) }
                    .testTag("product_opt_${opt.type.name}"),
                color = if (isSelected) opt.color.copy(alpha = 0.15f) else Navy900,
                shape = RoundedCornerShape(14.dp),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isSelected) opt.color else Navy700
                )
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(opt.color.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(opt.icon, contentDescription = null, tint = opt.color, modifier = Modifier.size(20.dp))
                        }
                        Column {
                            Text(opt.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text(opt.rateText, color = TextMuted, fontSize = 11.sp)
                        }
                    }

                    Surface(
                        color = opt.color.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(6.dp)
                    ) {
                        Text(
                            text = opt.badge,
                            color = opt.color,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
            }
        }
    }
}

private data class ProductOption(
    val type: ApplicationType,
    val title: String,
    val rateText: String,
    val icon: ImageVector,
    val badge: String,
    val color: Color,
    val amount: Double
)

// -------------------------------------------------------------
// STEP 2: FINANCIAL PROFILE & AFFORDABILITY
// -------------------------------------------------------------
@Composable
private fun StepTwoFinancialProfile(
    applicantName: String,
    onNameChange: (String) -> Unit,
    email: String,
    onEmailChange: (String) -> Unit,
    phone: String,
    onPhoneChange: (String) -> Unit,
    annualIncome: Double,
    onIncomeChange: (Double) -> Unit,
    existingMonthlyDebt: Double,
    onDebtChange: (Double) -> Unit,
    employmentType: String,
    onEmploymentChange: (String) -> Unit
) {
    val monthlyIncome = annualIncome / 12
    val dti = ((existingMonthlyDebt / monthlyIncome) * 100).coerceAtLeast(0.0)

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("STEP 2: APPLICANT & AFFORDABILITY PROFILE", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)

        OutlinedTextField(
            value = applicantName,
            onValueChange = onNameChange,
            label = { Text("Legal Full Name") },
            leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = CyberCyan) },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().testTag("app_name_input"),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, unfocusedBorderColor = Navy700, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
        )

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = email,
                onValueChange = onEmailChange,
                label = { Text("Email Address") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, unfocusedBorderColor = Navy700, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
            )
            OutlinedTextField(
                value = phone,
                onValueChange = onPhoneChange,
                label = { Text("Mobile Number") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, unfocusedBorderColor = Navy700, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
            )
        }

        // Income Slider
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Annual Gross Income", color = TextWhite, fontSize = 12.sp)
                Text("$${"%,.0f".format(annualIncome)}/year ($${"%,.0f".format(monthlyIncome)}/mo)", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Slider(
                value = annualIncome.toFloat(),
                onValueChange = { onIncomeChange(it.toDouble()) },
                valueRange = 30000f..300000f,
                steps = 27,
                colors = SliderDefaults.colors(thumbColor = EmeraldSuccess, activeTrackColor = EmeraldSuccess, inactiveTrackColor = Navy700)
            )
        }

        // Debt Slider
        Column {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text("Existing Monthly Debt / EMIs", color = TextWhite, fontSize = 12.sp)
                Text("$${"%,.0f".format(existingMonthlyDebt)}/mo", color = if (dti < 35) EmeraldSuccess else AmberOrange, fontWeight = FontWeight.Bold, fontSize = 12.sp)
            }
            Slider(
                value = existingMonthlyDebt.toFloat(),
                onValueChange = { onDebtChange(it.toDouble()) },
                valueRange = 0f..6000f,
                steps = 60,
                colors = SliderDefaults.colors(thumbColor = AmberOrange, activeTrackColor = AmberOrange, inactiveTrackColor = Navy700)
            )
        }

        // DTI Affordability Badge
        Surface(
            color = Navy900,
            shape = RoundedCornerShape(10.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (dti < 35) EmeraldSuccess else AmberOrange),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text("Debt-to-Income (DTI) Affordability Ratio", color = TextMuted, fontSize = 11.sp)
                    Text(
                        text = "${"%.1f".format(dti)}% (Threshold: < 45% for Prime Rates)",
                        color = if (dti < 35) EmeraldSuccess else AmberOrange,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                }
                Icon(
                    imageVector = if (dti < 35) Icons.Default.CheckCircle else Icons.Default.Shield,
                    contentDescription = null,
                    tint = if (dti < 35) EmeraldSuccess else AmberOrange
                )
            }
        }
    }
}

// -------------------------------------------------------------
// STEP 3: OCR SCANNER & BIOMETRIC VERIFICATION
// -------------------------------------------------------------
@Composable
private fun StepThreeDocumentOcrScanner(
    idProofType: String,
    onIdTypeChange: (String) -> Unit,
    idProofNumber: String,
    onIdNumberChange: (String) -> Unit,
    isDocumentScanned: Boolean,
    onScanTrigger: () -> Unit,
    isFaceMatchVerified: Boolean,
    onFaceMatchTrigger: () -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Text("STEP 3: AI OCR SCANNER & BIOMETRIC KYC", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = idProofType,
                onValueChange = onIdTypeChange,
                label = { Text("Government ID Type") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, unfocusedBorderColor = Navy700, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
            )
            OutlinedTextField(
                value = idProofNumber,
                onValueChange = onIdNumberChange,
                label = { Text("ID Document Number") },
                singleLine = true,
                modifier = Modifier.weight(1f),
                colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, unfocusedBorderColor = Navy700, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite)
            )
        }

        // Animated OCR Scanning Viewfinder
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Navy900),
            shape = RoundedCornerShape(14.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (isDocumentScanned) EmeraldSuccess else CyberCyan)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .clickable { onScanTrigger() },
                contentAlignment = Alignment.Center
            ) {
                if (!isDocumentScanned) {
                    AnimatedScannerBeam(beamColor = CyberCyan)
                }

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = if (isDocumentScanned) Icons.Default.VerifiedUser else Icons.Default.UploadFile,
                        contentDescription = null,
                        tint = if (isDocumentScanned) EmeraldSuccess else CyberCyan,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = if (isDocumentScanned) "✓ RealID & W-2 Income Verified (OCR Match 100%)" else "Tap to Scan RealID & Proof of Income",
                        color = TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 12.sp
                    )
                    Text(
                        text = "Encrypted On-Device Neural OCR Processing",
                        color = TextMuted,
                        fontSize = 10.sp
                    )
                }
            }
        }

        // Biometric Face Liveness Card
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { onFaceMatchTrigger() },
            color = Navy900,
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (isFaceMatchVerified) EmeraldSuccess else Navy700)
        ) {
            Row(
                modifier = Modifier.padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Icon(
                        imageVector = if (isFaceMatchVerified) Icons.Default.Face else Icons.Default.Fingerprint,
                        contentDescription = null,
                        tint = if (isFaceMatchVerified) EmeraldSuccess else CyberCyan
                    )
                    Column {
                        Text(
                            text = if (isFaceMatchVerified) "Biometric Passkey Authenticated" else "Link Biometric Liveness Passkey",
                            color = TextWhite,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                        Text("3D Facial Geometry & Hardware Security Module", color = TextMuted, fontSize = 10.sp)
                    }
                }
                Text(
                    text = if (isFaceMatchVerified) "PASS" else "VERIFY",
                    color = if (isFaceMatchVerified) EmeraldSuccess else CyberCyan,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp
                )
            }
        }
    }
}

// -------------------------------------------------------------
// STEP 4: AI UNDERWRITING NEURAL ANALYSIS
// -------------------------------------------------------------
@Composable
private fun StepFourAiUnderwriting(
    isProcessing: Boolean,
    progress: Float,
    aiScore: Int,
    applicantIncome: Double,
    dtiRatio: Double,
    onStartUnderwriting: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Text("STEP 4: AI AUTOMATED UNDERWRITING", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.ExtraBold)

        CyberRadarScan(radarColor = if (isProcessing) CyberCyan else EmeraldSuccess)

        if (isProcessing) {
            Text("Neural Engine Evaluating 50+ Financial Attributes...", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp)),
                color = CyberCyan,
                trackColor = Navy900
            )
            Text("Analyzing Cashflow Stability • Credit Bureau Records • AML Risk Matrix", color = TextMuted, fontSize = 11.sp)
        } else {
            Surface(
                color = Navy900,
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("SmartBank Underwriting Score", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("$aiScore / 100", color = EmeraldSuccess, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                    }

                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text("• Cashflow Reserve: Strong liquidity buffer (3.8x monthly expenses)", color = TextWhite.copy(alpha = 0.9f), fontSize = 11.sp)
                        Text("• Debt-to-Income: ${"%.1f".format(dtiRatio)}% meets prime tier criteria", color = TextWhite.copy(alpha = 0.9f), fontSize = 11.sp)
                        Text("• AML Compliance Check: Passed with 0 jurisdictional flags", color = TextWhite.copy(alpha = 0.9f), fontSize = 11.sp)
                        Text("• Recommended Action: Instant Automated Approval with zero manual review", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// STEP 5: INSTANT DECISION & ACTIVATION
// -------------------------------------------------------------
@Composable
private fun StepFiveInstantApproval(
    isApproved: Boolean,
    appType: ApplicationType,
    productTitle: String,
    requestedAmount: Double,
    aiScore: Int,
    applicantName: String,
    isSignatureCompleted: Boolean,
    onSign: () -> Unit,
    onFinalizeAndDisburse: () -> Unit
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        SuccessCelebrationParticles()

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(48.dp))

            Text("CONGRATULATIONS!", color = EmeraldSuccess, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            Text(
                text = "Your application for $productTitle has been instantly PRE-APPROVED by our AI Underwriter.",
                color = TextWhite,
                fontSize = 12.sp,
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Virtual Card or Certificate Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan)
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("SMARTBANK AI ISSUANCE", color = CyberCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text("AI SCORE: $aiScore", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    }
                    Text(productTitle, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text("Holder: $applicantName", color = TextMuted, fontSize = 11.sp)
                    Text("Facility: $${"%,.2f".format(requestedAmount)} Active Credit Line", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
            }

            // Digital Signature Pad Simulation
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSign() },
                color = Navy900,
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isSignatureCompleted) EmeraldSuccess else Navy700)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Draw, contentDescription = null, tint = if (isSignatureCompleted) EmeraldSuccess else CyberCyan)
                        Text(
                            text = if (isSignatureCompleted) "Digital Signature: '$applicantName' [VERIFIED]" else "Tap to Electronically Sign Digital Agreement",
                            color = TextWhite,
                            fontSize = 12.sp
                        )
                    }
                    if (isSignatureCompleted) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = EmeraldSuccess)
                    }
                }
            }

            Button(
                onClick = onFinalizeAndDisburse,
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth().height(48.dp).testTag("finalize_application_btn")
            ) {
                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Navy900)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Activate Product & Disburse Funds Now", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 13.sp)
            }
        }
    }
}

// -------------------------------------------------------------
// WIZARD STEP INDICATOR
// -------------------------------------------------------------
@Composable
private fun WizardStepIndicator(currentStep: Int) {
    val steps = listOf("Product", "Finances", "OCR KYC", "Underwrite", "Approved")

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, name ->
            val stepNumber = index + 1
            val isDone = stepNumber < currentStep
            val isCurrent = stepNumber == currentStep

            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(
                    modifier = Modifier
                        .size(28.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isDone -> EmeraldSuccess
                                isCurrent -> CyberCyan
                                else -> Navy700
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isDone) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = Navy900, modifier = Modifier.size(16.dp))
                    } else {
                        Text(
                            text = "$stepNumber",
                            color = if (isCurrent) Navy900 else TextMuted,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )
                    }
                }
                Text(
                    text = name,
                    color = if (isCurrent) CyberCyan else if (isDone) EmeraldSuccess else TextMuted,
                    fontSize = 9.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}

// -------------------------------------------------------------
// LIVE APPLICATION TRACKING CARD
// -------------------------------------------------------------
@Composable
private fun ApplicationTrackingCard(app: ApplicationFormEntity) {
    Card(
        modifier = Modifier.fillMaxWidth().testTag("app_card_${app.id}"),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(CyberCyan.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (app.type) {
                                ApplicationType.LOAN -> Icons.Default.MonetizationOn
                                ApplicationType.CREDIT_CARD -> Icons.Default.CreditCard
                                ApplicationType.ACCOUNT_OPENING -> Icons.Default.AccountBalance
                                ApplicationType.INSURANCE_POLICY -> Icons.Default.Policy
                            },
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(app.requestedAmountOrTier, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("App #${app.id} • ${app.applicantName}", color = TextMuted, fontSize = 11.sp)
                    }
                }

                Surface(
                    color = when (app.status) {
                        ApplicationStatus.APPROVED, ApplicationStatus.AI_PRE_APPROVED -> EmeraldSuccess.copy(alpha = 0.2f)
                        ApplicationStatus.REJECTED -> CrimsonDanger.copy(alpha = 0.2f)
                        else -> AmberOrange.copy(alpha = 0.2f)
                    },
                    shape = RoundedCornerShape(6.dp)
                ) {
                    Text(
                        text = app.status.name.replace('_', ' '),
                        color = when (app.status) {
                            ApplicationStatus.APPROVED, ApplicationStatus.AI_PRE_APPROVED -> EmeraldSuccess
                            ApplicationStatus.REJECTED -> CrimsonDanger
                            else -> AmberOrange
                        },
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            // Timeline Steps
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimelineNode(title = "OCR Doc", completed = true)
                TimelineNode(title = "Biometrics", completed = true)
                TimelineNode(title = "AI Score ${app.aiScore}", completed = true)
                TimelineNode(title = "Disbursed", completed = app.status == ApplicationStatus.APPROVED || app.status == ApplicationStatus.AI_PRE_APPROVED)
            }

            Surface(color = Navy900, shape = RoundedCornerShape(8.dp), modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    Text("AI Underwriter Remarks:", color = TextMuted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                    Text(app.aiRemarks, color = TextWhite.copy(alpha = 0.9f), fontSize = 11.sp)
                }
            }
        }
    }
}

@Composable
private fun TimelineNode(title: String, completed: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .clip(CircleShape)
                .background(if (completed) EmeraldSuccess else Navy700),
            contentAlignment = Alignment.Center
        ) {
            if (completed) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Navy900, modifier = Modifier.size(10.dp))
            }
        }
        Text(title, color = if (completed) TextWhite else TextMuted, fontSize = 10.sp)
    }
}
