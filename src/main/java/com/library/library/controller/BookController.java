package com.library.library.controller;

import com.library.library.model.Book;
import com.library.library.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {

    @Autowired
    private BookService bookService;

    // GET all books
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        List<Book> books = bookService.findAll();
        if (books.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 si pas de livres
        }
        return ResponseEntity.ok(books);
    }

    //  GET book by ID
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Long id) {
        Optional<Book> book = bookService.findById(id);
        return book.map(ResponseEntity::ok) // si présent -> 200
                .orElseGet(() -> ResponseEntity.notFound().build()); // sinon -> 404
    }

    // POST create new book
    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        if (book.getTitle() == null || book.getAuthor() == null) {
            return ResponseEntity.badRequest().build(); // 400 si info manquante
        }
        Book savedBook = bookService.save(book);
        return new ResponseEntity<>(savedBook, HttpStatus.CREATED);
    }

    //  PUT update existing book
    @PutMapping("/{id}")
    public ResponseEntity<Book> updateBook(@PathVariable Long id, @RequestBody Book bookDetails) {
        Optional<Book> optionalBook = bookService.findById(id);
        if (!optionalBook.isPresent()) {
            return ResponseEntity.notFound().build(); // 404 si pas trouvé
        }
        Book bookToUpdate = optionalBook.get();
        bookToUpdate.setTitle(bookDetails.getTitle());
        bookToUpdate.setAuthor(bookDetails.getAuthor());
        bookToUpdate.setCategory(bookDetails.getCategory());
        bookToUpdate.setAvailable(bookDetails.isAvailable());
        Book updatedBook = bookService.save(bookToUpdate);
        return ResponseEntity.ok(updatedBook);
    }

    //  DELETE book by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        Optional<Book> optionalBook = bookService.findById(id);
        if (!optionalBook.isPresent()) {
            return ResponseEntity.notFound().build(); // 404 si pas trouvé
        }
        bookService.deleteById(id);
        return ResponseEntity.noContent().build(); // 204 si supprimé
    }

}