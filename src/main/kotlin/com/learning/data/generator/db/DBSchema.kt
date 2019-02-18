package com.learning.data.generator.db

import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table

object Stocks : Table() {
    val id: Column<Int> = integer("id").primaryKey()
    val name = varchar("name", 50).index()
    val price = double("price")
    val time = datetime("update_ts")
}