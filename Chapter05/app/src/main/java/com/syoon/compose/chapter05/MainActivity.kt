package com.syoon.compose.chapter05

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import java.util.*
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //TextFieldDemo1()
            FlowOfEventsDemo()
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

/**
 * flowEventDemo 예제
 */

@SuppressLint("UnrememberedMutableState")
@Composable
@Preview
fun FlowOfEventsDemo() {
    val strCelsius = stringResource(id = R.string.celsius)
    val strFahrenheit = stringResource(id = R.string.fahrenheit)
    val temperature = remember { mutableStateOf("") }

    // textField에 값이 들어갈 때마다 재구성 -> 라디오버튼 선택 값 기억
    val scale = remember { mutableStateOf(R.string.celsius) }
    //val scale = mutableStateOf(R.string.celsius)

    Log.d("scale", "$scale")
    var convertedTemperature by remember { mutableStateOf(Float.NaN) }
    val calc = {
        val temp = temperature.value.toFloat()
        convertedTemperature = if (scale.value == R.string.celsius)
            (temp * 1.8F) + 32F
        else
            (temp - 32F) / 1.8F
    }
    val result = remember(convertedTemperature) {
        if (convertedTemperature.isNaN())
            ""
        else
            "${convertedTemperature}${
                if (scale.value == R.string.celsius)
                    strFahrenheit
                else strCelsius
            }"
    }
    val enabled = temperature.value.isNotBlank()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TemperatureTextField(
            temperature = temperature,
            modifier = Modifier.padding(bottom = 16.dp),
            callback = calc
        )
        TemperatureScaleButtonGroup(
            selected = scale,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Button(
            onClick = calc,
            enabled = enabled
        ) {
            Text(text = stringResource(id = R.string.convert))
        }
        if (result.isNotEmpty()) {
            Text(
                text = result,
                style = MaterialTheme.typography.h3
            )
        }
    }
}

@Composable
fun TemperatureTextField(
    temperature: MutableState<String>,
    modifier: Modifier = Modifier,
    callback: () -> Unit
) {
    TextField(
        value = temperature.value,
        onValueChange = {
            temperature.value = it
        },
        // hint
        placeholder = {
            Text(text = stringResource(id = R.string.placeholder))
        },
        modifier = modifier,
        keyboardActions = KeyboardActions(onAny = {
            callback()
        }),
        // 키보드 옵션 구성 선택
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = ImeAction.Done
        ),
        singleLine = true
    )
}

@Composable
fun TemperatureScaleButtonGroup(
    selected: MutableState<Int>,
    modifier: Modifier = Modifier
) {
    val sel = selected.value
    val onClick = { resId: Int -> selected.value = resId }
    Row(modifier = modifier) {
        TemperatureRadioButton(
            selected = sel == R.string.celsius,
            resId = R.string.celsius,
            onClick = onClick
        )
        TemperatureRadioButton(
            selected = sel == R.string.fahrenheit,
            resId = R.string.fahrenheit,
            onClick = onClick,
            modifier = Modifier.padding(start = 16.dp)
        )
    }
}

@Composable
fun TemperatureRadioButton(
    selected: Boolean,
    resId: Int,
    onClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        RadioButton(
            selected = selected,
            onClick = {
                onClick(resId)
            }
        )
        Text(
            text = stringResource(resId),
            modifier = Modifier
                .padding(start = 8.dp)
        )
    }
}