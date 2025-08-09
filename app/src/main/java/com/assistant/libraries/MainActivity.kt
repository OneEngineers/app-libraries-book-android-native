package com.assistant.libraries

import com.assistant.libraries.utilities.ApolloClientProvider

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.assistant.libraries.graphql.BooksQuery
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                BooksScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BooksScreen() {
    var books by remember { mutableStateOf(listOf<String>()) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        scope.launch {
            try {
                val res = ApolloClientProvider.apolloClient.query(BooksQuery()).execute()
                books = res.data?.books?.map {
                    it?.book_title
                    it?.ISBN as String
                } ?: emptyList()
            } catch (e: Exception) {
                error = e.message
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Books") })
        }
    ) { padding ->
        if (error != null) {
            Text("Error: $error", modifier = Modifier.fillMaxSize())
        } else {
            LazyColumn(contentPadding = padding) {
                items(books) { book ->
                    Text(book, style = MaterialTheme.typography.bodyLarge)
                }
            }
        }
    }
}
