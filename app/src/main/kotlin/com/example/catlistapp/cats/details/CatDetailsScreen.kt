package com.example.catlistapp.cats.details

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import coil.compose.rememberImagePainter
import com.example.catlistapp.R
import com.example.catlistapp.cats.api.model.CatApiModel
import com.example.catlistapp.cats.details.model.CatDetailsUiModel



fun NavGraphBuilder.catDetails(
    route: String,
    navController: NavController,
    arguments: List<NamedNavArgument>,
    onGalleryButtonClick: (String) -> Unit,
) = composable(
    route = route,
    arguments = arguments) { navBackStackEntry ->


    val catDetailsViewModel: CatDetailsViewModel = hiltViewModel(navBackStackEntry)

    val state = catDetailsViewModel.state.collectAsState()
    val context = LocalContext.current

    CatDetailsScreen(
        state = state.value,
        onClose = {
            navController.navigateUp()
        },
        context = context,
        onGalleryButtonClick = onGalleryButtonClick
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatDetailsScreen(
    state: CatDetailsContract.CatDetailsState,
    onClose: () -> Unit,
    context: Context,
    onGalleryButtonClick: (String) -> Unit,
) {

    val colors = MaterialTheme.colorScheme
    Scaffold(

        topBar = {

            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = state.cat?.name ?: "",
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0059FF)
                ),
                navigationIcon = {
                    IconButton(onClick = onClose) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = null,
                            tint = Color.White
                        )
                    }
                }
            )
        },
        content = { paddingValues ->
            if (state.loading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            } else {
                state.cat?.let { cat ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(paddingValues)
                            .verticalScroll(rememberScrollState())
                            .background(colors.background)
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 16.dp)
                        ) {
                            // Cat image
                            state.cat.reference_image_id?.let { imageId ->
                                Image(
                                    painter = rememberImagePainter(
                                        data = "https://cdn2.thecatapi.com/images/$imageId.jpg",
                                        builder = { crossfade(true) }
                                    ),
                                    contentDescription = "Cat image",
                                    modifier = Modifier
                                        .size(150.dp)
                                        .clip(shape = RoundedCornerShape(8.dp))
                                        .clickable {
                                            onGalleryButtonClick(cat.id)
                                        },
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(modifier = Modifier.width(16.dp))

                            Column(
                                modifier = Modifier
                                    .weight(1f)
                                    .align(Alignment.CenterVertically)
                            ) {
                                Text(buildBoldText("Countries of origin: ", cat.origin), style = MaterialTheme.typography.bodyLarge, color = colors.secondary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(buildBoldText("Life span: ", cat.life_span), style = MaterialTheme.typography.bodyLarge, color = colors.secondary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(buildBoldText("Weight: ", cat.weight.metric), style = MaterialTheme.typography.bodyLarge, color = colors.secondary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(buildBoldText("Rare: ", if (cat.rare == 1) "Rare" else "Common"), style = MaterialTheme.typography.bodyLarge, color = colors.secondary)
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        Text(buildBoldText("Description:"), style = MaterialTheme.typography.bodyLarge, color = colors.secondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = cat.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = colors.secondary,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 5
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(buildBoldText("Temperament:"), style = MaterialTheme.typography.bodyLarge, color = colors.secondary)
                        Spacer(modifier = Modifier.height(8.dp))
                        val temperaments = cat.temperament.split(", ")
                        Row(
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {
                            temperaments.forEach { temperament ->
                                SuggestionChip(
                                    onClick = {},
                                    label = { Text(text = temperament, color = Color.Black) },
                                    colors = ChipColors(
                                        labelColor = Color.White,
                                        containerColor = Color(0xFF0059FF),
                                        leadingIconContentColor = colors.primary,
                                        disabledContainerColor = colors.primary,
                                        disabledLabelColor = colors.primary,
                                        trailingIconContentColor = colors.primary,
                                        disabledLeadingIconContentColor = colors.primary,
                                        disabledTrailingIconContentColor = Color.White

                                    ),
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .background(colors.background)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(buildBoldText("Characteristics:"), style = MaterialTheme.typography.bodyLarge, color = colors.secondary)
                        val ratings = listOf(
                            Pair("Adaptability", cat.adaptability),
                            Pair("Affection Level", cat.affection_level),
                            Pair("Dog Friendly", cat.dog_friendly),
                            Pair("Energy Level", cat.energy_level),
                            Pair("Health Issues", cat.health_issues)
                        )

                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp)
                        ) {
                            ratings.forEach { (title, rating) ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = title,
                                        fontSize = 16.sp,
                                        modifier = Modifier.weight(1f),
                                        color = colors.secondary
                                    )
                                    Row(modifier = Modifier.padding(10.dp)) {
                                        for (i in 1..5) {
                                            Icon(
                                                imageVector = if (i <= rating) Icons.Filled.Star else Icons.Filled.Star,
                                                contentDescription = null,
                                                modifier = Modifier.size(24.dp),
                                                tint = if (i <= rating) Color(0xFF0059FF) else Color.LightGray
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }

                        Button(
                            onClick = {
                                cat.wikipedia_url?.let {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(it))
                                    context.startActivity(intent)
                                }
                            },
                            colors = ButtonColors(
                                contentColor = Color.White,
                                disabledContainerColor = Color.LightGray,
                                containerColor = Color(0xFF0059FF),
                                disabledContentColor = Color.Black
                            ),
                            modifier = Modifier.align(Alignment.CenterHorizontally),
//                            colors = ButtonDefaults.buttonColors(backgroundColor = Blue80)
                        ) {
                            Text(text = "Wikipedia", color = Color.White)
                        }
                    }
                }
            }
        }
    )
}






fun buildBoldText(boldText: String, normalText: String? = null): AnnotatedString {
    return buildAnnotatedString {
        append(boldText)
        addStyle(style = SpanStyle(fontWeight = FontWeight.Bold), start = 0, end = boldText.length)
        if (normalText != null) {
            append(normalText)
        }
    }
}


