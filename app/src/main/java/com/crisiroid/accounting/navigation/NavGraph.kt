import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.crisiroid.accounting.pages.AddTransactionScreen
import com.crisiroid.accounting.pages.CategoriesScreen
import com.crisiroid.accounting.pages.HistoryScreen
import com.crisiroid.accounting.pages.HomeScreen
import com.crisiroid.accounting.pages.LoginScreen
import com.crisiroid.accounting.pages.RegisterScreen
import com.crisiroid.accounting.pages.SplashScreen


@Composable
fun NavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Routes.Splash.route,
        modifier = modifier
    ) {
        composable(Routes.Splash.route) {
            SplashScreen(
                onLoginRequired = {
                    navController.navigate(Routes.Login.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                },
                onHome = {
                    navController.navigate(Routes.Home.route) {
                        popUpTo(Routes.Splash.route) { inclusive = true }
                    }
                }
            )
        }



        composable(Routes.Home.route) {
            HomeScreen(navController)
        }
        composable(Routes.Categories.route) {
            CategoriesScreen(navController)
        }
        composable(Routes.Transaction.route) {
            AddTransactionScreen(navController)
        }
        composable(Routes.History.route) {
            HistoryScreen(navController)
        }
        composable(Routes.Login.route) {
            LoginScreen(navController = navController)
        }
        composable(Routes.Register.route) {
            RegisterScreen(navController = navController)
        }


    }
}