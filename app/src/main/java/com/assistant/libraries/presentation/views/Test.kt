package com.assistant.libraries.presentation.views

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.assistant.libraries.R

class Test : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibraryApp()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LibraryApp() {
    var selectedIndex by remember { mutableStateOf(2) } // Borrow as default

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.app_logo),
                            contentDescription = "App Logo",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 8.dp)
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
                    IconButton(onClick = { /* Profile click */ }) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile"
                        )
                    }
                }
            )
        },
        bottomBar = {
            NavigationBar {
                val items = listOf("Home", "Search", "Borrow", "Return", "Notification")
                val icons = listOf(
                    Icons.Default.Home,
                    Icons.Default.Search,
                    painterResource(id = R.drawable.bookicon), // custom icon
                    painterResource(id = R.drawable.borrowbook), // custom icon
                    Icons.Default.Notifications
                )

                items.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = selectedIndex == index,
                        onClick = { selectedIndex = index },
                        icon = {
                            if (icons[index] is androidx.compose.ui.graphics.vector.ImageVector) {
                                Icon(
                                    imageVector = icons[index] as androidx.compose.ui.graphics.vector.ImageVector,
                                    contentDescription = label
                                )
                            } else {
                                Icon(
                                    painter = icons[index] as androidx.compose.ui.graphics.painter.Painter,
                                    contentDescription = label
                                )
                            }
                        },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = when (selectedIndex) {
                    0 -> "Home Screen"
                    1 -> "Search Screen"
                    2 -> "Borrow Screen"
                    3 -> "Return Screen"
                    4 -> "Notifications"
                    else -> ""
                }
            )
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

@Preview(showBackground = true)
@Composable
fun LibraryAppPreview() {
    LibraryApp()
}
