package com.example.reactivekafkasse.resource;

import com.example.reactivekafkasse.model.Employee;
import com.example.reactivekafkasse.service.EmployeeService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
public class EmployeeResource {

    private final EmployeeService employeeService;
    private final String RESOURCE_PATH = "employee";


    @PostMapping(RESOURCE_PATH)
    public ResponseEntity<?> sendEmployeeInfoToTopic(@RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.sendEmployeeInfoToTopic(employee), HttpStatus.OK);
    }

}
