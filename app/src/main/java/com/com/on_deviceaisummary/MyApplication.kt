package com.com.on_deviceaisummary

import android.app.Application
import com.chaquo.python.Python
import com.chaquo.python.PyObject
import com.chaquo.python.android.AndroidPlatform
import kotlinx.coroutines.*

class MyApplication : Application() {

    companion object {
        var model: PyObject? = null
        var tokenizer: PyObject? = null
        var isPythonInitialized = false
    }

    override fun onCreate() {
        super.onCreate()
        if (!Python.isStarted()) {
            Python.start(AndroidPlatform(this@MyApplication))
        }
        CoroutineScope(Dispatchers.IO).launch {
            initializePython()
            loadPythonModules()
        }
    }

    private suspend fun initializePython() {
        withContext(Dispatchers.IO) {
            if (!Python.isStarted()) {
                Python.start(AndroidPlatform(this@MyApplication))
            }
            isPythonInitialized = true
        }
    }

    private suspend fun loadPythonModules() {
        withContext(Dispatchers.IO) {
            val py = Python.getInstance()
            val pyModule = py.getModule("summary")
            model = pyModule.callAttr("load_model")
            tokenizer = pyModule.callAttr("load_tokenizer")
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        CoroutineScope(Dispatchers.IO).launch {
            cleanupPythonModules()
        }
    }

    private suspend fun cleanupPythonModules() {
        withContext(Dispatchers.IO) {
            val py = Python.getInstance()
            val pyModule = py.getModule("summary")
            pyModule.callAttr("cleanup")
        }
    }
}
