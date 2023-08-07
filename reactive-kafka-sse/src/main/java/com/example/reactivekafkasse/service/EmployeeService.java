package com.example.reactivekafkasse.service;

import com.example.reactivekafkasse.broker.EventProducer;
import com.example.reactivekafkasse.model.Employee;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.UUID;

@AllArgsConstructor
@Service
public class EmployeeService {

    private final EventProducer eventProducer;

    public Mono<Employee> sendEmployeeInfoToTopic(Employee employee) {

        return Mono.just(employee)
//                .doOnSuccess(e -> e.setId(UUID.randomUUID().toString()))
                .doOnSuccess(eventProducer::send);
    }
}
