package com.cav.spring.service.bank.cache;

import java.util.List;

import com.cav.spring.service.bank.model.banks.BankId;
import com.cav.spring.service.bank.model.banks.BankPOJO;
import com.cav.spring.service.bank.model.banks.Banks;

public interface BankCacheService {
	
	public void createBanks(Banks bank);
	public void updateBanks(Banks bank);
	public List <BankPOJO> cacheBanks(List <BankId> bankIds);
	public void removeBanks(List <BankId> bankIds);

}
