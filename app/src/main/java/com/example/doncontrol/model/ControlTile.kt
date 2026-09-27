package com.example.doncontrol.model

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector

enum class TileCategory(val displayName: String) {
    CONNECTIVITY("Connectivity"),
    PRIVACY_SECURITY("Privacy & Security"),
    DEVICE_DISPLAY("Device & Display"),
    SMART_HOME("Smart Home & Google Assistant")
}

data class ControlTile(
    val id: String,
    val title: String,
    val subtitle: String,
    val category: TileCategory,
    val isActive: Boolean = false,
    val activeColor: Color = Color(0xFFD0BCFF), // Lavender default
    val hasChevron: Boolean = false,
    val pageIndex: Int = 0
)

data class QuickSlider(
    val id: String,
    val name: String,
    val value: Float, // 0.0 to 1.0
    val isVolume: Boolean = false
)

data class MediaItem(
    val deviceName: String = "Pixel Tablet • Living Room",
    val title: String = "Golden Hour",
    val artist: String = "JVKE — this is what ___ feel...",
    val durationSeconds: Int = 209, // 3:29
    val currentPositionSeconds: Int = 128, // 2:08
    val isPlaying: Boolean = false,
    val isLiked: Boolean = true
)

data class NotificationItem(
    val id: String,
    val appName: String,
    val timeAgo: String,
    val title: String,
    val content: String,
    val actionButtons: List<String> = emptyList(),
    val quickReplyChips: List<String> = emptyList(),
    val categoryIconRes: String = "home"
)