package com.sms.checker.forwarder.feature.dev.presentation.screen.colorpalette.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.sms.checker.forwarder.feature.dev.R
import com.sms.checker.forwarder.feature.dev.presentation.screen.colorpalette.management.screen.DevColorPaletteAction
import com.sms.checker.forwarder.feature.dev.presentation.screen.colorpalette.management.screen.DevColorPaletteState
import com.sms.checker.forwarder.framework.theme.SmsCheckerTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun DevColorPaletteScreen(
    state: DevColorPaletteState,
    action: DevColorPaletteAction,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.feature_dev_item_color_palette),
                        style = SmsCheckerTheme.typography.titleLarge,
                        color = SmsCheckerTheme.color.onBackground
                    )
                },
                navigationIcon = {
                    IconButton(
                        content = {
                            Icon(
                                modifier = Modifier.size(24.dp),
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Navigation Back",
                                tint = SmsCheckerTheme.color.onBackground,
                            )
                        },
                        onClick = action.onBackPressed
                    )

                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = SmsCheckerTheme.color.background
                )
            )
        },
        containerColor = SmsCheckerTheme.color.background
    ) { paddingValues ->
        val items = colorItems()
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(16.dp)
        ) {
            items(items) { (name, color) ->
                ColorPaletteItem(name = name, color = color)
            }
        }
    }
}

@Composable
private fun ColorPaletteItem(
    name: String,
    color: Color,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(color, RoundedCornerShape(8.dp))
        )
        Column {
            Text(
                text = name,
                style = SmsCheckerTheme.typography.bodyLarge,
                color =  SmsCheckerTheme.color.onBackground
            )
            Text(
                text = color.toHex(),
                style = SmsCheckerTheme.typography.labelMedium,
                color = SmsCheckerTheme.color.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun colorItems(): List<Pair<String, Color>> = listOf(
    "primary" to SmsCheckerTheme.color.primary,
    "onPrimary" to SmsCheckerTheme.color.onPrimary,
    "primaryContainer" to SmsCheckerTheme.color.primaryContainer,
    "onPrimaryContainer" to SmsCheckerTheme.color.onPrimaryContainer,
    "background" to SmsCheckerTheme.color.background,
    "onBackground" to SmsCheckerTheme.color.onBackground,
    "surface" to SmsCheckerTheme.color.surface,
    "surfaceVariant" to SmsCheckerTheme.color.surfaceVariant,
    "onSurface" to SmsCheckerTheme.color.onSurface,
    "onSurfaceVariant" to SmsCheckerTheme.color.onSurfaceVariant,
    "outline" to SmsCheckerTheme.color.outline,
    "outlineVariant" to SmsCheckerTheme.color.outlineVariant,
    "error" to SmsCheckerTheme.color.error,
    "onError" to SmsCheckerTheme.color.onError,
)

private fun Color.toHex(): String {
    return "#%02X%02X%02X".format(
        (red * 255).toInt(),
        (green * 255).toInt(),
        (blue * 255).toInt(),
    )
}