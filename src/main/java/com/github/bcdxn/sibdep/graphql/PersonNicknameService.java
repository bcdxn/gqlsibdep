package com.github.bcdxn.sibdep.graphql;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class PersonNicknameService {
  private static final Logger log = LoggerFactory.getLogger(PersonNicknameService.class);

  static final Map<Integer, PersonNickname> nicknames = Map.of(
    1, new PersonNickname(1, "nickname:p1"),
    2, new PersonNickname(2, "nickname:p2"),
    3, new PersonNickname(3, "nickname:p3")
  );

  Map<Integer, PersonNickname> allNicknames() {
    log.info("calling allNicknames()");
    return nicknames;
  }

  List<PersonNickname> listAllNicknames() {
    return new ArrayList<PersonNickname>(this.allNicknames().values());
  }
}
