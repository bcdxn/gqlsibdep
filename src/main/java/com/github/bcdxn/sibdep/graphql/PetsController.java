package com.github.bcdxn.sibdep.graphql;

import java.util.List;

import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Controller
public class PetsController {

  @QueryMapping
  List<Pet> pets() {
    return List.of(
      new Pet("Luna", "cappuccino"),
      new Pet("Skipper", "black")
    );
  }
}
