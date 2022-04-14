package io.github.sandorex.the_dumb_charger_app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceManager
import androidx.preference.SwitchPreferenceCompat

private val TAG = SettingsActivity::class.simpleName

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings_activity)

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }

        if (!TorchManager.hasCamera())
            TorchManager.findCamera(applicationContext)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            if (findPreference<SwitchPreferenceCompat>(getString(R.string.key_dark_mode))?.isChecked == true) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
            }

            findPreference<SwitchPreferenceCompat>(getString(R.string.key_dark_mode))?.setOnPreferenceClickListener {
                Toast.makeText(this.context, "Please restart the application to apply the theme", Toast.LENGTH_SHORT).show()
                true
            }

            val enablePref = findPreference<SwitchPreferenceCompat>(getString(R.string.key_enabled))

            // set to current state of the service
//            enablePref?.callChangeListener(MonitorService.isRunning())
            // TODO: i do not know how to set it at start of settings activity so that it matches
            // current state and you could see if it actually is running, maybe do the ping method?
            enablePref?.setOnPreferenceChangeListener { _, newValue ->
                val service = Intent(this.context, MonitorService::class.java)

                if (newValue as Boolean) {
                    Toast.makeText(this.context, "Starting service", Toast.LENGTH_SHORT).show()
                    this.context?.startService(service)
                } else {
                    Toast.makeText(this.context, "Stopping service", Toast.LENGTH_SHORT).show()
                    this.context?.stopService(service)
                }

                true
            }

            findPreference<Preference>(getString(R.string.key_help))?.setOnPreferenceClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://dontkillmyapp.com/"))) // TODO: replace with wiki on github
                true
            }

            findPreference<Preference>(getString(R.string.key_test_torch))?.setOnPreferenceClickListener {
                TorchManager.signal()
                true
            }

            findPreference<Preference>(getString(R.string.key_test_charger))?.setOnPreferenceClickListener {
                Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show()
                true
            }

            findPreference<Preference>(getString(R.string.key_support_me))?.setOnPreferenceClickListener {
                Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show()
                true
            }

            findPreference<Preference>(getString(R.string.key_github))?.setOnPreferenceClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/sandorex/the-dumb-charger")))
                true
            }

            findPreference<Preference>(getString(R.string.key_license))?.setOnPreferenceClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gnu.org/licenses/gpl-3.0.en.html")))
                true
            }
        }
    }
}