package com.example.covidapi.Models

import com.google.gson.annotations.SerializedName
import com.google.gson.internal.LinkedTreeMap
import java.text.DecimalFormat

class Countries {
    var country: String = ""
    var percentage: String = "0.0"
    var vaccined: String = "0.0"
    var population: String = "0.0"

    constructor(country: String,percentage: Double ,vaccined: Double, population: Double) {
        this.country = country
        this.percentage = DecimalFormat("###.00").format(percentage)
        this.vaccined = DecimalFormat("###,###,#00").format(vaccined)
        this.population =  DecimalFormat("###,###,#00").format(population)
    }
}