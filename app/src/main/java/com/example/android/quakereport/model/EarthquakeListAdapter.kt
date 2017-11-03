package com.example.android.quakereport.model

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.android.quakereport.R
import java.text.DecimalFormat
import java.text.SimpleDateFormat

/**
 * Adapter to display a list of earthquake information such as magnitude, location, and
 * date of occurrence
 *
 * @author Alexander Casal
 */
class EarthquakeListAdapter(val context: Context, val earthquakes: List<Earthquake>)
    : BaseAdapter() {

    private val mLayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return earthquakes.size
    }

    override fun getItemId(pos: Int): Long {
        return 0
    }

    override fun getItem(pos: Int): Earthquake {
        return earthquakes[pos]
    }

    override fun getView(pos: Int, convertView: View?, parent: ViewGroup?): View? {
        val viewHolder: ViewHolder
        var view = convertView

        if (convertView == null) {
            view = mLayoutInflater.inflate(R.layout.item_earthquake, parent, false)
            viewHolder = ViewHolder(view)
            view.tag = viewHolder
        } else {
            viewHolder = view!!.tag as ViewHolder
        }

        viewHolder.bindEarthquake(getItem(pos))
        return view
    }

    private inner class ViewHolder(view: View) {
        val quakeMagnitude: TextView = view.findViewById(R.id.text_quake_magnitude)
        val quakeLocOffset: TextView = view.findViewById(R.id.text_quake_loc_offset)
        val quakeLocation: TextView = view.findViewById(R.id.text_quake_primary_location)
        val quakeDate: TextView = view.findViewById(R.id.text_quake_date)
        val quakeTime: TextView = view.findViewById(R.id.text_quake_time)

        fun bindEarthquake(earthquake: Earthquake) {
            val dateFormatter = SimpleDateFormat("LLL dd, yyyy")
            val timeFormatter = SimpleDateFormat("h:mm a")
            val locationParts = getLocationParts(earthquake.location)
            val magnitudeCircle = quakeMagnitude.background as GradientDrawable

            magnitudeCircle.setColor(getMagnitudeColor(earthquake.magnitude))
            quakeMagnitude.text = formatMagnitude(earthquake.magnitude)
            quakeLocOffset.text = locationParts[0]
            quakeLocation.text = locationParts[1]
            quakeDate.text = dateFormatter.format(earthquake.time)
            quakeTime.text = timeFormatter.format(earthquake.time)
        }

        private fun getLocationParts(location: String): Array<String> {
            val delimiter = " of "
            val offsetIndex = location.indexOf(delimiter)

            if (location.contains(delimiter)) {
                return arrayOf(
                        location.substring(0, (offsetIndex + delimiter.length)),
                        location.substring((offsetIndex + delimiter.length)..(location.length - 1))
                )
            } else {
                return arrayOf(
                        "Near the",
                        location
                )
            }
        }

        private fun formatMagnitude(mag: Float): String {
            val formatter = DecimalFormat("0.0")
            return formatter.format(mag)
        }

        private fun getMagnitudeColor(mag: Float): Int {
            return when(mag) {
                in 0..2 -> ContextCompat.getColor(context, R.color.magnitude1)
                in 2..3 -> ContextCompat.getColor(context, R.color.magnitude2)
                in 3..4 -> ContextCompat.getColor(context, R.color.magnitude3)
                in 4..5 -> ContextCompat.getColor(context, R.color.magnitude4)
                in 5..6 -> ContextCompat.getColor(context, R.color.magnitude5)
                in 6..7 -> ContextCompat.getColor(context, R.color.magnitude6)
                in 7..8 -> ContextCompat.getColor(context, R.color.magnitude7)
                in 8..9 -> ContextCompat.getColor(context, R.color.magnitude8)
                in 9..10 -> ContextCompat.getColor(context, R.color.magnitude9)
                else -> ContextCompat.getColor(context, R.color.magnitude10plus)
            }
        }
    }
}