package com.myapp.cinemascreen

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.myapp.cinemascreen.ui.screens.LoginViewModel
import com.myapp.cinemascreen.ui.theme.CinemaScreenTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        lifecycleScope.launch {
            viewModel.checkLogin()

            val isLoggedIn = viewModel.isLoginValue()
            val initialRoute = if(isLoggedIn) CinemaScreenDestinations.Main.route else CinemaScreenDestinations.LoginScreen.route

            setContent {
                CinemaScreenTheme {
//                    val isLogin by viewModel.isLogin.collectAsStateWithLifecycle()
//                    val initialRoute = if(isLogin) CinemaScreenDestinations.Main.route else CinemaScreenDestinations.LoginScreen.route
                    // A surface container using the 'background' color from the theme
                    CinemaScreenApp(initialRoute = initialRoute)
                }
            }

        }

    }

}


@Composable
fun CinemaScreenApp(initialRoute: String) {
    val mainNavController = rememberNavController()

    MainNavGraph(navController = mainNavController, initialScreenRoute = initialRoute)
}


@Composable
fun MainScreen(toDetailScreen : (Int, String) -> Unit, toProfileScreen: () -> Unit) {
    val navController = rememberNavController()
    Surface {
        Scaffold(
            bottomBar = { BottomNavBar(navController = navController)},
            contentWindowInsets = WindowInsets(top = 0.dp),
            containerColor = Color.White
        ) {
            Box(modifier = Modifier.padding(it)) {
                BottomNavGraph(navController = navController, toDetailScreen = toDetailScreen, toProfileScreen = toProfileScreen)
            }
        }
    }
}

@Composable
fun BottomNavBar(navController: NavHostController) {
    val screens = listOf(
        BottomBarDestinations.Home,
        BottomBarDestinations.Favorites,
        BottomBarDestinations.Find
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    NavigationBar {
        screens.forEach { screen ->
            AddItem(screen = screen, currentDestination = currentDestination, navController = navController )
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: BottomBarDestinations,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    NavigationBarItem(
        label = { Text(text = screen.title) },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        }==true,
        onClick = {
            navController.navigate(screen.route){
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        },
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            indicatorColor = MaterialTheme.colorScheme.surfaceColorAtElevation(
                LocalAbsoluteTonalElevation.current)
        )
    )
}


//fun getResourceAsText(path: String): String? =
//    object {}.javaClass.getResource(path)?.readText()

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    testGSON(LocalContext.current)
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}

@Preview(showBackground = true)
@Composable
fun MainPreview() {
    CinemaScreenTheme {
        MainScreen({intValue, stringValue -> }, {})
    }
}

