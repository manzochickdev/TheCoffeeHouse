package xyz.manzodev.thecoffeehouse.order.model

import java.io.Serializable

class ProductSelection(var name:String,var price:Long,var isSelected:Boolean = false) :Serializable{


    override fun equals(other: Any?): Boolean {
        var b = other as ProductSelection
        return (this.name.equals(b.name))
    }

    fun clone() : ProductSelection {
        return ProductSelection(name,price,isSelected)
    }
}