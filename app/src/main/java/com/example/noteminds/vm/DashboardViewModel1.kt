package com.example.noteminds.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.noteminds.Resource
import com.example.noteminds.data.repository.InterestsRepository
import com.example.noteminds.data.repository.UserRepository
import com.example.noteminds.ui.model.Task
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class DashboardViewModel1(
    private val userRepository: UserRepository,
    private val interestsRepository: InterestsRepository
) : ViewModel() {

    private val _tasks = MutableLiveData<Resource<List<Task>>>()
    val tasks: LiveData<Resource<List<Task>>> = _tasks

    fun loadTasks(userId: Int, token: String) {
        viewModelScope.launch {
            _tasks.value = Resource.Loading()
            try {
                // Try remote first
                when (val result = interestsRepository.getInterests(userId, token)) {
                    is Resource.Success -> {
                        result.data?.let { interests ->
                            createTasksFromInterests(interests)
                        } ?: run {
                            _tasks.value = Resource.Error("No interests found")
                        }
                    }
                    is Resource.Error -> {
                        // Fallback to local
                        val localInterests = userRepository.getUserInterests(userId)
                        if (localInterests.isNotEmpty()) {
                            createTasksFromInterests(localInterests)
                        } else {
                            _tasks.value = Resource.Error(result.message ?: "Error loading interests")
                        }
                    }
                    else -> {}
                }
            } catch (e: Exception) {
                _tasks.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

    private fun createTasksFromInterests(interests: List<String>) {
        val tasks = interests.mapIndexed { index, topic ->
            Task(
                id = index.toLong(),
                title = "Quiz on $topic",
                description = "Test your knowledge about $topic",
                status = getRandomStatus(index),
                topic = topic,
                isImportant = index == 0
            )
        }
        _tasks.value = Resource.Success(tasks)
    }

    private fun getRandomStatus(index: Int): Task.Status {
        return when (index % 3) {
            0 -> Task.Status.PENDING
            1 -> Task.Status.IN_PROGRESS
            else -> Task.Status.COMPLETED
        }
    }
}