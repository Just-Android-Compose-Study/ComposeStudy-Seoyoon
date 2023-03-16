package com.syoon.compose.chapter06

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.syoon.compose.chapter06.ui.screen.ComposeUnitConverterScreen
import com.syoon.compose.chapter06.ui.screen.DistancesConverter
import com.syoon.compose.chapter06.ui.screen.TemperatureConverter
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
    val naviController = rememberNavController() // NavHostController 인스턴스의 참조 얻기
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

            bottomBar = {
                ComposeUnitConverterBottomBar(naviController)
            }
        ) {
            ComposeUnitConverterNavHost(
                naviController = naviController,
                factory = factory,
                modifier = Modifier.padding(it)
            )
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

@Composable
fun ComposeUnitConverterBottomBar(naviController: NavHostController) {
    BottomNavigation {
        // 현재 NaviBackStackEntry 가져오기
        val naviBackStackEntry by naviController.currentBackStackEntryAsState()
        // NaviDestination에 접근
        val currentDestination = naviBackStackEntry?.destination
        ComposeUnitConverterScreen.screens.forEach { screen ->
            BottomNavigationItem(
                /**
                 * 중첩된 탐색을 사용하고 있는 경우 hierarchy 도우미 메서드를 통해
                 * 항목의 경로와 현재 대상 및 그 상위 대상의 경로를 비교하고
                 * 각 BottomNavigationItem의 선택된 상태를 확인
                 */
                selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                onClick = {
                    // 내비게이션 이동
                    naviController.navigate(screen.route) {
                        // 화면 인스턴스를 한 개만 생성한다는 의미
                        launchSingleTop = true
                    }
                },
                label = {
                    Text(text = stringResource(id = screen.label))
                },
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = stringResource(
                            id = screen.label
                        )
                    )
                },
                alwaysShowLabel = true

            )

        }
    }
}

/**
 * NavHost
 * 경로(graph)와 컴포저블 함수 간의 매핑이 이루어 진다
 */
@Composable
fun ComposeUnitConverterNavHost(
    naviController: NavHostController,
    factory: ViewModelFactory,
    modifier: Modifier,
) {
    NavHost(
        navController = naviController,
        startDestination = ComposeUnitConverterScreen.route_temperature,
        modifier = modifier,
    ) {
        // 목적지1
        composable(ComposeUnitConverterScreen.route_temperature) {
            TemperatureConverter(
                viewModel = viewModel(factory = factory)
            )
        }
        // 목적지2
        composable(ComposeUnitConverterScreen.route_distances) {
            DistancesConverter(
                viewModel = viewModel(factory = factory)
            )
        }
    }
}
