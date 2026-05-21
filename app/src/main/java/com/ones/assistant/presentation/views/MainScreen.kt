package com.ones.assistant.presentation.views

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
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.ones.assistant.R
import com.ones.assistant.presentation.viewmodel.BooksListViewModel
import com.ones.assistant.presentation.views.books.BookDetails
import com.ones.assistant.ui.widgets.HomeCategoryScreen
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay

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
    onPodcastClick: (String) -> Unit = {},
    onMovieClick: () -> Unit = {},
    onPodcastIconClick: () -> Unit = {},
    booksViewModel: BooksListViewModel = hiltViewModel()
)  {

    val uiState by booksViewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        booksViewModel.loadBooks()
    }

    var selectedIndex by remember { mutableIntStateOf(0) }

    Scaffold(

        topBar = {

            TopAppBar(

                title = {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.logo_app),
                            contentDescription = "Logo",
                            modifier = Modifier
                                .size(40.dp)
                                .padding(end = 8.dp)
                        )

                        Text(
                            text = "WeareOne",
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                },

                actions = {

                    IconButton(onClick = onSearchClick) {

                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }

                    IconButton(onClick = onProfileClick) {

                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "Profile"
                        )
                    }
                }
            )
        },

        bottomBar = {

            BottomNavBar(
                selectedIndex = selectedIndex,
                onItemSelected = { selectedIndex = it }
            )
        }

    ) { innerPadding ->

        when (selectedIndex) {

            0 -> {

                HomeScreen(
                    modifier = Modifier.padding(innerPadding),
                    books = uiState.books,
                    onBookClick = onBookClick,
                    onLibraryClick = onLibraryClick,
                    onPodcastClick = onPodcastClick,
                    onMovieClick = onMovieClick,
                    onPodcastIconClick = onPodcastIconClick
                )
            }

            1 -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text("Activity Screen")
                }
            }

            2 -> {

                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Text("More Screen")
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
    onPodcastClick: (String) -> Unit = {},
    onMovieClick: () -> Unit = {},
    onPodcastIconClick: () -> Unit = {}
){

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {

        item {
            BannerSection()
        }

        item {

            HomeCategoryScreen(
                onLibraryClick = onLibraryClick,
                onPodcastIconClick = onPodcastIconClick
            )
        }

        item {

            BookSection(
                books = books,
                onBookClick = onBookClick
            )
        }

        item {

            LibrarySection(
                onBookClick = onBookClick
            )
        }

        item {

            PodcastSection(
                onPodcastClick = onPodcastClick
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun BannerSection() {

    val pagerState = rememberPagerState(pageCount = { 3 })

    val bannerImages = listOf(
        R.drawable.library,
        R.drawable.podcasts,
        R.drawable.movies
    )

    LaunchedEffect(Unit) {

        while (true) {

            delay(3000)

            pagerState.animateScrollToPage(
                (pagerState.currentPage + 1) % pagerState.pageCount
            )
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(240.dp)
    ) {

        HorizontalPager(
            state = pagerState
        ) { page ->

            Image(
                painter = painterResource(id = bannerImages[page]),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 10.dp)
        ) {

            repeat(pagerState.pageCount) { index ->

                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(8.dp)
                        .background(
                            if (pagerState.currentPage == index)
                                Color.DarkGray
                            else
                                Color.LightGray,
                            CircleShape
                        )
                )
            }
        }
    }
}

@Composable
fun BookSection(
    books: List<BookDetails>,
    onBookClick: (String) -> Unit
) {

    LazyRow(
        contentPadding = PaddingValues(horizontal = 12.dp)
    ) {

        items(
            items = books,
            key = { it.id }
        ) { book ->

            BookItem(
                book = book,
                onBookClick = onBookClick
            )
        }
    }
}

@Composable
fun BookItem(
    book: BookDetails,
    onBookClick: (String) -> Unit
) {

    Column(
        modifier = Modifier
            .width(130.dp)
            .padding(8.dp)
            .clickable {
                onBookClick(book.id)
            },

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {

            Image(
                painter = rememberAsyncImagePainter(book.coverUrl),
                contentDescription = book.title,

                modifier = Modifier
                    .height(180.dp)
                    .fillMaxWidth(),

                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = book.title,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            text = book.author,
            fontSize = 11.sp,
            color = Color.Gray,
            maxLines = 1
        )
    }
}

data class PodcastItem(
    val title: String,
    val creator: String,
    val coverRes: Int
)

@Composable
fun LibrarySection(
    onBookClick: (String) -> Unit = {}
) {

    val books = listOf(

        PodcastItem(
            "The Octopus",
            "Stephen Covey",
            R.drawable.a1
        ),

        PodcastItem(
            "Losing the Plot",
            "Annaleise Byrd",
            R.drawable.losing_the_plot_book
        ),

        PodcastItem(
            "Atomic Habits",
            "James Clear.",
            R.drawable.automatic_habits_book
        ),

        PodcastItem(
            "Only Skill",
            "Jonathan Levi",
            R.drawable.only_skill_book
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Text(
            text = "Your Popular",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Text(
            text = "Based on your reading.",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow {

            items(books) { book ->

                PodcastCard(

                    podcast = book,

                    onClick = {
                        onBookClick(book.title)
                    }
                )
            }
        }
    }
}

@Composable
fun PodcastSection(
    onPodcastClick: (String) -> Unit = {}
) {

    val podcasts = listOf(

        PodcastItem(
            "How To",
            "Every two weeks",
            R.drawable.dear_to_lead
        ),
        PodcastItem(
            "Self-Improvement",
            "Updated weekly",
            R.drawable.for_the_record
        ),
        PodcastItem(
            "Music Commentary",
            "Updated 15 Jan 2026",
            R.drawable.music
        ),
        PodcastItem(
            "Buddhism",
            "Updated 15 Feb 2026",
            R.drawable.buddhism
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {

        Text(
            text = "You Might Like",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )

        Text(
            text = "Based on your listening.",
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        LazyRow {

            items(podcasts) { podcast ->

                PodcastCard(

                    podcast = podcast,

                    onClick = {
                        onPodcastClick(podcast.title)
                    }
                )
            }
        }
    }
}

@Composable
fun PodcastCard(
    podcast: PodcastItem,
    onClick: () -> Unit = {}
) {

    Column(
        modifier = Modifier
            .width(140.dp)
            .padding(8.dp)
            .clickable {
                onClick()
            },

        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = podcast.coverRes),
            contentDescription = podcast.title,

            modifier = Modifier
                .width(120.dp)
                .height(160.dp)
                .clip(RoundedCornerShape(8.dp)),

            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = podcast.title,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            maxLines = 1
        )

        Text(
            text = podcast.creator,
            fontSize = 10.sp,
            color = Color.Gray,
            maxLines = 1
        )
    }
}

@Composable
fun BottomNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {

    val items = listOf(
        "Home",
        "WishList",
        "More"
    )

    val icons = listOf(
        Icons.Default.Home,
        Icons.Default.MonitorHeart,
        Icons.Default.Apps
    )

    NavigationBar {

        items.forEachIndexed { index, label ->

            NavigationBarItem(
                selected = selectedIndex == index,

                onClick = {
                    onItemSelected(index)
                },

                icon = {

                    Icon(
                        imageVector = icons[index],
                        contentDescription = label
                    )
                },

                label = {
                    Text(label)
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    WearOneHome ()
}
