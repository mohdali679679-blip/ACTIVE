package com.example.data

import com.example.model.ChannelItem
import com.example.model.CommentItem
import com.example.model.PlaylistItem
import com.example.model.ShortItem
import com.example.model.VideoItem
import java.util.regex.Pattern

object YouTubeDataRepository {

    val categories = listOf(
        "All",
        "Gaming",
        "Music",
        "Technology",
        "Podcasts",
        "Science",
        "News",
        "Lo-Fi Beats",
        "AI & Coding",
        "Trending",
        "Recently uploaded"
    )

    val sampleChannels = listOf(
        ChannelItem(
            id = "c1",
            name = "MKBHD",
            avatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
            subscriberCount = "19.2M",
            hasUnread = true
        ),
        ChannelItem(
            id = "c2",
            name = "MrBeast",
            avatar = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150",
            subscriberCount = "310M",
            hasUnread = true
        ),
        ChannelItem(
            id = "c3",
            name = "Veritasium",
            avatar = "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=150",
            subscriberCount = "16.5M",
            hasUnread = false
        ),
        ChannelItem(
            id = "c4",
            name = "Fireship",
            avatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
            subscriberCount = "3.4M",
            hasUnread = true
        ),
        ChannelItem(
            id = "c5",
            name = "Lofi Girl",
            avatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
            subscriberCount = "14.1M",
            hasUnread = false,
            isLive = true
        ),
        ChannelItem(
            id = "c6",
            name = "Kurzgesagt",
            avatar = "https://images.unsplash.com/photo-1628157582853-a796fa650a6a?w=150",
            subscriberCount = "22.8M",
            hasUnread = false
        )
    )

    val videos = listOf(
        VideoItem(
            id = "L_LUpnjgPso",
            title = "Apple Vision Pro Review: Tomorrow's Tech Today!",
            channelTitle = "MKBHD",
            channelAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
            views = "14M views",
            publishedTime = "2 months ago",
            duration = "18:42",
            category = "Technology",
            description = "The Apple Vision Pro is finally here. After using it for weeks, here is the honest, unvarnished truth about spatial computing and what the future holds.\n\n0:00 Intro\n1:24 Hardware & Displays\n5:10 Eye & Hand Tracking\n10:45 Battery Life\n15:30 The Verdict\n\n#AppleVisionPro #TechReview #MKBHD",
            likesCount = "654K",
            commentsCount = "38.5K",
            subscribersCount = "19.2M"
        ),
        VideoItem(
            id = "jfKfPfyJRdk",
            title = "lofi hip hop radio - beats to relax/study to ☕ [24/7 Pure Ad-Free]",
            channelTitle = "Lofi Girl",
            channelAvatar = "https://images.unsplash.com/photo-1534528741775-53994a69daeb?w=150",
            views = "450M views",
            publishedTime = "Live now",
            duration = "LIVE",
            category = "Lo-Fi Beats",
            description = "Peaceful lofi hip hop radio beats to relax, study, sleep and chill to. Streaming continuously 24/7 with zero ad interruptions on ACTIVE.\n\n🎵 Listen on all streaming platforms\n🎨 Artwork by Juan Pablo\n\n#lofi #chill #studybeats",
            likesCount = "12M",
            commentsCount = "1.2M",
            subscribersCount = "14.1M"
        ),
        VideoItem(
            id = "kXYiU_JCYtU",
            title = "100+ Computer Science Concepts Explained in 13 Minutes",
            channelTitle = "Fireship",
            channelAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
            views = "4.9M views",
            publishedTime = "1 year ago",
            duration = "13:08",
            category = "AI & Coding",
            description = "Learn the most important computer science fundamentals in 13 minutes. Binary, CPU architecture, algorithms, Big O, operating systems, and memory management.\n\n#computerscience #programming #fireship",
            likesCount = "340K",
            commentsCount = "12.4K",
            subscribersCount = "3.4M"
        ),
        VideoItem(
            id = "9bZkp7q19f0",
            title = "PSY - GANGNAM STYLE (강남스타일) M/V [Official Remaster 4K]",
            channelTitle = "officialpsy",
            channelAvatar = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?w=150",
            views = "5.2B views",
            publishedTime = "12 years ago",
            duration = "4:13",
            category = "Music",
            description = "PSY - ‘GANGNAM STYLE(강남스타일)’ M/V \nStream original music ad-free on ACTIVE.\n\n#PSY #GANGNAMSTYLE #KPOP",
            likesCount = "28M",
            commentsCount = "5.6M",
            subscribersCount = "18.3M"
        ),
        VideoItem(
            id = "3JZ_D3ELwOQ",
            title = "The Simplest Math Problem No One Can Solve - Collatz Conjecture",
            channelTitle = "Veritasium",
            channelAvatar = "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=150",
            views = "39M views",
            publishedTime = "3 years ago",
            duration = "22:09",
            category = "Science",
            description = "The Collatz Conjecture is the simplest math problem that no one can solve — it looks easy enough for a 10-year-old, but the world's greatest mathematicians can't prove it.\n\n#Veritasium #Math #Science",
            likesCount = "1.8M",
            commentsCount = "78.2K",
            subscribersCount = "16.5M"
        ),
        VideoItem(
            id = "OPf0YbXqDm0",
            title = "The Last Human on Earth - What Happens in 100 Trillion Years?",
            channelTitle = "Kurzgesagt – In a Nutshell",
            channelAvatar = "https://images.unsplash.com/photo-1628157582853-a796fa650a6a?w=150",
            views = "24M views",
            publishedTime = "2 years ago",
            duration = "10:47",
            category = "Science",
            description = "Deep time is terrifying. What will the universe look like when the last stars burn out and the black holes evaporate?\n\nNarrated by Steve Taylor.\n#Kurzgesagt #Space #Universe",
            likesCount = "1.4M",
            commentsCount = "62K",
            subscribersCount = "22.8M"
        ),
        VideoItem(
            id = "dQw4w9WgXcQ",
            title = "Rick Astley - Never Gonna Give You Up (Official Music Video)",
            channelTitle = "Rick Astley",
            channelAvatar = "https://images.unsplash.com/photo-1492562080023-ab3db95bfbce?w=150",
            views = "1.5B views",
            publishedTime = "14 years ago",
            duration = "3:33",
            category = "Music",
            description = "The official video for Never Gonna Give You Up by Rick Astley. Completely unskippable no longer needed because ACTIVE has ZERO ads!\n\n#RickAstley #NeverGonnaGiveYouUp #80sMusic",
            likesCount = "16M",
            commentsCount = "2.4M",
            subscribersCount = "4.2M"
        ),
        VideoItem(
            id = "jNQXAC9IVRw",
            title = "Me at the zoo [The First Video Ever on YouTube]",
            channelTitle = "jawed",
            channelAvatar = "https://images.unsplash.com/photo-1472099645785-5658abf4ff4e?w=150",
            views = "325M views",
            publishedTime = "20 years ago",
            duration = "0:19",
            category = "Trending",
            description = "The first video on YouTube, uploaded at 8:27PM on Saturday April 23rd, 2005 by YouTube co-founder Jawed Karim at the San Diego Zoo.\n\n#YouTubeHistory #FirstVideo",
            likesCount = "15M",
            commentsCount = "11.2M",
            subscribersCount = "4.9M"
        ),
        VideoItem(
            id = "0e3GPea1Tyg",
            title = "$456,000 Squid Game In Real Life!",
            channelTitle = "MrBeast",
            channelAvatar = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150",
            views = "620M views",
            publishedTime = "2 years ago",
            duration = "25:41",
            category = "Gaming",
            description = "Recreating every single game from Squid Game in real life with 456 contestants competing for $456,000 cash!\n\n#MrBeast #SquidGame #Challenge",
            likesCount = "17M",
            commentsCount = "720K",
            subscribersCount = "310M"
        ),
        VideoItem(
            id = "lTRiuFIWV54",
            title = "Building the Ultimate $20,000 Gaming Desk Setup",
            channelTitle = "Linus Tech Tips",
            channelAvatar = "https://images.unsplash.com/photo-1522075469751-3a6694fb2f61?w=150",
            views = "8.4M views",
            publishedTime = "6 months ago",
            duration = "21:15",
            category = "Technology",
            description = "We spent over $20,000 building the most overkill custom liquid-cooled motorized battle station desk known to humankind.\n\n#GamingSetup #LTT #CustomPC",
            likesCount = "410K",
            commentsCount = "18.9K",
            subscribersCount = "15.8M"
        )
    )

    val shorts = listOf(
        ShortItem(
            id = "L_LUpnjgPso",
            title = "The wildest secret feature inside Vision Pro 🤯",
            channelTitle = "MKBHD",
            channelAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
            likesCount = "1.8M",
            commentsCount = "14.2K",
            musicTitle = "Original Audio - MKBHD Beats"
        ),
        ShortItem(
            id = "kXYiU_JCYtU",
            title = "Why Senior Devs never write comments 💀",
            channelTitle = "Fireship",
            channelAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
            likesCount = "940K",
            commentsCount = "8.9K",
            musicTitle = "Fireship Synthwave - Beyond Code"
        ),
        ShortItem(
            id = "0e3GPea1Tyg",
            title = "Giving $100,000 to the last person to take their hand off",
            channelTitle = "MrBeast",
            channelAvatar = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150",
            likesCount = "4.2M",
            commentsCount = "38.1K",
            musicTitle = "Epic Beast Bassline"
        ),
        ShortItem(
            id = "3JZ_D3ELwOQ",
            title = "This simple optical illusion will break your brain!",
            channelTitle = "Veritasium",
            channelAvatar = "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=150",
            likesCount = "2.1M",
            commentsCount = "19.5K",
            musicTitle = "Mind Bending Physics Theme"
        )
    )

    val sampleComments = listOf(
        CommentItem(
            id = "cm1",
            authorName = "TechExplorer",
            authorAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
            text = "Watching this completely ad-free on ACTIVE feels like magic! The quality switch is instant. Absolutely 10/10 app!",
            likes = "24K",
            timeAgo = "1 day ago",
            isPinned = true
        ),
        CommentItem(
            id = "cm2",
            authorName = "CodeMaster99",
            authorAvatar = "https://images.unsplash.com/photo-1570295999919-56ceb5ecca61?w=150",
            text = "Zero ads, instant speed controls, and smooth video playback. Never going back to standard YouTube.",
            likes = "8.3K",
            timeAgo = "18 hours ago"
        ),
        CommentItem(
            id = "cm3",
            authorName = "Sarah Jenkins",
            authorAvatar = "https://images.unsplash.com/photo-1580489944761-15a19d654956?w=150",
            text = "The audio quality and frame rate setting are so clear. Love the hybrid logo design too!",
            likes = "3.1K",
            timeAgo = "2 days ago"
        ),
        CommentItem(
            id = "cm4",
            authorName = "David Kim",
            authorAvatar = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?w=150",
            text = "Can confirm: 0 ads encountered. Pure unadulterated content stream.",
            likes = "1.5K",
            timeAgo = "3 days ago"
        )
    )

    val userPlaylists = listOf(
        PlaylistItem(
            id = "p1",
            title = "Liked videos",
            videoCount = 142,
            thumbnail = "https://img.youtube.com/vi/L_LUpnjgPso/hqdefault.jpg",
            subtitle = "Auto playlist • 142 videos"
        ),
        PlaylistItem(
            id = "p2",
            title = "Watch Later",
            videoCount = 28,
            thumbnail = "https://img.youtube.com/vi/kXYiU_JCYtU/hqdefault.jpg",
            subtitle = "Private • 28 videos"
        ),
        PlaylistItem(
            id = "p3",
            title = "Coding & AI Masterclass",
            videoCount = 45,
            thumbnail = "https://img.youtube.com/vi/3JZ_D3ELwOQ/hqdefault.jpg",
            subtitle = "Updated 2 days ago • 45 videos"
        ),
        PlaylistItem(
            id = "p4",
            title = "Lo-Fi Work Chill",
            videoCount = 64,
            thumbnail = "https://img.youtube.com/vi/jfKfPfyJRdk/hqdefault.jpg",
            subtitle = "Created by you • 64 videos"
        )
    )

    fun search(query: String): List<VideoItem> {
        val trimmed = query.trim()
        if (trimmed.isEmpty()) return videos

        // Check if query is a direct YouTube URL or 11-character video ID
        val extractedId = extractVideoId(trimmed)
        if (extractedId != null) {
            return listOf(
                VideoItem(
                    id = extractedId,
                    title = "YouTube Video ($extractedId) [Ad-Free Stream]",
                    channelTitle = "Direct Stream",
                    channelAvatar = "https://images.unsplash.com/photo-1535713875002-d1d0cf377fde?w=150",
                    views = "Custom Query",
                    publishedTime = "Live Query",
                    duration = "Full",
                    category = "Direct",
                    description = "Playing directly via YouTube API URL extraction with ZERO ads.\nVideo ID: $extractedId",
                    likesCount = "99K",
                    commentsCount = "1.2K"
                )
            ) + videos
        }

        return videos.filter {
            it.title.contains(trimmed, ignoreCase = true) ||
            it.channelTitle.contains(trimmed, ignoreCase = true) ||
            it.category.contains(trimmed, ignoreCase = true) ||
            it.description.contains(trimmed, ignoreCase = true)
        }
    }

    fun getByCategory(category: String): List<VideoItem> {
        if (category == "All") return videos
        if (category == "Trending") return videos.sortedByDescending { it.views }
        return videos.filter { it.category.equals(category, ignoreCase = true) }
            .ifEmpty { videos }
    }

    private fun extractVideoId(url: String): String? {
        if (url.length == 11 && !url.contains("/") && !url.contains(" ")) {
            return url
        }
        val pattern = "(?:youtu\\.be/|youtube\\.com/(?:embed/|v/|watch\\?v=|watch\\?.+&v=))([\\w-]{11})"
        val matcher = Pattern.compile(pattern).matcher(url)
        return if (matcher.find()) matcher.group(1) else null
    }
}
