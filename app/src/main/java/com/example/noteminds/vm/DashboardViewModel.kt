package com.example.noteminds.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteminds.ui.model.Task
import com.example.noteminds.data.repository.QuizRepository
import com.example.noteminds.data.repository.UserRepository
import kotlinx.coroutines.launch

class DashboardViewModel(
    private val userRepository: UserRepository,
    private val quizRepository: QuizRepository
) : ViewModel() {
    private val _randomTopic = MutableLiveData<String?>()
    val randomTopic: LiveData<String?> = _randomTopic

    private val _task = MutableLiveData<Task?>()
    val task: LiveData<Task?> = _task

    fun generateRandomTopic(userId: Int) {
        viewModelScope.launch {
            val interests = userRepository.getUserInterests(userId)
            if (interests.isNotEmpty()) {
                _randomTopic.value = interests.random()
                _task.value = Task(
                    title = "Quiz on ${_randomTopic.value}",
                    description = "Test your knowledge!"
                )
            }
        }
    }
}