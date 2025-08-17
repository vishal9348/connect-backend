package com.vishal.user_service.service.impl;

import com.vishal.user_service.exception.UserNotFoundException;
import com.vishal.user_service.model.Users;
import com.vishal.user_service.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private IUserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        try {
            Users users =userService.findUserByEmail(username);
            Set<String> role = users.getRole();
            return new User(users.getEmail(), users.getPassword(),
                    AuthorityUtils.createAuthorityList(users.getRole()));
        }
        catch (UserNotFoundException e){
            e.printStackTrace();
            throw new UserNotFoundException("User not found with this Email - "+username);
        }
    }
}
