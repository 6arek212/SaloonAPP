package com.example.ibrasaloonapp.presentation.ui.customer_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.ibrasaloonapp.presentation.components.SubTitle
import com.example.ibrasaloonapp.presentation.components.UserCard
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.presentation.components.StatsCard
import com.example.ibrasaloonapp.presentation.theme.*

@Composable
fun CustomersView(
    viewModel: CustomersViewModel = hiltViewModel(),
    navigateToUserDetails: (User) -> Unit
) {
    val users = viewModel.state.value.users
    val usersCount = viewModel.state.value.usersCount
    val newUsersCount = viewModel.state.value.newUsersCount
    val search = viewModel.state.value.search

    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        viewModel.onTriggerEvent(CustomersListEvent.GetCustomersList)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Gray1)
    ) {


        SubTitle(text = stringResource(id = R.string.customers))

        Spacer(modifier = Modifier.padding(6.dp))

        Row(modifier = Modifier.wrapContentWidth()) {
            TextField(
                modifier = Modifier,
                value = search,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                keyboardActions = KeyboardActions(onSearch = {
                    viewModel.onTriggerEvent(
                        CustomersListEvent.GetCustomersList
                    )
                    focusManager.clearFocus()
                }),
                leadingIcon = {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.search_normal_1),
                        contentDescription = "search"
                    )
                },
                onValueChange = { s ->
                    viewModel.onTriggerEvent(
                        CustomersListEvent.OnSearchChanged(s)
                    )
                })

            Button(onClick = {
                viewModel.onTriggerEvent(
                    CustomersListEvent.GetCustomersList
                )
                focusManager.clearFocus()
            }) {
                Text(text = "Search")
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp)
                ) {
                    StatsCard(
                        modifier = Modifier.weight(1f),
                        title = "Users",
                        value = "$usersCount",
                        visible = newUsersCount != null,
                        leadingIcon = ImageVector.vectorResource(id = R.drawable.graph),
                        cardColor = Orange
                    )

                    Spacer(modifier = Modifier.padding(8.dp))

                    StatsCard(
                        title = "New Users",
                        value = "$newUsersCount",
                        visible = newUsersCount != null,
                        leadingIcon = ImageVector.vectorResource(id = R.drawable.diagram),
                        cardColor = GrayBlue
                    )
                }
            }

            itemsIndexed(items = users) { index, item ->
                UserCard(user = item, onClick = { navigateToUserDetails(item) })
            }
        }
    }


}