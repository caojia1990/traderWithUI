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

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		instrumentComb.toFront();
		
		this.addColumns();
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
			
		    FuturesMarket futuresMarket = marketTable.getSelectionModel().getSelectedItem();
		    if(futuresMarket != null){
		        TraderMain.mdApi.unSubscribeMarketData(futuresMarket.getInstrumentID());
		    }
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
	
	/**
	 * 更新行情
	 * @param market
	 */
	public void market(FuturesMarket market){
	    
        for(int i =0; i< marketList.size() ; i++){
            if(marketList.get(i).getInstrumentID().equals(market.getInstrumentID())){
                marketList.set(i, market);
            }
        }
	}
	
	/**
     * 订阅合约
     * @param instrument
     */
    public void subMarketData(String instrument){
        for(int i =0; i< marketList.size() ; i++){
            if(marketList.get(i).getInstrumentID().equals(instrument)){
                return;
            }
        }
        FuturesMarket market = new FuturesMarket();
        market.setInstrumentID(instrument);
        marketList.add(market);
    }
	
	/**
	 * 删除合约订阅
	 * @param instrument
	 */
	public void unSubMarketData(String instrument){
	    for(int i =0; i< marketList.size() ; i++){
            if(marketList.get(i).getInstrumentID().equals(instrument)){
                marketList.remove(i);
            }
        }
	}
	
	public void addColumns(){
	    
	    TableColumn<FuturesMarket, String> instrumentIDCol = new TableColumn<>("合约");
	    instrumentIDCol.setCellValueFactory(new PropertyValueFactory<FuturesMarket, String>("instrumentID"));
        instrumentIDCol.setCellFactory(TextFieldTableCell.forTableColumn());
        marketTable.getColumns().add(instrumentIDCol);
        
        TableColumn<FuturesMarket, Double> lastPriceCol = new TableColumn<>("最新价");
        lastPriceCol.setCellValueFactory(new PropertyValueFactory<FuturesMarket, Double>("lastPrice"));
        marketTable.getColumns().add(lastPriceCol);
        
        TableColumn<FuturesMarket, Double> bid1Col = new TableColumn<>("买一价");
        bid1Col.setCellValueFactory(new PropertyValueFactory<FuturesMarket, Double>("bidPrice1"));
        marketTable.getColumns().add(bid1Col);
        
        TableColumn<FuturesMarket, Integer> bid1VolumeCol = new TableColumn<>("买一量");
        bid1VolumeCol.setCellValueFactory(new PropertyValueFactory<FuturesMarket, Integer>("bidVolume1"));
        marketTable.getColumns().add(bid1VolumeCol);
        
        TableColumn<FuturesMarket, Double> ask1Col = new TableColumn<>("卖一价");
        ask1Col.setCellValueFactory(new PropertyValueFactory<FuturesMarket, Double>("askPrice1"));
        marketTable.getColumns().add(ask1Col);
        
        TableColumn<FuturesMarket, Integer> ask1VolumeCol = new TableColumn<>("卖一量");
        ask1VolumeCol.setCellValueFactory(new PropertyValueFactory<FuturesMarket, Integer>("askVolume1"));
        marketTable.getColumns().add(ask1VolumeCol);
        
        TableColumn<FuturesMarket, Double> averagePriceCol = new TableColumn<>("均价");
        averagePriceCol.setCellValueFactory(new PropertyValueFactory<FuturesMarket, Double>("averagePrice"));
        marketTable.getColumns().add(averagePriceCol);
        
        TableColumn<FuturesMarket, Double> highestPriceCol = new TableColumn<>("最高价");
        highestPriceCol.setCellValueFactory(new PropertyValueFactory<FuturesMarket, Double>("highestPrice"));
        marketTable.getColumns().add(highestPriceCol);
        
        TableColumn<FuturesMarket, Double> lowestPriceCol = new TableColumn<>("最低价");
        lowestPriceCol.setCellValueFactory(new PropertyValueFactory<FuturesMarket, Double>("lowestPrice"));
        marketTable.getColumns().add(lowestPriceCol);
        
        TableColumn<FuturesMarket, Double> upperLimitPriceCol = new TableColumn<>("涨停价");
        upperLimitPriceCol.setCellValueFactory(new PropertyValueFactory<FuturesMarket, Double>("upperLimitPrice"));
        marketTable.getColumns().add(upperLimitPriceCol);
        
        TableColumn<FuturesMarket, Double> lowerLimitPriceCol = new TableColumn<>("跌停价");
        lowerLimitPriceCol.setCellValueFactory(new PropertyValueFactory<FuturesMarket, Double>("lowerLimitPrice"));
        marketTable.getColumns().add(lowerLimitPriceCol);
        
        TableColumn<FuturesMarket, Double> preClosePriceCol = new TableColumn<>("昨收盘");
        preClosePriceCol.setCellValueFactory(new PropertyValueFactory<FuturesMarket, Double>("preClosePrice"));
        marketTable.getColumns().add(preClosePriceCol);
        
        TableColumn<FuturesMarket, Double> preSettlementPriceCol = new TableColumn<>("昨结算");
        preSettlementPriceCol.setCellValueFactory(new PropertyValueFactory<FuturesMarket, Double>("preSettlementPrice"));
        marketTable.getColumns().add(preSettlementPriceCol);
        
        TableColumn<FuturesMarket, Double> closePriceCol = new TableColumn<>("收盘价");
        closePriceCol.setCellValueFactory(new PropertyValueFactory<FuturesMarket, Double>("closePrice"));
        marketTable.getColumns().add(closePriceCol);
        
        TableColumn<FuturesMarket, Double> settlementPriceCol = new TableColumn<>("结算价");
        settlementPriceCol.setCellValueFactory(new PropertyValueFactory<FuturesMarket, Double>("settlementPrice"));
        marketTable.getColumns().add(settlementPriceCol);
	}

}
