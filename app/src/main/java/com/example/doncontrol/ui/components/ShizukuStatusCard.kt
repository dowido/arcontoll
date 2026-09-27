package com.example.doncontrol.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.doncontrol.shizuku.ShizukuStatus
import com.example.doncontrol.ui.theme.*

@Composable
fun ShizukuStatusCard(
    status: ShizukuStatus,
    onRequestPermission: () -> Unit,
    modifier: Modifier = Modifier
) {
    if (status == ShizukuStatus.CONNECTED) return // Hide when successfully connected

    val (bg, icon, title, desc, buttonText) = when (status) {
        ShizukuStatus.PERMISSION_REQUIRED -> Tuple5(
            Color(0xFF332910),
            Icons.Default.Warning,
            "Shizuku Permission Required",
            "Grant permission to control system settings without root.",
            "Grant Permission"
        )
        ShizukuStatus.DISCONNECTED -> Tuple5(
            Color(0xFF2C1E20),
            Icons.Default.Terminal,
            "Shizuku Not Running",
            "Start Shizuku service via wireless debugging or ADB to enable system toggles.",
            "Connect Shizuku"
        )
        else -> Tuple5(
            TileInactive,
            Icons.Default.Warning,
            "Shizuku Service Unavailable",
            "Please install or enable Shizuku on your Android device.",
            "Check Status"
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = bg)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = "Shizuku Status",
                tint = if (status == ShizukuStatus.PERMISSION_REQUIRED) Color(0xFFFFD54F) else RemoveRed,
                modifier = Modifier.size(28.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = desc,
                    color = TextSecondary,
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = onRequestPermission,
                colors = ButtonDefaults.buttonColors(containerColor = ActiveLavender, contentColor = TextDark),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                shape = RoundedCornerShape(16.dp)
            ) {
                Text(
                    text = buttonText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

private data class Tuple5<A, B, C, D, E>(
    val a: A, val b: B, val c: C, val d: D, val e: E
)