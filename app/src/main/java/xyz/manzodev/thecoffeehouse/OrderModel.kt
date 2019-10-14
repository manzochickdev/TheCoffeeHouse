package xyz.manzodev.thecoffeehouse

import java.io.Serializable

class OrderModel() : Serializable {
    lateinit var address:String
    lateinit var dateModified:String
    lateinit var detail:String
    lateinit var totalPrice:String

    constructor(address:String , dateModified:String  , totalPrice:String,detail:String) : this(){
        this.address = address
        this.dateModified = dateModified
        this.totalPrice = totalPrice
        this.detail = detail
    }
}