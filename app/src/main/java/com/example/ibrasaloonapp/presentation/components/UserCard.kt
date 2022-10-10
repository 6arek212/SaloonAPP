package com.example.ibrasaloonapp.presentation.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.presentation.theme.AppTheme
import com.example.ibrasaloonapp.presentation.theme.Gray1


@Composable
fun UserCard(user: User) {

    Surface(shape = MaterialTheme.shapes.medium, elevation = 2.dp, color = Gray1) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            CircularImage(
                modifier = Modifier.size(30.dp),
                url = user.image,
                onClick = {})

            Spacer(modifier = Modifier.padding(8.dp))

            Text(
                text = "${user.firstName} ${user.lastName}",
                style = MaterialTheme.typography.body2,
                color = MaterialTheme.colors.onBackground
            )


            Spacer(modifier = Modifier.weight(1f))

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(id = R.drawable.edit),
                    contentDescription = "edit"
                )
            }


        }

    }
}


@Composable
@Preview
fun UserCardPreview() {
    AppTheme {
        UserCard(
            User(
                id = "",
                firstName = "tarik",
                lastName = "Husin",
                phone = "0525145565",
                role = "barber"
            )
        )
    }
}