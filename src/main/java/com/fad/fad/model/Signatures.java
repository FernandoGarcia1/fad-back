package com.fad.fad.model;

import lombok.Data;

@Data

public class Signatures {
    // @Id
    // @GeneratedValue(generator = "UUID")
    // @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    // private UUID id;
    private String name;
    private String phone;
    private String email;
    // private String id_user;
}
