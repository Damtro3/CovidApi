package com.example.covidapi

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.covidapi.API.RetrofitClient
import com.example.covidapi.Adapters.ListAdapter
import com.example.covidapi.Interfaces.DaggerIRetrofitClient
import com.example.covidapi.Interfaces.DaggerIStorageService
import com.example.covidapi.Interfaces.IGets
import com.example.covidapi.Models.Countries
import com.example.covidapi.Services.StorageService
import com.google.gson.internal.LinkedTreeMap
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import java.lang.Exception
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject


class MainActivity : AppCompatActivity() {
    @Inject
    lateinit var storage: StorageService
    lateinit var model: IGets
    @Inject
    lateinit var retrofitClient : RetrofitClient
    lateinit var retrofit: Retrofit
    private  var country :LinkedTreeMap<String, LinkedTreeMap<String, LinkedTreeMap<String,  Double>>> ?= null
    private  var population: Double = 0.0
    private  var vaccinePeople : Double =0.0
    private  var percentage : Double =0.0
    private var countriesList : ArrayList<Countries> =  ArrayList<Countries>()
    lateinit var listView : ListView
    private var  adapter : ListAdapter?= null
    private lateinit var  vaccinedTextView : TextView
    private lateinit var  percentageTextView : TextView
    private lateinit var findCountry : EditText
    private lateinit var save : Button
    private lateinit var load : Button
    private lateinit var refresh : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val retrofitComponent = DaggerIRetrofitClient.create()
        retrofitComponent.injector(this)
        val storageComponent = DaggerIStorageService.create()
        storageComponent.injector(this)
        init()
    }
    fun init()
    {
        listView = findViewById<ListView>(R.id.listView)
        vaccinedTextView = findViewById(R.id.vacinnedMain)
        percentageTextView = findViewById(R.id.percentageMain)
        findCountry = findViewById(R.id.findCountry)
        save = findViewById(R.id.save)
        load = findViewById(R.id.load)
        refresh = findViewById(R.id.refresh)
        storage.setPropertyString(this, "apiAddress", "https://covid-api.mmediagroup.fr/")
        retrofit= retrofitClient.GenerateClient(storage.getPropertyString(this, "apiAddress", "").toString(), this)!!;
        model = retrofit.create(IGets::class.java)
        executeConnection(model.GET_Countries())

        save.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                if (v != null) {
                    var now: Date = Date()
                    storage.setPropertyDate(v.context,"saveDate",now.time)
                    storage.setPropertyLong(v.context,"vaccinePeopleEurope", vaccinePeople.toLong())
                }
            }
        })
        load.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                var from: Date = Date( storage.getPropertyDate(v!!.context,"saveDate",Date().time))
                var format: SimpleDateFormat = SimpleDateFormat("dd-MM-YYYY")
                var lastSaveValue = storage.getPropertyLong(v.context,"vaccinePeopleEurope", vaccinePeople.toLong())
                Toast.makeText(v.context," od "+format.format(from)+" zaszczepiono w europie "+ (vaccinePeople - lastSaveValue).toInt() +" osób",Toast.LENGTH_LONG).show()
            }
        })
        refresh.setOnClickListener(object: View.OnClickListener{
            override fun onClick(v: View?) {
                executeConnection(model.GET_Countries())
            }
        })
    }

    fun executeConnection( callModel: Single<Object>)
    {
        try {
            callModel?.subscribeOn(Schedulers.io())?.retry(5)
                ?.observeOn(AndroidSchedulers.mainThread())
                ?.subscribe({ value->handleResponse(value) },
                    { error ->  Toast.makeText(this,"Błąd połaczenia",Toast.LENGTH_LONG).show() })
        }
        catch (exception: Exception)
        {
            Toast.makeText(this,"Błąd połaczenia",Toast.LENGTH_LONG).show()
        }

    }
    private fun  handleResponse(countries: Object)
    {
        population =0.0
        vaccinePeople= 0.0
        percentage = 0.0
        countriesList.clear()
        country = countries as LinkedTreeMap<String, LinkedTreeMap<String, LinkedTreeMap<String,  Double>>>
        var i =country?.get("Poland")?.get("All")?.get("people_vaccinated")
        country?.forEach {
            population += it?.value?.get("All")?.get("population")!!
            vaccinePeople += it?.value?.get("All")?.get("people_vaccinated")!!
            percentage = (it?.value?.get("All")?.get("people_vaccinated")!!*100)/it?.value?.get("All")?.get("population")!!
            countriesList.add(Countries(it?.key,percentage,it?.value?.get("All")?.get("people_vaccinated")!!,it?.value?.get("All")?.get("population")!!))
        }

        setAdapter()
        setMainTextViewValues()
        findByName(this)
    }
    private fun setAdapter()
    {
        adapter = ListAdapter(this, countriesList)
        listView.adapter = adapter
        listView.invalidate()
    }
    private fun setMainTextViewValues()
    {
        vaccinedTextView.text = DecimalFormat("###,###,#00").format(vaccinePeople).toString()
        percentageTextView.text =  DecimalFormat("###.00").format(((vaccinePeople*100)/population)).toString()+"%"
    }
    private fun findByName(context: Context)
    {
        findCountry.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                adapter = ListAdapter(context, search(countriesList) as ArrayList<Countries>)
                listView.adapter = adapter
                listView.invalidate()
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
    }

    private fun search(records: List<Countries>): List<Countries>?
    {
        val searchPositionList: MutableList<Countries> =
            ArrayList()
        for (i in records.indices) {
            if (records[i].country
                    .contains(findCountry.getText().toString())
            ) {
                searchPositionList.add(records[i])
            }
        }
        return searchPositionList
    }


    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        state.putSerializable("countriesList", countriesList)
        countriesList.clear()
    }

    override fun onRestoreInstanceState(state: Bundle) {
        super.onRestoreInstanceState(state)
        countriesList.clear()
        adapter = ListAdapter(this, search(countriesList) as ArrayList<Countries>)
        listView.adapter = adapter
        adapter?.notifyDataSetChanged()

        countriesList= state.getSerializable("countriesList") as ArrayList<Countries>

        adapter = ListAdapter(this, search(countriesList) as ArrayList<Countries>)
        listView.adapter = adapter
        adapter?.notifyDataSetChanged()
    }
}


