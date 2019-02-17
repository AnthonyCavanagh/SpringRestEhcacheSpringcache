package com.cav.spring.service.bank.cache;

import java.util.List;

import com.cav.spring.service.bank.model.accounts.AccountId;
import com.cav.spring.service.bank.model.accounts.AccountPOJO;


public interface AccountCacheService {
	
	public List<AccountPOJO> cacheAccount(List<AccountId> accountIds);
	public void updateAccounts(List<AccountPOJO> accounts);
	public void removeAccounts(List <AccountId> accountIds);
}
