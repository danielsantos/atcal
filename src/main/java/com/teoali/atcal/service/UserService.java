package com.teoali.atcal.service;

import com.teoali.atcal.config.MyUserPrincipal;
import com.teoali.atcal.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class UserService {

  public User getUser(Authentication authentication) {
    if (authentication == null) {
      User user = new User();
      user.setId(3L);
      user.setAdministrator(1);
      return user;
    }
    return ((MyUserPrincipal) authentication.getPrincipal()).getUser();
  }

}
