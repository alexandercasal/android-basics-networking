package com.example.android.quakereport.model

import java.util.*

/**
 * Represents a specific earthquake event
 *
 * @author Alexander Casal
 */
class Earthquake(val magnitude: Float, val location: String, val time: Date) {

    constructor(magnitude: Float, location: String, timeMilliseconds: Long)
        : this(magnitude, location, Date(timeMilliseconds))
}