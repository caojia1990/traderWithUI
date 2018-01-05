package com.caojia.trader.javafx.main;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_TE_RESUME_TYPE;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcDepthMarketDataField;
import org.hraink.futures.jctp.md.JCTPMdApi;
import org.hraink.futures.jctp.md.JCTPMdSpi;
import org.hraink.futures.jctp.trader.JCTPTraderApi;
import org.hraink.futures.jctp.trader.JCTPTraderSpi;

import com.caojia.trader.bean.FutureChange;
import com.caojia.trader.bean.FuturesMarket;
import com.caojia.trader.ctp.MyMdSpi;
import com.caojia.trader.ctp.MyTraderSpi;
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
    
    static Logger logger = Logger.getLogger(Application.class);
    
    public static String BROKER_ID = "9999";
    public static String USER_ID = "105839";
    public static String PASSWORD = "caojiactp1";
    
    //行情地址
    public static String marketFront = "tcp://180.168.146.187:10010";
    /** 行情API **/
    static JCTPMdApi mdApi;
    static JCTPMdSpi mdSpi;
    
    //交易地址 
    //public static String tradeFront = "tcp://180.168.146.187:10000";
    public static String tradeFront = "tcp://180.168.146.187:10001";
    static JCTPTraderApi traderApi;
    static JCTPTraderSpi traderSpi;
    Map<String , FutureChange> changeMap = new HashMap<String , FutureChange>();
    
    private TickController tickController;
    private TraderMainController mainController;

	@Override
	public void start(Stage primaryStage) throws IOException {
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/caojia/trader/javafx/view/TraderMain.fxml"));
		Parent root = fxmlLoader.load();
		mainController = fxmlLoader.getController();
		
        primaryStage.setTitle("程序化");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
        
        
        
        FXMLLoader tickChartfxmlLoader = new FXMLLoader(getClass().getResource("/com/caojia/trader/javafx/view/TickChart.fxml"));
		Node chart = tickChartfxmlLoader.load();
		mainController.getChartArea().setCenter(chart);
		tickController = tickChartfxmlLoader.getController();
		
		
		Thread thread = new Thread(new MarketThread(this));
		thread.setDaemon(true);
		thread.start();
		
		/*Thread thread = new Thread(new Runnable() {
			
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
		thread.start();*/
		
	}

	public static void main(String[] args) {
		launch(args);
	}
	
	
	
	
	
	
	
	
	
	public void onRtnDepthMarketData(CThostFtdcDepthMarketDataField pDepthMarketData){
        
        int volumeChange = 0;
        double openInterestChange = 0;
        FutureChange futureChange = this.getChange(pDepthMarketData.getInstrumentID());
        if(futureChange.getVolume() == 0){
            futureChange.setVolume(pDepthMarketData.getVolume());
            futureChange.setOpenInterest(pDepthMarketData.getOpenInterest());
        }else {
            volumeChange = pDepthMarketData.getVolume() - futureChange.getVolume();
            openInterestChange = pDepthMarketData.getOpenInterest() - futureChange.getOpenInterest();
            futureChange.setVolume(pDepthMarketData.getVolume());
            futureChange.setOpenInterest(pDepthMarketData.getOpenInterest());
        }
        
        FuturesMarket market = new FuturesMarket();
        
        market.setAskPrice1(pDepthMarketData.getAskPrice1());
        market.setAskVolume1(pDepthMarketData.getAskVolume1());
        market.setBidPrice1(pDepthMarketData.getBidPrice1());
        market.setBidVolume1(pDepthMarketData.getBidVolume1());
        market.setInstrumentID(pDepthMarketData.getInstrumentID());
        market.setLastPrice(pDepthMarketData.getLastPrice());
        market.setOpenInterest(pDepthMarketData.getOpenInterest());
        market.setVolume(pDepthMarketData.getVolume());
        market.setVolumeChange(volumeChange);
        market.setOpenInterestChange(openInterestChange);
        market.setTradeDate(pDepthMarketData.getTradingDay());
        market.setUpdateTime(pDepthMarketData.getUpdateTime());
        market.setUpdateMillisec(pDepthMarketData.getUpdateMillisec());
        
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //更新JavaFX的主线程的代码放在此处
                    tickController.addData(pDepthMarketData.getUpdateTime()+pDepthMarketData.getUpdateMillisec(), pDepthMarketData.getLastPrice());
                    mainController.market(market);
            }
        });
        //marketService.saveFutureMarket(pDepthMarketData,volumeChange,(int)openInterestChange);
    }
	
    public FutureChange getChange(String instrument){
        
        FutureChange change = this.changeMap.get(instrument);
        if(change == null){
            change = new FutureChange();
            changeMap.put(instrument, change);
        }
        return change;
    }
	
	
	
    public static class MarketThread implements Runnable {
        
        private TraderMain traderMain;
        
        
        public MarketThread(TraderMain traderMain){
            this.traderMain = traderMain;
        }
        

        public void run() {
            mdApi = JCTPMdApi.createFtdcTraderApi();
            
            mdSpi = new MyMdSpi(mdApi,traderMain);
            //注册spi
            mdApi.registerSpi(mdSpi);
            //注册前置机地址
            mdApi.registerFront(marketFront);
            mdApi.Init();
            
            mdApi.Join();
            
            mdApi.Release();
            
        }
        
    }
    
/*    public static class TradeThread implements Runnable {
        private Application application;
        
        public TradeThread(Application application){
            this.application = application;
        }
        

        public void run() {
            String dataPath = "ctpdata/test/";
            
//          traderApi = JCTPTraderApi.createFtdcTraderApi();
            traderApi = JCTPTraderApi.createFtdcTraderApi(dataPath);

            traderSpi = new MyTraderSpi(traderApi,application);
            
            //注册traderpi
            traderApi.registerSpi(traderSpi);
            //注册公有流
            traderApi.subscribePublicTopic(THOST_TE_RESUME_TYPE.THOST_TERT_QUICK);
            //注册私有流
            traderApi.subscribePrivateTopic(THOST_TE_RESUME_TYPE.THOST_TERT_QUICK);
            //注册前置机地址
            traderApi.registerFront(tradeFront);
            
            traderApi.init();
            traderApi.join();
            //回收api和JCTP
            traderApi.release();
            
        }
        
    }*/
}
