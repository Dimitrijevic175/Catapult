package com.example.catlistapp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.catlistapp.core.theme.CatListAppTheme
import com.example.catlistapp.navigation.CatNavigation
import com.example.catlistapp.profile.datastore.ProfileDataStore
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var profileDataStore: ProfileDataStore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            CatListAppTheme {
                CatNavigation(profileDataStore)
            }
        }
        val authData = profileDataStore.data.value
        Log.d("DATASTORE", "AuthData = $authData")
    }
}

