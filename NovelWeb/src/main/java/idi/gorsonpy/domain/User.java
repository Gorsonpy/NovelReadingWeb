package idi.gorsonpy.domain;

import lombok.Data;

@Data
public class User {
    long id;
    boolean admin;
    private String username;
    private String password;
}
