package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.ui.theme.ActiveBrandRed
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.NetflixRed
import com.example.ui.theme.PrimeAmber
import com.example.ui.theme.YouTubeBlack
import com.example.ui.theme.ZeroAdsGreen

/**
 * Hybrid Logo combining:
 * 1. YouTube: Red play rounded rectangle & pure white play triangle
 * 2. Netflix: Deep cinematic red vertical ribbons & gradient depth
 * 3. Amazon Prime: Signature curved smile arrow swoosh underneath
 * 4. Facebook: Royal blue outer squircle border accent
 */
@Composable
fun ActiveHybridBadge(
    modifier: Modifier = Modifier,
    size: Dp = 36.dp
) {
    Box(
        modifier = modifier
            .size(size)
            .clip(RoundedCornerShape(size * 0.26f))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0xFF1E212D),
                        YouTubeBlack,
                        Color(0xFF0D111A)
                    )
                )
            )
            .border(
                width = 1.5.dp,
                brush = Brush.sweepGradient(
                    listOf(
                        FacebookBlue,
                        ActiveBrandRed,
                        NetflixRed,
                        PrimeAmber,
                        FacebookBlue
                    )
                ),
                shape = RoundedCornerShape(size * 0.26f)
            ),
        contentAlignment = Alignment.Center
    ) {
        // Try rendering the high-res generated asset
        Image(
            painter = painterResource(id = R.drawable.img_active_logo),
            contentDescription = "ACTIVE Hybrid Logo",
            modifier = Modifier
                .fillMaxSize()
                .padding(size * 0.08f)
                .clip(RoundedCornerShape(size * 0.2f)),
            contentScale = ContentScale.Crop
        )

        // Vector overlay with dynamic Prime smile curve and play icon
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height

            // Amazon Prime curved smile arrow at the bottom
            val smilePath = Path().apply {
                moveTo(w * 0.22f, h * 0.78f)
                quadraticTo(
                    w * 0.52f, h * 0.92f,
                    w * 0.82f, h * 0.76f
                )
            }
            drawPath(
                path = smilePath,
                color = PrimeAmber,
                style = Stroke(width = w * 0.07f, cap = StrokeCap.Round)
            )

            // Arrow tip for prime smile
            val arrowHead = Path().apply {
                moveTo(w * 0.74f, h * 0.72f)
                lineTo(w * 0.84f, h * 0.76f)
                lineTo(w * 0.80f, h * 0.86f)
            }
            drawPath(
                path = arrowHead,
                color = PrimeAmber,
                style = Stroke(width = w * 0.06f, cap = StrokeCap.Round)
            )
        }
    }
}

@Composable
fun ActiveLogoHeader(
    modifier: Modifier = Modifier,
    badgeSize: Dp = 32.dp,
    showAdFreeBadge: Boolean = true
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        ActiveHybridBadge(size = badgeSize)

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "ACTIVE",
                fontSize = 20.sp,
                fontWeight = FontWeight.Black,
                fontFamily = FontFamily.SansSerif,
                letterSpacing = (-0.5).sp,
                color = Color.White
            )

            if (showAdFreeBadge) {
                Box(
                    modifier = Modifier
                        .padding(start = 6.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(ZeroAdsGreen.copy(alpha = 0.16f))
                        .border(0.8.dp, ZeroAdsGreen, RoundedCornerShape(4.dp))
                        .padding(horizontal = 4.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "ZERO ADS",
                        color = ZeroAdsGreen,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold,
                        letterSpacing = 0.5.sp
                    )
                }
            }
        }
    }
}
