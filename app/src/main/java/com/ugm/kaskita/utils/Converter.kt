package com.ugm.kaskita.utils

import java.text.NumberFormat
import java.util.*

object Converter {

    fun String.formatRupiah(): String {
        val localeID = Locale("in", "ID")
        val format =
            NumberFormat.getCurrencyInstance(localeID)
        return format.format(this.toLong())
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
        return format.format(this)
    }
}