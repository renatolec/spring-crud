package com.store.ui;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ConfigurableApplicationContext;

public class StageReadyEvent extends ApplicationEvent {

    private final ConfigurableApplicationContext appContext;

    public StageReadyEvent(Stage stage, ConfigurableApplicationContext appContext) {
        super(stage);
        this.appContext = appContext;
    }

    public Stage getStage() {
        return (Stage) getSource();
    }

    public ConfigurableApplicationContext getContext(){
        return appContext;
    }
}
