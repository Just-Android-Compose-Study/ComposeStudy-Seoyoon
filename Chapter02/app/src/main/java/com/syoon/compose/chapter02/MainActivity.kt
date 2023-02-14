package com.syoon.compose.chapter02

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.syoon.compose.chapter02.ui.theme.Chapter02Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Chapter02Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Factorial()
                }
            }
        }
    }
}

@Composable
fun Factorial() {
    var expanded by remember { mutableStateOf(false) }  // 상태 값 -> Composable 함수에서 parameter로 사용되면 이 값이 변경될 때 마다 Recomposition이 발생한다.
    var text by remember { mutableStateOf(factorialAsString(0)) } // 상태 값
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            modifier = Modifier.clickable {
                expanded = true
            },
            text = text,
            style = MaterialTheme.typography.h2
        )
        DropdownMenu(
            expanded = expanded,    // expanded 값에 따라 dropdown이 열리고 닫힌다.
            onDismissRequest = {
                expanded = false
            }) {
            for (n in 0 until 10) {
                DropdownMenuItem(onClick = {
                    expanded = false
                    text = factorialAsString(n)
                }) {
                    Text("${n.toString()}!")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Chapter02Theme {
        Factorial()
    }
}

// 출력 텍스트 생성
fun factorialAsString(n: Int): String {
    var result = 1L
    for (i in 1..n) {
        result *= i
    }
    return "$n! = $result"
}

