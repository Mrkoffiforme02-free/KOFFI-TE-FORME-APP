package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.ui.screens.*
import com.example.ui.theme.*
import com.example.ui.viewmodel.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize local Room Database & repository
        val database = AppDatabase.getDatabase(applicationContext)
        val repository = AppRepository(database.appDao())

        // Light weight factory provider inline
        val viewModel: MainViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return MainViewModel(repository) as T
            }
        })[MainViewModel::class.java]

        setContent {
            MyApplicationTheme {
                AppMainScreen(viewModel = viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppMainScreen(viewModel: MainViewModel) {
    val currentTabIndex by viewModel.currentTabIndex.collectAsState()
    val cartCount by viewModel.cartCount.collectAsState()

    // Keep track of the last tab index for back navigation from Cart
    var lastActiveTabIndex by remember { mutableStateOf(0) }

    // When navigating to non-cart index, update lastActiveTabIndex
    LaunchedEffect(currentTabIndex) {
        if (currentTabIndex in 0..4) {
            lastActiveTabIndex = currentTabIndex
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .testTag("app_main_scaffold"),
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "MR KOFFI",
                            color = PureWhite,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 20.sp,
                            letterSpacing = 1.sp
                        )
                    }
                },
                actions = {
                    // Shopping Cart click action badge
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .testTag("top_bar_cart_btn")
                            .clip(CircleShape)
                            .clickable {
                                // Navigate to special screen 5 (Cart)
                                viewModel.navigateToTab(5)
                            }
                            .padding(8.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Panier",
                            tint = if (currentTabIndex == 5) NeonGreen else PureWhite,
                            modifier = Modifier.size(24.dp)
                        )
                        // If items are in the cart, show a custom glowing count indicator badge
                        if (cartCount > 0) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .size(16.dp)
                                    .clip(CircleShape)
                                    .background(NeonGreen),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = cartCount.toString(),
                                    color = Color.Black,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground,
                    titleContentColor = PureWhite
                ),
                modifier = Modifier.border(0.dp, Color.Transparent) // Border stroke-free header
            )
        },
        bottomBar = {
            // Elegant M3 bottom tab navigation
            NavigationBar(
                containerColor = DarkBackground,
                tonalElevation = 12.dp,
                modifier = Modifier
                    .border(
                        BorderStroke(1.dp, BorderColor),
                        RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
                    )
                    .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                    .testTag("app_bottom_nav_bar")
            ) {
                // Tab 0: Home
                NavigationBarItem(
                    selected = currentTabIndex == 0,
                    onClick = { viewModel.navigateToTab(0) },
                    icon = { Icon(imageVector = Icons.Default.Home, contentDescription = "Accueil") },
                    label = { Text("Accueil", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = NeonGreen,
                        unselectedIconColor = SlateGray,
                        unselectedTextColor = SlateGray,
                        indicatorColor = NeonGreen
                    ),
                    modifier = Modifier.testTag("nav_tab_home")
                )

                // Tab 1: Portfolio
                NavigationBarItem(
                    selected = currentTabIndex == 1,
                    onClick = { viewModel.navigateToTab(1) },
                    icon = { Icon(imageVector = Icons.Default.PhotoLibrary, contentDescription = "Portfolio") },
                    label = { Text("Portfolio", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = NeonGreen,
                        unselectedIconColor = SlateGray,
                        unselectedTextColor = SlateGray,
                        indicatorColor = NeonGreen
                    ),
                    modifier = Modifier.testTag("nav_tab_portfolio")
                )

                // Tab 2: Courses
                NavigationBarItem(
                    selected = currentTabIndex == 2,
                    onClick = { viewModel.navigateToTab(2) },
                    icon = { Icon(imageVector = Icons.Default.School, contentDescription = "Formations") },
                    label = { Text("Formations", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = NeonGreen,
                        unselectedIconColor = SlateGray,
                        unselectedTextColor = SlateGray,
                        indicatorColor = NeonGreen
                    ),
                    modifier = Modifier.testTag("nav_tab_courses")
                )

                // Tab 3: About
                NavigationBarItem(
                    selected = currentTabIndex == 3,
                    onClick = { viewModel.navigateToTab(3) },
                    icon = { Icon(imageVector = Icons.Default.Info, contentDescription = "À Propos") },
                    label = { Text("À Propos", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = NeonGreen,
                        unselectedIconColor = SlateGray,
                        unselectedTextColor = SlateGray,
                        indicatorColor = NeonGreen
                    ),
                    modifier = Modifier.testTag("nav_tab_about")
                )

                // Tab 4: Contact/Profile
                NavigationBarItem(
                    selected = currentTabIndex == 4,
                    onClick = { viewModel.navigateToTab(4) },
                    icon = { Icon(imageVector = Icons.Default.ContactMail, contentDescription = "Profil") },
                    label = { Text("Profil", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.Black,
                        selectedTextColor = NeonGreen,
                        unselectedIconColor = SlateGray,
                        unselectedTextColor = SlateGray,
                        indicatorColor = NeonGreen
                    ),
                    modifier = Modifier.testTag("nav_tab_profile")
                )
            }
        }
    ) { innerPadding ->
        // Active panel renderer
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentTabIndex) {
                0 -> HomeScreen(viewModel = viewModel)
                1 -> PortfolioScreen(viewModel = viewModel)
                2 -> CoursesScreen(viewModel = viewModel)
                3 -> AboutScreen(viewModel = viewModel)
                4 -> ProfileContactScreen(viewModel = viewModel)
                5 -> CartScreen(viewModel = viewModel)
                else -> HomeScreen(viewModel = viewModel)
            }
        }
    }
}
