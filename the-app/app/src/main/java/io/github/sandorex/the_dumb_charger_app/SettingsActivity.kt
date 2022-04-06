package io.github.sandorex.the_dumb_charger_app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat


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

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // just in case
        if (!TorchManager.hasCamera())
            TorchManager.findCamera(applicationContext)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)

            findPreference<Preference>("torch_test")?.setOnPreferenceClickListener {
                TorchManager.signal()
                true
            }

            findPreference<Preference>("charger_test")?.setOnPreferenceClickListener {
                Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show()
                true
            }

            findPreference<Preference>("support_me")?.setOnPreferenceClickListener {
                Toast.makeText(context, "Not yet implemented", Toast.LENGTH_SHORT).show()
                true
            }

            findPreference<Preference>("github")?.setOnPreferenceClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/sandorex/the-dumb-charger")))
                true
            }

            findPreference<Preference>("license")?.setOnPreferenceClickListener {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("https://www.gnu.org/licenses/gpl-3.0.en.html")))
                true
            }


        }
    }
}