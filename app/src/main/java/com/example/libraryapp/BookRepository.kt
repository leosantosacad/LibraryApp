package com.example.libraryapp

class BookRepository(private val apiService: BookApiService) {

    suspend fun getAllBooks(): List<Book> {
        return apiService.getBooks()
    }

    suspend fun insert(book: Book) {
        apiService.createBook(book)
    }

    suspend fun update(book: Book) {
        book.id?.let { bookId ->
            apiService.updateBook(bookId, book)
        }
    }

    suspend fun delete(book: Book) {
        book.id?.let { bookId ->
            apiService.deleteBook(bookId)
        }
    }
}
