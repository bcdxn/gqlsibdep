package com.github.bcdxn.sibdep.graphql;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
public class LibraryController {
  @QueryMapping(name = "library")
  public CompletableFuture<Library> library() {
    return Library.getLibrary();
  }

  @SchemaMapping
  public CompletableFuture<List<Book>> books(Library library) {
    return Book.getBooks();
  }

  @SchemaMapping
  public CompletableFuture<List<Author>> authors(Library library) {
    return Author.getAuthors();
  }

  @SchemaMapping
  public CompletableFuture<Author> author(Book book) {
    return Author.getById(book.authorId());
  }
}
