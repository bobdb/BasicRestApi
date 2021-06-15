package com.example.basicrestapi;

//import com.example.basicrestapi.entities.AccountInfo;
//import com.example.basicrestapi.entities.Payment;
//import com.example.basicrestapi.entities.Person;
//import com.example.basicrestapi.payloads.PaymentResponse;
//import com.example.basicrestapi.repositories.PersonRepository;
//import com.example.basicrestapi.services.AuthenticationService;
//import com.example.basicrestapi.services.PaymentProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class BasicController {

    private final AtomicLong counter = new AtomicLong();

    @Autowired
    PersonRepository personRepository;

    @GetMapping("/hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("/persons")
    public List<Person> getPeople() {
        return personRepository.findAll();
    }

    @GetMapping("/person/{id}")
    public Person getPeopleById(@PathVariable("id") long id) {
        return personRepository.findById(id);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}