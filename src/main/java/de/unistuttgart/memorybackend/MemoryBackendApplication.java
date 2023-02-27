package de.unistuttgart.memorybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
        "de.unistuttgart.memorybackend.controller", "de.unistuttgart.memorybackend.data", "de.unistuttgart.memorybackend.data.mapper", "de.unistuttgart.memorybackend.repositories", "de.unistuttgart.memorybackend.service"})
public class MemoryBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemoryBackendApplication.class, args);
    }

}
