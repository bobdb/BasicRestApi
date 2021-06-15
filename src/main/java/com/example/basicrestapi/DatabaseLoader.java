package com.example.basicrestapi;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DatabaseLoader {

    private static final Logger log = LoggerFactory.getLogger(DatabaseLoader.class);

    @Bean
    CommandLineRunner initDatabase(PersonRepository repository) {

        return args -> {
            log.info("Preloading " + repository.save(new Person("John Doe", "engineer")));
            log.info("Preloading " + repository.save(new Person("Jane Doe", "doctor")));
        };
    }
}