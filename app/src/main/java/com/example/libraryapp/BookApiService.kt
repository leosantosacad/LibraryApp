package com.example.libraryapp

import retrofit2.Response
import retrofit2.http.*

interface BookApiService {

    @GET("livros")
    suspend fun getBooks(): List<Book>

    @GET("livros/{id}")
    suspend fun getBook(@Path("id") id: String): Book

    @POST("livros")
    suspend fun createBook(@Body book: Book): Book

    @PUT("livros/{id}")
    suspend fun updateBook(@Path("id") id: String, @Body book: Book): Book

    @DELETE("livros/{id}")
    suspend fun deleteBook(@Path("id") id: String): Response<Unit>
}
