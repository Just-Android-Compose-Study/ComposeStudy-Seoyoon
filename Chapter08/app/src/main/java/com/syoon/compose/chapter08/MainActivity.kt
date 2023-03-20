package com.syoon.compose.chapter08

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.syoon.compose.chapter08.ui.theme.Chapter08Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Chapter08Theme {
                //StateChangeDemo()
                SingleValueAnimationDemo()
            }
        }
    }
}

@Composable
fun StateChangeDemo() {
    var toggled by remember {
        mutableStateOf(false)
    }
    val color = if (toggled)
        Color.White
    else
        Color.Red

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            toggled = !toggled
        }) {
            Text(
                stringResource(id = R.string.toggle)
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 32.dp)
                .background(color = color)
                .size(128.dp)
        )
    }
}

@Composable
fun SingleValueAnimationDemo() {
    var toggled by remember {
        mutableStateOf(false)
    }
    // State<Color> 인스턴스 반환
    val color by animateColorAsState(
        targetValue = if (toggled) // targetValue가 변경될 때마다 애니메이션 실행
            Color.White
        else
            Color.Red,
        animationSpec = spring(stiffness = Spring.StiffnessVeryLow), // 애니메이션 사양 조절
        finishedListener = { color -> Log.d("color", "$color") } // 애니메이션 끝났을 떄 알림 리스너
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            toggled = !toggled
        }) {
            Text(
                stringResource(id = R.string.toggle)
            )
        }
        Box(
            modifier = Modifier
                .padding(top = 32.dp)
                .background(color = color)
                .size(128.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Chapter08Theme {
        //StateChangeDemo()
        SingleValueAnimationDemo()
    }
}