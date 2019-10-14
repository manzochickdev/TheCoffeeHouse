package xyz.manzodev.thecoffeehouse

import com.google.android.gms.maps.model.LatLng
import com.google.gson.annotations.Expose
import xyz.manzodev.thecoffeehouse.addresspicker.Address
import xyz.manzodev.thecoffeehouse.order.model.Product
import xyz.manzodev.thecoffeehouse.user.model.User
import java.io.Serializable

class CartInfo() {
    companion object{
        private var cartInstance : CartInfo? = null

        fun getInstance() : CartInfo{
            if (cartInstance==null) cartInstance = CartInfo()
            return cartInstance!!
        }

        fun reset(){
            var user = cartInstance!!.user
            var address = cartInstance!!.address
            cartInstance = CartInfo()
            cartInstance!!.user = user
            cartInstance!!.address = address
        }
    }

    enum class Mode : Serializable {ADD,EDIT}


    @Expose
    var order : ArrayList<Product>?=null
    @Expose
    var user:User? = null
    @Expose
    var address : Address? = null

    @Expose
    var quantity : Int = 0
    @Expose
    var totalPrice : Long = 0
    @Expose
    var deliveryTime : String = "As soon as possible (30 minutes)"
    @Expose
    var noteDelivery : String?=null
    @Expose
    var noteOrder : String? = null

    @Expose
    lateinit var dateModified:String

    @Expose
    lateinit var name:String



    fun updateProduct(product: Product, callback:(()->Unit)?, mode: Mode){
        if (mode== Mode.ADD){
            if (order ==null) order = ArrayList<Product>()
            order!!.find { it==product }?.let {
                it.quantity+=product.quantity

            } ?: order!!.add(product)
        }
        updateQuantity()
        updateTotalPrice()
        callback?.let { it() }
    }

    fun removeProduct(product: Product, callback:(()->Unit)?){
        order!!.remove(product)
        updateQuantity()
        updateTotalPrice()
        callback?.let { it() }

    }
    private fun updateQuantity(){
        order?.let { quantity = it.map { product -> product.quantity }.sum() }
    }

    private fun updateTotalPrice(){
        order?.let {
            totalPrice = it.map { product -> product.price}.sum()
        }
    }

}