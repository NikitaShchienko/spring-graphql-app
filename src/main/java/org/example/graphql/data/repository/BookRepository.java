package org.example.graphql.data.repository;

import org.example.graphql.data.entity.Author;
import org.example.graphql.data.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {

    List<Book> findAllByAuthor(Author author);

    List<Book> findBooksByNameLike(String name);
}