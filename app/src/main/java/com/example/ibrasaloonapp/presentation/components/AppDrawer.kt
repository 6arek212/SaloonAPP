package com.example.ibrasaloonapp.presentation.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibrasaloonapp.domain.model.MenuItem


@Composable
fun DrawerHeader() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp)
    ) {


    }
}


@Composable
fun DrawerBody(
    modifier: Modifier = Modifier,
    items: List<MenuItem> ,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onClick: (MenuItem) -> Unit
) {

    LazyColumn(modifier) {


        items(items = items) { item ->
            Row(modifier = Modifier
                .fillMaxWidth()
                .clickable { onClick(item) }
                .padding(16.dp)) {

                Icon(imageVector = item.icon, contentDescription = item.contentDescription)

                Spacer(modifier = Modifier.padding(16.dp))

                Text(text = item.title, modifier = Modifier.weight(1f), style = itemTextStyle)
            }
        }

    }
}