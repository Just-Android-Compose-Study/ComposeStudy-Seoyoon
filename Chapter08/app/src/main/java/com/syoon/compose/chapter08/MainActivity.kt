package com.syoon.compose.chapter08

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Slider
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
                //MultipleValuesAnimationDemo()
                //AnimatedVisibility()
                SizeChangedAnimationDemo()
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

    Log.d(
        "transition",
        "${transition.isRunning}, ${transition.segment}, ${transition.currentState}"
    )

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

@Composable
@Preview
fun AnimatedVisibility() {
    var visible by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = {
            visible = !visible
        }) {
            Text(
                text = stringResource(
                    id = if (visible)
                        R.string.hide
                    else
                        R.string.show
                )
            )
        }
        // visible 매개변수가 변경되면 콘텐츠를 노출하거나 사라지가 하는 애니메이션 수행
        AnimatedVisibility(
            visible = visible,
            // '+' 연산자 조합으로 Fade, Expand/Shrink, Slide 조합 가능 (순서 상관x)
            enter = slideInHorizontally() + expandIn(),
            exit = slideOutVertically() + fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .background(color = Color.Red)
                    .size(128.dp)
            )
        }
    }
}

@Composable
fun SizeChangedAnimationDemo() {
    var size by remember {
        mutableStateOf(1F)
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Slider(
            value = size,
            valueRange = (1F..4F),
            steps = 3,
            onValueChange = { // 1. 슬라이더 움직일 시 호출
                size = it // 2. 전달받은 값을 size에 할당
            },
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Text(
            text = stringResource(id = R.string.lines), // '#1\n#2\n#3\n#4\n#5'
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray)
                    // Composable 함수 내부의 사이즈가 변경되거나 컨턴츠 변경이 될 경우
                    // 동적으로 사이즈 변화를 애니메이션으로 처리
                .animateContentSize(animationSpec = snap(10000)),
            maxLines = size.toInt(), // 3. size 값을 maxLines로 사용
            color = Color.Blue
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Chapter08Theme {
        //StateChangeDemo()
        //SingleValueAnimationDemo()
        //MultipleValuesAnimationDemo()
        //AnimatedVisibility()
        SizeChangedAnimationDemo()
    }
}