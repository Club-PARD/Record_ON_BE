package com.pard.record_on_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RecordOnBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(RecordOnBeApplication.class, args);
    }

}
