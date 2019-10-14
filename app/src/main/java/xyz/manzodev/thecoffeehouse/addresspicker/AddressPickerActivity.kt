package xyz.manzodev.thecoffeehouse.addresspicker

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.DataBindingUtil
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.AutocompleteSessionToken
import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.ActivityAddressPickerBinding
import java.util.*
import android.util.Log
import android.view.View
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.*
import xyz.manzodev.thecoffeehouse.CartInfo
import kotlin.collections.ArrayList


class AddressPickerActivity : AppCompatActivity() {

    lateinit var activityAddressPickerActivity: ActivityAddressPickerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityAddressPickerActivity =
            DataBindingUtil.setContentView<ActivityAddressPickerBinding>(
                this,
                R.layout.activity_address_picker
            )

        init()
        configPlaceAutocomplete()
        configAct()

    }

    lateinit var addressResults: ArrayList<Pair<String, String>>
    lateinit var addressAdapter: AddressAdapter
    lateinit var placesClient : PlacesClient

    private fun init(){
    }

    private fun configAct(){
        activityAddressPickerActivity.btnMapPicker.setOnClickListener {
            var mapPickerFragment = MapPickerFragment()
            supportFragmentManager.beginTransaction().add(
                R.id.address_picker_container,
                mapPickerFragment,
                MapPickerFragment::class.java.simpleName
            )
                .addToBackStack(MapPickerFragment::class.java.simpleName).commit()
        }

        activityAddressPickerActivity.layoutPickTime.setOnClickListener {
            var deliveryTimePickerFragment  =
                DeliveryTimePickerFragment()
            deliveryTimePickerFragment.show(supportFragmentManager,
                DeliveryTimePickerFragment::class.java.simpleName)
        }

        activityAddressPickerActivity.layoutDefaultTime.setOnClickListener {
            notifyDeliveryTimeChange(resources.getString(R.string.defaultDeliveryTime))
        }
    }

    private fun configPlaceAutocomplete() {
        Places.initialize(this, getString(R.string.google_api_key))
        placesClient = Places.createClient(this)

        addressResults = ArrayList<Pair<String, String>>()
        addressAdapter = AddressAdapter(addressResults, this, this::onAddressSelected)
        activityAddressPickerActivity.rvAddressResult.adapter = addressAdapter

        var timer: Timer? = null
        var q = ""
        var token: AutocompleteSessionToken? = null

        activityAddressPickerActivity.etPlaceAutocomplete.addTextChangedListener(object :
            TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                timer = Timer()
                token = AutocompleteSessionToken.newInstance()
                timer!!.schedule(object : TimerTask() {
                    override fun run() {
                        getAddressResultInfo(token!!, placesClient, q)
                    }
                }, 2000)
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(s: CharSequence?, p1: Int, p2: Int, p3: Int) {
                timer?.let { it.cancel() }
                q = s.toString()
            }

        })
    }

    private fun getAddressResultInfo(
        token: AutocompleteSessionToken,
        placesClient: PlacesClient,
        q: String
    ) {
        val request = FindAutocompletePredictionsRequest.builder()
            // Call either setLocationBias() OR setLocationRestriction().
            .setCountry("VN")
            .setSessionToken(token)
            .setQuery(q)
            .build()

        placesClient.findAutocompletePredictions(request).addOnSuccessListener { response ->
            addressResults.clear()
            for (prediction in response.autocompletePredictions) {
                addressResults.add(
                    Pair(
                        prediction.getFullText(null).toString(),
                        prediction.placeId
                    )
                )
            }
            addressAdapter.notifyDataSetChanged()

        }.addOnFailureListener {
            Log.d("OK", it.message)
        }
    }

    private fun onAddressSelected(data: Pair<String, String>) {
        var placeFields = listOf(Place.Field.LAT_LNG)
        var request = FetchPlaceRequest.newInstance(data.second, placeFields)
        placesClient.fetchPlace(request).addOnSuccessListener{
            var address = Address(data.first,it.place.latLng!!)
            onAddressSelected(address)
        }.addOnFailureListener {
            Log.d("OK",it.message)
        }
    }

    fun onAddressSelected(address: Address) {
        CartInfo.getInstance().address = address

        var intent = Intent()
        intent.putExtra("address", address.string)
        setResult(Activity.RESULT_OK, intent)
        finish()
    }

    fun notifyDeliveryTimeChange(s:String){
        if (s == resources.getString(R.string.defaultDeliveryTime)){
            activityAddressPickerActivity.ivHourCheck.visibility = View.VISIBLE
            activityAddressPickerActivity.ivTimerCheck.visibility = View.GONE
        }
        else{
            activityAddressPickerActivity.tvTimer.text = s
            activityAddressPickerActivity.ivHourCheck.visibility = View.GONE
            activityAddressPickerActivity.ivTimerCheck.visibility = View.VISIBLE
        }
        CartInfo.getInstance().deliveryTime = s
        if (intent.getBooleanExtra("fromCart",false)){
            var intent = Intent()
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }
}
