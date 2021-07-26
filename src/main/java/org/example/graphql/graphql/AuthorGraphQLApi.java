package org.example.graphql.graphql;

import graphql.GraphQLException;
import io.leangen.graphql.annotations.*;
import io.leangen.graphql.spqr.spring.annotations.GraphQLApi;
import org.example.graphql.data.entity.Author;
import org.example.graphql.data.entity.Book;
import org.example.graphql.data.repository.AuthorRepository;
import org.example.graphql.data.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@GraphQLApi
@Component("graphql_AuthorService")
public class AuthorGraphQLApi {

    private final AuthorRepository authorRepository;
    private final BookRepository bookRepository;

    @Autowired
    public AuthorGraphQLApi(AuthorRepository authorRepository,
                            BookRepository bookRepository) {
        this.authorRepository = authorRepository;
        this.bookRepository = bookRepository;
    }

    @GraphQLQuery(name = "authors", description = "Authors")
    public List<Author> getAuthors() {
        return authorRepository.findAll();
    }

    @GraphQLQuery(name = "author")
    public Author getAuthor(@GraphQLArgument(name = "id", description = "Author ID") Integer id) {
        return authorRepository.getById(id);
    }

    @GraphQLQuery(name = "books")
    public List<Book> getBooks(@GraphQLContext Author author) {
        return bookRepository.findAllByAuthor(author);
    }

    @GraphQLMutation(name = "createAuthor", description = "Create author")
    //@GraphQLMutation(name = "createAuthor", description = "Create author", deprecationReason = "Use mutation addAuthor(...)")
    public Author createAuthor(@GraphQLArgument(name = "firstName", description = "First name") String firstName,
                               @GraphQLArgument(name = "lastName", description = "Last name") String lastName) {
        Author author = new Author(firstName, lastName);
        return authorRepository.save(author);
    }

    @GraphQLMutation(name = "addAuthor", description = "Add author")
    public Author addAuthor(@GraphQLNonNull @GraphQLArgument(name = "author", description = "Author") Author author) {
        return authorRepository.save(author);
    }

    @GraphQLMutation(name = "updateAuthor", description = "Update author")
    public Author updateAuthor(@GraphQLNonNull @GraphQLArgument(name = "id", description = "ID") Integer id,
                               @GraphQLArgument(name = "firstName", description = "First name") String firstName,
                               @GraphQLArgument(name = "lastName", description = "Last name") String lastName) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new GraphQLException("Author not found"));

        if (firstName != null) {
            author.setFirstName(firstName);
        }
        if (lastName != null) {
            author.setLastName(lastName);
        }

        return authorRepository.save(author);
    }

    @GraphQLMutation(name = "deleteAuthor", description = "Delete author")
    public void deleteAuthor(@GraphQLNonNull @GraphQLArgument(name = "id", description = "Author ID") Integer id) {
        Author author = authorRepository.findById(id).orElseThrow(() -> new GraphQLException("Author not found"));
        authorRepository.delete(author);
    }
}
