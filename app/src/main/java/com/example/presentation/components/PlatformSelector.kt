package com.example.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.domain.model.DriverPlatform
import com.example.presentation.theme.GacorEmerald
import com.example.presentation.theme.GrabGreen
import com.example.presentation.theme.GojekGreen
import com.example.presentation.theme.InDriveGreen
import com.example.presentation.theme.Slate950

@Composable
fun PlatformSelector(
    selectedPlatform: DriverPlatform,
    onPlatformSelected: (DriverPlatform) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        DriverPlatform.values().forEach { platform ->
            val isSelected = platform == selectedPlatform
            val platformBrandColor = when (platform) {
                DriverPlatform.GRAB_CAR -> GrabGreen
                DriverPlatform.GO_CAR -> GojekGreen
                DriverPlatform.IN_DRIVE -> InDriveGreen
            }

            OutlinedButton(
                onClick = { onPlatformSelected(platform) },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .testTag("platform_button_${platform.name.lowercase()}"),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = if (isSelected) platformBrandColor else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                    contentColor = if (isSelected) Slate950 else MaterialTheme.colorScheme.onSurface
                ),
                border = BorderStroke(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) platformBrandColor else MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                )
            ) {
                Text(
                    text = platform.displayName,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                )
            }
        }
    }
}
