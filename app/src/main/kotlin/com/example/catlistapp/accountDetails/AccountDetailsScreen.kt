package com.example.catlistapp.accountDetails


import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.example.catlistapp.cats.gallery.AppIconButton
import com.example.catlistapp.core.theme.EnableEdgeToEdge
import com.example.catlistapp.edit.EditContract

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
fun NavGraphBuilder.accountDetailsScreen(
    route: String,
    navController: NavController,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
) = composable(route = route) {
    val accountDetailsViewModel: AccountDetailsViewModel = hiltViewModel()
    val state = accountDetailsViewModel.state.collectAsState()

    EnableEdgeToEdge(isDarkTheme = false)

    AccountDetailsScreen(
        state = state.value,
        onBack = onBack,
        onEditProfile = onEditProfile
    )

}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountDetailsScreen (
    state: EditContract.EditState,
    onBack: () -> Unit,
    onEditProfile: () -> Unit,
) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Account Details", color = Color.White) },
                navigationIcon = {
                    AppIconButton(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        onClick = {
                            onBack()
                        }
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0059FF)
                ),
            )
        },
        content = {
            if (state.loading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally, // Centriranje sadržaja
                    verticalArrangement = Arrangement.Center // Vertikalno centriranje sadržaja
                ) {
                        Text(
                            text = "Fullname:",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        TextField(
                            value = state.fullName,
                            onValueChange = {},
                            readOnly = true,
                            enabled = false,
                        )
                    Spacer(Modifier.size(16.dp,16.dp))
                        Text(
                            text = "Nickname:",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        TextField(
                            value = state.nickname,
                            onValueChange = {},
                            readOnly = true,
                            enabled = false,
                        )
                    Spacer(Modifier.size(16.dp,16.dp))
                        Text(
                            text = "Email:",
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        TextField(
                            value = state.email,
                            onValueChange = {},
                            readOnly = true,
                            enabled = false,
                        )



                    Button(
                        onClick = onEditProfile,
                        modifier = Modifier.padding(top = 16.dp),
                        colors = ButtonColors(
                            contentColor = Color.White,
                            disabledContainerColor = Color.LightGray,
                            containerColor = Color(0xFF0059FF),
                            disabledContentColor = Color.Black
                        )
                    ) {
                        Text(text = "Edit Profile")
                    }
                }
            }
        }
    )
}

@Composable
fun ReadOnlyField(label: String, value: String) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = label,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        TextField(
            value = value,
            onValueChange = {},
            readOnly = true,
            enabled = false,
            modifier = Modifier.padding(bottom = 16.dp)
        )
    }
}

@Preview
@Composable
fun AccountDetailsScreenPreview() {
    AccountDetailsScreen(
        state = EditContract.EditState(
            fullName = "John Doe",
            nickname = "Johnny",
            email = "adsad@raf.rs",
            loading = false
        ),
        onBack = {},
        onEditProfile = {}
    )
}



