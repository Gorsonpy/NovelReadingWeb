package idi.gorsonpy.domain;

import lombok.Data;

@Data
public class User {
    long id;
    private String username;
    private String password;
    boolean admin;
}
