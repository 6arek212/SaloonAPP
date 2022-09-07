package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.StackedBarChart
import androidx.compose.material.icons.filled.StackedLineChart
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

@Composable
fun HomeAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(40.dp)
            .background(MaterialTheme.colors.primary)
            .padding(8.dp)
    ) {

        Icon(Icons.Filled.StackedLineChart, contentDescription = "")
    }
}

//TopAppBar(
//title = { Text(text = "Home") },
//backgroundColor = MaterialTheme.colors.primary,
//contentColor = MaterialTheme.colors.onPrimary,
//navigationIcon = {
//    IconButton(onClick = { /*TODO*/ }) {
//        Icon(imageVector = Icons.Filled.Menu, contentDescription = "toggle drawer" )
//    }
//}
//)