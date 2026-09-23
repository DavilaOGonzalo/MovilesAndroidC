package com.gonzalo.hospitaltecsup_ia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.gonzalo.hospitaltecsup_ia.navigation.AppNavigation
import com.gonzalo.hospitaltecsup_ia.ui.theme.HospitalTecsupmioTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            HospitalTecsupmioTheme {
                AppNavigation()
            }
        }
    }
}