package com.example.matchmate.card

import CardStackController
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.domain.model.User
import kotlinx.coroutines.launch
import rememberCardStackController

@Composable
fun CardStack(
    users: List<User>,
    onSwipedLeft: (User) -> Unit,
    onSwipedRight: (User) -> Unit
) {

    val controller = rememberCardStackController()
    var topIndex by remember(users.size) { mutableIntStateOf(users.lastIndex) }

    Box(modifier = Modifier.fillMaxSize()) {
        users.forEachIndexed { index, user ->
            key(user.id.value ?: index.toString()) {
                val isTopCard = index == topIndex
                val isVisible = index <= topIndex

                if (isVisible) {
                    UserCard(
                        user = user,
                        controller = if (isTopCard) controller else null,
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                translationX = if (isTopCard) controller.offsetX.value else 0f,
                                translationY = if (isTopCard) controller.offsetY.value else 0f,
                                rotationZ = if (isTopCard) controller.rotation.value else 0f
                            )
                            .draggableCard(controller)
                    )
                }
            }
        }

        if (users.isNotEmpty() && topIndex >= 0) {
            val currentUser = users[topIndex]

            controller.onSwipedLeft = {
                onSwipedLeft(currentUser)
                topIndex--
            }

            controller.onSwipedRight = {
                onSwipedRight(currentUser)
                topIndex--
            }
        }
    }
}

@Composable
fun UserCard(
    user: User,
    controller: CardStackController?,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth()
            .wrapContentHeight(),
        shape = RoundedCornerShape(24.dp),
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = user.picture.large,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.7f))
                        )
                    )
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            ) {
                val softWhite = Color(0xFFEEEEEE)

                Column(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(bottom = 100.dp)
                ) {
                    Text(
                        text = "${user.name.first} ${user.name.last}, ${user.dob.age}",
                        color = softWhite,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.SansSerif
                    )
                    Spacer(modifier = Modifier.height(6.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = "Location",
                            tint = softWhite,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = user.location.city,
                            color = softWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = "gender",
                            tint = softWhite,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = user.gender.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() },
                            color = softWhite,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Normal,
                            fontFamily = FontFamily.SansSerif
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .background(Color(0x4DEDEDED), shape = RoundedCornerShape(32.dp))
                        .padding(horizontal = 32.dp, vertical = 8.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(48.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(
                            onClick = { controller?.scope?.launch { controller.swipeLeft() } },
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color(0x99FFFFFF), shape = CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Dislike",
                                tint = Color.Black,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        IconButton(
                            onClick = { controller?.scope?.launch { controller.swipeRight() } },
                            modifier = Modifier
                                .size(48.dp)
                                .background(Color.Red, shape = CircleShape)
                        ) {
                            Icon(
                                imageVector = Icons.Default.FavoriteBorder,
                                contentDescription = "Like",
                                tint = Color.White,
                                modifier = Modifier.size(32.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}