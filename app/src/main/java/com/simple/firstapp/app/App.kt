package com.simple.firstapp.app

import android.app.Application
import com.log.filelogger.FileLogger
import com.simple.firstapp.BuildConfig
import java.io.File

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
    }

    private fun initLogger(){
        if (BuildConfig.DEBUG){
            FileLogger.initFileLogger(this.getExternalFilesDir(null).toString() + File.separator + "log")
            FileLogger.d("SessionStarted")
        }
    }
}