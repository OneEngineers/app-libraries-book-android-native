package com.ones.assistant.presentation.views

import com.ones.assistant.R
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import com.ones.assistant.ui.widgets.HomeCategoryScreen
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import kotlinx.coroutines.delay
import coil.compose.rememberAsyncImagePainter
import com.ones.assistant.presentation.views.books.BookDetails
import com.ones.assistant.presentation.viewmodel.BooksListViewModel
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.LaunchedEffect
import dagger.hilt.android.AndroidEntryPoint
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.shape.RoundedCornerShape


@AndroidEntryPoint
class MainScreen : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WearOneHome()
        }
    }
}

@SuppressLint("AutoboxingStateCreation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WearOneHome(
    onProfileClick: () -> Unit = {},
    onSearchClick: () -> Unit = {},
    onLibraryClick: () -> Unit = {},
    onBookClick: (String) -> Unit = {},
    onPodcastClick: () -> Unit = {},
    onMovieClick: () -> Unit = {},
    booksViewModel: BooksListViewModel = hiltViewModel()
) {
    val uiState by booksViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        booksViewModel.loadBooks()
    }

    var selectedIndex by remember { mutableIntStateOf(0) }
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFE0CFFA))
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.logo_app),
                                contentDescription = "App Logo",
                                modifier = Modifier
                                    .size(40.dp)
                                    .padding(end = 8.dp)
                            )
                            Text(
                                text = "WeareOne",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0D1B2A)
                            )
                        }
                    },
                    actions = {
                        IconButton(onClick = onSearchClick) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                        IconButton(onClick = onProfileClick) {
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
                0 -> HomeScreen(
                    modifier = Modifier.padding(innerPadding),
                    books = uiState.books,
                    onBookClick = onBookClick,
                    onLibraryClick = onLibraryClick,
                    onPodcastClick = onPodcastClick,
                    onMovieClick = onMovieClick
                )
                1 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Search Screen - Use top bar search or navigate to SearchScreen")
                }
                2 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Borrow Screen")
                }
                3 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Return Screen")
                }
                4 -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Notifications")
                }
            }
        }
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    books: List<BookDetails> = emptyList(),
    onBookClick: (String) -> Unit = {},
    onLibraryClick: () -> Unit = {},
    onPodcastClick: () -> Unit = {},
    onMovieClick: () -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        item { BannerSection() }
        item { HomeCategoryScreen(
                onLibraryClick = onLibraryClick,
                onPodcastClick = onPodcastClick,
                onMovieClick = onMovieClick
        ) }
        item { BookSection(books = books, onBookClick = onBookClick) }
        item { LibrarySection(onLibraryClick = onLibraryClick) }
        item { PodcastSection() }
        item { MovieSection(onMovieClick = onMovieClick) }
        item { Spacer(modifier = Modifier.height(8.dp)) }
    }
}

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun BannerSection() {
    val pagerState = rememberPagerState(pageCount = { 3 })
    val bannerImages = listOf(
        R.drawable.library,
        R.drawable.podcasts,
        R.drawable.movies
    )

    LaunchedEffect(pagerState) {
        while (true) {
            delay(3000)
            pagerState.animateScrollToPage((pagerState.currentPage + 1) % pagerState.pageCount)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        ) { page ->
            Image(
                painter = painterResource(id = bannerImages[page]),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            Modifier
                .height(20.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .background(color, shape = androidx.compose.foundation.shape.CircleShape)
                        .size(8.dp)
                )
            }
        }
    }
}

@Composable
fun BookSection(
    books: List<BookDetails> = emptyList(),
    onBookClick: (String) -> Unit = {}
) {
    Column {
        LazyRow(contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)) {
            items(items = books, key = { it.id }) { book ->
                BookItem(book = book, onBookClick = onBookClick)
            }
        }
    }
}

@Composable
fun BookItem(
    book: BookDetails,
    onBookClick: (String) -> Unit = {}
) {
    Column(
        modifier = Modifier
            .width(100.dp)
            .padding(8.dp)
            .clickable { onBookClick(book.id) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = rememberAsyncImagePainter(book.coverUrl),
            contentDescription = null,
            modifier = Modifier
                .height(140.dp)
                .width(200.dp),
            contentScale = ContentScale.Crop
        )
        Text(book.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
        Text(book.author, fontSize = 10.sp, color = Color.Gray, maxLines = 1)
    }
}
@Composable
fun LibrarySection(onLibraryClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "Your popular >",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Based on your reading.",
            fontSize = 14.sp,
            color = Color.Gray
        )

        LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
            items(listOf(
                PodcastItem("The 7 Habits Highly Effective People", "by Stephen Covey", R.drawable.habits_book),
                PodcastItem("Losing the Plot", "by Annaleise Byrd", R.drawable.losing_the_plot_book),
                PodcastItem("The Only Skill that Matters", "by Jonathan Levi", R.drawable.only_skill_book),
                PodcastItem("The Executive Coaching Playbook", "by Nadine Greiner, Ph.D. and Becky Davis, MA", R.drawable.coaching_book)
            )) { podcast ->
                PodcastCard(podcast)
            }
        }
    }
}
@Composable
fun MovieSection(onMovieClick: () -> Unit = {}) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "You Might Like >",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Based on your watching.",
            fontSize = 14.sp,
            color = Color.Gray
        )

        LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
            items(listOf(
                PodcastItem("The 7 Habits Highly Effective People", "by Stephen Covey", R.drawable.jailer),
                PodcastItem("Losing the Plot", "by Annaleise Byrd", R.drawable.beauty),
                PodcastItem("The Only Skill that Matters", "by Jonathan Levi", R.drawable.seven_scream),
                PodcastItem("The Executive Coaching Playbook", "by Nadine Greiner, Ph.D. and Becky Davis, MA", R.drawable.shawshak)
            )) { podcast ->
                PodcastCard(podcast)
            }
        }
    }
}
@Composable
fun PodcastSection() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Text(
            text = "You Might Like >",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(
            text = "Based on your listening.",
            fontSize = 14.sp,
            color = Color.Gray
        )

        LazyRow(contentPadding = PaddingValues(horizontal = 8.dp)) {
            items(listOf(
                PodcastItem("The 7 Habits Highly Effective People", "by Stephen Covey", R.drawable.dear_to_lead),
                PodcastItem("Losing the Plot", "by Annaleise Byrd", R.drawable.for_the_record),
                PodcastItem("The Only Skill that Matters", "by Jonathan Levi", R.drawable.the321),
                PodcastItem("The Executive Coaching Playbook", "by Nadine Greiner, Ph.D. and Becky Davis, MA", R.drawable.strategies)
            )) { podcast ->
                PodcastCard(podcast)
            }
        }
    }
}

data class PodcastItem(val title: String, val creator: String, val coverRes: Int)

@Composable
fun PodcastCard(podcast: PodcastItem) {
    Column(
        modifier = Modifier
            .width(140.dp)
            .padding(8.dp)
            .clickable { /* handle click */ },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = podcast.coverRes),
            contentDescription = podcast.title,
            modifier = Modifier
                .width(115  .dp)
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Text(podcast.title, fontWeight = FontWeight.Bold, fontSize = 12.sp, maxLines = 1)
        Text(podcast.creator, fontSize = 10.sp, color = Color.Gray, maxLines = 1)
    }
}
@Composable
fun BottomNavBar(selectedIndex: Int, onItemSelected: (Int) -> Unit) {
    val items = listOf("Home", "Content", "Transfer", "Activity", "Mores")
    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.ContentCopy,
        Icons.Default.Mediation,
        Icons.Default.MonitorHeart,
        Icons.Default.Apps
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
fun PreviewMainScreen() {
    WearOneHome()
}

