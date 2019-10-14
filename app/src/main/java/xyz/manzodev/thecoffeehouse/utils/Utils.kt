package xyz.manzodev.thecoffeehouse.utils

import java.util.*
import java.text.SimpleDateFormat


object Utils {
    fun reqCode() : Int{
        val currentTime = Calendar.getInstance().getTimeInMillis()
        val key = java.lang.Long.toString(currentTime)
        val length = key.length
        return Integer.parseInt(key.substring(length - 4, length))
    }

    fun getDate(): String {
        val dateFormat = SimpleDateFormat("HH:mm dd/MM/yyyy")
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Ho_Chi_Minh"))
        val today = Calendar.getInstance().time
        return dateFormat.format(today)
    }

}