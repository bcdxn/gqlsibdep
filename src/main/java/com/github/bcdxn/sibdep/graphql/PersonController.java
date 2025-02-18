package com.github.bcdxn.sibdep.graphql;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.dataloader.DataLoader;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Controller;

import reactor.core.publisher.Flux;

@Controller
class PersonController {

  PersonService personService;

  PersonController(
    PersonService personService,
    BatchLoaderRegistry registry
  ) {
    this.personService = personService;

    // register batch loader
    registry
      .forTypePair(Integer.class, Person.class)
      .registerBatchLoader(
        (integers, batchLoaderEnvironment) -> Flux.fromIterable(personService.getPeopleById(integers))
      );
  }

  @QueryMapping
  List<Person> people() {
    return personService.getAllPeople();
  }

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

  @SchemaMapping
  CompletableFuture<Person> bestFriend(Person person, DataLoader<Integer, Person> dataLoader) {
    return dataLoader.load(person.bestFriendId());
  }
}
