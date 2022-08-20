package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.domain.model.Session


@Composable
fun SessionCard(
    session: Session,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.heightIn(min = 100.dp),
        elevation = 10.dp,
        color = MaterialTheme.colors.surface,
        shape = MaterialTheme.shapes.large
    ) {


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                session.type?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.onSurface
                    )
                }

                Image(
                    modifier = Modifier.widthIn(max = 20.dp),
                    painter = painterResource(id = R.drawable.tools1),
                    contentDescription = "",
                )

            }

            Spacer(modifier = Modifier.padding(16.dp))

            session.date?.let {
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text = it,
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.onSurface
                )
            }
        }


    }
}