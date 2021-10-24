package com.aldhykohar.mytrackerlocation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aldhykohar.mytrackerlocation.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initClick()
    }

    private fun initClick() {
        with(binding) {
            mbTracker.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        MapsActivity::class.java
                    )
                )
            }

            mbService.setOnClickListener {
                startActivity(
                    Intent(
                        this@MainActivity,
                        MapsActivity::class.java
                    )
                )
            }
        }
    }
}