package xyz.manzodev.thecoffeehouse.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.ProductCellBinding
import xyz.manzodev.thecoffeehouse.order.model.Product

class ProductAdapter(var productList : ArrayList<Product>, var context:Context) : RecyclerView.Adapter<ProductAdapter.ViewHolder>(){

    interface OnProductClickListener{
        fun onProductClick(p:Product)
    }

    var onProductClickListener : OnProductClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context!!).inflate(R.layout.product_cell,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productCellBinding!!.product = productList[position]
        holder.productCellBinding!!.productHolder.setOnClickListener {
            onProductClickListener?.let { it.onProductClick(productList[position]) }
        }
        holder.productCellBinding!!.executePendingBindings()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val productCellBinding = DataBindingUtil.bind<ProductCellBinding>(itemView)
    }
}