package com.ones.assistant.presentation.views.books

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
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

// ===================== MAIN SCREEN =====================

@Composable
fun LibraryScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDE7F6))
    ) {
        TopBar(
            onBackClick = { navController.popBackStack() }
        )

        LazyColumn {
            item {
                BookSection(
                    title = "Trending Books",
                    books = getLibraryBooks(),
                    navController = navController
                )
            }

            item {
                BookSection(
                    title = "Classic Books",
                    books = getLibraryBooks(),
                    navController = navController
                )
            }

            item {
                BookSection(
                    title = "Books We Love",
                    books = getLibraryBooks(),
                    navController = navController
                )
            }
        }
    }
}

// ===================== TOP BAR =====================

@Composable
fun TopBar(onBackClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.White)
    ) {
        Icon(
            painter = painterResource(id = R.drawable.back),
            contentDescription = "Back",
            tint = Color.Black,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .size(20.dp)
                .clickable { onBackClick() }
        )

        Text(
            text = "LIBRARY",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.align(Alignment.Center)
        )

        Icon(
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = "Logo",
            tint = Color(0xFFE91E63),
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .size(32.dp)
        )
    }
}

// ===================== SECTION =====================

@Composable
fun BookSection(
    title: String,
    books: List<BookItem>,
    navController: NavController
) {
    Column(modifier = Modifier.padding(vertical = 12.dp)) {

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(books) { book ->
                BookHorizontalCard(book = book) {
                    navController.navigate("${Routes.BookDetailsScreen}/${book.id}")
                }
            }
        }
    }
}

// BOOK CARD

@Composable
fun BookHorizontalCard(
    book: BookItem,
    onClick: () -> Unit
) {
    val buttonText = when (book.id) {
        "1" -> "Join Waitlist"
        "2" -> "Checked Out"
        "3" -> "Preview Only"
        "4" -> "Read"
        else -> "Borrow"
    }

    Column(
        modifier = Modifier
            .width(120.dp)
            .clickable { onClick() }
    ) {
        Image(
            painter = painterResource(id = book.coverRes),
            contentDescription = book.title,
            modifier = Modifier
                .height(170.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(6.dp))

        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(vertical = 4.dp)
        ) {
            Text(
                text = buttonText,
                fontSize = 12.sp
            )
        }
    }
}

// ===================== DATA MODEL =====================

data class BookItem(
    val id: String,
    val title: String,
    val author: String,
    val coverRes: Int
)

// ===================== SAMPLE DATA =====================

fun getLibraryBooks(): List<BookItem> {
    return listOf(
        BookItem("1", "The Only Skill that Matters", "Jonathan Levi", R.drawable.only_skill_book),
        BookItem("2", "C++ Programming Language", "Bjarne Stroustrup", R.drawable.cplusplus_book),
        BookItem("3", "Coaching", "James Gosling", R.drawable.healthy_book),
        BookItem("4", "Losing the Plot", "Annaleise Byrd", R.drawable.losing_the_plot_book),
        BookItem("5", "7 Habits", "Stephen Covey", R.drawable.habits_book),
        BookItem("6", "Java Programming", "James Gosling", R.drawable.java_book),
        BookItem("7", "Executive Coaching", "Nadine Greiner", R.drawable.coaching_book)
    )
}