package com.caojia.trader.javafx.controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.caojia.trader.bean.FuturesMarket;
import com.caojia.trader.javafx.main.TraderMain;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;

public class TraderMainController implements Initializable{
	
	@FXML
	private BorderPane chartArea;
	
	@FXML
	private ComboBox<String> instrumentComb;
	
	@FXML
	private TableView<FuturesMarket> marketTable;
	
	@FXML
	private ListView<String> priceList;
	
	private ObservableList<FuturesMarket> marketList;
	
	@FXML
	private TableColumn<FuturesMarket, String> instrumentIDCol;
	
	@FXML
	private TableColumn<FuturesMarket, Double> lastPriceCol;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		instrumentComb.toFront();
		
		instrumentIDCol.setCellValueFactory(new PropertyValueFactory<FuturesMarket, String>("instrumentID"));
		instrumentIDCol.setCellFactory(TextFieldTableCell.forTableColumn());
		
		lastPriceCol.setCellValueFactory(new PropertyValueFactory<FuturesMarket, Double>("lastPrice"));
		//lastPriceCol.setCellFactory(TextFieldTableCell.forTableColumn());
		
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
			
			TraderMain.mdApi.subscribeMarketData(instrumentComb.getValue());
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
	
	public void market(FuturesMarket market){
	    if(marketList.size() < 1){
	        marketList.add(market);
	    }else {
            marketList.set(0, market);
        }
	}
	
	public void addColumns(){
	    
	    TableColumn<FuturesMarket, String> instrumentIDCol = new TableColumn<>("合约");
	    instrumentIDCol.setCellValueFactory(new PropertyValueFactory<FuturesMarket, String>("instrumentID"));
        instrumentIDCol.setCellFactory(TextFieldTableCell.forTableColumn());
        
        TableColumn<FuturesMarket, Double> lastPriceCol = new TableColumn<>("最新价");
        lastPriceCol.setCellValueFactory(new PropertyValueFactory<FuturesMarket, Double>("lastPrice"));
	}

}
