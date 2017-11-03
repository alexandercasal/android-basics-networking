package com.example.android.quakereport.controller

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ListView
import android.widget.ProgressBar
import android.widget.Toast
import com.example.android.quakereport.R
import com.example.android.quakereport.model.Earthquake
import com.example.android.quakereport.model.EarthquakeListAdapter
import com.example.android.quakereport.model.QuakeUtils
import com.example.android.quakereport.model.URL_USGS_EARTHQUAKE_API
import kotlinx.android.synthetic.main.earthquake_activity.*
import java.lang.ref.WeakReference

class EarthquakeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earthquake_activity)

        initEarthquakeListView()
    }

    /**
     * Set up the earthquakes list view with a click listener and triggers loading
     * the earthquake data from USGS in the background
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
        LoadEarthquakesAsyncTask(WeakReference(this)).execute(URL_USGS_EARTHQUAKE_API)
    }

    private class LoadEarthquakesAsyncTask(val activity: WeakReference<EarthquakeActivity>)
        : AsyncTask<String, Void, List<Earthquake>>() {

        override fun onPreExecute() {
            activity.get()?.findViewById<ProgressBar>(R.id.progress_load_earthquakes)
                    ?.visibility = View.VISIBLE
            activity.get()?.findViewById<ListView>(R.id.list_earthquakes)
                    ?.isEnabled = false
        }

        override fun doInBackground(vararg endpoint: String): List<Earthquake> {
            if (endpoint.isNotEmpty() && endpoint[0].isNotEmpty()) {
                val quakeUtils = QuakeUtils
                return quakeUtils.loadEarthquakes(endpoint[0])
            }

            return ArrayList<Earthquake>()
        }

        override fun onPostExecute(earthquakes: List<Earthquake>) {
            activity.get()?.let {
                val earthquakeListView: ListView = it.findViewById(R.id.list_earthquakes)
                earthquakeListView.adapter = EarthquakeListAdapter(it, earthquakes)
                earthquakeListView.isEnabled = true
                it.findViewById<ProgressBar>(R.id.progress_load_earthquakes).visibility = View.GONE
            }
        }


    }
}
