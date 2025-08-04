package com.example.matchmate.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.matchmate.card.CardStack
import com.example.domain.model.User
import com.example.matchmate.viewmodel.UiState
import com.example.matchmate.viewmodel.UserViewModel


@Composable
fun UserListScreen(
    viewModel: UserViewModel = hiltViewModel()
) {
    val isConnected by viewModel.isConnected.collectAsState()

    val homeUsersState by viewModel.homeUsers.collectAsStateWithLifecycle()

    when (homeUsersState) {
        is UiState.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(48.dp)
                )
            }
        }
        is UiState.Success -> {
            val users = (homeUsersState as UiState.Success<List<User>>).data
            if (users.isNotEmpty()) {
                CardStack(
                    users = users,
                    onSwipedLeft = { user ->
                        viewModel.declineMatch(user)
                    },
                    onSwipedRight = { user ->
                        viewModel.acceptMatch(user)
                    }
                )
            } else {
                val message = if(isConnected) "No Profiles Found" else "No Internet"
                EmptyProfilesMessage(message)
            }
            LaunchedEffect(users.size) {
                if (users.size <= 3) {
                    viewModel.fetchAllUsers()
                }
            }
        }
        is UiState.Error -> {
            val message = (homeUsersState as UiState.Error).message
            Text(text = "Error: $message", modifier = Modifier.fillMaxSize())
        }
    }
}

@Composable
fun EmptyProfilesMessage(
    message: String,
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "No Profiles Found",
                tint = Color.Gray,
                modifier = Modifier.size(64.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = message,
                color = Color.Gray,
                fontSize = 18.sp,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Please try again later.",
                color = Color.LightGray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}
