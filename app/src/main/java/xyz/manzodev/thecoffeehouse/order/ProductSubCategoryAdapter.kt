package xyz.manzodev.thecoffeehouse.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.SubCategoryCellBinding
import xyz.manzodev.thecoffeehouse.order.model.Product

class ProductSubCategoryAdapter(var productList : ArrayList<Product>, var context : Context, var listener:ProductAdapter.OnProductClickListener) : RecyclerView.Adapter<ProductSubCategoryAdapter.ViewHolder>(){
    var subCategory = productList.distinctBy { p -> p.subCategory }.map { it.subCategory }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.sub_category_cell,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return subCategory.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.subCategoryBinding!!.tvSubCategory.text = subCategory[position]
        var sortedProduct = ArrayList(productList.filter { p -> p.subCategory == subCategory[position] })
        var adapter = ProductAdapter(sortedProduct,context!!)
        adapter.onProductClickListener = listener
        holder.subCategoryBinding!!.rvProduct.adapter = adapter
        holder.subCategoryBinding!!.rvProduct.layoutManager = GridLayoutManager(context!!,2,GridLayoutManager.VERTICAL,false)



        holder.subCategoryBinding!!.executePendingBindings()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var subCategoryBinding = DataBindingUtil.bind<SubCategoryCellBinding>(itemView)
    }
}