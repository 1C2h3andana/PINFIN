package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessibilityNew
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Language
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
import com.example.ui.theme.NavyCardLight
import com.example.ui.theme.PurpleTech
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BankViewModel
import kotlin.math.pow

@Composable
fun LiteracyStudentHubScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Anti-Scam Academy", "Wealth & Compound Tool", "Inclusion & ESG")

    val selectedLanguage by viewModel.selectedLanguage.collectAsStateWithLifecycle()
    val isAccessibleSeniorMode by viewModel.isAccessibleSeniorMode.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        Text(
                            text = "FINANCIAL INCLUSION & LITERACY ACADEMY",
                            color = CyberCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.4.sp
                        )
                        PulsingStatusBadge(pulseColor = GoldAccent, badgeSize = 8.dp)
                    }
                    Text(
                        text = "Empowerment & Education",
                        color = TextWhite,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        item {
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = NavyCard,
                contentColor = CyberCyan,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = CyberCyan,
                        height = 3.dp
                    )
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        text = {
                            Text(
                                title,
                                fontSize = 12.sp,
                                fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == index) CyberCyan else TextMuted
                            )
                        }
                    )
                }
            }
        }

        when (selectedTab) {
            0 -> {
                // TAB 1: ANTI-SCAM & CYBER DEFENSE ACADEMY
                item {
                    AntiScamAcademyModule(viewModel = viewModel)
                }
            }
            1 -> {
                // TAB 2: WEALTH & COMPOUND CALCULATOR
                item {
                    WealthAndCompoundModule(viewModel = viewModel)
                }
            }
            2 -> {
                // TAB 3: GLOBAL INCLUSION, ACCESSIBILITY & GREEN ESG
                item {
                    GlobalInclusionAndEsgModule(
                        selectedLanguage = selectedLanguage,
                        onLanguageSelect = { viewModel.setLanguage(it) },
                        isAccessibleSeniorMode = isAccessibleSeniorMode,
                        onToggleSeniorMode = { viewModel.toggleAccessibleSeniorMode() },
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 1: ANTI-SCAM ACADEMY WITH INTERACTIVE QUIZ
// -------------------------------------------------------------
@Composable
private fun AntiScamAcademyModule(viewModel: BankViewModel) {
    var userScore by remember { mutableIntStateOf(0) }
    var answeredQuestions by remember { mutableStateOf(setOf<Int>()) }

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Academy Header Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, GoldAccent.copy(alpha = 0.5f))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Box(
                        modifier = Modifier.size(44.dp).clip(CircleShape).background(GoldAccent.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.EmojiEvents, contentDescription = null, tint = GoldAccent, modifier = Modifier.size(24.dp))
                    }
                    Column {
                        Text("Zero-Trust Cyber Defense Academy", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("Earn your Certified Cyber Sentinel Badge", color = TextMuted, fontSize = 11.sp)
                    }
                }
                Surface(
                    color = GoldAccent.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(
                        text = "Score: $userScore/300",
                        color = GoldAccent,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }

        // Quiz Question 1
        QuizCard(
            questionId = 1,
            scenario = "Scenario 1: You receive an SMS claiming your bank account is suspended and to click 'http://smartbank-login-secure.xyz' within 10 minutes to verify your identity.",
            options = listOf(
                "Click the link immediately to prevent account freeze",
                "Forward the SMS to friends to warn them",
                "Delete & Report Phishing URL: Legitimate banks never send urgent shortlinks"
            ),
            correctIndex = 2,
            isAnswered = answeredQuestions.contains(1),
            onAnswer = { isCorrect ->
                if (!answeredQuestions.contains(1)) {
                    answeredQuestions = answeredQuestions + 1
                    if (isCorrect) {
                        userScore += 100
                        viewModel.showMessage("✓ Correct! +100 Cyber Sentinel XP")
                    } else {
                        viewModel.showMessage("Incorrect: Never click suspicious external domains.")
                    }
                }
            }
        )

        // Quiz Question 2
        QuizCard(
            questionId = 2,
            scenario = "Scenario 2: An online seller tells you: 'I am sending you a refund QR code. Scan it and enter your UPI PIN to claim $250 cashback'.",
            options = listOf(
                "Scanning a QR and entering a PIN RECEIVES money into your account",
                "Entering a PIN ALWAYS AUTHORIZES A DEBIT (Sends money OUT of your account)",
                "It is safe if they send a government photo ID"
            ),
            correctIndex = 1,
            isAnswered = answeredQuestions.contains(2),
            onAnswer = { isCorrect ->
                if (!answeredQuestions.contains(2)) {
                    answeredQuestions = answeredQuestions + 2
                    if (isCorrect) {
                        userScore += 100
                        viewModel.showMessage("✓ Correct! +100 Cyber Sentinel XP")
                    } else {
                        viewModel.showMessage("Incorrect: PIN is ONLY used to SEND money, never receive.")
                    }
                }
            }
        )

        // Quiz Question 3
        QuizCard(
            questionId = 3,
            scenario = "Scenario 3: Someone calling from 'SmartBank Fraud Desk' asks you to read back the 6-digit OTP sent to your phone to reverse a fake transaction.",
            options = listOf(
                "Read the OTP since the caller identified themselves as bank staff",
                "NEVER share OTPs with anyone. Bank representatives will NEVER ask for your OTP",
                "Read only the first 3 digits"
            ),
            correctIndex = 1,
            isAnswered = answeredQuestions.contains(3),
            onAnswer = { isCorrect ->
                if (!answeredQuestions.contains(3)) {
                    answeredQuestions = answeredQuestions + 3
                    if (isCorrect) {
                        userScore += 100
                        viewModel.showMessage("✓ Correct! +100 Cyber Sentinel XP")
                    }
                }
            }
        )

        if (userScore == 300) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Navy900),
                shape = RoundedCornerShape(16.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(36.dp))
                    Text("CERTIFIED CYBER SENTINEL LEVEL 1", color = EmeraldSuccess, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                    Text("You have mastered real-world fraud identification and phishing mitigation techniques.", color = TextWhite, fontSize = 11.sp, textAlign = androidx.compose.ui.text.style.TextAlign.Center)
                }
            }
        }
    }
}

@Composable
private fun QuizCard(
    questionId: Int,
    scenario: String,
    options: List<String>,
    correctIndex: Int,
    isAnswered: Boolean,
    onAnswer: (Boolean) -> Unit
) {
    var selectedOption by remember { mutableStateOf<Int?>(null) }

    Card(
        modifier = Modifier.fillMaxWidth().testTag("quiz_card_$questionId"),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(scenario, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)

            options.forEachIndexed { index, option ->
                val isSelected = selectedOption == index
                val isCorrect = index == correctIndex

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(enabled = !isAnswered) {
                            selectedOption = index
                            onAnswer(isCorrect)
                        },
                    color = when {
                        isAnswered && isCorrect -> EmeraldSuccess.copy(alpha = 0.2f)
                        isAnswered && isSelected && !isCorrect -> CrimsonDanger.copy(alpha = 0.2f)
                        else -> Navy900
                    },
                    shape = RoundedCornerShape(10.dp),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        when {
                            isAnswered && isCorrect -> EmeraldSuccess
                            isAnswered && isSelected && !isCorrect -> CrimsonDanger
                            else -> Navy700
                        }
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = option,
                            color = when {
                                isAnswered && isCorrect -> EmeraldSuccess
                                isAnswered && isSelected && !isCorrect -> CrimsonDanger
                                else -> TextWhite
                            },
                            fontSize = 11.sp,
                            modifier = Modifier.weight(1f)
                        )
                        if (isAnswered && isCorrect) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldSuccess, modifier = Modifier.size(16.dp))
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 2: WEALTH & COMPOUND INTEREST TOOL
// -------------------------------------------------------------
@Composable
private fun WealthAndCompoundModule(viewModel: BankViewModel) {
    var initialDeposit by remember { mutableDoubleStateOf(5000.0) }
    var monthlyContribution by remember { mutableDoubleStateOf(500.0) }
    var annualInterestRate by remember { mutableDoubleStateOf(8.5) }
    var yearsToGrow by remember { mutableIntStateOf(10) }

    // Compound Interest Calculation: A = P(1 + r/n)^(nt) + PMT * [((1 + r/n)^(nt) - 1) / (r/n)]
    val r = annualInterestRate / 100.0 / 12.0
    val months = yearsToGrow * 12
    val principalGrowth = initialDeposit * (1 + r).pow(months.toDouble())
    val contributionsGrowth = if (r > 0) monthlyContribution * (((1 + r).pow(months.toDouble()) - 1) / r) else (monthlyContribution * months)
    val totalFutureValue = principalGrowth + contributionsGrowth
    val totalInvested = initialDeposit + (monthlyContribution * months)
    val totalInterestEarned = totalFutureValue - totalInvested

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.4f))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.TrendingUp, contentDescription = null, tint = CyberCyan)
                    Text("Compound Wealth Accelerator", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                }

                // Initial Deposit Slider
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Initial Investment", color = TextMuted, fontSize = 11.sp)
                        Text("$${"%,.0f".format(initialDeposit)}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Slider(
                        value = initialDeposit.toFloat(),
                        onValueChange = { initialDeposit = it.toDouble() },
                        valueRange = 500f..50000f,
                        steps = 99,
                        colors = SliderDefaults.colors(thumbColor = CyberCyan, activeTrackColor = CyberCyan, inactiveTrackColor = Navy700)
                    )
                }

                // Monthly Contribution
                Column {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text("Monthly Contribution", color = TextMuted, fontSize = 11.sp)
                        Text("$${"%,.0f".format(monthlyContribution)}/mo", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                    }
                    Slider(
                        value = monthlyContribution.toFloat(),
                        onValueChange = { monthlyContribution = it.toDouble() },
                        valueRange = 50f..3000f,
                        steps = 59,
                        colors = SliderDefaults.colors(thumbColor = EmeraldSuccess, activeTrackColor = EmeraldSuccess, inactiveTrackColor = Navy700)
                    )
                }

                // Rate & Years
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Annual Return: ${"%.1f".format(annualInterestRate)}%", color = TextMuted, fontSize = 11.sp)
                        Slider(
                            value = annualInterestRate.toFloat(),
                            onValueChange = { annualInterestRate = it.toDouble() },
                            valueRange = 2f..15f,
                            steps = 26,
                            colors = SliderDefaults.colors(thumbColor = GoldAccent, activeTrackColor = GoldAccent, inactiveTrackColor = Navy700)
                        )
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Time Horizon: $yearsToGrow Years", color = TextMuted, fontSize = 11.sp)
                        Slider(
                            value = yearsToGrow.toFloat(),
                            onValueChange = { yearsToGrow = it.toInt() },
                            valueRange = 1f..30f,
                            steps = 29,
                            colors = SliderDefaults.colors(thumbColor = PurpleTech, activeTrackColor = PurpleTech, inactiveTrackColor = Navy700)
                        )
                    }
                }

                // Result Box
                Surface(
                    color = Navy900,
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Projected Portfolio Value", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Text("$${"%,.2f".format(totalFutureValue)}", color = EmeraldSuccess, fontWeight = FontWeight.ExtraBold, fontSize = 16.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Capital Invested:", color = TextMuted, fontSize = 11.sp)
                            Text("$${"%,.2f".format(totalInvested)}", color = TextWhite, fontSize = 11.sp)
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Compound Interest Profit:", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 11.sp)
                            Text("+$${"%,.2f".format(totalInterestEarned)}", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// TAB 3: GLOBAL INCLUSION & ESG GREEN BANKING
// -------------------------------------------------------------
@Composable
private fun GlobalInclusionAndEsgModule(
    selectedLanguage: String,
    onLanguageSelect: (String) -> Unit,
    isAccessibleSeniorMode: Boolean,
    onToggleSeniorMode: () -> Unit,
    viewModel: BankViewModel
) {
    val languages = listOf(
        "English (US)", "Español (LatAm)", "हिन्दी (Hindi)", "Français (EU)",
        "العربية (Arabic)", "Mandarin (中文)", "Deutsch (German)", "Português (BR)", "日本語 (Japanese)"
    )

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Multi-Language Switcher Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Icon(Icons.Default.Language, contentDescription = null, tint = CyberCyan)
                    Text("Global Multilingual Inclusion", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
                Text("Select your primary language for 24/7 localized AI banking & support:", color = TextMuted, fontSize = 11.sp)

                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(languages) { lang ->
                        val isSelected = selectedLanguage == lang
                        Surface(
                            modifier = Modifier.clickable { onLanguageSelect(lang) },
                            color = if (isSelected) CyberCyan.copy(alpha = 0.2f) else Navy900,
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, if (isSelected) CyberCyan else Navy700)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                if (isSelected) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(14.dp))
                                }
                                Text(
                                    text = lang,
                                    color = if (isSelected) CyberCyan else TextWhite,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }

        // Senior & Accessible High-Contrast Mode Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (isAccessibleSeniorMode) GoldAccent else Navy700)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier.size(40.dp).clip(CircleShape).background(if (isAccessibleSeniorMode) GoldAccent.copy(alpha = 0.2f) else Navy900),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.AccessibilityNew, contentDescription = null, tint = if (isAccessibleSeniorMode) GoldAccent else CyberCyan)
                    }
                    Column {
                        Text("Senior & High-Contrast Mode", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text("Enlarged touch targets, high-visibility contrast & simplified UI", color = TextMuted, fontSize = 10.sp)
                    }
                }

                Button(
                    onClick = onToggleSeniorMode,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAccessibleSeniorMode) GoldAccent else Navy700
                    ),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(
                        text = if (isAccessibleSeniorMode) "ACTIVE" else "ENABLE",
                        color = if (isAccessibleSeniorMode) Navy900 else TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // ESG Sustainable Green Banking Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(16.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, EmeraldSuccess.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Icon(Icons.Default.Eco, contentDescription = null, tint = EmeraldSuccess)
                        Text("ESG Sustainable Green Banking", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    }
                    Surface(color = EmeraldSuccess.copy(alpha = 0.2f), shape = RoundedCornerShape(6.dp)) {
                        Text("Net-Zero Partner", color = EmeraldSuccess, fontSize = 10.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp))
                    }
                }

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Surface(color = Navy900, shape = RoundedCornerShape(10.dp), modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("18.4 kg", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("CO2 Offset This Month", color = TextMuted, fontSize = 10.sp)
                        }
                    }
                    Surface(color = Navy900, shape = RoundedCornerShape(10.dp), modifier = Modifier.weight(1f)) {
                        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("12 Trees", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("Planted via Round-Ups", color = TextMuted, fontSize = 10.sp)
                        }
                    }
                }
            }
        }
    }
}
