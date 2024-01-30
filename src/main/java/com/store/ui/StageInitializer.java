package com.store.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class StageInitializer implements ApplicationListener<StageReadyEvent> {

    @Override
    public void onApplicationEvent(StageReadyEvent event) {
        Stage stage = event.getStage();
        ApplicationContext context = event.getContext();
        FXMLLoader fxmlLoader = new FXMLLoader(CrudFX.class.getResource("/crud-test.fxml"));
        fxmlLoader.setControllerFactory(context::getBean);
        try {
            Scene scene = new Scene(fxmlLoader.load(), 1024, 650);
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
