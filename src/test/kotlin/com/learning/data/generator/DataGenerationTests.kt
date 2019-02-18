package com.learning.data.generator

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class DataGenerationTests{

    @Test
    fun `should generate sequence`(){
        val config = DataConfig(100.0,5.0,1,30)
        val sequence = generateSequence(config)()
        assertThat(sequence.count()).isEqualTo(config.count)
    }

    @Test
    fun `should generate data using sequence`(){
        val sequence = sequenceOf(23.5,27.89,11.5,33.8,50.0,20.0)
        val stocks = generateData(
            DataConfig(
                1.0,
                1.0,
                1,
                1
            )
        ) { sequence }
        assertThat(stocks.count()).isEqualTo(sequence.count())
        stocks.forEach {
            assertThat(it.name).isEqualTo("MRF")
            assertThat(it.price).isIn(sequence.asIterable())
            assertThat(it.time).isNotNull()
        }
    }
}