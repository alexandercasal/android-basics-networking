package com.example.android.quakereport.service

import com.example.android.quakereport.model.Earthquake

/**
 * Placeholder data
 *
 * @author Alexander Casal
 */
object DataService {

    fun getPlaceholderEarthquakes(): ArrayList<Earthquake> {
        val earthquakes = arrayListOf<Earthquake>(
                Earthquake(7.2f, "San Francisco", "Feb 2, 2016"),
                Earthquake(6.1f, "London", "July 20, 2015"),
                Earthquake(3.9f, "Tokyo", "Nov 10, 2014"),
                Earthquake(5.4f, "Mexico City", "May 3, 2014"),
                Earthquake(2.8f, "Moscow", "Jan 31, 2013"),
                Earthquake(4.9f, "Rio de Janeiro", "Aug 19, 2012"),
                Earthquake(1.6f, "Paris", "Oct 30, 2011")
        )

        return earthquakes
    }
}