package com.ugm.kaskita.utils

import android.widget.EditText

object Validation {

    fun EditText.isEmpty(): Boolean {
        if (this.text.toString() == ""){
            return true
        }
        return false
    }

    fun EditText.itText(): String {
        return this.text.toString()
    }
}