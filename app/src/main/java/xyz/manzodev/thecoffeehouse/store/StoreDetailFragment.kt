package xyz.manzodev.thecoffeehouse.store


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.FragmentStoreDetailBinding
import xyz.manzodev.thecoffeehouse.store.model.Store
import android.content.Intent
import android.net.Uri
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions


class StoreDetailFragment : Fragment(),OnMapReadyCallback {
    private lateinit var fragmentStoreDetailBinding : FragmentStoreDetailBinding

    var store:Store? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentStoreDetailBinding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_store_detail, container, false)

        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        setData()
        setAction()



        return fragmentStoreDetailBinding.root
    }
    //config
    private fun setData() {
        var bundle = this.arguments
        bundle?.let {
            store = bundle.getSerializable("store") as Store
            fragmentStoreDetailBinding.store = store
        }
    }

    //act

    override fun onMapReady(map: GoogleMap?) {
        store?.let {
            map!!.moveCamera(CameraUpdateFactory.newLatLng(it.location))
            map!!.addMarker(MarkerOptions().position(it.location!!))
        }
    }


    private fun setAction() {
        fragmentStoreDetailBinding.btnDirection.setOnClickListener {
            store?.let {
                //                val gmmIntentUri = Uri.parse("geo:${it.location!!.latitude},${it.location!!.longitude})")
//                val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
//                startActivity(mapIntent)

                val intent = Intent(
                    android.content.Intent.ACTION_VIEW,
                    Uri.parse("http://maps.google.com/maps?q=loc:${it.location!!.latitude},${it.location!!.longitude}")
                )
                startActivity(intent)
            }
        }


        fragmentStoreDetailBinding.btnDismiss.setOnClickListener { activity!!.onBackPressed() }
    }

}
