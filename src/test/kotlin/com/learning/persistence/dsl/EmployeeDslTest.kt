package com.learning.persistence.dsl

import com.learning.persistence.dao.db.EmployeeDao
import com.learning.persistence.dsl.db.Dao
import com.learning.persistence.dsl.db.Departments
import com.learning.persistence.dsl.db.EmployeeDaoDsl
import com.learning.persistence.dsl.db.Employees
import com.learning.persistence.dsl.model.Department
import com.learning.persistence.dsl.model.Employee
import org.assertj.core.api.Assertions.assertThat
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EmployeeDslTest {

    companion object {
        val db = Database.connect("jdbc:postgresql://localhost:5432/Test?user=postgres", "org.postgresql.Driver")
        val x = transaction { SchemaUtils.create(Employees,Departments) }
        val technologyDept = Department(10,"Technology")
        val emp = Employee(100,"Gitika","Bansal", technologyDept)
        val accountsDept = Department(10,"Accounts")
        val modifiedEmp = Employee(100,"Gitika","Bansal", accountsDept)
    }

    fun testWithDao(): Stream<Dao<Employee,Int>> {
        return Stream.of(EmployeeDaoDsl(), EmployeeDao())
    }

    @AfterEach
    fun cleanUp(){
        transaction {
            Employees.deleteAll()
            Departments.deleteAll()
        }
    }

    @ParameterizedTest
    @MethodSource("testWithDao")
    fun `should save an employee by inserting in tables employees and departments`(dao : Dao<Employee,Int>){
        dao.save(emp)

        transaction(db) {
            val departmentId = Departments.slice(Departments.id)
                .select { Departments.name eq emp.dept.name }.map { it[Departments.id] }.first()

            val employee = Employees.select { Employees.fname eq emp.fname }
            employee.forEach {
                assertThat(it[Employees.fname]).isEqualTo(emp.fname)
                assertThat(it[Employees.lname]).isEqualTo(emp.lname)
                assertThat(it[Employees.dept]).isEqualTo(departmentId)
            }
        }
    }

    @ParameterizedTest
    @MethodSource("testWithDao")
    fun `should save an employee by inserting in employees table but not in departments table if department already exists`(dao : Dao<Employee,Int>){
        transaction { Departments.insert { it[name] = technologyDept.name } }

        dao.save(emp)

        transaction(db) {
            val departmentId = Departments.slice(Departments.id)
                .select { Departments.name eq emp.dept.name }.map { it[Departments.id] }.first()

            val employee = Employees.select { Employees.fname eq emp.fname }
            employee.forEach {
                assertThat(it[Employees.fname]).isEqualTo(emp.fname)
                assertThat(it[Employees.lname]).isEqualTo(emp.lname)
                assertThat(it[Employees.dept]).isEqualTo(departmentId)
            }
        }

    }

    @ParameterizedTest
    @MethodSource("testWithDao")
    fun `should not insert duplicate employees`(dao : Dao<Employee,Int>){

        transaction { Departments.insert { it[name] = technologyDept.name } }

        dao.save(emp)

        transaction(db) {
            val departmentId = Departments.slice(Departments.id)
                .select { Departments.name eq emp.dept.name }.map { it[Departments.id] }.first()

            val employee = Employees.select { Employees.fname eq emp.fname }
            assertThat(employee.count()).isEqualTo(1)
            employee.forEach {
                assertThat(it[Employees.fname]).isEqualTo(emp.fname)
                assertThat(it[Employees.lname]).isEqualTo(emp.lname)
                assertThat(it[Employees.dept]).isEqualTo(departmentId)
            }
        }

        dao.save(modifiedEmp)

        transaction(db) {
            val departmentId = Departments.slice(Departments.id)
                .select { Departments.name eq modifiedEmp.dept.name }.map { it[Departments.id] }.first()

            val employee = Employees.select { Employees.fname eq modifiedEmp.fname }
            assertThat(employee.count()).isEqualTo(1)
            employee.forEach {
                assertThat(it[Employees.fname]).isEqualTo(modifiedEmp.fname)
                assertThat(it[Employees.lname]).isEqualTo(modifiedEmp.lname)
                assertThat(it[Employees.dept]).isEqualTo(departmentId)
            }
        }

    }
}