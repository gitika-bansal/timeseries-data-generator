package com.learning.persistence.dsl.db

import org.jetbrains.exposed.dao.IntIdTable

object Employees : IntIdTable(){

    val fname = varchar("fname",10)
    val lname = varchar("lname",10)
    val dept = reference("dept", Departments)
}

object Departments : IntIdTable(){
    val name = varchar("name",10)
}