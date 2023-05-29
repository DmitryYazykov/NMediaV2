package ru.netology.nmedia.activity

object FormatNumber {
    fun format(number: Long): String {
        return when {
            number < 1_000 -> number.toString()
            number < 10_000 -> {
                val decimalPart = (number % 1_000) / 100
                val formattedDecimalPart = if (decimalPart > 0L) ".$decimalPart"
                else ""
                (number / 1_000).toString() + formattedDecimalPart + "K"
            }

            number < 1_000_000 -> {
                val thousandsPart = number / 1_000
                val formattedNumber = if (number % 1000L == 0L) thousandsPart.toString()
                else String.format(thousandsPart.toString())
                formattedNumber + "K"
            }

            number < 10_000_000 -> {
                val millionsPart = number / 1_000_000
                val decimalPart = (number % 1_000_000) / 100_000
                val formattedDecimalPart = if (decimalPart > 0L) ".$decimalPart"
                else ""
                val formattedNumber = millionsPart.toString() + formattedDecimalPart + "M"
                formattedNumber
            }

            else -> {
                val millionsPart = number / 1_000_000
                val formattedNumber = if (number % 1_000_000L == 0L) millionsPart.toString()
                else String.format(millionsPart.toString())
                formattedNumber + "M"
            }
        }
    }
}