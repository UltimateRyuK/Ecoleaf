package com.example.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Collections
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.db.ScanRecord
import com.example.model.PlantType
import com.example.ui.components.EcoLeafHeroArt
import com.example.ui.components.EcoLeafLogoMark
import com.example.ui.components.EcoLeafWordmark
import com.example.ui.theme.DeepPlum
import com.example.ui.theme.GoldenAmber
import com.example.ui.theme.MutedTeal
import com.example.ui.theme.SoftLavender
import com.example.ui.theme.TerracottaCoral
import com.example.viewmodel.EcoLeafScreen

@Composable
fun HomeScreen(
    recentScans: List<ScanRecord>,
    onStartScan: () -> Unit,
    onUploadImage: () -> Unit,
    onSelectPlant: (PlantType) -> Unit,
    onOpenGuide: (PlantType) -> Unit,
    onNavigate: (EcoLeafScreen) -> Unit,
    onSelectScan: (ScanRecord) -> Unit
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Decorative background asymmetric accents
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Soft lavender background arc
            drawCircle(
                color = SoftLavender.copy(alpha = 0.08f),
                radius = w * 0.35f,
                center = Offset(w * 0.95f, h * 0.12f)
            )

            // Warm golden highlight dot
            drawCircle(
                color = GoldenAmber.copy(alpha = 0.45f),
                radius = 3.5.dp.toPx(),
                center = Offset(w * 0.12f, h * 0.28f)
            )

            // Terracotta small scanning mark in corner
            drawLine(
                color = TerracottaCoral.copy(alpha = 0.4f),
                start = Offset(w * 0.88f, h * 0.42f),
                end = Offset(w * 0.94f, h * 0.42f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
            drawLine(
                color = TerracottaCoral.copy(alpha = 0.4f),
                start = Offset(w * 0.94f, h * 0.42f),
                end = Offset(w * 0.94f, h * 0.46f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round
            )

            // Muted Teal subtle dot
            drawCircle(
                color = MutedTeal.copy(alpha = 0.4f),
                radius = 3.dp.toPx(),
                center = Offset(w * 0.84f, h * 0.68f)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp)
                .padding(top = 18.dp, bottom = 90.dp) // space for floating nav
                .testTag("home_screen_column"),
            horizontalAlignment = Alignment.Start
        ) {
            // Top Bar: EcoLeaf Wordmark Logo + Settings Icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                EcoLeafWordmark(logoSize = 28.dp, textSize = 21)

                IconButton(
                    onClick = { onNavigate(EcoLeafScreen.SETTINGS) },
                    modifier = Modifier
                        .size(42.dp)
                        .testTag("home_settings_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Settings",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Hero Section: Large Confident Headline + Subtitle
            Text(
                text = "Know your leaf.",
                style = MaterialTheme.typography.headlineLarge,
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground,
                letterSpacing = (-0.8).sp,
                lineHeight = 40.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Snap a leaf. Discover what it might be telling you.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                lineHeight = 22.sp
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Large Stylized Multi-Color Hero Illustration (Custom EcoLeaf Art)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentAlignment = Alignment.Center
            ) {
                EcoLeafHeroArt(modifier = Modifier.size(240.dp))
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons: Distinctive Primary & Secondary
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // Primary CTA: Scan a Leaf
                Button(
                    onClick = onStartScan,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DeepPlum,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(62.dp)
                        .testTag("scan_leaf_card")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color.White.copy(alpha = 0.20f),
                            modifier = Modifier.size(38.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = "Scan a Leaf",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                fontSize = 15.sp
                            )
                            Text(
                                text = "Take a new photo with camera",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color.White.copy(alpha = 0.85f),
                                fontSize = 12.sp
                            )
                        }
                    }
                }

                // Secondary CTA: Upload Image
                OutlinedButton(
                    onClick = onUploadImage,
                    shape = RoundedCornerShape(18.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onBackground
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.2.dp,
                        MaterialTheme.colorScheme.outline
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                        .testTag("home_upload_image_button")
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = TerracottaCoral.copy(alpha = 0.12f),
                            modifier = Modifier.size(34.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Collections,
                                    contentDescription = null,
                                    tint = TerracottaCoral,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(14.dp))

                        Column {
                            Text(
                                text = "Upload Image",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp
                            )
                            Text(
                                text = "Choose from your device library",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 11.sp
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Small encouraging line
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = MutedTeal,
                    modifier = Modifier.size(6.dp)
                ) {}
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Works with Money Plant, Snake Plant, Spider Plant, Rose & Marigold.",
                    style = MaterialTheme.typography.labelSmall,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.85f)
                )
            }
        }
    }
}
