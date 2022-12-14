package com.example.ibrasaloonapp.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.fragment.app.FragmentActivity
import com.example.ibrasaloonapp.presentation.theme.AppTheme
import com.example.ibrasaloonapp.presentation.ui.Navigation
import dagger.hilt.android.AndroidEntryPoint


private const val TAG = "MainActivity"

@AndroidEntryPoint
class MainActivity : FragmentActivity() {


    val viewModel: MainActivityViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
//            val systemUiController = rememberSystemUiController()
            AppTheme {
//                systemUiController.setSystemBarsColor(
//                    color = Color.Transparent
//                )
                    Navigation( mainViewModel = viewModel)
            }
        }
    }
}

