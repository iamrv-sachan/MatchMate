package com.example.matchmate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.domain.model.User
import com.example.domain.repoImpl.UserRepository
import com.example.domain.usecase.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    private val networkMonitor: NetworkMonitor
) : ViewModel() {

    private val _homeUsers = MutableStateFlow<UiState<List<User>>>(UiState.Loading)
    val homeUsers: StateFlow<UiState<List<User>>> = _homeUsers

    private val _profileMatches = MutableStateFlow<UiState<Flow<PagingData<User>>>>(UiState.Loading)
    val profileMatches: StateFlow<UiState<Flow<PagingData<User>>>> = _profileMatches

    private val _isConnected = MutableStateFlow(true)
    val isConnected: StateFlow<Boolean> = _isConnected

    init {
        observeNetworkChanges()
    }

    fun fetchAllUsers() {
        viewModelScope.launch {
            repository.fetchUsersFromApi()
            repository.getAllUsers()
                .catch {
                    _homeUsers.value = UiState.Error(it.toString())
                }
                .collect {
                    _homeUsers.value = UiState.Success(it)
                }
        }
    }

    fun refreshProfileMatches() {
        viewModelScope.launch {
            _profileMatches.value = UiState.Loading
            try {
                val pagingFlow = repository.getProfileMatchesUsers()
                    .cachedIn(viewModelScope)
                _profileMatches.value = UiState.Success(pagingFlow)
            } catch (e: Exception) {
                _profileMatches.value = UiState.Error(e.localizedMessage ?: "Unknown error")
            }
        }
    }

    fun acceptMatch(user: User) {
        viewModelScope.launch {
            repository.acceptMatch(user)
        }
    }

    fun declineMatch(user: User) {
        viewModelScope.launch {
            repository.declineMatch(user)
        }
    }

    private fun observeNetworkChanges() {
        viewModelScope.launch {
            networkMonitor.isConnected.collect { connected ->
                fetchAllUsers()
            }
        }
    }
}

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}