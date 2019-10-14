package xyz.manzodev.thecoffeehouse.addresspicker


import android.graphics.Point
import android.location.Geocoder
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng

import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.FragmentMapPickerBinding
import xyz.manzodev.thecoffeehouse.store.StoreFragment
import java.io.IOException
import java.util.*


class MapPickerFragment : StoreFragment() {

    private lateinit var fragmentMapPickerBinding: FragmentMapPickerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentMapPickerBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_map_picker, container, false)

        var mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)

        configAct()

        return fragmentMapPickerBinding.root
    }

    override fun onMapReady(map: GoogleMap?) {
        this.map = map
        getDeviceLocation(getLocationOnMarkerDrag())
    }

    var address : Address? = null
    private fun getLocationOnMarkerDrag() {
        var geocoder = Geocoder(context!!, Locale.getDefault())
        map!!.setOnCameraIdleListener {
            var x = fragmentMapPickerBinding.root.width / 2
            var y = fragmentMapPickerBinding.root.height / 2

            var position = map!!.projection.fromScreenLocation(Point(x,y))
            try{
                var addressed = geocoder.getFromLocation(position.latitude,position.longitude,1)
                if (addressed.size!=0){
                    address = Address(addressed[0].getAddressLine(0), LatLng(addressed[0].latitude,addressed[0].longitude))
                    fragmentMapPickerBinding.tvAddress.text = addressed[0].getAddressLine(0)
                }
            }catch (e:IOException){

            }
        }
    }

    private fun configAct(){
        fragmentMapPickerBinding.btnLocation.setOnClickListener { getDeviceLocation() }
        fragmentMapPickerBinding.btnOk.setOnClickListener {
            address?.let { (activity as AddressPickerActivity).onAddressSelected(it) }

        }
    }


}
