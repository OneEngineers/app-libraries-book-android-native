package com.ones.assistant.ui.widgets

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
fun HomeCategoryScreen(
    onLibraryClick: () -> Unit = {},
    onPodcastClick: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        CategoryGrid(
            onLibraryClick = onLibraryClick,
            onPodcastClick = onPodcastClick
        )
        Spacer(modifier = Modifier.height(18.dp))
    }
}

@Composable
fun CategoryGrid(
    onLibraryClick: () -> Unit,
    onPodcastClick: () -> Unit
) {
    val items = listOf(
        CategoryData("Podcast", R.drawable.podcasts_24px, null),
        CategoryData("Library", R.drawable.library_books_24px, null)
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.height(100.dp)
    ) {
        items(items) { item ->
            CategoryItem(
                name = item.title,
                icon = painterResource(id = item.iconRes),
                badge = item.badge,
                onClick = {
                    when (item.title) {
                        "Library" -> onLibraryClick()
                        "Podcast" -> onPodcastClick()
                    }
                }
            )
        }
    }
}

@Composable
fun CategoryItem(
    name: String,
    icon: Painter,
    badge: String? = null,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF4F4F4))
            .padding(8.dp)
            .fillMaxWidth()
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.TopStart) {
            Image(
                painter = icon,
                contentDescription = name,
                modifier = Modifier.size(30.dp),
                contentScale = ContentScale.Crop
            )
            badge?.let {
                Box(
                    modifier = Modifier
                        .background(Color(0xFFEDE7F6), RoundedCornerShape(12.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                        .align(Alignment.TopStart)
                ) {
                    Text(text = it, fontSize = 10.sp, color = Color.White)
                }
            }
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(name, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

data class CategoryData(val title: String, val iconRes: Int, val badge: String?)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHomeCategoryScreen() {
    HomeCategoryScreen()
}
