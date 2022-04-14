package io.github.sandorex.the_dumb_charger_app

import android.content.*
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {
    private lateinit var handler: Handler
//    private var maxCharge: Float = 39.0F;
//    private var maxCharge: Float = 85.0F;

    // todo run automatically when charging
    // todo if after signaling twice charging isnt stopped then stop the app as it clearly isnt the dumb charger
    private val updateTask = object : Runnable {
        override fun run() {
            update()
            handler.postDelayed(this, 5000)
        }
    }

    private val batinfoReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(ctxt: Context, intent: Intent) {
            Toast.makeText(this@MainActivity, "Intent: " + intent.action, Toast.LENGTH_SHORT).show()

            val bundle = intent.extras
            if (bundle != null) {
                for (key in bundle.keySet()) {
                    Log.e("io.github.sandorex.the_dumb_charger_app", key + " : " + if (bundle[key] != null) bundle[key] else "NULL")
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        TorchManager.findCamera(applicationContext) // TODO:

        val btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener {
            val service = Intent(this, MonitorService::class.java)
            this.stopService(service)
//            val bm = applicationContext.getSystemService(BATTERY_SERVICE) as BatteryManager
//            val batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY)
//            val status = bm.getIntProperty(BatteryManager.BATTERY_PLUGGED_USB)

//            Toast.makeText(this@MainActivity, "Your battery level is $batLevel%, $status", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this, SettingsActivity::class.java))

//            signal()
//            Toast.makeText(this@MainActivity, "You clicked", Toast.LENGTH_SHORT).show()
        }

//        val filter = IntentFilter()
//        filter.addAction(Intent.ACTION_BATTERY_CHANGED)
////        filter.addAction(Intent.ACTION_POWER_CONNECTED)
////        filter.addAction(Intent.ACTION_POWER_DISCONNECTED)
//        this.registerReceiver(this.batinfoReceiver, filter)

        val service = Intent(this, MonitorService::class.java)
        this.startService(service)

//        handler = Handler(Looper.getMainLooper())
//        handler.post(updateTask)
//        this.registerReceiver(this.batinfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

//    override fun finish() {
//        // is this even needed?
//        handler.removeCallbacks(updateTask)
//
//        super.finish()
//    }

//    override fun onPause() {
//        super.onPause()
//        handler.removeCallbacks(updateTask)
//    }
//
//    override fun onResume() {
//        super.onResume()
//        handler.post(updateTask)
//    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)

        // create the action button
        menuInflater.inflate(R.menu.action_bar, menu)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        // start settings activity on settings action press
        R.id.action_settings -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }

        else -> {
            // let the superclass handle it
            super.onOptionsItemSelected(item)
        }
    }

    private fun update() {
        TorchManager.toggleTorch()
//        Toast.makeText(this@MainActivity, "update", Toast.LENGTH_SHORT).show()
//        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
//            this.applicationContext.registerReceiver(null, ifilter)
//        }

    }
}