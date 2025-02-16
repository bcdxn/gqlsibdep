package com.github.bcdxn.sibdep.graphql;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public record Library(String id) {
  private static String _id = UUID.randomUUID().toString();
  public static CompletableFuture<Library> getLibrary() {
    return CompletableFuture.supplyAsync(() -> {
      return new Library(_id);
    });
  }
}
