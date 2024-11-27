import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.tertiaryprogramreccommender.R

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val currentRoute = navController.currentDestination?.route
    val items = listOf(
        NavigationItem.Home,
        NavigationItem.Insights,
        NavigationItem.Account
    )

    NavigationBar(
        containerColor = Color(0xFF2962FF),
        modifier = Modifier.height(85.dp) // Adjust height here
    ) {
        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    // Use ImageVector or painterResource based on availability
                    if (item.icon != null) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label,
                            tint = if (currentRoute == item.route) Color.Yellow else Color.White // Change tint based on current route
                        )
                    } else if (item.painterResourceId != null) {
                        Icon(
                            painter = painterResource(id = item.painterResourceId),
                            contentDescription = item.label,
                            tint = if (currentRoute == item.route) Color.Yellow else Color.White // Change tint based on current route
                        )
                    }
                },
                label = {
                    Text(
                        text = item.label,
                        color = if (currentRoute == item.route) Color.Yellow else Color.White // Change text color based on current route
                    )
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        // Avoid multiple copies of the same destination in the back stack
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                // Set the colors to make the background transparent
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = Color.Yellow,
                    unselectedIconColor = Color.White,
                    selectedTextColor = Color.Yellow,
                    unselectedTextColor = Color.White,
                    indicatorColor = Color.Transparent // Set the indicator color to transparent
                )
            )
        }
    }
}

sealed class NavigationItem(val route: String, val icon: ImageVector? = null, val painterResourceId: Int? = null, val label: String) {
    object Home : NavigationItem("Dashboard", icon = Icons.Filled.Home, label = "Dashboard")
    object Insights : NavigationItem("RecommendationActivity", painterResourceId = R.drawable.recommendations, label = "Recommendations")
    object Account : NavigationItem("EditAccountActivity", painterResourceId = R.drawable.person, label = "My Account")
}
