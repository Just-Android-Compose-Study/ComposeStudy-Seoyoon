package com.syoon.compose.chapter08

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
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
                //SingleValueAnimationDemo()
                MultipleValuesAnimationDemo()
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

@Composable
@Preview
fun MultipleValuesAnimationDemo() {
    var toggled by remember {
        mutableStateOf(false)
    }
    val transition = updateTransition( // 트렌지션을 구성하고 반환함
        targetState = toggled,
        label = "toggledTransition"
    )

    // 트렌지션 상태를 반영하는 프로퍼티
    transition.isRunning // 애니메이션이 실행 중인지 여부를 판단
    transition.segment // 초기상태와 현재 진행중인 트렌지션 대상 상태 포함
    transition.currentState // 트랜지션의 현재 상태

    Log.d("transition", "${transition.isRunning}, ${transition.segment}, ${transition.currentState}")

    // 자식 애니메이션 animate...()
    val borderWidth by transition.animateDp(label = "borderWidthTrasition") { state ->
        if (state)
            10.dp
        else
            1.dp
    }

    // 자식 애니메이션 animate...()
    val degrees by transition.animateFloat(label = "degreesTransition") { state ->
        if (state) -90F
        else
            0F
    }

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
                stringResource(R.string.toggle)
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(top = 32.dp)
                .border(
                    width = borderWidth,
                    color = Color.Black
                )
                .size(128.dp)
        ) {
            Text(
                text = stringResource(id = R.string.app_name),
                modifier = Modifier.rotate(degrees = degrees)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Chapter08Theme {
        //StateChangeDemo()
        //SingleValueAnimationDemo()
        MultipleValuesAnimationDemo()
    }
}