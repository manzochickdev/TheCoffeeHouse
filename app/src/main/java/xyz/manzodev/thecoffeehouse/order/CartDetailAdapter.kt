package xyz.manzodev.thecoffeehouse.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.CartDetailCellBinding
import xyz.manzodev.thecoffeehouse.order.model.Product

class CartDetailAdapter(var products : ArrayList<Product>,var context : Context,var onItemClickListener : ((p:Product)->Unit)?=null) : RecyclerView.Adapter<CartDetailAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.cart_detail_cell,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.cartDataCellBinding!!.product = products[position]
        holder.cartDataCellBinding!!.cartItemContainer.setOnClickListener {
            onItemClickListener?.let{
                it(products[position])
            }
        }
        holder.cartDataCellBinding!!.executePendingBindings()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var cartDataCellBinding = DataBindingUtil.bind<CartDetailCellBinding>(itemView)
    }
}