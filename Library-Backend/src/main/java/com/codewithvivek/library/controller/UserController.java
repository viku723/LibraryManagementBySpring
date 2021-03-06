package com.codewithvivek.library.controller;

import com.codewithvivek.library.dto.*;
import com.codewithvivek.library.model.User;
import com.codewithvivek.library.security.JwtTokenUtil;
import com.codewithvivek.library.service.UserServiceAble;
import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserServiceAble userService;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;

    public UserController(AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    @GetMapping("/all")
    @RolesAllowed("Admin")
    public ResponseEntity getAllUsers() throws Exception {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.status(200).body(users);
    }

    public Boolean isAdmin(int id) {
        return null;
    }

    @PostMapping("/add")
    public ResponseEntity saveUser(@RequestBody User user) throws Exception {
        try {
            userService.saveUser(user);
            HashMap<String, String> map = new HashMap<>();
            map.put("success", "Successfully create user");
            return ResponseEntity.status(201).body(map);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Unable to save " + e.getMessage());
        }
    }

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) {
        try {
            Authentication authenticate = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(
                                    request.getEmail(), request.getPassword()
                            )
                    );

            User user = (User) authenticate.getPrincipal();
            UserView userView = new UserView(user.getEmail(), user.getName(), jwtTokenUtil.generateAccessToken(user), user.getAuthorities());
            return ResponseEntity.ok()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            jwtTokenUtil.generateAccessToken(user)
                    )
                    .body(userView);
                    //.body(user);
        } catch (BadCredentialsException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
        }
    }
}
