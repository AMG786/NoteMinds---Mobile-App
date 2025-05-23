package com.example.noteminds.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteminds.ui.model.Question
import com.example.noteminds.Resource
import com.example.noteminds.data.api.ApiService
import com.example.noteminds.data.api.RetrofitInstance
import com.example.noteminds.data.model.UpdateHistoryRequest
import com.example.noteminds.data.repository.QuizRepository
import kotlinx.coroutines.launch

class QuizViewModel(private val repository: QuizRepository) : ViewModel() {
    private val apiService: ApiService= RetrofitInstance.api
    private val _quizState = MutableLiveData<Resource<List<Question>>>()
    val quizState: LiveData<Resource<List<Question>>> = _quizState

    private val _historyId = MutableLiveData<String>()
    val historyId: LiveData<String> = _historyId

    private val _navigateToResults = MutableLiveData<Boolean>()
    val navigateToResults: LiveData<Boolean> = _navigateToResults

    private val _submitState = MutableLiveData<Resource<Boolean>>()
    val submitState: LiveData<Resource<Boolean>> = _submitState

    private val _quizScore = MutableLiveData<Pair<Int, Int>>()
    val quizScore: LiveData<Pair<Int, Int>> = _quizScore

//    fun fetchQuiz(token:String,topic: String) {
//        _quizState.value = Resource.Loading()
//        viewModelScope.launch {
//            _quizState.value = repository.getQuizQuestions("Bearer $token",topic)
//        }
//    }
fun fetchQuiz(token: String, topic: String) {
    _quizState.value = Resource.Loading()
    viewModelScope.launch {
        when (val result = repository.getQuizQuestions(token, topic)) {
            is Resource.Success -> {
                _quizState.value = result.data?.let { Resource.Success(it.first) }
                _historyId.value = result.data?.second
            }
            is Resource.Error -> {
                _quizState.value = Resource.Error(result.message ?: "Unknown error")
            }
            else -> Unit
        }
    }
}
    fun submitQuiz(questions: List<Question>) {
        // Calculate score or process answers

        _navigateToResults.value = true
    }

    fun submitQuiz(historyId: String, questions: List<Question>, token: String) {
        viewModelScope.launch {
            _submitState.value = Resource.Loading()
            try {
                // Extract user answers (full text of selected options)
                val userAnswers = questions.map { question ->
                    question.options[question.selectedOption]
                }

                val response = apiService.updateQuizHistory(
                    token = "Bearer $token",
                    request = UpdateHistoryRequest(
                        history_id = historyId,
                        answers = userAnswers
                    )
                )

                if (response.isSuccessful) {
                    response.body()?.let {
                        _submitState.value = Resource.Success(true)
                        _quizScore.value = it.score to it.total_questions
                        _navigateToResults.value = true
                    } ?: run {
                        _submitState.value = Resource.Error("Empty response")
                    }
                } else {
                    _submitState.value = Resource.Error(response.message())
                }
            } catch (e: Exception) {
                _submitState.value = Resource.Error(e.message ?: "Unknown error")
            }
        }
    }

    fun doneNavigating() {
        _navigateToResults.value = false
    }
}