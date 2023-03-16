package com.syoon.compose.chapter05

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import java.util.*
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TextFieldDemo1()
        }
    }
}

/**
 * StateDemo 예제
 */
@Composable
@Preview
fun TextFieldDemo1() {
    val state = remember { mutableStateOf(TextFieldValue("")) }
    TextFieldDemo2(state)
}

@Composable
fun TextFieldDemo2(state: MutableState<TextFieldValue>) {
    TextField(
        value = state.value,
        onValueChange = {
            state.value = it
        },
        placeholder = { Text("Hello") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
@Preview
fun RememberWithKeyDemo() {
    var key by remember { mutableStateOf(false) }
    val date by remember(key) { mutableStateOf(Date()) }
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(date.toString())
        Button(onClick = { key = !key }) {
            Text(text = stringResource(id = R.string.click))
        }
    }
}

@Composable
@Preview
fun SimpleStateDemo1() {
    val num = remember { mutableStateOf(Random.nextInt(0, 10)) }
    Text(text = num.value.toString())
}

@Composable
@Preview
fun SimpleStateDemo2() {
    val num by remember { mutableStateOf(Random.nextInt(0, 10)) }
    Text(text = num.toString())
}

@Composable
@Preview
fun SimpleStatelessComposable1() {
    Text(text = "Hello Compose")
}

@Composable
fun SimpleStatelessComposable2(text: State<String>) {
    Text(text = text.value)
}