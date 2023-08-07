package com.example.reactivekafkasse.broker;

import com.example.reactivekafkasse.model.Employee;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Slf4j
@Component
public class EventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper mapper;

    public void send(Employee employee)
    {
        try
        {
            String json = mapper.writeValueAsString(employee);

            kafkaTemplate.send("eventtopic", json);
        }
        catch (JsonProcessingException e)
        {
            log.error("Object to json converter error.", e);
        }
    }
}
