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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.TransactionCategory
import com.example.data.model.TransactionEntity
import com.example.data.model.TransactionStatus
import com.example.data.model.TransactionType
import com.example.ui.theme.*
import com.example.ui.viewmodel.BankViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class TransactionPageType(val title: String, val icon: ImageVector, val code: String) {
    HISTORY("History", Icons.Default.Receipt, "HIST"),
    DETAILS("Details", Icons.Default.Description, "DET"),
    ANALYTICS("Analytics", Icons.Default.TrendingUp, "ANALYSIS"),
    SEARCH("Search & Filter", Icons.Default.Search, "SEARCH"),
    EXPORT_PDF("Export PDF", Icons.Default.Description, "PDF"),
    EXPORT_CSV("Export CSV", Icons.Default.Send, "CSV"),
    CATEGORIES("Categories", Icons.Default.Star, "CAT"),
    SPENDING_TIMELINE("Spending Timeline", Icons.Default.TrendingUp, "TIMELINE")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionModuleHubScreen(
    viewModel: BankViewModel,
    initialPage: TransactionPageType = TransactionPageType.HISTORY,
    onNavigateToPage: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var currentPage by remember { mutableStateOf(initialPage) }

    LaunchedEffect(initialPage) {
        currentPage = initialPage
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Navy900)
    ) {
        // --- 1. Top Bar / Header ---
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Navy800),
            border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(CyberCyan.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = currentPage.icon,
                                contentDescription = null,
                                tint = CyberCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Column {
                            Text(
                                "TRANSACTIONS SUITE",
                                color = CyberCyan,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.sp
                            )
                            Text(
                                currentPage.title,
                                color = TextWhite,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Surface(
                        color = CyberCyan.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.5f))
                    ) {
                        Text(
                            text = currentPage.code,
                            color = CyberCyan,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Black,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // --- 2. Horizontal Scrollable Page Tabs ---
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(TransactionPageType.values()) { page ->
                        val isSelected = currentPage == page
                        FilterChip(
                            selected = isSelected,
                            onClick = { currentPage = page },
                            label = {
                                Text(
                                    page.title,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            leadingIcon = {
                                Icon(
                                    imageVector = page.icon,
                                    contentDescription = null,
                                    modifier = Modifier.size(14.dp),
                                    tint = if (isSelected) Navy900 else CyberCyan
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = CyberCyan,
                                selectedLabelColor = Navy900,
                                containerColor = Navy700,
                                labelColor = TextWhite
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) CyberCyan else Navy700,
                                selectedBorderColor = CyberCyan
                            )
                        )
                    }
                }
            }
        }

        // --- 3. Dynamic Page Content with Smooth Transition Animation ---
        AnimatedContent(
            targetState = currentPage,
            transitionSpec = {
                if (targetState.ordinal > initialState.ordinal) {
                    slideInHorizontally { width -> width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> -width } + fadeOut()
                } else {
                    slideInHorizontally { width -> -width } + fadeIn() togetherWith
                            slideOutHorizontally { width -> width } + fadeOut()
                }.using(SizeTransform(clip = false))
            },
            modifier = Modifier.weight(1f),
            label = "TransactionPageTransition"
        ) { targetPage ->
            when (targetPage) {
                TransactionPageType.HISTORY -> TransactionHistoryScreen(viewModel)
                TransactionPageType.DETAILS -> TransactionDetailsScreen(viewModel)
                TransactionPageType.ANALYTICS -> TransactionAnalyticsScreen(viewModel)
                TransactionPageType.SEARCH -> TransactionSearchScreen(viewModel)
                TransactionPageType.EXPORT_PDF -> TransactionExportPdfScreen(viewModel)
                TransactionPageType.EXPORT_CSV -> TransactionExportCsvScreen(viewModel)
                TransactionPageType.CATEGORIES -> TransactionCategoriesScreen(viewModel)
                TransactionPageType.SPENDING_TIMELINE -> TransactionSpendingTimelineScreen(viewModel)
            }
        }
    }
}

// -------------------------------------------------------------------------
// 1. TRANSACTION HISTORY SCREEN
// -------------------------------------------------------------------------
@Composable
fun TransactionHistoryScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    var selectedFilter by remember { mutableStateOf("ALL") }

    val filteredList = remember(transactions, selectedFilter) {
        when (selectedFilter) {
            "CREDIT" -> transactions.filter { it.type == TransactionType.CREDIT }
            "DEBIT" -> transactions.filter { it.type == TransactionType.DEBIT }
            else -> transactions
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            // Quick Filter Chips
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("ALL", "CREDIT", "DEBIT").forEach { filter ->
                    val isSel = selectedFilter == filter
                    Button(
                        onClick = { selectedFilter = filter },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSel) ElectricBlue else Navy800,
                            contentColor = if (isSel) TextWhite else TextMuted
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.weight(1f).height(38.dp)
                    ) {
                        Text(filter, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        if (filteredList.isEmpty()) {
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = Navy800),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth().padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(Icons.Default.Receipt, contentDescription = null, tint = TextMuted, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("No Transactions Found", color = TextWhite, fontWeight = FontWeight.Bold)
                        Text("Transactions matching this filter will appear here.", color = TextMuted, fontSize = 12.sp)
                    }
                }
            }
        } else {
            items(filteredList) { tx ->
                Card(
                    colors = CardDefaults.cardColors(containerColor = Navy800),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                            val isCredit = tx.type == TransactionType.CREDIT
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(if (isCredit) EmeraldSuccess.copy(alpha = 0.2f) else CrimsonDanger.copy(alpha = 0.2f)),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    if (isCredit) Icons.Default.ArrowDownward else Icons.Default.ArrowUpward,
                                    contentDescription = null,
                                    tint = if (isCredit) EmeraldSuccess else CrimsonDanger,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(tx.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                Text(
                                    "${tx.category.name} • ${SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault()).format(Date(tx.timestamp))}",
                                    color = TextMuted,
                                    fontSize = 11.sp
                                )
                            }
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            val isCredit = tx.type == TransactionType.CREDIT
                            Text(
                                "${if (isCredit) "+" else "-"}$%,.2f".format(tx.amount),
                                color = if (isCredit) EmeraldSuccess else TextWhite,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 14.sp
                            )
                            Text(tx.status.name, color = CyberCyan, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 2. TRANSACTION DETAILS SCREEN
// -------------------------------------------------------------------------
@Composable
fun TransactionDetailsScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val activeTx = transactions.firstOrNull() ?: TransactionEntity(
        id = 990812L,
        accountId = 1L,
        title = "Global Settlement Rail Wire",
        recipient = "Federal Reserve FedNow Gateway",
        amount = 14500.00,
        type = TransactionType.DEBIT,
        category = TransactionCategory.TRANSFER,
        timestamp = System.currentTimeMillis(),
        status = TransactionStatus.COMPLETED
    )

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.3f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(CyberCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Receipt, contentDescription = null, tint = CyberCyan, modifier = Modifier.size(32.dp))
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(activeTx.title, color = TextWhite, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
                    Text("TXID: #${activeTx.id}", color = CyberCyan, fontSize = 12.sp, fontFamily = FontFamily.Monospace)

                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        "$%,.2f USD".format(activeTx.amount),
                        color = if (activeTx.type == TransactionType.CREDIT) EmeraldSuccess else TextWhite,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = Navy700)
                    Spacer(modifier = Modifier.height(16.dp))

                    DetailItemRow("Status", activeTx.status.name, EmeraldSuccess)
                    DetailItemRow("Category", activeTx.category.name, TextWhite)
                    DetailItemRow("Recipient", activeTx.recipient, CyberCyan)
                    DetailItemRow("Rail / Protocol", "FedNow / ISO 20022", CyberCyan)
                    DetailItemRow("Settlement Latency", "142 ms", GoldAccent)
                    DetailItemRow("SHA-256 Hash", "8f4a2...c91e", TextMuted)
                }
            }
        }

        item {
            Button(
                onClick = { viewModel.showMessage("Receipt cryptographically signed and downloaded.") },
                colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = Navy900),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(46.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Download Verified Proof Receipt", fontWeight = FontWeight.Bold)
            }
        }
    }
}

// -------------------------------------------------------------------------
// 3. TRANSACTION ANALYTICS SCREEN
// -------------------------------------------------------------------------
@Composable
fun TransactionAnalyticsScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val totalSpent = remember(transactions) {
        transactions.filter { it.type == TransactionType.DEBIT }.sumOf { it.amount }
    }
    val totalReceived = remember(transactions) {
        transactions.filter { it.type == TransactionType.CREDIT }.sumOf { it.amount }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MetricCard("Total Inflow", "$%,.2f".format(totalReceived), EmeraldSuccess, Modifier.weight(1f))
                MetricCard("Total Outflow", "$%,.2f".format(totalSpent), CrimsonDanger, Modifier.weight(1f))
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Outflow Distribution by Sector", color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(12.dp))

                    SectorProgressRow("Treasury & Investments", 0.45f, CyberCyan, "$45,000.00")
                    SectorProgressRow("Operational Expenses", 0.25f, ElectricBlue, "$25,000.00")
                    SectorProgressRow("Vendor & Supplier Wires", 0.20f, GoldAccent, "$20,000.00")
                    SectorProgressRow("Cloud & Infrastructure", 0.10f, PurpleTech, "$10,000.00")
                }
            }
        }

        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("AI Predictive Outflow Forecast", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        "Based on recurring standing orders and ISO 20022 velocity, your forecasted net outflow over the next 30 days is estimated at $12,450.00 USD (98.4% confidence score).",
                        color = TextMuted,
                        fontSize = 12.sp,
                        lineHeight = 18.sp
                    )
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 4. TRANSACTION SEARCH & FILTER SCREEN
// -------------------------------------------------------------------------
@Composable
fun TransactionSearchScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }
    var minAmount by remember { mutableStateOf("") }
    var maxAmount by remember { mutableStateOf("") }

    val results = remember(transactions, query, minAmount, maxAmount) {
        transactions.filter { tx ->
            val matchesQuery = query.isBlank() || tx.title.contains(query, ignoreCase = true) || tx.category.name.contains(query, ignoreCase = true)
            val min = minAmount.toDoubleOrNull() ?: 0.0
            val max = maxAmount.toDoubleOrNull() ?: Double.MAX_VALUE
            matchesQuery && tx.amount >= min && tx.amount <= max
        }
    }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search by recipient, memo, category...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = CyberCyan) },
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = CyberCyan,
                    unfocusedBorderColor = Navy700,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite
                ),
                shape = RoundedCornerShape(10.dp)
            )
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = minAmount,
                    onValueChange = { minAmount = it },
                    label = { Text("Min $") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, unfocusedBorderColor = Navy700, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    shape = RoundedCornerShape(10.dp)
                )
                OutlinedTextField(
                    value = maxAmount,
                    onValueChange = { maxAmount = it },
                    label = { Text("Max $") },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = CyberCyan, unfocusedBorderColor = Navy700, focusedTextColor = TextWhite, unfocusedTextColor = TextWhite),
                    shape = RoundedCornerShape(10.dp)
                )
            }
        }

        item {
            Text("Search Results (${results.size})", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }

        items(results) { tx ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(tx.title, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(tx.category.name, color = TextMuted, fontSize = 11.sp)
                    }
                    Text("$%,.2f".format(tx.amount), color = EmeraldSuccess, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 5. EXPORT PDF SCREEN
// -------------------------------------------------------------------------
@Composable
fun TransactionExportPdfScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var includeHashes by remember { mutableStateOf(true) }
    var selectedRange by remember { mutableStateOf("Last 90 Days") }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Export Statement to Certified PDF", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Generates a digitally stamped PDF with embedded SHA-256 cryptographic audit logs suitable for tax authorities and audits.", color = TextMuted, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(16.dp))

                    Text("Time Period", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Last 30 Days", "Last 90 Days", "Fiscal YTD").forEach { range ->
                            FilterChip(
                                selected = selectedRange == range,
                                onClick = { selectedRange = range },
                                label = { Text(range, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = CyberCyan,
                                    selectedLabelColor = Navy900,
                                    containerColor = Navy700,
                                    labelColor = TextWhite
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Include ZKP & Block Audit Hashes", color = TextWhite, fontSize = 12.sp)
                        Switch(
                            checked = includeHashes,
                            onCheckedChange = { includeHashes = it },
                            colors = SwitchDefaults.colors(checkedThumbColor = CyberCyan, checkedTrackColor = ElectricBlue)
                        )
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = { viewModel.showMessage("✓ Certified PDF Statement generated successfully ($selectedRange).") },
                        colors = ButtonDefaults.buttonColors(containerColor = CyberCyan, contentColor = Navy900),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(46.dp)
                    ) {
                        Icon(Icons.Default.Description, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Generate & Download PDF", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 6. EXPORT CSV SCREEN
// -------------------------------------------------------------------------
@Composable
fun TransactionExportCsvScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    var delimiter by remember { mutableStateOf("Comma (,)") }

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Text("Export Raw Ledger to CSV / TSV", color = EmeraldSuccess, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Export high-velocity transaction batches compatible with Excel, QuickBooks, SAP, and custom data pipelines.", color = TextMuted, fontSize = 12.sp)

                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Delimiter Format", color = TextWhite, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        listOf("Comma (,)", "Semicolon (;)", "Tab (\\t)").forEach { d ->
                            FilterChip(
                                selected = delimiter == d,
                                onClick = { delimiter = d },
                                label = { Text(d, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldSuccess,
                                    selectedLabelColor = Navy900,
                                    containerColor = Navy700,
                                    labelColor = TextWhite
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))
                    Button(
                        onClick = { viewModel.showMessage("✓ Ledger CSV exported with $delimiter delimiter.") },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess, contentColor = Navy900),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.fillMaxWidth().height(46.dp)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Export CSV Dataset", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 7. TRANSACTION CATEGORIES SCREEN
// -------------------------------------------------------------------------
@Composable
fun TransactionCategoriesScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val categories = listOf(
        Triple("Treasury & Yield", "$84,200.00", CyberCyan),
        Triple("Operational Expenses", "$32,150.00", ElectricBlue),
        Triple("Direct Cross-Border FX", "$28,400.00", PurpleTech),
        Triple("Vendor Invoices", "$14,800.00", GoldAccent),
        Triple("Cloud Hosting & SaaS", "$6,500.00", AmberOrange),
        Triple("Payroll & Benefits", "$48,900.00", EmeraldSuccess)
    )

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        item {
            Text("Category Ledger Breakdown", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
        items(categories) { (cat, amount, color) ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(10.dp).clip(CircleShape).background(color))
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(cat, color = TextWhite, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    }
                    Text(amount, color = color, fontWeight = FontWeight.ExtraBold, fontSize = 13.sp)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// 8. SPENDING TIMELINE SCREEN
// -------------------------------------------------------------------------
@Composable
fun TransactionSpendingTimelineScreen(viewModel: BankViewModel, modifier: Modifier = Modifier) {
    val milestones = listOf(
        Triple("Today 14:30", "Automated Yield Sweep to Treasury", "$2,450.00"),
        Triple("Yesterday 09:15", "SWIFT Wire Settlement (London Branch)", "$18,500.00"),
        Triple("Aug 18, 2026", "Cloud Computing Cluster Monthly Billing", "$3,200.00"),
        Triple("Aug 15, 2026", "Executive Payroll Direct Deposit", "$42,000.00"),
        Triple("Aug 10, 2026", "Smart Contract Staking Inflow", "$8,900.00")
    )

    LazyColumn(
        modifier = modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text("Chronological Spending Timeline", color = CyberCyan, fontWeight = FontWeight.Bold, fontSize = 15.sp)
        }
        items(milestones) { (date, event, amount) ->
            Card(
                colors = CardDefaults.cardColors(containerColor = Navy800),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(date, color = CyberCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(event, color = TextWhite, fontWeight = FontWeight.Medium, fontSize = 13.sp)
                    }
                    Text(amount, color = GoldAccent, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                }
            }
        }
    }
}

// -------------------------------------------------------------------------
// REUSABLE HELPER COMPOSABLES
// -------------------------------------------------------------------------
@Composable
fun MetricCard(title: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Navy800),
        shape = RoundedCornerShape(12.dp),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Text(title, color = TextMuted, fontSize = 11.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(value, color = color, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
fun SectorProgressRow(label: String, progress: Float, color: Color, amount: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, color = TextWhite, fontSize = 12.sp)
            Text(amount, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.height(4.dp))
        LinearProgressIndicator(
            progress = { progress },
            color = color,
            trackColor = Navy700,
            modifier = Modifier.fillMaxWidth().height(6.dp).clip(RoundedCornerShape(3.dp))
        )
    }
}

@Composable
fun DetailItemRow(label: String, value: String, valueColor: Color = TextWhite) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = TextMuted, fontSize = 12.sp)
        Text(value, color = valueColor, fontSize = 12.sp, fontWeight = FontWeight.Bold)
    }
}
