package com.store;

import com.store.ui.CrudFX;
import javafx.application.Application;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CrudApplication {

	public static void main(String[] args) {
		Application.launch(CrudFX.class, args);
	}

}
