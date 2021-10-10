package com.github.sergereinov.libgs1

import org.junit.Assert.*
import org.junit.Test


class FieldsAITest {

    @Test
    fun from() {

        assertEquals("Empty input", false, FieldsAI.from("").hasError)
        assertEquals("Unknown input '1'", true, FieldsAI.from("1").hasError)

        val f3102 = FieldsAI.from("3102001234").list
        assertEquals("f3102 list size", 1, f3102.size)
        assertEquals("f3102 body", "001234", f3102[0].textBody)
        assertEquals("f3102 ai", "310n", f3102[0].ai.ai)
        assertEquals("f3102 formatBody", "12.34", f3102[0].formatBody())

        val f11 = FieldsAI.from("11140823").list[0]
        assertEquals("f11 formatBody()", "23-08-2014", f11.formatBody())
        assertEquals("f11 formatBody(true)", "2014-08-23", f11.formatBody(true))

        val f21 = FieldsAI.from("21654321FEDCBA\u001D").list[0]
        assertEquals("f21 formatBody", "654321FEDCBA", f21.formatBody())

        val ff1 = FieldsAI.from(
            "010061414199999610ABCDEF123456\u001D21654321FEDCBA\u001D310200123411140823"
        )
        assertEquals("ff1 hasError", false, ff1.hasError)
        assertEquals("ff1 size", 5, ff1.list.size)
        assertEquals("ff1[0] body", "00614141999996", ff1.list[0].textBody)
        assertEquals("ff1[1] body", "ABCDEF123456", ff1.list[1].textBody)
        assertEquals("ff1[2] body", "654321FEDCBA", ff1.list[2].textBody)
        assertEquals("ff1[3] body", "001234", ff1.list[3].textBody)
        assertEquals("ff1[4] body", "140823", ff1.list[4].textBody)

        assertEquals("find GTIN", "01", ff1.list.firstOrNull { it.ai.dataTitle == "GTIN" }?.ai?.ai)
        assertEquals("find weight", "12.34", ff1.list.firstOrNull { it.ai.dataTitle.contains("weight", true) }?.formatBody())
    }

}