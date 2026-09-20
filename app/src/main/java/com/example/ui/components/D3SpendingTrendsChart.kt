package com.example.ui.components

import android.annotation.SuppressLint
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.ShowChart
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.DailySpendingPoint
import com.example.ui.theme.AmberOrange
import com.example.ui.theme.CyberCyan
import com.example.ui.theme.ElectricBlue
import com.example.ui.theme.EmeraldSuccess
import com.example.ui.theme.Navy700
import com.example.ui.theme.Navy800
import com.example.ui.theme.Navy900
import com.example.ui.theme.NavyCard
import com.example.ui.theme.NavyCardLight
import com.example.ui.theme.PurpleTech
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextWhite
import com.example.ui.viewmodel.BankViewModel
import org.json.JSONArray
import org.json.JSONObject

/**
 * High-performance Jetpack Compose component that renders an interactive D3.js line chart
 * displaying the user's spending trends over the last 30 days fetched directly from Room database.
 */
@Composable
fun D3SpendingTrendsChart(
    viewModel: BankViewModel,
    modifier: Modifier = Modifier
) {
    val spendingTrends by viewModel.spendingTrendsLast30Days.collectAsStateWithLifecycle()

    D3SpendingTrendsChart(
        spendingPoints = spendingTrends,
        modifier = modifier
    )
}

/**
 * Overloaded variant of [D3SpendingTrendsChart] allowing direct provision of [DailySpendingPoint] list.
 */
@Composable
fun D3SpendingTrendsChart(
    spendingPoints: List<DailySpendingPoint>,
    modifier: Modifier = Modifier,
    title: String = "30-Day Spending Velocity & Trends",
    onPointSelected: ((DailySpendingPoint) -> Unit)? = null
) {
    var selectedRangeDays by remember { mutableIntStateOf(30) }
    var selectedPoint by remember { mutableStateOf<DailySpendingPoint?>(null) }

    // Filter points based on selected range (7D, 14D, 30D)
    val filteredPoints = remember(spendingPoints, selectedRangeDays) {
        if (spendingPoints.isEmpty()) {
            emptyList()
        } else {
            spendingPoints.takeLast(selectedRangeDays)
        }
    }

    val totalSpent = remember(filteredPoints) { filteredPoints.sumOf { it.amount } }
    val averageDaily = remember(filteredPoints, totalSpent) {
        if (filteredPoints.isNotEmpty()) totalSpent / filteredPoints.size else 0.0
    }
    val peakPoint = remember(filteredPoints) { filteredPoints.maxByOrNull { it.amount } }
    val activeSpendDays = remember(filteredPoints) { filteredPoints.count { it.amount > 0.0 } }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("d3_spending_chart_card"),
        colors = CardDefaults.cardColors(containerColor = NavyCard),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, CyberCyan.copy(alpha = 0.25f))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            // Header Row with D3 Badge & Time Filter
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Surface(
                            color = CyberCyan.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(6.dp)
                        ) {
                            Text(
                                text = "D3.JS POWERED",
                                color = CyberCyan,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = 1.2.sp,
                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                            )
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldSuccess)
                            )
                            Text(
                                text = "Room DB",
                                color = EmeraldSuccess,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = title,
                        color = TextWhite,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Range Selector (7D | 14D | 30D)
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(Navy800)
                        .padding(2.dp)
                        .testTag("d3_spending_time_selector"),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    listOf(7, 14, 30).forEach { days ->
                        val isSelected = selectedRangeDays == days
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) CyberCyan else Color.Transparent)
                                .clickable {
                                    selectedRangeDays = days
                                    selectedPoint = null
                                }
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${days}D",
                                color = if (isSelected) Navy900 else TextMuted,
                                fontSize = 11.sp,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Key Financial Metrics Strip
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Navy800.copy(alpha = 0.6f))
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Metric 1: Total
                Column {
                    Text(
                        text = "TOTAL SPENT (${selectedRangeDays}D)",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$${"%,.2f".format(totalSpent)}",
                        color = TextWhite,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.ExtraBold,
                        modifier = Modifier.testTag("d3_spending_stat_total")
                    )
                }

                // Metric 2: Daily Avg
                Column {
                    Text(
                        text = "DAILY AVERAGE",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "$${"%,.2f".format(averageDaily)}/d",
                        color = CyberCyan,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Metric 3: Peak Day
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "PEAK SPEND",
                        color = TextMuted,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (peakPoint != null && peakPoint.amount > 0.0) "$${"%,.0f".format(peakPoint.amount)}" else "$0",
                        color = AmberOrange,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // D3.js Line Chart WebView Container
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Navy900)
                    .testTag("d3_spending_chart_container")
            ) {
                D3WebViewLineChart(
                    points = filteredPoints,
                    onPointClick = { point ->
                        selectedPoint = point
                        onPointSelected?.invoke(point)
                    }
                )
            }

            // Interactive Point Inspection Detail Card
            AnimatedVisibility(
                visible = selectedPoint != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                selectedPoint?.let { point ->
                    val isAboveAvg = point.amount > averageDaily
                    val diffPercent = if (averageDaily > 0) {
                        (((point.amount - averageDaily) / averageDaily) * 100).toInt()
                    } else 0

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("d3_spending_selected_point"),
                        color = NavyCardLight,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, if (isAboveAvg) AmberOrange.copy(alpha = 0.5f) else CyberCyan.copy(alpha = 0.5f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(34.dp)
                                        .clip(CircleShape)
                                        .background(if (isAboveAvg) AmberOrange.copy(alpha = 0.15f) else CyberCyan.copy(alpha = 0.15f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = if (isAboveAvg) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                                        contentDescription = null,
                                        tint = if (isAboveAvg) AmberOrange else CyberCyan,
                                        modifier = Modifier.size(18.dp)
                                    )
                                }

                                Column {
                                    Text(
                                        text = "${point.date} (${point.fullDate})",
                                        color = TextWhite,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp
                                    )
                                    Text(
                                        text = "${point.transactionCount} transaction${if (point.transactionCount != 1) "s" else ""}",
                                        color = TextMuted,
                                        fontSize = 11.sp
                                    )
                                }
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "$${"%,.2f".format(point.amount)}",
                                    color = TextWhite,
                                    fontWeight = FontWeight.ExtraBold,
                                    fontSize = 15.sp
                                )
                                Text(
                                    text = if (isAboveAvg) "+$diffPercent% vs avg" else "$diffPercent% vs avg",
                                    color = if (isAboveAvg) AmberOrange else EmeraldSuccess,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            // Footer Subtitle / Guidance
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        tint = TextMuted,
                        modifier = Modifier.size(13.dp)
                    )
                    Text(
                        text = "Tap points to inspect day transactions",
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                Text(
                    text = "$activeSpendDays active spend days",
                    color = CyberCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

/**
 * Internal AndroidView WebView wrapper that embeds and executes D3.js v7 scripts
 * to render a smooth spline line chart with area gradient fill and touch interactions.
 */
@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun D3WebViewLineChart(
    points: List<DailySpendingPoint>,
    onPointClick: (DailySpendingPoint) -> Unit
) {
    val mainHandler = remember { Handler(Looper.getMainLooper()) }

    val jsonData = remember(points) {
        val jsonArray = JSONArray()
        points.forEachIndexed { index, p ->
            val obj = JSONObject().apply {
                put("index", index)
                put("date", p.date)
                put("fullDate", p.fullDate)
                put("amount", p.amount)
                put("count", p.transactionCount)
            }
            jsonArray.put(obj)
        }
        jsonArray.toString()
    }

    AndroidView(
        modifier = Modifier.fillMaxWidth().height(230.dp),
        factory = { context ->
            WebView(context).apply {
                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.cacheMode = WebSettings.LOAD_DEFAULT
                settings.useWideViewPort = true
                settings.loadWithOverviewMode = true
                setBackgroundColor(0) // Transparent background

                val jsInterface = object {
                    @JavascriptInterface
                    fun onPointSelected(index: Int, date: String, amount: Double, count: Int) {
                        mainHandler.post {
                            val targetPoint = points.getOrNull(index)
                                ?: DailySpendingPoint(date, date, 0L, amount, count)
                            onPointClick(targetPoint)
                        }
                    }
                }
                addJavascriptInterface(jsInterface, "AndroidChartBridge")

                val html = generateD3ChartHtml(jsonData)
                loadDataWithBaseURL("https://d3js.org", html, "text/html", "UTF-8", null)
            }
        },
        update = { webView ->
            // Re-render chart dynamically when data points change
            val js = "if (window.renderSpendingChart) { window.renderSpendingChart($jsonData); }"
            webView.evaluateJavascript(js, null)
        }
    )
}

/**
 * Generates self-contained HTML containing D3.js visualization scripts, SVG filter definitions,
 * responsive dimensions, curved spline paths, and offline-safe fallback routines.
 */
private fun generateD3ChartHtml(initialDataJson: String): String {
    return """
<!DOCTYPE html>
<html>
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
<style>
  * { box-sizing: border-box; margin: 0; padding: 0; -webkit-tap-highlight-color: transparent; }
  body, html { width: 100%; height: 100%; overflow: hidden; background: transparent; font-family: -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif; }
  #container { width: 100%; height: 100%; position: relative; display: flex; align-items: center; justify-content: center; }
  svg { width: 100%; height: 100%; overflow: visible; }
  .grid line { stroke: rgba(255, 255, 255, 0.07); stroke-dasharray: 2, 4; }
  .grid path { stroke-width: 0; }
  .axis text { fill: #94A3B8; font-size: 10px; font-weight: 500; }
  .axis line, .axis path { stroke: rgba(255, 255, 255, 0.12); }
  .line-glow {
    fill: none;
    stroke: #00E5FF;
    stroke-width: 2.5;
    stroke-linecap: round;
    stroke-linejoin: round;
    filter: drop-shadow(0 3px 6px rgba(0, 229, 255, 0.45));
  }
  .area-glow {
    fill: url(#d3-spending-gradient);
    opacity: 0.35;
  }
  .data-dot {
    fill: #00E5FF;
    stroke: #0B1120;
    stroke-width: 2;
    cursor: pointer;
    transition: r 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  }
  .data-dot:hover, .data-dot.active {
    r: 6.5;
    fill: #10B981;
    stroke: #FFFFFF;
    stroke-width: 2;
  }
  .focus-line {
    stroke: rgba(0, 229, 255, 0.4);
    stroke-width: 1.5;
    stroke-dasharray: 3, 3;
    pointer-events: none;
  }
  .tooltip {
    position: absolute;
    display: none;
    background: rgba(15, 23, 42, 0.95);
    border: 1px solid #00E5FF;
    border-radius: 6px;
    padding: 4px 8px;
    color: #FFFFFF;
    font-size: 11px;
    pointer-events: none;
    box-shadow: 0 4px 12px rgba(0,0,0,0.6);
    transform: translate(-50%, -120%);
    white-space: nowrap;
    z-index: 100;
  }
</style>
<script src="https://d3js.org/d3.v7.min.js"></script>
</head>
<body>
<div id="container">
  <div id="tooltip" class="tooltip"></div>
</div>

<script>
  let currentData = $initialDataJson;

  function renderSpendingChart(data) {
    currentData = data;
    const container = document.getElementById('container');
    const existingSvg = container.querySelector('svg');
    if (existingSvg) existingSvg.remove();

    const tooltip = document.getElementById('tooltip');

    const width = container.clientWidth || 360;
    const height = container.clientHeight || 230;
    const margin = { top: 20, right: 18, bottom: 32, left: 46 };
    const chartWidth = width - margin.left - margin.right;
    const chartHeight = height - margin.top - margin.bottom;

    if (!data || data.length === 0) {
      const msg = document.createElement('div');
      msg.style.color = '#94A3B8';
      msg.style.fontSize = '12px';
      msg.innerText = 'No spending records in selected period.';
      container.appendChild(msg);
      return;
    }

    if (typeof d3 !== 'undefined') {
      // --- D3.js v7 Implementation ---
      const svg = d3.select("#container")
        .append("svg")
        .attr("width", width)
        .attr("height", height)
        .append("g")
        .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

      // Defs & Gradients
      const defs = svg.append("defs");
      const gradient = defs.append("linearGradient")
        .attr("id", "d3-spending-gradient")
        .attr("x1", "0%").attr("y1", "0%")
        .attr("x2", "0%").attr("y2", "100%");
      gradient.append("stop").attr("offset", "0%").attr("stop-color", "#00E5FF").attr("stop-opacity", 0.5);
      gradient.append("stop").attr("offset", "100%").attr("stop-color", "#00E5FF").attr("stop-opacity", 0.0);

      // X and Y Scales
      const x = d3.scalePoint()
        .domain(data.map(d => d.date))
        .range([0, chartWidth])
        .padding(0.1);

      const maxAmount = d3.max(data, d => d.amount) || 100;
      const y = d3.scaleLinear()
        .domain([0, maxAmount * 1.15])
        .nice()
        .range([chartHeight, 0]);

      // Horizontal grid lines
      svg.append("g")
        .attr("class", "grid")
        .call(d3.axisLeft(y).ticks(4).tickSize(-chartWidth).tickFormat(""));

      // Bottom Axis (Filter ticks to avoid overcrowding)
      const tickStep = Math.ceil(data.length / 6);
      const xAxis = d3.axisBottom(x)
        .tickValues(data.filter((d, i) => i % tickStep === 0 || i === data.length - 1).map(d => d.date));

      svg.append("g")
        .attr("class", "axis")
        .attr("transform", "translate(0," + chartHeight + ")")
        .call(xAxis);

      // Left Axis with Currency formatting
      const yAxis = d3.axisLeft(y)
        .ticks(4)
        .tickFormat(d => "$" + (d >= 1000 ? (d/1000).toFixed(1) + "k" : d));

      svg.append("g")
        .attr("class", "axis")
        .call(yAxis);

      // Curved Area
      const areaGenerator = d3.area()
        .x(d => x(d.date))
        .y0(chartHeight)
        .y1(d => y(d.amount))
        .curve(d3.curveMonotoneX);

      svg.append("path")
        .datum(data)
        .attr("class", "area-glow")
        .attr("d", areaGenerator);

      // Curved Line
      const lineGenerator = d3.line()
        .x(d => x(d.date))
        .y(d => y(d.amount))
        .curve(d3.curveMonotoneX);

      const path = svg.append("path")
        .datum(data)
        .attr("class", "line-glow")
        .attr("d", lineGenerator);

      // Animate line stroke
      const totalLength = path.node().getTotalLength();
      path
        .attr("stroke-dasharray", totalLength + " " + totalLength)
        .attr("stroke-dashoffset", totalLength)
        .transition()
        .duration(700)
        .ease(d3.easeCubicOut)
        .attr("stroke-dashoffset", 0);

      // Focus Crosshair Line
      const focusLine = svg.append("line")
        .attr("class", "focus-line")
        .style("display", "none")
        .attr("y1", 0)
        .attr("y2", chartHeight);

      // Interactive Data Dots
      const dots = svg.selectAll(".data-dot")
        .data(data)
        .enter()
        .append("circle")
        .attr("class", "data-dot")
        .attr("cx", d => x(d.date))
        .attr("cy", d => y(d.amount))
        .attr("r", data.length > 20 ? 3.0 : 4.0)
        .on("click", function(event, d) {
          dots.classed("active", false);
          d3.select(this).classed("active", true);

          focusLine
            .style("display", null)
            .attr("x1", x(d.date))
            .attr("x2", x(d.date));

          tooltip.style.display = "block";
          tooltip.innerHTML = "<b>" + d.date + "</b>: $" + d.amount.toFixed(2) + " (" + d.count + " tx)";
          tooltip.style.left = (margin.left + x(d.date)) + "px";
          tooltip.style.top = (margin.top + y(d.amount)) + "px";

          if (window.AndroidChartBridge && window.AndroidChartBridge.onPointSelected) {
            window.AndroidChartBridge.onPointSelected(d.index, d.date, d.amount, d.count);
          }
        });

    } else {
      // --- Embedded Resilient SVG Fallback (Zero network / offline mode) ---
      renderEmbeddedSvg(container, data, chartWidth, chartHeight, margin);
    }
  }

  function renderEmbeddedSvg(container, data, w, h, margin) {
    const maxVal = Math.max(...data.map(d => d.amount), 50) * 1.15;
    const stepX = w / (data.length - 1 || 1);

    const points = data.map((d, i) => {
      const cx = margin.left + (i * stepX);
      const cy = margin.top + (h - (d.amount / maxVal) * h);
      return { x: cx, y: cy, d: d };
    });

    let pathD = "M " + points[0].x + " " + points[0].y;
    for (let i = 1; i < points.length; i++) {
      const prev = points[i - 1];
      const curr = points[i];
      const mx = (prev.x + curr.x) / 2;
      pathD += " C " + mx + " " + prev.y + ", " + mx + " " + curr.y + ", " + curr.x + " " + curr.y;
    }

    const areaD = pathD + " L " + points[points.length - 1].x + " " + (margin.top + h) + " L " + points[0].x + " " + (margin.top + h) + " Z";

    const svg = document.createElementNS("http://www.w3.org/2000/svg", "svg");
    svg.setAttribute("viewBox", "0 0 " + (w + margin.left + margin.right) + " " + (h + margin.top + margin.bottom));
    svg.innerHTML = `
      <defs>
        <linearGradient id="d3-spending-gradient" x1="0" y1="0" x2="0" y2="1">
          <stop offset="0%" stop-color="#00E5FF" stop-opacity="0.4"/>
          <stop offset="100%" stop-color="#00E5FF" stop-opacity="0.0"/>
        </linearGradient>
      </defs>
      <path d="` + areaD + `" class="area-glow" />
      <path d="` + pathD + `" class="line-glow" />
    `;

    points.forEach((pt) => {
      const circle = document.createElementNS("http://www.w3.org/2000/svg", "circle");
      circle.setAttribute("cx", pt.x);
      circle.setAttribute("cy", pt.y);
      circle.setAttribute("r", "3.5");
      circle.setAttribute("class", "data-dot");
      circle.onclick = function() {
        if (window.AndroidChartBridge) {
          window.AndroidChartBridge.onPointSelected(pt.d.index, pt.d.date, pt.d.amount, pt.d.count);
        }
      };
      svg.appendChild(circle);
    });

    container.appendChild(svg);
  }

  window.renderSpendingChart = renderSpendingChart;

  window.addEventListener('DOMContentLoaded', () => {
    renderSpendingChart(currentData);
  });
  window.addEventListener('resize', () => {
    renderSpendingChart(currentData);
  });

  // Initial trigger
  setTimeout(() => {
    renderSpendingChart(currentData);
  }, 100);
</script>
</body>
</html>
""";
}
