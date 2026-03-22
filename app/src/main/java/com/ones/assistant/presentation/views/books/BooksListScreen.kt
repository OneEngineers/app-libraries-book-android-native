package com.ones.assistant.presentation.views.books

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.clickable
import androidx.compose.material3.*
import androidx.compose.material3.Text
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
import com.ones.assistant.R
import androidx.navigation.NavController




@Composable
fun LibraryScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0CFFA))
    ) {
        TopBar(onBackClick = {
            navController.popBackStack() // go back
        })
        LazyColumn(
            modifier = Modifier.padding(horizontal = 26.dp, vertical = 8.dp)
        ) {
            items(getLibraryBooks()) { book ->
                BookCard(book)
            }
        }
    }
}

@Composable
fun TopBar(onBackClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color(0xFFFFFFFF))
    ) {
        // Back icon on the left
        Icon(
            painter = painterResource(id = R.drawable.back), // replace with your back icon
            contentDescription = "Back",
            modifier = Modifier
                .align(Alignment.CenterStart)
                .padding(start = 16.dp)
                .size(21.dp)
                .clickable { onBackClick() },
            tint = Color.Black
        )

        // Centered semibold text
        Text(
            text = "LIBRARY",
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.Black,
            modifier = Modifier.align(Alignment.Center)
        )

        // Logo on the right
        Icon(
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = "Logo",
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 16.dp)
                .size(36.dp),
            tint = Color(0xFFE91E63)
        )
    }
}


@Composable
fun BookCard(book: BookItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .background(Color.White, shape = RoundedCornerShape(12.dp))
            .padding(12.dp)
    ) {
        Image(
            painter = painterResource(id = book.coverRes),
            contentDescription = book.title,
            modifier = Modifier
                .width(100.dp)
                .height(140.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.width(14.dp))
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(book.title, fontWeight = FontWeight.Bold, fontSize = 10.sp)
            Text("by ${book.author}", fontSize = 12.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "Available",
                fontSize = 11.sp,
                color = Color.White,
                modifier = Modifier
                    .background(Color(0xFF9C27B0), shape = RoundedCornerShape(16.dp))
                    .padding(horizontal = 20.dp, vertical = 0.5.dp)
            )
        }
    }
}

data class BookItem(val title: String, val author: String, val coverRes: Int)

fun getLibraryBooks(): List<BookItem> {
    return listOf(
        BookItem("The Only Skill that Matters", "Jonathan Levi", R.drawable.only_skill_book),
        BookItem("C++ Programming Language", "Bjarne Stroustrup", R.drawable.cplusplus_book),
        BookItem("Coaching", "James Gosling", R.drawable.healthy_book),
        BookItem("Losing the Plot", "Annaleise Byrd", R.drawable.losing_the_plot_book),
        BookItem("The 7 Habits Highly Effective People", "Stephen Covey", R.drawable.habits_book),
        BookItem("Java Programming Language", "James Gosling", R.drawable.java_book),
        BookItem("The Executive Coaching Playbook", "Nadine Greiner, Ph.D. and Becky Davis, MA", R.drawable.coaching_book)

    )
}
