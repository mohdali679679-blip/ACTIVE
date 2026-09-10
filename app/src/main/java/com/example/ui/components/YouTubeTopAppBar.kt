package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cast
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.ActiveBrandRed
import com.example.ui.theme.YouTubeBlack
import com.example.ui.theme.YouTubeTextPrimary
import com.example.ui.theme.ZeroAdsGreen

@Composable
fun YouTubeTopAppBar(
    onSearchClick: () -> Unit,
    onCastClick: () -> Unit,
    onNotificationsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onShieldClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(YouTubeBlack)
            .statusBarsPadding()
            .height(56.dp)
            .padding(horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Left: ACTIVE Brand + Logo + Zero Ads Shield
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.clickable { onShieldClick() }
        ) {
            ActiveLogoHeader(badgeSize = 28.dp, showAdFreeBadge = true)
        }

        // Right Icons: Cast, Notifications, Search, Profile
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            // Ad-Shield Status Quick Button
            IconButton(
                onClick = onShieldClick,
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Zero Ads Shield",
                    tint = ZeroAdsGreen,
                    modifier = Modifier.size(20.dp)
                )
            }

            // Cast Button
            IconButton(
                onClick = onCastClick,
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Cast,
                    contentDescription = "Cast to Device",
                    tint = YouTubeTextPrimary,
                    modifier = Modifier.size(22.dp)
                )
            }

            // Notifications with Badge
            IconButton(
                onClick = onNotificationsClick,
                modifier = Modifier.size(38.dp)
            ) {
                BadgedBox(
                    badge = {
                        Badge(
                            containerColor = ActiveBrandRed,
                            contentColor = Color.White
                        ) {
                            Text(text = "3", fontSize = 10.sp)
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.NotificationsNone,
                        contentDescription = "Notifications",
                        tint = YouTubeTextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            // Search Icon
            IconButton(
                onClick = onSearchClick,
                modifier = Modifier.size(38.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search YouTube",
                    tint = YouTubeTextPrimary,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Profile Avatar
            Box(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(28.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF333333))
                    .clickable { onProfileClick() },
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=100",
                    contentDescription = "Account",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.clip(CircleShape)
                )
            }
        }
    }
}
