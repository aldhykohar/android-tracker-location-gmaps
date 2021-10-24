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
                Intent(this@ServiceActivity, MyIntentService::class.java).also {
                    startService(it)
                    tvServiceInfo.text = getString(R.string.service_running)
                }
            }

            mbStop.setOnClickListener {
                MyIntentService.stopService()
                tvServiceInfo.text = getString(R.string.service_stopped)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        MyIntentService.stopService()
    }
}