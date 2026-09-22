package com.viltrumites.khuluma

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.viltrumites.khuluma.ui.navigation.KhulumaApp
import com.viltrumites.khuluma.ui.theme.KhulumaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent { KhulumaTheme { KhulumaApp() } }
    }
}
