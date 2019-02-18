package com.learning.data.generator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ValidationTests{

    @Test
    fun `should parse valid input`(){
        val input = arrayOf("100.0","5","1000","100")
        val dataConfig = parseInput(input)
        assertThat(dataConfig).isEqualTo(DataConfig(100.0, 5.0, 1000, 100))
    }

    @Test
    fun `should fail parsing invalid doubles in input`(){

    }

    @Test
    fun `should fail parsing invalid integers in input`(){

    }
}