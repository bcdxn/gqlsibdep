package com.github.bcdxn.sibdep.graphql;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public record Author(
  String id,
  String name
) {
  private static final Logger log = LoggerFactory.getLogger(Author.class);

  private static List<Author> authors = Arrays.asList(
    new Author("author1", "Mark Twain"),
    new Author("author2", "Charles Dickens"),
    new Author("author3", "Charlotte Bronte")
  );

  public static CompletableFuture<List<Author>> getAuthors() {
    log.info("::::::::::: getAuthors was called! :::::::::::");
    return CompletableFuture.supplyAsync(() -> {
      return Author.authors;
    });
  }

  public static CompletableFuture<Author> getById(String id) {
    return getAuthors().thenApplyAsync(authors ->
        authors.stream()
            .filter(author -> author.id().equals(id))
            .findFirst()
            .orElse(null) // Returns null if no author is found
    );
  }
}
