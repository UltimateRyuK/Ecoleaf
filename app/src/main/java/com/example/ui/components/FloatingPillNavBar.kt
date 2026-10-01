package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DeepPlum
import com.example.ui.theme.DeepPlumContainer
import com.example.viewmodel.EcoLeafScreen

/**
 * Signature EcoLeaf Floating Pill Navigation Bar.
 * Elevated, detached pill with 4 destinations: Home, Scan, Sprig, History.
 */
@Composable
fun FloatingPillNavBar(
    currentScreen: EcoLeafScreen,
    onNavigate: (EcoLeafScreen) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .navigationBarsPadding()
            .padding(start = 20.dp, end = 20.dp, bottom = 14.dp)
            .testTag("floating_pill_nav_bar"),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.surface.copy(alpha = 0.98f),
            shadowElevation = 8.dp,
            tonalElevation = 2.dp,
            border = androidx.compose.foundation.BorderStroke(
                1.dp,
                MaterialTheme.colorScheme.outline.copy(alpha = 0.7f)
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                FloatingNavItem(
                    label = "Home",
                    icon = Icons.Default.Home,
                    isSelected = currentScreen == EcoLeafScreen.HOME,
                    onClick = { onNavigate(EcoLeafScreen.HOME) },
                    testTag = "nav_item_home",
                    modifier = Modifier.weight(1f)
                )

                FloatingNavItem(
                    label = "Scan",
                    icon = Icons.Default.CameraAlt,
                    isSelected = currentScreen == EcoLeafScreen.PLANT_SELECT,
                    onClick = { onNavigate(EcoLeafScreen.PLANT_SELECT) },
                    testTag = "nav_item_scan",
                    modifier = Modifier.weight(1f)
                )

                FloatingNavItem(
                    label = "Sprig",
                    icon = Icons.Default.AutoAwesome,
                    isSelected = currentScreen == EcoLeafScreen.SPRIG,
                    onClick = { onNavigate(EcoLeafScreen.SPRIG) },
                    testTag = "nav_item_sprig",
                    modifier = Modifier.weight(1f)
                )

                FloatingNavItem(
                    label = "History",
                    icon = Icons.Default.History,
                    isSelected = currentScreen == EcoLeafScreen.HISTORY,
                    onClick = { onNavigate(EcoLeafScreen.HISTORY) },
                    testTag = "nav_item_history",
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}

@Composable
private fun FloatingNavItem(
    label: String,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit,
    testTag: String,
    modifier: Modifier = Modifier
) {
    val pillBgColor by animateColorAsState(
        targetValue = if (isSelected) DeepPlumContainer else Color.Transparent,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "PillBg"
    )

    val contentColor by animateColorAsState(
        targetValue = if (isSelected) DeepPlum else MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = spring(stiffness = Spring.StiffnessMediumLow),
        label = "ContentColor"
    )

    Box(
        modifier = modifier
            .height(52.dp)
            .clip(CircleShape)
            .background(pillBgColor)
            .clickable(onClick = onClick)
            .padding(horizontal = 6.dp, vertical = 6.dp)
            .testTag(testTag),
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = contentColor,
                modifier = Modifier.size(20.dp)
            )
            if (isSelected) {
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = label,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = contentColor,
                    maxLines = 1
                )
            }
        }
    }
}
