package org.study.subjectresource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SubjectResourceApplication {

    public static void main(String[] args) {
        SpringApplication.run(SubjectResourceApplication.class, args);
    }

}
