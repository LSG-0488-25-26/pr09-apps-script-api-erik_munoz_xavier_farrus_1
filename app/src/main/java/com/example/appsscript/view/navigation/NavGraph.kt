package com.example.appsscript.view.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.appsscript.model.repository.GamesRepository
import com.example.appsscript.model.repository.SettingsRepository
import com.example.appsscript.view.screens.InsertScreen
import com.example.appsscript.view.screens.LoginScreen
import com.example.appsscript.view.screens.MainScreen
import com.example.appsscript.view.screens.RegisterScreen
import com.example.appsscript.viewmodel.GamesViewModel
import com.example.appsscript.viewmodel.InsertViewModel
import com.example.appsscript.viewmodel.LoginViewModel
import com.example.appsscript.viewmodel.RegisterViewModel

@Composable
fun NavGraph(navController: NavHostController) {
    val context = LocalContext.current
    val settingsRepo = SettingsRepository(context)
    val gamesRepo = GamesRepository()

    NavHost(
        navController = navController,
        startDestination = Routes.Login.route
    ) {
        composable(Routes.Login.route) {
            val vm: LoginViewModel = viewModel(factory = LoginViewModelFactory(settingsRepo))
            LoginScreen(navController, vm)
        }
        composable(Routes.Register.route) {
            val vm: RegisterViewModel = viewModel(factory = RegisterViewModelFactory(settingsRepo))
            RegisterScreen(navController, vm)
        }
        composable(Routes.Main.route) {
            val gamesVm: GamesViewModel = viewModel(
                factory = GamesViewModelFactory(gamesRepo, settingsRepo)
            )
            MainScreen(navController, gamesVm)
        }
        composable(Routes.Insert.route) {
            val insertVm: InsertViewModel = viewModel(factory = InsertViewModelFactory(gamesRepo))
            InsertScreen(navController, insertVm)
        }
    }
}

class LoginViewModelFactory(private val repo: SettingsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return LoginViewModel(repo) as T
    }
}

class RegisterViewModelFactory(private val repo: SettingsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return RegisterViewModel(repo) as T
    }
}

class GamesViewModelFactory(
    private val gamesRepo: GamesRepository,
    private val settingsRepo: SettingsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return GamesViewModel(gamesRepo, settingsRepo) as T
    }
}

class InsertViewModelFactory(private val repo: GamesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        @Suppress("UNCHECKED_CAST")
        return InsertViewModel(repo) as T
    }
}
