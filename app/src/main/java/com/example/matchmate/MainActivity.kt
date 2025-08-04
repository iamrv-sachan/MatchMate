package com.example.matchmate

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.matchmate.screens.MatchMateScreens.Home
import com.example.matchmate.screens.MatchMateScreens.ProfileMatches
import com.example.matchmate.screens.MatchMateToggleTopBar
import com.example.matchmate.screens.ProfileMatchesScreen
import com.example.matchmate.screens.UserListScreen
import com.example.matchmate.ui.theme.MatchMateTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MatchMateTheme {
                val navController = rememberNavController()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth(),
                            contentAlignment = Alignment.TopCenter
                        ) {
                            MatchMateToggleTopBar(navController)
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = Home.route,
                        modifier = Modifier.padding(innerPadding).background(Color.Black)
                    ) {
                        composable(Home.route) {
                            UserListScreen()
                        }
                        composable(ProfileMatches.route) {
                            ProfileMatchesScreen()
                        }
                    }
                }
            }
        }
    }
}

