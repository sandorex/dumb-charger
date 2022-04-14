package io.github.sandorex.the_dumb_charger_app

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.widget.Toast

class TorchManager {
    companion object {
        private var cameraManager: CameraManager? = null
        private var cameraId: String = ""
        private var torchState: Boolean = false

        fun findCamera(context: Context): Boolean {
            val isFlashAvailable = context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT)

            if (!isFlashAvailable) {
                Toast.makeText(context, "Could not camera on this device", Toast.LENGTH_SHORT).show()
                return false
            }

            cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
            try {
                cameraId = cameraManager?.cameraIdList?.get(0) ?: return false
            } catch (e: CameraAccessException) {
                e.printStackTrace()
                return false
            }

            return true
        }

        fun hasCamera(): Boolean {
            return cameraId.isNotEmpty() && cameraManager != null
        }

        fun setTorch(state: Boolean) {
            try {
                torchState = state
                cameraManager?.setTorchMode(cameraId, state)
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