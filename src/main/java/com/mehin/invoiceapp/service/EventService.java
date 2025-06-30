package com.mehin.invoiceapp.service;

import com.mehin.invoiceapp.domain.UserEvent;
import com.mehin.invoiceapp.enumeration.EventType;
import jdk.jfr.Event;

import java.util.Collection;

public interface EventService {
    Collection<UserEvent> getEventsByUserId(Long userId);
    void addUserEvent(String email, EventType eventType, String device, String ipAddress);
    void addUserEvent(Long userId, EventType eventType, String device, String ipAddress);

}
