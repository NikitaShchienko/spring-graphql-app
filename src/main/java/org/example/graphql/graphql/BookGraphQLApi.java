package org.example.graphql.graphql;

import graphql.GraphQLException;
import io.leangen.graphql.annotations.*;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import io.leangen.graphql.spqr.spring.util.ConcurrentMultiMap;
import org.example.graphql.data.entity.Author;
import org.example.graphql.data.entity.Book;
import org.example.graphql.data.entity.BookType;
import org.example.graphql.data.repository.AuthorRepository;
import org.example.graphql.data.repository.BookRepository;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;

import java.util.List;

@Component("graphql_BookGraphQLApi")
@GraphQLApi
public class BookGraphQLApi {

    private final ConcurrentMultiMap<Integer, FluxSink<Book>> subscribers = new ConcurrentMultiMap<>();
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Autowired
    public BookGraphQLApi(BookRepository bookRepository, AuthorRepository authorRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
    }

    @GraphQLQuery(name = "books")
    public List<Book> getBooks() {
        return bookRepository.findAll();
    }

    @GraphQLQuery(name = "books")
    public List<Book> getBooks(@GraphQLArgument(name = "Filter", description = "Filter by name") String filter) {
        return bookRepository.findBooksByNameLike("%" + filter + "%");
    }

    @GraphQLQuery(name = "book")
    public Book getBook(@GraphQLArgument(name = "id", description = "Book ID") Integer id) {
        return bookRepository.getById(id);
    }

    @GraphQLMutation(name = "createBook")
    public Book createBook(@GraphQLNonNull @GraphQLArgument(name = "name", description = "Book name") String name,
                           @GraphQLNonNull @GraphQLArgument(name = "authorId", description = "Author ID") Integer authorId,
                           @GraphQLNonNull @GraphQLArgument(name = "type", description = "Author ID") BookType bookType) {
        Author author = authorRepository.getById(authorId);
        return bookRepository.save(new Book(name, author, bookType));
    }

    @GraphQLMutation(name = "deleteBook")
    public void deleteAuthor(@GraphQLNonNull @GraphQLArgument(name = "id", description = "Book ID") Integer id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new GraphQLException("Book not found"));
        bookRepository.delete(book);
    }

    @GraphQLMutation(name = "updateBook")
    public Book updateBook(@GraphQLNonNull @GraphQLArgument(name = "id", description = "Book ID") Integer id,
                           @GraphQLNonNull @GraphQLArgument(name = "name", description = "Book name") String name) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new GraphQLException("Book not found"));
        book.setName(name);

        subscribers.get(id).forEach(subscriber -> subscriber.next(book));

        return bookRepository.save(book);
    }

    @GraphQLSubscription(name = "subscriptionOnBook")
    public Publisher<Book> bookStatusChanged(@GraphQLArgument(name = "id", description = "ID") Integer id) {
        System.out.println("");
        return Flux.create(subscriber ->
                        subscribers.add(id, subscriber.onDispose(() -> subscribers.remove(id, subscriber))),
                FluxSink.OverflowStrategy.LATEST);
    }
}
