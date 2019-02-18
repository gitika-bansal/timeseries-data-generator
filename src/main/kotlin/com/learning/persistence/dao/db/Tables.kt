package com.learning.persistence.dao.db

import com.learning.persistence.dsl.db.Departments
import com.learning.persistence.dsl.db.Employees
import org.jetbrains.exposed.dao.EntityID
import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass


class PEmployee(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PEmployee>(Employees)

    var fname by Employees.fname
    var lname     by Employees.lname
    var dept by PDepartment referencedOn Employees.dept
}

class PDepartment(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<PDepartment>(Departments)
    var name by Departments.name
}