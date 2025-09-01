package com.assistant.libraries.presentation.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.assistant.libraries.R

class MainScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibraryLighthouseScreen()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryLighthouseScreen() {
    var selectedIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = "App Logo",
                            modifier = Modifier.size(40.dp).padding(end = 8.dp)
                        )
                        Text(
                            text = "Library Lighthouse",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0D1B2A)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* TODO: Profile action */ }) {
                        Icon(Icons.Default.AccountCircle, contentDescription = "Profile")
                    }
                }
            )
        },
        bottomBar = {
            BottomNavBar(selectedIndex = selectedIndex, onItemSelected = { selectedIndex = it })
        }
    ) { innerPadding ->
        when (selectedIndex) {
            0 -> HomeScreen(modifier = Modifier.padding(innerPadding))
            1 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Search Screen") }
            2 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Borrow Screen") }
            3 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Return Screen") }
            4 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Notifications") }
        }
    }
}

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item { BannerSection() }
        item { CategorySection() }
        item { BookSection(title = "Books") }
        item { BookSection(title = "Report") }
        item { BookSection(title = "Audio") }
        item { Spacer(modifier = Modifier.height(16.dp)) }
    }
}



@Composable
fun BannerSection() {
    Image(
        painter = painterResource(id = R.drawable.images),
        contentDescription = null,
        modifier = Modifier.fillMaxWidth().height(150.dp),
        contentScale = ContentScale.Crop
    )
}

@Composable
fun CategorySection() {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
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
            contentDescription = null,
            tint = Color.Unspecified,
            modifier = Modifier.size(32.dp)
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
        LazyRow(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)) {
            items(6) { BookItem() }
        }
    }
}

@Composable
fun BookItem() {
    Column(
        modifier = Modifier.width(100.dp).padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.book_cover),
            contentDescription = null,
            modifier = Modifier.height(140.dp).fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
        Text("Book Title", fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
        Text("Author Name", fontSize = 10.sp, color = Color.Gray, maxLines = 1)
    }
}

@Composable
fun BottomNavBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    val items = listOf("Home", "Search", "Borrow", "Return", "Notification")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.Search,
        Icons.Default.Bookmark,
        Icons.Default.Refresh,
        Icons.Default.Notifications
    )

    NavigationBar {
        items.forEachIndexed { index, label ->
            NavigationBarItem(
                selected = selectedIndex == index,
                onClick = { onItemSelected(index) },
                icon = { Icon(icons[index], contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLibraryLighthouse() {
    LibraryLighthouseScreen()
}
