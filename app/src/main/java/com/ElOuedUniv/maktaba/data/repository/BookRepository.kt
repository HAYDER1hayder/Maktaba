package com.ElOuedUniv.maktaba.data.repository

import com.ElOuedUniv.maktaba.data.model.Book

class BookRepository {

    private val booksList = listOf(
        Book(isbn = "978-0132350884", title = "Clean Code", nbPages = 464),
        Book(isbn = "978-0201616224", title = "The Pragmatic Programmer", nbPages = 352),
        Book(isbn = "978-0201633610", title = "Design Patterns", nbPages = 416),
        Book(isbn = "978-0134757599", title = "Refactoring", nbPages = 448),
        Book(isbn = "978-1492078005", title = "Head First Design Patterns", nbPages = 672),
        Book(isbn = "978-1950325474", title = "UIKit Apprentice", nbPages = 827),
        Book(isbn = "978-1617296147", title = "Flutter in Action", nbPages = 368),
        Book(isbn = "978-1950325825", title = "Swift Apprentice", nbPages = 384),
        Book(isbn = "978-1950325511", title = "Git Apprentice", nbPages = 172),
        Book(isbn = "978-0132764056", title = "iOS Test-Driven Development", nbPages = 	256)
    )

    fun getAllBooks(): List<Book> {

        return booksList
        //return booksList.filter { it.nbPages > 400 }
    }


    fun getBookByIsbn(isbn: String): Book? {
        return booksList.find { it.isbn == isbn }
    }
}
