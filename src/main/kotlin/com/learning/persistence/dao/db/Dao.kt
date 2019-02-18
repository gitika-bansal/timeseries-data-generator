package com.learning.persistence.dao.db

import com.learning.persistence.dsl.db.Dao
import com.learning.persistence.dsl.db.Departments
import com.learning.persistence.dsl.db.Employees
import com.learning.persistence.dsl.model.Department
import com.learning.persistence.dsl.model.Employee
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.transactions.transaction

class EmployeeDao : Dao<Employee, Int> {

    override fun save(obj: Employee) {
        transaction {
            val deptt = PDepartment.find { Departments.name eq obj.dept.name }.firstOrNull()?.let { it }
                ?: PDepartment.new { name = obj.dept.name }
            PEmployee.find { (Employees.fname eq obj.fname) and (Employees.lname eq obj.lname) }.firstOrNull()
                ?.let {
                    it.dept = deptt
                }
                ?: PEmployee.new {
                    fname = obj.fname
                    lname = obj.lname
                    dept = deptt
                }
        }
    }

    override fun getById(id: Int): Employee? = PEmployee.findById(id)?.let { Employee(it.id.value,it.fname,it.lname,
        Department(it.dept.id.value,it.dept.name)) }

    override fun <U> getByIdAndMap(id: Int, mapper: (Employee) -> U): U? = getById(id)?.let { mapper(it) } ?: null

}