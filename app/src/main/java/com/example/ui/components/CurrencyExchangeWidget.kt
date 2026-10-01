package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.example.network.CurrencyExchangeClient
import com.example.ui.theme.*
import com.example.ui.viewmodel.BankViewModel

/**
 * Live Foreign Exchange Rates and Currency Conversion Widget.
 * Fetches real-time rates via Retrofit and converts transaction amounts
 * from foreign currencies into base account currency (USD) and vice-versa.
 */
@Composable
fun CurrencyExchangeWidget(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()
    val exchangeRates by viewModel.exchangeRates.collectAsStateWithLifecycle()
    val isFetching by viewModel.isFetchingRates.collectAsStateWithLifecycle()
    val lastUpdate by viewModel.ratesLastUpdated.collectAsStateWithLifecycle()
    val recentTransactions by viewModel.transactions.collectAsStateWithLifecycle()

    var foreignAmountInput by remember { mutableStateOf("100") }
    var selectedForeignCurrency by remember { mutableStateOf("EUR") }
    var isForeignToBase by remember { mutableStateOf(true) } // true: Foreign -> Base ($ USD), false: Base -> Foreign

    val availableCurrencies = listOf(
        "EUR" to "Euro (€)",
        "GBP" to "British Pound (£)",
        "JPY" to "Japanese Yen (¥)",
        "CAD" to "Canadian Dollar ($)",
        "AUD" to "Australian Dollar ($)",
        "INR" to "Indian Rupee (₹)",
        "CHF" to "Swiss Franc (Fr)",
        "CNY" to "Chinese Yuan (¥)",
        "SGD" to "Singapore Dollar ($)",
        "AED" to "UAE Dirham (د.إ)",
        "BRL" to "Brazilian Real (R$)"
    )

    val inputAmount = foreignAmountInput.toDoubleOrNull() ?: 0.0

    val convertedAmount = remember(inputAmount, selectedForeignCurrency, isForeignToBase, exchangeRates) {
        if (isForeignToBase) {
            viewModel.convertCurrency(inputAmount, selectedForeignCurrency, "USD")
        } else {
            viewModel.convertCurrency(inputAmount, "USD", selectedForeignCurrency)
        }
    }

    val currentRateQuotation = remember(selectedForeignCurrency, exchangeRates) {
        CurrencyExchangeClient.getRateQuotation(selectedForeignCurrency, "USD")
    }

    val cardBg = if (isDarkMode) NavyCard else MaterialTheme.colorScheme.surface
    val surfaceVariant = if (isDarkMode) Navy900 else MaterialTheme.colorScheme.surfaceVariant
    val textPrimary = if (isDarkMode) TextWhite else MaterialTheme.colorScheme.onSurface
    val textSecondary = if (isDarkMode) TextMuted else MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("currency_exchange_widget"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header with Retrofit Live Badge & Refresh
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
                            .background(ElectricBlue.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CurrencyExchange,
                            contentDescription = null,
                            tint = ElectricBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Live Foreign Exchange",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = textPrimary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = EmeraldSuccess.copy(alpha = 0.15f)
                            ) {
                                Text(
                                    text = "Retrofit API",
                                    color = EmeraldSuccess,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                )
                            }
                        }
                        Text(
                            text = "Base: USD • $currentRateQuotation",
                            style = MaterialTheme.typography.bodySmall,
                            color = textSecondary,
                            fontSize = 11.sp
                        )
                    }
                }

                IconButton(
                    onClick = { viewModel.fetchLiveExchangeRates("USD") },
                    modifier = Modifier
                        .size(36.dp)
                        .testTag("btn_refresh_exchange_rates")
                ) {
                    if (isFetching) {
                        CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp, color = ElectricBlue)
                    } else {
                        Icon(
                            imageVector = Icons.Default.Sync,
                            contentDescription = "Refresh Live Rates",
                            tint = ElectricBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Currency Selector Chips
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                availableCurrencies.forEach { (code, label) ->
                    val isSelected = code == selectedForeignCurrency
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedForeignCurrency = code },
                        label = {
                            Text(
                                text = code,
                                fontSize = 12.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = ElectricBlue,
                            selectedLabelColor = Color.White
                        ),
                        modifier = Modifier.testTag("currency_chip_$code")
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Conversion Box
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    // Amount Input
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = foreignAmountInput,
                            onValueChange = { foreignAmountInput = it },
                            label = {
                                Text(
                                    if (isForeignToBase) "Amount in $selectedForeignCurrency" else "Amount in USD ($)"
                                )
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .weight(1f)
                                .testTag("input_foreign_amount")
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        // Swap Direction Button
                        IconButton(
                            onClick = { isForeignToBase = !isForeignToBase },
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(ElectricBlue.copy(alpha = 0.15f))
                                .testTag("btn_swap_currency_direction")
                        ) {
                            Icon(
                                imageVector = Icons.Default.SwapHoriz,
                                contentDescription = "Swap conversion direction",
                                tint = ElectricBlue
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Converted Output Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = if (isForeignToBase) "Converted to Base (USD)" else "Converted to $selectedForeignCurrency",
                                fontSize = 11.sp,
                                color = textSecondary
                            )
                            Text(
                                text = if (isForeignToBase) {
                                    "$${"%,.2f".format(convertedAmount)} USD"
                                } else {
                                    "${"%,.2f".format(convertedAmount)} $selectedForeignCurrency"
                                },
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldSuccess
                            )
                        }

                        // Presets
                        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            listOf("50", "200", "500").forEach { preset ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = if (isDarkMode) Navy800 else Color.White,
                                    modifier = Modifier
                                        .clickable { foreignAmountInput = preset }
                                        .padding(horizontal = 6.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = preset,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = textPrimary,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Quick Convert Recent Transaction
            if (recentTransactions.isNotEmpty()) {
                Spacer(modifier = Modifier.height(10.dp))
                val firstTx = recentTransactions.first()
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .clickable {
                            foreignAmountInput = "%.2f".format(firstTx.amount)
                            isForeignToBase = false
                        }
                        .padding(vertical = 4.dp, horizontal = 2.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Convert latest tx: ${firstTx.title} ($${"%.2f".format(firstTx.amount)})",
                        fontSize = 11.sp,
                        color = ElectricBlue,
                        fontWeight = FontWeight.Medium
                    )
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = null,
                        tint = ElectricBlue,
                        modifier = Modifier.size(14.dp)
                    )
                }
            }
        }
    }
}
