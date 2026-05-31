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
import coil.compose.rememberAsyncImagePainter
import com.ones.assistant.presentation.views.Routes

@Composable
fun WishListScreen(
    navController: NavController? = null,
    wishListViewModel: WishListViewModel = viewModel(),
    showTopBar: Boolean = true,
    onBookClick: (String) -> Unit = {},
    onPodcastClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))
    ) {
        // TOP BAR
        if (showTopBar) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController?.popBackStack() }) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                }
                Spacer(modifier = Modifier.width(70.dp))
                Text("My WishList", fontSize = 24.sp, fontWeight = FontWeight.SemiBold)
            }
        }

        // LIST OF FAVORITES
        LazyColumn(modifier = Modifier.padding(16.dp)) {
            items(wishListViewModel.favorites) { item: WishlistItem ->
                WishListCard(
                    item = item,
                    onClick = {
                        when (item) {
                            is WishlistItem.Book -> onBookClick(item.id)
                            is WishlistItem.Podcast -> onPodcastClick(item.id)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun WishListCard(item: WishlistItem,
                 onClick: () -> Unit
                 ) {
    Card(
        modifier = Modifier.fillMaxWidth().padding(8.dp),
        shape = RoundedCornerShape(18.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier.background(Color(0xFFE8DFF0)).padding(16.dp)
        ) {
            val painter = when (item) {
                is WishlistItem.Podcast -> painterResource(id = item.imageRes)
                is WishlistItem.Book -> rememberAsyncImagePainter(model = item.coverUrl)
            }

            Image(
                painter = painter,
                contentDescription = item.title,
                modifier = Modifier.width(85.dp).height(135.dp).clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(item.title, fontSize = 22.sp, fontWeight = FontWeight.SemiBold)
                Text("by ${item.author}", fontSize = 16.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = if (item is WishlistItem.Book) "Book Added" else "Added to WishList",
                        fontSize = 18.sp,
                        color = Color.Gray
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = Color.Red)
                }
            }
        }
    }
}
