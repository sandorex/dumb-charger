package io.github.sandorex.the_dumb_charger_app

import android.app.Service
import android.content.*
import android.os.BatteryManager
import android.os.IBinder
import android.util.Log
import androidx.preference.PreferenceManager

private val TAG = BuildConfig.APPLICATION_ID + "." + MonitorService::class.simpleName

class MonitorService : Service() {
    private var prefs: SharedPreferences? = null

    // is charging using enabled charging method
    private var monitoring = false

    private val receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            val extras = intent.extras ?: return
            /*
            Intent: android.intent.action.BATTERY_CHANGED
            technology : Li-poly
            icon-small : 17303234
            max_charging_voltage : 0
            health : 2
            max_charging_current : 0
            status : 2
            plugged : 2
            present : true
            charge_counter : 0
            level : 31
            scale : 100
            temperature : 260
            voltage : 3943
            invalid_charger : 0
             */

            when (extras.getInt("status")) {
                BatteryManager.BATTERY_PLUGGED_AC -> {
                    monitoring = true
                }

                BatteryManager.BATTERY_PLUGGED_WIRELESS -> {
//                    if (prefs.getBoolean())
                }

                BatteryManager.BATTERY_PLUGGED_USB -> {

                }

                else -> {
                    monitoring = false

                    // not charging so just return
                    return
                }
            }

            Log.e(TAG, "Intent: " + intent.action)

            val bundle = intent.extras
            if (bundle != null) {
                for (key in bundle.keySet()) {
                    Log.e(TAG, key + " : " + if (bundle[key] != null) bundle[key] else "NULL")
                }
            }
//            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
//            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
//            val batteryLevel = level * 100 / scale.toFloat()
//            batteryTxt.setText("$batteryPct%")
//            if (batteryLevel >= maxCharge) {
//                Toast.makeText(this@MainActivity, "Battery reached max charge%", Toast.LENGTH_SHORT).show()
//                signal()
//            }

        }
    }

    override fun onCreate() {
        super.onCreate()

        if (!TorchManager.hasCamera())
            TorchManager.findCamera(this)

        this.prefs = PreferenceManager.getDefaultSharedPreferences(this)
    }

    override fun onDestroy() {
        super.onDestroy()

        RUNNING = false

        this.unregisterReceiver(this.receiver)

//        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
//        preferences.edit().putBoolean("enabled", false).apply()
    }

    override fun onStartCommand(intent1: Intent?, flags: Int, startId: Int): Int {
        RUNNING = true

        this.registerReceiver(this.receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

//        this.slow_charge = preferences.getBoolean("slow_charge", true)
//        preferences.edit().putBoolean("enabled", true).apply()


        return START_STICKY
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    companion object {
        private var RUNNING = false

        fun isRunning() = RUNNING
    }
}