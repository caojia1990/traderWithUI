package com.caojia.trader.javafx.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TickControllerTest extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {

		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/caojia/trader/javafx/view/TickChart.fxml"));
		Parent root = fxmlLoader.load();
		
        primaryStage.setTitle("Tick");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
		
        
        Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				TickController tickController = fxmlLoader.getController();
				int i = 0;
				while (true) {
					
					
					DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh24:mm:ss");
					String category = LocalDateTime.now().format(dateTimeFormatter);
					
					Number number = 18;
					
					Platform.runLater(new Runnable() {
					    @Override
					    public void run() {
					        //更新JavaFX的主线程的代码放在此处
					    		tickController.addData(category, number);
					    }
					});
					
					try {
						Thread.sleep(500);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});
		
		thread.setDaemon(true);
		thread.start();
	}

	public static void main(String[] args) {
		launch(args);
		
		
	}
}
