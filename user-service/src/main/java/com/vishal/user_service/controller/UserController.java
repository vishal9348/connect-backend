package com.vishal.user_service.controller;

import com.vishal.user_service.exception.UserNotFountException;
import com.vishal.user_service.model.Users;
import com.vishal.user_service.payload.request.LoginRequest;
import com.vishal.user_service.security.JwtUtils;
import com.vishal.user_service.service.IUserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    @PreAuthorize(value = "hasAnyRole('ADMIN','ANONYMOUS')")
    @PostMapping()
    public ResponseEntity<Users> createUser(@RequestBody @Valid Users users){
        Users savedUsers = userService.createUser(users);
        return new ResponseEntity<>(savedUsers, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<Users> getUserById(@PathVariable UUID userId) throws UserNotFountException{
        Users users = userService.findUserById(userId);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Users>> findAllUser(){
        List<Users> users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("/email")
    public ResponseEntity<Users> findUserByEmail(@RequestParam String email) throws UserNotFountException{
        Users usersByEmail = userService.findUserByEmail(email);
        return new ResponseEntity<>(usersByEmail, HttpStatus.OK);
    }

    @PatchMapping("/{userId}")
    public ResponseEntity<Users> updateUser(@RequestBody Users users, @PathVariable UUID userId) throws UserNotFountException{
        Users updatedUsers = userService.updateUser(users, userId);
        return new ResponseEntity<>(updatedUsers, HttpStatus.OK);
    }

    @PostMapping("/authentication")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequest request) {
        Map<String, String> resp = new HashMap<>();

        try {
            Authentication auth = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
            auth = authenticationManager.authenticate(auth);

            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            String token = jwtUtils.CreateToken(request.getEmail(), authorities);
            resp.put("token", token);

            return ResponseEntity.ok(resp);
        } catch (AuthenticationException e) {
            resp.put("error", "Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(resp);
        }
    }

}
