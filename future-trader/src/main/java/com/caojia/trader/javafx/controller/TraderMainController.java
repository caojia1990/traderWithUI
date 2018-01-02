package com.caojia.trader.javafx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcDepthMarketDataField;

import com.bohai.finance.model.Bank;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class TraderMainController implements Initializable{
	
	@FXML
	private BorderPane chartArea;
	
	@FXML
	private ComboBox<String> instrumentComb;
	
	@FXML
	private TableView<CThostFtdcDepthMarketDataField> marketTable;
	
	@FXML
	private ListView<String> priceList;
	
	private ObservableList<CThostFtdcDepthMarketDataField> marketList;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		instrumentComb.toFront();
		
		marketList = FXCollections.observableArrayList();
		
		marketTable.setItems(marketList);
	}

	public BorderPane getChartArea() {
		return chartArea;
	}

	public void setChartArea(BorderPane chartArea) {
		this.chartArea = chartArea;
	}
	
	@FXML
	public void choseInstrument(KeyEvent event) {
		
		System.out.println(event.getText());
		
		switch (event.getCode()) {
		case ENTER:
		
			break;
		case ESCAPE:
		
			break;
		case DELETE:
			
			break;
		default:
			
			if(!instrumentComb.isVisible()) {
				
				instrumentComb.setValue(null);
				instrumentComb.setVisible(true);
				
			}
			instrumentComb.setValue(null);
			instrumentComb.toFront();
			instrumentComb.requestFocus();
			break;
		}
		
	}
	
	@FXML
	public void searchInstrument(KeyEvent event) {
		
		System.out.println(instrumentComb.getValue());
		switch (event.getCode()) {
		case ENTER:
			instrumentComb.toBack();
			marketTable.requestFocus();
			break;
		case ESCAPE:
			instrumentComb.toBack();
			//instrumentComb.setVisible(false);
			marketTable.requestFocus();
			break;
		default:
			
			break;
		}
	}

}
