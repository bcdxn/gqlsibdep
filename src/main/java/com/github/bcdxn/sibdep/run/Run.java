package com.github.bcdxn.sibdep.run;

import java.time.LocalDateTime;

public record Run(
  Integer id, 
  String title,
  LocalDateTime startedOn,
  LocalDateTime completedOn,
  Integer miles,
  Location location
) {}
