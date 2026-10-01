package com.example.ui.components

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.theme.*
import com.example.ui.viewmodel.BankViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Speech-to-Text Voice Query Widget for the Dashboard.
 * Allows users to tap the microphone, speak natural queries like
 * "How much did I spend on groceries this month?", and inspect real-time Room database results.
 */
@Composable
fun VoiceSpendingQueryWidget(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val voiceResult by viewModel.voiceQueryResult.collectAsStateWithLifecycle()
    val isProcessing by viewModel.isVoiceProcessing.collectAsStateWithLifecycle()

    var textQueryInput by remember { mutableStateOf("") }
    var isExpandedDetails by remember { mutableStateOf(false) }

    // Speech-to-Text Activity Launcher
    val speechRecognitionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val spokenMatches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = spokenMatches?.firstOrNull() ?: ""
            if (spokenText.isNotBlank()) {
                textQueryInput = spokenText
                viewModel.executeVoiceQuery(spokenText)
            }
        }
    }

    // Permission launcher for microphone
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            try {
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                    putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                    putExtra(RecognizerIntent.EXTRA_PROMPT, "Ask SmartBank AI (e.g. 'How much did I spend on groceries this month?')")
                }
                speechRecognitionLauncher.launch(intent)
            } catch (e: Exception) {
                Toast.makeText(context, "Speech recognition not available on device", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, "Microphone permission required for voice input", Toast.LENGTH_SHORT).show()
        }
    }

    fun startSpeechRecognition() {
        permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
    }

    val cardBg = if (isDarkMode) NavyCard else MaterialTheme.colorScheme.surface
    val surfaceVariant = if (isDarkMode) Navy900 else MaterialTheme.colorScheme.surfaceVariant
    val textPrimary = if (isDarkMode) TextWhite else MaterialTheme.colorScheme.onSurface
    val textSecondary = if (isDarkMode) TextMuted else MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("voice_spending_query_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(38.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(ElectricBlue, NeonCyan)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = "Voice Query",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Voice Financial Intelligence",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = textPrimary
                        )
                        Text(
                            text = "Ask Room database in natural language",
                            style = MaterialTheme.typography.bodySmall,
                            color = textSecondary
                        )
                    }
                }

                if (voiceResult != null) {
                    IconButton(
                        onClick = { viewModel.clearVoiceQueryResult() },
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Clear result",
                            tint = textSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Query Input & Mic Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = textQueryInput,
                    onValueChange = { textQueryInput = it },
                    placeholder = {
                        Text(
                            "Tap mic or ask e.g. 'Groceries this month'",
                            fontSize = 13.sp,
                            color = textSecondary
                        )
                    },
                    trailingIcon = {
                        if (textQueryInput.isNotBlank()) {
                            IconButton(
                                onClick = {
                                    viewModel.executeVoiceQuery(textQueryInput)
                                },
                                modifier = Modifier.testTag("voice_query_submit_btn")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = ElectricBlue
                                )
                            }
                        }
                    },
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(onSearch = {
                        if (textQueryInput.isNotBlank()) {
                            viewModel.executeVoiceQuery(textQueryInput)
                        }
                    }),
                    singleLine = true,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("voice_query_input_field")
                )

                Spacer(modifier = Modifier.width(10.dp))

                // Microphone Trigger Button
                FilledIconButton(
                    onClick = { startSpeechRecognition() },
                    colors = IconButtonDefaults.filledIconButtonColors(containerColor = ElectricBlue),
                    modifier = Modifier
                        .size(52.dp)
                        .testTag("voice_query_mic_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = "Tap to speak financial question",
                        tint = Color.White,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Suggestion Chips (Horizontal Scroll)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                val sampleQueries = listOf(
                    "Groceries this month",
                    "Shopping spending",
                    "Bills paid this month",
                    "Entertainment spending",
                    "What is my balance?"
                )

                sampleQueries.forEach { sample ->
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = surfaceVariant,
                        modifier = Modifier
                            .clickable {
                                textQueryInput = sample
                                viewModel.executeVoiceQuery(sample)
                            }
                            .testTag("chip_voice_${sample.replace(" ", "_")}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.RecordVoiceOver,
                                contentDescription = null,
                                tint = ElectricBlue,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = sample,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = textPrimary
                            )
                        }
                    }
                }
            }

            // Results Section
            AnimatedVisibility(
                visible = voiceResult != null,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                voiceResult?.let { result ->
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("voice_query_result_box"),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = surfaceVariant
                            )
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                // Spoken query badge
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FormatQuote,
                                        contentDescription = null,
                                        tint = NeonCyan,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "\"${result.query}\"",
                                        fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                                        fontSize = 12.sp,
                                        color = textSecondary
                                    )
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                // Main Answer Text
                                Text(
                                    text = result.spokenAnswer,
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.SemiBold,
                                    color = textPrimary
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                // Key metrics row
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(
                                            text = "Total Evaluated",
                                            fontSize = 11.sp,
                                            color = textSecondary
                                        )
                                        Text(
                                            text = "$${"%,.2f".format(result.totalAmount)}",
                                            fontSize = 20.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (result.intentType == com.example.domain.engine.VoiceQueryIntentType.TOTAL_INCOME) EmeraldSuccess else ElectricBlue
                                        )
                                    }

                                    if (result.budgetLimit != null && result.budgetLimit > 0) {
                                        Column(horizontalAlignment = Alignment.End) {
                                            Text(
                                                text = "Monthly Limit",
                                                fontSize = 11.sp,
                                                color = textSecondary
                                            )
                                            val pct = result.budgetPercentage ?: 0f
                                            val badgeColor = when {
                                                pct >= 100f -> CrimsonError
                                                pct >= 80f -> AmberWarning
                                                else -> EmeraldSuccess
                                            }
                                            Text(
                                                text = "${pct.toInt()}% of $${"%,.0f".format(result.budgetLimit)}",
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = badgeColor
                                            )
                                        }
                                    }
                                }

                                // Expandable list of matching transactions
                                if (result.matchingTransactions.isNotEmpty()) {
                                    Spacer(modifier = Modifier.height(10.dp))
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(8.dp))
                                            .clickable { isExpandedDetails = !isExpandedDetails }
                                            .padding(vertical = 4.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "${result.matchingTransactions.size} Matching Records from Room",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = ElectricBlue
                                        )
                                        Icon(
                                            imageVector = if (isExpandedDetails) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                            contentDescription = null,
                                            tint = ElectricBlue,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }

                                    AnimatedVisibility(visible = isExpandedDetails) {
                                        val dateFormat = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                                        Column(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(top = 8.dp),
                                            verticalArrangement = Arrangement.spacedBy(6.dp)
                                        ) {
                                            result.matchingTransactions.take(6).forEach { tx ->
                                                Row(
                                                    modifier = Modifier
                                                        .fillMaxWidth()
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(if (isDarkMode) Navy800 else Color.White)
                                                        .padding(horizontal = 10.dp, vertical = 6.dp),
                                                    horizontalArrangement = Arrangement.SpaceBetween,
                                                    verticalAlignment = Alignment.CenterVertically
                                                ) {
                                                    Column(modifier = Modifier.weight(1f)) {
                                                        Text(
                                                            text = tx.title,
                                                            fontSize = 12.sp,
                                                            fontWeight = FontWeight.SemiBold,
                                                            color = textPrimary,
                                                            maxLines = 1
                                                        )
                                                        Text(
                                                            text = dateFormat.format(Date(tx.timestamp)),
                                                            fontSize = 10.sp,
                                                            color = textSecondary
                                                        )
                                                    }
                                                    Text(
                                                        text = "$${"%,.2f".format(tx.amount)}",
                                                        fontSize = 12.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        color = if (tx.type == com.example.data.model.TransactionType.CREDIT) EmeraldSuccess else CrimsonError
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
