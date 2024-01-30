package com.store.ui;

import com.store.CrudApplication;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

public class CrudFX extends Application {

    private ConfigurableApplicationContext appContext;

    @Override
    public void init() {
        appContext = new SpringApplicationBuilder(CrudApplication.class).run();
    }

    @Override
    public void stop() {
        appContext.close();
        Platform.exit();
    }

    @Override
    public void start(Stage stage) {
        appContext.publishEvent(new StageReadyEvent(stage, appContext));
    }


}
