package xyz.manzodev.thecoffeehouse.account.orderhistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import xyz.manzodev.thecoffeehouse.OrderModel
import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.ActivityOrderHistoryDetailBinding
import xyz.manzodev.thecoffeehouse.order.CartDetailAdapter

class OrderHistoryDetailActivity : AppCompatActivity() {

    lateinit var activityOrderHistoryDetailBinding : ActivityOrderHistoryDetailBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityOrderHistoryDetailBinding = DataBindingUtil.setContentView(this,R.layout.activity_order_history_detail)

        bindingData()
    }

    private fun bindingData(){
        var order = intent.getSerializableExtra("order") as String
        var cartInfo = OrderFactory.decodeOrder(order)
        var vm = OrderHistoryDetailViewModel(cartInfo)
        activityOrderHistoryDetailBinding.vm = vm

        var orderedProduct = vm.getOrderedProduct()
        var adapter = CartDetailAdapter(orderedProduct,this)
        activityOrderHistoryDetailBinding.rvOrderedProduct.adapter = adapter

    }

}
