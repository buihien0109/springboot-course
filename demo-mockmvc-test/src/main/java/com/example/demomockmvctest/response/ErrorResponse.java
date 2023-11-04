package com.example.demomockmvctest.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private HttpStatus status;
    private Object message;
}
