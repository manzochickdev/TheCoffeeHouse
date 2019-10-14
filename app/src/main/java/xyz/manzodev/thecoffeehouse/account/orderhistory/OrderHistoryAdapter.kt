package xyz.manzodev.thecoffeehouse.account.orderhistory

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.manzodev.thecoffeehouse.OrderModel
import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.OrderHistoryCellBinding

class OrderHistoryAdapter(var orderData : ArrayList<OrderModel>,var context:Context,var callback : (o:OrderModel)->Unit) : RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.order_history_cell,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return orderData.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.orderHistoryCellBinding.order = orderData[position]
        holder.orderHistoryCellBinding.orderHistoryContainer.setOnClickListener { callback(orderData[position]) }
        holder.orderHistoryCellBinding.executePendingBindings()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var orderHistoryCellBinding = DataBindingUtil.bind<OrderHistoryCellBinding>(itemView) as OrderHistoryCellBinding
    }
}