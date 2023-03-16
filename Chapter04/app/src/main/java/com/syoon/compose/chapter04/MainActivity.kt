package com.syoon.compose.chapter04

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Checkbox
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.MeasurePolicy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //PredefinedLayoutsDemo()
            //ConstraintLayoutDemo()
            CustomLayoutDemo()
        }
    }
}

/**
 * 기본 구성 요소 예제
 */
@Composable
@Preview
fun PredefinedLayoutsDemo() {
    val red = remember { mutableStateOf(true) }
    val green = remember { mutableStateOf(true) }
    val blue = remember { mutableStateOf(true) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        CheckboxWithLabel(
            label = stringResource(id = R.string.red),
            state = red
        )
        CheckboxWithLabel(
            label = stringResource(id = R.string.green),
            state = green
        )
        CheckboxWithLabel(
            label = stringResource(id = R.string.blue),
            state = blue
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.LightGray)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Cyan)
            ) {

            }

            Text(
                text = "테스트",
                modifier = Modifier.padding(16.dp),
            )

            if (red.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red)
                )
            }
            if (green.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                        .background(Color.Green)
                )
            }
            if (blue.value) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(64.dp)
                        .background(Color.Blue)
                )
            }
        }
    }
}

@Composable
fun CheckboxWithLabel(label: String, state: MutableState<Boolean>) { //다른 컴포저블 함수 재구성
    Row(
        modifier = Modifier.clickable {
            state.value = !state.value
        }, verticalAlignment = Alignment.CenterVertically //박스와 텍스트 수직 정렬 맞춤
    ) {
        Checkbox(
            checked = state.value,
            onCheckedChange = {
                state.value = it
            }
        )
        Text(
            text = label,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

/**
 * Constraint 예제
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
fun ConstraintLayoutDemo() {
    val red = remember { mutableStateOf(true) }
    val green = remember { mutableStateOf(true) }
    val blue = remember { mutableStateOf(true) }
    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        val (cbRed, cbGreen, cbBlue, boxRed, boxGreen, boxBlue) = createRefs()

        ColumnWithText()

//        CheckboxWithLabel(
//            label = stringResource(id = R.string.red),
//            state = red,
//            modifier = Modifier.constrainAs(cbRed) {
//                top.linkTo(parent.top)
//            }
//        )
//        CheckboxWithLabel(
//            label = stringResource(id = R.string.green),
//            state = green,
//            modifier = Modifier.constrainAs(cbGreen) {
//                top.linkTo(cbRed.bottom)
//            }
//        )
//        CheckboxWithLabel(
//            label = stringResource(id = R.string.blue),
//            state = blue,
//            modifier = Modifier.constrainAs(cbBlue) {
//                top.linkTo(cbGreen.bottom)
//            }
//        )
//        if (red.value) {
//            Box(
//                modifier = Modifier
//                    .background(Color.Red)
//                    .constrainAs(boxRed) {
//                        start.linkTo(parent.start)
//                        end.linkTo(parent.end)
//                        top.linkTo(cbBlue.bottom, margin = 16.dp)
//                        bottom.linkTo(parent.bottom)
//                        width = Dimension.fillToConstraints
//                        height = Dimension.fillToConstraints
//                    }
//            )
//        }
//        if (green.value) {
//            Box(
//                modifier = Modifier
//                    .background(Color.Green)
//                    .constrainAs(boxGreen) {
//                        start.linkTo(parent.start, margin = 32.dp)
//                        end.linkTo(parent.end, margin = 32.dp)
//                        top.linkTo(cbBlue.bottom, margin = (16 + 32).dp)
//                        bottom.linkTo(parent.bottom, margin = 32.dp)
//                        width = Dimension.fillToConstraints
//                        height = Dimension.fillToConstraints
//                    }
//            )
//        }
//        if (blue.value) {
//            Box(
//                modifier = Modifier
//                    .background(Color.Blue)
//                    .constrainAs(boxBlue) {
//                        start.linkTo(parent.start, margin = 64.dp)
//                        end.linkTo(parent.end, margin = 64.dp)
//                        top.linkTo(cbBlue.bottom, margin = (16 + 64).dp)
//                        bottom.linkTo(parent.bottom, margin = 64.dp)
//                        width = Dimension.fillToConstraints
//                        height = Dimension.fillToConstraints
//                    }
//            )
//        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun CheckboxWithLabel(
    label: String,
    state: MutableState<Boolean>,
    modifier: Modifier = Modifier
) {
    ConstraintLayout(modifier = modifier.clickable {
        state.value = !state.value
    }) {
        val (checkbox, text) = createRefs() // 참조 생성
        Checkbox(
            checked = state.value,
            onCheckedChange = {
                state.value = it
            },
            modifier = Modifier.constrainAs(checkbox) {
            }
        )
        Text(
            text = label,
            modifier = Modifier.constrainAs(text) {
                start.linkTo(checkbox.end, margin = 8.dp)
                top.linkTo(checkbox.top)
                bottom.linkTo(checkbox.bottom)
            }
        )
    }
}

@Preview
@Composable
fun ColumnWithText() {
    Column { // 부모의 최대 너비는 텍스트 높이에 영향을 미친다 (가로, 세로모드 테스트)
        Text(
            text = "Android UI development with Jetpack Compose", // 길이가 긴 텍스트
            style = MaterialTheme.typography.h3
        )

        Text(
            text = "Hello Compose",
            style = MaterialTheme.typography.h5.merge(TextStyle(color = Color.Red))
        )

    }
}

/**
 * Custom 레이아웃 예제
 */
@Composable
@Preview
fun CustomLayoutDemo() {
    SimpleFlexBox {
        for (i in 0..42) {
            ColoredBox()
        }
    }
}

@Composable
fun ColoredBox() {
    Box(
        modifier = Modifier
            .border(
                width = 2.dp,
                color = Color.Black //2dp의 검은색 테두리
            )
            .background(randomColor())
            .width((40 * randomInt123()).dp)
            .height((10 * randomInt123()).dp)
    )
}

@Composable
fun SimpleFlexBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Layout(
        modifier = modifier,
        content = content,
        measurePolicy = simpleFlexboxMeasurePolicy()
    )
}

private fun simpleFlexboxMeasurePolicy(): MeasurePolicy =
    MeasurePolicy { measurables, constraints -> // measurables은 측정할 element, constraint는 부모로부터 받은 min/max의 height/width의 범위
        // measure로 사이즈를 측정하여 배치 가능한 사이즈 얻는다
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }
        Log.d("즉정값", "$placeables")
        layout( // MeasureScope에 포함된 layout() 함수
            constraints.maxWidth,
            constraints.maxHeight
        ) {
            var yPos = 0
            var xPos = 0
            var maxY = 0

            // 위치 계산
            placeables.forEach { placeable ->
                if (xPos + placeable.width >
                    constraints.maxWidth
                ) {
                    xPos = 0
                    yPos += maxY
                    maxY = 0
                }
                placeable.placeRelative(
                    x = xPos,
                    y = yPos
                )
                xPos += placeable.width
                if (maxY < placeable.height) {
                    maxY = placeable.height
                }
            }
        }
    }

private fun randomInt123() = Random.nextInt(1, 4)

private fun randomColor() = when (randomInt123()) {
    1 -> Color.Red
    2 -> Color.Green
    else -> Color.Blue
}

/**
 * Layout test
 */
@Composable
fun TestLayout() {
    Column(
        modifier = Modifier
            .background(Color.Blue)
            .padding(30.dp)
    ) {

        Box(
            modifier = Modifier
                //.fillMaxSize(0.6f)
                .background(Color.Black)
        ) {

        }

    }
}

@Composable
fun TestLayout2() {
    Column(
        modifier = Modifier
            //.fillMaxSize()
            .background(Color.Yellow)
    ) {
    }
}
