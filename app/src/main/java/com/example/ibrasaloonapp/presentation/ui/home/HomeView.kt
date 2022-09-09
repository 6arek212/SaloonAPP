package com.example.ibrasaloonapp.presentation.ui.home

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.core.stringDateToDateFormat
import com.example.ibrasaloonapp.core.stringDateToTimeFormat
import com.example.ibrasaloonapp.domain.model.Appointment
import com.example.ibrasaloonapp.domain.model.AuthData
import com.example.ibrasaloonapp.domain.model.User
import com.example.ibrasaloonapp.presentation.MainActivityViewModel
import com.example.ibrasaloonapp.presentation.components.*
import com.example.ibrasaloonapp.presentation.theme.*
import com.example.ibrasaloonapp.presentation.ui.Screen


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeView(
    navController: NavController,
    mainViewModel: MainActivityViewModel,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val appointment = viewModel.state.value.appointment
    val user = mainViewModel.state.value.authData?.user

    DefaultScreenUI(onRemoveHeadFromQueue = { /*TODO*/ }) {
        BackdropScaffold(
            scaffoldState = rememberBackdropScaffoldState(BackdropValue.Revealed),
            frontLayerScrimColor = Color.Unspecified,
            appBar = {
                HomeAppBar()
            },
            backLayerContent = {
                BackLayer(user)
            },
            frontLayerContent = {
                FrontLayer(appointment = appointment) { navController.navigate(Screen.BookAppointment.route) }
            },
            frontLayerElevation = 10.dp,
            frontLayerBackgroundColor = Gray1
        )
    }

}


@Composable
fun BackLayer(user: User?) {

    user?.let {
        Column {
            ImageName(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, top = 24.dp),
                firstName = it.firstName,
                lastName = it.lastName
            )
            Spacer(modifier = Modifier.padding(16.dp))
        }
    }

}

@Composable
fun FrontLayer(appointment: Appointment?, navigateToBookAppointment: () -> Unit) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        Appointment(
            appointment = appointment,
            navigateToBookAppointment = navigateToBookAppointment
        )


        OurStaff()

        Spacer(modifier = Modifier.padding(16.dp))

        Stories()

        Spacer(modifier = Modifier.padding(16.dp))

        AboutUs()
    }

}


@Composable
fun Appointment(
    modifier: Modifier = Modifier,
    appointment: Appointment?,
    navigateToBookAppointment: () -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .heightIn(min = 400.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Orange,
                        Orange2
                    )
                )
            )
            .padding(8.dp, 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Button(
            modifier = Modifier,
            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = Color.Transparent),
            border = BorderStroke(1.dp, Gray1),
            shape = MaterialTheme.shapes.large,
            onClick = { navigateToBookAppointment() }
        ) {
            Text(
                text = stringResource(id = R.string.book),
                style = MaterialTheme.typography.h4,
                color = Gray1,
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))


        appointment?.let {

            Text(
                text = stringResource(id = R.string.you_have_appointment),
                color = Color.White,
                style = MaterialTheme.typography.h3
            )

            Spacer(modifier = Modifier.padding(4.dp))

            Text(
                text = "${stringDateToDateFormat(appointment.startTime)} " +
                        "${stringResource(id = R.string.at)} " +
                        stringDateToTimeFormat(appointment.startTime),
                color = Color.White,
                style = MaterialTheme.typography.body1
            )

            Spacer(modifier = Modifier.padding(4.dp))


            Spacer(modifier = Modifier.padding(4.dp))

            ImageChip(
                modifier = Modifier.fillMaxWidth(),
                text = "${appointment.worker.firstName} ${appointment.worker.lastName}",
                onClick = { },
                isSelected = false,
                url = appointment.worker.image
            )
        }

        if (appointment == null) {

            Text(
                text = stringResource(id = R.string.you_dont_have_appointment),
                color = Color.White,
                style = MaterialTheme.typography.h3
            )

        }


    }
}


@Composable
fun OurStaff() {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(White)
    ) {
        SubTitle(modifier = Modifier.padding(8.dp), text = stringResource(id = R.string.our_staff))

        Spacer(modifier = Modifier.padding(8.dp))


        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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
fun Stories() {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(White)
    ) {
        SubTitle(modifier = Modifier.padding(8.dp), text = stringResource(id = R.string.stories))

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
            .fillMaxWidth()
            .heightIn(min = 300.dp)
            .clip(MaterialTheme.shapes.medium)
            .background(White)
            .padding(bottom = 20.dp)
    ) {
        SubTitle(modifier = Modifier.padding(8.dp), text = stringResource(id = R.string.about_us))


        Spacer(modifier = Modifier.padding(8.dp))


        Box(modifier = Modifier.padding(8.dp)) {
            Text(
                modifier = Modifier,
                text = stringResource(id = R.string.about_us_details),
                style = MaterialTheme.typography.body2,
                color = Black4
            )
        }
    }
}


@Preview(showBackground = false)
@Composable
fun HomePreview() {
//    HomeView()
}