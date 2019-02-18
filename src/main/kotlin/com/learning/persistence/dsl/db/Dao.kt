package com.learning.persistence.dsl.db

import com.learning.persistence.dsl.model.Department
import com.learning.persistence.dsl.model.Employee
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

interface Dao<T,R> {
    fun save(obj : T)
    fun getById(id : R) : T?
    fun <U>getByIdAndMap(id : R, mapper : (T)->U) : U?
}

class EmployeeDaoDsl : Dao<Employee, Int> {

    companion object {
        val resultRowToEmployee = {r : ResultRow ->
            Employee(r[Employees.id].value,r[Employees.fname],r[Employees.lname], resultRowToDept(r))}

        val resultRowToDept = {r: ResultRow -> Department(r[Departments.id].value,r[Departments.name])}

        val resultRowToDeptId = {r: ResultRow -> r[Departments.id]}

        val resultRowToEmpId = { r: ResultRow -> r[Employees.id] }
    }

    override fun save(e: Employee) {
        transaction {
            val deptId = Departments.select { Departments.name eq e.dept.name }.map(resultRowToDeptId).firstOrNull()
                ?: Departments.insertAndGetId { it[name] = e.dept.name }

            val emp = Employees.select {(Employees.fname eq e.fname) and (Employees.lname eq e.lname)}.map(resultRowToEmpId).firstOrNull()
            emp?.let { Employees.update({ Employees.id eq emp }){ it[Employees.dept] = deptId } } ?:
            Employees.insert {
                it[fname] = e.fname
                it[lname] = e.lname
                it[dept] = deptId
            }
        }
    }

    override fun getById(id: Int): Employee? = Employees.innerJoin(Departments).select { Employees.id eq id }.map(resultRowToEmployee).firstOrNull()

    override fun <U> getByIdAndMap(id: Int, mapper: (Employee) -> U): U? = getById(id)?.let{ mapper(it) } ?: null

}