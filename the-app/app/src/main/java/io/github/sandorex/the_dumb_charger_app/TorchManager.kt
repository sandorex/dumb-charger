package io.github.sandorex.the_dumb_charger_app

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.widget.Toast

class TorchManager {
    companion object {
        private lateinit var cameraManager: CameraManager
        private lateinit var cameraId: String
        private var torchState: Boolean = false

//        fun findCamera(context: Context, applicationContext: Context) {
        fun findCamera(context: Context) {
            val isFlashAvailable = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)

            if (!isFlashAvailable) {
                Toast.makeText(context, "Could not camera on this device", Toast.LENGTH_SHORT).show()
//                val alert = AlertDialog.Builder(context).create()
//                alert.setTitle("Oops!")
//                alert.setMessage("Flash not available in this device...")
//                alert.setButton(DialogInterface.BUTTON_POSITIVE, "OK") { _, _ -> finish() }
//                alert.show()
            }

            cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                cameraId = cameraManager.cameraIdList[0]
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

        fun hasCamera(): Boolean {
            return cameraId.isEmpty()
        }

        fun setTorch(state: Boolean) {
            try {
                torchState = state
                cameraManager.setTorchMode(cameraId, state)
            } catch (e: CameraAccessException) {
                e.printStackTrace()
            }
        }

        fun toggleTorch() {
            setTorch(!torchState)
        }

        fun signal() {
            Thread.sleep(50)
            setTorch(true)
            Thread.sleep(100)
            setTorch(false)
        }
    }
}