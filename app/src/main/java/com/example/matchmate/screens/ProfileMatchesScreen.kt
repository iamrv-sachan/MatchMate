package com.example.matchmate.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.example.domain.model.User
import com.example.matchmate.viewmodel.UiState.Error
import com.example.matchmate.viewmodel.UiState.Loading
import com.example.matchmate.viewmodel.UiState.Success
import com.example.matchmate.viewmodel.UserViewModel
import kotlinx.coroutines.flow.Flow

@Composable
fun ProfileMatchesScreen(
    viewModel: UserViewModel = hiltViewModel()
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.refreshProfileMatches()
    }
    val profileMatchesState by viewModel.profileMatches.collectAsState()

    when (profileMatchesState) {
        is Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }

        is Success -> {
            val pagingFlow = (profileMatchesState as Success<Flow<PagingData<User>>>).data
            val lazyPagingItems = pagingFlow.collectAsLazyPagingItems()
            if (lazyPagingItems.itemCount != 0) {
                ProfileGrid(lazyPagingItems)
            } else {
                val message = "No Matches"
                EmptyProfilesMessage(message)
            }
        }

        is Error -> {
            val message = (profileMatchesState as Error).message
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "Error: $message", color = Color.Red)
            }
        }
    }
}

@Composable
fun ProfileGrid(users: LazyPagingItems<User>) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(8.dp)
    ) {
        items(users.itemCount) { index ->
            val user = users[index]
            if (user != null) {
                ProfileCard(user = user)
            }
        }
    }
}

@Composable
fun ProfileCard(user: User) {
    val primaryTextColor = Color.White
    val secondaryTextColor = Color.White.copy(alpha = 0.8f)
    val iconTint = Color.White.copy(alpha = 0.9f)
    val iconSize = 18.dp

    val nameAgeTextStyle = MaterialTheme.typography.titleMedium.copy(
        color = primaryTextColor,
        fontSize = 22.sp,
        fontWeight = FontWeight.Bold
    )

    val detailTextStyle = MaterialTheme.typography.bodySmall.copy(
        color = secondaryTextColor,
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal
    )

    Box(
        modifier = Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(Color.Black)
            .fillMaxWidth()
            .aspectRatio(0.6f)
    ) {
        AsyncImage(
            model = user.picture.large,
            contentDescription = "User profile picture of ${user.name.first}",
            contentScale = ContentScale.Crop,
            modifier = Modifier.matchParentSize()
        )

        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .fillMaxWidth()
                .height(140.dp)
                .background(
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f),
                            Color.Black.copy(alpha = 0.95f)
                        )
                    )
                )
        )

        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(10.dp)
                .background(
                    color = if (user.isDeclined) Color.White.copy(alpha = 0.9f) else Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(50)
                )
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = if (user.isDeclined) "Declined" else "Accepted",
                style = MaterialTheme.typography.labelSmall,
                color = if (user.isDeclined) Color.Black else Color.White
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(start = 12.dp, end = 12.dp, bottom = 12.dp)
        ) {
            Text(
                text = "${user.name.first}, ${user.dob.age}",
                style = nameAgeTextStyle
            )
            Spacer(modifier = Modifier.height(4.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.LocationOn,
                    contentDescription = "Location",
                    tint = iconTint,
                    modifier = Modifier.size(iconSize)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = user.location.city,
                    style = detailTextStyle
                )
            }
            Spacer(modifier = Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Call,
                    contentDescription = "Phone Number",
                    tint = iconTint,
                    modifier = Modifier.size(iconSize)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = user.phone,
                    style = detailTextStyle
                )
            }
            Spacer(modifier = Modifier.height(2.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email Address",
                    tint = iconTint,
                    modifier = Modifier.size(iconSize)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = user.email,
                    style = detailTextStyle,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}