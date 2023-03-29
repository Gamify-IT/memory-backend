package de.unistuttgart.memorybackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;

@ImportAutoConfiguration({ FeignAutoConfiguration.class })
@SpringBootApplication(scanBasePackages = { "de.unistuttgart" })
@EnableFeignClients
public class MemoryBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(MemoryBackendApplication.class, args);
    }
}
