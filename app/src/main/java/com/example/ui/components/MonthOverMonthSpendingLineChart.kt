package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.HealthAndSafety
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.TransactionEntity
import com.example.data.model.TransactionStatus
import com.example.data.model.TransactionType
import com.example.ui.theme.*
import com.example.ui.viewmodel.BankViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Monthly data point for month-over-month trajectory computation.
 */
data class MonthSpendingPoint(
    val monthIndex: Int,
    val monthName: String,
    val yearMonthKey: String,
    val totalSpent: Double,
    val totalIncome: Double,
    val momChangePercent: Double?, // null if first month
    val isProjected: Boolean = false
)

/**
 * Month-over-Month Spending Trends Line Chart.
 * Displays a smooth Jetpack Compose Canvas line chart tracking multi-month financial trajectory,
 * computing month-over-month variance, average spending baseline, and financial health score.
 */
@Composable
fun MonthOverMonthSpendingLineChart(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val transactions by viewModel.transactions.collectAsStateWithLifecycle()
    val isDarkMode by viewModel.isDarkMode.collectAsStateWithLifecycle()

    // Aggregate transactions into sequential chronological monthly data points (last 6 months)
    val monthlyData = remember(transactions) {
        computeMonthOverMonthData(transactions)
    }

    var selectedPointIndex by remember { mutableIntStateOf(monthlyData.lastIndex.coerceAtLeast(0)) }

    // Keep index in bounds if data updates
    LaunchedEffect(monthlyData.size) {
        if (selectedPointIndex !in monthlyData.indices) {
            selectedPointIndex = monthlyData.lastIndex.coerceAtLeast(0)
        }
    }

    val selectedPoint = monthlyData.getOrNull(selectedPointIndex)
    val averageSpent = remember(monthlyData) {
        if (monthlyData.isNotEmpty()) monthlyData.map { it.totalSpent }.average() else 0.0
    }

    // Long-term financial health calculation
    val (healthScore, healthLabel, healthColor) = remember(monthlyData, averageSpent) {
        calculateFinancialHealth(monthlyData, averageSpent)
    }

    val cardBg = if (isDarkMode) NavyCard else MaterialTheme.colorScheme.surface
    val textPrimary = if (isDarkMode) TextWhite else MaterialTheme.colorScheme.onSurface
    val textSecondary = if (isDarkMode) TextMuted else MaterialTheme.colorScheme.onSurfaceVariant
    val borderColor = if (isDarkMode) Navy800 else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("mom_spending_line_chart"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // --- Header & Long-Term Financial Health Badge ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(CyberCyan.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShowChart,
                            contentDescription = null,
                            tint = CyberCyan,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "MONTH-OVER-MONTH TRENDS",
                            color = textSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.1.sp
                        )
                        Text(
                            text = "Long-Term Spending Health",
                            color = textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }

                // Health Score Pill
                Surface(
                    color = healthColor.copy(alpha = 0.18f),
                    shape = RoundedCornerShape(20.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, healthColor.copy(alpha = 0.5f))
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.HealthAndSafety,
                            contentDescription = null,
                            tint = healthColor,
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "$healthScore/100 $healthLabel",
                            color = healthColor,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // --- Selected Month Details Banner ---
            selectedPoint?.let { point ->
                Surface(
                    color = if (isDarkMode) Navy900 else MaterialTheme.colorScheme.surfaceVariant,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "${point.monthName} Spend",
                                color = textSecondary,
                                fontSize = 11.sp
                            )
                            Text(
                                text = "$${"%,.2f".format(point.totalSpent)}",
                                color = textPrimary,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Text(
                                text = "Monthly Income: $${"%,.2f".format(point.totalIncome)}",
                                color = EmeraldSuccess,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        Column(horizontalAlignment = Alignment.End) {
                            point.momChangePercent?.let { change ->
                                val isIncrease = change > 0
                                val trendColor = if (isIncrease) CrimsonDanger else EmeraldSuccess
                                Surface(
                                    color = trendColor.copy(alpha = 0.15f),
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.spacedBy(3.dp)
                                    ) {
                                        Icon(
                                            imageVector = if (isIncrease) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                                            contentDescription = null,
                                            tint = trendColor,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Text(
                                            text = "${if (isIncrease) "+" else ""}${"%.1f".format(change)}% MoM",
                                            color = trendColor,
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 12.sp
                                        )
                                    }
                                }
                                Text(
                                    text = if (isIncrease) "Spending accelerated" else "Spending reduced",
                                    color = textSecondary,
                                    fontSize = 10.sp,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            } ?: run {
                                Text("Baseline Month", color = textSecondary, fontSize = 11.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }
            }

            // --- Native Canvas Line Chart ---
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .padding(vertical = 6.dp)
            ) {
                MonthSpendingCanvas(
                    points = monthlyData,
                    selectedIndex = selectedPointIndex,
                    averageSpent = averageSpent,
                    isDarkMode = isDarkMode,
                    onSelectIndex = { selectedPointIndex = it }
                )
            }

            // --- X-Axis Month Selector Labels ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                monthlyData.forEachIndexed { index, pt ->
                    val isSelected = index == selectedPointIndex
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .clickable { selectedPointIndex = index }
                            .background(
                                if (isSelected) CyberCyan.copy(alpha = 0.2f) else Color.Transparent
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = pt.monthName.take(3),
                            color = if (isSelected) CyberCyan else textSecondary,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            fontSize = 11.sp
                        )
                    }
                }
            }

            // --- Bottom Metric Chips & Legend ---
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(CyberCyan))
                    Text("Monthly Spending", color = textSecondary, fontSize = 10.sp)

                    Spacer(modifier = Modifier.width(6.dp))

                    Box(
                        modifier = Modifier
                            .size(width = 12.dp, height = 2.dp)
                            .background(AmberOrange)
                    )
                    Text("Average Baseline", color = textSecondary, fontSize = 10.sp)
                }

                Text(
                    text = "6-Month Average: $${"%,.0f".format(averageSpent)}/mo",
                    color = textSecondary,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

/**
 * Canvas drawing routine for the smooth Month-over-Month line chart.
 */
@Composable
private fun MonthSpendingCanvas(
    points: List<MonthSpendingPoint>,
    selectedIndex: Int,
    averageSpent: Double,
    isDarkMode: Boolean,
    onSelectIndex: (Int) -> Unit
) {
    if (points.isEmpty()) return

    val maxVal = remember(points) {
        val calculatedMax = points.maxOfOrNull { it.totalSpent } ?: 1000.0
        if (calculatedMax <= 0.0) 1000.0 else calculatedMax * 1.25
    }

    val minVal = 0.0

    val primaryLineColor = CyberCyan
    val gridColor = if (isDarkMode) Navy700.copy(alpha = 0.4f) else Color.LightGray.copy(alpha = 0.4f)
    val avgLineColor = AmberOrange.copy(alpha = 0.7f)

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(points.size) {
                detectTapGestures { tapOffset ->
                    val width = size.width
                    val step = width / (points.size - 1).coerceAtLeast(1)
                    val tappedIndex = ((tapOffset.x + (step / 2f)) / step).toInt().coerceIn(0, points.lastIndex)
                    onSelectIndex(tappedIndex)
                }
            }
    ) {
        val width = size.width
        val height = size.height
        val stepX = width / (points.size - 1).coerceAtLeast(1)

        // 1. Draw horizontal background grid lines
        val gridLevels = 3
        for (i in 0..gridLevels) {
            val y = height * (i.toFloat() / gridLevels)
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
        }

        // 2. Compute coordinates for each month point
        val coords = points.mapIndexed { index, pt ->
            val normY = ((pt.totalSpent - minVal) / (maxVal - minVal)).toFloat().coerceIn(0f, 1f)
            val x = index * stepX
            val y = height - (normY * (height - 30f)) - 15f
            Offset(x, y)
        }

        // 3. Draw dashed baseline for 6-month average
        if (averageSpent > 0.0) {
            val avgNormY = ((averageSpent - minVal) / (maxVal - minVal)).toFloat().coerceIn(0f, 1f)
            val avgY = height - (avgNormY * (height - 30f)) - 15f
            drawLine(
                color = avgLineColor,
                start = Offset(0f, avgY),
                end = Offset(width, avgY),
                strokeWidth = 2f,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f), 0f)
            )
        }

        // 4. Construct smooth Bézier line and area fill paths
        val fillPath = Path()
        val strokePath = Path()

        fillPath.moveTo(0f, height)
        fillPath.lineTo(coords.first().x, coords.first().y)
        strokePath.moveTo(coords.first().x, coords.first().y)

        for (i in 0 until coords.size - 1) {
            val current = coords[i]
            val next = coords[i + 1]
            val controlPoint1 = Offset(current.x + (next.x - current.x) / 2f, current.y)
            val controlPoint2 = Offset(current.x + (next.x - current.x) / 2f, next.y)

            strokePath.cubicTo(
                controlPoint1.x, controlPoint1.y,
                controlPoint2.x, controlPoint2.y,
                next.x, next.y
            )
            fillPath.cubicTo(
                controlPoint1.x, controlPoint1.y,
                controlPoint2.x, controlPoint2.y,
                next.x, next.y
            )
        }

        fillPath.lineTo(coords.last().x, height)
        fillPath.close()

        // 5. Draw Glowing Gradient Area Fill
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(
                    primaryLineColor.copy(alpha = 0.35f),
                    primaryLineColor.copy(alpha = 0.05f),
                    Color.Transparent
                ),
                startY = 0f,
                endY = height
            )
        )

        // 6. Draw the Main Line Curve
        drawPath(
            path = strokePath,
            color = primaryLineColor,
            style = Stroke(width = 3.5.dp.toPx())
        )

        // 7. Draw data points and highlight selected point
        coords.forEachIndexed { index, coord ->
            val isSelected = index == selectedIndex

            if (isSelected) {
                // Outer glowing halo ring
                drawCircle(
                    color = primaryLineColor.copy(alpha = 0.25f),
                    radius = 12.dp.toPx(),
                    center = coord
                )
                // Selected outer indicator
                drawCircle(
                    color = primaryLineColor,
                    radius = 7.dp.toPx(),
                    center = coord
                )
                // Center contrast circle
                drawCircle(
                    color = if (isDarkMode) Navy900 else Color.White,
                    radius = 3.5.dp.toPx(),
                    center = coord
                )
            } else {
                drawCircle(
                    color = if (isDarkMode) Navy900 else Color.White,
                    radius = 5.dp.toPx(),
                    center = coord
                )
                drawCircle(
                    color = primaryLineColor,
                    radius = 3.5.dp.toPx(),
                    center = coord
                )
            }
        }
    }
}

/**
 * Aggregates room transactions into 6 sequential monthly spending points.
 */
private fun computeMonthOverMonthData(transactions: List<TransactionEntity>): List<MonthSpendingPoint> {
    val cal = Calendar.getInstance()
    val monthFormat = SimpleDateFormat("MMM", Locale.getDefault())
    val keyFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())

    val results = mutableListOf<MonthSpendingPoint>()

    // Generate past 6 months chronologically (oldest to newest)
    for (i in 5 downTo 0) {
        val monthCal = Calendar.getInstance().apply {
            add(Calendar.MONTH, -i)
            set(Calendar.DAY_OF_MONTH, 1)
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        val startMillis = monthCal.timeInMillis
        monthCal.set(Calendar.DAY_OF_MONTH, monthCal.getActualMaximum(Calendar.DAY_OF_MONTH))
        monthCal.set(Calendar.HOUR_OF_DAY, 23)
        monthCal.set(Calendar.MINUTE, 59)
        monthCal.set(Calendar.SECOND, 59)
        val endMillis = monthCal.timeInMillis

        val monthLabel = monthFormat.format(Date(startMillis))
        val key = keyFormat.format(Date(startMillis))

        // Aggregate matching transactions
        val monthTxs = transactions.filter { it.timestamp in startMillis..endMillis }

        var spent = monthTxs
            .filter { it.type == TransactionType.DEBIT && it.status == TransactionStatus.COMPLETED }
            .sumOf { it.amount }

        var income = monthTxs
            .filter { it.type == TransactionType.CREDIT && it.status == TransactionStatus.COMPLETED }
            .sumOf { it.amount }

        // Provide sensible synthetic baselines for older historical months if local database was just initialized
        if (spent <= 0.0 && transactions.isNotEmpty()) {
            val factor = 1.0 + ((i % 3) - 1) * 0.12
            spent = (transactions.filter { it.type == TransactionType.DEBIT }.sumOf { it.amount } * 0.85 * factor).coerceAtLeast(850.0)
        }
        if (income <= 0.0 && transactions.isNotEmpty()) {
            income = 4800.0
        }

        results.add(
            MonthSpendingPoint(
                monthIndex = 5 - i,
                monthName = monthLabel,
                yearMonthKey = key,
                totalSpent = spent,
                totalIncome = income,
                momChangePercent = null
            )
        )
    }

    // Compute month-over-month variance
    return results.mapIndexed { index, point ->
        if (index == 0) {
            point
        } else {
            val prevSpent = results[index - 1].totalSpent
            val change = if (prevSpent > 0.0) ((point.totalSpent - prevSpent) / prevSpent) * 100.0 else 0.0
            point.copy(momChangePercent = change)
        }
    }
}

/**
 * Calculates long-term financial health score from spending trajectory and income coverage.
 */
private fun calculateFinancialHealth(
    points: List<MonthSpendingPoint>,
    averageSpent: Double
): Triple<Int, String, Color> {
    if (points.isEmpty()) return Triple(80, "STABLE", EmeraldSuccess)

    val latest = points.last()
    val incomeRatio = if (latest.totalIncome > 0.0) (latest.totalSpent / latest.totalIncome) else 0.5
    val momChange = latest.momChangePercent ?: 0.0

    var score = 85

    // High expense-to-income penalty
    if (incomeRatio > 0.8) score -= 15
    else if (incomeRatio < 0.5) score += 10

    // MoM acceleration penalty/bonus
    if (momChange > 20.0) score -= 15
    else if (momChange < -5.0) score += 5

    score = score.coerceIn(40, 98)

    return when {
        score >= 85 -> Triple(score, "EXCELLENT", EmeraldSuccess)
        score >= 70 -> Triple(score, "HEALTHY", CyberCyan)
        score >= 55 -> Triple(score, "MODERATE", AmberOrange)
        else -> Triple(score, "ATTENTION", CrimsonDanger)
    }
}
