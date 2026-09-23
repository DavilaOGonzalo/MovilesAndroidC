package com.gonzalo.hospitaltecsup_mio.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gonzalo.hospitaltecsup_mio.screens.ConfirmationScreen
import com.gonzalo.hospitaltecsup_mio.screens.DoctorProfileScreen
import com.gonzalo.hospitaltecsup_mio.screens.HomeScreen
import com.gonzalo.hospitaltecsup_mio.screens.ScheduleAppointmentScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController)
        }

        composable(
            route = Screen.DoctorProfile.route,
            arguments = listOf(
                navArgument(name = "doctorId") {
                    type = NavType.IntType
                },
            ),
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            DoctorProfileScreen(navController, doctorId)
        }

        composable(
            route = Screen.ScheduleAppointment.route,
            arguments = listOf(
                navArgument(name = "doctorId") {
                    type = NavType.IntType
                },
            ),
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            ScheduleAppointmentScreen(navController, doctorId)
        }

        composable(
            route = Screen.Confirmation.route,
            arguments = listOf(
                navArgument(name = "doctorId") {
                    type = NavType.IntType
                },
                navArgument(name = "date") {
                    type = NavType.StringType
                },
                navArgument(name = "time") {
                    type = NavType.StringType
                },
            ),
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val time = backStackEntry.arguments?.getString("time") ?: ""
            ConfirmationScreen(navController, doctorId, date, time)
        }
    }
}