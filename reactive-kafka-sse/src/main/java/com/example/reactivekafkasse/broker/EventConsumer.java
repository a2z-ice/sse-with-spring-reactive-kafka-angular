package com.example.reactivekafkasse.broker;

import com.example.reactivekafkasse.model.Employee;
import com.example.reactivekafkasse.service.EmployeeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Component
public class EventConsumer
{
    private final ObjectMapper mapper;
    private final EmployeeService employeeService;

    private final Map<String, EmitterProcessor<ServerSentEvent<Employee>>> emitterProcessorMap = new ConcurrentHashMap<>();

    public Flux<ServerSentEvent<Employee>> get(String employeeId, String eventId)
    {
        employeeService.saveEventId(employeeId, eventId);

        EmitterProcessor<ServerSentEvent<Employee>> emitterProcessor = EmitterProcessor.create();
        emitterProcessorMap.put(eventId, emitterProcessor);

        return emitterProcessor.doFinally(signalType -> emitterProcessorMap.remove(eventId)).log();
    }

    @KafkaListener(id = "eventId", topics = "eventtopic")
    public void receive(String data) throws JsonProcessingException {

        Employee employee = mapper.readValue(data, Employee.class);
        Set<String> eventIds = employeeService.getEventIds(employee.getId());

        for(String eventId: eventIds) {
            if (emitterProcessorMap.containsKey(eventId)) {
                EmitterProcessor<ServerSentEvent<Employee>> emitterProcessor = emitterProcessorMap.get(eventId);
                if (emitterProcessor != null) {
                    emitterProcessor.onNext(ServerSentEvent.builder(employee).id(UUID.randomUUID().toString()).build());
                }
            }
        }
    }

}