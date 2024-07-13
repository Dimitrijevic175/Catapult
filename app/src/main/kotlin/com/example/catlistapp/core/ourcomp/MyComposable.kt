package com.example.catlistapp.core.ourcomp

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.example.catlistapp.cats.gallery.model.CatGalleryUiModel

@Composable
fun PhotoPreview(
    modifier: Modifier,
    photo: CatGalleryUiModel,
) {
    Box(modifier = modifier, contentAlignment = Alignment.BottomCenter) {
        SubcomposeAsyncImage(
            modifier = Modifier.fillMaxSize(),
            model = photo.url,
            loading = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(36.dp),
                    )
                }

            },
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
        )
    }
}