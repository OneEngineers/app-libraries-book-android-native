package com.ones.assistant.presentation.views.podcast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ones.assistant.R
import com.ones.assistant.presentation.views.feature.PodcastItem
import com.ones.assistant.presentation.views.feature.WishListViewModel

@Composable
fun PodcastDetailScreen(
    podcastId: String,
    onBackClick: () -> Unit = {},
    wishListViewModel: WishListViewModel = viewModel()
) {
    // Example podcast object (normally you’d fetch by ID)
    val podcast = PodcastItem(
        id = podcastId,
        title = "15 Minute English",
        author = "Podcast Host",
        imageRes = R.drawable.dear_to_lead
    )

    Column {
        // TOP BAR
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                modifier = Modifier.clickable { onBackClick() }
            )

            Spacer(modifier = Modifier.weight(1f))

            Text("Podcast: ${podcast.title}", fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.weight(1f))

            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "Favorite",
                modifier = Modifier.clickable {
                    wishListViewModel.addFavorite(podcast)
                }
            )
        }

        // PODCAST INFO
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.size(180.dp),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Image(
                    painter = painterResource(id = podcast.imageRes),
                    contentDescription = "Podcast Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(podcast.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("by ${podcast.author}", color = Color.Magenta, fontSize = 13.sp)

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = { /* TODO: play podcast */ },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB000F5)),
                modifier = Modifier.width(160.dp).height(48.dp)
            ) {
                Text("Play", color = Color.White)
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "IELTS grammar requires a blend of accuracy and range, focusing on using simple and complex sentences.",
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // EPISODES SECTION
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
            color = Color(0xFFD1C4E9)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Episodes", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Divider()
                Spacer(modifier = Modifier.height(12.dp))

                LazyColumn {
                    items(10) {
                        EpisodeItem()
                    }
                }
            }
        }
    }
}

@Composable
fun EpisodeItem() {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "IELTS grammar requires a blend of accuracy and range, focusing on using simple and complex sentences.",
            fontSize = 13.sp
        )
        Spacer(modifier = Modifier.height(8.dp))
        Divider()
    }
}
