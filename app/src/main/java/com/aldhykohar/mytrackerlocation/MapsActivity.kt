package com.aldhykohar.mytrackerlocation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.aldhykohar.mytrackerlocation.databinding.ActivityMapsBinding
import com.aldhykohar.mytrackerlocation.model.LocationModel
import com.aldhykohar.mytrackerlocation.utils.Constans.LOCATION
import com.aldhykohar.mytrackerlocation.utils.Constans.LOCATION_TRACKER
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.*

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    private lateinit var mRefLocation: DatabaseReference

    private lateinit var listenerLocation: ValueEventListener

    private var userMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        retrieveLocation()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        userMarker = mMap.addMarker(
            MarkerOptions().position(LatLng(0.0, 0.0))
                .title("Marker")
        )

    }

    private fun retrieveLocation() {
        mRefLocation = FirebaseDatabase.getInstance().getReference(LOCATION_TRACKER).child(LOCATION)
            .child("12345")
        mRefLocation.keepSynced(true)
        listenerLocation = mRefLocation.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val location = snapshot.getValue(LocationModel::class.java)
                location?.run {

                    userMarker?.position = LatLng(location.latitude, location.longitude)
                    updateCameraMap(LatLng(this.latitude, this.longitude))

                }
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun updateCameraMap(point1: LatLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(point1, 14.0f))
    }

    override fun onDestroy() {
        mRefLocation.removeEventListener(listenerLocation)
        super.onDestroy()
    }
}