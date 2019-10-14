package xyz.manzodev.thecoffeehouse.account.orderhistory

import xyz.manzodev.thecoffeehouse.CartInfo
import xyz.manzodev.thecoffeehouse.order.model.Product
import xyz.manzodev.thecoffeehouse.utils.StringUtils

class OrderHistoryDetailViewModel(var order:CartInfo) {

    fun getName():String{
        return "Order #${order.name}"
    }

    fun getOrderTime():String{
        return order.dateModified.substring(0,5)
    }

    fun getTotalPrice():String{
        return StringUtils.formatPrice(order.totalPrice)
    }

    fun getOrderedProduct (): ArrayList<Product>{
        return order.order!!
    }
}