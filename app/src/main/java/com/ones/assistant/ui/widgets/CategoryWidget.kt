package com.ones.assistant.ui.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ones.assistant.R


@Composable
fun HomeCategoryScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // 🔹 Grid of categories
        CategoryGrid()

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 Bottom row with Payment & Points
        BottomRow()
    }
}

@Composable
fun CategoryGrid() {
    val items = listOf(
        CategoryData("Transport", R.drawable.podcasts_24px, "Promo"),
        CategoryData("Podcast", R.drawable.podcasts_24px, null),
        CategoryData("Library", R.drawable.library_books_24px, null),
//        CategoryData("Lab Panel", R.drawable.lab_panel_24px, null),
        CategoryData("Movie", R.drawable.movie_icon, null),
//        CategoryData("Nham24", R.drawable.podcasts_24px, "Fave"),
//        CategoryData("Ride later", R.drawable.podcasts_24px, null),
        CategoryData("All", R.drawable.read_more_30dp, null),
    )

    LazyVerticalGrid(


        columns = GridCells.Fixed(4),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.height(160.dp)
    ) {
        items(items) { item ->
            CategoryItem(
                name = item.title,
                icon = painterResource(id = item.iconRes),
                badge = item.badge
            )
        }
    }
}

@Composable
fun CategoryItem(
    name: String,
    icon: Painter,
    badge: String? = null,
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            //.background(Color(0xFFF5FAF8)) // light background like screenshot
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.TopStart) {
            Image(
                painter = icon,
                contentDescription = name,
                modifier = Modifier
                    .size(30.dp)
                    .align(Alignment.Center),
                contentScale = ContentScale.Crop
            )

            badge?.let {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFFF7A00), RoundedCornerShape(12.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(
                        text = it,
                        fontSize = 10.sp,
                        color = Color.White
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun BottomRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        BottomCard(
            title = "Wallet",
            subtitle = "Add a card",
            iconRes = R.drawable.wallet_24px,
            modifier = Modifier.weight(1f)
        )

        BottomCard(
            title = "Points",
            subtitle = "0",
            iconRes = R.drawable.paid_24px,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun BottomCard(title: String, subtitle: String, iconRes: Int, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .padding(12.dp)
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 12.sp, color = Color.Gray)
            Text(subtitle, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        }
        Spacer(modifier = Modifier.width(8.dp))
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            modifier = Modifier.size(24.dp)
        )
    }
}

data class CategoryData(val title: String, val iconRes: Int, val badge: String?)
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHomeCategoryScreen() {
    HomeCategoryScreen()
}