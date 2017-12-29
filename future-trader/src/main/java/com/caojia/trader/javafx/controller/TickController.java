package com.caojia.trader.javafx.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;

public class TickController implements Initializable{

	@FXML
	private LineChart<String, Number> lineChart;
	
	@FXML
	private CategoryAxis categoryAxis;
	
	@FXML
	private NumberAxis numberAxis;
	
//	private ObservableList<Data<String, Number>> list;
	private Series<String, Number> series;
	
	
	public void initialize(URL location, ResourceBundle resources) {
	    
		
		String stockLineChartCss = getClass().getResource("/com/caojia/trader/javafx/view/TickChart.css").toExternalForm();
		
		lineChart.getStylesheets().add(stockLineChartCss);
	        
		series = new Series<>();
		
		lineChart.getData().add(series);
		
		
	}
	
	public void addData(String category, Number number) {
		
	    numberAxis.setLowerBound(55400);
        numberAxis.setUpperBound(55500);
        numberAxis.setTickUnit(10); 
		series.getData().add(new Data<String, Number>(category, number));
		if(series.getData().size() > 60) {
			series.getData().remove(0);
		}
		
	}
	
	

}
