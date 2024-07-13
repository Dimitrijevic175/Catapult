package com.example.catlistapp.edit

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
//import com.example.catlistapp.accountDetails.AccountDetailsViewModel
import kotlinx.coroutines.launch

fun NavGraphBuilder.editScreen(
    route: String,
    navController: NavController,
    onBack: () -> Unit
) = composable(route = route) {
    val viewModel: EditViewModel = hiltViewModel()
    val state = viewModel.state.collectAsState()
//    val viewModel2: AccountDetailsViewModel = hiltViewModel()

    EditScreen(
        state = state.value,
        eventPublisher = viewModel::setEvent,
        onBack = onBack,
//        eventPublisher2 = viewModel2::setEvent
    )
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    state: EditContract.EditState,
    eventPublisher: (EditContract.EditEvent) -> Unit,
//    eventPublisher2: (AccountDetailsContract.AccountDetailsEvent) -> Unit,
    onBack: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    var nickname by remember { mutableStateOf(TextFieldValue(state.nickname)) }
    var fullName by remember { mutableStateOf(TextFieldValue(state.fullName)) }
    var email by remember { mutableStateOf(TextFieldValue(state.email)) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(
                    text = "Edit Profile",
                    color = Color.White,
                    textAlign = TextAlign.Center
                ) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFF0059FF)
                ),
            )
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Fullname:",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = fullName,
                    onValueChange = {
                        fullName = it
                        eventPublisher(EditContract.EditEvent.FullNameChanged(it.text))
//                        eventPublisher2(AccountDetailsContract.AccountDetailsEvent.FullNameChanged(it.text))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Nickname:",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = nickname,
                    onValueChange = {
                        nickname = it
                        eventPublisher(EditContract.EditEvent.NicknameChanged(it.text))
//                        eventPublisher2(AccountDetailsContract.AccountDetailsEvent.NicknameChanged(it.text))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Email:",
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Bold
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        eventPublisher(EditContract.EditEvent.EmailChanged(it.text))
//                        eventPublisher2(AccountDetailsContract.AccountDetailsEvent.EmailChanged(it.text))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val valid = validateInputs(nickname.text, fullName.text, email.text)
                        if (valid) {
                            coroutineScope.launch {
                                eventPublisher(EditContract.EditEvent.SaveClicked)
                                onBack()
                            }
                        }
                    },
                    modifier = Modifier.padding(top = 16.dp),
                    colors = ButtonColors(
                        contentColor = Color.White,
                        disabledContainerColor = Color.LightGray,
                        containerColor = Color(0xFF0059FF),
                        disabledContentColor = Color.Black
                    )
                ) {
                    Text("Save")
                }
            }
        }
    )
}

private fun validateInputs(nickname: String, fullName: String, email: String): Boolean {
    return nickname.isNotBlank() && fullName.isNotBlank() && email.isNotBlank() && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}

@Preview(showBackground = true)
@Composable
fun EditScreenPreview() {
    val mockState = EditContract.EditState(
        nickname = "MockNickname",
        fullName = "Mock FullName",
        email = "mock@example.com"
    )
    EditScreen(
        state = mockState,
        eventPublisher = {},
        onBack = {},
//        eventPublisher2 = {}
    )
}