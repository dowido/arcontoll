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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doncontrol.model.MediaItem
import com.example.doncontrol.ui.theme.*

@Composable
fun MediaPlayerCard(
    mediaItem: MediaItem,
    onPlayPauseClick: () -> Unit,
    onLikeClick: () -> Unit,
    onPositionChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            // Top Row: Device Picker Pill & Audio Output Icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(16.dp))
                        .background(TileInactive)
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TabletAndroid,
                        contentDescription = "Device",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = mediaItem.deviceName,
                        color = TextPrimary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Select Device",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                }

                Icon(
                    imageVector = Icons.Default.GraphicEq,
                    contentDescription = "Playing Audio",
                    tint = ActiveLavender,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Track Row: Album Cover + Title/Artist + Like Heart
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Album Art Placeholder
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(ActiveLavender.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "Album Art",
                        tint = ActiveLavender,
                        modifier = Modifier.size(28.dp)
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                // Title & Artist Column
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = mediaItem.title,
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = mediaItem.artist,
                        color = TextSecondary,
                        fontSize = 13.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                IconButton(onClick = onLikeClick) {
                    Icon(
                        imageVector = if (mediaItem.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Like",
                        tint = if (mediaItem.isLiked) ActivePeach else TextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Progress Slider Bar
            val currentProgress = mediaItem.currentPositionSeconds.toFloat() / mediaItem.durationSeconds.toFloat()
            Slider(
                value = currentProgress,
                onValueChange = { onPositionChange((it * mediaItem.durationSeconds).toInt()) },
                colors = SliderDefaults.colors(
                    thumbColor = ActiveLavender,
                    activeTrackColor = ActiveLavender,
                    inactiveTrackColor = TileInactive
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = formatTime(mediaItem.currentPositionSeconds),
                    color = TextSecondary,
                    fontSize = 11.sp
                )
                Text(
                    text = formatTime(mediaItem.durationSeconds),
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Playback Controls Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onPositionChange(0) }) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous",
                        tint = TextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }

                IconButton(onClick = { onPositionChange((mediaItem.currentPositionSeconds - 10).coerceAtLeast(0)) }) {
                    Icon(
                        imageVector = Icons.Default.Replay10,
                        contentDescription = "Rewind 10s",
                        tint = TextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                // Play / Pause Circle
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(ActiveLavender)
                        .clickable { onPlayPauseClick() },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (mediaItem.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (mediaItem.isPlaying) "Pause" else "Play",
                        tint = TextDark,
                        modifier = Modifier.size(28.dp)
                    )
                }

                IconButton(onClick = { onPositionChange((mediaItem.currentPositionSeconds + 10).coerceAtMost(mediaItem.durationSeconds)) }) {
                    Icon(
                        imageVector = Icons.Default.Forward10,
                        contentDescription = "Forward 10s",
                        tint = TextPrimary,
                        modifier = Modifier.size(22.dp)
                    )
                }

                IconButton(onClick = { onPositionChange(mediaItem.durationSeconds) }) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next",
                        tint = TextPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        }
    }
}

private fun formatTime(seconds: Int): String {
    val mins = seconds / 60
    val secs = seconds % 60
    return String.format("%d:%02d", mins, secs)
}