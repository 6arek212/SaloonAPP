package com.example.ibrasaloonapp.presentation.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.stringDateToDateFormat
import com.example.ibrasaloonapp.core.stringDateToTimeFormat
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.MenuItem
import com.example.ibrasaloonapp.presentation.components.DefaultScreenUI
import com.example.ibrasaloonapp.presentation.components.HomeAppBar
import com.example.ibrasaloonapp.presentation.components.ImageAndName
import com.example.ibrasaloonapp.presentation.components.StoryCard
import com.example.ibrasaloonapp.presentation.theme.Gray2
import com.example.ibrasaloonapp.presentation.theme.Orange
import com.example.ibrasaloonapp.presentation.theme.Red
import com.example.ibrasaloonapp.presentation.ui.Screen


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeView(navController: NavController, viewModel: HomeViewModel = hiltViewModel()) {

    val appointment = viewModel.state.value.appointment


    DefaultScreenUI(onRemoveHeadFromQueue = { /*TODO*/ }) {
        BackdropScaffold(
            scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
            frontLayerScrimColor = Color.Unspecified,
            appBar = {
                HomeAppBar()
            },
            backLayerContent = {
                backLayer() { navController.navigate(Screen.BookAppointment.route) }
            },
            frontLayerContent = {
                frontLayer(appointment = appointment)
            }
        )
    }

}


@Composable
fun backLayer(navigateToBookAppointment: () -> Unit) {

    Column() {

        ImageAndName(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp, top = 24.dp),
            firstName = "Tarik",
            lastName = "Husin"
        )

        Spacer(modifier = Modifier.padding(16.dp))

        Button(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Orange),
            shape = MaterialTheme.shapes.medium,
            onClick = { navigateToBookAppointment() }) {
            androidx.compose.material.Text(
                text = "Book Appointment",
                style = MaterialTheme.typography.h3,
                color = MaterialTheme.colors.onPrimary,
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))
    }

}

@Composable
fun frontLayer(appointment: Appointment?) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        appointment?.let {
            CurrentAppointment(appointment = appointment)
        }

        Spacer(modifier = Modifier.padding(16.dp))

        OurStaff()

        Spacer(modifier = Modifier.padding(16.dp))

        Stories()

        Spacer(modifier = Modifier.padding(16.dp))

        AboutUs()
    }

}


@Composable
fun OurStaff() {

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = "Our Great Staff",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h2
        )

        Divider(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .height(2.dp)
                .background(Gray2)
        )

        Spacer(modifier = Modifier.padding(8.dp))


        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                modifier = Modifier
                    .width(100.dp)
                    .height(100.dp)
                    .clip(CircleShape)
                    .border(1.dp, Gray2, CircleShape),
                painter = painterResource(id = R.drawable.woker1),
                contentDescription = "",
                contentScale = ContentScale.Crop
            )

            Text(text = "Ibraheem Tohme")
        }


    }


}


@Composable
fun CurrentAppointment(modifier: Modifier = Modifier, appointment: Appointment?) {
    appointment?.let {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colors.secondary)
                .padding(8.dp, 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "You have an appointment",
                color = Red,
                style = MaterialTheme.typography.h3
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                text = "${stringDateToDateFormat(appointment.startTime)} at ${
                    stringDateToTimeFormat(
                        appointment.startTime
                    )
                } for ${appointment.worker.firstName} ${appointment.worker.lastName}",
                color = MaterialTheme.colors.onSecondary,
                style = MaterialTheme.typography.body1
            )

        }

    }
}


@Composable
fun Stories() {
    val scrollState = rememberScrollState()

    Column {
        Text(
            text = "Stories",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h2
        )

        Divider(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .height(2.dp)
                .background(Gray2)
        )

        Spacer(modifier = Modifier.padding(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            StoryCard(image = R.drawable.story1)
            StoryCard(image = R.drawable.story2)
            StoryCard(image = R.drawable.story3)
            StoryCard(image = R.drawable.story4)
        }

    }
}


@Composable
fun AboutUs() {
    Column(
        modifier = Modifier
            .padding(bottom = 20.dp)
            .fillMaxWidth()
    ) {

        Text(
            text = "About Us",
            color = MaterialTheme.colors.onBackground,
            style = MaterialTheme.typography.h2
        )

        Divider(
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
                .height(2.dp)
                .background(Gray2)
        )

        Spacer(modifier = Modifier.padding(8.dp))


        Text(
            text = "This page has a unique feel, thanks to the deconstructed action figures representing the founders, Leigh Whipday and Jonny Lander.",
            style = MaterialTheme.typography.body1
        )
    }
}


@Preview(showBackground = false)
@Composable
fun HomePreview() {
//    HomeView()
}