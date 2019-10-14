package xyz.manzodev.thecoffeehouse.addresspicker


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

import xyz.manzodev.thecoffeehouse.R
import xyz.manzodev.thecoffeehouse.databinding.FragmentDeliveryTimePickerBinding
import java.util.*
import kotlin.collections.ArrayList
import android.widget.ArrayAdapter
import xyz.manzodev.thecoffeehouse.CartInfo


class DeliveryTimePickerFragment : BottomSheetDialogFragment() {
    lateinit var fragmentDeliveryTimePickerBinding : FragmentDeliveryTimePickerBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentDeliveryTimePickerBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_delivery_time_picker, container, false)
        configSpinner()
        configAct()

        return fragmentDeliveryTimePickerBinding.root
    }

    //config
    private lateinit var availableTime : ArrayList<DeliveryTime>

    private fun configSpinner(){
        availableTime = calculateAvailableTime()

        val dateArrayAdapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, availableTime.map { it.day })
        dateArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        fragmentDeliveryTimePickerBinding.spDate.adapter = dateArrayAdapter

        fragmentDeliveryTimePickerBinding.spDate.setSelection(0)
    }

    var selectedDay:Int = -1
    private fun configAct(){
        fragmentDeliveryTimePickerBinding.spDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {}

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                val hoursArrayAdapter = ArrayAdapter<String>(context!!, android.R.layout.simple_spinner_item, availableTime[position].hours)
                hoursArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

                fragmentDeliveryTimePickerBinding.spHour.adapter = hoursArrayAdapter

                fragmentDeliveryTimePickerBinding.tvTime.text = availableTime[position].day
                selectedDay = position
            }
        }

        fragmentDeliveryTimePickerBinding.spHour.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(p0: AdapterView<*>?) {
            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                if (selectedDay>-1) fragmentDeliveryTimePickerBinding.tvTime.text = "${availableTime[selectedDay].day} , ${availableTime[selectedDay].hours[position]}"
            }

        }

        fragmentDeliveryTimePickerBinding.btnPick.setOnClickListener {
            (activity as AddressPickerActivity).notifyDeliveryTimeChange(fragmentDeliveryTimePickerBinding.tvTime.text.toString())
            dismiss()
        }
    }

    //func
    private fun calculateAvailableTime() : ArrayList<DeliveryTime>{
        var availableTime = ArrayList<DeliveryTime>()

        var cal = Calendar.getInstance()
        cal.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"))
        var current = cal.get(Calendar.HOUR_OF_DAY)

        if (current < 7) {
            val hour = ArrayList<String>()
            for (i in 7..21) {
                val s = "$i:00 - $i:30"
                hour.add(s)
            }
            availableTime.add(DeliveryTime("Today", hour))
            addStableTime(availableTime, cal)
            return availableTime
        } else if (current in 8..20) {
            val hour = ArrayList<String>()
            hour.add("Now")
            for (i in current + 1..21) {
                val s = "$i:00 - $i:30"
                hour.add(s)
            }
            availableTime.add(DeliveryTime("Today", hour))
            addStableTime(availableTime, cal)
            return availableTime
        } else {
            addStableTime(availableTime, cal)
            return availableTime
        }
    }

    private fun addStableTime(list : ArrayList<DeliveryTime>,cal:Calendar){
        val hour = ArrayList<String>()
        for (i in 7..21) {
            val s = "$i:00 - $i:30"
            hour.add(s)
        }

        for (i in 0..1) {
            cal.add(Calendar.DAY_OF_MONTH, 1)
            val day =
                cal.get(Calendar.DAY_OF_MONTH).toString() + "/" + (cal.get(Calendar.MONTH) + 1)
            list.add(DeliveryTime(day, hour))
        }
    }

    inner class DeliveryTime(val day:String,val hours: ArrayList<String>)
}
