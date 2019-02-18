package com.learning.data.generator

import com.learning.data.generator.db.StocksDao
import org.jetbrains.exposed.sql.Database
import org.joda.time.DateTime
import java.util.*

fun main(args: Array<String>) {

    val dataConfig = parseInput(args)
    Database.connect(
        Config.dbConnUrl,
        Config.dbDriver
    )
    val stocksDao = StocksDao()
    generateData(
        dataConfig, generateSequence(dataConfig)
    ).chunked(100).forEach {
        stocksDao.insertStocks(it)
    }

}

fun parseInput(args: Array<String>) : DataConfig {
    val mean = args[0].toDouble()
    val stdDeviation = args[1].toDouble()
    val interval = args[2].toInt()
    val count = args[3].toInt()
    return DataConfig(mean, stdDeviation, interval, count)
}

fun generateSequence(dataConfig: DataConfig) = fun() : Sequence<Double>{
    return IntRange(1,dataConfig.count)
        .map{
            dataConfig.meanValue + (Math.random() * dataConfig.stdDeviation * Math.pow(-1.0,Math.ceil(Math.random()*100)))}
        .map(Math::ceil).asSequence()
}

fun generateData(dataConfig: DataConfig, sequenceGenerator : () -> Sequence<Double>) : Sequence<Stock> {
    var time = DateTime.now()
    return sequenceGenerator().mapIndexed { index, price->
        time = time.withDurationAdded(dataConfig.intervalMs.toLong(),Calendar.MILLISECOND)
        Stock(index, "MRF", price, time)
    }.asSequence()
}


