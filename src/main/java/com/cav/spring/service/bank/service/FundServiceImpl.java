package com.cav.spring.service.bank.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Import;
import org.springframework.stereotype.Service;

import com.cav.spring.service.bank.cache.FundCacheService;
import com.cav.spring.service.bank.entity.Fund;
import com.cav.spring.service.bank.model.funds.FundId;
import com.cav.spring.service.bank.model.funds.FundPOJO;
import com.cav.spring.service.bank.model.funds.Funds;
import com.cav.spring.service.bank.repository.FundRepository;


@Service
public class FundServiceImpl implements FundService {
	
	@Autowired
	private FundCacheService cacheService;

	/*
	public Funds listFunds(List<FundId> fundIds) {
		List <FundPOJO> fundList = new ArrayList<FundPOJO>();
		for(FundId fundId : fundIds){
			fundList.add(cacheService.cacheFund(fundId.getId()));
		}
		Funds funds = new Funds();
		funds.setFundList(fundList);
		return funds;
	}*/
	
	@Override
	public Funds listFunds(List<FundId> fundIds) {
		Funds funds = new Funds();
		List<FundPOJO> fundList = cacheService.casheFunds(fundIds);
		funds.setFundList(fundList);
		return funds;
	}
	

	public Funds listAllFunds() {
		// TODO Auto-generated method stub
		return null;
	}

	public void removeFunds(List<FundId> fundId) {
		// TODO Auto-generated method stub
	}
	

	@Override
	public void updateFunds(Funds funds) {
		cacheService.updateFunds(funds.getFundList());
		
	}
	

	
	
}
