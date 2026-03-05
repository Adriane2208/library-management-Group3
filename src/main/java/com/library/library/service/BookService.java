package com.library.library.service;

import com.library.library.model.Book;

import java.util.List;
import java.util.Optional;

public interface BookService {

    List<Book> findAll();                // Récupérer tous les livres
    Optional<Book> findById(Long id);    // Récupérer un livre par ID
    Book save(Book book);                // Créer ou mettre à jour un livre
    void deleteById(Long id);           // Supprimer un livre par ID
}