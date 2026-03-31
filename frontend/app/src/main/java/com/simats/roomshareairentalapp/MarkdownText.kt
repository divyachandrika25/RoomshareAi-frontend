package com.simats.roomshareairentalapp

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MarkdownText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = AppTheme.TextPrimary,
    fontSize: androidx.compose.ui.unit.TextUnit = 14.sp,
    fontWeight: FontWeight? = null
) {
    // A simple markdown-aware renderer for Compose
    val lines = text.split("\n")
    
    Column(modifier = modifier) {
        lines.forEach { line ->
            if (line.trim().startsWith("* ") || line.trim().startsWith("- ")) {
                // Bullet point
                Row(modifier = Modifier.padding(vertical = 2.dp)) {
                    Text(
                        text = " • ",
                        color = color,
                        fontSize = fontSize,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = parseBoldItalic(line.trim().substring(2)),
                        color = color,
                        fontSize = fontSize,
                        fontWeight = fontWeight
                    )
                }
            } else if (line.trim().startsWith("#")) {
                // Simple Header
                val level = line.takeWhile { it == '#' }.length
                val headerText = line.drop(level).trim()
                Text(
                    text = headerText,
                    color = color,
                    fontSize = (fontSize.value + (4 - level) * 2).sp,
                    fontWeight = FontWeight.ExtraBold,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            } else if (line.isNotBlank()) {
                // Normal line
                Text(
                    text = parseBoldItalic(line),
                    color = color,
                    fontSize = fontSize,
                    fontWeight = fontWeight,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            }
        }
    }
}

private fun parseBoldItalic(text: String) = buildAnnotatedString {
    var i = 0
    while (i < text.length) {
        when {
            // Bold: **text**
            text.startsWith("**", i) -> {
                val end = text.indexOf("**", i + 2)
                if (end != -1) {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(text.substring(i + 2, end))
                    }
                    i = end + 2
                } else {
                    append(text[i])
                    i++
                }
            }
            // Italic: *text* (avoid confusion with bullet points at the start)
            text.startsWith("*", i) && !text.startsWith("**", i) -> {
                val end = text.indexOf("*", i + 1)
                if (end != -1 && end != i + 1) { // ensure it's not and empty span or bold
                     withStyle(style = SpanStyle(fontStyle = FontStyle.Italic)) {
                        append(text.substring(i + 1, end))
                    }
                    i = end + 1
                } else {
                    append(text[i])
                    i++
                }
            }
            else -> {
                append(text[i])
                i++
            }
        }
    }
}
