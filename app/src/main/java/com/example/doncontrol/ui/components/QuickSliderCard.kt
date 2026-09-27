package com.example.doncontrol.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness7
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doncontrol.ui.theme.*

@Composable
fun QuickSliderCard(
    title: String,
    value: Float, // 0f..1f
    onValueChange: (Float) -> Unit,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    fillColor: Color = ActiveLavender
) {
    var componentWidth by remember { mutableStateOf(1f) }

    Box(
        modifier = modifier
            .height(56.dp)
            .clip(RoundedCornerShape(28.dp))
            .background(TileInactive)
            .onGloballyPositioned { componentWidth = it.size.width.toFloat() }
            .pointerInput(Unit) {
                detectTapGestures { offset ->
                    if (componentWidth > 0f) {
                        val newValue = (offset.x / componentWidth).coerceIn(0f, 1f)
                        onValueChange(newValue)
                    }
                }
            }
            .pointerInput(Unit) {
                detectTransformGestures { _, pan, _, _ ->
                    if (componentWidth > 0f) {
                        val delta = pan.x / componentWidth
                        val newValue = (value + delta).coerceIn(0f, 1f)
                        onValueChange(newValue)
                    }
                }
            }
    ) {
        // Filled Progress Portion
        Box(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(value)
                .clip(RoundedCornerShape(28.dp))
                .background(fillColor)
        )

        // Overlay Text & Icon
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = if (value > 0.3f) TextDark else TextPrimary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    text = title,
                    color = if (value > 0.45f) TextDark else TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = "${(value * 100).toInt()}%",
                color = if (value > 0.85f) TextDark else TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}