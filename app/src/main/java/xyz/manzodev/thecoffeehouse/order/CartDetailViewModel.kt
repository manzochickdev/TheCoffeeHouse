package xyz.manzodev.thecoffeehouse.order

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.database.FirebaseDatabase
import com.google.gson.GsonBuilder
import xyz.manzodev.thecoffeehouse.CartInfo
import xyz.manzodev.thecoffeehouse.OrderModel
import xyz.manzodev.thecoffeehouse.account.orderhistory.OrderFactory
import xyz.manzodev.thecoffeehouse.order.model.CartDelegate
import xyz.manzodev.thecoffeehouse.order.model.Product
import xyz.manzodev.thecoffeehouse.utils.StringUtils
import xyz.manzodev.thecoffeehouse.utils.Utils

class CartDetailViewModel(private var cartInfo: CartInfo, var delegate: CartDelegate? = null) :
    BaseObservable() {

    @get:Bindable
    var customerName: String = if (cartInfo.user != null) cartInfo.user!!.name else ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.customerName)
        }

    @get:Bindable
    var customerPhone: String =
        if (cartInfo.user != null && cartInfo.user!!.phone != null) cartInfo.user!!.phone!! else ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.customerPhone)
        }

    @get:Bindable
    var noteDelivery : String = if(cartInfo.noteDelivery!=null) cartInfo.noteDelivery!! else ""
    set(value) {
        field = value
        notifyPropertyChanged(BR.noteDelivery)
    }

    @get:Bindable
    var noteOrder : String = if(cartInfo.noteOrder!=null) cartInfo.noteDelivery!! else ""
        set(value) {
            field = value
            notifyPropertyChanged(BR.noteOrder)
        }



    @Bindable
    fun getAddressString(): String {
        cartInfo.address?.let {
            return it.string
        }
        return ""
    }

    fun getAddressLatlng(): LatLng? {
        cartInfo.address?.let {
            return it.latLng
        }

        return null
    }


    @Bindable
    fun getDeliveryTime(): String {
        return cartInfo.deliveryTime
    }

    fun updateAddress(){notifyPropertyChanged(BR.addressString);notifyPropertyChanged(BR.deliveryTime)}

    fun getOrderedProduct(): ArrayList<Product>? {
        return cartInfo.order
    }

    @Bindable
    fun getTotalPrice(): String {
        return StringUtils.formatPrice(cartInfo.totalPrice)
    }

    fun updatePrice(){
        notifyPropertyChanged(BR.totalPrice)
    }

    fun pickAddress() {
        delegate!!.pickAddress()
    }

    fun placeOrder(onSuccess:()->Unit,onFailure : (s:String)->Unit) {
        if (cartInfo.user==null) {onFailure ("You must be login to place order");return}
        if  (customerName.isBlank()) {onFailure ("Name must not be blank");return}
        if  (customerPhone.isBlank()) {onFailure ("Phone must not be blank");return}
        if (cartInfo.address == null) {onFailure("Address must not be blank");return}

        cartInfo.user!!.name = customerName
        cartInfo.user!!.phone = customerPhone
        cartInfo.noteDelivery = noteDelivery
        cartInfo.noteOrder = noteOrder

        cartInfo.name = "TCH1122"
        cartInfo.dateModified = Utils.getDate()
        var gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        var json = gson.toJson(cartInfo)
        var orderModel = OrderModel(cartInfo.address!!.string,cartInfo.dateModified,getTotalPrice(),json)
        FirebaseDatabase.getInstance().getReference("User").child(cartInfo.user!!.uid).child("Order").push().setValue(orderModel)
        onSuccess()

    }

}