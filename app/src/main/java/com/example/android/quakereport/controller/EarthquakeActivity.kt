package com.example.android.quakereport.controller

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.android.quakereport.R
import com.example.android.quakereport.model.EarthquakeListAdapter
import com.example.android.quakereport.model.QuakeUtils
import kotlinx.android.synthetic.main.earthquake_activity.*

class EarthquakeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earthquake_activity)

        list_earthquakes.adapter = EarthquakeListAdapter(this, QuakeUtils.extractEarthquakes())
    }
}
