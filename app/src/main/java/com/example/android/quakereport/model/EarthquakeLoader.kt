package com.example.android.quakereport.model

import android.content.AsyncTaskLoader
import android.content.Context

/**
 * Loader to handle requesting earthquake data from the USGS Earthquake Catalog
 */
class EarthquakeLoader(context: Context, private val url: String)
    : AsyncTaskLoader<MutableList<Earthquake>>(context) {

    companion object {
        val PARAM_URL = "EndpointURL"
    }

    override fun onStartLoading() {
        forceLoad()
        takeContentChanged()
    }

    override fun loadInBackground(): MutableList<Earthquake> {
        if (url.isNotEmpty()) {
            val quakeUtils = QuakeUtils
            return quakeUtils.loadEarthquakes(url)
        }

        return ArrayList<Earthquake>()
    }

    override fun onStopLoading() {
        cancelLoad()
    }
}