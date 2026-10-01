package com.example.ui.screens

import android.graphics.Bitmap
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ml.ModelPredictionResult
import com.example.model.PlantCondition
import com.example.model.PlantType
import com.example.ui.components.EcoLeafLogoMark
import com.example.ui.theme.DeepPlum
import com.example.ui.theme.DeepPlumContainer
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.GoldenAmberContainer
import com.example.ui.theme.MutedTeal
import com.example.ui.theme.MutedTealContainer
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.SoftLavenderContainer
import com.example.ui.theme.TerracottaCoral
import com.example.ui.theme.TerracottaCoralContainer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiagnosisResultScreen(
    plantType: PlantType,
    bitmap: Bitmap?,
    isAnalyzing: Boolean,
    predictionResult: ModelPredictionResult?,
    conditionDetail: PlantCondition?,
    confidenceThreshold: Float,
    savedRecordId: Long?,
    onBack: () -> Unit,
    onSaveNotes: (String) -> Unit,
    onOpenGuide: (PlantType) -> Unit,
    onAskSprig: () -> Unit,
    onScanAgain: () -> Unit
) {
    BackHandler { onBack() }

    var symptomsExpanded by remember { mutableStateOf(true) }
    var whatToDoExpanded by remember { mutableStateOf(true) }
    var preventionExpanded by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Diagnosis",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("result_back_button")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = { onOpenGuide(plantType) },
                        modifier = Modifier.testTag("result_guide_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.MenuBook,
                            contentDescription = "Guide",
                            tint = DeepPlum
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        if (isAnalyzing) {
            // ==================== 1. CREATIVE ANALYSIS SCREEN ====================
            val transition = rememberInfiniteTransition(label = "AnalysisScan")
            val sweepProgress by transition.animateFloat(
                initialValue = 0f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(1800, easing = FastOutSlowInEasing),
                    repeatMode = RepeatMode.Restart
                ),
                label = "ScanSweep"
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(22.dp),
                    modifier = Modifier.padding(32.dp)
                ) {
                    // EcoLeaf Brand Mark
                    EcoLeafLogoMark(size = 48.dp)

                    // Leaf Preview with sweeping scanning frame
                    if (bitmap != null) {
                        Box(
                            modifier = Modifier
                                .size(140.dp)
                                .clip(RoundedCornerShape(22.dp))
                                .background(MaterialTheme.colorScheme.surface)
                                .border(1.5.dp, DeepPlum.copy(alpha = 0.4f), RoundedCornerShape(22.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                bitmap = bitmap.asImageBitmap(),
                                contentDescription = "Leaf being analyzed",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                            // Sweeping scan beam
                            Canvas(modifier = Modifier.fillMaxSize()) {
                                val beamY = size.height * sweepProgress
                                drawLine(
                                    brush = Brush.horizontalGradient(
                                        listOf(
                                            Color.Transparent,
                                            TerracottaCoral.copy(alpha = 0.5f),
                                            TerracottaCoral,
                                            TerracottaCoral.copy(alpha = 0.5f),
                                            Color.Transparent
                                        )
                                    ),
                                    start = Offset(0f, beamY),
                                    end = Offset(size.width, beamY),
                                    strokeWidth = 3.dp.toPx(),
                                    cap = StrokeCap.Round
                                )
                            }
                        }
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Reading your leaf…",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            fontSize = 17.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Classifying with on-device ${plantType.commonName} model",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        } else if (predictionResult != null) {
            val topConfidence = predictionResult.topConfidence
            val isLowConfidence = topConfidence < confidenceThreshold
            val topClass = predictionResult.topClass
            val isHealthy = topClass.contains("Healthy", ignoreCase = true)
            val displayName = conditionDetail?.displayName ?: topClass.replace(plantType.commonName.replace(" ", ""), "").trim('_')

            if (isLowConfidence) {
                // ==================== 2. ELEGANT LOW CONFIDENCE SCREEN ====================
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 24.dp, vertical = 20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.weight(1f),
                        verticalArrangement = Arrangement.Center
                    ) {
                        if (bitmap != null) {
                            Box(
                                modifier = Modifier
                                    .size(130.dp)
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .border(1.2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(22.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = "Leaf photograph",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                            Spacer(modifier = Modifier.height(24.dp))
                        }

                        // Warm Amber / Soft Lavender card
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surface,
                            border = BorderStroke(1.2.dp, GoldenAmber.copy(alpha = 0.4f)),
                            shadowElevation = 2.dp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("low_confidence_banner")
                        ) {
                            Column(
                                modifier = Modifier.padding(22.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Surface(
                                    shape = CircleShape,
                                    color = GoldenAmberContainer,
                                    modifier = Modifier.size(50.dp)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = Icons.Default.Warning,
                                            contentDescription = null,
                                            tint = GoldenAmber,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(14.dp))

                                Text(
                                    text = "Let's take another look.",
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onBackground
                                )

                                Spacer(modifier = Modifier.height(6.dp))

                                Text(
                                    text = "The photo isn't clear enough for a confident match.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                Spacer(modifier = Modifier.height(10.dp))

                                Text(
                                    text = "Top match was ${(topConfidence * 100).toInt()}% (threshold: ${(confidenceThreshold * 100).toInt()}%)",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = SoftLavender,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    // Actions: Retake Photo & Upload Another
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Button(
                            onClick = onScanAgain,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepPlum,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("retake_low_conf_btn")
                        ) {
                            Icon(imageVector = Icons.Default.CameraAlt, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Retake Photo",
                                style = MaterialTheme.typography.labelLarge,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        OutlinedButton(
                            onClick = onScanAgain,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = MaterialTheme.colorScheme.onBackground
                            ),
                            border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Collections, contentDescription = null, modifier = Modifier.size(18.dp), tint = TerracottaCoral)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Upload Another",
                                style = MaterialTheme.typography.labelLarge,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        OutlinedButton(
                            onClick = onAskSprig,
                            shape = RoundedCornerShape(16.dp),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = DeepPlum
                            ),
                            border = BorderStroke(1.2.dp, SoftLavender.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("low_conf_ask_sprig_btn")
                        ) {
                            Icon(imageVector = Icons.Default.AutoAwesome, contentDescription = null, modifier = Modifier.size(18.dp), tint = DeepPlum)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Ask Sprig for Guidance",
                                style = MaterialTheme.typography.labelLarge,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepPlum
                            )
                        }
                    }
                }
            } else {
                // ==================== 3. MAIN CONFIDENT RESULT SCREEN ====================
                val resultAccent = if (isHealthy) MutedTeal else TerracottaCoral
                val resultContainer = if (isHealthy) MutedTealContainer else TerracottaCoralContainer

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .testTag("diagnosis_result_column"),
                    contentPadding = PaddingValues(start = 24.dp, end = 24.dp, top = 8.dp, bottom = 40.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Large Dominant Result Header
                    item {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = plantType.commonName,
                                style = MaterialTheme.typography.labelLarge,
                                color = DeepPlum,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp
                            )

                            Spacer(modifier = Modifier.height(2.dp))

                            Text(
                                text = if (isHealthy) "Looks Healthy" else "Likely $displayName",
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                fontSize = 28.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Colored Confidence Badge
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = resultContainer
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 5.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = resultAccent,
                                        modifier = Modifier.size(7.dp)
                                    ) {}
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "${(topConfidence * 100).toInt()}% confidence",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = resultAccent,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    // Leaf Photo in Rounded Container
                    if (bitmap != null) {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(210.dp)
                                    .clip(RoundedCornerShape(22.dp))
                                    .background(MaterialTheme.colorScheme.surface)
                                    .border(1.2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(22.dp)),
                                contentAlignment = Alignment.Center
                            ) {
                                Image(
                                    bitmap = bitmap.asImageBitmap(),
                                    contentDescription = "Diagnosed leaf",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            }
                        }
                    }

                    // Top 3 Predictions Horizontal Bars with Rounded Ends
                    if (predictionResult.topPredictions.isNotEmpty()) {
                        item {
                            Surface(
                                shape = RoundedCornerShape(18.dp),
                                color = MaterialTheme.colorScheme.surface,
                                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                                shadowElevation = 1.dp,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Column(modifier = Modifier.padding(18.dp)) {
                                    Text(
                                        text = "Top Predictions",
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        fontSize = 15.sp
                                    )

                                    Spacer(modifier = Modifier.height(14.dp))

                                    predictionResult.topPredictions.forEachIndexed { index, pred ->
                                        val labelClean = pred.classKey.replace(plantType.commonName.replace(" ", ""), "").trim('_')
                                        val isTop = index == 0
                                        val percent = (pred.score * 100).toInt()
                                        val barColor = when (index) {
                                            0 -> if (isHealthy) MutedTeal else DeepPlum
                                            1 -> SoftLavender
                                            else -> TerracottaCoral.copy(alpha = 0.5f)
                                        }

                                        Column(modifier = Modifier.padding(vertical = 5.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(
                                                    text = labelClean,
                                                    style = MaterialTheme.typography.bodyMedium,
                                                    fontWeight = if (isTop) FontWeight.Bold else FontWeight.Medium,
                                                    color = if (isTop) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                                                    fontSize = 13.sp
                                                )
                                                Text(
                                                    text = "$percent%",
                                                    style = MaterialTheme.typography.labelMedium,
                                                    fontWeight = if (isTop) FontWeight.Bold else FontWeight.Normal,
                                                    color = barColor
                                                )
                                            }

                                            Spacer(modifier = Modifier.height(5.dp))

                                            LinearProgressIndicator(
                                                progress = { pred.score },
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .height(7.dp)
                                                    .clip(CircleShape),
                                                color = barColor,
                                                trackColor = MaterialTheme.colorScheme.surfaceVariant
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Information Sections with small colored icons
                    if (conditionDetail != null) {
                        // Section: Symptoms (Terracotta Coral)
                        item {
                            CreativeDetailSection(
                                title = "Symptoms",
                                icon = Icons.Default.Info,
                                iconTint = TerracottaCoral,
                                isExpanded = symptomsExpanded,
                                onToggle = { symptomsExpanded = !symptomsExpanded }
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    conditionDetail.symptoms.forEach { symptom ->
                                        Row(verticalAlignment = Alignment.Top) {
                                            Text(
                                                text = "• ",
                                                color = TerracottaCoral,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = symptom,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontSize = 13.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        // Section: What to do (Deep Plum)
                        item {
                            CreativeDetailSection(
                                title = if (isHealthy) "Recommended Care" else "What to do",
                                icon = Icons.Default.Lightbulb,
                                iconTint = DeepPlum,
                                isExpanded = whatToDoExpanded,
                                onToggle = { whatToDoExpanded = !whatToDoExpanded }
                            ) {
                                Text(
                                    text = conditionDetail.whatToDo,
                                    style = MaterialTheme.typography.bodyMedium,
                                    lineHeight = 21.sp,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    fontSize = 13.sp
                                )
                            }
                        }

                        // Section: Prevention (Muted Teal)
                        item {
                            CreativeDetailSection(
                                title = "Prevention",
                                icon = Icons.Default.Shield,
                                iconTint = MutedTeal,
                                isExpanded = preventionExpanded,
                                onToggle = { preventionExpanded = !preventionExpanded }
                            ) {
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    conditionDetail.prevention.forEach { prev ->
                                        Row(verticalAlignment = Alignment.Top) {
                                            Text(
                                                text = "• ",
                                                color = MutedTeal,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = prev,
                                                style = MaterialTheme.typography.bodyMedium,
                                                color = MaterialTheme.colorScheme.onSurface,
                                                fontSize = 13.sp
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // "Ask Sprig about this result" Action
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = onAskSprig,
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = SoftLavenderContainer,
                                contentColor = DeepPlum
                            ),
                            border = BorderStroke(1.2.dp, SoftLavender.copy(alpha = 0.5f)),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 1.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp)
                                .testTag("ask_sprig_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = DeepPlum,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Ask Sprig about this result",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = DeepPlum,
                                fontSize = 15.sp
                            )
                        }
                    }

                    // Primary Action: Scan Another Leaf
                    item {
                        Spacer(modifier = Modifier.height(4.dp))
                        Button(
                            onClick = onScanAgain,
                            shape = RoundedCornerShape(18.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = DeepPlum,
                                contentColor = Color.White
                            ),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .testTag("scan_again_button")
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Scan Another Leaf",
                                style = MaterialTheme.typography.labelLarge,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CreativeDetailSection(
    title: String,
    icon: ImageVector,
    iconTint: Color,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    content: @Composable () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        shadowElevation = 1.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        shape = CircleShape,
                        color = iconTint.copy(alpha = 0.12f),
                        modifier = Modifier.size(32.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = icon,
                                contentDescription = null,
                                tint = iconTint,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 15.sp
                    )
                }

                IconButton(onClick = onToggle, modifier = Modifier.size(32.dp)) {
                    Icon(
                        imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            AnimatedVisibility(visible = isExpanded) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    content()
                }
            }
        }
    }
}
