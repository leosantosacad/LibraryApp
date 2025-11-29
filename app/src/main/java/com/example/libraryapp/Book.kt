package com.example.libraryapp

data class Book(
    val id: String? = null,
    val imagem: String,
    val titulo: String,
    val isbn: String,
    val autor: String,
    val editora: String,
    val anoPublicacao: Int,
    val genero: String,
    val preco: String
)
