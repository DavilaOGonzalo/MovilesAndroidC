package com.tecsup.lavacampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import com.tecsup.lavacampus.data.prefs.PreferenciasStore
import com.tecsup.lavacampus.ui.navigation.NavLava
import com.tecsup.lavacampus.ui.theme.LavaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val preferencias = remember { PreferenciasStore(context.applicationContext) }
            val rol by preferencias.perfilActivo.collectAsState(initial = null)
            val modoOscuro by preferencias.modoOscuro.collectAsState(initial = false)
            LavaTheme(rol = rol, darkTheme = modoOscuro) { NavLava(preferencias) }
        }
    }
}
