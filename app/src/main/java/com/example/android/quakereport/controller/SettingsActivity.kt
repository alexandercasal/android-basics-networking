package com.example.android.quakereport.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceFragment
import com.example.android.quakereport.R

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
    }

    class EarthquakePreferenceFragment : PreferenceFragment() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            addPreferencesFromResource(R.xml.settings_main)
        }
    }
}
