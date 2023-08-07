package com.example.reactivekafkasse.resource;

import com.example.reactivekafkasse.broker.EventConsumer;
import com.example.reactivekafkasse.model.Employee;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@AllArgsConstructor
@RestController
public class EventResource {

    private final EventConsumer eventConsumer;

    @GetMapping(path = "/events/{userId}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<Employee>> getEvents(@PathVariable("userId") String userId)
    {
        return eventConsumer
                .get(userId)
                ;
    }

}
