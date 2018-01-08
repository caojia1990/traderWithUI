package com.caojia.trader.service.impl;

import org.apache.log4j.Logger;
import org.hraink.futures.ctp.thostftdcuserapistruct.CThostFtdcDepthMarketDataField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.caojia.trader.dao.FutureMarketDaoImpl;
import com.caojia.trader.service.FutureMarketService;

@Service("futureMarketService")
public class FutureMarketServiceImpl implements FutureMarketService{
	
	static Logger logger = Logger.getLogger(FutureMarketServiceImpl.class);
	
	@Autowired
	private FutureMarketDaoImpl futureMarketDao;

	public void saveFutureMarket(CThostFtdcDepthMarketDataField dataField,int volumeChange,int openInterestChange) {
		
		try {
            int result = this.futureMarketDao.insert(dataField,volumeChange,openInterestChange);
        } catch (Exception e) {
            logger.error("保存行情数据失败",e);
        }
		/*if(result > 0 ){
			logger.debug("保存行情成功,合约代码： "+dataField.getInstrumentID());
		}else {
			logger.error("保存行情失败");
		}*/
		
	}
	
	

}
