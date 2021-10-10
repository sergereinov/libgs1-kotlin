package com.github.sergereinov.libgs1

import java.lang.StringBuilder

data class FieldAI(
    val ai: AI,
    val textAi: String,
    val textBody: String,
    val referencePos: Int,
    val referenceLen: Int
) {

    fun formatBody(dateAsISO: Boolean = false): String? {

        if (textBody.isEmpty()) return null

        when(ai.dataFormat) {

            AI.GS1_STRING -> return textBody

            AI.GS1_DATE -> {
                if (textBody.indexOfFirst { !it.isDigit() } >= 0) return null

                //YYMMDD
                val yy: Int = 2000 + textBody.substring(0, 2).toInt()
                val mm: Int = textBody.substring(2, 4).toInt()
                val dd: Int = textBody.substring(4, 6).toInt()
                if ((mm < 1) || (mm > 12) || (dd < 1) || (dd > 31)) return null //err fmt

                return if (dateAsISO) {
                    "%04d-%02d-%02d".format(yy, mm, dd) //YYYY-MM-DD
                } else {
                    "%02d-%02d-%04d".format(dd, mm, yy) //DD-MM-YYYY
                }
            }

            AI.GS1_DECIMAL -> {
                if (textBody.indexOfFirst { !it.isDigit() } >= 0) return null
                if (ai.ai.last() != 'n') return null

                val decimalPoint = textAi.takeLast(1).toInt()
                val pos = textBody.length - decimalPoint

                return if (pos <= 0) {
                    "0." + "0".repeat(-pos) + textBody
                } else {
                    val sb = StringBuilder(textBody)
                    if (decimalPoint > 0) sb.insert(pos, '.')
                    val d = sb.trimStart('0')
                    if (d.isEmpty() || d.startsWith('.')) "0$d" else d.toString()
                }
            }
        }

        return null
    }

}