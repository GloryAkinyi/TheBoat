package com.wekesamabwi.theboat.navigation

import TradingScreen
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.wekesamabwi.theboat.ui.theme.screens.about.AboutScreen
import com.wekesamabwi.theboat.viewmodel.AuthViewModel
import com.wekesamabwi.theboat.ui.theme.screens.alertdetailsscreen.AlertDetailsScreen
import com.wekesamabwi.theboat.data.UserDatabase
import com.wekesamabwi.theboat.repository.UserRepository
import com.wekesamabwi.theboat.ui.theme.screens.aiscreens.AIScreen
import com.wekesamabwi.theboat.ui.theme.screens.auth.LoginScreen
import com.wekesamabwi.theboat.ui.theme.screens.auth.RegisterScreen
import com.wekesamabwi.theboat.ui.theme.screens.converter.ConverterScreen
import com.wekesamabwi.theboat.ui.theme.screens.currency.CurrencyChartScreen
import com.wekesamabwi.theboat.ui.theme.screens.dashboard.DashboardScreen
import com.wekesamabwi.theboat.ui.theme.screens.home.HomeScreen
import com.wekesamabwi.theboat.ui.theme.screens.marketscreen.MarketScreen
import com.wekesamabwi.theboat.ui.theme.screens.notificationscreen.NotificationScreen
import com.wekesamabwi.theboat.ui.theme.screens.splash.SplashScreen


@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    startDestination: String = ROUT_DASHBOARD
) {
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(ROUT_HOME) {
            HomeScreen(navController)
        }

        composable(ROUT_ABOUT) {
            AboutScreen(navController)
        }

        composable(ROUT_ALERT_DETAILS) {
            AlertDetailsScreen(navController, alertId = "")
        }

        composable(ROUT_MARKET) {
            MarketScreen(navController)
        }

        composable(ROUT_TRADE) {
            TradingScreen(navController)
        }

        composable(ROUT_DASHBOARD) {
            DashboardScreen(navController)
        }

        composable(ROUT_SPLASH) {
            SplashScreen(navController)
        }

        composable(ROUT_CONVERTER) {
            ConverterScreen(navController)
        }

        composable(ROUT_NOTIFICATION) {
            NotificationScreen(navController)
        }

        composable(ROUT_AIAssistantScreen) {
            AIScreen(navController)
        }


        composable(ROUT_CURRENCYCHART) {
            CurrencyChartScreen(navController)
        }









        composable("${ROUT_ALERT_DETAILS}/{id}") { backStackEntry ->
            val alertId = backStackEntry.arguments?.getString("id") ?: ""
            AlertDetailsScreen(navController, alertId)
        }


        //Auth

        // Initialize Room Database and Repository for Authentication
        val appDatabase = UserDatabase.getDatabase(context)
        val authRepository = UserRepository(appDatabase.userDao())
        val authViewModel: AuthViewModel = AuthViewModel(authRepository) // Direct instantiation

        composable(ROUT_REGISTER) {
            RegisterScreen(authViewModel, navController) {  // ✅ Fixed parameter order
                navController.navigate(ROUT_LOGIN) {
                    popUpTo(ROUT_REGISTER) { inclusive = true } // ✅ Prevent back navigation to Register
                }
            }
        }

        composable(ROUT_LOGIN) {
            LoginScreen(authViewModel,navController) {
                navController.navigate(ROUT_HOME) {
                    popUpTo(ROUT_LOGIN) { inclusive = true } // ✅ Prevent going back to login
                }
            }
        }









    }
}
