package com.github.bcdxn.sibdep.graphql;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.dataloader.DataLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Controller;

import graphql.schema.DataFetchingEnvironment;
import io.netty.util.concurrent.CompleteFuture;
import reactor.core.publisher.Flux;

@Controller
class PersonController {
  private static final Logger log = LoggerFactory.getLogger(PersonController.class);

  PersonService personService;
  PersonNicknameService nicknameService;

  PersonController(
    PersonService personService,
    PersonNicknameService nicknameService,
    BatchLoaderRegistry registry
  ) {
    this.personService = personService;
    this.nicknameService = nicknameService;

    // register batch loader
    registry
      .forTypePair(Integer.class, Person.class)
      .registerBatchLoader(
        (integers, batchLoaderEnvironment) -> Flux.fromIterable(personService.getPeopleById(integers))
      );
  }

  @QueryMapping
  Grouping grouping(DataFetchingEnvironment env) {
    env.getGraphQlContext().put("isLoading", false);
    return new Grouping();
  }

  @SchemaMapping(typeName = "Grouping", field = "people")
  // List<Person> people(Grouping source, DataLoader<Integer, Person> dataLoader) {
  List<Person> people(Grouping source, DataFetchingEnvironment env) {
    // var people = personService.listAllPeople();
    Boolean isLoading = env.getGraphQlContext().get("isLoading");
    DataLoader<Integer, Person> dataLoader = env.getDataLoaderRegistry().getDataLoader(Person.class.getName());
    
    if (isLoading == null || !isLoading) {
      log.info("completable future is not loading -- priming via grouping { people }");
      env.getGraphQlContext().put("isLoading", true);
      primePeople(dataLoader);
    } else {
      log.info("completable future is loading");
    }

    var futures = dataLoader.getCacheMap().getAll();

    CompletableFuture<Void> allDoneFuture = CompletableFuture.allOf(
      futures.toArray(new CompletableFuture[0])
    );

    return allDoneFuture.thenApply(v -> 
      futures.stream().map(CompletableFuture::join).collect(Collectors.toList())
    ).join();
  }

  // N+1
  // @SchemaMapping
  // Person bestFriend(Person person) {
  //   return personService.getPersonById(person.bestFriendId());
  // }

  // @BatchMapping
  // List<Person> bestFriend(List<Person> people) {
  //   List<Integer> ids = people
  //     .stream()
  //     .map(p -> p.bestFriendId())
  //     .collect(Collectors.toList());
  //   return personService.getPeopleById(ids);
  // }

  // @SchemaMapping
  // CompletableFuture<Person> bestFriend(Person person, DataLoader<Integer, Person> dataLoader) {
  //   return dataLoader.load(person.bestFriendId());
  // }

  @SchemaMapping
  CompletableFuture<Person> bestFriend(Person person, DataLoader<Integer, Person> dataLoader) {
    return dataLoader.load(person.bestFriendId());
  }

  @SchemaMapping(typeName = "Grouping", field = "nicknames")
  List<PersonNickname> nicknames() {
    return this.nicknameService.listAllNicknames();
  }

  @SchemaMapping
  CompletableFuture<String> fullName(PersonNickname nickname, DataFetchingEnvironment env) {
    Boolean isLoading = env.getGraphQlContext().get("isLoading");
    DataLoader<Integer, Person> dataLoader = env.getDataLoaderRegistry().getDataLoader(Person.class.getName());
    
    if (isLoading == null || !isLoading) {
      log.info("completable future is not loading - priming via nicknames { fullName }");
      env.getGraphQlContext().put("isLoading", true);
      primePeople(dataLoader);
    } else {
      log.info("completable future is loading");
    }

    return dataLoader.load(nickname.id()).thenApplyAsync(person -> {
      return person.name();
    });
  }

  private void primePeople(DataLoader<Integer, Person> dataLoader) {
    var people = personService.listAllPeople();
    people.stream().forEach(person -> {
      dataLoader.prime(person.id(), person);
    });
  }
}
