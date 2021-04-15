package com.kaps.siliconreportkotlin.view.activity

import android.Manifest.permission
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.kaps.siliconreportkotlin.R
import com.kaps.siliconreportkotlin.databinding.ActivitySplashBinding
import com.kaps.siliconreportkotlin.viewmodel.SplashViewModel

class SplashActivity : AppCompatActivity() {

    var PERMISSION_REQUEST_CODE = 111
    var binding: ActivitySplashBinding? = null
    var snackbar: Snackbar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding!!.viewModel = SplashViewModel(this)
        binding!!.viewModel!!.checkPermission()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSION_REQUEST_CODE -> if (grantResults.size > 0) {
                val cameraAccepted =
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                val readStorageAccepted =
                    grantResults[1] == PackageManager.PERMISSION_GRANTED
                val writeStorageAccepted =
                    grantResults[2] == PackageManager.PERMISSION_GRANTED
                if (cameraAccepted && writeStorageAccepted && readStorageAccepted) {
                    binding!!.viewModel!!.onProgress()
                } else {
                    if (snackbar != null) {
                        snackbar!!.dismiss()
                    }
                    snackbar = Snackbar
                        .make(
                            binding!!.constraintLayout,
                            "You need to allow access the permissions",
                            Snackbar.LENGTH_INDEFINITE
                        )
                        .setAction("OK") {
                            snackbar!!.dismiss()
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                requestPermissions(
                                    arrayOf(
                                        permission.CAMERA,
                                        permission.READ_EXTERNAL_STORAGE,
                                        permission.WRITE_EXTERNAL_STORAGE
                                    ),
                                    PERMISSION_REQUEST_CODE
                                )
                            } else {
                                ActivityCompat.requestPermissions(
                                    this@SplashActivity, arrayOf(
                                        permission.CAMERA,
                                        permission.READ_EXTERNAL_STORAGE,
                                        permission.WRITE_EXTERNAL_STORAGE
                                    ), PERMISSION_REQUEST_CODE
                                )
                            }
                        }
                    snackbar!!.setActionTextColor(Color.YELLOW)
                    val sbView = snackbar!!.view
                    val textView =
                        sbView.findViewById<View>(R.id.snackbar_text) as TextView
                    textView.setTextColor(Color.WHITE)
                    snackbar!!.show()
                    return
                }
            }
        }
    }
}