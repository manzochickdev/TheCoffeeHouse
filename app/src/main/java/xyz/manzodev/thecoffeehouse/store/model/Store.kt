package xyz.manzodev.thecoffeehouse.store.model

import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.model.OpeningHours
import java.io.Serializable

class Store(val name:String,val address:String,
            var location:LatLng?,
            var img : String?,
            var openingHours: String,
            var phone : String) : Serializable {
}