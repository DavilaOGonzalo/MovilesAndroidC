package com.tecsup.pokemon

import android.content.Context
import androidx.room.Room
import com.tecsup.pokemon.data.local.AppDatabase
import com.tecsup.pokemon.data.auth.FirebaseAuthRepository
import com.tecsup.pokemon.data.remote.RetrofitClient
import com.tecsup.pokemon.data.repository.PokemonRepositoryImpl
import com.tecsup.pokemon.domain.repository.PokemonRepository
import com.tecsup.pokemon.domain.repository.AuthRepository
import com.tecsup.pokemon.notification.Notificador
import com.tecsup.pokemon.notification.NotificadorAndroid

class AppContainer(context: Context) {
    private val database = Room.databaseBuilder(
        context.applicationContext,
        AppDatabase::class.java,
        "pokemon_fire.db",
    ).build()

    val notificador: Notificador = NotificadorAndroid(context.applicationContext)
    val pokemonRepository: PokemonRepository = PokemonRepositoryImpl(
        api = RetrofitClient.api,
        dao = database.pokemonDao(),
    )
    val authRepository: AuthRepository by lazy { FirebaseAuthRepository() }
}
