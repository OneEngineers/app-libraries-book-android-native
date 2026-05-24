package com.ones.assistant.presentation.views.podcast

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import com.ones.assistant.R
import com.ones.assistant.presentation.views.Routes


@Composable
fun PodcastScreen(navController: NavController) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0CFFA))
    ) {

        TopBar(
            onBackClick = { navController.popBackStack() }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            items(getPodcasts()) { podcast ->

                PodcastCard(
                    podcast = podcast,
                    onClick = {
                        navController.navigate(
                            "${Routes.PodcastDetailScreen}/${podcast.title}"
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun TopBar(onBackClick: () -> Unit = {}) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .background(Color.White)
    ) {

        Icon(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .size(24.dp)
                .clickable { onBackClick() }
        )

        Text(
            text = "PODCAST",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.Center),
            color = Color.Black
        )

        Image(
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .size(32.dp)
        )
    }
}

@Composable
fun PodcastCard(
    podcast: PodcastItem,
    onClick: () -> Unit
) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .clickable { onClick() }
            .padding(12.dp)
    ) {

        Image(
            painter = painterResource(id = podcast.coverRes),
            contentDescription = podcast.title,
            modifier = Modifier
                .width(160.dp)
                .height(140.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {

            Text(
                text = podcast.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 2
            )

            Text(
                text = "by ${podcast.creator}",
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}



data class PodcastItem(
    val title: String,
    val creator: String,
    val coverRes: Int
)



fun getPodcasts(): List<PodcastItem> {
    return listOf(
        PodcastItem("Chhaya Talk", "Education", R.drawable.dear_to_lead),
        PodcastItem("The 7 Habits Highly Effective People", "Stephen Covey", R.drawable.dear_to_lead),
        PodcastItem("Losing the Plot", "Annaleise Byrd", R.drawable.for_the_record),
        PodcastItem("The Only Skill that Matters", "Jonathan Levi", R.drawable.the321),
        PodcastItem("Losing the Plot", "Annaleise Byrd", R.drawable.strategies),
        PodcastItem("The Only Skill that Matters", "Jonathan Levi", R.drawable.dear_to_lead),
        PodcastItem("The Executive Coaching Playbook", "Nadine Greiner & Becky Davis", R.drawable.for_the_record)
    )
}