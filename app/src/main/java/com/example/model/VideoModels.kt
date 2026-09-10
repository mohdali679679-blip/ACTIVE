package com.example.model

data class VideoItem(
    val id: String,
    val title: String,
    val channelTitle: String,
    val channelAvatar: String,
    val views: String,
    val publishedTime: String,
    val duration: String,
    val description: String,
    val category: String,
    val isVerified: Boolean = true,
    val likesCount: String = "128K",
    val commentsCount: String = "4.2K",
    val subscribersCount: String = "12.4M",
    val videoUrl: String = "https://www.youtube.com/watch?v=$id",
    val thumbnailUrl: String = "https://img.youtube.com/vi/$id/hqdefault.jpg"
)

data class ShortItem(
    val id: String,
    val title: String,
    val channelTitle: String,
    val channelAvatar: String,
    val likesCount: String,
    val commentsCount: String,
    val musicTitle: String,
    val thumbnailUrl: String = "https://img.youtube.com/vi/$id/hqdefault.jpg"
)

data class ChannelItem(
    val id: String,
    val name: String,
    val avatar: String,
    val subscriberCount: String,
    val hasUnread: Boolean = false,
    val isLive: Boolean = false
)

data class CommentItem(
    val id: String,
    val authorName: String,
    val authorAvatar: String,
    val text: String,
    val likes: String,
    val timeAgo: String,
    val isPinned: Boolean = false
)

enum class VideoQuality(val label: String, val badge: String, val resolution: String) {
    AUTO("Auto (1080p)", "AUTO", "1080p"),
    UHD_4K("2160p (4K UHD)", "4K", "2160p"),
    QHD_1440("1440p (QHD)", "2K", "1440p"),
    FHD_1080("1080p60 (Full HD)", "FHD", "1080p"),
    HD_720("720p (HD)", "HD", "720p"),
    SD_480("480p", "SD", "480p"),
    SAVER_360("360p (Data Saver)", "360p", "360p"),
    LOW_240("240p", "240p", "240p"),
    ULTRA_144("144p (Ultra Saver)", "144p", "144p")
}

data class PlaybackSpeed(val label: String, val speed: Float) {
    companion object {
        val ALL = listOf(
            PlaybackSpeed("0.25x", 0.25f),
            PlaybackSpeed("0.5x", 0.5f),
            PlaybackSpeed("0.75x", 0.75f),
            PlaybackSpeed("Normal (1.0x)", 1.0f),
            PlaybackSpeed("1.25x", 1.25f),
            PlaybackSpeed("1.5x", 1.5f),
            PlaybackSpeed("1.75x", 1.75f),
            PlaybackSpeed("2.0x", 2.0f)
        )
    }
}

data class PlaylistItem(
    val id: String,
    val title: String,
    val videoCount: Int,
    val thumbnail: String,
    val subtitle: String
)
