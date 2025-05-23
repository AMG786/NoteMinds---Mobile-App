package com.example.noteminds.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteminds.Resource
import com.example.noteminds.data.model.LoginResponse
import com.example.noteminds.data.model.RegisterResponse
import com.example.noteminds.data.repository.AuthRepository
import kotlinx.coroutines.launch

class AuthViewModel(
private val repository: AuthRepository) : ViewModel() {
    private val _registerState = MutableLiveData<Resource<RegisterResponse>>()
    val registerState: LiveData<Resource<RegisterResponse>> = _registerState

private val _loginState = MutableLiveData<Resource<LoginResponse>>()
    val loginState: LiveData<Resource<LoginResponse>> = _loginState

    fun login(usernameOrEmail: String, password: String) {
        viewModelScope.launch {
            _loginState.value = Resource.Loading()
            _loginState.value = repository.login(usernameOrEmail, password)

        }
    }
    fun register(username: String, email: String, password: String) {
        viewModelScope.launch {
            _registerState.value = Resource.Loading()
            _registerState.value = repository.register(username, email, password)
        }
    }


}