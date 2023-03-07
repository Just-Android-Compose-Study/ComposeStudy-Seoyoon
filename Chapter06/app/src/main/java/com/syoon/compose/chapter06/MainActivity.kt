package com.syoon.compose.chapter06

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import com.syoon.compose.chapter06.ui.viewmodels.ComposeUnitConverterTheme
import com.syoon.compose.chapter06.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = ViewModelFactory(Repository(applicationContext)) // factory 객체 생성
        setContent {
            ComposeUnitConverter(factory)
        }
    }
}

@Composable
fun ComposeUnitConverter(factory: ViewModelFactory) {
    val menuItems = listOf("Item #1", "Item #2")
    val scaffoldState = rememberScaffoldState() // 자식에 따라 다른 상태 기억하기
    val snackbarCoroutineScope = rememberCoroutineScope() // Coroutine Scope
    ComposeUnitConverterTheme {
        Scaffold(
            scaffoldState = scaffoldState,
            topBar = {
                     ComposeUnitConverterTopBar(menuItems = menuItems) { s ->
                         snackbarCoroutineScope.launch {
                             // showSnackbar()는 suspend 함수이므로 Coroutine Scope 내에서 호출
                             scaffoldState.snackbarHostState.showSnackbar(s)
                         }
                     }
            },
        ) {
        }
    }
}

@Composable
fun ComposeUnitConverterTopBar(menuItems: List<String>, onClick: (String) -> Unit) {
    var menuOpened by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Text(text = stringResource(id = R.string.app_name))
        },
        actions = { // 툴바에 여러 메뉴를 만들 수 있는 속성
            Box {
                // 클릭 enabled에 따라 UI 상호작용
                IconButton(onClick = { menuOpened = true }) {
                    Icon(Icons.Default.MoreVert, "")
                }
                // 메뉴 출력
                DropdownMenu(
                    expanded = menuOpened, // open, close
                    onDismissRequest = { menuOpened = false }, // 메뉴의 바깥 영역 터치 시 닫힘
                ) {
                    menuItems.forEachIndexed { index, s ->
                        if (index > 0) Divider()
                        DropdownMenuItem(onClick = {
                            menuOpened = false
                            onClick(s) // 상응하는 메뉴 아이템이 선택됐을 떄 호출됨
                        }) {
                            Text(s)
                        }
                    }
                }
            }
        }
    )
}
