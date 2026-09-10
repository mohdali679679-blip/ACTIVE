package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Comment
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.YouTubeDataRepository
import com.example.model.ShortItem
import com.example.ui.player.CommentsBottomSheet
import com.example.ui.player.YouTubePlayerView
import com.example.ui.theme.ActiveBrandRed
import com.example.ui.theme.YouTubeTextPrimary
import com.example.ui.theme.YouTubeTextSecondary
import com.example.ui.theme.ZeroAdsGreen

@Composable
fun ShortsScreen(
    initialShort: ShortItem? = null,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val shortsList = remember { YouTubeDataRepository.shorts }
    var currentIndex by remember {
        mutableIntStateOf(
            if (initialShort != null) shortsList.indexOfFirst { it.id == initialShort.id }.coerceAtLeast(0) else 0
        )
    }

    val currentShort = shortsList.getOrElse(currentIndex) { shortsList.first() }

    var isLiked by remember(currentShort.id) { mutableStateOf(false) }
    var isDisliked by remember(currentShort.id) { mutableStateOf(false) }
    var isSubscribed by remember(currentShort.id) { mutableStateOf(false) }
    var showCommentsSheet by remember { mutableStateOf(false) }

    // Vinyl record rotation
    val infiniteTransition = rememberInfiniteTransition(label = "vinyl")
    val rotation by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "rotation"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    if (dragAmount < -50) {
                        // Swipe up -> next short
                        if (currentIndex < shortsList.lastIndex) currentIndex++
                    } else if (dragAmount > 50) {
                        // Swipe down -> previous short
                        if (currentIndex > 0) currentIndex--
                    }
                }
            }
    ) {
        // Embedded Player View
        YouTubePlayerView(
            videoId = currentShort.id,
            playbackSpeed = 1.0f,
            isLooping = true,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient Scrim for readable text
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0x66000000),
                            Color.Transparent,
                            Color.Transparent,
                            Color(0xDD000000)
                        )
                    )
                )
        )

        // Top Zero-Ads Shield Indicator
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(start = 14.dp, top = 8.dp)
                .clip(RoundedCornerShape(6.dp))
                .background(Color(0x990A0B10))
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Shield,
                contentDescription = "Zero Ads",
                tint = ZeroAdsGreen,
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "ACTIVE Shorts • Zero Ads",
                color = ZeroAdsGreen,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
        }

        // Right-side Action Sidebar (Like, Dislike, Comments, Share, Remix, Sound Disc)
        Column(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 12.dp, bottom = 80.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Like
            ShortActionItem(
                icon = Icons.Default.ThumbUp,
                label = if (isLiked) "Liked" else currentShort.likesCount,
                tint = if (isLiked) ActiveBrandRed else Color.White,
                onClick = {
                    isLiked = !isLiked
                    if (isLiked) isDisliked = false
                }
            )

            // Dislike
            ShortActionItem(
                icon = Icons.Default.ThumbDown,
                label = "Dislike",
                tint = if (isDisliked) ActiveBrandRed else Color.White,
                onClick = {
                    isDisliked = !isDisliked
                    if (isDisliked) isLiked = false
                }
            )

            // Comments
            ShortActionItem(
                icon = Icons.Default.Comment,
                label = currentShort.commentsCount,
                tint = Color.White,
                onClick = { showCommentsSheet = true }
            )

            // Share
            ShortActionItem(
                icon = Icons.Default.Share,
                label = "Share",
                tint = Color.White,
                onClick = {
                    val shareIntent = Intent(Intent.ACTION_SEND).apply {
                        type = "text/plain"
                        putExtra(Intent.EXTRA_TEXT, "Watch '${currentShort.title}' ad-free on ACTIVE: https://youtube.com/shorts/${currentShort.id}")
                    }
                    context.startActivity(Intent.createChooser(shareIntent, "Share Short"))
                }
            )

            // Remix
            ShortActionItem(
                icon = Icons.Default.ContentCut,
                label = "Remix",
                tint = Color.White,
                onClick = { /* Remix */ }
            )

            // Spinning Vinyl Music Disc
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF222222))
                    .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = currentShort.channelAvatar,
                    contentDescription = "Sound Disc",
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                        .rotate(rotation)
                )
            }
        }

        // Bottom Details Overlay: Channel info, Subscribe, Caption, Sound Ticker
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth(0.8f)
                .padding(start = 14.dp, bottom = 80.dp)
        ) {
            // Channel Info + Subscribe Button
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                AsyncImage(
                    model = currentShort.channelAvatar,
                    contentDescription = currentShort.channelTitle,
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                )

                Text(
                    text = "@${currentShort.channelTitle}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(if (isSubscribed) Color(0x66000000) else ActiveBrandRed)
                        .clickable { isSubscribed = !isSubscribed }
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = if (isSubscribed) "Subscribed" else "Subscribe",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Caption
            Text(
                text = currentShort.title,
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Sound Title Ticker
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0x55000000))
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.MusicNote,
                    contentDescription = "Music",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = currentShort.musicTitle,
                    color = Color.White,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        // Comments Bottom Sheet
        if (showCommentsSheet) {
            CommentsBottomSheet(
                comments = YouTubeDataRepository.sampleComments,
                onAddComment = { /* Add */ },
                onDismiss = { showCommentsSheet = false }
            )
        }
    }
}

@Composable
private fun ShortActionItem(
    icon: ImageVector,
    label: String,
    tint: Color,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0x33000000)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = tint,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = label,
            color = Color.White,
            fontSize = 11.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}
