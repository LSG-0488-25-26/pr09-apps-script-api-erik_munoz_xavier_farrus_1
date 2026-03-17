package com.example.appsscript.view.navigation

sealed class Routes(val route: String) {
    object Login : Routes("login")
    object Register : Routes("register")
    object Main : Routes("main")
    object Insert : Routes("insert")
}
