package com.learning.data.generator.db

import com.learning.data.generator.Stock
import org.jetbrains.exposed.sql.batchInsert
import org.jetbrains.exposed.sql.transactions.transaction

class StocksDao{

    fun insertStocks(stocks : List<Stock>){
        transaction {
            Stocks.batchInsert(stocks){ stock ->
                this[Stocks.id] = stock.id
                this[Stocks.name] = stock.name
                this[Stocks.price] = stock.price
                this[Stocks.time] = stock.time }
        }

    }
}








