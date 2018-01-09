package com.caojia.trader.javafx.main;

import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_CC_Immediately;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_FCC_NotForceClose;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_OST_Canceled;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_OST_NoTradeNotQueueing;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_TC_IOC;
import static org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_FTDC_VC_AV;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;
import org.hraink.futures.ctp.thostftdcuserapidatatype.ThostFtdcUserApiDataTypeLibrary.THOST_TE_RESUME_TYPE;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcDepthMarketDataField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcInputOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcOrderField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcRspInfoField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcSpecificInstrumentField;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcTradeField;
import org.hraink.futures.jctp.md.JCTPMdApi;
import org.hraink.futures.jctp.md.JCTPMdSpi;
import org.hraink.futures.jctp.trader.JCTPTraderApi;
import org.hraink.futures.jctp.trader.JCTPTraderSpi;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;
import com.caojia.trader.bean.CloseRelation;
import com.caojia.trader.bean.FutureChange;
import com.caojia.trader.bean.FuturesMarket;
import com.caojia.trader.bean.Position;
import com.caojia.trader.ctp.MyMdSpi;
import com.caojia.trader.ctp.MyTraderSpi;
import com.caojia.trader.dao.CommonRedisDao;
import com.caojia.trader.javafx.controller.TickController;
import com.caojia.trader.javafx.controller.TraderMainController;
import com.caojia.trader.util.SpringContextUtil;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import strategy.FollowLargeNoCut;

public class TraderMain extends Application {
    
    static Logger logger = Logger.getLogger(Application.class);
    
    public static String BROKER_ID = "9999";
    public static String USER_ID = "105839";
    public static String PASSWORD = "caojiactp1";
    
    public static String BUY = "buy:";
    public static String SELL = "sell:";
    
    //行情地址
    public static String marketFront = "tcp://180.168.146.187:10010";
    /** 行情API **/
    public static JCTPMdApi mdApi;
    static JCTPMdSpi mdSpi;
    
    //交易地址 
    //public static String tradeFront = "tcp://180.168.146.187:10000";
    public static String tradeFront = "tcp://180.168.146.187:10001";
    static JCTPTraderApi traderApi;
    static JCTPTraderSpi traderSpi;
    Map<String , FutureChange> changeMap = new HashMap<String , FutureChange>();
    
    //private static FutureMarketService marketService;
    private static CommonRedisDao commonRedisDao;
    
    private TickController tickController;
    private TraderMainController mainController;
    
    AtomicInteger request = new AtomicInteger(0);
    //行情队列
    private static BlockingQueue<FuturesMarket> marketQueue;
    
    /**
     * 止盈条数
     */
    private int targetTick = 1;

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
		
		
		ClassPathXmlApplicationContext classPathXmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        classPathXmlApplicationContext.start();
        //行情service
        //marketService = (FutureMarketService) SpringContextUtil.getBean("futureMarketService");
        commonRedisDao = (CommonRedisDao) SpringContextUtil.getBean("commonRedisDao");
        
		
		/**
		 * 行情线程
		 */
		Thread thread = new Thread(new MarketThread(this));
		thread.setDaemon(true);
		thread.start();
		
		/**
		 * 交易线程
		 */
		Thread trade = new Thread(new TradeThread(this));
		trade.setDaemon(true);
        trade.start();
        
        /**
         * 策略线程
         */
        marketQueue = new LinkedBlockingDeque<FuturesMarket>(); 
        Thread strategy = new Thread(new FollowLargeNoCut(this));
        strategy.setDaemon(true);
        strategy.start();
        
	}

	public static void main(String[] args) {
	    
		launch(args);
	}
	
	
	
	
	
	/**************************************行情接口回调*************************************/
	/**
	 * 深度行情回报
	 * @param pDepthMarketData
	 */
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
        market.setAveragePrice(pDepthMarketData.getAveragePrice());
        market.setHighestPrice(pDepthMarketData.getHighestPrice());
        market.setLowestPrice(pDepthMarketData.getLowestPrice());
        market.setUpperLimitPrice(pDepthMarketData.getUpperLimitPrice());
        market.setLowerLimitPrice(pDepthMarketData.getLowerLimitPrice());
        market.setPreClosePrice(pDepthMarketData.getPreClosePrice());
        market.setPreSettlementPrice(pDepthMarketData.getPreSettlementPrice());
        market.setClosePrice(pDepthMarketData.getClosePrice());
        market.setSettlementPrice(pDepthMarketData.getSettlementPrice());
        try {
            marketQueue.put(market);
        } catch (InterruptedException e) {
            logger.error("推送行情失败",e);
        }
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                //更新JavaFX的主线程的代码放在此处
                    //tickController.addData(pDepthMarketData.getUpdateTime()+pDepthMarketData.getUpdateMillisec(), pDepthMarketData.getLastPrice());
                    mainController.market(market);
            }
        });
        //marketService.saveFutureMarket(pDepthMarketData,volumeChange,(int)openInterestChange);
    }
	
	
	/**
	 * 订阅回报
	 * @param pSpecificInstrument
	 */
	public void onRspSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument){
	    mainController.subMarketData(pSpecificInstrument.getInstrumentID());
	}
	
	/**
	 * 取消订阅回报
	 * @param pSpecificInstrument
	 */
	public void onRspUnSubMarketData(CThostFtdcSpecificInstrumentField pSpecificInstrument){
	    mainController.unSubMarketData(pSpecificInstrument.getInstrumentID());
	}
	/**************************************行情接口回调结束*************************************/
	
	/**************************************交易请求接口****************************************/
	/**
     * 下单操作
     * @param inputOrderField
     * @return
     */
    public int reqOrderInsert(CThostFtdcInputOrderField inputOrderField){
        
        //期货公司代码
        inputOrderField.setBrokerID(BROKER_ID);
        //投资者代码
        inputOrderField.setInvestorID(USER_ID);
        // 用户代码
        inputOrderField.setUserID(USER_ID);
        // 组合投机套保标志
        inputOrderField.setCombHedgeFlag("1");
        // 有效期类型    不成交即撤单
        inputOrderField.setTimeCondition(THOST_FTDC_TC_IOC);
        // GTD日期
        inputOrderField.setGTDDate("");
        // 成交量类型
        inputOrderField.setVolumeCondition(THOST_FTDC_VC_AV);
        // 最小成交量
        inputOrderField.setMinVolume(0);
        // 触发条件
        inputOrderField.setContingentCondition(THOST_FTDC_CC_Immediately);
        // 止损价
        inputOrderField.setStopPrice(0);
        // 强平原因
        inputOrderField.setForceCloseReason(THOST_FTDC_FCC_NotForceClose);
        // 自动挂起标志
        inputOrderField.setIsAutoSuspend(0);
        
        int result = traderApi.reqOrderInsert(inputOrderField, request.incrementAndGet());
        
        return result;
    }
    /**************************************交易请求结束*****************************************/
	
	
	/**************************************交易接口回调*****************************************/
	/**
     * 报单回报
     * @param pOrder
     */
    public void onRtnOrder(CThostFtdcOrderField pOrder) {
        
        if(pOrder.getCombOffsetFlag().equals("0") && (pOrder.getOrderStatus() == THOST_FTDC_OST_Canceled || THOST_FTDC_OST_NoTradeNotQueueing == pOrder.getOrderStatus())){
            logger.debug("开仓失败，报单状态："+pOrder.getOrderStatus()+", 报单信息："+pOrder.getStatusMsg());
          //开仓失败后持仓数量减去
            if(pOrder.getDirection() == '0'){
                //买持量减去
                commonRedisDao.increamentByKey(BUY+pOrder.getInstrumentID(), (long) -pOrder.getVolumeTotalOriginal());
            }else {
                commonRedisDao.increamentByKey(SELL+pOrder.getInstrumentID(), (long) -pOrder.getVolumeTotalOriginal());
            }
        }else if (!pOrder.getCombOffsetFlag().equals("0") && (pOrder.getOrderStatus() == THOST_FTDC_OST_Canceled || THOST_FTDC_OST_NoTradeNotQueueing == pOrder.getOrderStatus())) {
            logger.debug("平仓失败，报单状态："+pOrder.getOrderStatus()+", 报单信息："+pOrder.getStatusMsg());
            
            String tradeID = commonRedisDao.getHash(CloseRelation.CLOSE_RELATION+pOrder.getInstrumentID(), pOrder.getOrderRef());
            String jsonStr = commonRedisDao.getHash(Position.POSITION+pOrder.getInstrumentID(), tradeID);
            Position position = JSON.parseObject(jsonStr, Position.class);
            position.setStatus("0");
            commonRedisDao.cacheHash(Position.POSITION+pOrder.getInstrumentID(), tradeID, JSON.toJSONString(position));
            //删除平仓对应关系
            commonRedisDao.deleteHash(CloseRelation.CLOSE_RELATION+pOrder.getInstrumentID(), pOrder.getOrderRef());
            
           //开仓失败后持仓数量加上
            if(pOrder.getDirection() == '0'){
                //卖持量加上
                commonRedisDao.increamentByKey(SELL+pOrder.getInstrumentID(), (long) pOrder.getVolumeTotalOriginal());
            }else {
                //买持量加上
                commonRedisDao.increamentByKey(BUY+pOrder.getInstrumentID(), (long) pOrder.getVolumeTotalOriginal());
            }
        }
    }
    
    /**
     * 开仓失败回调
     * @param pInputOrder
     * @param pRspInfo
     * @param nRequestID
     * @param bIsLast
     */
    public void onRspOrderInsert(CThostFtdcInputOrderField pInputOrder,
            CThostFtdcRspInfoField pRspInfo, int nRequestID, boolean bIsLast) {
        if(pInputOrder.getCombOffsetFlag().equals("0")){
            //开仓失败
            //开仓失败后持仓数量减去
            if(pInputOrder.getDirection() == '0'){
                //买持量减去
                commonRedisDao.increamentByKey(BUY+pInputOrder.getInstrumentID(), (long) -pInputOrder.getVolumeTotalOriginal());
            }else {
                commonRedisDao.increamentByKey(SELL+pInputOrder.getInstrumentID(), (long) -pInputOrder.getVolumeTotalOriginal());
            }
        }else {
            //平仓失败
            String tradeID = commonRedisDao.getHash(CloseRelation.CLOSE_RELATION+pInputOrder.getInstrumentID(), pInputOrder.getOrderRef());
            String jsonStr = commonRedisDao.getHash(Position.POSITION+pInputOrder.getInstrumentID(), tradeID);
            Position position = JSON.parseObject(jsonStr, Position.class);
            position.setStatus("0");
            commonRedisDao.cacheHash(Position.POSITION+pInputOrder.getInstrumentID(), tradeID, JSON.toJSONString(position));
            
            commonRedisDao.deleteHash(CloseRelation.CLOSE_RELATION+pInputOrder.getInstrumentID(), pInputOrder.getOrderRef());
            
            //开仓失败后持仓数量加上
            if(pInputOrder.getDirection() == '0'){
                //卖持量加上
                commonRedisDao.increamentByKey(SELL+pInputOrder.getInstrumentID(), (long) pInputOrder.getVolumeTotalOriginal());
            }else {
                //买持量加上
                commonRedisDao.increamentByKey(BUY+pInputOrder.getInstrumentID(), (long) pInputOrder.getVolumeTotalOriginal());
            }
        }
    }
    
    /**
     * 成交回调
     * @param pTrade
     */
    public void onRtnTrade(CThostFtdcTradeField pTrade){
        logger.debug("成交信息："+JSON.toJSONString(pTrade));
        if(pTrade.getOffsetFlag() == '0'){
            logger.info("已成交，成交价："+pTrade.getPrice());
            
            Position position = new Position();
            position.setInstrumentID(pTrade.getInstrumentID());
            position.setDirection(String.valueOf(pTrade.getDirection()));
            position.setTradeID(pTrade.getTradeID());
            position.setPrice(pTrade.getPrice());
            position.setVolume(pTrade.getVolume());
            position.setTradeDate(pTrade.getTradingDay());
            
            
            //第二天可能成交编号重复
            logger.debug("缓存持仓信息："+JSON.toJSONString(position));
            commonRedisDao.cacheHash(Position.POSITION+position.getInstrumentID(), position.getTradeID(), JSON.toJSONString(position));
            
        }else {
            logger.info("已平仓，成交价："+pTrade.getPrice());
            //获取对应的开仓成交编号
            String tradeID = commonRedisDao.getHash(CloseRelation.CLOSE_RELATION+pTrade.getInstrumentID(), pTrade.getOrderRef());
            logger.debug("获取平仓编号："+pTrade.getTradeID()+"对应的开仓成交编号："+tradeID);
            //删除持仓
            if(tradeID != null){
                commonRedisDao.deleteHash(Position.POSITION+pTrade.getInstrumentID(), tradeID);
                logger.debug("删除持仓："+tradeID);
                //删除平仓对应关系
                commonRedisDao.deleteHash(CloseRelation.CLOSE_RELATION+pTrade.getInstrumentID(), pTrade.getOrderRef());
            }
            
        }
    }
    /**************************************交易接口回调结束*************************************/
	
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
    
    public static class TradeThread implements Runnable {
        private TraderMain traderMain;
        
        public TradeThread(TraderMain traderMain){
            this.traderMain = traderMain;
        }
        

        public void run() {
            String dataPath = "ctpdata/test/";
            
//          traderApi = JCTPTraderApi.createFtdcTraderApi();
            traderApi = JCTPTraderApi.createFtdcTraderApi(dataPath);

            traderSpi = new MyTraderSpi(traderApi,traderMain);
            
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
        
    }
    
    public BlockingQueue<FuturesMarket> getMarketQueue() {
        return marketQueue;
    }

    public int getTargetTick() {
        return targetTick;
    }

    public void setTargetTick(int targetTick) {
        this.targetTick = targetTick;
    }
    
    

}
