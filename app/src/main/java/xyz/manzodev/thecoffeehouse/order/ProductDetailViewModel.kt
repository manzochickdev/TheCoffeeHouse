package xyz.manzodev.thecoffeehouse.order

import android.content.Context
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.databinding.library.baseAdapters.BR
import xyz.manzodev.thecoffeehouse.CartInfo
import xyz.manzodev.thecoffeehouse.order.model.Product
import xyz.manzodev.thecoffeehouse.order.model.ProductSelection

class ProductDetailViewModel(private var p: Product,var view:ProductDetailFragment,var mode : CartInfo.Mode) : BaseObservable(){

    var product:Product = if (mode==CartInfo.Mode.EDIT) p else Product(p.name,p.desc,p.imgUrl,p.cloneSize(),p.cloneTopping(),p.mainCategory,p.subCategory)

    @get:Bindable
    var quantity : Int = p.quantity
        set(value) {
            field = value
            notifyPropertyChanged(BR.quantity)
            notifyPropertyChanged(BR.price)
        }

    @get:Bindable
    val price : Long
    get() = quantity * (product.size.findLast { it.isSelected }?.price!! +
            (product.topping?.let { it.filter { selection -> selection.isSelected }.sumByDouble {selection -> selection.price.toDouble()}.toLong()} ?: 0L)!!)


    init {
        setUpSize()
        setUpTopping()
    }



    fun addProduct() {
        product.quantity = quantity
        product.price = price
        product.orderDesc = product.size.filter { selection -> selection.isSelected }.joinToString(" , ", "Size : ", "") { it.name } + "\n"+
                product.topping?.let{
                    if(it.any { selection -> selection.isSelected }) {
                        it.joinToString(" , ","Topping : ","") {selection->selection.name}
                    } else "" }

        if  (mode== CartInfo.Mode.ADD){
            view.updateCartAddMode(product)
        }
        else{
            view.updateCartEditMode(product)
        }
    }

    fun removeProduct(){
        view.updateCartRemove(p)
    }

    fun increaseQuantity(){
        quantity++
        notifyPropertyChanged(BR.quantity)
    }

    fun decreaseQuantity(){
        if (quantity<=1) {
            if (mode== CartInfo.Mode.EDIT){
                quantity=0
                notifyPropertyChanged(BR.removeButtonVisible)
                notifyPropertyChanged(BR.quantity)
            }
            return
        }
        quantity--
        notifyPropertyChanged(BR.removeButtonVisible)
        notifyPropertyChanged(BR.quantity)

    }

    @get:Bindable
    var removeButtonVisible : Boolean = false
    get() = quantity==0


    enum class ChangedProperties {SIZE,TOPPING}

    fun toggleSelection(selection:ProductSelection,changedProperties: ChangedProperties){
        if (changedProperties== ChangedProperties.SIZE){
            product.size.forEach { it.isSelected = (it==selection) }
        }
        else{
            var found  = product.topping!!.findLast { it==selection }
            found?.let { it.isSelected  = !it.isSelected}
        }
    }

    //extension
    fun Product.cloneSize() : ArrayList<ProductSelection>{
        return ArrayList(this.size.map { it.clone() })
    }

    fun Product.cloneTopping() : ArrayList<ProductSelection>?{
        return ArrayList(this.topping?.map { it.clone() })
    }

    //config
    lateinit var sizeAdapter : SelectionAdapter
    private fun setUpSize(){
        sizeAdapter = SelectionAdapter(product.size,view.context!!,SelectionAdapter.SelectMode.SINGLE)
        sizeAdapter.onSelectionListener = object : SelectionAdapter.OnSelectionListener{
            override fun onSelection(selection: ProductSelection) {
                toggleSelection(selection,ChangedProperties.SIZE)
                notifyPropertyChanged(BR.price)
                sizeAdapter.notifyDataSetChanged()
            }

        }
    }

    var toppingAdapter : SelectionAdapter?=null

    private fun setUpTopping(){
        product.topping?.let {
            toppingAdapter = SelectionAdapter(it,view.context!!,SelectionAdapter.SelectMode.MULTI)
            toppingAdapter!!.onSelectionListener = object  : SelectionAdapter.OnSelectionListener{
                override fun onSelection(selection: ProductSelection) {
                    toggleSelection(selection,ChangedProperties.TOPPING)
                    notifyPropertyChanged(BR.price)
                    toppingAdapter!!.notifyDataSetChanged()
                }
            }
        }
    }



}