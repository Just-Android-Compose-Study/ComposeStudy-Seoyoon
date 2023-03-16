package com.syoon.compose.chapter03

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.DrawModifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.min

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            OrderDemo()

//            BoxWithConstraints(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier.fillMaxSize()
//            ) {
//                Column(
//                    modifier = Modifier.width(min(400.dp, maxWidth)),
//                    horizontalAlignment = Alignment.CenterHorizontally
//                ) {
//                    Log.d("###", "재구성")
//                    val color = remember { mutableStateOf(Color.Magenta) }
//                    ColorPicker(color) // slider가 움직일 때마다 color 값이 변경됨
//                    Text(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .background(color.value),
//                        text = "#${color.value.toArgb().toUInt().toString(16)}",
//                        textAlign = TextAlign.Center,
//                        style = MaterialTheme.typography.h4.merge(
//                            TextStyle(
//                                color = color.value.complementary() // 텍스트 컬러는 보색으로 나타남
//                            )
//                        )
//                    )
//                }
//            }
        }
    }
}

/**
 * ColorPicker 예제
 */
@Composable
fun ColorPicker(color: MutableState<Color>) {
    // 색 공간을 기반으로 하는 float 반환
    val red = color.value.red
    val green = color.value.green
    val blue = color.value.blue
    Column {
        Slider(
            value = red, // 슬라이더가 나타낼 값 (0~1F 사이)
            onValueChange = { // 슬라이더 드래그 or 선 터치 시 호출
                color.value = Color(
                    it, // onValueChange로 얻는 현재 슬라이더의 새로운 색상
                    green,
                    blue,
                )
            })
        Slider(
            value = green,
            onValueChange = { color.value = Color(red, it, blue) })
        Slider(
            value = blue,
            onValueChange = { color.value = Color(red, green, it) })
    }
}

// Color의 확장함수 - 전달받은 색상의 보색을 연산
fun Color.complementary() = Color(
    red = 1F - red,
    green = 1F - green,
    blue = 1F - blue
)

/**
 * ModifierDemo 예제
 */

// 체이닝에서 변경자의 위치는 변경자가 언제 실행되는지를 결정한다
@Composable
fun OrderDemo() {
    var color by remember { mutableStateOf(Color.Blue) }
    Box(
        modifier = Modifier
            .fillMaxSize()
//            .clickable { // padding 앞으로 이동시키면 간격을 클릭해도 동작한다
//                color = if (color == Color.Blue)
//                    Color.Red
//                else
//                    Color.Blue
//            }
            .padding(32.dp)
            .border(BorderStroke(width = 2.dp, color = color))
            .background(Color.LightGray)
            .clickable {
                color = if (color == Color.Blue)
                    Color.Red
                else
                    Color.Blue
            }


    )
}
