package com.davila.avisoscampus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import com.davila.avisoscampus.data.AvisosRepositoryMemoria
import com.davila.avisoscampus.ui.PantallaAvisos
import com.davila.avisoscampus.ui.theme.AvisosCampusTheme
import com.davila.avisoscampus.viewmodel.AvisosViewModel
import com.davila.avisoscampus.viewmodel.AvisosViewModelFactory

class MainActivity : ComponentActivity() {
    private val repository by lazy { AvisosRepositoryMemoria() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AvisosCampusTheme {
                val vm: AvisosViewModel = viewModel(factory = AvisosViewModelFactory(repository))
                PantallaAvisos(vm)
            }
        }
    }
}
