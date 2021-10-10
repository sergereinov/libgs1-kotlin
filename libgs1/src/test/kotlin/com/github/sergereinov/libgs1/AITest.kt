package com.github.sergereinov.libgs1

import org.junit.Assert.*
import org.junit.Test

class AITest {

    @Test
    fun compareToSequence() {
        assertEquals("compare 00 to 01", -1, AI.all[0].compareToSequence("01"))
        assertEquals("compare 01 to 01", 0, AI.all[1].compareToSequence("01"))
        assertEquals("compare 02 to 01", 1, AI.all[2].compareToSequence("01"))
        assertEquals("compare 310n to 3102",0, AI.all.first { it.ai == "310n" }.compareToSequence("3102"))
    }

    @Test
    fun from() {
        assertNull("Empty input", AI.from(""))
        assertNull("Unknown input '1'", AI.from("1"))
        assertNull("Unknown input 'abcdef'", AI.from("abcdef"))
        assertNull("Unknown AI 03", AI.from("03"))
        assertEquals("AI from 01","01", AI.from("01")?.ai)
        assertEquals("AI from 3102", "310n", AI.from("3102")?.ai)
    }
}
