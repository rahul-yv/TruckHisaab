package com.truckhisaab.util

object Validators {

    fun isValidPhone(phone: String): Boolean {
        val cleaned = phone.replace(" ", "").replace("-", "")
        return cleaned.length == 10 && cleaned.all { it.isDigit() } && cleaned[0] in '6'..'9'
    }

    fun isValidOtp(otp: String): Boolean =
        otp.length == 6 && otp.all { it.isDigit() }

    fun isValidAmount(amount: String): Boolean {
        val value = amount.toDoubleOrNull() ?: return false
        return value > 0 && value < 10_000_000
    }

    fun isValidTruckNumber(number: String): Boolean {
        val cleaned = number.replace(" ", "").replace("-", "").uppercase()
        return cleaned.length in 8..12 && cleaned.any { it.isLetter() } && cleaned.any { it.isDigit() }
    }

    fun isValidWeight(weight: String): Boolean {
        val value = weight.toDoubleOrNull() ?: return false
        return value > 0 && value <= 100
    }

    fun isValidName(name: String): Boolean =
        name.trim().length >= 2

    fun isValidLicenseNumber(license: String): Boolean =
        license.trim().length >= 10

    fun formatPhone(phone: String): String {
        val cleaned = phone.replace(" ", "").replace("-", "")
        return if (cleaned.length == 10) {
            "${cleaned.substring(0, 5)} ${cleaned.substring(5)}"
        } else cleaned
    }
}
