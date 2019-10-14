package xyz.manzodev.thecoffeehouse.order.model

import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.Expose
import xyz.manzodev.thecoffeehouse.BR
import java.io.Serializable

class Product(@Expose var name:String,
              var desc:String,
              var imgUrl:String,
              var size : ArrayList<ProductSelection>,
              var topping : ArrayList<ProductSelection>?,
              var mainCategory:String,
              var subCategory:String) : Serializable{

    init {
        size[0].isSelected = true
    }

    @Expose
    var quantity : Int=1


    var dispPrice = size[0].price

    @Expose
    var orderDesc:String?=null

    @Expose
    var price : Long=0


    override fun equals(other: Any?): Boolean {
        var b = other as Product
        var check = this.name == b.name && this.orderDesc==b.orderDesc
        return(this.name == b.name && this.orderDesc==b.orderDesc)
    }




}