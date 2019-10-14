package xyz.manzodev.thecoffeehouse.order

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.databinding.DataBindingUtil
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.ActivityCartDetailBinding
import xyz.manzodev.thecoffeehouse.CartInfo
import xyz.manzodev.thecoffeehouse.addresspicker.AddressPickerActivity
import xyz.manzodev.thecoffeehouse.order.model.CartDelegate
import xyz.manzodev.thecoffeehouse.order.model.Product
import xyz.manzodev.thecoffeehouse.utils.Utils

class CartDetailActivity : AppCompatActivity(),CartDelegate {

    lateinit var activityCartDetailBinding: ActivityCartDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCartDetailBinding = DataBindingUtil.setContentView<ActivityCartDetailBinding>(this,R.layout.activity_cart_detail)
        configData()
        configAct()
    }


    //config
    lateinit var viewmodel  : CartDetailViewModel
    private fun configData(){
        viewmodel = CartDetailViewModel(CartInfo.getInstance(),this)
        activityCartDetailBinding.vm = viewmodel
        configRecyclerView()
        configAddress()
    }


    var map:GoogleMap?=null
    private fun configAddress(){
        if (map!=null) {
           viewmodel.getAddressLatlng()?.let {
                map!!.moveCamera(CameraUpdateFactory.newLatLng(it))
                map!!.addMarker(MarkerOptions().position(it))
            }
        }

        else{
            val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
            mapFragment.getMapAsync{ googleMap ->
                viewmodel.getAddressLatlng()?.let {
                    map = googleMap
                    map!!.moveCamera(CameraUpdateFactory.newLatLng(it))
                    map!!.addMarker(MarkerOptions().position(it))
                }
            }
        }
    }

    private fun configRecyclerView() {
        var products = viewmodel.getOrderedProduct()
        products?.let {
            var cartDetailAdapter = CartDetailAdapter(it,this,this::onProductClick)
            activityCartDetailBinding.rvOrderedProduct.adapter = cartDetailAdapter
        }
    }

    private fun configAct() {
        activityCartDetailBinding.btnOrder.setOnClickListener {
            viewmodel.placeOrder({
                CartInfo.reset()
                finish()
            },{
                Toast.makeText(this,it,LENGTH_SHORT).show()
            })
        }
    }

    //func
    fun updateCart(){
        activityCartDetailBinding.rvOrderedProduct.adapter!!.notifyDataSetChanged()
        viewmodel.updatePrice()
        var a = viewmodel.getOrderedProduct()
        if (viewmodel.getOrderedProduct()!!.size==0) finish()
    }

    var reqCode:Int ?=null
    override fun pickAddress(){
        var intent = Intent(this,AddressPickerActivity::class.java)
        reqCode = Utils.reqCode()
        intent.putExtra("fromCart",true)
        startActivityForResult(intent,reqCode!!)
    }

    //act
    private fun onProductClick(p:Product){
        var bundle = Bundle()
        bundle.putSerializable("product",p)
        bundle.putSerializable("mode", CartInfo.Mode.EDIT)
        var productDetailFragment = ProductDetailFragment()
        productDetailFragment.arguments = bundle

        productDetailFragment.show(supportFragmentManager!!,ProductDetailFragment::class.java.simpleName)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode== Activity.RESULT_OK){
            if (requestCode==reqCode){
                viewmodel.updateAddress()
                configAddress()
            }
        }
    }
}
