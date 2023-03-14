package com.syoon.compose.chapter07

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LaunchedEffectDemo()
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition") // **** Composable 함수의 lifecycle과 직접적으로 연결되어 있지 않음을 의미
@Composable
fun LaunchedEffectDemo() {
    /**
     * rememberSaveable를 사용하면 화면 회전 시 0초로 초기화 되고 프로세스는 중단되지 않는다
     * remember를 사용하면 화면 회전 시 0초로 초기화 됨과 동시에 프로세스가 종료된다
     */
    var clickCount by rememberSaveable { mutableStateOf(0) }
    var counter by remember { mutableStateOf(0) }

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //Log.d("clickCount", "$clickCount") // 호출되는 순서 확인
        Row {
            Log.d("###", "Row Composable 재구성")
            Log.d("###", "clickCount : $clickCount")
            Button(onClick = { clickCount += 1 }) {
                Text(
                    text = if (clickCount == 0)
                        stringResource(id = R.string.start)
                    else
                        stringResource(id = R.string.restart)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(enabled = clickCount > 0, onClick = { clickCount = 0 }) {
                Text(text = stringResource(id = R.string.stop))
            }

            if (clickCount > 0) {
                DisposableEffect(clickCount) { // 1. clickCount 상태가 변경될 때마다 호출됨
                    println("init: clickCount is $clickCount")
                    onDispose {
                        println("dispose: clickCount is $clickCount")
                        scope.cancel()
                    }
                }
                Log.d("###", "counter: $counter")

                // **** rememberCoroutineScope로 코루틴스코프 객체 생성
                scope.launch {
                    //counter = 0
                    while (isActive) {
                        counter += 1
                        delay(1000)
                    }
                }
//                LaunchedEffect(clickCount) {
//                    // 1. 비동기 작업을 수행하고
//                    counter = 0
//                    while (isActive) { // 현재 job이 active인지 아닌지 반환
//                        counter += 1 // 2. 상태가 변경된다
//                        delay(1000)
//                    }
//                }
            }
        }
        // 3. 상태에 따라 화면이 구성된다
        Text(
            text = "$counter",
            style = MaterialTheme.typography.h3,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    LaunchedEffectDemo()
}