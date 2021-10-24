package com.aldhykohar.mytrackerlocation

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.aldhykohar.mytrackerlocation.databinding.ActivityMainBinding
import com.aldhykohar.mytrackerlocation.service.ServiceActivity
import com.aldhykohar.mytrackerlocation.utils.CheckPermission.hasLocationForegroundPermission
import com.google.firebase.database.DatabaseReference

import com.google.firebase.database.FirebaseDatabase


class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        requestPermission()
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
                        ServiceActivity::class.java
                    )
                )
            }
        }
    }

    private fun requestPermission() {
        val permissionToRequest = mutableListOf<String>()
        if (!hasLocationForegroundPermission()) {
            permissionToRequest.add(Manifest.permission.ACCESS_COARSE_LOCATION)
            permissionToRequest.add(Manifest.permission.ACCESS_FINE_LOCATION)
            permissionToRequest.add(Manifest.permission.ACCESS_NETWORK_STATE)
        }

        if (permissionToRequest.isNotEmpty()) ActivityCompat.requestPermissions(
            this,
            permissionToRequest.toTypedArray(),
            0
        ) else {
            Toast.makeText(this, "Permission is granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 0 && grantResults.isNotEmpty()) {
            for (i in grantResults.indices) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Log.d("PermissionRequest", "${permissions[i]} granted")
                }
            }
        }
    }
}