package com.example.android.quakereport.model

import android.content.AsyncTaskLoader
import android.content.Context
import android.util.Log

/**
 * Loader to handle requesting earthquake data from the USGS Earthquake Catalog
 */
class EarthquakeLoader(context: Context, private val url: String)
    : AsyncTaskLoader<MutableList<Earthquake>>(context) {

    private val TAG = EarthquakeLoader::class.java.simpleName

    companion object {
        val PARAM_URL = "EndpointURL"
    }

    override fun onStartLoading() {
        Log.i(TAG, "onStartLoading called")
        forceLoad()
        takeContentChanged()
    }

    override fun loadInBackground(): MutableList<Earthquake> {
        Log.i(TAG, "loadInBackground called")
        if (url.isNotEmpty()) {
            val quakeUtils = QuakeUtils
            return quakeUtils.loadEarthquakes(url)
        }

        return ArrayList<Earthquake>()
    }

    override fun onStopLoading() {
        Log.i(TAG, "onStopLoading called")
        cancelLoad()
    }
}