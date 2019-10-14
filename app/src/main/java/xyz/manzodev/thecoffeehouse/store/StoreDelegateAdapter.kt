package xyz.manzodev.thecoffeehouse.store

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.StoreDelegateCellBinding
import xyz.manzodev.thecoffeehouse.store.model.StoreDelegate

class StoreDelegateAdapter(var storeDelegate : ArrayList<StoreDelegate>,var context : Context) :
    RecyclerView.Adapter<StoreDelegateAdapter.ViewHolder>() {

    interface OnStoreDelegateClickListener{
        fun onStoreDelegateClick( s: StoreDelegate)
    }


    var onStoreDelegateClickListener : OnStoreDelegateClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.store_delegate_cell,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        //return storeDelegate.size
        return 5
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.storeDelegateCellBinding!!.tvName.text = storeDelegate.get(0).name
        holder.storeDelegateCellBinding!!.tvName.setOnClickListener {
            onStoreDelegateClickListener?.let { it.onStoreDelegateClick(storeDelegate.get(0)) }
        }
        holder.storeDelegateCellBinding!!.executePendingBindings()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var storeDelegateCellBinding = DataBindingUtil.bind<StoreDelegateCellBinding>(itemView)
    }
}