package com.example.ibrasaloonapp.presentation.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibrasaloonapp.domain.model.MenuItem
import com.example.ibrasaloonapp.presentation.theme.Gray1
import com.example.ibrasaloonapp.presentation.theme.Green


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
    items: List<MenuItem>,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onClick: (MenuItem) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp)
    ) {


        items(items = items, key = { item -> item.id }) { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(MaterialTheme.shapes.large)
                    .border(1.dp, Gray1, MaterialTheme.shapes.large)
                    .clickable { onClick(item) }
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {

                Icon(imageVector = item.icon, contentDescription = item.contentDescription)

                Spacer(modifier = Modifier.padding(16.dp))

                Text(text = item.title, modifier = Modifier.weight(1f), style = itemTextStyle)
            }
        }

    }
}