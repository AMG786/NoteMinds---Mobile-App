package com.example.noteminds.data.repository

import com.example.noteminds.Resource
import com.example.noteminds.data.api.ApiService
import com.example.noteminds.data.model.LoginRequest
import com.example.noteminds.data.model.LoginResponse
import com.example.noteminds.data.model.RegisterRequest
import com.example.noteminds.data.model.RegisterResponse
import com.example.noteminds.data.room.dao.UserDao

class AuthRepository(
    private val authApi: ApiService,
    private val userDao: UserDao? = null
) {
    suspend fun register(
        username: String,
        email: String,
        password: String
    ): Resource<RegisterResponse> {
        return try {
            val response = authApi.register(RegisterRequest(username, email, password))

            if (response.isSuccessful) {
                response.body()?.let { registerResponse ->
                    // Optional local caching
//                    userDao?.insertUser(
//                        User(
//                            id = registerResponse.userId.toInt(),
//                            username = username,
//                            email = email,
//                            password = "", // Never store actual password
//                            phone = null
//                        )
//                    )
                    Resource.Success(registerResponse)
                } ?: Resource.Error("Empty response")
            } else {
                Resource.Error(response.message())
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Unknown error")
        }
    }

    suspend fun login(usernameOrEmail: String, password: String): Resource<LoginResponse> {
        return try {

            val response = authApi.login(LoginRequest(usernameOrEmail, password))

            if (response.isSuccessful) {
                response.body()?.let { loginResponse ->
                    // 2. Save user locally (optional)
//                    userDao?.insertUser(
//                        User(
//                            id = loginResponse.userId.toInt(),
//                            username = loginResponse.username,
//                            email = "", // Email not returned in login response
//                            password = "", // Never store actual password
//                            phone = null
//                        )
//                    )
                    Resource.Success(loginResponse)
                } ?: Resource.Error("Empty response")

            } else {
                // 3. Fallback to Room DB if needed
                val localUser = userDao?.getUser(usernameOrEmail, password)
                if (localUser != null) {
                    Resource.Success(
                        LoginResponse(
                            token = "local_token",
                            userId = localUser.id.toString(),
                            username = localUser.username
                        )
                    )
                } else {
                    Resource.Error(response.message())
                }
            }
        } catch (e: Exception) {
            Resource.Error(e.localizedMessage ?: "Login failed")
        }
    }


}

//    suspend fun login(
//        usernameOrEmail: String,
//        password: String
//    ): Resource<LoginResponse> {
//        return try {
//            val response = authApi.login(LoginRequest(usernameOrEmail, password))
//
//            if (response.isSuccessful) {
//                response.body()?.let { loginResponse: LoginResponse ->
//
//                Resource.Success(loginResponse)
//
//                    // Update local user cache if needed
////                    userDao?.updateUser(
////                        User(
////                            id = loginResponse.userId.toInt(),
////                            username = loginResponse.username,
////                            email = if (usernameOrEmail.contains("@")) usernameOrEmail else "",
////                            password = "",
////                            phone = null
////                        )
////                    )
////                    Resource.Success(loginResponse)
//                } ?: Resource.Error("Empty response")
//            } else {
//                Resource.Error(response.message())
//            }
//        } catch (e: Exception) {
//            Resource.Error(e.localizedMessage ?: "Unknown error")
//        }
//    }