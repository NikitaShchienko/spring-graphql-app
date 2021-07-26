# Spring GraphQL
Spring boot application with GraphQL

## Query

Simple query:
```js
query getAuthors {
    authors {
        id
        lastName
        firstName
    }
}

query getBooks {
    books {
        name
    }
}
```

Query with arguments:
```js
query getBook {
  book(id: 1) {
    id
    name
  }
}
```

## Variables

Query with variables:
```json
query getBooks($filter: String = "Кап") {
  books(Filter: $filter) {
    id
    name
  }
}

# Variable
{
  "filter": "У"
}
```

## Alias

Query with alias:
```js
query getBook {
  firstBook: book(id: 1) {
    id
    name
  }
  secondBook: book(id: 2) {
    id
    name
  }
}
```

## Mutation

Example:
```js
mutation delete {
   deleteAuthor(id: 1)
 }

 mutation create {
   createAuthor(
     lastName: "Last name"
     firstName: "First name") {
      id
      firstName
   }
 }

mutaion update {
	updateAuthor(
		id: 1
		lastName: "Last name") {
		  id
          firstName
		  lastName
	}
}
```

## Fragment 

Query with fragment:
```js

fragment nameFields on Author {
	firstName
	lastName
}

query getAllAuthors {
  authors {
    id
    ...nameFields
  }
}

```


## Input type

Mutation with AuthorInput type:

```js
mutation addAuthor {
  addAuthor(author: {
    lastName: "Test 1"
    firstName: "Test 2"
  }) {
    id
    lastName
    firstName
  }
}
```

## Directive

Directive `@include`

```js
query getAuthors($withBooks: Boolean = false) {
  authors {
    id
    lastName
    firstName
    books @include(if: $withBooks) {
      name
    }
  }
}

# Variables
{
  "withBooks": true
}
```

Directive `@skip`

```js
query getAuthors($withoutBooks: Boolean = true) {
  authors {
    id
    lastName
    firstName
    books @skip(if: $withoutBooks) {
      name
    }
  }
}

# Variables
{
  "$withoutBooks": false
}
```

# SPQR

GraphQL SPQR (GraphQL Schema Publisher & Query Resolver, pronounced like speaker) is a simple-to-use library for rapid development of GraphQL APIs in Java.

Link: https://github.com/leangen/graphql-spqr-spring-boot-starter

## Annotation

@GraphQLApi

Example:

```java
@Component("graphql_BookGraphQLApi")
@GraphQLApi
public class BookGraphQLApi {
...
}
```

@GraphQLEnumValue

Example: 
```java
@GraphQLType(name = "BookType", description = "Book type")
public enum BookType {

    @GraphQLEnumValue(name = "Novel", description = "Novel") NOVEL
}
```

@GraphQLArgument

Example:
```java
    @GraphQLQuery(name = "book")
    public Book getBook(@GraphQLArgument(name = "id", description = "Book ID") Integer id) {
        return bookRepository.getById(id);
    }
```

@GraphQLMutation

```java
    @GraphQLMutation(name = "createAuthor")
    public Author createAuthor(@GraphQLArgument(name = "firstName", description = "First name") String firstName,
                               @GraphQLArgument(name = "lastName", description = "Last name") String lastName) {
        Author author = new Author(firstName, lastName);
        return authorRepository.save(author);
    }
```

@GraphQLNonNull

```java
    @GraphQLMutation(name = "updateBook")
    public Book updateBook(@GraphQLNonNull @GraphQLArgument(name = "id", description = "Book ID") Integer id,
                           @GraphQLNonNull @GraphQLArgument(name = "name", description = "Book name") String name) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new GraphQLException("Book not found"));
        book.setName(name);

        subscribers.get(id).forEach(subscriber -> subscriber.next(book));

        return bookRepository.save(book);
    }
```

## Links

GraphQL Java: https://www.graphql-java.com/documentation/master/
