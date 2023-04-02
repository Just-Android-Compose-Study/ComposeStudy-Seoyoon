package com.syoon.compose.chapter09.ui.interopdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.syoon.compose.chapter09.R
import com.syoon.compose.chapter09.databinding.ViewActivityLayoutBinding

const val KEY = "key"

class ViewActivity : AppCompatActivity() {

    private lateinit var binding: ViewActivityLayoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MyViewModel by viewModels()

        // intent에서 'KEY'에 해당하는 Float값 가져오기
        viewModel.setSliderValue(intent.getFloatExtra(KEY, 0F))

        // xml 레이아웃 인플레이트
        binding = ViewActivityLayoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 변경되는 값 observing
        viewModel.sliderValue.observe(this) {
            binding.slider.value = it
        }

        // 슬라이더 값 변경될 때 호출
        binding.slider.addOnChangeListener { _, value, _ ->
            viewModel.setSliderValue(value)  // ViewModel 프로퍼티 갱신 -> 모든 관찰자 호출 -> 뷰, 컴포저블 갱신
        }

        // xml View에서 컴포저블을 사용할 수 있게 만들어주는 class
        binding.composeView.run {
            // 뷰 내부 구성처리 전략 설정 -> 뷰가 윈도우에서 분리될 때마다 View의 모든 Composable 함수를 삭제하는 전략
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindow)

            // 컴포저블 함수를 composeView에 표시
            setContent {
                val sliderValue = viewModel.sliderValue.observeAsState()
                sliderValue.value?.let {
                    Log.d("###", "$it")
                    ComposeDemo(it) {
                        // click
                        val i = Intent(
                            context,
                            ComposeActivity::class.java
                        )
                        i.putExtra(KEY, it)
                        startActivity(i)
                    }
                }
            }
        }
    }
}

@Composable
fun ComposeDemo(value: Float, onClick: () -> Unit) {
    Log.d("###", "ComposeDemo 재구성 $value")
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box( // Text를 수직 중앙에 위치시키기 위해 Box로 감싸기
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.secondary)
                .height(64.dp),
            contentAlignment = Alignment.Center
        ) {

        }
        Text(
            text = value.toString()
        )
        Button(
            onClick = onClick,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.compose_activity))
        }
    }
}