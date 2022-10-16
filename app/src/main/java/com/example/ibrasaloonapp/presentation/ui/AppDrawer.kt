package com.example.ibrasaloonapp.presentation.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.ArrowRight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ibrasaloonapp.domain.model.MenuItem
import com.example.ibrasaloonapp.presentation.components.IconText
import com.example.ibrasaloonapp.presentation.theme.*


@Composable
fun DrawerHeader() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 64.dp)
    ) {


    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DrawerBody(
    modifier: Modifier = Modifier,
    items: List<MenuItem>,
    itemTextStyle: TextStyle = TextStyle(fontSize = 18.sp),
    onClick: (MenuItem) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(16.dp)
    ) {


        items(items = items, key = { item -> item.id }) { item ->
            Surface(
                onClick = { onClick(item) },
                modifier = Modifier
                    .padding(bottom = 8.dp),
                elevation = 0.dp,
                shape = MaterialTheme.shapes.small,
                color = Gray2,
                border = BorderStroke(1.dp, Gray1)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    IconText(
                        text = item.title,
                        icon = item.icon,
                        contentDescription = item.contentDescription,
                        textStyle = itemTextStyle
                    )

                    Spacer(modifier = Modifier.padding(horizontal = 8.dp))


                    Icon(
                        imageVector = Icons.Outlined.ArrowForwardIos,
                        contentDescription = "right arrow",
                        modifier = Modifier.size(12.dp)
                    )
                }
            }
        }

    }
}