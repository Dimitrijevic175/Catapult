package com.example.catlistapp.cats.gallery

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MediumTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import coil.compose.SubcomposeAsyncImage

import com.example.catlistapp.cats.gallery.model.CatGalleryUiModel
import com.example.catlistapp.core.ourcomp.PhotoPreview

fun NavGraphBuilder.catGallery(
    route: String,
    arguments: List<NamedNavArgument>,
    onPhotoClick: (catId: String, photoIndex: Int) -> Unit,
    onClose: () -> Unit,
) = composable(
    route = route,
    arguments = arguments,
) { navBackStackEntry ->

    val catGalleryViewModel: CatGalleryViewModel = hiltViewModel(navBackStackEntry)


    val state = catGalleryViewModel.state.collectAsState()

    CatGalleryScreen(
        state = state.value,
        onPhotoClick = onPhotoClick,
        onClose = onClose,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatGalleryScreen(
    state: CatGalleryContract.CatGalleryState,
    onPhotoClick: (catId: String, photoIndex: Int) -> Unit,
    onClose: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Gallery",
                        color = Color.White,
                        textAlign = TextAlign.Center
                    )
                },
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.Default.ArrowBack,
                        onClick = onClose,
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0059FF)
                ),
            )
            Spacer(modifier = Modifier.height(16.dp))
        },
        content = { paddingValues ->
            BoxWithConstraints(
                modifier = Modifier,
                contentAlignment = Alignment.BottomCenter,
            ) {
                val screenWidth = this.maxWidth
                val cellSize = (screenWidth / 2) - 4.dp

                LazyVerticalGrid(
                    modifier = Modifier.fillMaxSize(),
                    columns = GridCells.Fixed(2),
                    contentPadding = paddingValues,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(items = state.photos,
                        key = { index: Int, item: String ->
                            item
                        }) { index: Int, item: String ->
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clickable {
                                    onPhotoClick(state.catId, index)
                                },
                            model = item,
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            loading = {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.align(Alignment.Center)
                                    )
                                }
                            }
                        )
                    }
                }
            }
        },
    )
}


@Composable
fun AppIconButton(
    modifier: Modifier = Modifier,
    imageVector: ImageVector,
    onClick: () -> Unit,
    contentDescription: String? = null,
    tint: Color = Color.White,
) {
    IconButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = contentDescription,
            tint = tint,

        )
    }
}





