package com.truckhisaab.ui.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Onboarding : Screen("onboarding")
    data object Login : Screen("login")
    data object Otp : Screen("otp/{phone}") {
        fun createRoute(phone: String) = "otp/$phone"
    }
    data object ProfileSetup : Screen("profile_setup")
    data object FirstTruck : Screen("first_truck")

    data object Dashboard : Screen("dashboard")
    data object Notifications : Screen("notifications")

    data object TripList : Screen("trips")
    data object AddTrip : Screen("add_trip")
    data object TripDetail : Screen("trip/{tripId}") {
        fun createRoute(tripId: String) = "trip/$tripId"
    }
    data object ActiveTrip : Screen("active_trip/{tripId}") {
        fun createRoute(tripId: String) = "active_trip/$tripId"
    }

    data object ExpenseList : Screen("expenses")
    data object AddExpense : Screen("add_expense")
    data object ExpenseAnalytics : Screen("expense_analytics")
    data object FuelTracker : Screen("fuel_tracker")

    data object Report : Screen("report")

    data object DocumentList : Screen("documents")
    data object AddDocument : Screen("add_document")
    data object DocumentDetail : Screen("document/{docId}") {
        fun createRoute(docId: String) = "document/$docId"
    }

    data object TruckList : Screen("trucks")
    data object AddTruck : Screen("add_truck")
    data object TruckDetail : Screen("truck/{truckId}") {
        fun createRoute(truckId: String) = "truck/$truckId"
    }

    data object DriverList : Screen("drivers")
    data object AddDriver : Screen("add_driver")
    data object DriverDetail : Screen("driver/{driverId}") {
        fun createRoute(driverId: String) = "driver/$driverId"
    }

    data object Profile : Screen("profile")
    data object Settings : Screen("settings")
    data object Help : Screen("help")
    data object Emergency : Screen("emergency")
}
