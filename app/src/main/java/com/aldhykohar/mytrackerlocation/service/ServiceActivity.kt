package com.aldhykohar.mytrackerlocation.service

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aldhykohar.mytrackerlocation.R
import com.aldhykohar.mytrackerlocation.databinding.ActivityServiceBinding

class ServiceActivity : AppCompatActivity() {

    private val binding: ActivityServiceBinding by lazy {
        ActivityServiceBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initClick()
    }

    private fun initClick() {
        with(binding) {
            mbStart.setOnClickListener {
                Intent(
                    this@ServiceActivity,
                    LocationService::class.java
                ).also { service ->
                    service.action = LocationService.ACTION_START_FOREGROUND_SERVICE
                    tvServiceInfo.text = getString(R.string.service_stopped)
                    startService(service)
                }
            }

            /*mbStop.setOnClickListener {
                Intent(this@ServiceActivity, LocationService::class.java).also {
                    tvServiceInfo.text = getString(R.string.service_stopped)
                    stopService(it)
                }
            }*/
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MyIntentService.stopService()
    }
}