package com.example.blacklotus.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Modifier
import com.example.blacklotus.ui.navigation.Navigation
import com.example.blacklotus.ui.theme.BlackLotusTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BlackLotusTheme {
                Box(modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.statusBars).background(
                    MaterialTheme.colorScheme.primary
                )){
                    Navigation()
                }
            }
        }
    }
}
