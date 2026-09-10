package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.ActiveBrandRed
import com.example.ui.theme.FacebookBlue
import com.example.ui.theme.NetflixRed
import com.example.ui.theme.PrimeAmber
import com.example.ui.theme.ZeroAdsGreen
import kotlinx.coroutines.delay

@Composable
fun AppOpenAnimationScreen(
    onAnimationFinished: () -> Unit
) {
    val scale = remember { Animatable(0.3f) }
    val alpha = remember { Animatable(0f) }
    val textAlpha = remember { Animatable(0f) }
    val badgeAlpha = remember { Animatable(0f) }

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val glowRadius by infiniteTransition.animateFloat(
        initialValue = 0.8f,
        targetValue = 1.25f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glow"
    )

    LaunchedEffect(Unit) {
        // Phase 1: Logo zoom & fade-in
        alpha.animateTo(1f, tween(350, easing = LinearEasing))
        scale.animateTo(1.15f, tween(500, easing = FastOutSlowInEasing))
        scale.animateTo(1.0f, tween(250, easing = FastOutSlowInEasing))

        // Phase 2: Brand title reveal
        textAlpha.animateTo(1f, tween(300, easing = FastOutSlowInEasing))

        // Phase 3: Zero-Ads guarantee badge reveal
        badgeAlpha.animateTo(1f, tween(300, easing = FastOutSlowInEasing))

        // Hold briefly for cinematic impact
        delay(900)

        // Phase 4: Smooth transition out
        alpha.animateTo(0f, tween(300, easing = LinearEasing))
        onAnimationFinished()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF08090D))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                // Allow tapping to skip
                onAnimationFinished()
            },
        contentAlignment = Alignment.Center
    ) {
        // Multi-brand ambient glow halo behind logo
        Box(
            modifier = Modifier
                .size(240.dp)
                .scale(glowRadius)
                .alpha(alpha.value * 0.45f)
                .blur(48.dp)
                .clip(CircleShape)
                .background(
                    brush = Brush.radialGradient(
                        colors = listOf(
                            NetflixRed,
                            ActiveBrandRed,
                            FacebookBlue,
                            PrimeAmber.copy(alpha = 0.5f),
                            Color.Transparent
                        )
                    )
                )
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .scale(scale.value)
                .alpha(alpha.value)
        ) {
            // High-impact Hybrid Logo Badge
            ActiveHybridBadge(size = 96.dp)

            Spacer(modifier = Modifier.height(24.dp))

            // App Name with brand gradient flair
            Text(
                text = "ACTIVE",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp,
                modifier = Modifier.alpha(textAlpha.value)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Subtitle & 100% Zero-Ads YouTube engine guarantee
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier
                    .alpha(badgeAlpha.value)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFF161922))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = "Shield",
                    tint = ZeroAdsGreen,
                    modifier = Modifier.size(16.dp)
                )

                Text(
                    text = "Zero Ads • Pure Stream • YouTube Engine",
                    color = ZeroAdsGreen,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // Skip prompt at bottom
        Text(
            text = "Tap anywhere to enter",
            color = Color.White.copy(alpha = 0.35f),
            fontSize = 11.sp,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp)
                .alpha(badgeAlpha.value)
        )
    }
}
