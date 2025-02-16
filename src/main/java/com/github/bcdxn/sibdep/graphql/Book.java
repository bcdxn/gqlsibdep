package com.github.bcdxn.sibdep.graphql;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public record Book(
  String id,
  String title,
  String authorId
) {
  private static List<Book> books = Arrays.asList(
    new Book("book1", "The Adventures of Tom Sawyer", "author1"),
    new Book("book2", "A Christmas Carol", "author2"),
    new Book("book3", "Jane Eyre", "author3")
  );

  public static CompletableFuture<List<Book>> getBooks() {
    return CompletableFuture.supplyAsync(() -> {
      return Book.books;
    });
  }

  public static Book getById(String id) {
    return books.stream()
      .filter(book -> book.id().equals(id))
      .findFirst()
      .orElse(null);  
  }
}
