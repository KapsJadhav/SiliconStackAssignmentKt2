package com.kaps.siliconreportkotlin.viewmodel

import android.Manifest.permission
import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import com.kaps.siliconreportkotlin.view.activity.MainActivity
import com.kaps.siliconreportkotlin.view.activity.SplashActivity

class SplashViewModel(var context: Context) : ViewModel() {

    var PERMISSION_REQUEST_CODE = 111

    fun checkPermission() {
        ActivityCompat.requestPermissions(
            (context as Activity),
            arrayOf(
                permission.CAMERA,
                permission.READ_EXTERNAL_STORAGE,
                permission.WRITE_EXTERNAL_STORAGE
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    fun onProgress() {
        Thread(Runnable {
            for (i in 1..100) {
                if (i == 100) {
                    onSuccess()
                }
                Thread.sleep(10)
            }
        }).start()
    }

    fun onSuccess() {
        context.startActivity(Intent(context, MainActivity::class.java))
        val activity: SplashActivity = context as SplashActivity
        activity.finish()
    }

}