package com.example.reactivekafkasse.broker;

import com.example.reactivekafkasse.model.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@AllArgsConstructor
@Component
public class EventConsumer
{
    private final ObjectMapper mapper;

    private final Map<String, EmitterProcessor<ServerSentEvent<Employee>>> emitterProcessorMap = new ConcurrentHashMap<>();

    public Flux<ServerSentEvent<Employee>> get(String userId)
    {
        EmitterProcessor<ServerSentEvent<Employee>> emitterProcessor = EmitterProcessor.create();
        emitterProcessorMap.put(userId, emitterProcessor);

        return emitterProcessor.doFinally(signalType -> emitterProcessorMap.remove(userId)).log();
    }

    @KafkaListener(id = "eventId", topics = "eventtopic")
    public void receive(String data) throws JsonProcessingException {

        Employee employee = mapper.readValue(data, Employee.class);

        if(emitterProcessorMap.containsKey(employee.getId())) {
            EmitterProcessor<ServerSentEvent<Employee>> emitterProcessor = emitterProcessorMap.get(employee.getId());
            if (emitterProcessor != null) {
                emitterProcessor.onNext(ServerSentEvent.builder(employee).id(UUID.randomUUID().toString()).build());
            }
        }
    }

}