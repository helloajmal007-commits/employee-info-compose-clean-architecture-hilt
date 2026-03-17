package com.dubaiza.clean.demo.domain.usecase

import com.dubaiza.clean.demo.domain.model.Employee
import com.dubaiza.clean.demo.domain.repository.EmployeeRepository
import com.dubaiza.clean.demo.feature.employee2.EmployeeStatus

class InsertEmployeesUsecase (private val repository: EmployeeRepository) {

    suspend operator fun invoke(){
        val sampleList = listOf(
            Employee(   0,"Jhon", "Driver", "Development", EmployeeStatus.ALL, "123456", "abc@gmail.com"),
            Employee(   1, "Jhon", "Driver", "Development", EmployeeStatus.ALL, "123456", "abc@gmail.com"),
            Employee(   3,"Jhon", "Driver", "Development", EmployeeStatus.ALL, "123456", "abc@gmail.com"),
        )

        repository.inserAlltEmployee(sampleList)
    }
}