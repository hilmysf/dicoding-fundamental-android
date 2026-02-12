package com.hilmysf.fundamental.helper

import java.text.SimpleDateFormat
import java.util.Locale

object DateHelper {
    fun formatDate(inputDate: String): String? {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputFormat =
            SimpleDateFormat("HH:mm dd MMMM yyyy", Locale("id", "ID"))
        val date = inputFormat.parse(inputDate)
        val result = date?.let { outputFormat.format(it) }
        return result
    }
}