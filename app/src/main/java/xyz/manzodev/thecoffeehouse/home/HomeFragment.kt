package xyz.manzodev.thecoffeehouse.home


import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.FragmentHomeBinding
import xyz.manzodev.thecoffeehouse.home.model.News
import xyz.manzodev.thecoffeehouse.home.model.NewsWrapper
import xyz.manzodev.thecoffeehouse.user.LoginActivity
import xyz.manzodev.thecoffeehouse.user.UserDetailActivity
import xyz.manzodev.thecoffeehouse.user.model.User
import androidx.recyclerview.widget.LinearLayoutManager
import xyz.manzodev.thecoffeehouse.CartInfo


class HomeFragment : Fragment(), NewsAdapter.OnNewsClickListener {

    lateinit var fragmentHomeBinding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        configUser()
        configMusic()
        configNewsList()
        configAct()
        return fragmentHomeBinding.root
    }



    private fun configNewsList() {
        var news1 = News("BÁNH TRUNG THU THE COFFEE HOUSE: THỨC QUÀ MÙA TRĂNG MANG HỒN VỊ XƯA",
            "Mùa trăng tháng 8 đang về mang theo bao nao nức mong chờ cùng kỉ niệm gợi nhớ kí ức xa xăm của từng người. Tết của con nít đấy,...",
            "https://www.thecoffeehouse.com/blogs/news/banh-trung-thu-the-coffee-house-thuc-qua-mua-trang-mang-hon-vi-xua",
            "https://file.hstatic.net/1000075078/article/img_0588_copy_ef393894e1284b88b3e625ed0d2ffb7a_grande.jpg")

        var newsList = ArrayList<News>()
        newsList.add(news1)

        //var newsAdapter = NewsAdapter(NewsWrapper.listNews, context!!)
        var newsAdapter = NewsAdapter(newsList, context!!)
        newsAdapter!!.onNewsClickListener = this@HomeFragment

        fragmentHomeBinding.rvNews.adapter = newsAdapter


        //todo : release mode
//
//
//        var loadNews = object : AsyncTask<Void, Void, ArrayList<News>>() {
//            override fun doInBackground(vararg p0: Void?): ArrayList<News> {
//                return NewsWrapper.getNews()
//            }
//            override fun onPostExecute(result: ArrayList<News>?) {
//                newsAdapter!!.notifyDataSetChanged()
//            }
//
//        }.execute()
//
//        fragmentHomeBinding.rvNews.addOnScrollListener(object : RecyclerView.OnScrollListener(){
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                if (loadNews.status == AsyncTask.Status.RUNNING) return
//                Log.d("OK",loadNews.status.toString())
//                var mLayoutManager = recyclerView.layoutManager as LinearLayoutManager
//
//                val visibleItemCount = mLayoutManager.childCount
//                val totalItemCount = mLayoutManager.itemCount
//                val pastVisibleItems = mLayoutManager.findFirstVisibleItemPosition()
//                if (pastVisibleItems + visibleItemCount >= totalItemCount) {
//                    loadNews = object : AsyncTask<Void, Void, ArrayList<News>>() {
//                        override fun doInBackground(vararg p0: Void?): ArrayList<News> {
//                            return NewsWrapper.getNews()
//                        }
//                        override fun onPostExecute(result: ArrayList<News>?) {
//                            newsAdapter!!.notifyDataSetChanged()
//                        }
//
//                    }.execute()
//                }
//            }
//        })

    }

    private fun configUser() {
        User.getUser {
            fragmentHomeBinding.user = it
            CartInfo.getInstance().user = it
        }
    }


    private fun configAct() {
        fragmentHomeBinding.userBar.setOnClickListener {
            var intent: Intent
            intent = fragmentHomeBinding.user?.let {
                 Intent(context!!, UserDetailActivity::class.java)
            } ?: Intent(context!!, LoginActivity::class.java)


            startActivity(intent)


        }
    }

    private fun configMusic(){
        fragmentHomeBinding.ivSgo.startAnimation(AnimationUtils.loadAnimation(context!!,R.anim.rotation))
    }

    //act
    override fun onNewsClick(n: News) {
        var intent = Intent(context, NewsDetailActivity::class.java)
        intent.putExtra("news", n)
        startActivity(intent)
    }
}
