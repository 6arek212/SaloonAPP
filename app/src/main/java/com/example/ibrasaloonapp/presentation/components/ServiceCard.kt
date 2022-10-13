package com.example.ibrasaloonapp.presentation.components

import DropDownMenuComponent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.ibrasaloonapp.R
import com.example.ibrasaloonapp.presentation.theme.*


@Composable
fun ServiceCard(text: String, price: String, onDelete: () -> Unit) {
    Surface(elevation = 2.dp, shape = MaterialTheme.shapes.medium) {
        ConstraintLayout {
            val (box, icon) = createRefs()

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .constrainAs(box) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                    }
            ) {

                Column {
                    Text(text = text, style = MaterialTheme.typography.body2)
                    Spacer(modifier = Modifier.padding(4.dp))
                    Text(text = "${stringResource(id = R.string.price)} $price", style = MaterialTheme.typography.body2)
                }
            }

            IconButton(
                modifier = Modifier.constrainAs(icon) {
                    end.linkTo(box.end)
                    top.linkTo(box.top)
                    bottom.linkTo(box.bottom)
                },
                onClick = onDelete
            ) {
                Icon(
                    imageVector = Icons.Filled.RemoveCircle,
                    contentDescription = "delete",
                    tint = Red
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ServiceAddingCard(
    text: String,
    expandNew: Boolean = false,
    onAdding: (title: String, price: String) -> Unit,
    onExpandChange: () -> Unit
) {

    val context = LocalContext.current

    var dropDownExpand by rememberSaveable {
        mutableStateOf(false)
    }

    var ketTitle by rememberSaveable {
        mutableStateOf("")
    }

    var title by rememberSaveable {
        mutableStateOf("")
    }

    var price by rememberSaveable {
        mutableStateOf("")
    }


    Surface(elevation = 2.dp, shape = MaterialTheme.shapes.medium) {

        Column {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (!expandNew) {
                    Text(
                        text = text,
                        style = MaterialTheme.typography.body2,
                        color = Black2
                    )
                }


                IconButton(
                    modifier = Modifier,
                    onClick = onExpandChange
                ) {
                    Icon(
                        imageVector = Icons.Filled.AddCircle,
                        contentDescription = "add",
                        tint = Green
                    )
                }
            }



            AnimatedVisibility(visible = expandNew) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    Row(verticalAlignment = Alignment.CenterVertically) {

                        DropDownMenuComponent(
                            label = stringResource(id = R.string.pick_service),
                            expanded = dropDownExpand,
                            selectedOptionText = title,
                            onExpandedChange = { dropDownExpand = !dropDownExpand },
                            onDismissRequest = { dropDownExpand = false }) {
                            DropdownMenuItem(onClick = {
                                ketTitle = "Hair Cut"
                                title = context.getString(R.string.hair_cut)
                                dropDownExpand = false
                            }) {
                                Text(text = stringResource(id = R.string.hair_cut))
                            }

                            DropdownMenuItem(onClick = {
                                ketTitle = "Face Cut"
                                title = context.getString(R.string.face_cut)
                                dropDownExpand = false
                            }) {
                                Text(text = stringResource(id = R.string.face_cut))
                            }


                            DropdownMenuItem(onClick = {
                                ketTitle = "Wax"
                                title = context.getString(R.string.wax)
                                dropDownExpand = false
                            }) {
                                Text(text = stringResource(id = R.string.wax))
                            }


                            DropdownMenuItem(onClick = {
                                ketTitle = "Massage"
                                title = context.getString(R.string.massage)
                                dropDownExpand = false
                            }) {
                                Text(text = stringResource(id = R.string.massage))
                            }
                        }

                        Spacer(modifier = Modifier.padding(4.dp))

                        OutlinedTextField(
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                backgroundColor = Orange,
                                textColor = White,
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color.Transparent
                            ),
                            value = price,
                            textStyle = MaterialTheme.typography.h4,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            singleLine = true,
                            onValueChange = { s -> price = s },
                            label = {
                                Text(
                                    text = stringResource(id = R.string.price),
                                    style = MaterialTheme.typography.body2,
                                    color = Black1
                                )
                            })
                    }

                    Spacer(modifier = Modifier.padding(6.dp))

                    ProgressButton(
                        onClick = { onAdding(ketTitle, price) },
                        color = MaterialTheme.colors.primary,
                        progressColor = Gray2
                    ) {
                        Text(
                            text = stringResource(id = R.string.add),
                            style = MaterialTheme.typography.body2
                        )
                    }
                }
            }
        }

    }
}


@Composable
@Preview
fun ServiceCardPreview() {
    AppTheme {
        ServiceAddingCard(text = "Hair Cut", onAdding = { title, price -> }, onExpandChange = {})
    }
}