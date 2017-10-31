package com.example.android.quakereport.model

import java.util.*

/**
 * Represents a specific earthquake event
 *
 * @author Alexander Casal
 */
class Earthquake(val magnitude: Float, val location: String, val time: Date, val url: String) {

    constructor(magnitude: Float, location: String, timeMilliseconds: Long, url: String)
        : this(magnitude, location, Date(timeMilliseconds), url)
}