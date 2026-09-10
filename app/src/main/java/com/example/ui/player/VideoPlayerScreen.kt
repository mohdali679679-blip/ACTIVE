package com.example.ui.player

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.material.icons.filled.FileDownloadDone
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.YouTubeDataRepository
import com.example.model.CommentItem
import com.example.model.VideoItem
import com.example.model.VideoQuality
import com.example.ui.components.VideoCard
import com.example.ui.theme.ActiveBrandRed
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.PrimeAmber
import com.example.ui.theme.YouTubeBlack
import com.example.ui.theme.YouTubeDarkSurface
import com.example.ui.theme.YouTubeElevatedSurface
import com.example.ui.theme.YouTubeTextMuted
import com.example.ui.theme.YouTubeTextPrimary
import com.example.ui.theme.YouTubeTextSecondary
import com.example.ui.theme.ZeroAdsGreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VideoPlayerScreen(
    video: VideoItem,
    onClose: () -> Unit,
    onRelatedVideoClick: (VideoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var isLiked by remember { mutableStateOf(false) }
    var isDisliked by remember { mutableStateOf(false) }
    var isSubscribed by remember { mutableStateOf(false) }
    var isDownloaded by remember { mutableStateOf(false) }
    var isSavedToPlaylist by remember { mutableStateOf(false) }
    var isDescriptionExpanded by remember { mutableStateOf(false) }

    // Smart Settings State
    var showSettingsSheet by remember { mutableStateOf(false) }
    var showCommentsSheet by remember { mutableStateOf(false) }
    var selectedQuality by remember { mutableStateOf(VideoQuality.AUTO) }
    var selectedSpeed by remember { mutableFloatStateOf(1.0f) }
    var isLooping by remember { mutableStateOf(false) }
    var ambientMode by remember { mutableStateOf(true) }

    val commentsList = remember { YouTubeDataRepository.sampleComments.toMutableList() }
    val relatedVideos = remember(video.id) {
        YouTubeDataRepository.videos.filter { it.id != video.id }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(YouTubeBlack)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Video Player View (16:9 Aspect Ratio) with Top Bar Controls
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(16f / 9f)
                    .background(Color.Black)
            ) {
                // Ambient Mode Glow
                if (ambientMode) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .blur(32.dp)
                            .background(
                                Brush.radialGradient(
                                    colors = listOf(
                                        ActiveBrandRed.copy(alpha = 0.25f),
                                        FacebookBlue.copy(alpha = 0.15f),
                                        Color.Transparent
                                    )
                                )
                            )
                    )
                }

                // YouTube Embedded Player
                YouTubePlayerView(
                    videoId = video.id,
                    playbackSpeed = selectedSpeed,
                    isLooping = isLooping,
                    modifier = Modifier.fillMaxSize()
                )

                // Top Player Header: Minimize + Speed Badge + Settings Gear
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = onClose,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0x99000000))
                    ) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Minimize Player",
                            tint = Color.White,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Current Speed Pill Badge
                        if (selectedSpeed != 1.0f) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(ActiveBrandRed)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "${String.format("%.2f", selectedSpeed)}x",
                                    color = Color.White,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        // Current Quality Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xCC111111))
                                .border(0.8.dp, Color(0x44FFFFFF), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = selectedQuality.badge,
                                color = Color.White,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Smart Playback Settings Gear
                        IconButton(
                            onClick = { showSettingsSheet = true },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color(0x99000000))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Playback Settings",
                                tint = Color.White,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }
            }

            // Scrollable Content Below Player
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) {
                // Title and Description Section
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp)
                    ) {
                        // Video Title
                        Text(
                            text = video.title,
                            color = YouTubeTextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 22.sp
                        )

                        // Views, Date, and Description expander
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp)
                                .clickable { isDescriptionExpanded = !isDescriptionExpanded },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${video.views} • ${video.publishedTime} • #ACTIVE",
                                color = YouTubeTextSecondary,
                                fontSize = 12.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isDescriptionExpanded) "less" else "...more",
                                color = YouTubeTextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }

                        // Expanded Description
                        AnimatedVisibility(visible = isDescriptionExpanded) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 8.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(YouTubeDarkSurface)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = video.description,
                                    color = YouTubeTextPrimary,
                                    fontSize = 13.sp,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                // Channel Info Bar
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            AsyncImage(
                                model = video.channelAvatar,
                                contentDescription = video.channelTitle,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                            )

                            Spacer(modifier = Modifier.width(10.dp))

                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = video.channelTitle,
                                        color = YouTubeTextPrimary,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    if (video.isVerified) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = "Verified",
                                            tint = YouTubeTextMuted,
                                            modifier = Modifier
                                                .padding(start = 4.dp)
                                                .size(13.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = video.subscribersCount,
                                    color = YouTubeTextSecondary,
                                    fontSize = 12.sp
                                )
                            }
                        }

                        // Subscribe Button
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(18.dp))
                                .background(if (isSubscribed) YouTubeDarkSurface else Color.White)
                                .clickable { isSubscribed = !isSubscribed }
                                .padding(horizontal = 14.dp, vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isSubscribed) {
                                    Icon(
                                        imageVector = Icons.Default.NotificationsActive,
                                        contentDescription = "Subscribed",
                                        tint = YouTubeTextPrimary,
                                        modifier = Modifier
                                            .padding(end = 4.dp)
                                            .size(16.dp)
                                    )
                                }
                                Text(
                                    text = if (isSubscribed) "Subscribed" else "Subscribe",
                                    color = if (isSubscribed) YouTubeTextPrimary else Color.Black,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }

                // Interactive Action Buttons Row (Like, Dislike, Share, Download, Remix)
                item {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Like & Dislike Pill
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(18.dp))
                                .background(YouTubeDarkSurface)
                                .padding(horizontal = 4.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                modifier = Modifier
                                    .clickable {
                                        isLiked = !isLiked
                                        if (isLiked) isDisliked = false
                                    }
                                    .padding(horizontal = 8.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ThumbUp,
                                    contentDescription = "Like",
                                    tint = if (isLiked) ActiveBrandRed else YouTubeTextPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isLiked) "129K" else video.likesCount,
                                    color = if (isLiked) ActiveBrandRed else YouTubeTextPrimary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .height(18.dp)
                                    .width(1.dp)
                                    .background(Color(0xFF383838))
                            )

                            IconButton(
                                onClick = {
                                    isDisliked = !isDisliked
                                    if (isDisliked) isLiked = false
                                },
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ThumbDown,
                                    contentDescription = "Dislike",
                                    tint = if (isDisliked) ActiveBrandRed else YouTubeTextPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        // Share Button
                        ActionPillButton(
                            icon = Icons.Default.Share,
                            label = "Share",
                            onClick = {
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, "Watch '${video.title}' ad-free on ACTIVE: ${video.videoUrl}")
                                }
                                context.startActivity(Intent.createChooser(shareIntent, "Share Video"))
                            }
                        )

                        // Download Button (Zero Ads)
                        ActionPillButton(
                            icon = if (isDownloaded) Icons.Default.FileDownloadDone else Icons.Default.FileDownload,
                            label = if (isDownloaded) "Downloaded" else "Download",
                            active = isDownloaded,
                            onClick = { isDownloaded = !isDownloaded }
                        )

                        // Remix / Clip Button
                        ActionPillButton(
                            icon = Icons.Default.ContentCut,
                            label = "Remix",
                            onClick = { /* Remix action */ }
                        )

                        // Save to Playlist
                        ActionPillButton(
                            icon = if (isSavedToPlaylist) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            label = "Save",
                            active = isSavedToPlaylist,
                            onClick = { isSavedToPlaylist = !isSavedToPlaylist }
                        )
                    }
                }

                // Comments Card Teaser
                item {
                    val topComment = commentsList.firstOrNull()
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(YouTubeDarkSurface)
                            .clickable { showCommentsSheet = true }
                            .padding(12.dp)
                    ) {
                        Column {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Comments ${video.commentsCount}",
                                    color = YouTubeTextPrimary,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Icon(
                                    imageVector = Icons.Default.KeyboardArrowDown,
                                    contentDescription = "Expand comments",
                                    tint = YouTubeTextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            if (topComment != null) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    AsyncImage(
                                        model = topComment.authorAvatar,
                                        contentDescription = topComment.authorName,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = topComment.text,
                                        color = YouTubeTextPrimary,
                                        fontSize = 12.sp,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Up Next Header
                item {
                    Text(
                        text = "Up Next (Ad-Free Continuous Play)",
                        color = YouTubeTextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
                    )
                }

                // Related Videos List
                items(relatedVideos, key = { it.id }) { related ->
                    VideoCard(
                        video = related,
                        onClick = { onRelatedVideoClick(related) },
                        onMenuClick = { /* Show options */ }
                    )
                }
            }
        }

        // Smart Playback Settings Bottom Sheet
        if (showSettingsSheet) {
            SmartPlaybackSettingsSheet(
                selectedQuality = selectedQuality,
                selectedSpeed = selectedSpeed,
                isLooping = isLooping,
                ambientMode = ambientMode,
                onQualitySelected = { selectedQuality = it },
                onSpeedSelected = { selectedSpeed = it },
                onLoopToggled = { isLooping = it },
                onAmbientToggled = { ambientMode = it },
                onDismiss = { showSettingsSheet = false }
            )
        }

        // Comments Bottom Sheet
        if (showCommentsSheet) {
            CommentsBottomSheet(
                comments = commentsList,
                onAddComment = { newText ->
                    commentsList.add(
                        0,
                        CommentItem(
                            id = "c_${System.currentTimeMillis()}",
                            authorName = "You",
                            authorAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100",
                            text = newText,
                            likes = "1",
                            timeAgo = "Just now"
                        )
                    )
                },
                onDismiss = { showCommentsSheet = false }
            )
        }
    }
}

@Composable
private fun ActionPillButton(
    icon: ImageVector,
    label: String,
    active: Boolean = false,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .clip(RoundedCornerShape(18.dp))
            .background(if (active) ActiveBrandRed else YouTubeDarkSurface)
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = if (active) Color.White else YouTubeTextPrimary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            color = if (active) Color.White else YouTubeTextPrimary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommentsBottomSheet(
    comments: List<CommentItem>,
    onAddComment: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var commentInput by remember { mutableStateOf("") }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = Color(0xFF16171D)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Comments (${comments.size})",
                    color = YouTubeTextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                IconButton(
                    onClick = onDismiss,
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

            // Input field
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = commentInput,
                    onValueChange = { commentInput = it },
                    placeholder = { Text("Add a comment...", color = YouTubeTextMuted, fontSize = 13.sp) },
                    modifier = Modifier.weight(1f),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = ActiveBrandRed,
                        unfocusedBorderColor = YouTubeElevatedSurface
                    ),
                    shape = RoundedCornerShape(24.dp)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(if (commentInput.isNotBlank()) ActiveBrandRed else YouTubeDarkSurface)
                        .clickable(enabled = commentInput.isNotBlank()) {
                            onAddComment(commentInput)
                            commentInput = ""
                        }
                        .padding(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Send",
                        tint = if (commentInput.isNotBlank()) Color.White else YouTubeTextMuted,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            // Comments List
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
                    .padding(horizontal = 16.dp)
            ) {
                items(comments, key = { it.id }) { comment ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        AsyncImage(
                            model = comment.authorAvatar,
                            contentDescription = comment.authorName,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                        )

                        Spacer(modifier = Modifier.width(10.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = comment.authorName,
                                    color = YouTubeTextSecondary,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = comment.timeAgo,
                                    color = YouTubeTextMuted,
                                    fontSize = 11.sp
                                )
                            }

                            Text(
                                text = comment.text,
                                color = YouTubeTextPrimary,
                                fontSize = 13.sp,
                                modifier = Modifier.padding(top = 2.dp),
                                lineHeight = 18.sp
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(top = 6.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ThumbUp,
                                    contentDescription = "Likes",
                                    tint = YouTubeTextSecondary,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = comment.likes,
                                    color = YouTubeTextSecondary,
                                    fontSize = 11.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
