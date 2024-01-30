package com.store.configuration;

import com.store.CrudController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class AppConfig {

    @Bean @Scope("prototype")
    public CrudController crudController() {
        return new CrudController();
    }

}
