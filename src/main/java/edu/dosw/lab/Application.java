package edu.dosw.lab;

import edu.dosw.lab.agilismo.ExecutableStockMonitoringSystem;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.util.Scanner;

@SpringBootApplication(scanBasePackages = "edu.dosw.lab")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    CommandLineRunner cli(ExecutableStockMonitoringSystem runner) {
        return args -> {
            try (Scanner sc = new Scanner(System.in)) {
                runner.runInteractive(sc);
            }
        };
    }
}
