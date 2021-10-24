package com.aldhykohar.mytrackerlocation.service

import android.app.IntentService
import android.content.Intent
import android.util.Log


/**
 * Created by aldhykohar on 10/24/2021.
 */
class MyIntentService : IntentService("My Intent Service") {

    init {
        instance = this
    }

    companion object {
        private lateinit var instance: MyIntentService

        var isRunning = false

        fun stopService() {
            Log.e("My Intent Service", "Service Is Stopping...")
            isRunning = false
            instance.stopSelf()
        }
    }

    override fun onHandleIntent(p0: Intent?) {
        try {
            isRunning = true
            while (isRunning) {
                Log.e("My Intent Service", "Service Is Running...")
                Thread.sleep(1000)
            }
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }
}