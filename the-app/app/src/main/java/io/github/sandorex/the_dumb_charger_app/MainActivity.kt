package io.github.sandorex.the_dumb_charger_app

import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.os.BatteryManager
import android.os.Bundle
import android.os.Handler
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private lateinit var cameraManager: CameraManager
    private lateinit var cameraId: String
    private lateinit var handler: Handler
    private var torchState: Boolean = false
    private var maxCharge: Float = 39.0F;
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
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val batteryLevel = level * 100 / scale.toFloat()
//            batteryTxt.setText("$batteryPct%")
            if (batteryLevel >= maxCharge) {
                Toast.makeText(this@MainActivity, "Battery reached max charge%", Toast.LENGTH_SHORT).show()
                signal()
            }

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val isFlashAvailable = applicationContext.packageManager
            .hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)

        if (!isFlashAvailable) {
            val alert = AlertDialog.Builder(this).create()
            alert.setTitle("Oops!")
            alert.setMessage("Flash not available in this device...")
            alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK") { _, _ -> finish() }
            alert.show()
        }

        cameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        try {
            cameraId = cameraManager.cameraIdList[0]
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }

        val btn = findViewById<Button>(R.id.button)
        btn.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))

//            signal()
//            Toast.makeText(this@MainActivity, "You clicked", Toast.LENGTH_SHORT).show()
        }

//        handler = Handler(Looper.getMainLooper())
//        this.registerReceiver(this.batinfoReceiver, IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }

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
        menuInflater.inflate(R.menu.action_bar, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.action_settings -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }

        else -> {
            // let the superclass handle it
            super.onOptionsItemSelected(item)
        }
    }


    private fun setTorch(state: Boolean) {
        try {
            torchState = !torchState
            cameraManager.setTorchMode(cameraId, torchState)
        } catch (e: CameraAccessException) {
            e.printStackTrace()
        }
    }

    private fun signal() {
        Thread.sleep(50)
        setTorch(true)
        Thread.sleep(100)
        setTorch(false)
    }

    private fun update() {
//        val batteryStatus: Intent? = IntentFilter(Intent.ACTION_BATTERY_CHANGED).let { ifilter ->
//            this.applicationContext.registerReceiver(null, ifilter)
//        }

    }
}