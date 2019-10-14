package xyz.manzodev.thecoffeehouse.home

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.AsyncTask
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.ActivityNewsDetailBinding
import xyz.manzodev.thecoffeehouse.home.model.News
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.ViewGroup
import android.view.WindowManager


class NewsDetailActivity : AppCompatActivity() {

    lateinit var activityNewsDetailBinding : ActivityNewsDetailBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityNewsDetailBinding = DataBindingUtil.setContentView<ActivityNewsDetailBinding>(
            this,
            R.layout.activity_news_detail
        )

        var news = intent.getSerializableExtra("news") as News?
        news?.let {
            activityNewsDetailBinding.news = it
            //jsoup
            getNewsDetail(it)
            //end jsoup
        }

        configAction()
    }

    private fun configAction() {
        activityNewsDetailBinding.tvDismiss.setOnClickListener { finish() }
    }

    private fun getNewsDetail(it: News){
        object : AsyncTask<Void, Void, Document>() {
            override fun doInBackground(vararg p0: Void?): Document {
                val doc = Jsoup.connect(it.url).get()
                return doc
            }

            override fun onPostExecute(result: Document?) {
                val outer = result!!.select("div.article_content > p")
                for (e in outer) {

                    if (e.select("span").text().isNotEmpty()) {
                        var textView = TextView(this@NewsDetailActivity)
                        textView.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT
                        )
                        textView.text = e.select("span").text()
                        val typeface = Typeface.createFromAsset(assets, "font/Lato-Light.ttf")
                        textView.typeface = typeface
                        textView.setTextColor(resources.getColor(R.color.black))
                        textView.textSize = 18f
                        textView.setLineSpacing(8f,1f)
                        val params = LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)
                        val margin = resources.getDimensionPixelSize(R.dimen.defaultMargin)
                        params.setMargins(margin,margin,margin,margin)
                        textView.layoutParams = params
                        activityNewsDetailBinding.contentHolder.addView(textView)
                    } else if (e.select("img").attr("src").isNotEmpty()) {
                        var imgUrl = e.select("img").attr("src")

                        var imageView = ImageView(this@NewsDetailActivity)
                        imageView.layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            resources.getDimensionPixelSize(R.dimen.image)
                        )
                        Glide.with(this@NewsDetailActivity).load(imgUrl).into(imageView)
                        imageView.scaleType = ImageView.ScaleType.CENTER_CROP

                        activityNewsDetailBinding.contentHolder.addView(imageView)
                    }

                }
            }
        }.execute()

    }
}
