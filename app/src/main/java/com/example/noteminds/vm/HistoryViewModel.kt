package com.example.noteminds.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteminds.Resource
import com.example.noteminds.data.api.ApiService
import com.example.noteminds.data.model.HistoryItem
import kotlinx.coroutines.launch


// Extension function to convert API models
class HistoryViewModel(private val apiService: ApiService) : ViewModel() {
    private val _historyState = MutableLiveData<Resource<List<HistoryItem>>>()
    val historyState: LiveData<Resource<List<HistoryItem>>> = _historyState

    fun fetchUserHistory(token: String) {
        viewModelScope.launch {
            _historyState.value = Resource.Loading()
            try {
                val response = apiService.getUserHistory("Bearer $token")
                if (response.isSuccessful) {
                    response.body()?.let {
                        println("API Response: $it") // Add logging
                        _historyState.value = Resource.Success(it)
                    } ?: run {
                        println("Empty response body")
                        _historyState.value = Resource.Error("Empty response")
                    }
                } else {
                    println("API Error: ${response.code()} - ${response.message()}")
                    _historyState.value = Resource.Error("API error: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Exception: ${e.message}")
                e.printStackTrace()
                _historyState.value = Resource.Error("Network error: ${e.message}")
            }
        }
    }
}