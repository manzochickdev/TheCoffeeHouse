package xyz.manzodev.thecoffeehouse.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.ProductMultiChoiceCellBinding
import xyz.manzodev.thecoffeehouse.databinding.ProductSingleChoiceCellBinding
import xyz.manzodev.thecoffeehouse.order.model.ProductSelection

class SelectionAdapter(var selection: ArrayList<ProductSelection>,var context: Context,var selectMode: SelectMode) : RecyclerView.Adapter<SelectionAdapter.ViewHolder>() {


    interface OnSelectionListener{
        fun onSelection(selection: ProductSelection)
    }

    var onSelectionListener : OnSelectionListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var layoutInflater = LayoutInflater.from(context)
        if(viewType==0) {
            var view = layoutInflater.inflate(R.layout.product_single_choice_cell,parent,false)
            return ViewHolder(view,SelectMode.SINGLE)
        }
        else{
            var view = layoutInflater.inflate(R.layout.product_multi_choice_cell,parent,false)
            return ViewHolder(view,SelectMode.MULTI)
        }
    }

    override fun getItemCount(): Int {
        return selection.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        when(holder.itemViewType){
            0 ->{
                holder.productSingleChoiceCellBinding!!.selection = selection[position]
                holder.productSingleChoiceCellBinding!!.choiceContainer.setOnClickListener {
                    onSelectionListener?.let { it.onSelection(selection[position]) }
                }
                holder.productSingleChoiceCellBinding!!.executePendingBindings()
            }

            1->{
                holder.productMultiChoiceCellBinding!!.selection = selection[position]
                holder.productMultiChoiceCellBinding!!.choiceContainer.setOnClickListener {
                    onSelectionListener?.let { it.onSelection(selection[position]) }
                }
                holder.productMultiChoiceCellBinding!!.executePendingBindings()
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (selectMode==SelectMode.SINGLE) return 0
        return 1
    }

    enum class SelectMode{SINGLE,MULTI}


    inner class ViewHolder(itemView: View,var selectMode: SelectMode) : RecyclerView.ViewHolder(itemView){
        var productSingleChoiceCellBinding : ProductSingleChoiceCellBinding? = null
        var productMultiChoiceCellBinding :ProductMultiChoiceCellBinding? = null
        init {
            if  (selectMode == SelectMode.SINGLE) {
                productSingleChoiceCellBinding = DataBindingUtil.bind<ProductSingleChoiceCellBinding>(itemView)
            }
            else{
                productMultiChoiceCellBinding = DataBindingUtil.bind<ProductMultiChoiceCellBinding>(itemView)
            }
        }
    }

}