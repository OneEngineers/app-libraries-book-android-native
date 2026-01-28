package com.ones.assistant.presentation.views.books

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.ones.assistant.presentation.viewmodel.BookDetailsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BookDetailsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                BookDetailsScreen(
                    bookId = "book_123",
                    onBackClick = { finish() },
                    onBorrowClick = { /* Handle borrow */ },
                    onWishlistClick = { /* Handle wishlist */ }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    bookId: String,
    viewModel: BookDetailsViewModel = hiltViewModel(),
    onBackClick: () -> Unit = {},
    onBorrowClick: () -> Unit = {},
    onWishlistClick: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    var isInWishlist by remember { mutableStateOf(false) }
    var isBorrowed by remember { mutableStateOf(false) }
    
    // Load book details when screen is displayed
    LaunchedEffect(bookId) {
        viewModel.loadBookDetail(bookId)
    }
    
    val book = uiState.book
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Book Details",
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { isInWishlist = !isInWishlist }) {
                        Icon(
                            if (isInWishlist) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Add to Wishlist",
                            tint = if (isInWishlist) Color.Red else Color.Gray
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            uiState.errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = uiState.errorMessage ?: "Unknown error",
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadBookDetail(bookId) }) {
                            Text("Retry")
                        }
                    }
                }
            }
            book == null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Book not found", color = Color.Gray)
                }
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF4F4F4))
                        .padding(innerPadding)
                ) {
            // Book Cover and Basic Info
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        if (book.coverUrl.isNotEmpty()) {
                            AsyncImage(
                                model = book.coverUrl,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(200.dp)
                                    .clip(RoundedCornerShape(8.dp)),
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        Text(
                            text = book.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        Text(
                            text = "by ${book.author}",
                            fontSize = 18.sp,
                            color = Color.Gray,
                            textAlign = TextAlign.Center
                        )
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        // Rating
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFFFFD700),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${book.rating} (${book.totalRatings} reviews)",
                                fontSize = 14.sp,
                                color = Color.Gray
                            )
                        }
                    }
                }
            }
            
            // Book Information
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Book Information",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        BookInfoRow("ISBN", book.isbn)
                        BookInfoRow("Published", book.publishedYear)
                        BookInfoRow("Pages", if (book.pages > 0) book.pages.toString() else "N/A")
                        BookInfoRow("Language", book.language.ifEmpty { "N/A" })
                        BookInfoRow("Category", book.category.ifEmpty { "N/A" })
                        if (book.rating > 0f) {
                            BookInfoRow("Rating", "${book.rating} (${book.totalRatings} reviews)")
                        }
                    }
                }
            }
            
            // Description
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Description",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1A237E),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                        
                        Text(
                            text = book.description,
                            fontSize = 14.sp,
                            color = Color.Black,
                            lineHeight = 20.sp
                        )
                    }
                }
            }
            
            // Action Buttons
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Button(
                        onClick = {
                            isBorrowed = !isBorrowed
                            onBorrowClick()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isBorrowed) Color.Gray else Color(0xFF1A237E)
                        ),
                        enabled = !isBorrowed
                    ) {
                        Icon(
                            if (isBorrowed) Icons.Default.Check else Icons.Default.Book,
                            contentDescription = null,
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isBorrowed) "Borrowed" else "Borrow Book",
                            color = Color.White
                        )
                    }
                    
                    OutlinedButton(
                        onClick = {
                            isInWishlist = !isInWishlist
                            onWishlistClick()
                        },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = if (isInWishlist) Color.Red else Color(0xFF1A237E)
                        )
                    ) {
                        Icon(
                            if (isInWishlist) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = null
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isInWishlist) "In Wishlist" else "Add to Wishlist"
                        )
                    }
                }
            }
            
            item { Spacer(modifier = Modifier.height(16.dp)) }
                }
            }
        }
    }
}

@Composable
fun BookInfoRow(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = label,
            fontSize = 14.sp,
            color = Color.Gray,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = value,
            fontSize = 14.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f)
        )
    }
}

data class BookDetails(
    val id: String,
    val title: String,
    val author: String,
    val description: String,
    val isbn: String,
    val publishedYear: String,
    val pages: Int,
    val language: String,
    val category: String,
    val rating: Float,
    val totalRatings: Int,
    val availableCopies: Int,
    val totalCopies: Int,
    val coverRes: Int = 0,
    val coverUrl: String = String()
)

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BookDetailsScreenPreview() {
    MaterialTheme {
        BookDetailsScreen(
            bookId = "book_123",
            onBackClick = {},
            onBorrowClick = {},
            onWishlistClick = {}
        )
    }
}
