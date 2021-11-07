package com.example.covidapi.Adapters
import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.covidapi.Models.Countries
import com.example.covidapi.R
import java.text.DecimalFormat
import java.util.*

class ListAdapter(private val context: Context, private val arrayList: java.util.ArrayList<Countries>) : BaseAdapter() {
    private lateinit var countryName: TextView
    private lateinit var vaccined: TextView
    private lateinit var percentage: TextView
    private lateinit var populaion: TextView

    override fun getCount(): Int {
        return arrayList.size
    }
    override fun getItem(position: Int): Any {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.adapter_mainlist, parent, false)
        countryName = convertView.findViewById(R.id.countryName)
        percentage = convertView.findViewById(R.id.percentage)
        vaccined = convertView.findViewById(R.id.vaccined)
        populaion = convertView.findViewById(R.id.populaion)

        countryName.text = arrayList.get(position).country
        percentage.text = arrayList.get(position).percentage+"%"
        vaccined.text =arrayList.get(position).vaccined
        populaion.text =arrayList.get(position).population
        return convertView
    }
}