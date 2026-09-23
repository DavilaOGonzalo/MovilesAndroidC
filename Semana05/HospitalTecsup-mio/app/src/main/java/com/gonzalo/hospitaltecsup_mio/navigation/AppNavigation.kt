package com.gonzalo.hospitaltecsup_mio.navigation

import android.net.Uri
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
                navArgument(name = "name") {
                    type = NavType.StringType
                },
                navArgument(name = "specialty") {
                    type = NavType.StringType
                },
                navArgument(name = "rating") {
                    type = NavType.FloatType
                },
            ),
        ) { backStackEntry ->
            val doctorId = backStackEntry.arguments?.getInt("doctorId") ?: 0
            val name = backStackEntry.arguments
                ?.getString("name")
                ?.let { Uri.decode(it) }
                .orEmpty()
            val specialty = backStackEntry.arguments
                ?.getString("specialty")
                ?.let { Uri.decode(it) }
                .orEmpty()
            val rating = backStackEntry.arguments?.getFloat("rating") ?: 0f
            DoctorProfileScreen(navController, doctorId, name, specialty, rating)
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