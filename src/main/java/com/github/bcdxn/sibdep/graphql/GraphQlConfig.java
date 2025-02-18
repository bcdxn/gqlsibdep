package com.github.bcdxn.sibdep.graphql;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import graphql.analysis.MaxQueryDepthInstrumentation;

@Configuration
public class GraphQlConfig {
  private static final Logger log = LoggerFactory.getLogger(GraphQlConfig.class);

  public GraphQlConfig() {
    log.info("configuration class successfully loaded");
  }

  @Bean
  MaxQueryDepthInstrumentation maxQueryComplexityInstrumentation() {
    return new MaxQueryDepthInstrumentation(5);
  }
}
