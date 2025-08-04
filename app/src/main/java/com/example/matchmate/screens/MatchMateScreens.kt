package com.example.matchmate.screens

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.ui.graphics.vector.ImageVector

sealed class MatchMateScreens(val icon: ImageVector, val route: String, val title: String) {
    data object Home : MatchMateScreens(Icons.Default.Home,"home", "Discover")
    data object ProfileMatches : MatchMateScreens(Icons.Default.FavoriteBorder,"profile_matches", "Matches")
}