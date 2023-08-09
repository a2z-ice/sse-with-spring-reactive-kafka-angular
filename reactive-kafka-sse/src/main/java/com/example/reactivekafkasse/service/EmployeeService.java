package com.example.reactivekafkasse.service;

import com.example.reactivekafkasse.broker.EventProducer;
import com.example.reactivekafkasse.model.Employee;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@AllArgsConstructor
@Service
public class EmployeeService {

    private final EventProducer eventProducer;

    private final Map<String, Set<String>> eventIdsMap = new ConcurrentHashMap<>();

    public Mono<Employee> sendEmployeeInfoToTopic(Employee employee) {

        return Mono.just(employee)
//                .doOnSuccess(e -> e.setId(UUID.randomUUID().toString()))
                .doOnSuccess(eventProducer::send);
    }

    public void saveEventId(String employeeId, String eventId) {
        if(!eventIdsMap.containsKey(employeeId)) {
            putNewUserIdWithEventId(employeeId, eventId);
        }
        updateEventIdsForExistingUserId(employeeId, eventId);

        log.info(eventIdsMap.toString());
    }

    private void putNewUserIdWithEventId(String employeeId, String uniqueId) {
        Set<String> eventIds = new HashSet<>();
        eventIds.add(uniqueId);
        eventIdsMap.put(employeeId, eventIds);
    }

    private void updateEventIdsForExistingUserId(String employeeId, String uniqueId) {
        Set<String> eventIds = eventIdsMap.get(employeeId);
        eventIds.add(uniqueId);
    }
    
    public Set<String> getEventIds(String employeeId) {
        if(eventIdsMap.containsKey(employeeId)) return eventIdsMap.get(employeeId);
        return new HashSet<>();
    }
}
