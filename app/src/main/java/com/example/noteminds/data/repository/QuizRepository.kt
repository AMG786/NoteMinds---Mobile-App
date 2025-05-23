package com.example.noteminds.data.repository


import com.example.noteminds.Resource
import com.example.noteminds.data.api.ApiService
import com.example.noteminds.ui.model.Question

/**
Created by Abdul Mueez, 04/24/2025
 */
class QuizRepository(private val apiService: ApiService) {



    suspend fun getQuizQuestions(token: String, topic: String): Resource<Pair<List<Question>, String>> {
        return try {
            val response = apiService.getQuiz("Bearer $token", topic)
            if (response.isSuccessful) {
                val body = response.body()
                val questions = body?.quiz?.mapIndexed { index, quizQuestion ->
                    quizQuestion.toQuestion().copy(
                        id = index.toLong(),
                        number = index + 1,
                        title = "Question ${index + 1}"
                    )
                } ?: emptyList()
                val historyId = body?.history_id ?: ""
                Resource.Success(Pair(questions, historyId))
            } else {
                Resource.Error("API Error: ${response.code()}")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Unknown error")
        }
    }


}

//
//    suspend fun getQuizQuestions(token:String,topic: String): Resource<List<Question>> {
//        return try {
//            val response = apiService.getQuiz(token,topic)
//            if (response.isSuccessful) {
//                val questions = response.body()?.quiz?.mapIndexed { index, quizQuestion ->
//                    quizQuestion.toQuestion().copy(
//                        id = index.toLong(),
//                        number = index + 1,
//                        title = "Question ${index + 1}"
//                    )
//                } ?: emptyList()
//
//
//
//                Resource.Success(questions)
//            } else {
//                Resource.Error("API Error: ${response.code()}")
//            }
//        } catch (e: Exception) {
//            Resource.Error(e.message ?: "Unknown error")
//        }
//    }