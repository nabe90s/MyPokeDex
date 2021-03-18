package com.example.mypokedex.ui.home

import androidx.annotation.MainThread
import androidx.lifecycle.*
import com.example.mypokedex.base.LiveCoroutinesViewModel
import com.example.mypokedex.model.Pokemon
import com.example.mypokedex.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeRepository: HomeRepository
): LiveCoroutinesViewModel(), LifecycleObserver {

    private val pokemonFetchingIndex: MutableStateFlow<Int> = MutableStateFlow(0)
    val pokemonListLiveData: LiveData<List<Pokemon>>

    private val _toastLiveData: MutableLiveData<String> = MutableLiveData()
    val toastLiveData: LiveData<String> get() = _toastLiveData

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> =_isLoading

    init {
        Timber.d("init HomeViewModel")

        pokemonListLiveData = pokemonFetchingIndex.asLiveData().switchMap { page ->
            _isLoading.value = true
            homeRepository.fetchPokemonList(
                page = page,
                onSuccess = { _isLoading.postValue(false) },
                onError = { _toastLiveData.postValue(it) }
            ).asLiveDataOnViewModelScope()
        }
    }

    @MainThread
    fun fetchPokemonList() = pokemonFetchingIndex.value ++

    fun onPokemonClicked(pokemon: Pokemon) {
        //TODO("Not yet implemented")
    }
}

