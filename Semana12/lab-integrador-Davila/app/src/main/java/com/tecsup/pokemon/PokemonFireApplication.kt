package com.tecsup.pokemon

import android.app.Application

class PokemonFireApplication : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
