package com.ugm.kaskita.utils

import android.content.Context
import android.util.Log
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

object Logs {
    
    fun String.loge(context: Context) {
        Log.e(context.packageName, this)
    }
    
    fun Int.formatRupiah(): String {
        val localeID = Locale("in", "ID")
        val format =
            NumberFormat.getCurrencyInstance(localeID)
        return format.format(this.toLong())
    }    
    
    fun Float.formatRupiah(): String {
        val localeID = Locale("in", "ID")
        val format =
            NumberFormat.getCurrencyInstance(localeID)
        return format.format(this.toLong())
    }    
    
    fun Long.formatRupiah(): String {
        val localeID = Locale("in", "ID")
        val format =
            NumberFormat.getCurrencyInstance(localeID)
        return format.format(this.toLong())
    }
}