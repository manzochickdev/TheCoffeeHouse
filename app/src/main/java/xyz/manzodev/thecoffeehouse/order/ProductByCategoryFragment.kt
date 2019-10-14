package xyz.manzodev.thecoffeehouse.order


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil

import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.FragmentProductByCategoryBinding
import xyz.manzodev.thecoffeehouse.CartInfo
import xyz.manzodev.thecoffeehouse.order.model.Product


class ProductByCategoryFragment : Fragment(){


    private lateinit var fragmentProductByCategoryBinding: FragmentProductByCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentProductByCategoryBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_product_by_category, container, false)
        configProduct()
        return fragmentProductByCategoryBinding.root
    }

    private fun configProduct() {
        var bundle = arguments
        bundle?.let {
            var products = bundle.getSerializable("products") as ArrayList<Product>

            var adapter = ProductSubCategoryAdapter(products,context!!,(parentFragment as OrderFragment))
            fragmentProductByCategoryBinding.rvProduct.adapter = adapter

        }

    }

    fun scrollToSubCategory(y:Int){
        fragmentProductByCategoryBinding.rvProduct.smoothScrollToPosition(y)
    }

}
