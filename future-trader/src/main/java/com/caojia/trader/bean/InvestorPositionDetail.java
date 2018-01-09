package com.caojia.trader.bean;

/**
 * 投资者持仓明细
 * @author caojia
 *
 */
public class InvestorPositionDetail {
    
    //CTP字段
    /**
     * 合约代码 
     */
    private String instrumentID;
    /**
     * 经纪公司代码 
     */
    private String brokerID;
    /**
     * 投资者代码 
     */
    private String investorID;
    /**
     * 投机套保标志 
     */
    private String hedgeFlag;
    /**
     * 买卖方向 
     */
    private String direction;
    /**
     * 开仓日期 
     */
    private String openDate;
    /**
     * 成交编号 
     */
    private String tradeID;
    /**
     * 数量 
     */
    private int volume;
    /**
     * 开仓价 
     */
    private double openPrice;
    /**
     * 交易日 
     */
    private String tradingDay;
    /**
     * 结算编号 
     */
    private String SettlementID;
    /**
     * 成交类型 
     */
    private String TradeType;
    /**
     * 组合合约代码 
     */
    private String CombInstrumentID;

    //自定义字段
    /**
     * 止损价
     */
    private double stopLossPrice;
    
    /**
     * 止盈价
     */
    private double stopWinPrice;
    
    /**
     * 状态 0：持仓   1：平仓中
     */
    private String status = "0";

    public String getInstrumentID() {
        return instrumentID;
    }

    public void setInstrumentID(String instrumentID) {
        this.instrumentID = instrumentID;
    }

    public String getBrokerID() {
        return brokerID;
    }

    public void setBrokerID(String brokerID) {
        this.brokerID = brokerID;
    }

    public String getInvestorID() {
        return investorID;
    }

    public void setInvestorID(String investorID) {
        this.investorID = investorID;
    }

    public String getHedgeFlag() {
        return hedgeFlag;
    }

    public void setHedgeFlag(String hedgeFlag) {
        this.hedgeFlag = hedgeFlag;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getTradeID() {
        return tradeID;
    }

    public void setTradeID(String tradeID) {
        this.tradeID = tradeID;
    }

    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public String getTradingDay() {
        return tradingDay;
    }

    public void setTradingDay(String tradingDay) {
        this.tradingDay = tradingDay;
    }

    public String getSettlementID() {
        return SettlementID;
    }

    public void setSettlementID(String settlementID) {
        SettlementID = settlementID;
    }

    public String getTradeType() {
        return TradeType;
    }

    public void setTradeType(String tradeType) {
        TradeType = tradeType;
    }

    public String getCombInstrumentID() {
        return CombInstrumentID;
    }

    public void setCombInstrumentID(String combInstrumentID) {
        CombInstrumentID = combInstrumentID;
    }

    public double getStopLossPrice() {
        return stopLossPrice;
    }

    public void setStopLossPrice(double stopLossPrice) {
        this.stopLossPrice = stopLossPrice;
    }

    public double getStopWinPrice() {
        return stopWinPrice;
    }

    public void setStopWinPrice(double stopWinPrice) {
        this.stopWinPrice = stopWinPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    
    
}
