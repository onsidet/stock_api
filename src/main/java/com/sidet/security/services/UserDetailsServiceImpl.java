package com.sidet.security.services;
import com.sidet.entity.User;
import com.sidet.repository.UserRepository;
import com.sidet.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository;

  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user;
    Optional<User> userName = userRepository.getByUsernameAndStatus(username, Constants.ACTIVE_STATUS);
    if(userName.isPresent()){
      user = userName.get();
    }else{
      user = userRepository.getByPhoneAndStatus(username, Constants.ACTIVE_STATUS)
              .orElseThrow(() -> new UsernameNotFoundException("User Not Found with phone: " + username));
    }

    return UserDetailsImpl.build(user);
  }

}
