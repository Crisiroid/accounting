sealed class Routes(val route: String) {

    object Home : Routes("home")
    object Categories : Routes("categories")
    object History : Routes("history")
    object Login : Routes("login")
    object Register : Routes("register")
    object Splash : Routes("splash")
    object Transaction : Routes("transaction")

}