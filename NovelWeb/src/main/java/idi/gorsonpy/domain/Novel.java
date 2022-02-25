package idi.gorsonpy.domain;

import lombok.Data;

@Data
public class Novel {
    Long id;
    String name;
    String picture;
    String author;
    String description;
}
