package xyz.manzodev.thecoffeehouse.addresspicker

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.Expose

class Address(@Expose var string:String) {

    @Expose
    var lat:Double?=null
    @Expose
    var long:Double?=null

    var latLng : LatLng?=null
    set(value) {
        value?.let {
            field = it
            lat = it.latitude
            long = it.longitude
        }
    }
    constructor(string:String,latLng : LatLng?) : this(string){
        this.latLng = latLng
    }
}