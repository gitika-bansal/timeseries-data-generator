package com.learning.persistence.dsl.model

data class Employee(var id : Int, val fname : String, val lname : String, val dept : Department)

data class Department(var id: Int,val name : String)

data class EmployeeDetails(val empId : Int, val fname : String, val lname : String, val dept : String)