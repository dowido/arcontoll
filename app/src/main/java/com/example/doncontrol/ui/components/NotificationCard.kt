package com.example.doncontrol.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doncontrol.model.NotificationItem
import com.example.doncontrol.ui.theme.*

@Composable
fun NotificationCard(
    notification: NotificationItem,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Header: App Icon, App Name, Time
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(getNotificationAppColor(notification.appName)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = getNotificationAppIcon(notification.appName),
                            contentDescription = notification.appName,
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = notification.appName,
                        color = TextPrimary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = notification.timeAgo,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = "Expand",
                    tint = TextSecondary,
                    modifier = Modifier
                        .size(18.dp)
                        .clickable { onDismiss() }
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Body Content Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = notification.title,
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = notification.content,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                }

                // If Google Home / Camera Preview
                if (notification.appName == "Google Home") {
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .size(width = 64.dp, height = 48.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(TileInactive),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Videocam,
                            contentDescription = "Camera Stream",
                            tint = ActiveMint,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                } else if (notification.appName == "Messages") {
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(ActivePeach),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "A",
                            color = TextDark,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }
            }

            // Action Buttons / Quick Reply Chips
            if (notification.actionButtons.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    notification.actionButtons.forEach { actionLabel ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (actionLabel.contains("Live") || actionLabel.contains("Reply")) ActiveLavender else TileInactive)
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = actionLabel,
                                color = if (actionLabel.contains("Live") || actionLabel.contains("Reply")) TextDark else TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            if (notification.quickReplyChips.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    notification.quickReplyChips.take(2).forEachIndexed { index, chip ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (index == 0) ActiveLavender else TileInactive)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .clickable { },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = chip,
                                color = if (index == 0) TextDark else TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }
        }
    }
}

private fun getNotificationAppColor(appName: String): Color {
    return when (appName) {
        "Google Home" -> ActiveMint
        "Gmail" -> Color(0xFFEA4335)
        "Messages" -> Color(0xFF1A73E8)
        else -> ActiveLavender
    }
}

private fun getNotificationAppIcon(appName: String) = when (appName) {
    "Google Home" -> Icons.Default.Home
    "Gmail" -> Icons.Default.Email
    "Messages" -> Icons.Default.Message
    else -> Icons.Default.Notifications
}