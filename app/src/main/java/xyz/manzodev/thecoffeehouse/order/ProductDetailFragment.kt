package xyz.manzodev.thecoffeehouse.order


import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment

import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.FragmentProductDetailBinding
import xyz.manzodev.thecoffeehouse.CartInfo
import xyz.manzodev.thecoffeehouse.order.model.Product

class ProductDetailFragment : DialogFragment() {

    lateinit var mode : CartInfo.Mode
    lateinit var fragmentProductDetailBinding : FragmentProductDetailBinding

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var dialog =  super.onCreateDialog(savedInstanceState)
        dialog.window.requestFeature(Window.FEATURE_NO_TITLE)
        return dialog
    }

    override fun onStart() {
        super.onStart()

        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentProductDetailBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_product_detail, container, false)
        configData()
        return fragmentProductDetailBinding.root
    }

    //config
    lateinit var product : Product
    lateinit var viewmodel : ProductDetailViewModel

    private fun configData() {
        var bundle = arguments
        bundle?.let {
            product = it.get("product") as Product
            mode = it.get("mode") as CartInfo.Mode
            bindingData(product)
        }
    }

    private fun bindingData(product : Product) {
        viewmodel = ProductDetailViewModel(product,this,mode)
        fragmentProductDetailBinding.viewmodel = viewmodel
        fragmentProductDetailBinding.rvSize.adapter = viewmodel.sizeAdapter
        fragmentProductDetailBinding.rvTopping.adapter = viewmodel.toppingAdapter
    }

    //act

    fun updateCartAddMode(product:Product){
        CartInfo.getInstance().updateProduct(product, {
            (parentFragment as OrderFragment).updateCart()
            dismiss()
        }, CartInfo.Mode.ADD)
    }

    fun updateCartEditMode(product:Product){
        CartInfo.getInstance().updateProduct(product,{
            (activity!! as CartDetailActivity).updateCart()
            dismiss()
        }, CartInfo.Mode.EDIT)
    }

    fun updateCartRemove(product:Product){
        CartInfo.getInstance().removeProduct(product){
            (activity!! as CartDetailActivity).updateCart()
            dismiss()
        }
    }






}
