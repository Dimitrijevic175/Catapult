package com.example.catlistapp.cats.gallery.photo

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage

import com.example.catlistapp.cats.gallery.AppIconButton

fun NavGraphBuilder.catPhotoScreen(
    route: String,
    navController: NavController,
    arguments: List<NamedNavArgument>
) = composable(route = route, arguments = arguments) { navBackStackEntry ->

    val catPhotoViewModel: CatPhotoViewModel = hiltViewModel(navBackStackEntry)
    val catState by catPhotoViewModel.catPhotoState.collectAsState()


    Surface(
        tonalElevation = 1.dp
    ) {
        CatPhotoScreen(
            catState = catState,
            onClose = { navController.navigateUp() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CatPhotoScreen(
    catState: ICatPhotoContract.CatPhotoState,
    onClose: () -> Unit
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = "Photo", textAlign = TextAlign.Center, color = Color.White)
                },
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        onClick = onClose
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0059FF)
                ),
            )
        },
        content = {
            val pagerState = rememberPagerState(
                pageCount = {
                    catState.photos.size
                },
                initialPage = catState.photoIndex
            )


            if (catState.isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(it)
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            } else if (catState.error != null) {
                Box(
                    modifier = Modifier
                        .padding(it)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    val errorMessage = when (catState.error) {
                        is ICatPhotoContract.CatPhotoState.DetailsError.DataUpdateFailed ->
                            "Failed to load. Error message: ${catState.error.cause?.message}."
                    }

                    Text(text = errorMessage, fontSize = 20.sp)
                }
            } else if (catState.photos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "There is no data for that cat",
                        fontSize = 20.sp
                    )
                }
            } else {
                HorizontalPager(
                    state = pagerState,
                    contentPadding = it
                ) { pageIndex ->
                    val photo = catState.photos[pageIndex]

                    SubcomposeAsyncImage(
                        modifier = Modifier.fillMaxSize(),
                        model = photo,
                        contentDescription = null,
                        contentScale = ContentScale.FillWidth,
                    )
                }
            }
        }
    )
}