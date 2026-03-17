package com.dubaiza.clean.demo.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.dubaiza.clean.demo.feature.employee.presentation.EmployeeViewModel
import com.dubaiza.clean.demo.feature.employee.presentation.screens.AddEmployeeScreen
import com.dubaiza.clean.demo.feature.employee.presentation.EmployeeScreen
import com.dubaiza.clean.demo.feature.employee.presentation.EmployeeViewModel2
import com.dubaiza.clean.demo.feature.employee.presentation.screens.AddEmployeeScreen2
import com.dubaiza.clean.demo.feature.employee.presentation.screens.EditEmployeeScreen
import com.dubaiza.clean.demo.feature.employee2.EmployeeListScreen
import com.dubaiza.clean.demo.feature.splash.presentation.SplashScreen

@Composable
fun AppNavGraph(viewModel: EmployeeViewModel2) {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route,
        enterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Left,
                animationSpec = tween(300)
            )
        },
        popEnterTransition = {
            slideIntoContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        },
        popExitTransition = {
            slideOutOfContainer(
                AnimatedContentTransitionScope.SlideDirection.Right,
                animationSpec = tween(300)
            )
        }
    ){

        // 🔥 Splash Screen
        composable(Routes.Splash.route) {
            SplashScreen(
                onNavigate = {
                    navController.navigate(Routes.EmployeeListScreen.route) {
//                    navController.navigate(Routes.EmployeeList.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // Employee List Screen
        composable(Routes.EmployeeList.route) {
            EmployeeScreen(viewModel = viewModel, onAddEmployee = {
                    navController.navigate(Routes.AddEmployee.route)
                }, onEditEmployee = { employee ->
                    viewModel.setSelectedEmployee(employee)
                    navController.navigate(Routes.EditEmployee.createRoute(employee.id))
                })
        }

        // Employee List Screen
        composable(Routes.EmployeeListScreen.route) {
            EmployeeListScreen(
                viewModel = viewModel,
                onAddEmployee = {
                    navController.navigate(Routes.AddEmployee2.route)
                }, onEditEmployee = { employee ->

                    viewModel.setSelectedEmployee(employee)
                    navController.navigate(Routes.EditEmployee.createRoute(employee.id))
                },
                onDeleteEmployee = { employee ->

                },

            )
        }

        // Add Employee Screen
        composable(Routes.AddEmployee.route) {
            AddEmployeeScreen(
                onSave = { employee ->
                    viewModel.addEmployee(employee)
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        // Add Employee Screen new
        composable(Routes.AddEmployee2.route) {
            AddEmployeeScreen2 (
                onSave = { employee ->
                    viewModel.addEmployee(employee)
                    navController.popBackStack()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = Routes.EditEmployee.route,
            arguments = listOf(
                navArgument("employeeId") { type = NavType.IntType }
            ),
            deepLinks = listOf(
                navDeepLink {
                    uriPattern = "https://dubaiza.com/employee/{employeeId}"
                }
            )
        ) { backStackEntry ->

            val employeeId = backStackEntry.arguments?.getInt("employeeId") ?: 0

            EditEmployeeScreen(
                viewModel,
//                employeeID = employeeId,
                onSave = {navController.popBackStack()},
                onBack = { navController.popBackStack() }
            )
        }
    }
}