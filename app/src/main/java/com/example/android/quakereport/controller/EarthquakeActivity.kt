package com.example.android.quakereport.controller

import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.example.android.quakereport.R
import com.example.android.quakereport.model.Earthquake
import com.example.android.quakereport.model.URL_USGS_EARTHQUAKE_API
import kotlinx.android.synthetic.main.earthquake_activity.*

class EarthquakeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.earthquake_activity)

        initEarthquakeListView()
    }

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

        LoadEarthquakesAsyncTask().execute(URL_USGS_EARTHQUAKE_API)
    }

    private inner class LoadEarthquakesAsyncTask : AsyncTask<String, Void, List<Earthquake>>() {

        override fun onPreExecute() {
            progress_load_earthquakes.visibility = View.VISIBLE
            list_earthquakes.isEnabled = false
        }

        override fun doInBackground(vararg endpoint: String): List<Earthquake> {
            return ArrayList<Earthquake>()
        }

        override fun onPostExecute(earthquakes: List<Earthquake>) {
            progress_load_earthquakes.visibility = View.GONE
            list_earthquakes.isEnabled = true
        }
    }
}
