package org.mhacks.app.pref

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.mhacks.app.pref.ui.PrefFragment

class PrefActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pref)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings_container, PrefFragment())
                .commit()
    }
}