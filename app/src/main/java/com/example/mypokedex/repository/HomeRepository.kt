package com.example.mypokedex.repository

import androidx.annotation.WorkerThread
import com.example.mypokedex.model.mapper.ErrorResponseMapper
import com.example.mypokedex.network.PokedexClient
import com.skydoves.sandwich.map
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onException
import com.skydoves.sandwich.suspendOnSuccess
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class HomeRepository @Inject constructor(
    private val pokedexClient: PokedexClient
) {

    @WorkerThread
    fun fetchPokemonList(
        page: Int,
        onSuccess: () -> Unit,
        onError: (String?) -> Unit
    ) = flow {
        val response = pokedexClient.fetchPokemonList(page)
        response.suspendOnSuccess {
            data?.let {
                val pokemons = it.results
                pokemons.forEach { p -> p.page = page }
                emit(pokemons)
                onSuccess()
            }
        }.onError {
            map(ErrorResponseMapper) {
                onError("[Code: $code]: $message")
            }
        }.onException {
            onError(message)
        }
    }.flowOn(Dispatchers.IO)
}