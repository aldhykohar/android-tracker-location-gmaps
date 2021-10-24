package com.aldhykohar.mytrackerlocation

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aldhykohar.mytrackerlocation.databinding.ActivityMainBinding
import com.aldhykohar.mytrackerlocation.service.ServiceActivity
import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.FirebaseDatabase




class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initClick()

        // Write a message to the database
        // Write a message to the database
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("message")

        myRef.setValue("Hello, World!")
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
                        ServiceActivity::class.java
                    )
                )
            }
        }
    }
}