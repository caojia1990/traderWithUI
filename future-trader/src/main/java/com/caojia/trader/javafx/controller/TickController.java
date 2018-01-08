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
	
	private double min = 0;;
	
	private double max = 0;;
	
	
	public void initialize(URL location, ResourceBundle resources) {
	    
		
		String stockLineChartCss = getClass().getResource("/com/caojia/trader/javafx/view/TickChart.css").toExternalForm();
		
		lineChart.getStylesheets().add(stockLineChartCss);
	        
		series = new Series<>();
		
		lineChart.getData().add(series);
		
		
	}
	
	public void addData(String category, Number number) {
		
	    if(number.doubleValue() > max){
	        max = number.doubleValue();
	        min = number.doubleValue() - 50;
	    }
	    if(number.doubleValue() < min){
	        min = number.doubleValue();
	    }
	    
	    numberAxis.setLowerBound(min);
        numberAxis.setUpperBound(max);
        numberAxis.setTickUnit(10); 
		series.getData().add(new Data<String, Number>(category, number));
		if(series.getData().size() > 60) {
			series.getData().remove(0);
		}
		
	}
	

}
