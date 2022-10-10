package com.example.ibrasaloonapp.presentation.ui.customerList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ibrasaloonapp.presentation.components.SubTitle
import com.example.ibrasaloonapp.presentation.components.UserCard


@Composable
fun CustomersView(viewModel: CustomersViewModel = hiltViewModel()) {
    val customers = viewModel.state.value.customers


    LaunchedEffect(Unit) {
        viewModel.onTriggerEvent(CustomersListEvent.GetCustomersList)
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp)
        .background(MaterialTheme.colors.background)) {

        SubTitle(text = "Customers")

        Spacer(modifier = Modifier.padding(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            itemsIndexed(items = customers) { index, item ->
                UserCard(user = item)
            }
        }
    }


}