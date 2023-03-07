package com.syoon.compose.chapter06.ui.viewmodels

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Color
val AndroidGreen = Color(0xFF3DDC84)
val AndroidGreenDark = Color(0xFF20B261)
val Orange = Color(0xFFFFA500)
val OrangeDark = Color(0xFFCC8400)

// Theme
private val DarkColorPalette = darkColors(
    primary = AndroidGreen,
    primaryVariant = AndroidGreenDark,
    secondary = Orange,
    secondaryVariant = OrangeDark,
)

// Theme
private val LightColorPalette = lightColors(
    primary = AndroidGreen,
    primaryVariant = AndroidGreenDark,
    secondary = Orange,
    secondaryVariant = OrangeDark,
)

@Composable
fun UnitConverterTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), // darkTheme 사용 감지
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }
    /**
     * 머터리얼 테마의 메인 진입점
     * 커스텀 색상, 모양, 텍스트 스타일을 매개변수로 전달 받음
     * 값 미설정 시 기본 값이 사용됨
     */
    MaterialTheme(
        colors = colors,
        // Type
        typography = Typography(button = TextStyle(fontSize = 24.sp)),
        // Shape
        shapes = Shapes(small = CutCornerShape(8.dp)),
        content = content,
    )
}

// 테마 중첩 예제
@Composable
@Preview
fun MaterialThemeDemo() {
    MaterialTheme(
        typography = Typography(
            h1 = TextStyle(color = Color.Red)
        ),
    ) {
        Row {
            Text(
                text = "Hello",
                style = MaterialTheme.typography.h1,
            )
            Spacer(modifier = Modifier.width(2.dp))
            // 중첩 테마 적용
            MaterialTheme(
                typography = Typography(
                    h1 = TextStyle(color = Color.Blue) // 부모의 테마 재정의
                )
            ) {
                Text(
                    text = "Compose",
                    style = MaterialTheme.typography.h1,
                )
            }
        }
    }
}
