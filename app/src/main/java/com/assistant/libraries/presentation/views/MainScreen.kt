package com.assistant.libraries.presentation.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.assistant.libraries.R

class MainScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibraryLighthouseScreen()
        }
    }
}

@Composable
fun LibraryLighthouseScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBarSection()
        SearchSection()
        BannerSection()
        CategorySection()
        BookSection(title = "Books")
        BookSection(title = "Report")
        BookSection(title = "Audio")
        Spacer(modifier = Modifier.weight(1f))
        BottomNavBar()
    }
}

@Composable
fun TopBarSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(id = R.drawable.borrowbook),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Library Lighthouse", fontWeight = FontWeight.Bold, fontSize = 18.sp)
        }
        Row {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Notifications, contentDescription = null)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Default.Person, contentDescription = null)
            }
        }
    }
}

@Composable
fun SearchSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color(0xFFF0F0F0), RoundedCornerShape(50))
            .padding(horizontal = 12.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray)
        Spacer(modifier = Modifier.width(8.dp))
        TextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Search for books...") },
            modifier = Modifier.weight(1f),
            singleLine = true
        )
        Button(onClick = {}, shape = RoundedCornerShape(50)) {
            Text("Search")
        }
    }
}

@Composable
fun BannerSection() {
    Image(
        painter = painterResource(id = R.drawable.book_cover),
        contentDescription = null,
        modifier = Modifier
            .fillMaxWidth()
            .height(150.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun CategorySection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CategoryItem("Books", R.drawable.book_cover)
        CategoryItem("Report", R.drawable.book_cover)
        CategoryItem("Audio", R.drawable.book_cover)
    }
}

@Composable
fun CategoryItem(name: String, icon: Int) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            painter = painterResource(id = icon),
            contentDescription = name,
            modifier = Modifier.size(40.dp)
        )
        Text(name)
    }
}

@Composable
fun BookSection(title: String) {
    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF1A237E))
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(title, color = Color.White, fontWeight = FontWeight.Bold)
            Text("See All", color = Color.White)
        }
        LazyRow(
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
        ) {
            items(6) {
                BookItem()
            }
        }
    }
}

@Composable
fun BookItem() {
    Column(
        modifier = Modifier
            .width(100.dp)
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.book_cover),
            contentDescription = null,
            modifier = Modifier
                .height(140.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Text("Book Title", fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
        Text("Author Name", fontSize = 10.sp, color = Color.Gray, maxLines = 1)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomNavBar() {
    NavigationBar {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, null) },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Search, null) },
            label = { Text("Search") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Search, null) },
            label = { Text("Borrow") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Refresh, null) },
            label = { Text("Return") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Notifications, null) },
            label = { Text("Notification") }
        )
    }
}
