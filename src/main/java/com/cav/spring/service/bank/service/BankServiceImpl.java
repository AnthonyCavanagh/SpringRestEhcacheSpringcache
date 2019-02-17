package com.cav.spring.service.bank.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cav.spring.service.bank.cache.BankCacheService;
import com.cav.spring.service.bank.model.banks.BankId;
import com.cav.spring.service.bank.model.banks.BankPOJO;
import com.cav.spring.service.bank.model.banks.Banks;

@Service
public class BankServiceImpl implements BankService{

	@Autowired
	private BankCacheService cacheService;
	
	
	@Override
	public void createBanks(Banks banks) {
		cacheService.createBanks(banks);
	}

	@Override
	public void updateBanks(Banks banks) {
		cacheService.updateBanks(banks);
		
	}

	@Override
	public Banks listBanks(List<BankId> bankIds) {
		List<BankPOJO> bankList = cacheService.cacheBanks(bankIds);
		Banks banks = new Banks();
		banks.setBankList(bankList);
		return banks;
	}

	@Override
	public void removeBanks(List<BankId> bankIds) {
		cacheService.removeBanks(bankIds);
	}

}
