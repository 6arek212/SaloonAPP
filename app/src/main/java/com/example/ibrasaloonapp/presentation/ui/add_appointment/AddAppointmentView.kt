package com.example.ibrasaloonapp.presentation.ui.add_appointment

import androidx.compose.foundation.layout.Column
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun AddAppointmentView(
    navController: NavController,
    viewModel: AddAppointmentViewModel = hiltViewModel()
) {

    Column {

        Text(text = "Hello World")


    }




}