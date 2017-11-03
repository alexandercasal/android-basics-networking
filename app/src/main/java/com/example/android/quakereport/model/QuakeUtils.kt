package com.example.android.quakereport.model

import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.SocketTimeoutException
import java.net.URL
import java.util.ArrayList

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
object QuakeUtils {

    val TAG = QuakeUtils.javaClass.simpleName

    /**
     * Loads the earthquake data from the USGS
     */
    fun loadEarthquakes(url: String): List<Earthquake> {
        val endpointURL = parseURL(url)
        val response = endpointURL?.let {
            makeHTTPRequest(it)
        }
        val earthquakes: List<Earthquake> = response?.let {
            parseEarthquakesJSON(it)
        } ?: ArrayList<Earthquake>()

        return earthquakes
    }

    /**
     * Parses a url String to a URL object
     */
    private fun parseURL(url: String): URL? {
        try {
            return URL(url)
        } catch (e: MalformedURLException) {
            Log.e(TAG, e.message)
        }

        return null
    }

    /**
     * Makes an HTTP request to the provided URL and obtains the response body
     * @param url HTTP URL
     * @return List of 0 or more Earthquakes
     */
    private fun makeHTTPRequest(url: URL): String {
        val connection = url.openConnection() as? HttpURLConnection
        connection?.connectTimeout = 5000
        connection?.readTimeout = 5000
        connection?.requestMethod = "GET"

        try {
            connection?.connect()
            if (connection?.responseCode == 200) {
                return extractResponse(connection.inputStream)
            } else {
                Log.e(TAG, "Error response code ${connection?.responseCode}")
            }
        } catch (e: IOException) {
            Log.e(TAG, "Problem opening connection", e)
        } catch (e: SocketTimeoutException) {
            Log.e(TAG, "Timed out when requesting earthquake data", e)
        } finally {
            connection?.disconnect()
        }

        return ""
    }

    /**
     * Parses an InputStream and builds a String
     */
    private fun extractResponse(inputStream: InputStream): String {
        val bufferedReader = BufferedReader(InputStreamReader(inputStream))
        val response = StringBuilder()

        try {
            var line = bufferedReader.readLine()
            while (line != null) {
                response.append(line)
                line = bufferedReader.readLine()
            }
        } catch(e: IOException) {
            Log.e(TAG, "Problem extracting response body from HTTP request", e)
            return ""
        } finally {
            bufferedReader.close()
        }

        return response.toString()
    }

    /**
     * Extract earthquake information from the JSON string.
     * @param json geojson containing a root JSON object
     * @return List of 0 or more Earthquakes
     */
    private fun parseEarthquakesJSON(json: String): List<Earthquake> {
        val earthquakes = ArrayList<Earthquake>()

        try {
            val root = JSONObject(json)
            val features = root.getJSONArray("features")

            for (i in 0 until features.length() - 1) {
                val feature = features.getJSONObject(i)
                val properties = feature.getJSONObject("properties")

                val magnitude: Float = properties.getDouble("mag").toFloat()
                val location: String = properties.getString("place")
                val milliseconds: Long = properties.getLong("time")
                val url: String = properties.getString("url")
                earthquakes.add(Earthquake(magnitude, location, milliseconds, url))
            }
        } catch (e: JSONException) {
            Log.e(TAG, "Problem parsing the earthquake JSON results", e)
        }

        return earthquakes
    }
}