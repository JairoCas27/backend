package com.finli.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserHomeDTO {
    private Integer id;
    private String name;
    private String email;
    private String subscriptionType;
    private String status;
    private String registrationDate; // yyyy-MM-dd
    private String photo;            // url o base64
}
