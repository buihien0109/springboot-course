package com.example.demo.application.event.event.type.user;

import lombok.*;

@ToString
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDeletedEvent {
    private Long userId;
}
