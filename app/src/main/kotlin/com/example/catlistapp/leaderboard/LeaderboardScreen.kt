package com.example.catlistapp.leaderboard

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catlistapp.cats.gallery.AppIconButton
import java.text.DecimalFormat


@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.leaderboardScreen(
    route : String,
    navController: NavController,
    arguments: List<NamedNavArgument>,
) = composable(route = route, arguments = arguments) {
    val leaderboardViewModel: LeaderboardViewModel = hiltViewModel()
    val state by leaderboardViewModel.leaderboardState.collectAsState()

    Surface(
        tonalElevation = 1.dp
    ) {
        Scaffold (
            topBar = {
                CenterAlignedTopAppBar(
                    title = {
                        Text(text = "Leaderboard",
                            color = Color.White,
                            textAlign = TextAlign.Center
                        )
                    },
                    navigationIcon = {
                        AppIconButton(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            onClick = {
                                navController.navigate("cats")
                            }
                        )
                    },
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                        containerColor = Color(0xFF0059FF)
                    ),
                )
            },
            content = {
                LeaderboardScreen(
                    state = state,
                    paddingValues = it,
                )
            }
        )
    }
}

@Composable
fun LeaderboardScreen(
    state : LeaderboardContract.LeaderboardState,
    paddingValues: PaddingValues,

) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
    ) {
        if (state.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
        else if (state.error != null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val errorMessage = when (state.error) {
                    is LeaderboardContract.LeaderboardState.DetailsError.DataUpdateFailed ->
                        "Failed to load. Error message: ${state.error.cause?.message}."
                }

                Text(text = errorMessage, fontSize = 20.sp)
            }
        }
        else {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                itemsIndexed(state.results) { index, user ->
                    val repeats = state.results.count { it.nickname == user.nickname }
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(8.dp),
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = (index + 1).toString(),
                                modifier = Modifier.padding(16.dp),
                                fontWeight = FontWeight.Bold
                            )
                            Column(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = user.nickname,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = "User shared result $repeats times",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            val formattedResult = DecimalFormat("#.##").format(user.result)
                            Text(
                                text = "$formattedResult points",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(16.dp)
                            )
                        }
                    }
                    if (index < state.results.size - 1) {
                        HorizontalDivider()
                    }
                }
            }
        }
    }
}


