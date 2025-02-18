package com.github.bcdxn.sibdep.graphql;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

@Controller
record PersonController(PersonService personService) {
  @QueryMapping
  List<Person> people() {
    return personService.getAllPeople();
  }

  // @SchemaMapping
  // Person bestFriend(Person person) {
  //   return personService.getPersonById(person.bestFriendId());
  // }

  @BatchMapping
  List<Person> bestFriend(List<Person> people) {
    List<Integer> ids = people
      .stream()
      .map(p -> p.bestFriendId())
      .collect(Collectors.toList());
    return personService.getPeopleById(ids);
  }
}
