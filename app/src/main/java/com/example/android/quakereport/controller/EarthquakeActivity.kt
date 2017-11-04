package com.example.android.quakereport.controller

import android.app.LoaderManager
import android.content.Intent
import android.content.Loader
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.android.quakereport.R
import com.example.android.quakereport.model.*
import kotlinx.android.synthetic.main.earthquake_activity.*

class EarthquakeActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<MutableList<Earthquake>> {

    private val LOADER_EARTHQUAKE_ID = 1
    private lateinit var mAdapter: EarthquakeListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earthquake_activity)

        initEarthquakeListView()
    }

    /**
     * Set up the earthquakes list view with a click listener and triggers loading
     * the earthquake data from USGS in the background.
     */
    private fun initEarthquakeListView() {
        list_earthquakes.setOnItemClickListener { adapterView, view, i, l ->
            val quake: Earthquake? = adapterView.getItemAtPosition(i) as? Earthquake
            val urlErrorMsg = "Unable to open details"

            if (quake != null && !quake.url.isEmpty()) {
                var quakeURL = quake.url

                // A URL missing http:// or https:// will cause the intent to fail and possibly crash the app
                if (!quakeURL.startsWith("http://") && !quakeURL.startsWith("https://")) {
                    quakeURL = "http://" + quakeURL
                }

                val quakeInfoIntent = Intent(Intent.ACTION_VIEW, Uri.parse(quakeURL))
                if (quakeInfoIntent.resolveActivity(this.packageManager) != null) {
                    startActivity(quakeInfoIntent)
                } else {
                    Toast.makeText(this, urlErrorMsg, Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, urlErrorMsg, Toast.LENGTH_SHORT).show()
            }
        }

        mAdapter = EarthquakeListAdapter(this, ArrayList<Earthquake>())
        list_earthquakes.adapter = mAdapter
        loaderManager.initLoader(LOADER_EARTHQUAKE_ID, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<MutableList<Earthquake>>? {
        if (id == LOADER_EARTHQUAKE_ID) {
            progress_load_earthquakes.visibility = View.VISIBLE
            return EarthquakeLoader(
                    this,
                    URL_USGS_EARTHQUAKE_API
            )
        }

        return null
    }

    override fun onLoadFinished(loader: Loader<MutableList<Earthquake>>, data: MutableList<Earthquake>?) {
        if (loader.id == LOADER_EARTHQUAKE_ID) {
            progress_load_earthquakes.visibility = View.GONE
            mAdapter.clear()

            if (data != null && data.isNotEmpty()) {
                mAdapter.addAll(data)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<MutableList<Earthquake>>) {
        if (loader.id == LOADER_EARTHQUAKE_ID) {
            mAdapter.clear()
        }
    }
}
