package com.syoon.compose.chapter09

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.MutableLiveData
import com.google.zxing.BarcodeFormat
import com.journeyapps.barcodescanner.BarcodeCallback
import com.journeyapps.barcodescanner.BarcodeResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import com.journeyapps.barcodescanner.DefaultDecoderFactory

class MainActivity : ComponentActivity() {

    private lateinit var barcodeView: DecoratedBarcodeView

    // text를 상태로 관찰
    private val text = MutableLiveData("")

    // permission
    private val requestPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                barcodeView.resume()
            }
        }


    @SuppressLint("InflateParams")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 1. 레이아웃 인플레이트 및 barcodeView 초기화
        val root = layoutInflater.inflate(R.layout.layout, null) // 컴포즈 UI에 뷰 계층 넣는데 사용
        barcodeView = root.findViewById(R.id.barcode_scanner)

        // 2. 바코드 스캐너에서 인식할 수 있는 바코드 형식 결정
        val formats = listOf(BarcodeFormat.QR_CODE, BarcodeFormat.CODE_39)
        barcodeView.barcodeView.decoderFactory = DefaultDecoderFactory(formats)

        // 3. 바코드 스캐너 콜백 등록
        val callback = object : BarcodeCallback {
            override fun barcodeResult(result: BarcodeResult) { // 스캔 결과 처리
                // text가 null이거나 이미 처리된 결과와 동일할 경우
                if (result.text == null || result.text == text.value) {
                    return
                }
                text.value = result.text // 스캔 결과 할당
            }
        }
        barcodeView.decodeContinuous(callback) // 지속적인 스캐닝 프로세스 수행

        // 4. 바코드 스캐너 초기화 및 콜백 실행
        barcodeView.initializeFromIntent(intent) // intent 매개변수 전달 -> 액티비티가 시작될 때 전달된 인텐트에서 바코드 스캔 수행

        // 5. Compose UI 설정
        setContent {
            val state = text.observeAsState()
            state.value?.let {
                ZxingDemo(root, it)
            }

        }
    }

    override fun onResume() {
        super.onResume()
        // 카메라 권한 요청
        requestPermission.launch(Manifest.permission.CAMERA)
    }

    override fun onPause() {
        super.onPause()
        // 바코드 뷰 정지
        barcodeView.pause()
    }

    // 하드웨어 버튼(키) 이벤트 처리
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return barcodeView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event)
    }
}

@Composable
fun ZxingDemo(root: View, value: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter,
    ) {
        // Android View를 Composable 함수 내에 배치하는데 사용됨
        // factory 매개변수를 통해 뷰 생성
        AndroidView(modifier = Modifier.fillMaxSize(), factory = {
            root
        })
        if (value.isNotBlank()) {
            // 스캔 결과물 보여주기
            Text(
                modifier = Modifier.padding(16.dp),
                text = value,
                color = Color.White,
                style = MaterialTheme.typography.h4,
            )
        }
    }
}