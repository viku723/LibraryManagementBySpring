package com.codewithvivek.library.dto;

import java.util.*;
import lombok.*;
import org.springframework.security.core.*;

@Data
public class UserView {

    private int id;
    private String email;
    private String name;
    private String accessToken;
    private Collection<? extends GrantedAuthority> roles;

    public UserView(String email, String name, String accessToken, Collection<? extends GrantedAuthority> roles) {
        this.email = email;
        this.name = name;
        this.accessToken = accessToken;
        this.roles = roles;
    }

}