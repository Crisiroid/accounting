package com.crisiroid.accounting.api


import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST


data class RegisterRequest(
    val fullName: String,
    val ssn: String,
    val username: String,
    val password: String
)
data class RegisterResponse(val status: String, val code: Int)
data class LoginRequest(val name: String, val password: String)
data class LoginResponse(val status: String, val code: Int) // Adjust based on actual API response

interface ApiService {
    @POST("/m1/918173-900658-default/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @POST("/m1/918173-900658-default/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
}