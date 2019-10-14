package xyz.manzodev.thecoffeehouse.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.NewsCellBinding
import xyz.manzodev.thecoffeehouse.home.model.News

class NewsAdapter(var newsList : ArrayList<News> , var context : Context) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    interface OnNewsClickListener{
        fun onNewsClick(n:News)
    }

    var onNewsClickListener : OnNewsClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.news_cell,parent,false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return newsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.newsCellBinding!!.news = newsList[position]
        holder.newsCellBinding!!.newsHolder.setOnClickListener {
            onNewsClickListener?.let {
                it.onNewsClick(newsList[position])
            }
        }
        holder.newsCellBinding!!.executePendingBindings()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var newsCellBinding= DataBindingUtil.bind<NewsCellBinding>(itemView)
    }
}