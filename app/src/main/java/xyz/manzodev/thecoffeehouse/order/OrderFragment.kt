package xyz.manzodev.thecoffeehouse.order


import android.app.Dialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil

import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.FragmentOrderBinding
import xyz.manzodev.thecoffeehouse.CartInfo
import xyz.manzodev.thecoffeehouse.order.model.Product
import xyz.manzodev.thecoffeehouse.order.model.ProductSelection
import android.app.Activity.RESULT_OK
import android.graphics.Typeface
import android.view.Gravity
import android.view.WindowManager
import android.widget.TextView
import org.w3c.dom.Text
import xyz.manzodev.thecoffeehouse.addresspicker.AddressPickerActivity
import xyz.manzodev.thecoffeehouse.databinding.SubcategoryLayoutBinding
import xyz.manzodev.thecoffeehouse.utils.StringDividerTemplateAdapter
import xyz.manzodev.thecoffeehouse.utils.Utils


class OrderFragment : Fragment(),ProductAdapter.OnProductClickListener {

    lateinit var fragmentOrderBinding: FragmentOrderBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentOrderBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_order, container, false)
        configViewPager()
        configAct()
        configTab()

        return fragmentOrderBinding.root
    }


    override fun onResume() {
        super.onResume()
        updateCart()
    }

    //config
    lateinit var productList: ArrayList<Product>

    private fun configViewPager() {
        var sizes = ArrayList<ProductSelection>()
        sizes.add(ProductSelection("Nho", 5))
        sizes.add(ProductSelection("Lon", 10))

        var topping = ArrayList<ProductSelection>()
        topping.add(ProductSelection("Loai 1", 5))
        topping.add(ProductSelection("Loai 2", 10))


        var product = Product(
            "BẠC SỈU",
            "Theo chân những người gốc Hoa đến định cư tại Sài Gòn, Bạc sỉu là cách gọi tắt của \"Bạc tẩy sỉu phé\" trong tiếng Quảng Đông, chính là: Ly sữa trắng kèm một chút cà phê.",
            "http://product.hstatic.net/1000075078/product/white_vnese_coffee_9968c1184d7f4634a9bb9fce7b5ff313_master.jpg",
            sizes, topping, "Beverage", "Coffee"
        )

        var p2 = Product("Nau Da", "", "", sizes, topping, "Beverage", "Coffee")
        var p3 = Product("Den Da", "", "", sizes, topping, "Beverage", "Coffee")

        var p4 = Product("Tra bi dao", "", "", sizes, topping, "Beverage", "Tra")
        var p5 = Product("Do An", "", "", sizes, topping, "Food", "Do an")

        productList = ArrayList<Product>(arrayListOf(product, p2, p3, p4, p5))
        var adapter = ProductByCategoryAdapter(childFragmentManager!!, productList)
        fragmentOrderBinding.vpProduct.adapter = adapter
        fragmentOrderBinding.tabCategory.setupWithViewPager(fragmentOrderBinding.vpProduct)

    }

    private fun configTab() {
        var vg = fragmentOrderBinding.tabCategory.getChildAt(0) as ViewGroup
        var count = vg.childCount
        for (j in 0 until count) {
            var vgTab = vg.getChildAt(j) as ViewGroup
            var tabChildCount = vgTab.childCount
            for (i in 0 until tabChildCount) {
                var tabViewChild = vgTab.getChildAt(i)
                if (tabViewChild is TextView) {
                    val typeface = Typeface.createFromAsset(context!!.assets, "font/Lato-Semibold.ttf")
                    tabViewChild.typeface = typeface
                    tabViewChild.textSize = 16f
                }
            }
        }
    }

    var dialog: Dialog? = null
    private fun configSubcategory() {
        if (dialog != null) {
            dialog!!.show()
            return
        }
        dialog = Dialog(context!!)
        var view =
            LayoutInflater.from(context).inflate(R.layout.subcategory_layout, null, false)
        var binding =
            DataBindingUtil.bind<SubcategoryLayoutBinding>(view) as SubcategoryLayoutBinding

        dialog!!.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog!!.window.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        dialog!!.setCanceledOnTouchOutside(true)
        dialog!!.setContentView(view)

        var layoutParams = dialog!!.window.attributes
        dialog!!.window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        layoutParams.gravity = Gravity.TOP or Gravity.LEFT
        dialog!!.window.attributes = layoutParams
        dialog!!.show()


        var adapter = StringDividerTemplateAdapter(
            ArrayList(productList.distinctBy { p -> p.subCategory }.map { p -> p.subCategory }),
            context!!,
            this::onSubcategoryClick
        )
        binding.rvSubcategory.adapter = adapter
        binding.btnDismiss.setOnClickListener { dialog!!.dismiss() }
        binding.viewHolder.setOnClickListener { dialog!!.dismiss() }
    }


    //func
    private fun onSubcategoryClick(s: String) {
        var position = findCategoryPosition(s)
        fragmentOrderBinding.vpProduct.currentItem = position!!.first
        var page =
            childFragmentManager.findFragmentByTag("android:switcher:" + R.id.vp_product + ":" + fragmentOrderBinding.vpProduct.currentItem) as ProductByCategoryFragment
        page.scrollToSubCategory(position.second)
        dialog!!.dismiss()
    }

    fun findCategoryPosition(category: String): Pair<Int, Int>? {
        var categories: ArrayList<String> =
            ArrayList(productList.distinctBy { product -> product.mainCategory }.map { product -> product.mainCategory })
        var subCategories = ArrayList<ArrayList<String>>()
        for (c in categories) {
            subCategories.add(ArrayList(productList.filter { p -> p.mainCategory == c }.distinctBy { p -> p.subCategory }.map { p -> p.subCategory }))
        }

        var x = -1
        var y = -1

        for (c in subCategories) {
            for (s in c) {
                if (s == category) {
                    y = c.indexOf(s)
                    x = subCategories.indexOf(c)
                    break
                }
            }
        }

        if (x == -1 || y == -1) return null
        return Pair(x, y)
    }

    fun updateCart() {
        fragmentOrderBinding.cart = CartInfo.getInstance()
    }

    private fun configAct() {
        fragmentOrderBinding.cartLayout.setOnClickListener {
            var intent = Intent(context!!, CartDetailActivity::class.java)
            startActivity(intent)
        }

        fragmentOrderBinding.btnSubcategory.setOnClickListener {
            configSubcategory()
        }

        fragmentOrderBinding.layoutPlace.setOnClickListener {
            var intent = Intent(context!!, AddressPickerActivity::class.java)
            startActivityForResult(intent, 1212)
        }
        fragmentOrderBinding.ivSearch.setOnClickListener {
            var intent = Intent(context!!,ProductSearchActivity::class.java)
            intent.putExtra("products",productList)

            startActivityForResult(intent,1213)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            when (requestCode) {
                1212 -> {
                    var address = data!!.getStringExtra("address")
                    fragmentOrderBinding.tvAddress.text = address
                }

                1213->{
                    var product = data!!.getSerializableExtra("product") as Product
                    onProductClick(product)
                }
            }
        }
    }


    //act

    override fun onProductClick(p: Product) {
        var bundle = Bundle()
        bundle.putSerializable("product",p)
        bundle.putSerializable("mode", CartInfo.Mode.ADD)
        var productDetailFragment = ProductDetailFragment()
        productDetailFragment.arguments = bundle

        productDetailFragment.show(childFragmentManager!!,ProductDetailFragment::class.java.simpleName)
    }
}
