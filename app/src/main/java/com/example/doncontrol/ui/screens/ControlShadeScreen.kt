package com.example.doncontrol.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doncontrol.model.ControlTile
import com.example.doncontrol.shizuku.ShizukuStatus
import com.example.doncontrol.ui.components.*
import com.example.doncontrol.ui.theme.*
import com.example.doncontrol.viewmodel.ControlCenterViewModel

@Composable
fun ControlShadeScreen(
    viewModel: ControlCenterViewModel,
    onNavigateToCustomizer: () -> Unit
) {
    val activeTiles by viewModel.activeTiles.collectAsState()
    val displayBrightness by viewModel.displayBrightness.collectAsState()
    val mediaVolume by viewModel.mediaVolume.collectAsState()
    val mediaItem by viewModel.mediaItem.collectAsState()
    val notifications by viewModel.notifications.collectAsState()
    val shizukuStatus by viewModel.shizukuStatus.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()

    val totalPages = 2

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 100.dp)
        ) {
            // Header Bar
            item {
                HeaderBar(
                    title = "Quick Settings",
                    subtitle = "ANDROID 17 CONTROL",
                    onProfileClick = { },
                    onPowerClick = { }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Shizuku Connection Alert (if needed)
            item {
                ShizukuStatusCard(
                    status = shizukuStatus,
                    onRequestPermission = { viewModel.requestShizukuPermission() }
                )
            }

            // Sliders Row (Display Brightness & Media Volume side-by-side)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickSliderCard(
                        title = "Display",
                        value = displayBrightness,
                        onValueChange = { viewModel.updateBrightness(it) },
                        icon = Icons.Default.Brightness7,
                        modifier = Modifier.weight(1f),
                        fillColor = ActiveLavender
                    )

                    QuickSliderCard(
                        title = "Media",
                        value = mediaVolume,
                        onValueChange = { viewModel.updateVolume(it) },
                        icon = Icons.Default.VolumeUp,
                        modifier = Modifier.weight(1f),
                        fillColor = ActivePeach
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Quick Settings Tiles Grid (2 columns)
            item {
                val tilesForPage = activeTiles.drop(currentPage * 8).take(8)

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (i in tilesForPage.indices step 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val tile1 = tilesForPage[i]
                            ControlTileCard(
                                tile = tile1,
                                onClick = { viewModel.toggleTile(tile1.id) },
                                modifier = Modifier.weight(1f)
                            )

                            if (i + 1 < tilesForPage.size) {
                                val tile2 = tilesForPage[i + 1]
                                ControlTileCard(
                                    tile = tile2,
                                    onClick = { viewModel.toggleTile(tile2.id) },
                                    modifier = Modifier.weight(1f)
                                )
                            } else {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Page Indicator Dots
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    for (p in 0 until totalPages) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 4.dp)
                                .size(if (p == currentPage) 16.dp else 8.dp, 8.dp)
                                .clip(CircleShape)
                                .background(if (p == currentPage) ActiveLavender else TileInactive)
                                .clickable { viewModel.setPage(p) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Media Player Card
            item {
                MediaPlayerCard(
                    mediaItem = mediaItem,
                    onPlayPauseClick = { viewModel.toggleMediaPlayPause() },
                    onLikeClick = { viewModel.toggleMediaLike() },
                    onPositionChange = { viewModel.updateMediaPosition(it) }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Notifications Header
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Notifications",
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(ActiveLavender),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${notifications.size}",
                                color = TextDark,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }

                    if (notifications.isNotEmpty()) {
                        Text(
                            text = "Clear all",
                            color = ActiveLavender,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable { viewModel.clearAllNotifications() }
                        )
                    }
                }
            }

            // Notifications List
            items(notifications.size) { index ->
                val notification = notifications[index]
                NotificationCard(
                    notification = notification,
                    onDismiss = { viewModel.dismissNotification(notification.id) }
                )
            }
        }

        // Bottom Floating Bar
        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(BackgroundDark.copy(alpha = 0.95f))
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            BottomControlBar(
                onEditLayoutClick = onNavigateToCustomizer,
                onAddClick = onNavigateToCustomizer,
                onPowerClick = { },
                onSettingsClick = { }
            )
        }
    }
}