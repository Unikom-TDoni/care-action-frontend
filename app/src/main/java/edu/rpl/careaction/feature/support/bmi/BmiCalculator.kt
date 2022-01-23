package edu.rpl.careaction.feature.support.bmi

import kotlin.math.pow

class BmiCalculator {
    fun calculateBmi(weight: Double, height: Double): Double =
        weight / height.pow(2)

    fun getBmiResult(bmi: Double): String =
        when {
            bmi < 18.5 -> "You have a under weight "
            bmi > 18.5 && bmi < 22.9 -> "You have a normal weight"
            bmi > 23 && bmi < 29.9 -> "You have a over weight"
            bmi > 30 -> "You are obese"
            else -> "Can't calculate Bmi"
        }

    fun convertCmToM(number: Double) =
        number / 100
}