package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.YouTubeDataRepository
import com.example.model.ChannelItem
import com.example.model.VideoItem
import com.example.ui.components.VideoCard
import com.example.ui.theme.ActiveBrandRed
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.YouTubeBlack
import com.example.ui.theme.YouTubeChipBackground
import com.example.ui.theme.YouTubeChipSelected
import com.example.ui.theme.YouTubeChipSelectedText
import com.example.ui.theme.YouTubeTextPrimary
import com.example.ui.theme.YouTubeTextSecondary

@Composable
fun SubscriptionsScreen(
    onVideoClick: (VideoItem) -> Unit,
    onVideoMenuClick: (VideoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Today", "Live", "Videos", "Shorts", "Unwatched")
    val channels = remember { YouTubeDataRepository.sampleChannels }
    val videos = remember { YouTubeDataRepository.videos }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(YouTubeBlack)
    ) {
        // Channel Story Circles Row (YouTube authentic style)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(channels, key = { it.id }) { channel ->
                ChannelStoryItem(channel = channel, onClick = { /* Filter by channel */ })
            }

            // "All" manager button
            item {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.clickable { /* Manage subscriptions */ }
                ) {
                    Box(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF222222)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "All",
                            color = FacebookBlue,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Manage",
                        color = YouTubeTextSecondary,
                        fontSize = 11.sp
                    )
                }
            }
        }

        // Filter chips row
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filters) { filter ->
                val isSelected = filter == selectedFilter
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) YouTubeChipSelected else YouTubeChipBackground)
                        .clickable { selectedFilter = filter }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = filter,
                        color = if (isSelected) YouTubeChipSelectedText else YouTubeTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        // Subscriptions Video Feed
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(videos, key = { it.id }) { video ->
                VideoCard(
                    video = video,
                    onClick = { onVideoClick(video) },
                    onMenuClick = { onVideoMenuClick(video) }
                )
            }

            item {
                Spacer(modifier = Modifier.height(72.dp))
            }
        }
    }
}

@Composable
private fun ChannelStoryItem(
    channel: ChannelItem,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier.size(56.dp),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = channel.avatar,
                contentDescription = channel.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(52.dp)
                    .clip(CircleShape)
                    .border(
                        width = if (channel.isLive) 2.dp else if (channel.hasUnread) 1.5.dp else 0.dp,
                        color = if (channel.isLive) ActiveBrandRed else if (channel.hasUnread) FacebookBlue else Color.Transparent,
                        shape = CircleShape
                    )
            )

            // Live badge or unread blue dot
            if (channel.isLive) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .clip(RoundedCornerShape(3.dp))
                        .background(ActiveBrandRed)
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text("LIVE", color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Black)
                }
            } else if (channel.hasUnread) {
                Box(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .size(10.dp)
                        .clip(CircleShape)
                        .background(FacebookBlue)
                        .border(1.dp, YouTubeBlack, CircleShape)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = channel.name,
            color = YouTubeTextPrimary,
            fontSize = 11.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Center,
            modifier = Modifier.width(60.dp)
        )
    }
}
