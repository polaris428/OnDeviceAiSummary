package com.com.on_deviceaisummary

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.chaquo.python.Python
import com.devlog.myapplication.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        findViewById<Button>(R.id.button).setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                if (!MyApplication.isPythonInitialized || MyApplication.model == null || MyApplication.tokenizer == null) {
                    Toast.makeText(this@MainActivity2, "모델이 아직 로드되지 않았습니다.", Toast.LENGTH_LONG).show()
                } else {
                    try {
                        val py = Python.getInstance()
                        val pyObj = py.getModule("summary")
                        val result = pyObj.callAttr("summarize", "코틀린 멀티 플랫폼은 모바일 뿐만 아니라 JVM, Native 및 JS 타깃을 지원하는 넓은 개념의 기술로 안드로이드 네이티브 앱 개발자들에게는 iOS 개발을 할 수 있는 사실에 큰 주목을 받게 됐다.")
                        findViewById<TextView>(R.id.textVIew).text= result.toString()
                        println(result.toString())  // 요약된 텍스트 출력
                    } catch (e: Exception) {
                        Toast.makeText(this@MainActivity2, "오류 발생: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }

    }


}