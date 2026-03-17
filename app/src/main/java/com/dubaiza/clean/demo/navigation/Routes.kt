package com.dubaiza.clean.demo.navigation


sealed class Routes(val route: String) {

    data object Splash : Routes("splash")
    data object EmployeeList : Routes("employee_list")
    data object EmployeeListScreen : Routes("employee_list_screen")
    data object AddEmployee : Routes("add_employee")
    data object AddEmployee2 : Routes("add_employee_2")

    // ✅ With argument
    data object EditEmployee : Routes("edit_employee/{employeeId}") {
        fun createRoute(employeeId: Int) = "edit_employee/$employeeId"
    }
}