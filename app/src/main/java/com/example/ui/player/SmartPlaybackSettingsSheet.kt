package com.example.ui.player

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GraphicEq
import androidx.compose.material.icons.filled.HighQuality
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NightlightRound
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PlaybackSpeed
import com.example.model.VideoQuality
import com.example.ui.theme.ActiveBrandRed
import com.example.ui.theme.YouTubeDarkSurface
import com.example.ui.theme.YouTubeElevatedSurface
import com.example.ui.theme.YouTubeTextMuted
import com.example.ui.theme.YouTubeTextPrimary
import com.example.ui.theme.YouTubeTextSecondary
import com.example.ui.theme.ZeroAdsGreen
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SmartPlaybackSettingsSheet(
    selectedQuality: VideoQuality,
    selectedSpeed: Float,
    isLooping: Boolean,
    ambientMode: Boolean,
    onQualitySelected: (VideoQuality) -> Unit,
    onSpeedSelected: (Float) -> Unit,
    onLoopToggled: (Boolean) -> Unit,
    onAmbientToggled: (Boolean) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var activeSubmenu by remember { mutableStateOf<SettingsSubmenu?>(null) }
    var fineSpeed by remember { mutableFloatStateOf(selectedSpeed) }
    var sleepTimer by remember { mutableStateOf("Off") }
    var showStatsForNerds by remember { mutableStateOf(false) }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF16171D),
        dragHandle = null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp)
                .verticalScroll(rememberScrollState())
        ) {
            // Header Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Tune,
                        contentDescription = "Settings",
                        tint = YouTubeTextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = when (activeSubmenu) {
                            SettingsSubmenu.QUALITY -> "Video Quality"
                            SettingsSubmenu.SPEED -> "Playback Speed"
                            null -> "Playback Settings"
                        },
                        color = YouTubeTextPrimary,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                IconButton(
                    onClick = {
                        if (activeSubmenu != null) activeSubmenu = null else onDismiss()
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Close",
                        tint = YouTubeTextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Zero Ads Shield Active Callout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(ZeroAdsGreen.copy(alpha = 0.12f))
                    .border(1.dp, ZeroAdsGreen.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Ad Shield",
                    tint = ZeroAdsGreen,
                    modifier = Modifier.size(24.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = "Zero-Ads Shield Active",
                        color = ZeroAdsGreen,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "100% ad-free playback enabled • Continuous stream",
                        color = YouTubeTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            when (activeSubmenu) {
                SettingsSubmenu.QUALITY -> {
                    // Quality Selection List
                    Text(
                        text = "Select streaming resolution",
                        color = YouTubeTextMuted,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                    )

                    VideoQuality.values().forEach { quality ->
                        val isSelected = quality == selectedQuality
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onQualitySelected(quality)
                                    activeSubmenu = null
                                }
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(if (isSelected) ActiveBrandRed else YouTubeDarkSurface)
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(
                                        text = quality.badge,
                                        color = if (isSelected) Color.White else YouTubeTextSecondary,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }

                                Spacer(modifier = Modifier.width(12.dp))

                                Text(
                                    text = quality.label,
                                    color = if (isSelected) Color.White else YouTubeTextPrimary,
                                    fontSize = 14.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Selected",
                                    tint = ActiveBrandRed,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                }

                SettingsSubmenu.SPEED -> {
                    // Smart Playback Speed Screen with presets and fine-tuning slider
                    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                        Text(
                            text = "Current Speed: ${String.format("%.2f", fineSpeed)}x",
                            color = YouTubeTextPrimary,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Fine speed slider
                        Slider(
                            value = fineSpeed,
                            onValueChange = {
                                fineSpeed = (it * 20).roundToInt() / 20f
                                onSpeedSelected(fineSpeed)
                            },
                            valueRange = 0.25f..2.5f,
                            colors = SliderDefaults.colors(
                                thumbColor = ActiveBrandRed,
                                activeTrackColor = ActiveBrandRed,
                                inactiveTrackColor = YouTubeElevatedSurface
                            )
                        )

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("0.25x", color = YouTubeTextMuted, fontSize = 11.sp)
                            Text("1.0x Normal", color = YouTubeTextMuted, fontSize = 11.sp)
                            Text("2.5x Max", color = YouTubeTextMuted, fontSize = 11.sp)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Quick Presets",
                            color = YouTubeTextSecondary,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Grid of speed pills
                        PlaybackSpeed.ALL.chunked(4).forEach { row ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                row.forEach { speedItem ->
                                    val isSelected = (fineSpeed - speedItem.speed) in -0.05f..0.05f
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) ActiveBrandRed else YouTubeDarkSurface)
                                            .clickable {
                                                fineSpeed = speedItem.speed
                                                onSpeedSelected(speedItem.speed)
                                            }
                                            .padding(vertical = 10.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = speedItem.label.replace(" (1.0x)", ""),
                                            color = if (isSelected) Color.White else YouTubeTextPrimary,
                                            fontSize = 13.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                null -> {
                    // Main Settings Menu
                    SettingsOptionRow(
                        icon = Icons.Default.HighQuality,
                        title = "Quality",
                        value = selectedQuality.label,
                        onClick = { activeSubmenu = SettingsSubmenu.QUALITY }
                    )

                    SettingsOptionRow(
                        icon = Icons.Default.Speed,
                        title = "Playback Speed",
                        value = if (selectedSpeed == 1.0f) "Normal" else "${String.format("%.2f", selectedSpeed)}x",
                        onClick = { activeSubmenu = SettingsSubmenu.SPEED }
                    )

                    // Sleep Timer
                    SettingsOptionRow(
                        icon = Icons.Default.NightlightRound,
                        title = "Sleep Timer",
                        value = sleepTimer,
                        onClick = {
                            sleepTimer = when (sleepTimer) {
                                "Off" -> "15 min"
                                "15 min" -> "30 min"
                                "30 min" -> "45 min"
                                "45 min" -> "60 min"
                                "60 min" -> "End of video"
                                else -> "Off"
                            }
                        }
                    )

                    // Loop Video Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Repeat,
                                contentDescription = "Loop",
                                tint = YouTubeTextPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = "Loop video",
                                color = YouTubeTextPrimary,
                                fontSize = 14.sp
                            )
                        }

                        Switch(
                            checked = isLooping,
                            onCheckedChange = onLoopToggled,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ActiveBrandRed,
                                uncheckedTrackColor = YouTubeDarkSurface
                            )
                        )
                    }

                    // Ambient Lighting Toggle
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.GraphicEq,
                                contentDescription = "Ambient",
                                tint = YouTubeTextPrimary,
                                modifier = Modifier.size(22.dp)
                            )
                            Spacer(modifier = Modifier.width(14.dp))
                            Text(
                                text = "Ambient Mode (Atmosphere Glow)",
                                color = YouTubeTextPrimary,
                                fontSize = 14.sp
                            )
                        }

                        Switch(
                            checked = ambientMode,
                            onCheckedChange = onAmbientToggled,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = ActiveBrandRed,
                                uncheckedTrackColor = YouTubeDarkSurface
                            )
                        )
                    }

                    // Stats for nerds toggle
                    SettingsOptionRow(
                        icon = Icons.Default.Info,
                        title = "Stats for nerds",
                        value = if (showStatsForNerds) "Visible" else "Hidden",
                        onClick = { showStatsForNerds = !showStatsForNerds }
                    )

                    if (showStatsForNerds) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0D0E12))
                                .padding(12.dp)
                        ) {
                            Column {
                                Text("Viewport / Frames: 1080x608 / 0 dropped", color = ZeroAdsGreen, fontSize = 11.sp)
                                Text("Current / Optimal Res: ${selectedQuality.resolution} / ${selectedQuality.resolution}", color = ZeroAdsGreen, fontSize = 11.sp)
                                Text("Codecs: vp09.00.51.08.01.01.01.01.00 (303) / mp4a.40.2 (140)", color = YouTubeTextSecondary, fontSize = 10.sp)
                                Text("Bandwidth / Buffer Health: 62.4 Mbps / 24.8s", color = YouTubeTextSecondary, fontSize = 10.sp)
                                Text("ACTIVE Engine: Ad-Shield 100% Intercept Active", color = ZeroAdsGreen, fontSize = 11.sp)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

private enum class SettingsSubmenu {
    QUALITY, SPEED
}

@Composable
private fun SettingsOptionRow(
    icon: ImageVector,
    title: String,
    value: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = YouTubeTextPrimary,
                modifier = Modifier.size(22.dp)
            )
            Spacer(modifier = Modifier.width(14.dp))
            Text(
                text = title,
                color = YouTubeTextPrimary,
                fontSize = 14.sp
            )
        }

        Text(
            text = value,
            color = YouTubeTextSecondary,
            fontSize = 13.sp
        )
    }
}
