package com.example.makepayment.android

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.makepayment.android.Screens.HomeScreen
import com.example.makepayment.android.Screens.Profile
import com.example.makepayment.android.Screens.Service
import com.example.makepayment.android.ui.theme.MakePaymentTheme


class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val name = intent.getStringExtra("userName")
            val phoneNumber = intent.getStringExtra("phoneNumber")
            MakePaymentTheme {
                val navController = rememberNavController()
                BottomBar(navController = navController, name = name, phoneNumber = phoneNumber)
            }
        }
    }
}

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Profile : Screen("profile")
    object Settings : Screen("settings")
}

data class BottomNavigationItem(
    val title: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val badgeCount: Int? = null,
    val screen: Screen
)


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBar(navController: NavController, name: String?, phoneNumber: String?) {
    val selectedItem = remember { mutableStateOf(Screen.Home as Screen) }

    val bottomNavigationItems = listOf(
        BottomNavigationItem(
            title = "Home",
            selectedIcon = Icons.Filled.Home,
            unselectedIcon = Icons.Outlined.Home,
            screen = Screen.Home
        ),
        BottomNavigationItem(
            title = "Chat",
            selectedIcon = Icons.Filled.Email,
            unselectedIcon = Icons.Outlined.Email,
            screen = Screen.Profile
        ),
        BottomNavigationItem(
            title = "Settings",
            selectedIcon = Icons.Filled.Settings,
            unselectedIcon = Icons.Outlined.Settings,
            screen = Screen.Settings
        )
    )
    Scaffold(
        bottomBar = {
            NavigationBar {
                bottomNavigationItems.forEachIndexed { index, item ->
                    NavigationBarItem(
                        selected = selectedItem.value == item.screen,
                        onClick = {
                            selectedItem.value = item.screen
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.startDestinationId)
                            }
                        },
                        label = {
                            Text(text = item.title)
                        },
                        alwaysShowLabel = false,
                        icon = {
                            BadgedBox(
                                badge = {
                                    item.badgeCount?.let {
                                        Badge {
                                            Text(text = it.toString())
                                        }
                                    }
                                }
                            ) {
                                Icon(
                                    imageVector = if (selectedItem.value == item.screen) {
                                        item.selectedIcon
                                    } else item.unselectedIcon,
                                    contentDescription = item.title
                                )
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            NavHost(navController = navController as NavHostController, startDestination = Screen.Home.route) {
                composable(Screen.Home.route) {
                    if (name != null && phoneNumber != null) {
                        HomeScreen(name = name, phoneNo = phoneNumber)
                    }
                }
                composable(Screen.Profile.route) {
                    if (phoneNumber != null) {
                        Profile(phoneNo = phoneNumber)
                    }
                }
                composable(Screen.Settings.route) {
                    Service()
                }
            }
        }
    }
}
