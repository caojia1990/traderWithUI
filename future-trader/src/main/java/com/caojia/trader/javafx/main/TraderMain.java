package com.caojia.trader.javafx.main;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.caojia.trader.javafx.controller.TickController;
import com.caojia.trader.javafx.controller.TraderMainController;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TraderMain extends Application {

	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/caojia/trader/javafx/view/TraderMain.fxml"));
		Parent root = fxmlLoader.load();
		TraderMainController mainController = fxmlLoader.getController();
		
        primaryStage.setTitle("程序化");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        
        
        
        FXMLLoader tickChartfxmlLoader = new FXMLLoader(getClass().getResource("/com/caojia/trader/javafx/view/TickChart.fxml"));
		Node chart = tickChartfxmlLoader.load();
		mainController.getChartArea().setCenter(chart);
		TickController tickController = tickChartfxmlLoader.getController();
		
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				
				while (true) {
					
					
					String	category = LocalDateTime.now().toString();
					
					Platform.runLater(new Runnable() {
					    @Override
					    public void run() {
					        //更新JavaFX的主线程的代码放在此处
					    		tickController.addData(category, Math.random()*20);
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
