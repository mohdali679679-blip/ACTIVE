package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.YouTubeDataRepository
import com.example.model.ShortItem
import com.example.model.VideoItem
import com.example.ui.components.ShortsShelf
import com.example.ui.components.VideoCard
import com.example.ui.theme.YouTubeBlack
import com.example.ui.theme.YouTubeChipBackground
import com.example.ui.theme.YouTubeChipSelected
import com.example.ui.theme.YouTubeChipSelectedText
import com.example.ui.theme.YouTubeTextPrimary
import com.example.ui.theme.YouTubeTextSecondary
import com.example.ui.theme.ZeroAdsGreen

@Composable
fun HomeScreen(
    onVideoClick: (VideoItem) -> Unit,
    onShortClick: (ShortItem) -> Unit,
    onVideoMenuClick: (VideoItem) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedCategory by remember { mutableStateOf("All") }
    val displayVideos = remember(selectedCategory) {
        YouTubeDataRepository.getByCategory(selectedCategory)
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(YouTubeBlack)
    ) {
        // Horizontally Scrollable Category Filter Chips (YouTube style)
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            contentPadding = PaddingValues(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Explore Compass Pill
            item {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(YouTubeChipBackground)
                        .clickable { selectedCategory = "Trending" }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Explore,
                        contentDescription = "Explore",
                        tint = YouTubeTextPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }

            items(YouTubeDataRepository.categories) { category ->
                val isSelected = category == selectedCategory
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isSelected) YouTubeChipSelected else YouTubeChipBackground)
                        .clickable { selectedCategory = category }
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = category,
                        color = if (isSelected) YouTubeChipSelectedText else YouTubeTextPrimary,
                        fontSize = 13.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                }
            }
        }

        // Home Feed List
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            // Zero-Ads Active Floating Banner (authentic ACTIVE feature)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF141A17))
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = "Shield",
                        tint = ZeroAdsGreen,
                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = "ACTIVE Zero-Ads Shield is running • 100% pure video streams",
                        color = ZeroAdsGreen,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            // First few videos
            val firstBatch = displayVideos.take(2)
            items(firstBatch, key = { it.id }) { video ->
                VideoCard(
                    video = video,
                    onClick = { onVideoClick(video) },
                    onMenuClick = { onVideoMenuClick(video) }
                )
            }

            // Embedded Shorts Shelf (Authentic YouTube experience)
            item {
                ShortsShelf(
                    shorts = YouTubeDataRepository.shorts,
                    onShortClick = onShortClick
                )
            }

            // Remaining videos
            val remainingVideos = displayVideos.drop(2)
            items(remainingVideos, key = { it.id }) { video ->
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
