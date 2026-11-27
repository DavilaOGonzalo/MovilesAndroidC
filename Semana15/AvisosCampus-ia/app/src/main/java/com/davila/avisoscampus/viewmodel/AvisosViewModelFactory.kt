package com.davila.avisoscampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.davila.avisoscampus.data.AvisosRepository

class AvisosViewModelFactory(private val repo: AvisosRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(AvisosViewModel::class.java))
        return AvisosViewModel(repo) as T
    }
}
