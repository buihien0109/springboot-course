package com.example.demo.application.event.event.listener;

import com.example.demo.application.event.event.type.user.UserCreatedEvent;
import com.example.demo.application.event.event.type.user.UserDeletedEvent;
import com.example.demo.application.event.event.type.user.UserUpdatedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class UserEventListener {

    @EventListener
    public void onUserCreated(UserCreatedEvent event) {
        System.out.println("User created with ID: " + event.getUserId());
    }

    @EventListener
    public void onUserUpdated(UserUpdatedEvent event) {
        System.out.println("User updated with ID: " + event.getUserId());
    }

    @EventListener
    public void onUserDeleted(UserDeletedEvent event) {
        System.out.println("User deleted with ID: " + event.getUserId());
    }
}
