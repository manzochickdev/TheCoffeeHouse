package xyz.manzodev.thecoffeehouse

import androidx.databinding.DataBindingUtil
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import android.widget.Toast.LENGTH_SHORT
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import xyz.manzodev.thecoffeehouse.account.AccountFragment
import xyz.manzodev.thecoffeehouse.store.StoreFragment
import xyz.manzodev.thecoffeehouse.databinding.ActivityMainBinding
import xyz.manzodev.thecoffeehouse.home.HomeFragment
import xyz.manzodev.thecoffeehouse.order.OrderFragment
import xyz.manzodev.thecoffeehouse.store.model.Store
import xyz.manzodev.thecoffeehouse.user.model.User

class MainActivity : AppCompatActivity(), View.OnClickListener {

    //properties
    lateinit var activityMainBinding: ActivityMainBinding

    //life cycle
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fullScreenMode()
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        init()
    }

    //config
    private fun fullScreenMode(){
        requestWindowFeature(Window.FEATURE_NO_TITLE)

        // Make Fullscreen
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN)

        // Hide the toolbar
        supportActionBar?.hide()
    }
    private fun init() {


        activityMainBinding.btnHome.setOnClickListener(this)
        activityMainBinding.btnOrder.setOnClickListener(this)
        activityMainBinding.btnStore.setOnClickListener(this)
        activityMainBinding.btnAccount.setOnClickListener(this)

        var homeFragment = HomeFragment()
        inflateFragment(homeFragment,FragmentMode.REPLACE)
        notifySelectedChange(0)
    }


    //func
    private fun notifySelectedChange(selected: Int) {
        activityMainBinding.selected = selected
    }

    //act
    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_home -> {
                notifySelectedChange(0)
                var homeFragment = HomeFragment()
                inflateFragment(homeFragment,FragmentMode.REPLACE)
            }

            R.id.btn_order -> {
                notifySelectedChange(1)
                var orderFragment = OrderFragment()
                inflateFragment(orderFragment,FragmentMode.REPLACE)
            }
            R.id.btn_store -> {
                notifySelectedChange(2)
                var storeFragment = StoreFragment()
                inflateFragment(storeFragment,FragmentMode.REPLACE)
            }
            R.id.btn_account -> {
                notifySelectedChange(3)
                var accountFragment = AccountFragment()
                inflateFragment(accountFragment,FragmentMode.REPLACE)
            }
            else -> {
            }
        }
    }

    enum class FragmentMode { ADD, REPLACE }

    fun inflateFragment(fragment: Fragment, mode: FragmentMode, bundle: Bundle? = null) {
//        if  (supportFragmentManager.backStackEntryCount>=1){
//            if (supportFragmentManager.getBackStackEntryAt(supportFragmentManager.backStackEntryCount-1).name == fragment.javaClass.simpleName) return
//        }

        if (supportFragmentManager.findFragmentByTag(fragment.javaClass.simpleName)!=null) return
        fragment.arguments = bundle

        var transaction = supportFragmentManager.beginTransaction()
        if (mode == FragmentMode.REPLACE) {
            transaction.replace(R.id.fragment_container, fragment, fragment.javaClass.simpleName)
        } else {
            transaction.add(R.id.fragment_container, fragment, fragment.javaClass.simpleName)
        }
        Log.d("OK",fragment.javaClass.simpleName)
        transaction.commit()
    }

    override fun onBackPressed(){
        if (supportFragmentManager.findFragmentByTag(HomeFragment::class.java.simpleName)==null){
            var homeFragment = HomeFragment()
            inflateFragment(homeFragment,FragmentMode.REPLACE)
            notifySelectedChange(0)
        }
//        Log.d("OK",""+supportFragmentManager.backStackEntryCount)
//        if (supportFragmentManager.backStackEntryCount > 1 ){
//            var name = supportFragmentManager.getBackStackEntryAt(0).name
//            supportFragmentManager.popBackStackImmediate(name,0)
//            var homeFragment = HomeFragment()
//            inflateFragment(homeFragment,FragmentMode.REPLACE)
//        }

        else super.onBackPressed()
    }
}
