package xyz.manzodev.thecoffeehouse.utils

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.StringDividerCellBinding

class StringDividerTemplateAdapter(var data:ArrayList<String>, var context: Context, var callback:(s:String)->Unit) : RecyclerView.Adapter<StringDividerTemplateAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.string_divider_cell,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.stringDividerCellBinding!!.text = data[position]
        holder.stringDividerCellBinding!!.container.setOnClickListener {
            callback(data[position])
        }
        holder.stringDividerCellBinding!!.executePendingBindings()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var stringDividerCellBinding = DataBindingUtil.bind<StringDividerCellBinding>(itemView)
    }
}