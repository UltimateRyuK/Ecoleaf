package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DeepPlum
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.MutedTeal
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.TerracottaCoral

/**
 * Custom minimalist EcoLeaf Brand Logo Mark.
 * Integrates an organic leaf silhouette with subtle geometric scanning brackets and lens aperture.
 */
@Composable
fun EcoLeafLogoMark(
    modifier: Modifier = Modifier,
    size: Dp = 32.dp,
    primaryColor: Color = DeepPlum,
    accentColor: Color = TerracottaCoral,
    dotColor: Color = GoldenAmber
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        // Left organic leaf half (botanical silhouette)
        val leftLeaf = Path().apply {
            moveTo(w * 0.5f, h * 0.12f)
            cubicTo(
                w * 0.12f, h * 0.30f,
                w * 0.10f, h * 0.68f,
                w * 0.5f, h * 0.90f
            )
            cubicTo(
                w * 0.42f, h * 0.65f,
                w * 0.42f, h * 0.35f,
                w * 0.5f, h * 0.12f
            )
            close()
        }
        drawPath(
            path = leftLeaf,
            color = primaryColor
        )

        // Right side scanning bracket / curved sensor contour
        val scanBracket = Path().apply {
            moveTo(w * 0.52f, h * 0.14f)
            cubicTo(
                w * 0.88f, h * 0.28f,
                w * 0.90f, h * 0.50f,
                w * 0.82f, h * 0.64f
            )
        }
        drawPath(
            path = scanBracket,
            color = accentColor,
            style = Stroke(width = w * 0.08f, cap = StrokeCap.Round)
        )

        val bottomScan = Path().apply {
            moveTo(w * 0.74f, h * 0.78f)
            cubicTo(
                w * 0.66f, h * 0.86f,
                w * 0.58f, h * 0.89f,
                w * 0.50f, h * 0.90f
            )
        }
        drawPath(
            path = bottomScan,
            color = primaryColor,
            style = Stroke(width = w * 0.08f, cap = StrokeCap.Round)
        )

        // Geometric AI focus dot in the scan aperture
        drawCircle(
            color = dotColor,
            radius = w * 0.07f,
            center = Offset(w * 0.68f, h * 0.46f)
        )
    }
}

/**
 * Full EcoLeaf Typographic Brand Header
 */
@Composable
fun EcoLeafWordmark(
    modifier: Modifier = Modifier,
    logoSize: Dp = 26.dp,
    textSize: Int = 20
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        EcoLeafLogoMark(size = logoSize)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "EcoLeaf",
            style = MaterialTheme.typography.titleLarge,
            fontSize = textSize.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground,
            letterSpacing = (-0.3).sp
        )
    }
}

/**
 * Large expressive hero botanical illustration with multi-color organic shapes,
 * delicate veins, scanning brackets and geometric accents.
 */
@Composable
fun EcoLeafHeroArt(
    modifier: Modifier = Modifier
) {
    val transition = rememberInfiniteTransition(label = "HeroScanPulse")
    val scanPulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "Pulse"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // 1. Soft lavender background blob (asymmetric organic atmosphere)
        val blobPath = Path().apply {
            moveTo(w * 0.35f, h * 0.15f)
            cubicTo(w * 0.85f, h * 0.05f, w * 0.98f, h * 0.45f, w * 0.80f, h * 0.82f)
            cubicTo(w * 0.65f, h * 0.95f, w * 0.20f, h * 0.90f, w * 0.12f, h * 0.65f)
            cubicTo(w * 0.05f, h * 0.40f, w * 0.15f, h * 0.22f, w * 0.35f, h * 0.15f)
            close()
        }
        drawPath(
            path = blobPath,
            color = SoftLavender.copy(alpha = 0.15f)
        )

        // 2. Terracotta Coral organic crescent accent
        drawCircle(
            color = TerracottaCoral.copy(alpha = 0.12f),
            radius = w * 0.32f,
            center = Offset(w * 0.72f, h * 0.34f)
        )

        // 3. Golden Amber small satellite sun dot
        drawCircle(
            color = GoldenAmber,
            radius = w * 0.038f,
            center = Offset(w * 0.22f, h * 0.24f)
        )

        // 4. Muted Teal accent curved line
        val tealCurve = Path().apply {
            moveTo(w * 0.16f, h * 0.75f)
            cubicTo(w * 0.28f, h * 0.86f, w * 0.48f, h * 0.88f, w * 0.62f, h * 0.84f)
        }
        drawPath(
            path = tealCurve,
            color = MutedTeal.copy(alpha = 0.45f),
            style = Stroke(width = 2.5.dp.toPx(), cap = StrokeCap.Round)
        )

        // 5. Stylized Primary Leaf Silhouette (Deep Plum outline + translucent body)
        val leafMain = Path().apply {
            moveTo(w * 0.50f, h * 0.12f)
            cubicTo(
                w * 0.86f, h * 0.28f,
                w * 0.90f, h * 0.66f,
                w * 0.50f, h * 0.88f
            )
            cubicTo(
                w * 0.10f, h * 0.66f,
                w * 0.14f, h * 0.28f,
                w * 0.50f, h * 0.12f
            )
            close()
        }

        drawPath(
            path = leafMain,
            color = DeepPlum.copy(alpha = 0.06f)
        )
        drawPath(
            path = leafMain,
            color = DeepPlum,
            style = Stroke(width = 2.4.dp.toPx(), cap = StrokeCap.Round)
        )

        // Central Spine / Stem
        val stem = Path().apply {
            moveTo(w * 0.50f, h * 0.20f)
            cubicTo(w * 0.51f, h * 0.45f, w * 0.49f, h * 0.72f, w * 0.50f, h * 0.92f)
        }
        drawPath(
            path = stem,
            color = DeepPlum.copy(alpha = 0.75f),
            style = Stroke(width = 1.8.dp.toPx(), cap = StrokeCap.Round)
        )

        // Leaf lateral veins (delicate)
        drawLine(
            color = DeepPlum.copy(alpha = 0.35f),
            start = Offset(w * 0.50f, h * 0.36f),
            end = Offset(w * 0.68f, h * 0.28f),
            strokeWidth = 1.4.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = DeepPlum.copy(alpha = 0.35f),
            start = Offset(w * 0.50f, h * 0.52f),
            end = Offset(w * 0.72f, h * 0.46f),
            strokeWidth = 1.4.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = DeepPlum.copy(alpha = 0.35f),
            start = Offset(w * 0.50f, h * 0.68f),
            end = Offset(w * 0.66f, h * 0.64f),
            strokeWidth = 1.4.dp.toPx(),
            cap = StrokeCap.Round
        )

        drawLine(
            color = DeepPlum.copy(alpha = 0.35f),
            start = Offset(w * 0.50f, h * 0.44f),
            end = Offset(w * 0.30f, h * 0.38f),
            strokeWidth = 1.4.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = DeepPlum.copy(alpha = 0.35f),
            start = Offset(w * 0.50f, h * 0.60f),
            end = Offset(w * 0.28f, h * 0.56f),
            strokeWidth = 1.4.dp.toPx(),
            cap = StrokeCap.Round
        )

        // 6. Subtle Modern AI Scanning Viewfinder Brackets
        val bracketColor = TerracottaCoral.copy(alpha = 0.75f)
        val bracketLen = w * 0.12f
        val bracketStroke = 1.8.dp.toPx()

        // Top Left Bracket
        drawLine(bracketColor, Offset(w * 0.22f, h * 0.22f), Offset(w * 0.22f + bracketLen, h * 0.22f), bracketStroke, StrokeCap.Round)
        drawLine(bracketColor, Offset(w * 0.22f, h * 0.22f), Offset(w * 0.22f, h * 0.22f + bracketLen), bracketStroke, StrokeCap.Round)

        // Top Right Bracket
        drawLine(bracketColor, Offset(w * 0.78f, h * 0.22f), Offset(w * 0.78f - bracketLen, h * 0.22f), bracketStroke, StrokeCap.Round)
        drawLine(bracketColor, Offset(w * 0.78f, h * 0.22f), Offset(w * 0.78f, h * 0.22f + bracketLen), bracketStroke, StrokeCap.Round)

        // Bottom Left Bracket
        drawLine(bracketColor, Offset(w * 0.22f, h * 0.78f), Offset(w * 0.22f + bracketLen, h * 0.78f), bracketStroke, StrokeCap.Round)
        drawLine(bracketColor, Offset(w * 0.22f, h * 0.78f), Offset(w * 0.22f, h * 0.78f - bracketLen), bracketStroke, StrokeCap.Round)

        // Bottom Right Bracket
        drawLine(bracketColor, Offset(w * 0.78f, h * 0.78f), Offset(w * 0.78f - bracketLen, h * 0.78f), bracketStroke, StrokeCap.Round)
        drawLine(bracketColor, Offset(w * 0.78f, h * 0.78f), Offset(w * 0.78f, h * 0.78f - bracketLen), bracketStroke, StrokeCap.Round)

        // 7. Subtle animated sweeping scan ray across leaf
        val scanY = h * 0.26f + (h * 0.48f * scanPulse)
        drawLine(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    TerracottaCoral.copy(alpha = 0.50f),
                    TerracottaCoral.copy(alpha = 0.85f),
                    TerracottaCoral.copy(alpha = 0.50f),
                    Color.Transparent
                ),
                startX = w * 0.28f,
                endX = w * 0.72f
            ),
            start = Offset(w * 0.28f, scanY),
            end = Offset(w * 0.72f, scanY),
            strokeWidth = 1.6.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Tiny decorative dots
        drawCircle(SoftLavender, radius = 2.5.dp.toPx(), center = Offset(w * 0.82f, h * 0.58f))
        drawCircle(MutedTeal, radius = 2.dp.toPx(), center = Offset(w * 0.16f, h * 0.48f))
    }
}

/**
 * Beautiful Startup Splash Screen with custom EcoLeaf logo mark,
 * animated reveal, and warm cream background with faint abstract botanical pattern.
 */
@Composable
fun EcoLeafSplashScreen(
    onFinished: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            EcoLeafLogoMark(size = 72.dp)
            Text(
                text = "EcoLeaf",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = (-0.5).sp
            )
            Text(
                text = "Your leaf's first AI health check.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
