package com.learning.data.generator

import com.learning.data.generator.db.Stocks
import com.learning.data.generator.db.StocksDao
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteAll
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CRUDOperationTests{

    companion object {
        val db = Database.connect("jdbc:postgresql://localhost:5432/Test?user=postgres", "org.postgresql.Driver")
        val dao = StocksDao()
        fun assertStock(stock : Stock) = fun(resultRow : ResultRow) {
            assertThat(resultRow[Stocks.name]).isEqualTo(stock.name)
            assertThat(resultRow[Stocks.price]).isEqualTo(stock.price)
            assertThat(resultRow[Stocks.time]).isEqualTo(stock.time)
        }
    }

    @AfterEach
    fun cleanUp(){
        transaction { Stocks.deleteAll() }
    }

    @Test
    fun `should bulk insert data in table`() {
        val stock1 = Stock(1, "MRF", 100.0, DateTime.now())
        val stock2 = Stock(2, "MRF", 100.0, DateTime.now())
        val stocks = listOf(stock1,stock2)
        dao.insertStocks(stocks)
        transaction {
            Stocks.select { Stocks.id inList  stocks.map { it.id } }.forEachIndexed { index, resultRow -> assertStock(
                stocks[index]
            )(resultRow)}
        }
    }

}