package xyz.manzodev.thecoffeehouse.account


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import xyz.manzodev.thecoffeehouse.CartInfo

import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.account.orderhistory.OrderHistoryActivity
import xyz.manzodev.thecoffeehouse.databinding.FragmentAccountBinding
import xyz.manzodev.thecoffeehouse.user.model.User

class AccountFragment : Fragment() {

    private lateinit var fragmentAccountBinding : FragmentAccountBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentAccountBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_account, container, false)

        configUser()
        configData()
        configAct()

        return fragmentAccountBinding.root
    }

    private fun configUser(){
        User.getUser {user->
            user?.let {
                fragmentAccountBinding.user = it
                CartInfo.getInstance().user = user
            }
        }
    }

    private fun configData(){
        fragmentAccountBinding.btnOrderHistory.visibility = if (CartInfo.getInstance().user!=null) VISIBLE else GONE
    }

    private fun configAct(){
        fragmentAccountBinding.btnNowPlaying.setOnClickListener {
            var intent = Intent(context!!,NowPlayingMusicActivity::class.java)
            startActivity(intent)
        }
        fragmentAccountBinding.btnOrderHistory.setOnClickListener {
            var intent = Intent(context!!,OrderHistoryActivity::class.java)
            startActivity(intent)
        }
    }
}
