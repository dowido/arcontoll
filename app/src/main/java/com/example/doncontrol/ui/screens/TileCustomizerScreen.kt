package com.example.doncontrol.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doncontrol.model.TileCategory
import com.example.doncontrol.ui.components.ControlTileCard
import com.example.doncontrol.ui.components.QuickSliderCard
import com.example.doncontrol.ui.theme.*
import com.example.doncontrol.viewmodel.ControlCenterViewModel

@Composable
fun TileCustomizerScreen(
    viewModel: ControlCenterViewModel,
    onDoneClick: () -> Unit
) {
    val activeTiles by viewModel.activeTiles.collectAsState()
    val availableTiles by viewModel.availableTiles.collectAsState()
    val displayBrightness by viewModel.displayBrightness.collectAsState()
    val mediaVolume by viewModel.mediaVolume.collectAsState()
    val currentPage by viewModel.currentPage.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundDark)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(top = 16.dp, bottom = 40.dp)
        ) {
            // Top App Bar
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = onDoneClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Text(
                        text = "Tile Customizer",
                        color = TextPrimary,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // Done Button Pill
                        Button(
                            onClick = onDoneClick,
                            colors = ButtonDefaults.buttonColors(containerColor = ActiveLavender, contentColor = TextDark),
                            shape = RoundedCornerShape(20.dp),
                            contentPadding = PaddingValues(horizontal = 18.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Done",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        // Profile Avatar
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(TileInactive),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = "Profile",
                                tint = TextPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Sub-header Row: Editing Page & Reset Layout
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(ActiveMint)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Editing Page ${currentPage + 1} of 2",
                            color = ActiveMint,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Reset Layout Button
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(TileInactive)
                                .clickable { viewModel.resetLayout() }
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.RestartAlt,
                                contentDescription = "Reset Layout",
                                tint = TextPrimary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Reset Layout",
                                color = TextPrimary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Default.Visibility,
                                contentDescription = "Preview",
                                tint = TextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            // Instruction Banner Card
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = CardBackground)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.OpenWith,
                                contentDescription = "Drag",
                                tint = ActiveLavender,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "Tap - to remove or drag to reposition tiles.",
                                color = TextPrimary,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(TileInactive)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${activeTiles.size} Active",
                                color = TextSecondary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Active Tiles Grid Section
            item {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    for (i in activeTiles.indices step 2) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val tile1 = activeTiles[i]
                            ControlTileCard(
                                tile = tile1,
                                onClick = { },
                                modifier = Modifier.weight(1f),
                                isEditMode = true,
                                onRemoveClick = { viewModel.removeTileFromActive(tile1.id) }
                            )

                            if (i + 1 < activeTiles.size) {
                                val tile2 = activeTiles[i + 1]
                                ControlTileCard(
                                    tile = tile2,
                                    onClick = { },
                                    modifier = Modifier.weight(1f),
                                    isEditMode = true,
                                    onRemoveClick = { viewModel.removeTileFromActive(tile2.id) }
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
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(16.dp, 8.dp)
                            .clip(CircleShape)
                            .background(ActiveLavender)
                    )
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 4.dp)
                            .size(8.dp)
                            .clip(CircleShape)
                            .background(TileInactive)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))
            }

            // Quick Sliders Section
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "QUICK SLIDERS",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Always Fixed",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

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
                        title = "Ring",
                        value = mediaVolume,
                        onValueChange = { viewModel.updateVolume(it) },
                        icon = Icons.Default.VolumeUp,
                        modifier = Modifier.weight(1f),
                        fillColor = TileInactive
                    )
                }

                Spacer(modifier = Modifier.height(28.dp))
            }

            // Available Tiles Header
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.GridView,
                            contentDescription = "Available Tiles",
                            tint = ActiveLavender,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Available Tiles",
                            color = TextPrimary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Text(
                        text = "Tap + to add",
                        color = TextSecondary,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Available Tiles Categorized
            val categories = TileCategory.values()
            categories.forEach { category ->
                val tilesInCategory = availableTiles.filter { it.category == category }
                if (tilesInCategory.isNotEmpty()) {
                    item {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Icon(
                                imageVector = when (category) {
                                    TileCategory.CONNECTIVITY -> Icons.Default.CellTower
                                    TileCategory.PRIVACY_SECURITY -> Icons.Default.Security
                                    TileCategory.DEVICE_DISPLAY -> Icons.Default.Smartphone
                                    TileCategory.SMART_HOME -> Icons.Default.HomeWork
                                },
                                contentDescription = category.displayName,
                                tint = ActiveMint,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = category.displayName,
                                color = ActiveMint,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    item {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            for (i in tilesInCategory.indices step 2) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    val tile1 = tilesInCategory[i]
                                    ControlTileCard(
                                        tile = tile1,
                                        onClick = { },
                                        modifier = Modifier.weight(1f),
                                        isEditMode = true,
                                        onAddClick = { viewModel.addTileToActive(tile1.id) }
                                    )

                                    if (i + 1 < tilesInCategory.size) {
                                        val tile2 = tilesInCategory[i + 1]
                                        ControlTileCard(
                                            tile = tile2,
                                            onClick = { },
                                            modifier = Modifier.weight(1f),
                                            isEditMode = true,
                                            onAddClick = { viewModel.addTileToActive(tile2.id) }
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }

            // Bottom Footer Info Banner
            item {
                Spacer(modifier = Modifier.height(20.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 24.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.TouchApp,
                        contentDescription = "Info",
                        tint = TextSecondary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Tap Done above to save changes to Android Control Shade",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}