package net.csa.conference;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
class ConferenceMicroservice {

    public static void main(String[] args) {

        System.out.println("Test");

        SpringApplication.run(ConferenceMicroservice.class, args);
    }

}
