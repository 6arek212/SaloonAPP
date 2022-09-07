package com.example.ibrasaloonapp.presentation.ui.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun ProfileView(navController: NavController, viewModel: ProfileViewModel = hiltViewModel()) {
    val user = viewModel.state.value.user
    user?.let {
        Column() {

            Text(text = "Profile")

            Text(text = user.firstName)

            Text(text = user.lastName)

            Text(text = user.phone)

            Button(onClick = { /*TODO*/ }) {
                Text(text = "Update")
            }

        }

    }
}