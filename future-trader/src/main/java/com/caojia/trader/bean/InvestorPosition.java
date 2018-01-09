package com.caojia.trader.bean;

/**
 * 投资者持仓信息
 * @author caojia
 *
 */
public class InvestorPosition {

    //合约代码
    private String instrumentID;
    
    //经纪公司代码
    private String brokerID;
    
    //投资者代码
    private String investorID;
    
    //持仓多空方向  
    private String posiDirection;
    
    //投机套保标志
    private String hedgeFlag;
    
    //持仓日期  
    private String positionDate;
    
    //上日持仓
    private int ydPosition;
    
    //今日持仓
    private int position;
    
    //多头冻结
    private int longFrozen;
    
    //空头冻结
    private int shortFrozen;
    
    //开仓冻结金额
    private double longFrozenAmount;
    
    //开仓冻结金额
    private double shortFrozenAmount;
    
    //开仓量
    private int openVolume;
    
    //平仓量
    private int closeVolume;
    
    //开仓金额
    private double openAmount;
    
    //平仓金额
    private double closeAmount;
    
    //持仓成本
    private double positionCost;
    
    //上次占用的保证金
    private double preMargin;
    
    //占用的保证金
    private double useMargin;
    
    //冻结的保证金
    private double frozenMargin;
    
    //冻结的资金
    private double frozenCash;
    
    //冻结的手续费
    private double frozenCommission;
    
    //资金差额
    private double cashIn;
    
    //手续费
    private double commission;  
    
    //平仓盈亏
    private double closeProfit;
    
    //持仓盈亏
    private double positionProfit;
    
    //上次结算价
    private double preSettlementPrice;
    
    //本次结算价
    private double settlementPrice;
    
    //交易日
    private String tradingDay;
    
    //结算编号
    private String settlementID;

    /**
     * @return 合约代码
     */
    public String getInstrumentID() {
        return instrumentID;
    }

    public void setInstrumentID(String instrumentID) {
        this.instrumentID = instrumentID;
    }

    /**
     * 
     * @return 经纪公司代码
     */
    public String getBrokerID() {
        return brokerID;
    }

    public void setBrokerID(String brokerID) {
        this.brokerID = brokerID;
    }

    /**
     * 
     * @return 投资者代码 
     */
    public String getInvestorID() {
        return investorID;
    }

    public void setInvestorID(String investorID) {
        this.investorID = investorID;
    }

    /**
     * 
     * @return 持仓多空方向  
     */
    public String getPosiDirection() {
        return posiDirection;
    }

    public void setPosiDirection(String posiDirection) {
        this.posiDirection = posiDirection;
    }

    /**
     * 
     * @return 投机套保标志
     */
    public String getHedgeFlag() {
        return hedgeFlag;
    }

    public void setHedgeFlag(String hedgeFlag) {
        this.hedgeFlag = hedgeFlag;
    }

    /**
     * 
     * @return 持仓日期
     */
    public String getPositionDate() {
        return positionDate;
    }

    public void setPositionDate(String positionDate) {
        this.positionDate = positionDate;
    }

    /**
     * 
     * @return 上日持仓
     */
    public int getYdPosition() {
        return ydPosition;
    }

    public void setYdPosition(int ydPosition) {
        this.ydPosition = ydPosition;
    }

    /**
     * 
     * @return 今日持仓
     */
    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    /**
     * 
     * @return 多头冻结
     */
    public int getLongFrozen() {
        return longFrozen;
    }

    public void setLongFrozen(int longFrozen) {
        this.longFrozen = longFrozen;
    }

    /**
     * 
     * @return 空头冻结
     */
    public int getShortFrozen() {
        return shortFrozen;
    }

    public void setShortFrozen(int shortFrozen) {
        this.shortFrozen = shortFrozen;
    }

    /**
     * 
     * @return 开仓冻结金额
     */
    public double getLongFrozenAmount() {
        return longFrozenAmount;
    }

    public void setLongFrozenAmount(double longFrozenAmount) {
        this.longFrozenAmount = longFrozenAmount;
    }

    /**
     * 
     * @return 开仓冻结金额
     */
    public double getShortFrozenAmount() {
        return shortFrozenAmount;
    }

    public void setShortFrozenAmount(double shortFrozenAmount) {
        this.shortFrozenAmount = shortFrozenAmount;
    }

    /**
     * 
     * @return 开仓量
     */
    public int getOpenVolume() {
        return openVolume;
    }

    public void setOpenVolume(int openVolume) {
        this.openVolume = openVolume;
    }

    /**
     * 
     * @return 平仓量
     */
    public int getCloseVolume() {
        return closeVolume;
    }

    public void setCloseVolume(int closeVolume) {
        this.closeVolume = closeVolume;
    }

    /**
     * 
     * @return 开仓金额
     */
    public double getOpenAmount() {
        return openAmount;
    }

    public void setOpenAmount(double openAmount) {
        this.openAmount = openAmount;
    }

    /**
     * 
     * @return 平仓金额
     */
    public double getCloseAmount() {
        return closeAmount;
    }

    public void setCloseAmount(double closeAmount) {
        this.closeAmount = closeAmount;
    }

    /**
     * 
     * @return 持仓成本
     */
    public double getPositionCost() {
        return positionCost;
    }

    public void setPositionCost(double positionCost) {
        this.positionCost = positionCost;
    }

    /**
     * 
     * @return 上次占用的保证金
     */
    public double getPreMargin() {
        return preMargin;
    }

    public void setPreMargin(double preMargin) {
        this.preMargin = preMargin;
    }

    /**
     * 
     * @return 占用的保证金
     */
    public double getUseMargin() {
        return useMargin;
    }

    public void setUseMargin(double useMargin) {
        this.useMargin = useMargin;
    }

    /**
     * 
     * @return 冻结的保证金
     */
    public double getFrozenMargin() {
        return frozenMargin;
    }

    public void setFrozenMargin(double frozenMargin) {
        this.frozenMargin = frozenMargin;
    }

    /**
     * 
     * @return 冻结的资金
     */
    public double getFrozenCash() {
        return frozenCash;
    }

    public void setFrozenCash(double frozenCash) {
        this.frozenCash = frozenCash;
    }

    /**
     * 
     * @return 冻结的手续费
     */
    public double getFrozenCommission() {
        return frozenCommission;
    }

    public void setFrozenCommission(double frozenCommission) {
        this.frozenCommission = frozenCommission;
    }

    /**
     * 
     * @return 资金差额
     */
    public double getCashIn() {
        return cashIn;
    }

    public void setCashIn(double cashIn) {
        this.cashIn = cashIn;
    }

    /**
     * 
     * @return 手续费
     */
    public double getCommission() {
        return commission;
    }

    public void setCommission(double commission) {
        this.commission = commission;
    }

    /**
     * 
     * @return 平仓盈亏
     */
    public double getCloseProfit() {
        return closeProfit;
    }

    public void setCloseProfit(double closeProfit) {
        this.closeProfit = closeProfit;
    }

    /**
     * 
     * @return 持仓盈亏
     */
    public double getPositionProfit() {
        return positionProfit;
    }

    public void setPositionProfit(double positionProfit) {
        this.positionProfit = positionProfit;
    }

    /**
     * 
     * @return 上次结算价
     */
    public double getPreSettlementPrice() {
        return preSettlementPrice;
    }

    public void setPreSettlementPrice(double preSettlementPrice) {
        this.preSettlementPrice = preSettlementPrice;
    }

    /**
     * 
     * @return 本次结算价
     */
    public double getSettlementPrice() {
        return settlementPrice;
    }

    public void setSettlementPrice(double settlementPrice) {
        this.settlementPrice = settlementPrice;
    }

    /**
     * 
     * @return 交易日
     */
    public String getTradingDay() {
        return tradingDay;
    }

    public void setTradingDay(String tradingDay) {
        this.tradingDay = tradingDay;
    }

    /**
     * 
     * @return 结算编号
     */
    public String getSettlementID() {
        return settlementID;
    }

    public void setSettlementID(String settlementID) {
        this.settlementID = settlementID;
    }
    
    
}
