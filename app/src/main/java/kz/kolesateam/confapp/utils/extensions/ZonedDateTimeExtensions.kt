package kz.kolesateam.confapp.utils.extensions

import org.threeten.bp.ZonedDateTime

fun ZonedDateTime.getEventFormattedDateTime(): String {
    val hourString = getNumberPrefixedWithZero(this.hour)
    val minuteString = getNumberPrefixedWithZero(this.minute)

    return "$hourString:$minuteString"
}

private fun getNumberPrefixedWithZero(
    number: Int
): String = if (number > 9){
    number.toString()
} else {
    "0$number"
}