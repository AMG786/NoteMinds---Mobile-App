package com.example.noteminds.data.api

import com.example.noteminds.data.model.HistoryItem
import com.example.noteminds.data.model.InterestRequest
import com.example.noteminds.data.model.InterestResponse
import com.example.noteminds.data.model.LoginRequest
import com.example.noteminds.data.model.LoginResponse
import com.example.noteminds.data.model.PdfUploadResponse
import com.example.noteminds.data.model.QuizResponse
import com.example.noteminds.data.model.RegisterRequest
import com.example.noteminds.data.model.RegisterResponse
import com.example.noteminds.data.model.UpdateHistoryRequest
import com.example.noteminds.data.model.UpdateHistoryResponse
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query
/**
 Created by Abdul Mueez, 04/24/2025
 */
interface ApiService {

    @POST("auth/register")
    suspend fun register(
        @Body request: RegisterRequest
    ): Response<RegisterResponse>

    @POST("auth/login")
    suspend fun login(
        @Body request: LoginRequest
    ): Response<LoginResponse>

    @GET("/api/quiz")
    suspend fun getQuiz(
        @Header("Authorization") token: String,
        @Query("topic") topic: String
    ): Response<QuizResponse>

    @POST("api/user/interests")
    suspend fun saveInterests(
        @Header("Authorization") token: String,
        @Body request: InterestRequest
    ): Response<InterestResponse>

    @GET("api/user/interests")
    suspend fun getInterests(
        @Header("Authorization") token: String
    ): Response<InterestResponse>

    @POST("api/history/save")
    suspend fun saveQuizResults(
        @Header("Authorization") token: String,
        @Query("history_id") historyId: String,
        @Body questions: List<Map<String, Any>>
    ): Response<Void>

    // ApiService.kt
    @POST("api/history/update")
    suspend fun updateQuizHistory(
        @Header("Authorization") token: String,
        @Body request: UpdateHistoryRequest
    ): Response<UpdateHistoryResponse>

    // Add to ApiService.kt
    @GET("api/history1")
    suspend fun getUserHistory(
        @Header("Authorization") token: String
    ): Response<List<HistoryItem>>

    @Multipart
    @POST("api/pdf/upload1")
    suspend fun processPdf(
        @Header("Authorization") token: String,
        @Part pdf: MultipartBody.Part
    ): Response<PdfUploadResponse>
}


