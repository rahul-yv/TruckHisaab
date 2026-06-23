package com.truckhisaab.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalShipping
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.truckhisaab.ui.screens.dashboard.DashboardScreen
import com.truckhisaab.ui.screens.documents.AddDocumentScreen
import com.truckhisaab.ui.screens.documents.DocumentDetailScreen
import com.truckhisaab.ui.screens.documents.DocumentListScreen
import com.truckhisaab.ui.screens.driver.AddDriverScreen
import com.truckhisaab.ui.screens.driver.DriverDetailScreen
import com.truckhisaab.ui.screens.driver.DriverListScreen
import com.truckhisaab.ui.screens.expense.AddExpenseScreen
import com.truckhisaab.ui.screens.expense.ExpenseAnalyticsScreen
import com.truckhisaab.ui.screens.expense.ExpenseListScreen
import com.truckhisaab.ui.screens.other.EmergencyScreen
import com.truckhisaab.ui.screens.other.FuelTrackerScreen
import com.truckhisaab.ui.screens.other.HelpScreen
import com.truckhisaab.ui.screens.other.NotificationsScreen
import com.truckhisaab.ui.screens.profile.ProfileScreen
import com.truckhisaab.ui.screens.report.ReportScreen
import com.truckhisaab.ui.screens.settings.SettingsScreen
import com.truckhisaab.ui.screens.splash.SplashScreen
import com.truckhisaab.ui.screens.onboarding.OnboardingScreen
import com.truckhisaab.ui.screens.auth.LoginScreen
import com.truckhisaab.ui.screens.auth.OtpScreen
import com.truckhisaab.ui.screens.trip.ActiveTripScreen
import com.truckhisaab.ui.screens.trip.AddTripScreen
import com.truckhisaab.ui.screens.trip.TripDetailScreen
import com.truckhisaab.ui.screens.trip.TripListScreen
import com.truckhisaab.ui.screens.truck.AddTruckScreen
import com.truckhisaab.ui.screens.truck.TruckDetailScreen
import com.truckhisaab.ui.screens.truck.TruckListScreen
import com.truckhisaab.ui.theme.TruckRed

data class BottomNavItem(val label: String, val icon: ImageVector, val route: String)

val bottomNavItems = listOf(
    BottomNavItem("Home", Icons.Default.Home, Screen.Dashboard.route),
    BottomNavItem("Trips", Icons.Default.LocalShipping, Screen.TripList.route),
    BottomNavItem("Hisaab", Icons.Default.Assessment, Screen.Report.route),
    BottomNavItem("Profile", Icons.Default.Person, Screen.Profile.route)
)

private val bottomNavRoutes = bottomNavItems.map { it.route }.toSet()

@Composable
fun TruckHisaabNavGraph() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val showBottomBar = currentRoute in bottomNavRoutes

    Scaffold(
        bottomBar = {
            if (showBottomBar) {
                NavigationBar(containerColor = Color.White, tonalElevation = 8.dp) {
                    bottomNavItems.forEach { item ->
                        val selected = navBackStackEntry?.destination?.hierarchy?.any { it.route == item.route } == true
                        NavigationBarItem(
                            icon = { Icon(item.icon, item.label) },
                            label = { Text(item.label, fontSize = 11.sp, fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal) },
                            selected = selected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            colors = NavigationBarItemDefaults.colors(selectedIconColor = TruckRed, selectedTextColor = TruckRed, unselectedIconColor = Color.Gray, unselectedTextColor = Color.Gray, indicatorColor = TruckRed.copy(0.1f))
                        )
                    }
                }
            }
        },
        floatingActionButton = {
            if (currentRoute == Screen.Dashboard.route) {
                FloatingActionButton(onClick = { navController.navigate(Screen.AddTrip.route) }, containerColor = TruckRed, contentColor = Color.White) {
                    Icon(Icons.Default.Add, "Quick Add")
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Splash.route,
            modifier = Modifier.padding(innerPadding),
            enterTransition = { fadeIn(tween(200)) },
            exitTransition = { fadeOut(tween(200)) }
        ) {
            // Onboarding
            composable(Screen.Splash.route) {
                SplashScreen(onNavigate = {
                    navController.navigate(Screen.Onboarding.route) { popUpTo(Screen.Splash.route) { inclusive = true } }
                })
            }
            composable(Screen.Onboarding.route) {
                OnboardingScreen(onFinish = {
                    navController.navigate(Screen.Login.route) { popUpTo(Screen.Onboarding.route) { inclusive = true } }
                })
            }
            composable(Screen.Login.route) {
                LoginScreen(
                    onOtpSent = { phone -> navController.navigate(Screen.Otp.createRoute(phone)) },
                    onSkip = { navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Login.route) { inclusive = true } } }
                )
            }
            composable(Screen.Otp.route) { entry ->
                val phone = entry.arguments?.getString("phone") ?: ""
                OtpScreen(phone = phone, onVerified = {
                    navController.navigate(Screen.Dashboard.route) { popUpTo(Screen.Login.route) { inclusive = true } }
                }, onBack = { navController.popBackStack() })
            }

            // Dashboard
            composable(Screen.Dashboard.route) {
                DashboardScreen(
                    onNavigateToTrip = { navController.navigate(Screen.TripList.route) },
                    onNavigateToExpense = { navController.navigate(Screen.ExpenseList.route) },
                    onNavigateToReport = { navController.navigate(Screen.Report.route) },
                    onNavigateToDocuments = { navController.navigate(Screen.DocumentList.route) },
                    onNavigateToNotifications = { navController.navigate(Screen.Notifications.route) },
                    onNavigateToSettings = { navController.navigate(Screen.Settings.route) },
                    onNavigateToTripDetail = { navController.navigate(Screen.TripDetail.createRoute(it)) },
                    onNavigateToActiveTrip = { navController.navigate(Screen.ActiveTrip.createRoute(it)) },
                    onNavigateToToll = { navController.navigate(Screen.ExpenseList.route) },
                    onNavigateToRepair = { navController.navigate(Screen.ExpenseList.route) },
                    onNavigateToFuel = { navController.navigate(Screen.FuelTracker.route) }
                )
            }

            // Trips
            composable(Screen.TripList.route) {
                TripListScreen(
                    onBack = { navController.popBackStack() },
                    onAddTrip = { navController.navigate(Screen.AddTrip.route) },
                    onTripDetail = { navController.navigate(Screen.TripDetail.createRoute(it)) },
                    onActiveTrip = { navController.navigate(Screen.ActiveTrip.createRoute(it)) }
                )
            }
            composable(Screen.AddTrip.route) {
                AddTripScreen(onBack = { navController.popBackStack() }, onDone = { navController.popBackStack() })
            }
            composable(Screen.TripDetail.route) { entry ->
                val tripId = entry.arguments?.getString("tripId") ?: ""
                TripDetailScreen(tripId = tripId, onBack = { navController.popBackStack() })
            }
            composable(Screen.ActiveTrip.route) { entry ->
                val tripId = entry.arguments?.getString("tripId") ?: ""
                ActiveTripScreen(
                    tripId = tripId,
                    onBack = { navController.popBackStack() },
                    onAddExpense = { navController.navigate(Screen.AddExpense.route) },
                    onComplete = { navController.popBackStack() }
                )
            }

            // Expenses
            composable(Screen.ExpenseList.route) {
                ExpenseListScreen(
                    onBack = { navController.popBackStack() },
                    onAddExpense = { navController.navigate(Screen.AddExpense.route) },
                    onAnalytics = { navController.navigate(Screen.ExpenseAnalytics.route) }
                )
            }
            composable(Screen.AddExpense.route) {
                AddExpenseScreen(onBack = { navController.popBackStack() }, onDone = { navController.popBackStack() })
            }
            composable(Screen.ExpenseAnalytics.route) {
                ExpenseAnalyticsScreen(onBack = { navController.popBackStack() })
            }
            composable(Screen.FuelTracker.route) {
                FuelTrackerScreen(onBack = { navController.popBackStack() })
            }

            // Report
            composable(Screen.Report.route) {
                ReportScreen(onBack = { navController.popBackStack() })
            }

            // Documents
            composable(Screen.DocumentList.route) {
                DocumentListScreen(
                    onBack = { navController.popBackStack() },
                    onAddDoc = { navController.navigate(Screen.AddDocument.route) },
                    onDocDetail = { navController.navigate(Screen.DocumentDetail.createRoute(it)) }
                )
            }
            composable(Screen.AddDocument.route) {
                AddDocumentScreen(onBack = { navController.popBackStack() }, onDone = { navController.popBackStack() })
            }
            composable(Screen.DocumentDetail.route) { entry ->
                val docId = entry.arguments?.getString("docId") ?: ""
                DocumentDetailScreen(docId = docId, onBack = { navController.popBackStack() })
            }

            // Trucks
            composable(Screen.TruckList.route) {
                TruckListScreen(
                    onBack = { navController.popBackStack() },
                    onAddTruck = { navController.navigate(Screen.AddTruck.route) },
                    onTruckDetail = { navController.navigate(Screen.TruckDetail.createRoute(it)) }
                )
            }
            composable(Screen.AddTruck.route) {
                AddTruckScreen(onBack = { navController.popBackStack() }, onDone = { navController.popBackStack() })
            }
            composable(Screen.TruckDetail.route) { entry ->
                val truckId = entry.arguments?.getString("truckId") ?: ""
                TruckDetailScreen(truckId = truckId, onBack = { navController.popBackStack() })
            }

            // Drivers
            composable(Screen.DriverList.route) {
                DriverListScreen(
                    onBack = { navController.popBackStack() },
                    onAddDriver = { navController.navigate(Screen.AddDriver.route) },
                    onDriverDetail = { navController.navigate(Screen.DriverDetail.createRoute(it)) }
                )
            }
            composable(Screen.AddDriver.route) {
                AddDriverScreen(onBack = { navController.popBackStack() }, onDone = { navController.popBackStack() })
            }
            composable(Screen.DriverDetail.route) { entry ->
                val driverId = entry.arguments?.getString("driverId") ?: ""
                DriverDetailScreen(driverId = driverId, onBack = { navController.popBackStack() })
            }

            // Profile
            composable(Screen.Profile.route) {
                ProfileScreen(
                    onBack = { navController.popBackStack() },
                    onSettings = { navController.navigate(Screen.Settings.route) },
                    onNotifications = { navController.navigate(Screen.Notifications.route) },
                    onHelp = { navController.navigate(Screen.Help.route) },
                    onEmergency = { navController.navigate(Screen.Emergency.route) },
                    onLogout = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
                        }
                    }
                )
            }

            // Other
            composable(Screen.Settings.route) { SettingsScreen(onBack = { navController.popBackStack() }) }
            composable(Screen.Notifications.route) { NotificationsScreen(onBack = { navController.popBackStack() }) }
            composable(Screen.Help.route) { HelpScreen(onBack = { navController.popBackStack() }) }
            composable(Screen.Emergency.route) { EmergencyScreen(onBack = { navController.popBackStack() }) }
        }
    }
}
