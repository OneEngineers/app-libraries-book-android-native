package com.ones.assistant.ui.widgets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
    onPodcastIconClick: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(15.dp)
    ) {

        CategoryGrid(
            onLibraryClick = onLibraryClick,
            onPodcastIconClick = onPodcastIconClick
        )

        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
fun CategoryGrid(
    onLibraryClick: () -> Unit,
    onPodcastIconClick: () -> Unit
) {

    val items = listOf(
        CategoryData(
            title = "Podcast",
            iconRes = R.drawable.podcasts_24px,
            badge = null
        ),
        CategoryData(
            title = "Library",
            iconRes = R.drawable.library_books_24px,
            badge = null
        )
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.height(70.dp)
    ) {

        items(items) { item ->

            CategoryItem(
                name = item.title,
                icon = painterResource(id = item.iconRes),
                badge = item.badge,

                onClick = {

                    when (item.title) {

                        "Library" -> {
                            onLibraryClick()
                        }

                        "Podcast" -> {
                            onPodcastIconClick()
                        }
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
            .clickable {
                onClick()
            },

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            contentAlignment = Alignment.TopStart
        ) {

            Image(
                painter = icon,
                contentDescription = name,
                modifier = Modifier.size(30.dp),
                contentScale = ContentScale.Crop
            )

            badge?.let {

                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFFEDE7F6),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(
                            horizontal = 6.dp,
                            vertical = 2.dp
                        )
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

        Text(
            text = name,
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

data class CategoryData(
    val title: String,
    val iconRes: Int,
    val badge: String?
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewHomeCategoryScreen() {

    HomeCategoryScreen(
        onLibraryClick = {},
        onPodcastIconClick = {}
    )
}