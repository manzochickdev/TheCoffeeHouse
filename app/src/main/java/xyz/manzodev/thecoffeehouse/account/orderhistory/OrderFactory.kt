package xyz.manzodev.thecoffeehouse.account.orderhistory

import android.util.Log
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.gson.Gson
import xyz.manzodev.thecoffeehouse.CartInfo
import xyz.manzodev.thecoffeehouse.OrderModel
import xyz.manzodev.thecoffeehouse.user.model.User

object OrderFactory {
    private var orderData:ArrayList<OrderModel>?=null

    fun getOrderHistory(onFinish:(data:ArrayList<OrderModel>?)->Unit){
        if (CartInfo.getInstance().user==null) onFinish(null)
        var user = CartInfo.getInstance().user as User
        FirebaseDatabase.getInstance().getReference("User").child(user.uid).child("Order").addValueEventListener( object:ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {}

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (orderData==null) orderData = ArrayList<OrderModel>()
                for (d:DataSnapshot in dataSnapshot.children){
                    var order = d.getValue(OrderModel::class.java)
                    orderData!!.add(order!!)
                }
                onFinish(orderData)
            }

        })
    }


    fun decodeOrder(data:String) : CartInfo{
        var gson = Gson()
        var order = gson.fromJson(data,CartInfo::class.java)
        return order
    }
}