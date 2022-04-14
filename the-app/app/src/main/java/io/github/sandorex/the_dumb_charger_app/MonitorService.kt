package io.github.sandorex.the_dumb_charger_app

import android.app.Service
import android.content.*
import android.os.BatteryManager
import android.os.IBinder
import android.util.Log
import androidx.preference.PreferenceManager

private val TAG = MonitorService::class.simpleName

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


            Log.e(TAG, "Intent: " + intent.action)

            val bundle = intent.extras
            if (bundle != null) {
                for (key in bundle.keySet()) {
                    Log.e(TAG, key + " : " + if (bundle[key] != null) bundle[key] else "NULL")
                }
            }

            val min_charge = prefs?.getInt(getString(R.string.key_min_charge), 0) ?: 0
            val max_charge = prefs?.getInt(getString(R.string.key_max_charge), 85) ?: 85
            val attempts = prefs?.getInt(getString(R.string.key_attempts), 2) ?: 2
//            val max_voltage // TODO:
//            val min_voltage

            val status = extras.getInt(BatteryManager.EXTRA_STATUS)
            val level = extras.getInt(BatteryManager.EXTRA_LEVEL)

            when (status) {
                BatteryManager.BATTERY_STATUS_CHARGING -> {

                    if (level >= max_charge) {
                        for (var i = 0; i < attempts; ++i) {

                        }
                    }
                }

                BatteryManager.BATTERY_STATUS_DISCHARGING, BatteryManager.BATTERY_STATUS_NOT_CHARGING  -> {

                }

                else -> return
            }

            if (status != BatteryManager.BATTERY_STATUS_CHARGING && status != Batt) {
                if (prefs?.getInt(getString(R.string.key_min_charge), 0) != 0) {
                    monitoring
                }
            }



//            when (extras.getInt("status")) {
//                BatteryManager.BATTERY_PLUGGED_AC -> {
//                    monitoring = true
//                }
//
//                BatteryManager.BATTERY_PLUGGED_WIRELESS -> {
//                    if (prefs?.getBoolean(getString(R.string.key_on_wireless), true) == true) {
//                        monitoring = true
//                    } else {
//                        monitoring = false
//                        return
//                    }
//                }
//
//                BatteryManager.BATTERY_PLUGGED_USB -> {
//                    // TODO find a way to check if connected to usb host
////                    if (prefs?.getBoolean(getString(R.string.key_on_slow_charge), true) == true) {
////                        monitoring = true
////                    } else {
////                        monitoring = false
////                        return
////                    }
//                }
//
//                else -> {
//                    monitoring = false
//
//                    // not charging so just return
//                    return
//                }
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

        Log.d(TAG, "Stopping monitor service")

        this.unregisterReceiver(this.receiver)

//        val preferences: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
//        preferences.edit().putBoolean("enabled", false).apply()
    }

    override fun onStartCommand(intent1: Intent?, flags: Int, startId: Int): Int {
        RUNNING = true

        // android.hardware.action.USB_STATE
        Log.d(TAG, "Starting monitor service")

        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_BATTERY_CHANGED)
        filter.addAction("android.hardware.action.USB_STATE")

        this.registerReceiver(this.receiver, filter)
//        this.registerReceiver(this.receiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED))

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