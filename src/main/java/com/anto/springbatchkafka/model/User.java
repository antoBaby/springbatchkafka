package com.anto.springbatchkafka.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    private Long id;
    private String name;
    private String address;
    private String phone;
    private String mobile;
    private String email;
    private Integer zipCode;


}
