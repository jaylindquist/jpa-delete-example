package com.bitwiseor.jpa.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.xml.crypto.Data;
import java.util.Arrays;
import java.util.List;

@SpringBootApplication
public class ExampleApplication {
    private static Logger log = LoggerFactory.getLogger(ExampleApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ExampleApplication.class, args);
    }

    @Bean
    public CommandLineRunner load(DataEntityRepo repo) {
        return new CommandLineRunner() {
            @Override
            public void run(String... args) throws Exception {
                log.info("Saving Entity A");
                final DataEntity dataA = new DataEntity();
                dataA.setFirst(1L);
                dataA.setLast(10L);
                dataA.setMeta("Entity A");
                repo.save(dataA);

                log.info("Saving Entity B");
                final DataEntity dataB = new DataEntity();
                dataB.setFirst(8L);
                dataB.setLast(100L);
                dataB.setMeta("Entity B");
                repo.save(dataB);
            }
        };
    }

    @Bean
    public CommandLineRunner update(DataEntityRepo repo, DataService service) {
        return new CommandLineRunner() {
            @Override
            @Transactional
            public void run(String... args) throws Exception {
                log.info("Finding all entities");
                List<DataEntity> values = repo.findAll();

                DataEntity toCombine = values.get(0);
                DataEntity toDelete = values.get(1);

                toCombine.setFirst(Math.min(toCombine.getFirst(), toDelete.getFirst()));
                toCombine.setLast(Math.max(toCombine.getLast(), toDelete.getLast()));
                toCombine.setMeta("Merged");

                service.commit(toCombine, toDelete);
            }
        };
    }
}
