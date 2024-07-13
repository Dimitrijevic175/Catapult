package com.example.catlistapp.cats.list

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Leaderboard
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Quiz
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign

import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

import com.example.catlistapp.R
import com.example.catlistapp.accountDetails.accountDetailsScreen
import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.cats.gallery.AppIconButton
import com.example.catlistapp.core.theme.Black55
import com.example.catlistapp.core.theme.Blue55
import com.example.catlistapp.core.theme.EnableEdgeToEdge
import com.example.catlistapp.core.theme.LightGrey55
import com.example.catlistapp.core.theme.White55
import com.example.catlistapp.edit.editScreen
import kotlinx.coroutines.launch

fun NavGraphBuilder.cats(
    route: String,
    onCatClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardClick: (Int) -> Unit
) = composable(
    route = route
) {
    val catListViewModel: CatListViewModel = hiltViewModel()

    val state = catListViewModel.state.collectAsState()

    EnableEdgeToEdge(isDarkTheme = false)

    CatListScreen(
        state = state.value, eventPublisher = {catListViewModel.setEvent(it)}, onCatClick = onCatClick, onProfileClick = onProfileClick, onLeaderboardClick = onLeaderboardClick, onQuizClick = onQuizClick) {
    }
}

@Composable
fun ButtonProfile(
    onProfileClick: () -> Unit
){
    Button(onClick = { onProfileClick() }) {
        Text(text = "Profile")
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatListScreen(
    state: CatListContract.CatListState,
    eventPublisher: (uiEvent: CatListContract.CatListUiEvent) -> Unit,
    onCatClick: (String) -> Unit,
    onProfileClick: () -> Unit,
    onQuizClick: () -> Unit,
    onLeaderboardClick: (Int) -> Unit,
    onDrawerMenuClick: () -> Unit,
) {

    val colors = MaterialTheme.colorScheme

    var searchText by remember{ mutableStateOf("")}

    if(searchText.isEmpty()){
        eventPublisher(CatListContract.CatListUiEvent.CloseSearchMode)
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Cats",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0059FF)
                ),
                navigationIcon = {
                    Row {
                        AppIconButton(
                            imageVector = Icons.Default.AccountCircle,
                            onClick = onProfileClick,
                        )
                        AppIconButton(
                            imageVector = Icons.Default.Leaderboard,
                            onClick = { onLeaderboardClick(1) },
                        )
                        AppIconButton(
                            imageVector = Icons.Default.Quiz,
                            onClick = onQuizClick,
                        )
                    }
                }
            )
        },
    )
    { paddingValues ->
                if (state.loading) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(colors.background),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                } else {

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                            .background(colors.background),
                    ) {

                        OutlinedTextField(
                            value = searchText,
                            onValueChange = {
                                searchText = it
                                if (it.isEmpty()) {
                                    eventPublisher(CatListContract.CatListUiEvent.CloseSearchMode)
                                } else {
                                    eventPublisher(
                                        CatListContract.CatListUiEvent.SearchQueryChanged(
                                            it
                                        )
                                    )
                                }
                            },
                            label = { Text("Search cats") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp, horizontal = 16.dp),
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search icon",
                                    tint = Color.Gray
                                )
                            },
                            textStyle = TextStyle(color = colors.secondary),
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        if (state.isSearchMode) {

                            state.filteredCats.forEach { cat ->
                                CatCard(cat = cat, onCatClick = onCatClick)
                            }
                        } else {
                            state.cats.forEach { cat ->
                                CatCard(cat = cat, onCatClick = onCatClick)
                            }
                        }
                    }
                }
            }
}




@Composable
fun CatCard(
    cat: CatApiModel,
    onCatClick: (String) -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
            .padding(bottom = 16.dp)
            .clickable {
                onCatClick(cat.id)
            },
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(
            containerColor = LightGrey55
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ime mačke centrirano
            Text(
                text = cat.name,
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = Black55,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Temperamenti
            Row(
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                val temperaments = cat.temperament.split(", ").take(3)
                temperaments.forEach { temperament ->
                    SuggestionChip(
                        onClick = {},
                        label = { Text(text = temperament, color = Color.Black) },
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
            }

            // Deskripcija sa ograničenjem na 250 znakova
            Text(
                text = cat.description.take(120) + "...",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Black,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}


