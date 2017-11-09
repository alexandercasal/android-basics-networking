package com.example.android.quakereport.controller

import android.app.LoaderManager
import android.content.Context
import android.content.Intent
import android.content.Loader
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.android.quakereport.R
import com.example.android.quakereport.model.*
import kotlinx.android.synthetic.main.earthquake_activity.*

class EarthquakeActivity : AppCompatActivity(), LoaderManager.LoaderCallbacks<MutableList<Earthquake>> {

    private val TAG = EarthquakeActivity::class.java.simpleName
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

        list_earthquakes.emptyView = text_no_earthquakes_found
        mAdapter = EarthquakeListAdapter(this, ArrayList<Earthquake>())
        list_earthquakes.adapter = mAdapter

        val cm = this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
        if (activeNetwork != null && activeNetwork.isConnectedOrConnecting) {
            Log.i(TAG, "initLoader called")
            loaderManager.initLoader(LOADER_EARTHQUAKE_ID, null, this)
        } else {
            text_no_earthquakes_found.text = getString(R.string.no_internet_connection)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.action_settings) {
            val settingsIntent = Intent(this, SettingsActivity::class.java)
            startActivity(settingsIntent)
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onCreateLoader(id: Int, args: Bundle?): Loader<MutableList<Earthquake>>? {
        if (id == LOADER_EARTHQUAKE_ID) {
            Log.i(TAG, "onCreateLoader for loader $LOADER_EARTHQUAKE_ID")
            progress_load_earthquakes.visibility = View.VISIBLE

            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            val magnitude = sharedPreferences.getString(
                    getString(R.string.settings_min_magnitude_key),
                    getString(R.string.settings_min_magnitude_default))

            val baseUri = Uri.parse(URL_USGS_EARTHQUAKE_API)
            val uriBuilder = baseUri.buildUpon()

            uriBuilder.appendQueryParameter("format", "geojson")
            uriBuilder.appendQueryParameter("limit", "10")
            uriBuilder.appendQueryParameter("minmag", magnitude)
            uriBuilder.appendQueryParameter("orderby", "time")

            return EarthquakeLoader(
                    this,
                    uriBuilder.toString()
            )
        }

        return null
    }

    override fun onLoadFinished(loader: Loader<MutableList<Earthquake>>, data: MutableList<Earthquake>?) {
        if (loader.id == LOADER_EARTHQUAKE_ID) {
            text_no_earthquakes_found.text = getString(R.string.no_earthquakes_found)
            Log.i(TAG, "Loader $LOADER_EARTHQUAKE_ID finished loading")
            progress_load_earthquakes.visibility = View.GONE
            mAdapter.clear()

            if (data != null && data.isNotEmpty()) {
                mAdapter.addAll(data)
            }
        }
    }

    override fun onLoaderReset(loader: Loader<MutableList<Earthquake>>) {
        if (loader.id == LOADER_EARTHQUAKE_ID) {
            Log.e(TAG, "Resetting loader $LOADER_EARTHQUAKE_ID")
            mAdapter.clear()
        }
    }
}
