package com.ones.assistant.presentation.views.books

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.ones.assistant.R
import com.ones.assistant.presentation.viewmodel.BooksListViewModel
import com.ones.assistant.presentation.views.Routes

@Composable
fun LibraryScreen(
    navController: NavController,
    booksListViewModel: BooksListViewModel = hiltViewModel()
) {
    val uiState by booksListViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        booksListViewModel.loadBooks()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0CFFA))
    ) {
        TopBar(onBackClick = { navController.popBackStack() })

        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = uiState.errorMessage ?: "Failed to load books",
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        Button(
                            onClick = { booksListViewModel.loadBooks() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF7E57C2)
                            )
                        ) {
                            Text(text = "Retry")
                        }
                    }
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    items(uiState.books, key = { it.id }) { book ->
                        BookListCard(
                            book = book,
                            onClick = {
                                navController.navigate(
                                    "${Routes.BookDetailsScreen}/${book.id}"
                                )
                            }
                        )
                    }
                }
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
            text = "LIBRARY",
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
fun BookListCard(
    book: BookDetails,
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
            painter = if (book.coverUrl.isNotBlank()) {
                rememberAsyncImagePainter(model = book.coverUrl)
            } else {
                painterResource(id = R.drawable.dear_to_lead)
            },
            contentDescription = book.title,
            modifier = Modifier
                .width(100.dp)
                .height(130.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = book.title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                maxLines = 2,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "by ${book.author}",
                fontSize = 12.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = book.category.ifBlank { "Unknown category" },
                fontSize = 12.sp,
                color = Color.Gray
            )
        }
    }
}
