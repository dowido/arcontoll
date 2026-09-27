package com.example.doncontrol.viewmodel

import android.app.Application
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.doncontrol.model.ControlTile
import com.example.doncontrol.model.MediaItem
import com.example.doncontrol.model.NotificationItem
import com.example.doncontrol.model.TileCategory
import com.example.doncontrol.shizuku.ShizukuManager
import com.example.doncontrol.shizuku.ShizukuStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class ScreenState {
    CONTROL_SHADE,
    TILE_CUSTOMIZER
}

class ControlCenterViewModel(application: Application) : AndroidViewModel(application) {

    val shizukuManager = ShizukuManager(application.applicationContext)

    private val _shizukuStatus = MutableStateFlow(ShizukuStatus.DISCONNECTED)
    val shizukuStatus: StateFlow<ShizukuStatus> = _shizukuStatus.asStateFlow()

    private val _currentScreen = MutableStateFlow(ScreenState.CONTROL_SHADE)
    val currentScreen: StateFlow<ScreenState> = _currentScreen.asStateFlow()

    private val _currentPage = MutableStateFlow(0)
    val currentPage: StateFlow<Int> = _currentPage.asStateFlow()

    private val _displayBrightness = MutableStateFlow(0.74f)
    val displayBrightness: StateFlow<Float> = _displayBrightness.asStateFlow()

    private val _mediaVolume = MutableStateFlow(0.58f)
    val mediaVolume: StateFlow<Float> = _mediaVolume.asStateFlow()

    private val _mediaItem = MutableStateFlow(MediaItem())
    val mediaItem: StateFlow<MediaItem> = _mediaItem.asStateFlow()

    private val _notifications = MutableStateFlow(
        listOf(
            NotificationItem(
                id = "1",
                appName = "Google Home",
                timeAgo = "4m ago",
                title = "Front Door Cam",
                content = "Person detected standing near front porch delivery drop zone.",
                actionButtons = listOf("Live View", "Mute 1h")
            ),
            NotificationItem(
                id = "2",
                appName = "Gmail",
                timeAgo = "18m ago",
                title = "Google Cloud Platform",
                content = "Weekly Cluster Health & Deployment Report\nProduction-US-East-1 sustained 99.998% uptime...",
                actionButtons = listOf("Archive", "Reply")
            ),
            NotificationItem(
                id = "3",
                appName = "Messages",
                timeAgo = "32m ago",
                title = "Alex Rivera",
                content = "\"Are we still meeting for lunch at 1? Found a great sushi place!\"",
                quickReplyChips = listOf("Yes, let's do it! 🍣", "Running 10m late", "Custom reply")
            )
        )
    )
    val notifications: StateFlow<List<NotificationItem>> = _notifications.asStateFlow()

    // Color Constants
    private val lavenderColor = Color(0xFFD0BCFF)
    private val mintColor = Color(0xFF82E0AA)
    private val peachColor = Color(0xFFFFB4A2)

    private val initialTiles = listOf(
        ControlTile("internet", "Internet", "Google Fiber 5G", TileCategory.CONNECTIVITY, isActive = true, activeColor = lavenderColor, hasChevron = true),
        ControlTile("bluetooth", "Bluetooth", "Buds Pro 2", TileCategory.CONNECTIVITY, isActive = true, activeColor = lavenderColor, hasChevron = true),
        ControlTile("dnd", "DND", "Off", TileCategory.PRIVACY_SECURITY, isActive = false),
        ControlTile("flashlight", "Flashlight", "Max beam", TileCategory.DEVICE_DISPLAY, isActive = true, activeColor = mintColor),
        ControlTile("auto_rotate", "Auto-rotate", "Portrait locked", TileCategory.DEVICE_DISPLAY, isActive = false),
        ControlTile("night_light", "Night Light", "Until 7:00 AM", TileCategory.DEVICE_DISPLAY, isActive = false),
        ControlTile("quick_share", "Quick Share", "Contacts only", TileCategory.CONNECTIVITY, isActive = true, activeColor = lavenderColor),
        ControlTile("wallet", "Wallet", "**** 9042", TileCategory.CONNECTIVITY, isActive = false),
        ControlTile("screen_record", "Record", "Ready", TileCategory.DEVICE_DISPLAY, isActive = false, activeColor = peachColor)
    )

    private val initialAvailableTiles = listOf(
        ControlTile("airplane", "Airplane Mode", "Radio off", TileCategory.CONNECTIVITY),
        ControlTile("vpn", "VPN", "Google One", TileCategory.CONNECTIVITY),
        ControlTile("nfc", "NFC", "Tap & pay", TileCategory.CONNECTIVITY),
        ControlTile("uwb", "Ultra-Wideband", "Car key ready", TileCategory.CONNECTIVITY),
        ControlTile("camera_access", "Camera Access", "System kill-switch", TileCategory.PRIVACY_SECURITY),
        ControlTile("mic_access", "Mic Access", "System-wide toggle", TileCategory.PRIVACY_SECURITY),
        ControlTile("private_space", "Private Space", "Biometric locked apps container", TileCategory.PRIVACY_SECURITY),
        ControlTile("dark_theme", "Dark Theme", "OLED pitch black", TileCategory.DEVICE_DISPLAY),
        ControlTile("extra_dim", "Extra Dim", "Night reading", TileCategory.DEVICE_DISPLAY),
        ControlTile("bedtime_mode", "Bedtime Mode", "Grayscale filter", TileCategory.DEVICE_DISPLAY),
        ControlTile("live_caption", "Live Caption", "On-device AI", TileCategory.DEVICE_DISPLAY),
        ControlTile("home_devices", "Home Devices", "Thermostat & lights", TileCategory.SMART_HOME),
        ControlTile("ai_routines", "AI Routines", "Gemini Live", TileCategory.SMART_HOME)
    )

    private val _activeTiles = MutableStateFlow<List<ControlTile>>(initialTiles.take(8))
    val activeTiles: StateFlow<List<ControlTile>> = _activeTiles.asStateFlow()

    private val _availableTiles = MutableStateFlow<List<ControlTile>>(initialAvailableTiles)
    val availableTiles: StateFlow<List<ControlTile>> = _availableTiles.asStateFlow()

    init {
        shizukuManager.register(object : ShizukuManager.Listener {
            override fun onStatusChanged(status: ShizukuStatus) {
                _shizukuStatus.value = status
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        shizukuManager.unregister()
    }

    fun requestShizukuPermission() {
        shizukuManager.requestPermission()
    }

    fun navigateTo(screen: ScreenState) {
        _currentScreen.value = screen
    }

    fun setPage(page: Int) {
        _currentPage.value = page
    }

    fun toggleTile(tileId: String) {
        _activeTiles.value = _activeTiles.value.map { tile ->
            if (tile.id == tileId) {
                val newActiveState = !tile.isActive
                executeTileAction(tileId, newActiveState)
                tile.copy(isActive = newActiveState)
            } else {
                tile
            }
        }
    }

    private fun executeTileAction(tileId: String, newActiveState: Boolean) {
        viewModelScope.launch {
            when (tileId) {
                "internet" -> shizukuManager.setWifiEnabled(newActiveState)
                "bluetooth" -> shizukuManager.setBluetoothEnabled(newActiveState)
                "flashlight" -> shizukuManager.setFlashlightEnabled(newActiveState)
                "airplane" -> shizukuManager.setAirplaneMode(newActiveState)
                "dark_theme" -> shizukuManager.setDarkTheme(newActiveState)
                "auto_rotate" -> shizukuManager.setAutoRotate(newActiveState)
            }
        }
    }

    fun removeTileFromActive(tileId: String) {
        val tileToRemove = _activeTiles.value.find { it.id == tileId }
        if (tileToRemove != null) {
            _activeTiles.value = _activeTiles.value.filter { it.id != tileId }
            _availableTiles.value = _availableTiles.value + tileToRemove.copy(isActive = false)
        }
    }

    fun addTileToActive(tileId: String) {
        val tileToAdd = _availableTiles.value.find { it.id == tileId }
        if (tileToAdd != null) {
            _availableTiles.value = _availableTiles.value.filter { it.id != tileId }
            _activeTiles.value = _activeTiles.value + tileToAdd.copy(isActive = false)
        }
    }

    fun resetLayout() {
        _activeTiles.value = initialTiles.take(8)
        _availableTiles.value = initialAvailableTiles
    }

    fun updateBrightness(value: Float) {
        _displayBrightness.value = value.coerceIn(0f, 1f)
        shizukuManager.setBrightness((_displayBrightness.value * 100).toInt())
    }

    fun updateVolume(value: Float) {
        _mediaVolume.value = value.coerceIn(0f, 1f)
        shizukuManager.setVolume((_mediaVolume.value * 100).toInt())
    }

    fun toggleMediaPlayPause() {
        _mediaItem.value = _mediaItem.value.copy(isPlaying = !_mediaItem.value.isPlaying)
    }

    fun toggleMediaLike() {
        _mediaItem.value = _mediaItem.value.copy(isLiked = !_mediaItem.value.isLiked)
    }

    fun updateMediaPosition(positionSeconds: Int) {
        _mediaItem.value = _mediaItem.value.copy(currentPositionSeconds = positionSeconds)
    }

    fun clearAllNotifications() {
        _notifications.value = emptyList()
    }

    fun dismissNotification(id: String) {
        _notifications.value = _notifications.value.filter { it.id != id }
    }
}