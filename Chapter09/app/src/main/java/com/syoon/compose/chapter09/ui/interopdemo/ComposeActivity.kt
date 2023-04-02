package com.syoon.compose.chapter09.ui.interopdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.syoon.compose.chapter09.R
import com.syoon.compose.chapter09.databinding.CustomBinding

class ComposeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MyViewModel by viewModels()

        viewModel.setSliderValue(intent.getFloatExtra(KEY, 0F))

        setContent {
            ViewIntegrationDemo(viewModel) {
                Log.d("###", "onCreate에 호출")
                val i = Intent(
                    this,
                    ViewActivity::class.java
                )
                i.putExtra(KEY, viewModel.sliderValue.value)
                startActivity(i)
            }
        }
    }
}

@Composable
fun ViewIntegrationDemo(viewModel: MyViewModel, onClick: () -> Unit) {

    // 관찰 가능한 sliderValue값을 가져옴 -> MutableState<Float?> 타입으로 저장
    val sliderValueState = viewModel.sliderValue.observeAsState()
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text(text = stringResource(id = R.string.compose_activity))
            })
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Slider(
                modifier = Modifier.fillMaxWidth(),
                value = sliderValueState.value ?: 0F,
                onValueChange = {
                    // ViewModel 프로퍼티의 값 갱신 -> 모든 관찰자 호출 -> 뷰 컴포저블 갱신
                    viewModel.setSliderValue(it)
                },
            )

            // viewBinding으로 xml View를 composable 함수 내에 배치
            AndroidViewBinding(
                modifier = Modifier.fillMaxWidth(),
                factory = CustomBinding::inflate // 빌드 중에 생성 및 갱신
            ) {
                Log.d("###", "호출")
                // 업데이트 되는 뷰 - CustomBinding이 인플레이트 된 후 바로 호출
                this.textView.text = sliderValueState.value.toString()
                this.button.setOnClickListener {
                    onClick()
                }
            }
        }

    }
}