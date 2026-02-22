package org.hkpc.dtd;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;

//disable the default user and password generation by Spring Security as we don't need it
@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
@MapperScan("org.hkpc.dtd.component.postgres.dao")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}

