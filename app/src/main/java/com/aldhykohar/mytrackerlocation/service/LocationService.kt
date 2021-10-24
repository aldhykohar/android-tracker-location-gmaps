package com.aldhykohar.mytrackerlocation.service

import android.app.*
import android.content.Intent
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.aldhykohar.mytrackerlocation.MainActivity
import com.aldhykohar.mytrackerlocation.R
import com.aldhykohar.mytrackerlocation.utils.Constans.LOCATION
import com.aldhykohar.mytrackerlocation.utils.Constans.LOCATION_TRACKER
import com.google.android.gms.location.*
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


/**
 * Created by aldhykohar on 10/24/2021.
 */
class LocationService : Service() {

    companion object {
        const val TAG = "LocationTracker"

        private const val PACKAGE_NAME = "com.aldhykohar.mytrackerlocation.service.LocationService"

        const val ACTION_BROADCAST = "$PACKAGE_NAME.broadcast"

        const val EXTRA_LOCATION = "$PACKAGE_NAME.location"

        const val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"

        const val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"

        private const val UPDATE_INTERVAL_IN_MILLISECONDS = (1000 * 12).toLong()
        private const val FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2

        const val CHANNEL_ID = "location_tracker_id"
        val CHANNEL_NAME: CharSequence = "Malltronik"
    }

    lateinit var mLocationRequest: LocationRequest

    private var mReference: DatabaseReference? = null

    /**
     * The current location.
     */
    private var mLocation: Location? = null

    /**
     * Provides access to the Fused Location Provider API.
     */
    private lateinit var mFusedLocationClient: FusedLocationProviderClient

    /**
     * Callback for changes in location.
     */
    lateinit var mLocationCallback: LocationCallback

    private fun locationTracker() {
        mReference = FirebaseDatabase.getInstance().getReference(LOCATION_TRACKER)
            .child(LOCATION)
    }

    override fun onCreate() {
        super.onCreate()
        locationTracker()
        Log.e(TAG, "LocationTracker service created")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if (intent != null) {
            val action = intent.action
            if (action != null) {
                when (action) {
                    ACTION_START_FOREGROUND_SERVICE -> startForegroundService()
                    ACTION_STOP_FOREGROUND_SERVICE -> stopForegroundService()
                }
            }
        }
        return START_STICKY
    }


    private fun startForegroundService() {
        Log.i(TAG, "Start foreground service")
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mLocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)
                onNewLocation(locationResult.lastLocation)
            }
        }
        createLocationRequest()
        try {
            mFusedLocationClient.requestLocationUpdates(
                mLocationRequest,
                mLocationCallback, Looper.myLooper()
            )
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permission. Could not request updates. $unlikely")
        }
        getLastLocation()

        // Create Notification channel
        createNotificationChannel()

        // Create intent that will bring our app to the front, as if it was tapped in the app
        // launcher
        val showTaskIntent = Intent(applicationContext, MainActivity::class.java)
        showTaskIntent.action = Intent.ACTION_MAIN
        showTaskIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        val contentIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            showTaskIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val builder: Notification.Builder = Notification.Builder(applicationContext)
            .setContentTitle(getString(R.string.app_name))
            .setContentText("Layanan lokasi sedang berjalan")
            .setOnlyAlertOnce(true)
            .setWhen(System.currentTimeMillis())

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID) // Channel ID
        }
        startForeground(1, builder.build())

    }

    private fun stopForegroundService() {
        Log.i(TAG, "Stop Foreground Service")

        mFusedLocationClient.removeLocationUpdates(mLocationCallback)
        // Stop Foreground Service and Remove Notification
        stopForeground(true)
        // Stop the service
        stopSelf()
    }

    private fun createNotificationChannel() {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val notificationChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.BLUE
            notificationChannel.setSound(null, null)
            notificationChannel.setShowBadge(false)
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    /**
     * Sets the location request parameters.
     */
    private fun createLocationRequest() {
        mLocationRequest = LocationRequest()
        mLocationRequest.interval = UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.fastestInterval = FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    }

    private fun getLastLocation() {
        try {
            mFusedLocationClient.lastLocation
                .addOnCompleteListener { task: Task<Location?> ->
                    if (task.isSuccessful && task.result != null) {
                        mLocation = task.result
                    } else {
                        Log.w(TAG, "Failed to get location.")
                    }
                }
        } catch (unlikely: SecurityException) {
            Log.e(TAG, "Lost location permission.$unlikely")
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    private fun onNewLocation(location: Location) {
        val id = "12345"
        mReference?.child(id)?.setValue(location)
        Log.i(TAG, "New location: $location")
        mLocation = location

        // Notify anyone listening for broadcasts about the new location.
        val intent = Intent(ACTION_BROADCAST)
        intent.putExtra(EXTRA_LOCATION, location)
        LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
    }
}