package com.github.bcdxn.sibdep.graphql;

import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;

import reactor.core.publisher.Mono;

@Component
class MyInterceptor implements WebGraphQlInterceptor {
  @SuppressWarnings("null")
  @Override
  public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
    return chain.next(request)
        .map(response -> {
          response.getResponseHeaders().add("special-header", "true");
          return response;
        });
  }

}
