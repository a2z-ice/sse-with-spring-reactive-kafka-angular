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

import java.util.UUID;

@AllArgsConstructor
@Component
public class EventConsumer
{
    private final ObjectMapper mapper;
    private final EmitterProcessor<ServerSentEvent<Employee>> emitter = EmitterProcessor.create();
//    private final EmitterProcessor<String> emitter = EmitterProcessor.create();

    public Flux<ServerSentEvent<Employee>> get(String userId)
    {
        return emitter.log();
    }

    @KafkaListener(id = "eventId", topics = "eventtopic")
    public void receive(String data) throws JsonProcessingException {
        /*ServerSentEvent.<String> builder()
                .id(String.valueOf(sequence))
                .event("periodic-event")
                .data("SSE - " + LocalTime.now().toString())
                .build()*/

        emitter.onNext(ServerSentEvent.builder(mapper.readValue(data, Employee.class)).id(UUID.randomUUID().toString()).build());
    }

}