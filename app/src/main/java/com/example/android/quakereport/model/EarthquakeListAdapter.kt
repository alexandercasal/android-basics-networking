package com.example.android.quakereport.model

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.android.quakereport.R
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter

/**
 * Adapter to display a list of earthquakes
 *
 * @author Alexander Casal
 */
class EarthquakeListAdapter(val context: Context, val earthquakes: ArrayList<Earthquake>)
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
        val quakeLocation: TextView = view.findViewById(R.id.text_quake_location)
        val quakeDate: TextView = view.findViewById(R.id.text_quake_date)
        val quakeTime: TextView = view.findViewById(R.id.text_quake_time)

        fun bindEarthquake(earthquake: Earthquake) {
            val dateFormatter = SimpleDateFormat("LLL dd, yyyy")
            val timeFormatter = SimpleDateFormat("h:mm a")
            quakeMagnitude.text = earthquake.magnitude.toString()
            quakeLocation.text = earthquake.location
            quakeDate.text = dateFormatter.format(earthquake.time)
            quakeTime.text = timeFormatter.format(earthquake.time)
        }
    }
}