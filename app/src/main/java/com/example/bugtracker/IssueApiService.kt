package com.example.bugtracker

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface IssueApiService {
    @GET("issues")
    suspend fun getIssues(): List<IssueTicket>

    @POST("issues")
    suspend fun createIssue(@Body issue: IssueTicket): IssueTicket

    @PUT("issues/{id}")
    suspend fun updateIssue(@Path("id") id: Long, @Body issue: IssueTicket): IssueTicket

    @DELETE("issues/{id}")
    suspend fun deleteIssue(@Path("id") id: Long)
}