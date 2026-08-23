package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.MonetizationOn
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.AssetType
import com.example.data.model.InvestmentEntity
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
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BankViewModel

@Composable
fun InvestmentsScreen(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val investments by viewModel.investments.collectAsStateWithLifecycle()
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val investmentAccount = accounts.find { it.type == com.example.data.model.AccountType.INVESTMENT } ?: accounts.firstOrNull()

    val totalPortfolioValue = investments.sumOf { it.sharesOrUnits * it.currentPrice }
    val totalInvestedCost = investments.sumOf { it.sharesOrUnits * it.avgBuyPrice }
    val totalProfitLoss = totalPortfolioValue - totalInvestedCost
    val totalReturnPercent = if (totalInvestedCost > 0) (totalProfitLoss / totalInvestedCost) * 100 else 0.0

    var selectedTradeAsset by remember { mutableStateOf<InvestmentEntity?>(null) }
    var isTradeBuyMode by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(top = 12.dp, bottom = 80.dp)
    ) {
        // Top Header
        item {
            Text(
                text = "WEALTH & ASSET MANAGEMENT",
                color = CyberCyan,
                fontSize = 11.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 1.5.sp
            )
            Text(
                text = "Investment Portfolio Vault",
                color = TextWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Portfolio Valuation Card
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("portfolio_value_card"),
                colors = CardDefaults.cardColors(containerColor = NavyCard),
                shape = RoundedCornerShape(20.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f))
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("TOTAL ASSETS VALUATION", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Surface(
                            color = if (totalProfitLoss >= 0) EmeraldSuccess.copy(alpha = 0.2f) else CrimsonDanger.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "${if (totalProfitLoss >= 0) "+" else ""}${"%.2f".format(totalReturnPercent)}% ALL TIME",
                                color = if (totalProfitLoss >= 0) EmeraldSuccess else CrimsonDanger,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = "$${"%,.2f".format(totalPortfolioValue)}",
                        color = TextWhite,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 32.sp
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(10.dp))
                            .background(Navy900)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total Invested", color = TextMuted, fontSize = 11.sp)
                            Text("$${"%,.2f".format(totalInvestedCost)}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(Navy700))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Total Net P&L", color = TextMuted, fontSize = 11.sp)
                            Text(
                                text = "${if (totalProfitLoss >= 0) "+" else ""}$${"%,.2f".format(totalProfitLoss)}",
                                color = if (totalProfitLoss >= 0) EmeraldSuccess else CrimsonDanger,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )
                        }
                        Box(modifier = Modifier.width(1.dp).height(24.dp).background(Navy700))
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Unallocated Cash", color = TextMuted, fontSize = 11.sp)
                            Text("$${"%,.2f".format(investmentAccount?.balance ?: 0.0)}", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // Section Title: Holdings
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Your Active Holdings (${investments.size})", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Real-Time Execution", color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            }
        }

        // Holdings List
        items(investments) { asset ->
            AssetHoldingItem(
                asset = asset,
                onTrade = { isBuy ->
                    selectedTradeAsset = asset
                    isTradeBuyMode = isBuy
                }
            )
        }
    }

    // Trade Dialog
    selectedTradeAsset?.let { asset ->
        TradeExecutionDialog(
            asset = asset,
            isBuyMode = isTradeBuyMode,
            cashBalance = investmentAccount?.balance ?: 0.0,
            onDismiss = { selectedTradeAsset = null },
            onConfirmTrade = { shares, price ->
                if (isTradeBuyMode) {
                    viewModel.buyAsset(asset.symbol, shares, price)
                } else {
                    viewModel.sellAsset(asset.symbol, shares, price)
                }
                selectedTradeAsset = null
            }
        )
    }
}

@Composable
private fun AssetHoldingItem(
    asset: InvestmentEntity,
    onTrade: (isBuy: Boolean) -> Unit
) {
    val totalValue = asset.sharesOrUnits * asset.currentPrice
    val totalGain = totalValue - (asset.sharesOrUnits * asset.avgBuyPrice)
    val gainPercent = if (asset.avgBuyPrice > 0) ((asset.currentPrice - asset.avgBuyPrice) / asset.avgBuyPrice) * 100 else 0.0

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, Navy700)
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(
                                when (asset.assetType) {
                                    AssetType.CRYPTO -> AmberOrange.copy(alpha = 0.2f)
                                    AssetType.GOLD -> GoldAccent.copy(alpha = 0.2f)
                                    AssetType.ETF -> ElectricBlue.copy(alpha = 0.2f)
                                    else -> CyberCyan.copy(alpha = 0.2f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (asset.assetType) {
                                AssetType.CRYPTO -> Icons.Default.CurrencyBitcoin
                                AssetType.GOLD -> Icons.Default.MonetizationOn
                                else -> Icons.Default.TrendingUp
                            },
                            contentDescription = null,
                            tint = when (asset.assetType) {
                                AssetType.CRYPTO -> AmberOrange
                                AssetType.GOLD -> GoldAccent
                                AssetType.ETF -> ElectricBlue
                                else -> CyberCyan
                            },
                            modifier = Modifier.size(22.dp)
                        )
                    }
                    Column {
                        Text(asset.name, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("${asset.symbol} • ${asset.sharesOrUnits} units", color = TextMuted, fontSize = 11.sp)
                    }
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text("$${"%,.2f".format(totalValue)}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Text(
                        text = "${if (gainPercent >= 0) "+" else ""}${"%.2f".format(gainPercent)}% ($${"%.2f".format(totalGain)})",
                        color = if (gainPercent >= 0) EmeraldSuccess else CrimsonDanger,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = { onTrade(true) },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(36.dp)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, tint = Navy900, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Buy", color = Navy900, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }

                Button(
                    onClick = { onTrade(false) },
                    colors = ButtonDefaults.buttonColors(containerColor = CrimsonDanger),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.weight(1f).height(36.dp)
                ) {
                    Icon(Icons.Default.Remove, contentDescription = null, tint = TextWhite, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Sell", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
private fun TradeExecutionDialog(
    asset: InvestmentEntity,
    isBuyMode: Boolean,
    cashBalance: Double,
    onDismiss: () -> Unit,
    onConfirmTrade: (shares: Double, price: Double) -> Unit
) {
    var sharesText by remember { mutableStateOf("1.0") }
    val shares = sharesText.toDoubleOrNull() ?: 0.0
    val totalCost = shares * asset.currentPrice

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier.fillMaxWidth(0.95f),
            colors = CardDefaults.cardColors(containerColor = NavyCard),
            shape = RoundedCornerShape(20.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (isBuyMode) EmeraldSuccess else CrimsonDanger)
        ) {
            Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                Text(
                    text = if (isBuyMode) "Execute Buy Order: ${asset.symbol}" else "Execute Sell Order: ${asset.symbol}",
                    color = TextWhite,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Surface(color = Navy900, shape = RoundedCornerShape(10.dp), modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Current Market Price", color = TextMuted, fontSize = 12.sp)
                        Text("$${"%,.2f".format(asset.currentPrice)}", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                }

                OutlinedTextField(
                    value = sharesText,
                    onValueChange = { sharesText = it },
                    label = { Text("Quantity / Shares") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = CyberCyan,
                        unfocusedBorderColor = Navy700,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )

                Surface(
                    color = Navy900,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text("Total Order Value:", color = TextMuted, fontSize = 12.sp)
                            Text("$${"%,.2f".format(totalCost)}", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        if (isBuyMode) {
                            Text("Available Cash: $${"%,.2f".format(cashBalance)}", color = if (cashBalance >= totalCost) EmeraldSuccess else CrimsonDanger, fontSize = 11.sp)
                        } else {
                            Text("Your Shares: ${asset.sharesOrUnits}", color = if (asset.sharesOrUnits >= shares) EmeraldSuccess else CrimsonDanger, fontSize = 11.sp)
                        }
                    }
                }

                Button(
                    onClick = { onConfirmTrade(shares, asset.currentPrice) },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isBuyMode) EmeraldSuccess else CrimsonDanger
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(46.dp)
                ) {
                    Text(
                        text = if (isBuyMode) "Confirm Buy Market Order" else "Confirm Sell Liquidation",
                        color = if (isBuyMode) Navy900 else TextWhite,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp
                    )
                }
            }
        }
    }
}
