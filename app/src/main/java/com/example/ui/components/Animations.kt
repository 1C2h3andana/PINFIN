package com.example.ui.components

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlin.math.*

/**
 * -------------------------------------------------------------------------------------
 * 1. MODULE THEME SYSTEM & CINEMATIC ANIMATED AMBIENT BACKGROUNDS
 * -------------------------------------------------------------------------------------
 */
enum class ModuleAtmosphere(
    val primaryColor: Color,
    val secondaryColor: Color,
    val accentColor: Color,
    val particleType: String
) {
    LANDING(CyberCyan, RoyalBlue, GoldAccent, "EARTH_NODES"),
    BANKING(CyberCyan, ElectricBlue, EmeraldSuccess, "CIRCUIT_DATA"),
    AI_INTELLIGENCE(PurpleTech, CyberCyan, ElectricBlue, "NEURAL_SYNAPSE"),
    FRAUD_SECURITY(CrimsonDanger, AmberOrange, PurpleTech, "RADAR_CYBER"),
    INVESTMENTS(EmeraldSuccess, GoldAccent, CyberCyan, "MARKET_VELOCITY"),
    CLIMATE(EmeraldSuccess, TealAccent, CyberCyan, "WEATHER_PARTICLES"),
    HEALTHCARE(TealAccent, CyberCyan, ElectricBlue, "HOLO_VITAL"),
    GOVERNMENT(GoldAccent, RoyalBlue, PurpleTech, "INFRASTRUCTURE_GRID"),
    FUTURE_TECH(PurpleTech, CyberCyan, EmeraldLight, "QUANTUM_NODES")
}

/**
 * Planetary Holographic Atmospheric Canvas with Floating Particles & Dynamic Constellations
 */
@Composable
fun PlanetaryCinematicBackground(
    modifier: Modifier = Modifier,
    atmosphere: ModuleAtmosphere = ModuleAtmosphere.LANDING,
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "planetaryTransition")
    
    // Slow Ambient Gradient Pulse
    val gradientShift by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 14000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "gradientShift"
    )

    // Orbital Rotation for Cosmic Network Nodes
    val orbitalRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 35000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "orbitalRotation"
    )

    // Sub-wavelength Quantum Grid Scan
    val scanLineY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanLine"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Navy900)
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val canvasW = size.width
            val canvasH = size.height
            val center = Offset(canvasW * 0.5f, canvasH * 0.4f)

            // 1. Deep Space Radial Aura Gradient
            val auraColor = atmosphere.secondaryColor.copy(alpha = 0.18f + (gradientShift * 0.08f))
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(auraColor, atmosphere.primaryColor.copy(alpha = 0.05f), Color.Transparent),
                    center = center,
                    radius = canvasW * 0.85f
                ),
                radius = canvasW * 0.85f,
                center = center
            )

            // 2. Specialized Animated Planetary Background based on ModuleAtmosphere
            when (atmosphere) {
                ModuleAtmosphere.LANDING -> {
                    val globeRadius = canvasW * 0.38f
                    val angleRad = Math.toRadians(orbitalRotation.toDouble())

                    // Planetary Outer Glow
                    drawCircle(
                        color = CyberCyan.copy(alpha = 0.08f),
                        radius = globeRadius * 1.15f,
                        center = center,
                        style = Stroke(width = 1.5f)
                    )
                    drawCircle(
                        color = ElectricBlue.copy(alpha = 0.15f),
                        radius = globeRadius,
                        center = center,
                        style = Stroke(width = 2f)
                    )

                    // Planetary Latitudinal Ellipses
                    for (i in 1..4) {
                        val latH = globeRadius * (i * 0.22f)
                        drawOval(
                            color = CyberCyan.copy(alpha = 0.08f),
                            topLeft = Offset(center.x - globeRadius, center.y - latH / 2),
                            size = Size(globeRadius * 2, latH),
                            style = Stroke(width = 1f)
                        )
                    }

                    // Constellation Data Orbiting Satellites
                    for (k in 0..7) {
                        val kAngle = angleRad + (k * (Math.PI / 4.0))
                        val nodeX = center.x + (cos(kAngle) * globeRadius * 1.1f).toFloat()
                        val nodeY = center.y + (sin(kAngle) * globeRadius * 0.65f).toFloat()

                        drawCircle(color = GoldAccent.copy(alpha = 0.7f), radius = 3.5f, center = Offset(nodeX, nodeY))
                        drawLine(
                            color = CyberCyan.copy(alpha = 0.25f),
                            start = center,
                            end = Offset(nodeX, nodeY),
                            strokeWidth = 1f
                        )
                    }
                }
                ModuleAtmosphere.BANKING -> {
                    // Microchip Circuit Lines & Flowing Financial Currency Nodes
                    for (xStep in 0..6) {
                        val x = (canvasW / 7f) * xStep
                        drawLine(
                            color = CyberCyan.copy(alpha = 0.06f),
                            start = Offset(x, 0f),
                            end = Offset(x, canvasH),
                            strokeWidth = 1f
                        )
                    }
                    val packetY = (scanLineY * canvasH)
                    drawCircle(
                        color = EmeraldSuccess.copy(alpha = 0.6f),
                        radius = 4f,
                        center = Offset(canvasW * 0.28f, packetY)
                    )
                    drawCircle(
                        color = CyberCyan.copy(alpha = 0.8f),
                        radius = 5f,
                        center = Offset(canvasW * 0.72f, canvasH - packetY)
                    )
                }
                ModuleAtmosphere.FRAUD_SECURITY -> {
                    // Cyber Threat Radar Grid & Sweep Beam
                    val radarR = canvasW * 0.55f
                    drawCircle(color = CrimsonDanger.copy(alpha = 0.12f), radius = radarR, center = center, style = Stroke(width = 1.5f))
                    drawCircle(color = CrimsonDanger.copy(alpha = 0.08f), radius = radarR * 0.65f, center = center, style = Stroke(width = 1f))
                    drawCircle(color = CrimsonDanger.copy(alpha = 0.05f), radius = radarR * 0.35f, center = center, style = Stroke(width = 1f))

                    val sweepAngle = Math.toRadians(orbitalRotation.toDouble() * 1.5)
                    val sweepEnd = Offset(
                        center.x + (cos(sweepAngle) * radarR).toFloat(),
                        center.y + (sin(sweepAngle) * radarR).toFloat()
                    )
                    drawLine(
                        brush = Brush.linearGradient(listOf(CrimsonDanger.copy(alpha = 0.9f), Color.Transparent)),
                        start = center,
                        end = sweepEnd,
                        strokeWidth = 2.5f
                    )
                }
                ModuleAtmosphere.CLIMATE -> {
                    // Atmospheric Isobars & Environmental Green Energy Waves
                    for (w in 0..3) {
                        val wavePhase = (orbitalRotation * 0.05f) + (w * 1.2f)
                        val startY = canvasH * (0.25f + (w * 0.18f))
                        val path = Path().apply {
                            moveTo(0f, startY)
                            for (step in 0..10) {
                                val px = (canvasW / 10f) * step
                                val py = startY + (sin(wavePhase + (step * 0.6f)) * 24f)
                                lineTo(px, py)
                            }
                        }
                        drawPath(path = path, color = EmeraldSuccess.copy(alpha = 0.08f), style = Stroke(width = 1.5f))
                    }
                }
                else -> {
                    // Neural AI Synapse Mesh & Interconnected Hexagon Matrix
                    for (h in 0..5) {
                        val nAngle = Math.toRadians((orbitalRotation * 0.6 + h * 60.0))
                        val nx = center.x + (cos(nAngle) * (canvasW * 0.35f)).toFloat()
                        val ny = center.y + (sin(nAngle) * (canvasW * 0.35f)).toFloat()
                        drawCircle(color = atmosphere.primaryColor.copy(alpha = 0.5f), radius = 3.5f, center = Offset(nx, ny))
                        drawLine(
                            color = atmosphere.accentColor.copy(alpha = 0.15f),
                            start = center,
                            end = Offset(nx, ny),
                            strokeWidth = 1.2f
                        )
                    }
                }
            }

            // 3. Subtle Holographic Scan-Line Passing Vertically
            val scanY = scanLineY * canvasH
            drawLine(
                brush = Brush.horizontalGradient(
                    listOf(Color.Transparent, atmosphere.primaryColor.copy(alpha = 0.25f), Color.Transparent)
                ),
                start = Offset(0f, scanY),
                end = Offset(canvasW, scanY),
                strokeWidth = 2f
            )
        }

        // Render actual foreground page content inside the atmospheric container
        content()
    }
}

/**
 * -------------------------------------------------------------------------------------
 * 2. HOLOGRAPHIC & GLASSMORPHISM CONTAINER (FUTURISTIC FLOATING CARDS)
 * -------------------------------------------------------------------------------------
 */
@Composable
fun HolographicGlassCard(
    modifier: Modifier = Modifier,
    borderColor: Color = CyberCyan.copy(alpha = 0.35f),
    glowColor: Color = CyberCyan.copy(alpha = 0.15f),
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color = NavyCard.copy(alpha = 0.82f),
    isInteractive: Boolean = true,
    onClick: (() -> Unit)? = null,
    content: @Composable ColumnScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition(label = "holographicCardPulse")
    val borderPulse by infiniteTransition.animateFloat(
        initialValue = 0.3f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "borderPulse"
    )

    var isHovered by remember { mutableStateOf(false) }
    val animatedElevation by animateDpAsState(
        targetValue = if (isHovered) 12.dp else 4.dp,
        label = "elevationAnim"
    )

    Box(
        modifier = modifier
            .shadow(animatedElevation, shape, ambientColor = glowColor, spotColor = glowColor)
            .clip(shape)
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        backgroundColor,
                        Navy800.copy(alpha = 0.95f),
                        backgroundColor.copy(alpha = 0.75f)
                    ),
                    start = Offset(0f, 0f),
                    end = Offset(400f, 600f)
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        borderColor.copy(alpha = borderPulse),
                        ElectricBlue.copy(alpha = 0.2f),
                        borderColor.copy(alpha = borderPulse * 0.6f)
                    )
                ),
                shape = shape
            )
            .then(
                if (onClick != null && isInteractive) {
                    Modifier.clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(color = CyberCyan)
                    ) { onClick() }
                } else Modifier
            )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            content = content
        )
    }
}

/**
 * -------------------------------------------------------------------------------------
 * 3. AI-STYLE SCANNING / REVEAL TYPING HEADER
 * -------------------------------------------------------------------------------------
 */
@Composable
fun HolographicTypingHeader(
    title: String,
    subtitle: String? = null,
    tag: String = "PFIN // AI-CORE",
    accentColor: Color = CyberCyan,
    modifier: Modifier = Modifier
) {
    var displayedChars by remember { mutableStateOf(0) }

    LaunchedEffect(title) {
        displayedChars = 0
        for (i in 1..title.length) {
            displayedChars = i
            kotlinx.coroutines.delay(22)
        }
    }

    val infiniteTransition = rememberInfiniteTransition(label = "headerCursor")
    val cursorBlink by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(500, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "cursorBlink"
    )

    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(accentColor)
            )
            Text(
                text = tag,
                color = accentColor,
                fontSize = 10.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.8.sp,
                fontFamily = FontFamily.Monospace
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = title.take(displayedChars),
                color = TextWhite,
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.5.sp
            )
            if (displayedChars < title.length || cursorBlink > 0.5f) {
                Box(
                    modifier = Modifier
                        .padding(start = 3.dp)
                        .width(3.dp)
                        .height(20.dp)
                        .background(accentColor)
                )
            }
        }
        if (subtitle != null) {
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = subtitle,
                color = TextMuted,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}

/**
 * -------------------------------------------------------------------------------------
 * 4. ANIMATED NUMERICAL COUNTER (COUNTS UPWARD FROM ZERO)
 * -------------------------------------------------------------------------------------
 */
@Composable
fun AnimatedCountUpText(
    targetValue: Double,
    prefix: String = "$",
    suffix: String = "",
    decimals: Int = 2,
    color: Color = TextWhite,
    fontSize: TextUnit = 22.sp,
    fontWeight: FontWeight = FontWeight.Black,
    modifier: Modifier = Modifier
) {
    var animatedValue by remember { mutableStateOf(0.0) }
    val animProgress = remember { Animatable(0f) }

    LaunchedEffect(targetValue) {
        animProgress.snapTo(0f)
        animProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1200, easing = FastOutSlowInEasing)
        )
    }

    val formatStr = "%,.${decimals}f"
    val displayNum = formatStr.format(targetValue * animProgress.value)

    Text(
        text = "$prefix$displayNum$suffix",
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        fontFamily = FontFamily.Monospace,
        modifier = modifier
    )
}

/**
 * -------------------------------------------------------------------------------------
 * 5. ADVANCED RADIAL AI IQ / HEALTH GAUGE WITH ROTATING HOLOGRAPHIC RINGS
 * -------------------------------------------------------------------------------------
 */
@Composable
fun HolographicRadialGauge(
    score: Float,
    maxScore: Float = 100f,
    label: String = "HEALTH IQ",
    primaryColor: Color = CyberCyan,
    modifier: Modifier = Modifier.size(130.dp)
) {
    val infiniteTransition = rememberInfiniteTransition(label = "gaugeGlow")
    val haloRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(8000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "haloRotation"
    )

    val animatedScore = remember { Animatable(0f) }
    LaunchedEffect(score) {
        animatedScore.animateTo(
            targetValue = (score / maxScore).coerceIn(0f, 1f),
            animationSpec = tween(1500, easing = FastOutSlowInEasing)
        )
    }

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val strokeW = 10.dp.toPx()
            val radius = (size.minDimension - strokeW * 2) / 2

            // Track background arc
            drawArc(
                color = Navy700.copy(alpha = 0.5f),
                startAngle = 135f,
                sweepAngle = 270f,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeW, cap = StrokeCap.Round)
            )

            // Active Progress Gradient Arc
            drawArc(
                brush = Brush.sweepGradient(
                    listOf(primaryColor.copy(alpha = 0.5f), primaryColor, EmeraldSuccess)
                ),
                startAngle = 135f,
                sweepAngle = 270f * animatedScore.value,
                useCenter = false,
                topLeft = Offset(center.x - radius, center.y - radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(width = strokeW, cap = StrokeCap.Round)
            )

            // Outer Neon Ticks & Holographic Dashes
            val tickRadius = radius + strokeW * 0.9f
            for (t in 0..18) {
                val tAngle = Math.toRadians((135.0 + (t * (270.0 / 18.0))))
                val tx1 = center.x + (cos(tAngle) * tickRadius).toFloat()
                val ty1 = center.y + (sin(tAngle) * tickRadius).toFloat()
                val tx2 = center.x + (cos(tAngle) * (tickRadius + 4.dp.toPx())).toFloat()
                val ty2 = center.y + (sin(tAngle) * (tickRadius + 4.dp.toPx())).toFloat()

                drawLine(
                    color = primaryColor.copy(alpha = 0.35f),
                    start = Offset(tx1, ty1),
                    end = Offset(tx2, ty2),
                    strokeWidth = 1.5f
                )
            }
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = "${(score * animatedScore.value / (score / maxScore).coerceAtLeast(0.01f)).toInt()}",
                color = TextWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = label,
                color = primaryColor,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

/**
 * -------------------------------------------------------------------------------------
 * 6. HARDWARE-ACCELERATED POLYNOMIAL FINANCIAL CURVE (CANVAS GRAPH)
 * -------------------------------------------------------------------------------------
 */
@Composable
fun HolographicFinancialLineGraph(
    dataPoints: List<Float>,
    lineColor: Color = CyberCyan,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .height(140.dp)
) {
    val progress = remember { Animatable(0f) }

    LaunchedEffect(dataPoints) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing)
        )
    }

    Canvas(modifier = modifier) {
        if (dataPoints.size < 2) return@Canvas

        val w = size.width
        val h = size.height
        val minVal = dataPoints.minOrNull() ?: 0f
        val maxVal = (dataPoints.maxOrNull() ?: 100f).coerceAtLeast(minVal + 1f)
        val range = maxVal - minVal

        val points = dataPoints.mapIndexed { index, value ->
            val x = (index.toFloat() / (dataPoints.size - 1)) * w
            val normalizedY = 1f - ((value - minVal) / range)
            val y = normalizedY * (h - 24f) + 12f
            Offset(x, y)
        }

        val currentPointCount = (points.size * progress.value).toInt().coerceAtLeast(2)
        val animatedPoints = points.take(currentPointCount)

        val path = Path()
        val fillPath = Path()

        path.moveTo(animatedPoints.first().x, animatedPoints.first().y)
        fillPath.moveTo(animatedPoints.first().x, h)
        fillPath.lineTo(animatedPoints.first().x, animatedPoints.first().y)

        for (i in 0 until animatedPoints.size - 1) {
            val p0 = animatedPoints[i]
            val p1 = animatedPoints[i + 1]
            val controlPoint1 = Offset(p0.x + (p1.x - p0.x) / 2f, p0.y)
            val controlPoint2 = Offset(p0.x + (p1.x - p0.x) / 2f, p1.y)

            path.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, p1.x, p1.y)
            fillPath.cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, p1.x, p1.y)
        }

        fillPath.lineTo(animatedPoints.last().x, h)
        fillPath.close()

        // 1. Draw glowing gradient fill under curve
        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(lineColor.copy(alpha = 0.35f), lineColor.copy(alpha = 0.05f), Color.Transparent)
            )
        )

        // 2. Draw sharp stroke
        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 3f, cap = StrokeCap.Round, join = StrokeJoin.Round)
        )

        // 3. Draw active head pulse node
        val headPoint = animatedPoints.last()
        drawCircle(color = lineColor.copy(alpha = 0.3f), radius = 8f, center = headPoint)
        drawCircle(color = TextWhite, radius = 4f, center = headPoint)
    }
}

/**
 * -------------------------------------------------------------------------------------
 * 7. HOLOGRAPHIC AI INTELLIGENCE CORE / VOICE WAVEFORM
 * -------------------------------------------------------------------------------------
 */
@Composable
fun HolographicAiAssistantCore(
    modifier: Modifier = Modifier.size(72.dp),
    coreColor: Color = CyberCyan
) {
    val infiniteTransition = rememberInfiniteTransition(label = "aiCoreTransition")

    val coreRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "coreRotation"
    )

    val corePulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.18f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "corePulse"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val baseRadius = (size.minDimension / 2) * 0.8f

            // Outer Quantum Orbit
            drawCircle(
                color = coreColor.copy(alpha = 0.2f),
                radius = baseRadius * corePulse,
                center = center,
                style = Stroke(width = 1.5f)
            )

            // Inner Rotating Triangular/Hex Nodes
            val angleRad = Math.toRadians(coreRotation.toDouble())
            for (i in 0..2) {
                val a = angleRad + (i * (2 * Math.PI / 3))
                val nodeX = center.x + (cos(a) * baseRadius * 0.7f).toFloat()
                val nodeY = center.y + (sin(a) * baseRadius * 0.7f).toFloat()

                drawCircle(color = ElectricBlue, radius = 3.5f, center = Offset(nodeX, nodeY))
                drawLine(
                    color = coreColor.copy(alpha = 0.4f),
                    start = center,
                    end = Offset(nodeX, nodeY),
                    strokeWidth = 2f
                )
            }

            // Central Luminous Core
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(TextWhite, coreColor, Color.Transparent),
                    center = center,
                    radius = baseRadius * 0.45f
                ),
                radius = baseRadius * 0.45f,
                center = center
            )
        }
    }
}

/**
 * -------------------------------------------------------------------------------------
 * 8. ANIMATED STATUS BADGE & CYBER SCANNING HELPERS (BACKWARD COMPATIBLE)
 * -------------------------------------------------------------------------------------
 */
@Composable
fun PulsingStatusBadge(
    modifier: Modifier = Modifier,
    pulseColor: Color = CyberCyan,
    badgeSize: Dp = 12.dp
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulseTransition")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 2.4f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scaleAnim"
    )
    val alpha by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "alphaAnim"
    )

    Box(
        modifier = modifier.size(badgeSize * 2.5f),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(badgeSize * scale)
                .clip(CircleShape)
                .background(pulseColor.copy(alpha = alpha))
        )
        Box(
            modifier = Modifier
                .size(badgeSize)
                .clip(CircleShape)
                .background(pulseColor)
        )
    }
}

@Composable
fun AnimatedScannerBeam(
    modifier: Modifier = Modifier,
    beamColor: Color = CyberCyan
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scannerTransition")
    val scanOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scannerAnim"
    )

    Canvas(modifier = modifier) {
        val yPos = size.height * scanOffset
        
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color.Transparent,
                    beamColor.copy(alpha = 0.25f),
                    beamColor.copy(alpha = 0.8f),
                    beamColor.copy(alpha = 0.25f),
                    Color.Transparent
                ),
                startY = (yPos - 20f).coerceAtLeast(0f),
                endY = (yPos + 20f).coerceAtMost(size.height)
            ),
            topLeft = Offset(0f, (yPos - 20f).coerceAtLeast(0f)),
            size = Size(size.width, 40f.coerceAtMost(size.height))
        )

        drawLine(
            color = beamColor,
            start = Offset(0f, yPos),
            end = Offset(size.width, yPos),
            strokeWidth = 3f
        )
    }
}

@Composable
fun SuccessCelebrationParticles(
    modifier: Modifier = Modifier,
    particleCount: Int = 30
) {
    val infiniteTransition = rememberInfiniteTransition(label = "confettiTransition")
    val progress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2500, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "confettiProgress"
    )

    val colors = remember {
        listOf(CyberCyan, EmeraldSuccess, ElectricBlue, Color(0xFFFFD700), Color(0xFFFF6B6B))
    }

    Canvas(modifier = modifier) {
        for (i in 0 until particleCount) {
            val angle = (i * (360f / particleCount)) * (Math.PI / 180f)
            val speed = 150f + ((i * 37) % 200)
            val dist = progress * speed
            val x = (size.width / 2f) + (cos(angle) * dist).toFloat()
            val y = (size.height / 2f) + (sin(angle) * dist).toFloat() + (progress * 100f)
            val alpha = (1f - progress).coerceIn(0f, 1f)
            val color = colors[i % colors.size].copy(alpha = alpha)

            drawCircle(
                color = color,
                radius = (4f + (i % 5)),
                center = Offset(x, y)
            )
        }
    }
}

@Composable
fun CyberRadarScan(
    modifier: Modifier = Modifier,
    radarColor: Color = CyberCyan
) {
    val infiniteTransition = rememberInfiniteTransition(label = "radarTransition")
    val angle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "radarAngle"
    )

    Canvas(modifier = modifier.size(120.dp)) {
        val center = Offset(size.width / 2, size.height / 2)
        val radius = size.minDimension / 2 - 8f

        drawCircle(color = radarColor.copy(alpha = 0.15f), radius = radius, center = center, style = Stroke(width = 2f))
        drawCircle(color = radarColor.copy(alpha = 0.25f), radius = radius * 0.66f, center = center, style = Stroke(width = 2f))
        drawCircle(color = radarColor.copy(alpha = 0.35f), radius = radius * 0.33f, center = center, style = Stroke(width = 2f))

        drawLine(color = radarColor.copy(alpha = 0.2f), start = Offset(center.x, 8f), end = Offset(center.x, size.height - 8f), strokeWidth = 1.5f)
        drawLine(color = radarColor.copy(alpha = 0.2f), start = Offset(8f, center.y), end = Offset(size.width - 8f, center.y), strokeWidth = 1.5f)

        val rad = angle * (Math.PI / 180f)
        val endX = center.x + (radius * cos(rad)).toFloat()
        val endY = center.y + (radius * sin(rad)).toFloat()

        drawLine(
            color = radarColor,
            start = center,
            end = Offset(endX, endY),
            strokeWidth = 3f
        )
    }
}

/**
 * -------------------------------------------------------------------------------------
 * 9. SIGNATURE ANIMATION 1: "THE FINANCIAL PULSE OF HUMANITY"
 * One Person -> Family -> Business -> City -> Country -> Global Economy -> Planetary PFIN
 * -------------------------------------------------------------------------------------
 */
@Composable
fun FinancialPulseOfHumanityVisualizer(
    modifier: Modifier = Modifier,
    activeStageIndex: Int = 0,
    onStageSelected: ((Int) -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "pulseOfHumanity")
    val pulseProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "pulseProgress"
    )
    val ringRotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 20000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ringRotation"
    )

    val stages = listOf(
        "One Person" to CyberCyan,
        "Family Circle" to ElectricBlue,
        "Enterprise Network" to PurpleTech,
        "Smart Metropolis" to TealAccent,
        "Sovereign Nation" to GoldAccent,
        "Planetary Network" to EmeraldSuccess,
        "PFIN OS Core" to CyberCyan
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(NavyCard.copy(alpha = 0.85f))
            .border(1.dp, CyberCyan.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                PulsingStatusBadge(pulseColor = CyberCyan, badgeSize = 8.dp)
                Text(
                    text = "THE FINANCIAL PULSE OF HUMANITY",
                    color = CyberCyan,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.2.sp,
                    fontFamily = FontFamily.Monospace
                )
            }
            Text(
                text = "LFI-∞ PROTOCOL",
                color = TextMuted,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp),
            contentAlignment = Alignment.Center
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val center = Offset(size.width / 2, size.height / 2)
                val maxRadius = size.minDimension * 0.44f

                // Draw concentric expansion rings
                for (i in 1..6) {
                    val r = maxRadius * (i / 6f)
                    val alpha = if (i <= activeStageIndex + 1) 0.3f else 0.08f
                    drawCircle(
                        color = CyberCyan.copy(alpha = alpha),
                        radius = r,
                        center = center,
                        style = Stroke(width = 1f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(6f, 6f), 0f))
                    )
                }

                // Draw propagating quantum pulse wave
                val waveRadius = maxRadius * pulseProgress
                drawCircle(
                    color = ElectricBlue.copy(alpha = (1f - pulseProgress).coerceIn(0f, 0.8f)),
                    radius = waveRadius,
                    center = center,
                    style = Stroke(width = 2.5f)
                )

                // Orbital constellation satellites
                val rotRad = Math.toRadians(ringRotation.toDouble())
                for (k in 0..7) {
                    val kAngle = rotRad + (k * (Math.PI / 4.0))
                    val kRadius = maxRadius * 0.85f
                    val kX = center.x + (cos(kAngle) * kRadius).toFloat()
                    val kY = center.y + (sin(kAngle) * kRadius * 0.55f).toFloat()

                    drawCircle(color = GoldAccent.copy(alpha = 0.8f), radius = 3f, center = Offset(kX, kY))
                    drawLine(
                        color = CyberCyan.copy(alpha = 0.2f),
                        start = center,
                        end = Offset(kX, kY),
                        strokeWidth = 1f
                    )
                }

                // Central Luminous Origin (One Person / Universal Core)
                drawCircle(
                    brush = Brush.radialGradient(
                        colors = listOf(TextWhite, CyberCyan, ElectricBlue.copy(alpha = 0.2f)),
                        center = center,
                        radius = 24.dp.toPx()
                    ),
                    radius = 18.dp.toPx(),
                    center = center
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "PFIN",
                    color = Navy900,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    fontFamily = FontFamily.Monospace
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Interactive stage stepper bar
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            stages.forEachIndexed { index, (name, color) ->
                val isSelected = index <= activeStageIndex
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onStageSelected?.invoke(index) }
                ) {
                    Box(
                        modifier = Modifier
                            .size(if (index == activeStageIndex) 14.dp else 10.dp)
                            .clip(CircleShape)
                            .background(if (isSelected) color else Navy700)
                            .border(1.dp, if (isSelected) TextWhite else Color.Transparent, CircleShape)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = name.split(" ").first(),
                        color = if (isSelected) TextWhite else TextMuted,
                        fontSize = 9.sp,
                        fontWeight = if (index == activeStageIndex) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

/**
 * -------------------------------------------------------------------------------------
 * 10. SIGNATURE ANIMATION 2: "THE FUTURE PATH" TIMELINE VISUALIZER
 * 2026 -> 2030 -> 2040 -> 2050 -> 2075 -> 2100+
 * -------------------------------------------------------------------------------------
 */
@Composable
fun FuturePathTimelineVisualizer(
    modifier: Modifier = Modifier,
    selectedYearIndex: Int = 0,
    onYearSelected: (Int) -> Unit = {}
) {
    val epochs = listOf(
        Triple("2026", "Digital Banking & AI Copilot", CyberCyan),
        Triple("2030", "Autonomous Financial Twins", ElectricBlue),
        Triple("2040", "Smart City Autonomous Grids", TealAccent),
        Triple("2050", "Planetary Climate & ESG Ledger", EmeraldSuccess),
        Triple("2075", "Quantum Cryptographic Defense", PurpleTech),
        Triple("2100+", "Interplanetary Space Commerce", GoldAccent)
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(NavyCard.copy(alpha = 0.85f))
            .border(1.dp, ElectricBlue.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "THE FUTURE PATH // 2026 - 2100+",
                color = ElectricBlue,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 1.2.sp,
                fontFamily = FontFamily.Monospace
            )
            Text(
                text = "HORIZON PROJECTION",
                color = TextMuted,
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Interactive timeline slider
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            epochs.forEachIndexed { index, (year, _, color) ->
                val isSelected = index == selectedYearIndex
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) color.copy(alpha = 0.25f) else Navy800)
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) color else Navy700,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { onYearSelected(index) }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = year,
                        color = if (isSelected) color else TextMuted,
                        fontSize = 11.sp,
                        fontWeight = if (isSelected) FontWeight.Black else FontWeight.Medium,
                        fontFamily = FontFamily.Monospace
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Selected Era Showcase
        val currentEpoch = epochs[selectedYearIndex.coerceIn(0, epochs.size - 1)]
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Navy900)
                .border(1.dp, currentEpoch.third.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .clip(CircleShape)
                    .background(currentEpoch.third)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "ERA ${currentEpoch.first}: ${currentEpoch.second}",
                    color = TextWhite,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Automated simulation modeling dynamic multi-decade planetary capital velocity.",
                    color = TextMuted,
                    fontSize = 10.sp
                )
            }
        }
    }
}

/**
 * -------------------------------------------------------------------------------------
 * 11. HOLOGRAPHIC DIGITAL TWIN SIMULATION CANVAS
 * -------------------------------------------------------------------------------------
 */
@Composable
fun HolographicDigitalTwinVisualizer(
    modifier: Modifier = Modifier.size(170.dp),
    healthScore: Float = 94f,
    netWorthGrowth: Float = 1.18f
) {
    val infiniteTransition = rememberInfiniteTransition(label = "twinTransition")
    val orbitAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "twinOrbit"
    )
    val auraPulse by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.15f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "auraPulse"
    )

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val center = Offset(size.width / 2, size.height / 2)
            val radius = size.minDimension * 0.42f

            // Holographic Human Aura Field
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(CyberCyan.copy(alpha = 0.25f * auraPulse), ElectricBlue.copy(alpha = 0.08f), Color.Transparent),
                    center = center,
                    radius = radius * 1.2f
                ),
                radius = radius * 1.2f,
                center = center
            )

            // Outer Quantum Nodes (Income, Savings, Investments, Career, Healthcare, Retirement)
            val rad = Math.toRadians(orbitAngle.toDouble())
            val nodes = listOf("SAV", "INV", "INS", "RET", "TAX", "HLT")
            nodes.forEachIndexed { index, tag ->
                val nAngle = rad + (index * (Math.PI / 3.0))
                val nx = center.x + (cos(nAngle) * radius * 0.85f).toFloat()
                val ny = center.y + (sin(nAngle) * radius * 0.55f).toFloat()

                drawCircle(color = CyberCyan.copy(alpha = 0.8f), radius = 4f, center = Offset(nx, ny))
                drawLine(
                    color = ElectricBlue.copy(alpha = 0.35f),
                    start = center,
                    end = Offset(nx, ny),
                    strokeWidth = 1.2f
                )
            }

            // Central Silhouette Hologram Indicator
            drawCircle(
                color = CyberCyan,
                radius = 10.dp.toPx(),
                center = Offset(center.x, center.y - 12.dp.toPx())
            )
            drawRoundRect(
                color = ElectricBlue,
                topLeft = Offset(center.x - 14.dp.toPx(), center.y + 2.dp.toPx()),
                size = Size(28.dp.toPx(), 32.dp.toPx()),
                cornerRadius = androidx.compose.ui.geometry.CornerRadius(10f, 10f)
            )
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(top = 110.dp)
        ) {
            Text(
                text = "TWIN IQ ${healthScore.toInt()}",
                color = CyberCyan,
                fontSize = 9.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

