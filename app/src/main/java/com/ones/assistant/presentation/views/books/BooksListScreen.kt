package com.ones.assistant.presentation.views.books

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
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

    val allBooks = getLibraryBooks()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFEDE7F6))
    ) {

        TopBar(
            onBackClick = { navController.popBackStack() }
        )

        LazyColumn {

            // Trending Books
            item {
                BookSection(
                    title = "Trending Books",
                    books = allBooks,
                    navController = navController
                )
            }

            // Art Genre
            item {
                BookSection(
                    title = "Art",
                    books = allBooks.filter { it.genre == "Art" },
                    navController = navController
                )
            }

            // Crime Genre
            item {
                BookSection(
                    title = "Crime",
                    books = allBooks.filter { it.genre == "Crime" },
                    navController = navController
                )
            }

            // Business Genre
            item {
                BookSection(
                    title = "Business",
                    books = allBooks.filter { it.genre == "Business" },
                    navController = navController
                )
            }

//            // Health Genre
//            item {
//                BookSection(
//                    title = "Health",
//                    books = allBooks.filter { it.genre == "Health" },
//                    navController = navController
//                )
//            }
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

// ===================== BOOK SECTION =====================

@Composable
fun BookSection(
    title: String,
    books: List<BookItem>,
    navController: NavController
) {

    Column(
        modifier = Modifier.padding(vertical = 12.dp)
    ) {

        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            items(books) { book ->

                BookHorizontalCard(book = book) {

                    navController.navigate(
                        "${Routes.BookDetailsScreen}/${book.id}"
                    )
                }
            }
        }
    }
}

// ===================== BOOK CARD =====================

@Composable
fun BookHorizontalCard(
    book: BookItem,
    onClick: () -> Unit
) {

    val buttonText = when (book.id) {
        "1" -> "Read"
        "2" -> "Preview Only"
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
            contentPadding = PaddingValues(vertical = 4.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF7E57C2)
            )
        ) {

            Text(
                text = buttonText,
                fontSize = 12.sp
            )
        }
    }
}

// DATA MODEL

data class BookItem(
    val id: String,
    val title: String,
    val author: String,
    val genre: String,
    val coverRes: Int
)

// BOOK DATA

fun getLibraryBooks(): List<BookItem> {

    return listOf(

        // ART
        BookItem(
            id = "1",
            title = "The Octopus",
            author = "Jonathan Levi",
            genre = "Art",
            coverRes = R.drawable.a1
        ),
        BookItem(
            id = "1",
            title = "Gods Comics",
            author = "Jonathan Levi",
            genre = "Art",
            coverRes = R.drawable.a2
        ),
        BookItem(
            id = "1",
            title = "When You're Brave Enough",
            author = "Jonathan Levi",
            genre = "Art",
            coverRes = R.drawable.a3
        ),
        BookItem(
            id = "1",
            title = "Me(m)Diewistyka",
            author = "Jonathan Levi",
            genre = "Art",
            coverRes = R.drawable.a4
        ),
//        BookItem(
//            id = "1",
//            title = "The Only Skill that Matters",
//            author = "Jonathan Levi",
//            genre = "Art",
//            coverRes = R.drawable.a5
//        ),
        // BUSINESS
        BookItem(
            id = "2",
            title = "The Let Them",
            author = "Bjarne Stroustrup",
            genre = "Business",
            coverRes = R.drawable.b1
        ),
        BookItem(
            id = "2",
            title = "The 5 Types of wealth",
            author = "Bjarne Stroustrup",
            genre = "Business",
            coverRes = R.drawable.b5
        ),

        // HEALTH (BOOK 3)
        BookItem(
            id = "3",
            title = "A Violent Masterpiece",
            author = "James Gosling",
            genre = "Crime",
            coverRes = R.drawable.c2
        ),

        // CRIME
        BookItem(
            id = "4",
            title = "Murder at the Hotel Orient",
            author = "Annaleise Byrd",
            genre = "Crime",
            coverRes = R.drawable.c1
        ),
        BookItem(
            id = "4",
            title = "The Secrets of the Abbey",
            author = "Annaleise Byrd",
            genre = "Crime",
            coverRes = R.drawable.c3
        ),
        BookItem(
            id = "4",
            title = "Blood Trail",
            author = "Annaleise Byrd",
            genre = "Crime",
            coverRes = R.drawable.c4
        ),
        BookItem(
            id = "4",
            title = "The Nuremberg Women",
            author = "Annaleise Byrd",
            genre = "Crime",
            coverRes = R.drawable.c5
        ),
        // BUSINESS
        BookItem(
            id = "5",
            title = "Empire of AI",
            author = "Stephen Covey",
            genre = "Business",
            coverRes = R.drawable.b4
        ),

        // BUSINESS
        BookItem(
            id = "6",
            title = "Abundance",
            author = "James Gosling",
            genre = "Business",
            coverRes = R.drawable.b2
        ),

        // BUSINESS
        BookItem(
            id = "7",
            title = "Hidden Potentail",
            author = "Nadine Greiner",
            genre = "Business",
            coverRes = R.drawable.b3
        )
    )
}