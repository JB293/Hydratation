package com.example.hydroflow

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.hydroflow.ui.theme.HydrationScreen
import com.example.hydroflow.ui.theme.HydrationTheme
import com.example.hydroflow.ui.theme.HydrationViewModel

class MainActivity : ComponentActivity() {

    // Initialisation du ViewModel via le délégué Kotlin viewModels()
    private val viewModel: HydrationViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            // Application du thème sombre Material 3 personnalisé
            HydrationTheme(darkTheme = true) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    HydrationScreen(viewModel = viewModel)
                }
            }
        }
    }
}