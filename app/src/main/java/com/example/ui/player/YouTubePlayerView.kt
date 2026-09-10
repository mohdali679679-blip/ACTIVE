package com.example.ui.player

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun YouTubePlayerView(
    videoId: String,
    playbackSpeed: Float,
    isLooping: Boolean,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val webView = remember {
        WebView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            setBackgroundColor(android.graphics.Color.BLACK)

            settings.apply {
                javaScriptEnabled = true
                domStorageEnabled = true
                mediaPlaybackRequiresUserGesture = false
                loadWithOverviewMode = true
                useWideViewPort = true
                cacheMode = WebSettings.LOAD_DEFAULT
                mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            }

            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun shouldInterceptRequest(
                    view: WebView?,
                    request: WebResourceRequest?
                ): WebResourceResponse? {
                    val url = request?.url?.toString() ?: ""
                    // Ad blocking interceptor: block known ad domains and tracker scripts
                    if (url.contains("doubleclick.net") ||
                        url.contains("googleads") ||
                        url.contains("pagead") ||
                        url.contains("adservice")
                    ) {
                        return WebResourceResponse("text/plain", "UTF-8", null)
                    }
                    return super.shouldInterceptRequest(view, request)
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    // Inject custom scripts to enforce playback speed, loop, and hide banners
                    view?.evaluateJavascript(
                        """
                        (function() {
                            var v = document.querySelector('video');
                            if (v) {
                                v.playbackRate = $playbackSpeed;
                                v.loop = $isLooping;
                            }
                            // Clean ad elements and popups
                            var ads = document.querySelectorAll('.ytp-ad-module, .ytp-ad-overlay-container, .ytp-ad-player-overlay');
                            ads.forEach(function(el) { el.style.display = 'none'; });
                        })();
                        """.trimIndent(),
                        null
                    )
                }
            }
        }
    }

    // React to videoId change
    LaunchedEffect(videoId) {
        val embedHtml = """
            <!DOCTYPE html>
            <html>
            <head>
                <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=no">
                <style>
                    * { margin: 0; padding: 0; box-sizing: border-box; background-color: #000; }
                    html, body { width: 100%; height: 100%; overflow: hidden; }
                    iframe { width: 100%; height: 100%; border: none; }
                </style>
            </head>
            <body>
                <iframe 
                    id="player"
                    src="https://www.youtube-nocookie.com/embed/$videoId?autoplay=1&playsinline=1&enablejsapi=1&rel=0&modestbranding=1&controls=1&iv_load_policy=3&fs=1"
                    allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share"
                    allowfullscreen>
                </iframe>
                <script>
                    var tag = document.createElement('script');
                    tag.src = "https://www.youtube.com/iframe_api";
                    var firstScriptTag = document.getElementsByTagName('script')[0];
                    firstScriptTag.parentNode.insertBefore(tag, firstScriptTag);

                    var player;
                    function onYouTubeIframeAPIReady() {
                        player = new YT.Player('player', {
                            events: {
                                'onReady': onPlayerReady
                            }
                        });
                    }
                    function onPlayerReady(event) {
                        event.target.setPlaybackRate($playbackSpeed);
                        event.target.playVideo();
                    }
                </script>
            </body>
            </html>
        """.trimIndent()

        webView.loadDataWithBaseURL("https://www.youtube.com", embedHtml, "text/html", "UTF-8", null)
    }

    // React to playback speed changes in real-time
    LaunchedEffect(playbackSpeed) {
        webView.evaluateJavascript(
            """
            (function() {
                if (typeof player !== 'undefined' && player.setPlaybackRate) {
                    player.setPlaybackRate($playbackSpeed);
                }
                var v = document.querySelector('video');
                if (v) {
                    v.playbackRate = $playbackSpeed;
                }
            })();
            """.trimIndent(),
            null
        )
    }

    // React to loop toggle
    LaunchedEffect(isLooping) {
        webView.evaluateJavascript(
            """
            (function() {
                var v = document.querySelector('video');
                if (v) {
                    v.loop = $isLooping;
                }
            })();
            """.trimIndent(),
            null
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            webView.stopLoading()
            webView.destroy()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AndroidView(
            factory = { webView },
            modifier = Modifier.fillMaxSize()
        )
    }
}
