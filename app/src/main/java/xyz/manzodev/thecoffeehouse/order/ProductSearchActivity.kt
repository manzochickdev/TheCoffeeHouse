package xyz.manzodev.thecoffeehouse.order

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.ActivityProductSearchBinding
import xyz.manzodev.thecoffeehouse.order.model.Product
import xyz.manzodev.thecoffeehouse.utils.StringUtils

class ProductSearchActivity : AppCompatActivity(), ProductAdapter.OnProductClickListener {

    lateinit var activityProductSearchBinding: ActivityProductSearchBinding
    lateinit var products : ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityProductSearchBinding = DataBindingUtil.setContentView(this,R.layout.activity_product_search)

        products = ArrayList<Product>()
        var adapter = ProductAdapter(products,this)
        adapter.onProductClickListener = this
        activityProductSearchBinding.rvProduct.adapter = adapter
        activityProductSearchBinding.rvProduct.layoutManager = GridLayoutManager(this,2,GridLayoutManager.VERTICAL,false)


        var data = intent.getSerializableExtra("products") as ArrayList<Product>


        activityProductSearchBinding.etSearch.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                products.clear()
                var q = StringUtils.searchConvert(p0.toString().trim().toLowerCase())

                if (q.isNotBlank() && q!!.length>=2){
                    products.addAll(data.filter { p->StringUtils.searchConvert(p.name.toLowerCase()).contains(q!!)})
                }

                adapter.notifyDataSetChanged()
            }

        })


    }

    override fun onProductClick(p: Product) {
        var intent = Intent()
        intent.putExtra("product",p)
        setResult(Activity.RESULT_OK,intent)
        finish()
    }
}
