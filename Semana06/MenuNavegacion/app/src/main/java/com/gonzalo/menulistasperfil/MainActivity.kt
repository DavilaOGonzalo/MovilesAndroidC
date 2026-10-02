package com.gonzalo.menulistasperfil

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gonzalo.menulistasperfil.navigation.AppNavigation
import com.gonzalo.menulistasperfil.ui.theme.MenuListasPerfilTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MenuListasPerfilTheme {
                AppNavigation()
            }
        }
    }
}
