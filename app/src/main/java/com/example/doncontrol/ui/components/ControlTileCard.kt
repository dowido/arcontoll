package com.example.doncontrol.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doncontrol.model.ControlTile
import com.example.doncontrol.ui.theme.*

@Composable
fun ControlTileCard(
    tile: ControlTile,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEditMode: Boolean = false,
    onRemoveClick: (() -> Unit)? = null,
    onAddClick: (() -> Unit)? = null
) {
    val backgroundColor by animateColorAsState(
        targetValue = if (tile.isActive) tile.activeColor else TileInactive,
        label = "tileBgColor"
    )

    val contentColor = if (tile.isActive) TextDark else TextPrimary
    val subtitleColor = if (tile.isActive) TextDark.copy(alpha = 0.7f) else TextSecondary

    Box(
        modifier = modifier
            .padding(vertical = 4.dp, horizontal = 4.dp)
            .fillMaxWidth()
            .height(72.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxSize()
        ) {
            // Tile Icon Box
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(
                        if (tile.isActive) TextDark.copy(alpha = 0.1f) else Color.White.copy(alpha = 0.08f)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = getTileIcon(tile.id),
                    contentDescription = tile.title,
                    tint = contentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            // Title & Subtitle Column
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = tile.title,
                    color = contentColor,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = tile.subtitle,
                    color = subtitleColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Chevron or Add/Remove Badge
            if (isEditMode) {
                if (onRemoveClick != null) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(RemoveRed)
                            .clickable { onRemoveClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove tile",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                } else if (onAddClick != null) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(AddButtonBg)
                            .clickable { onAddClick() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add tile",
                            tint = Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            } else if (tile.hasChevron) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Options",
                    tint = contentColor.copy(alpha = 0.6f),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

fun getTileIcon(tileId: String): ImageVector {
    return when (tileId) {
        "internet" -> Icons.Default.Wifi
        "bluetooth" -> Icons.Default.Bluetooth
        "flashlight" -> Icons.Default.FlashlightOn
        "dnd" -> Icons.Default.DoNotDisturbOn
        "wallet" -> Icons.Default.AccountBalanceWallet
        "screen_record" -> Icons.Default.RadioButtonChecked
        "auto_rotate" -> Icons.Default.ScreenRotation
        "night_light" -> Icons.Default.NightsStay
        "quick_share" -> Icons.Default.Share
        "airplane" -> Icons.Default.AirplanemodeActive
        "vpn" -> Icons.Default.VpnKey
        "nfc" -> Icons.Default.Nfc
        "uwb" -> Icons.Default.Key
        "camera_access" -> Icons.Default.PhotoCamera
        "mic_access" -> Icons.Default.Mic
        "private_space" -> Icons.Default.Security
        "dark_theme" -> Icons.Default.DarkMode
        "extra_dim" -> Icons.Default.Brightness4
        "bedtime_mode" -> Icons.Default.Bedtime
        "live_caption" -> Icons.Default.ClosedCaption
        "home_devices" -> Icons.Default.HomeWork
        "ai_routines" -> Icons.Default.AutoAwesome
        else -> Icons.Default.Widgets
    }
}