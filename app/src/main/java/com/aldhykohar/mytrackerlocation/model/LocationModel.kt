package com.aldhykohar.mytrackerlocation.model


/**
 * Created by aldhykohar on 10/24/2021.
 */
class LocationModel(
    var accuracy: Int = 0,
    var altitude: Int = 0,
    var bearing: Int = 0,
    var bearingAccuracyDegrees: Int = 0,
    var speed: Int = 0,
    var speedAccuracyMetersPerSecond: Int = 0,
    var verticalAccuracyMeters: Int = 0,
    val complete: Boolean = false,
    var fromMockProvider: Boolean = false,
    val provider: String? = null,
    val time: Long = 0,
    var elapsedRealtimeNanos: Long = 0,
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)