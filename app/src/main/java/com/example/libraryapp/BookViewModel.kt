package com.example.libraryapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class BookViewModel(private val repository: BookRepository) : ViewModel() {

    private val _allBooks = MutableLiveData<List<Book>>()
    val allBooks: LiveData<List<Book>> = _allBooks

    init {
        fetchBooks()
    }

    private fun fetchBooks() {
        viewModelScope.launch {
            try {
                _allBooks.value = repository.getAllBooks()
            } catch (e: Exception) {
                // Handle error
            }
        }
    }

    fun addBook(
        imagem: String, titulo: String, isbn: String, autor: String, editora: String,
        anoPublicacao: String, genero: String, preco: String
    ) {
        viewModelScope.launch {
            val anoPublicacaoInt = anoPublicacao.toIntOrNull() ?: 0
            val book = Book(
                imagem = imagem, titulo = titulo, isbn = isbn, autor = autor,
                editora = editora, anoPublicacao = anoPublicacaoInt, genero = genero, preco = preco
            )
            repository.insert(book)
            fetchBooks() // Refresh list
        }
    }

    fun updateBook(book: Book) {
        viewModelScope.launch {
            repository.update(book)
            fetchBooks() // Refresh list
        }
    }

    fun deleteBook(book: Book) {
        viewModelScope.launch {
            repository.delete(book)
            fetchBooks() // Refresh list
        }
    }
}

class BookViewModelFactory(private val repository: BookRepository) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BookViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BookViewModel(repository) as T
        } else {
            throw IllegalArgumentException("Unknown View Model class")
        }
    }
}