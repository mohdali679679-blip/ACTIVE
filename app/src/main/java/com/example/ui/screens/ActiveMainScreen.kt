package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoNotDisturb
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.Subscriptions
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material.icons.filled.WatchLater
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.FlashOn
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Subscriptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.model.ShortItem
import com.example.model.VideoItem
import com.example.ui.components.ActiveLogoHeader
import com.example.ui.components.AppOpenAnimationScreen
import com.example.ui.components.YouTubeTopAppBar
import com.example.ui.player.VideoPlayerScreen
import com.example.ui.theme.ActiveBrandRed
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.PrimeAmber
import com.example.ui.theme.YouTubeBlack
import com.example.ui.theme.YouTubeDarkSurface
import com.example.ui.theme.YouTubeElevatedSurface
import com.example.ui.theme.YouTubeTextPrimary
import com.example.ui.theme.YouTubeTextSecondary
import com.example.ui.theme.ZeroAdsGreen
import kotlinx.coroutines.launch

enum class MainTab(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
) {
    HOME("Home", Icons.Filled.Home, Icons.Outlined.Home),
    SHORTS("Shorts", Icons.Filled.FlashOn, Icons.Outlined.FlashOn),
    SUBSCRIPTIONS("Subscriptions", Icons.Filled.Subscriptions, Icons.Outlined.Subscriptions),
    YOU("You", Icons.Filled.AccountCircle, Icons.Outlined.AccountCircle)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveMainScreen() {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    // App Open Animation state
    var showAppOpenAnimation by remember { mutableStateOf(true) }

    // Navigation & Screen states
    var currentTab by remember { mutableStateOf(MainTab.HOME) }
    var isSearchActive by remember { mutableStateOf(false) }

    // Active Video Player Overlay State
    var activeVideo by remember { mutableStateOf<VideoItem?>(null) }
    var activeShortForPlayer by remember { mutableStateOf<ShortItem?>(null) }

    // Modals & Dialogs
    var selectedVideoForMenu by remember { mutableStateOf<VideoItem?>(null) }
    var showCastDialog by remember { mutableStateOf(false) }
    var showNotificationsSheet by remember { mutableStateOf(false) }
    var showProfileDialog by remember { mutableStateOf(false) }
    var showShieldInfoDialog by remember { mutableStateOf(false) }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            topBar = {
                // Top App Bar visible on Home, Subscriptions, You (Shorts has its own immersive overlay)
                if (currentTab != MainTab.SHORTS && !isSearchActive) {
                    YouTubeTopAppBar(
                        onSearchClick = { isSearchActive = true },
                        onCastClick = { showCastDialog = true },
                        onNotificationsClick = { showNotificationsSheet = true },
                        onProfileClick = { showProfileDialog = true },
                        onShieldClick = { showShieldInfoDialog = true }
                    )
                }
            },
            bottomBar = {
                if (activeVideo == null && !isSearchActive) {
                    NavigationBar(
                        containerColor = YouTubeBlack,
                        contentColor = YouTubeTextPrimary,
                        tonalElevation = 0.dp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .navigationBarsPadding()
                            .height(54.dp)
                    ) {
                        MainTab.values().forEach { tab ->
                            val isSelected = currentTab == tab
                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { currentTab = tab },
                                icon = {
                                    Icon(
                                        imageVector = if (isSelected) tab.selectedIcon else tab.unselectedIcon,
                                        contentDescription = tab.title,
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = tab.title,
                                        fontSize = 10.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = YouTubeTextPrimary,
                                    selectedTextColor = YouTubeTextPrimary,
                                    unselectedIconColor = YouTubeTextSecondary,
                                    unselectedTextColor = YouTubeTextSecondary,
                                    indicatorColor = Color.Transparent
                                )
                            )
                        }
                    }
                }
            },
            containerColor = YouTubeBlack
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                when (currentTab) {
                    MainTab.HOME -> {
                        HomeScreen(
                            onVideoClick = { video -> activeVideo = video },
                            onShortClick = { short ->
                                activeShortForPlayer = short
                                currentTab = MainTab.SHORTS
                            },
                            onVideoMenuClick = { video -> selectedVideoForMenu = video }
                        )
                    }

                    MainTab.SHORTS -> {
                        ShortsScreen(initialShort = activeShortForPlayer)
                    }

                    MainTab.SUBSCRIPTIONS -> {
                        SubscriptionsScreen(
                            onVideoClick = { video -> activeVideo = video },
                            onVideoMenuClick = { video -> selectedVideoForMenu = video }
                        )
                    }

                    MainTab.YOU -> {
                        LibraryScreen(
                            onVideoClick = { video -> activeVideo = video },
                            onSettingsClick = { showShieldInfoDialog = true }
                        )
                    }
                }
            }
        }

        // Search Screen Overlay
        AnimatedVisibility(
            visible = isSearchActive,
            enter = fadeIn() + slideInVertically { it / 4 },
            exit = fadeOut() + slideOutVertically { it / 4 }
        ) {
            SearchScreen(
                onBack = { isSearchActive = false },
                onVideoClick = { video ->
                    isSearchActive = false
                    activeVideo = video
                }
            )
        }

        // Full Screen Video Player Overlay
        AnimatedVisibility(
            visible = activeVideo != null,
            enter = slideInVertically { it },
            exit = slideOutVertically { it }
        ) {
            activeVideo?.let { currentPlayingVideo ->
                VideoPlayerScreen(
                    video = currentPlayingVideo,
                    onClose = { activeVideo = null },
                    onRelatedVideoClick = { newVideo -> activeVideo = newVideo }
                )
            }
        }

        // App Open Animation Overlay
        if (showAppOpenAnimation) {
            AppOpenAnimationScreen(
                onAnimationFinished = { showAppOpenAnimation = false }
            )
        }

        // Video 3-dots Menu Bottom Sheet
        selectedVideoForMenu?.let { video ->
            VideoActionBottomSheet(
                video = video,
                onDismiss = { selectedVideoForMenu = null },
                onAction = { action ->
                    selectedVideoForMenu = null
                    coroutineScope.launch {
                        snackbarHostState.showSnackbar(action)
                    }
                }
            )
        }

        // Cast to Device Dialog
        if (showCastDialog) {
            CastDeviceDialog(onDismiss = { showCastDialog = false })
        }

        // Notifications Bottom Sheet
        if (showNotificationsSheet) {
            NotificationsBottomSheet(onDismiss = { showNotificationsSheet = false })
        }

        // Profile Dialog
        if (showProfileDialog) {
            ProfileAccountDialog(onDismiss = { showProfileDialog = false })
        }

        // Zero-Ads Shield Info Dialog
        if (showShieldInfoDialog) {
            ZeroAdsShieldDialog(onDismiss = { showShieldInfoDialog = false })
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VideoActionBottomSheet(
    video: VideoItem,
    onDismiss: () -> Unit,
    onAction: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF1B1D25)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = video.thumbnailUrl,
                    contentDescription = video.title,
                    modifier = Modifier
                        .size(48.dp)
                        .clip(RoundedCornerShape(6.dp))
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = video.title,
                        color = YouTubeTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1
                    )
                    Text(
                        text = video.channelTitle,
                        color = YouTubeTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }

            Divider(color = YouTubeElevatedSurface, modifier = Modifier.padding(vertical = 8.dp))

            ActionSheetItem(
                icon = Icons.Default.WatchLater,
                title = "Save to Watch later",
                onClick = { onAction("Saved to Watch later") }
            )

            ActionSheetItem(
                icon = Icons.Default.BookmarkBorder,
                title = "Save to playlist",
                onClick = { onAction("Saved to playlist") }
            )

            ActionSheetItem(
                icon = Icons.Default.FileDownload,
                title = "Download video (Zero Ads)",
                onClick = { onAction("Downloaded '${video.title}' offline") }
            )

            ActionSheetItem(
                icon = Icons.Default.Share,
                title = "Share",
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Watch '${video.title}' ad-free on ACTIVE: ${video.videoUrl}")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Video"))
                    onDismiss()
                }
            )

            ActionSheetItem(
                icon = Icons.Default.DoNotDisturb,
                title = "Not interested",
                onClick = { onAction("Video hidden from recommendations") }
            )
        }
    }
}

@Composable
private fun ActionSheetItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = YouTubeTextPrimary,
            modifier = Modifier.size(22.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(
            text = title,
            color = YouTubeTextPrimary,
            fontSize = 14.sp
        )
    }
}

@Composable
private fun CastDeviceDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1E212A),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Cast, contentDescription = "Cast", tint = YouTubeTextPrimary)
                Spacer(modifier = Modifier.width(10.dp))
                Text("Connect to a device", color = YouTubeTextPrimary, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                CastDeviceRow("Living Room Android TV", "Smart Cast Ready")
                CastDeviceRow("Bedroom Chromecast 4K", "Ultra HD • Ad-Free")
                CastDeviceRow("ACTIVE Wireless Stream", "Direct Wi-Fi")
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = FacebookBlue)
            }
        }
    )
}

@Composable
private fun CastDeviceRow(name: String, status: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(YouTubeDarkSurface)
            .clickable { /* Select device */ }
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Tv, contentDescription = name, tint = Color.White, modifier = Modifier.size(22.dp))
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(name, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Text(status, color = ZeroAdsGreen, fontSize = 11.sp)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NotificationsBottomSheet(onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF181A22)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Notifications", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(12.dp))
            NotificationItemRow("MKBHD uploaded: Apple Vision Pro Review", "2 hours ago")
            NotificationItemRow("Veritasium uploaded: The Collatz Conjecture solved?", "1 day ago")
            NotificationItemRow("ACTIVE Zero-Ads Shield updated: 42 new tracking ads blocked", "3 days ago")
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
private fun NotificationItemRow(title: String, time: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(ActiveBrandRed),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Notifications, contentDescription = "Alert", tint = Color.White, modifier = Modifier.size(18.dp))
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(title, color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Medium)
            Text(time, color = YouTubeTextSecondary, fontSize = 11.sp)
        }
    }
}

@Composable
private fun ProfileAccountDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF1B1D26),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100",
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Alex Rivers", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Text("alex.rivers@gmail.com", color = YouTubeTextSecondary, fontSize = 12.sp)
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("• ACTIVE VIP Pass: Unlimited Ad-Free", color = ZeroAdsGreen, fontSize = 13.sp, fontWeight = FontWeight.Bold)
                Text("• Background Audio Playback: Enabled", color = Color.White, fontSize = 13.sp)
                Text("• Sponsor Auto-Skipping: Active", color = Color.White, fontSize = 13.sp)
                Text("• Connected to official YouTube API", color = YouTubeTextSecondary, fontSize = 12.sp)
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Done", color = FacebookBlue, fontWeight = FontWeight.Bold)
            }
        }
    )
}

@Composable
private fun ZeroAdsShieldDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = Color(0xFF141720),
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Shield, contentDescription = "Shield", tint = ZeroAdsGreen, modifier = Modifier.size(28.dp))
                Spacer(modifier = Modifier.width(10.dp))
                Text("ACTIVE Zero-Ads Shield", color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Zero Advertisements Guaranteed",
                    color = ZeroAdsGreen,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "ACTIVE intercepts video streams directly from the YouTube API, filtering out pre-roll, mid-roll, post-roll, overlay ads, banners, and promotional trackers in real-time.\n\nEnjoy continuous 4K/1080p video, background audio playback, and custom playback speeds smoothly.",
                    color = YouTubeTextSecondary,
                    fontSize = 13.sp,
                    lineHeight = 18.sp
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Got it", color = ZeroAdsGreen, fontWeight = FontWeight.Bold)
            }
        }
    )
}
