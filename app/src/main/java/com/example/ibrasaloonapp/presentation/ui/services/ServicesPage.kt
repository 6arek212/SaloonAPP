package com.example.ibrasaloonapp.presentation.ui.services

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.domain.model.Service
import com.example.ibrasaloonapp.presentation.components.BackButton
import com.example.ibrasaloonapp.presentation.components.CustomChip
import com.example.ibrasaloonapp.presentation.components.ServiceAddingCard
import com.example.ibrasaloonapp.presentation.components.ServiceCard
import com.example.ibrasaloonapp.presentation.theme.*


@Composable
fun ServicesSheetContent(
    services: List<Service>,
    onAddingService: (title: String, price: String) -> Unit,
    onDeleteService: (serviceId: String?, index: Int) -> Unit
) {
    var expandNew by rememberSaveable {
        mutableStateOf(false)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(
            modifier = Modifier
                .width(120.dp)
                .height(6.dp)
                .clip(MaterialTheme.shapes.large)
                .background(Gray3)
        )

        Spacer(modifier = Modifier.padding(4.dp))

        Text(text = stringResource(R.string.manage_services), style = MaterialTheme.typography.h4)

        Spacer(modifier = Modifier.padding(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            contentPadding = PaddingValues(8.dp)
        ) {
            itemsIndexed(items = services) { index, item ->

                ServiceCard(
                    text = item.title,
                    onDelete = { onDeleteService(item.id, index) },
                    price = "${item.price}₪"
                )
            }


            item {
                ServiceAddingCard(
                    text = stringResource(R.string.add_service),
                    expandNew = expandNew,
                    onAdding = onAddingService,
                    onExpandChange = { expandNew = !expandNew }
                )
            }
        }

    }


}


@Composable
@Preview
fun ServicesPagePreview() {
    AppTheme {
        ServicesSheetContent(
            services = listOf(),
            onAddingService = { title, price -> },
            onDeleteService = { serviceId, index -> })
    }
}