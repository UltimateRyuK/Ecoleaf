package com.example.ui.screens

import android.graphics.Bitmap
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.PickVisualMediaRequest
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ml.ImageQualityResult
import com.example.model.PlantType
import com.example.ui.theme.DeepPlum
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.MutedTeal
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.TerracottaCoral

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CapturePreviewScreen(
    plantType: PlantType,
    bitmap: Bitmap?,
    qualityResult: ImageQualityResult?,
    onBack: () -> Unit,
    onCapturePhoto: (Bitmap) -> Unit,
    onPickImageUri: (Uri) -> Unit,
    onAnalyze: () -> Unit,
    onRetake: () -> Unit,
    onChooseAnotherPlant: () -> Unit
) {
    BackHandler { onBack() }

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            onPickImageUri(uri)
        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { capturedBitmap ->
        if (capturedBitmap != null) {
            onCapturePhoto(capturedBitmap)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (bitmap == null) "AI Leaf Scanner" else "Preview Leaf",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = plantType.commonName,
                            style = MaterialTheme.typography.labelSmall,
                            color = DeepPlum,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onBack,
                        modifier = Modifier.testTag("capture_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = onChooseAnotherPlant,
                        modifier = Modifier.testTag("change_plant_button")
                    ) {
                        Text(
                            text = "Change Plant",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TerracottaCoral
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
        if (bitmap == null) {
            // ==================== STATE A: DEDICATED AI LEAF SCANNER ====================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Viewfinder Container with Scanning Brackets & Reticle
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .clip(RoundedCornerShape(26.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(26.dp))
                        .testTag("camera_viewport"),
                    contentAlignment = Alignment.Center
                ) {
                    // Scanning reticle animation
                    ScannerReticleCanvas(
                        modifier = Modifier.fillMaxSize(),
                        bracketColor = TerracottaCoral,
                        accentColor = SoftLavender
                    )

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = DeepPlum.copy(alpha = 0.08f),
                            modifier = Modifier.size(68.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    tint = DeepPlum,
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))

                        Text(
                            text = "Place one leaf inside the frame",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            text = "Clear • close • well lit",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action Controls: Large circular button with colored ring + Upload Image
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Secondary Upload Action: Outlined pill button
                    OutlinedButton(
                        onClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MaterialTheme.colorScheme.onBackground
                        ),
                        border = BorderStroke(1.2.dp, MaterialTheme.colorScheme.outline),
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp)
                            .testTag("upload_image_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Collections,
                            contentDescription = null,
                            tint = TerracottaCoral,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Upload Image",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // Primary Circular Capture Button with colored ring
                    Box(
                        modifier = Modifier
                            .size(76.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.sweepGradient(
                                    listOf(DeepPlum, TerracottaCoral, GoldenAmber, DeepPlum)
                                )
                            )
                            .padding(4.dp)
                            .testTag("take_photo_button"),
                        contentAlignment = Alignment.Center
                    ) {
                        Surface(
                            onClick = {
                                try {
                                    cameraLauncher.launch(null)
                                } catch (e: Exception) {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                            },
                            shape = CircleShape,
                            color = DeepPlum,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Surface(
                                    shape = CircleShape,
                                    color = Color.White,
                                    modifier = Modifier.size(26.dp)
                                ) {}
                            }
                        }
                    }
                }
            }
        } else {
            // ==================== STATE B: PREVIEW VIEW ====================
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp, vertical = 14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    // Small Plant Label & "Ready to scan" indicator
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = MutedTeal,
                            modifier = Modifier.size(8.dp)
                        ) {}
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "${plantType.commonName} • Ready to scan",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Large Leaf Image with soft rounded corners
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f, fill = false)
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(24.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .border(1.2.dp, MaterialTheme.colorScheme.outline, RoundedCornerShape(24.dp))
                            .testTag("leaf_image_preview"),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Selected leaf preview",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    // Non-intrusive quality check banner
                    if (qualityResult != null && !qualityResult.isAcceptable) {
                        Spacer(modifier = Modifier.height(14.dp))
                        Surface(
                            shape = RoundedCornerShape(14.dp),
                            color = GoldenAmber.copy(alpha = 0.12f),
                            border = BorderStroke(1.dp, GoldenAmber.copy(alpha = 0.35f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = GoldenAmber,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = qualityResult.warningMessage ?: "Please take a clearer photo.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Actions: Primary "Analyze Leaf" (DeepPlum) + Secondary "Retake"
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Button(
                        onClick = onAnalyze,
                        shape = RoundedCornerShape(18.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DeepPlum,
                            contentColor = Color.White
                        ),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .testTag("analyze_leaf_button")
                    ) {
                        Text(
                            text = "Analyze Leaf",
                            style = MaterialTheme.typography.labelLarge,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    TextButton(
                        onClick = onRetake,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(44.dp)
                            .testTag("retake_photo_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Retake",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * Animated Viewfinder Reticle with subtle scanning corner brackets
 */
@Composable
private fun ScannerReticleCanvas(
    modifier: Modifier = Modifier,
    bracketColor: Color,
    accentColor: Color
) {
    val transition = rememberInfiniteTransition(label = "ScannerPulse")
    val pulse by transition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ScanPulse"
    )

    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        val bLength = 28.dp.toPx()
        val bStroke = 2.5.dp.toPx()

        val left = w * 0.14f
        val right = w * 0.86f
        val top = h * 0.18f
        val bottom = h * 0.82f

        // Top Left
        drawLine(bracketColor, Offset(left, top), Offset(left + bLength, top), bStroke, StrokeCap.Round)
        drawLine(bracketColor, Offset(left, top), Offset(left, top + bLength), bStroke, StrokeCap.Round)

        // Top Right
        drawLine(bracketColor, Offset(right, top), Offset(right - bLength, top), bStroke, StrokeCap.Round)
        drawLine(bracketColor, Offset(right, top), Offset(right, top + bLength), bStroke, StrokeCap.Round)

        // Bottom Left
        drawLine(bracketColor, Offset(left, bottom), Offset(left + bLength, bottom), bStroke, StrokeCap.Round)
        drawLine(bracketColor, Offset(left, bottom), Offset(left, bottom - bLength), bStroke, StrokeCap.Round)

        // Bottom Right
        drawLine(bracketColor, Offset(right, bottom), Offset(right - bLength, bottom), bStroke, StrokeCap.Round)
        drawLine(bracketColor, Offset(right, bottom), Offset(right, bottom - bLength), bStroke, StrokeCap.Round)

        // Animated Sweeping Scan Beam
        val scanY = top + (bottom - top) * pulse
        drawLine(
            brush = Brush.horizontalGradient(
                colors = listOf(
                    Color.Transparent,
                    bracketColor.copy(alpha = 0.35f),
                    bracketColor.copy(alpha = 0.85f),
                    bracketColor.copy(alpha = 0.35f),
                    Color.Transparent
                ),
                startX = left,
                endX = right
            ),
            start = Offset(left, scanY),
            end = Offset(right, scanY),
            strokeWidth = 2.dp.toPx(),
            cap = StrokeCap.Round
        )

        // Botanical decorative accents on edges
        drawCircle(accentColor.copy(alpha = 0.6f), radius = 2.5.dp.toPx(), center = Offset(left - 8.dp.toPx(), h * 0.5f))
        drawCircle(accentColor.copy(alpha = 0.6f), radius = 2.5.dp.toPx(), center = Offset(right + 8.dp.toPx(), h * 0.5f))
    }
}
