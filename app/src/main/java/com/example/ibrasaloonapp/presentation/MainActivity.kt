package com.example.ibrasaloonapp.presentation

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Box
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.BookOnline
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.example.ibrasaloonapp.domain.model.MenuItem
import com.example.ibrasaloonapp.presentation.theme.AppTheme
import com.example.ibrasaloonapp.presentation.ui.Navigation
import com.example.ibrasaloonapp.presentation.ui.Screen
import com.example.ibrasaloonapp.presentation.ui.home.DrawerBody
import com.example.ibrasaloonapp.presentation.ui.home.DrawerHeader
import com.example.ibrasaloonapp.presentation.ui.login.LoginViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


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

                Navigation(viewModel)
            }
        }
    }
}

