package xyz.manzodev.thecoffeehouse.store


import android.animation.Animator
import android.animation.ValueAnimator
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.databinding.DataBindingUtil
import android.location.Location
import android.os.Bundle
import android.os.PersistableBundle
import androidx.fragment.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.FragmentStoreBinding
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.Task
import xyz.manzodev.thecoffeehouse.MainActivity
import xyz.manzodev.thecoffeehouse.store.model.Store
import xyz.manzodev.thecoffeehouse.store.model.StoreDelegate
import xyz.manzodev.thecoffeehouse.utils.PermissionBuilder
import java.util.jar.Manifest


open class StoreFragment : Fragment(), OnMapReadyCallback,
    StoreDelegateAdapter.OnStoreDelegateClickListener, GoogleMap.OnInfoWindowClickListener {
    val TAG: String = "StoreFragment"

    private lateinit var fragmentStoreBinding: FragmentStoreBinding
    var map: GoogleMap? = null
    var mFusedLocationProviderClient: FusedLocationProviderClient? = null
    lateinit var permissionBuilder: PermissionBuilder
    var lastLocation: Location? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentStoreBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_store, container, false
        )

        init()


        var mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)


        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context!!)


        fragmentStoreBinding.btnLocation.setOnClickListener { map?.also { getDeviceLocation() } }
        fragmentStoreBinding.tvStoreSelect.setOnClickListener { showStore() }

        return fragmentStoreBinding.root
    }

    var defaultLocation = LatLng(11.9692968, 108.4405309)

    //func
    var storeDelegateAdapter: StoreDelegateAdapter? = null
    lateinit var storeDelegates: ArrayList<StoreDelegate>
    private fun init() {
        var stores = ArrayList<Store>()
        stores.add(Store("Nguyen Thi", "16 Nguyen Thi", LatLng(20.440383, 106.1785954),
            "https://scontent.fhph1-1.fna.fbcdn.net/v/t1.0-9/33191533_2088472084760140_2292922514931712000_n.jpg?_nc_cat=109&_nc_oc=AQl-To1d987thB5E5GH7KN0Cz7zJmKaw-c8xtdeeKnYafAI1__TtGhz9k7yc-SP4tfghz2iePPC0MriEBWZ336B5&_nc_ht=scontent.fhph1-1.fna&oh=a93cc1fe7a1c4f0235333454e961dbb4&oe=5DF860FD",
            "8:00-10:00","0919xxxxx"))
        storeDelegates = ArrayList<StoreDelegate>()
        storeDelegates.add(StoreDelegate("Nam Dinh", LatLng(20.417834, 106.1318774), stores))

        storeDelegateAdapter = StoreDelegateAdapter(storeDelegates, context!!)
        storeDelegateAdapter?.onStoreDelegateClickListener = this
        fragmentStoreBinding.rvStoreDelegate.adapter = storeDelegateAdapter
    }

    //store delegate list animation
    private fun showStore() {
        if (fragmentStoreBinding.rvStoreDelegate.visibility == View.VISIBLE){
            var finalHeight = fragmentStoreBinding.rvStoreDelegate.height
            var mAnimator = slideAnimator(finalHeight, 0)

            var animatorListener = object : Animator.AnimatorListener{
                override fun onAnimationRepeat(p0: Animator?) {}

                override fun onAnimationEnd(p0: Animator?) {
                    fragmentStoreBinding.rvStoreDelegate.visibility = View.GONE
                }

                override fun onAnimationCancel(p0: Animator?) {}
                override fun onAnimationStart(p0: Animator?) {}

            }

            mAnimator.addListener(animatorListener)
            mAnimator.start()
        }
        else{
            fragmentStoreBinding.rvStoreDelegate.visibility = View.VISIBLE
            var width = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED)
            var height = View.MeasureSpec.makeMeasureSpec(0,View.MeasureSpec.UNSPECIFIED)
            fragmentStoreBinding.rvStoreDelegate.measure(width,height)
            var mAnimation = slideAnimator(0,fragmentStoreBinding.rvStoreDelegate.measuredHeight)
            mAnimation.start()

        }
    }
    private fun slideAnimator(start:Int,end:Int) : ValueAnimator {
        var animator = ValueAnimator.ofInt(start,end)
        animator.addUpdateListener {
            var value = it.animatedValue as Int
            var layoutParams = fragmentStoreBinding.rvStoreDelegate.layoutParams
            layoutParams.height = value
            fragmentStoreBinding.rvStoreDelegate.layoutParams = layoutParams
        }
        return animator
    }
    //end store delegate list animation

    //find store by delegate
    override fun onStoreDelegateClick(s: StoreDelegate) {
        moveCamera(s.location!!, false)
    }

    //end find store by delegate


    fun getDeviceLocation(callback : Unit?=null) {
        permissionBuilder = PermissionBuilder()
        permissionBuilder.withFragment(this)
            .withPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
            .withRationale("Need Location", "Need Location")
            .withSuccessListener {
                var locationResult: Task<Location> = mFusedLocationProviderClient!!.lastLocation
                locationResult.addOnCompleteListener {
                    if (it.isSuccessful) {
                        it.result?.let {
                            lastLocation = it
                            moveCamera(
                                LatLng(lastLocation!!.latitude, lastLocation!!.longitude),
                                true,R.drawable.ic_pin_location
                            )
                        } ?: moveCamera(defaultLocation, true,R.drawable.ic_pin_location)
                        callback
                    } else {
                        moveCamera(defaultLocation, true,R.drawable.ic_pin_location)
                        callback
                    }
                }

            }.build()
    }

    private fun moveCamera(latLng: LatLng, shouldShowMarker: Boolean,customMarkerId : Int? = null) {
        if (shouldShowMarker) {

            var markerOptions = MarkerOptions()
            markerOptions.position(latLng)
            customMarkerId?.let {
                var bitmap = BitmapFactory.decodeResource(resources, it)
                var smallMarker = Bitmap.createScaledBitmap(bitmap, (bitmap.width * 0.8).toFloat().toInt(), (bitmap.height * 0.8).toFloat().toInt(), false)
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(smallMarker)) }
            map?.addMarker(markerOptions)
        }
        map?.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
    }

    //act
    override fun onMapReady(map: GoogleMap?) {
        this.map = map

        var bitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_map_maker)
        var smallMarker = Bitmap.createScaledBitmap(bitmap, resources.getDimensionPixelSize(R.dimen.marker), resources.getDimensionPixelSize(R.dimen.marker), false)

        for (storeDelegate in storeDelegates) {
            for (store in storeDelegate.store) {
                var marker = MarkerOptions()
                marker.position(store.location!!)
                marker.title(store.name)
                marker.snippet(store.address)



                marker.icon(BitmapDescriptorFactory.fromBitmap(smallMarker))

                var mark = this.map!!.addMarker(marker)
                mark.tag = store
            }
        }

        getDeviceLocation()
        this.map!!.setOnInfoWindowClickListener(this)
    }

    override fun onInfoWindowClick(marker: Marker?) {
        var bundle = Bundle()
        bundle.putSerializable("store",marker!!.tag as Store)

        (activity!! as MainActivity).inflateFragment(StoreDetailFragment(),MainActivity.FragmentMode.REPLACE,bundle)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        permissionBuilder.onRequestResult(requestCode, grantResults)
    }



}
