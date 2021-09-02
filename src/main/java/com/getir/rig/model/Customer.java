package com.getir.rig.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@AllArgsConstructor
public class Customer {

    @Id
    private String id;
    private String userName;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
