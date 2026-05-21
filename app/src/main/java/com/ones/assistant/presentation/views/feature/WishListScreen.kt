package com.ones.assistant.presentation.views.feature

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun WishListScreen(
    navController: NavController,
    wishListViewModel: WishListViewModel = viewModel()
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))
    ) {
        // TOP BAR
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Spacer(modifier = Modifier.width(70.dp))
            Text("My WishList", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
        }

        // LIST OF FAVORITES
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(wishListViewModel.favorites) { podcast: PodcastItem ->
                WishListCard(podcast)
            }
        }
    }
}

@Composable
fun WishListCard(podcast: PodcastItem) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(18.dp)
    ) {
        Row(
            modifier = Modifier.background(Color(0xFFE8DFF0)).padding(16.dp)
        ) {
            Image(
                painter = painterResource(id = podcast.imageRes),
                contentDescription = podcast.title,
                modifier = Modifier.width(85.dp).height(135.dp).clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(podcast.title, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                Text("by ${podcast.author}", fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Added to WishList", fontSize = 18.sp, color = Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = Color.Red)
                }
            }
        }
    }
}
