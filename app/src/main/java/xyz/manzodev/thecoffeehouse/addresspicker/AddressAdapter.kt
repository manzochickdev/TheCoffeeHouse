package xyz.manzodev.thecoffeehouse.addresspicker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.AddressCellBinding

class AddressAdapter(var address:ArrayList<Pair<String,String>>, var context: Context, var onAddressSelected : (a:Pair<String,String>)->Unit) :  RecyclerView.Adapter<AddressAdapter.ViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.address_cell,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return address.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.addressCellBinding.tvAddress.text = address[position].first
        holder.addressCellBinding.addressHolder.setOnClickListener {
            onAddressSelected(address[position])
        }
        holder.addressCellBinding.executePendingBindings()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var addressCellBinding = DataBindingUtil.bind<AddressCellBinding>(itemView) as AddressCellBinding
    }
}