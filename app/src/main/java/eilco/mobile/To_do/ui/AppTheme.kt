package eilco.mobile.To_do.ui

import androidx.compose.material.MaterialTheme
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun AppTheme(
    themeColor: Color,
    content: @Composable () -> Unit
) {
    val colors = lightColors(
        primary = themeColor,
        primaryVariant = themeColor,
        secondary = themeColor
    )

    MaterialTheme(
        colors = colors,
        typography = MaterialTheme.typography,
        shapes = MaterialTheme.shapes,
        content = content
    )
}
