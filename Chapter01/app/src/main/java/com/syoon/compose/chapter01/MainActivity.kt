package com.syoon.compose.chapter01

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.syoon.compose.chapter01.ui.theme.Chapter01Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Chapter01Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Hello()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(
        text = stringResource(R.string.hello, name),
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.subtitle1
    )
}

@Preview(
    widthDp = 100,
    heightDp = 100,
    showSystemUi = true,
    group = "my-group-1"
)
@Composable
fun Welcome() {
    Text(
        text = stringResource(R.string.welcome),
        style = MaterialTheme.typography.subtitle1
    )
}

// 열, 텍스트 필드, 버튼 사용
@Composable
fun TextAndButton(name: MutableState<String>, nameEntered: MutableState<Boolean>) {
    Row(modifier = Modifier.padding(top = 8.dp)) {
        TextField(
            value = name.value,
            onValueChange = {
                name.value = it
            },
            placeholder = {
                Text(text = stringResource(id = R.string.hint))
            },
            modifier = Modifier
                .alignByBaseline()
                .weight(1.0f),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                capitalization = KeyboardCapitalization.Words,
            ),
            keyboardActions = KeyboardActions(onAny = {
                nameEntered.value = true
            })
        )
        Button(
            onClick = {
                nameEntered.value = true
            }, modifier = Modifier
                .alignByBaseline()
                .padding(8.dp)
        ) {
            Text(text = stringResource(id = R.string.done))
        }
    }
}

// 인사말 출력
@Preview(group = "my-group-1")
@Composable
fun Hello() {
    /*상태 생성 및 관리
    * mutableStateOf(): 상태 생성
    * remember: 상태 기억
    */
    val name = remember{ mutableStateOf("") }
    val nameEntered = remember {
        mutableStateOf(false)
    }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        if (nameEntered.value) {
            Greeting(name.value)
        } else {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Welcome()
                TextAndButton(name, nameEntered)
            }
        }
    }
}

// 미리보기
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Chapter01Theme {
        Greeting("Android")
    }
}