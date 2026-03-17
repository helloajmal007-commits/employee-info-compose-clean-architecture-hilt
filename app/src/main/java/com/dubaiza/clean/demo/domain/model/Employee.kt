package com.dubaiza.clean.demo.domain.model

import com.dubaiza.clean.demo.feature.employee2.EmployeeStatus

data class Employee (
    val id: Int = 0,
    val name: String,
    val designation: String,
    val department: String,
    val status: EmployeeStatus,
    val phone: String,
    val email: String
)