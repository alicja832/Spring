package com.example.pythonapp.details;

import com.example.pythonapp.model.UserEntity;
import com.example.pythonapp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import com.example.pythonapp.exception.UserNotFoundException;

@Service
public class AppUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) {
        UserEntity user = userRepository.findByName(username).orElseThrow(UserNotFoundException::new);
        return new AppUserDetails(user);
    }
}
