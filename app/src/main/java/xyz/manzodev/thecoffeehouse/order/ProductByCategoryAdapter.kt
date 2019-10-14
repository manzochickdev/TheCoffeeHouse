package xyz.manzodev.thecoffeehouse.order

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import xyz.manzodev.thecoffeehouse.order.model.Product
import xyz.manzodev.thecoffeehouse.utils.Utils
import java.util.*
import kotlin.collections.ArrayList

class ProductByCategoryAdapter(fm: FragmentManager?,var products : ArrayList<Product>) : FragmentPagerAdapter(fm) {
    var categories : ArrayList<String> = ArrayList(products.distinctBy { product -> product.mainCategory }.map { product -> product.mainCategory })

    override fun getItem(position: Int): Fragment {
        var bundle = Bundle()
        var productByCategory = ArrayList(products.filter { p->p.mainCategory==categories[position] })
        bundle.putSerializable("products",productByCategory)

        var fragment = ProductByCategoryFragment()
        fragment.arguments = bundle

        return fragment

    }

    override fun getPageTitle(position: Int): CharSequence? {
        return categories[position]
    }

    override fun getCount(): Int {
        return categories.size
    }

}