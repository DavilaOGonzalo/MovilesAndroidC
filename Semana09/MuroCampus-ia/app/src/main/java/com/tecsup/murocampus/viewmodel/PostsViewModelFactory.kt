package com.tecsup.murocampus.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

inline fun <reified T : ViewModel> postsViewModelFactory(
    crossinline crear: () -> T,
): ViewModelProvider.Factory = viewModelFactory {
    initializer { crear() }
}
