package xyz.manzodev.thecoffeehouse.home.model

import android.R.attr
import android.os.AsyncTask
import android.util.Log
import java.nio.file.Files.size
import org.jsoup.Jsoup



object NewsWrapper {
    private var baseUrl = "https://www.thecoffeehouse.com"
    private var nextPage : String? = "/blogs/news"

    var listNews  = ArrayList<News>()

    fun getNews() : ArrayList<News>{
        nextPage?.let {
            val doc = Jsoup.connect(baseUrl+nextPage).get()

            val outer = doc.select("div.blog_lists > div.blog_item")

            for (e in outer) {
                if (e.select("div > h3 > a").text().isNotBlank())
                {
                    var title = e.select("div > h3 > a").text()
                    var url = baseUrl + e.select("div > h3 > a").attr("href")
                    var summary = e.select("div > div.short_des").text()
                    var tempUrl = e.select("a > div").attr("style").replace("background-image: url(//", "")
                    val imgUrl = "https://" + tempUrl.substring(0,tempUrl.length-1)


                    var news = News(title, summary, url, imgUrl)
                    //ok
                    listNews!!.add(news)
                }

            }

            nextPage =  doc.select("ul.pagination > li > a.next").attr("href")
        }



        return listNews!!
    }
}