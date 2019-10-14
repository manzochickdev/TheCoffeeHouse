package xyz.manzodev.thecoffeehouse.account.orderhistory

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import xyz.manzodev.thecoffeehouse.OrderModel
import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.ActivityOrderHistoryBinding

class OrderHistoryActivity : AppCompatActivity() {

    lateinit var activityOrderHistoryBinding: ActivityOrderHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityOrderHistoryBinding = DataBindingUtil.setContentView(this,R.layout.activity_order_history)
        configData()
    }

    private fun configData(){
        OrderFactory.getOrderHistory {orderData->
            if (orderData!=null){
                if (orderData.size>0){
                    activityOrderHistoryBinding.hasOrdered = true
                    var adapter = OrderHistoryAdapter(orderData,this,this::onOrderSelected)
                    activityOrderHistoryBinding.rvOrderHistory.adapter = adapter
                }
                else activityOrderHistoryBinding.hasOrdered = false
            }
            else activityOrderHistoryBinding.hasOrdered = false

        }
    }

    private fun onOrderSelected(o:OrderModel){
        var intent = Intent(this,OrderHistoryDetailActivity::class.java)
        intent.putExtra("order",o.detail)
        startActivity(intent)

    }
}
