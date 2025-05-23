package com.example.noteminds.data.repository

import com.example.noteminds.data.api.ApiService
import com.example.noteminds.data.model.InterestRequest
import com.example.noteminds.data.room.dao.UserDao
import com.example.noteminds.Resource


class InterestsRepository(
    private val api: ApiService,
    private val userDao: UserDao
) {
    suspend fun saveInterests(
        userId: Int,
        token: String,
        interests: List<String>
    ): Resource<Boolean> {
        return try {
            // 1. Save to backend
            val response = api.saveInterests(
                token = "Bearer $token",
                request = InterestRequest(interests)
            )

            if (response.isSuccessful) {
                // 2. Save to local DB
//                userDao.insertInterests(
//                    interests.map { Interest(userId = userId, topicName = it) }
//                )
                Resource.Success(true)
            } else {
                Resource.Error("Failed to save interests")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Network error")
        }
    }

    suspend fun getInterests(userId: Int, token: String): Resource<List<String>> {
        return try {
            // Try remote first
            val response = api.getInterests("Bearer $token")
            if (response.isSuccessful) {
                response.body()?.let { it ->
                    // Update local cache
//                    userDao.insertInterests(
//                        it.interests.map { topic ->
//                            Interest(userId = userId, topicName = topic)
//                        }
//                    )
                    Resource.Success(it.interests)
                } ?: Resource.Error("Empty response")
            } else {
                // Fallback to local
                val local = userDao.getUserInterests(userId)
                if (local.isNotEmpty()) {
                    Resource.Success(local)
                } else {
                    Resource.Error("No interests found")
                }
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to fetch interests")
        }
    }
}