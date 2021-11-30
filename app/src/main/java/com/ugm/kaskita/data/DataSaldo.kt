package com.ugm.kaskita.data

import com.ugm.kaskita.utils.Time
import com.ugm.kaskita.utils.Time.toString

data class DataSaldo(
    val name: String? = "",
    val total: Long  = 0,
    val type: String?  = "",
    val tanggal: String?  = Time.getCurrentDateTime().toString("yyyy/MM/dd HH:mm:ss")
)
