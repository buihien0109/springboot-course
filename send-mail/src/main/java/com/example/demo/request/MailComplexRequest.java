package com.example.demo.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MailComplexRequest {
    private String to;
    private String subject;
    private String username;
    private String email;
    private Integer age;
    private String city;
    private String country;
}
