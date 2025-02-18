package com.github.bcdxn.sibdep.graphql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PersonService {
  private static final Logger log = LoggerFactory.getLogger(PersonService.class);

  static final Map<Integer, Person> people = Map.of(
    1, new Person(1, "p1", 2),
    2, new Person(2, "p2", 3),
    3, new Person(3, "p3", 1)
  );

  Map<Integer, Person> allPeople() {
    log.info("calling allPeople()");
    return people;
  }

  List<Person> getAllPeople() {
    return new ArrayList<Person>(this.allPeople().values());
  }

  Person getPersonById(Integer id) {
    return this.allPeople().get(id);
  }

  List<Person> getPeopleById(List<Integer> ids) {
    var filteredList = new ArrayList<Person>();
    var allPeople = this.allPeople();

    for (Integer i = 0; i < ids.size(); i++) {
      filteredList.add(allPeople.get(ids.get(i)));
    }

    return filteredList;
  }
}
