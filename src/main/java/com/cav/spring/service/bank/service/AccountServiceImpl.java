package com.cav.spring.service.bank.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cav.spring.service.bank.cache.AccountCacheService;
import com.cav.spring.service.bank.model.accounts.AccountId;
import com.cav.spring.service.bank.model.accounts.AccountPOJO;
import com.cav.spring.service.bank.model.accounts.Accounts;
import com.cav.spring.service.bank.model.funds.FundPOJO;


@Service
public class AccountServiceImpl implements AccountService{

	@Autowired
	private AccountCacheService cacheService;
	
	@Override
	public void updateAccounts(Accounts accounts) {
		cacheService.updateAccounts(accounts.getAccountsList());
		
	}

	@Override
	public Accounts listAccounts(List<AccountId> accountIds) {
		List <AccountPOJO> accountsList = new ArrayList<AccountPOJO>();
		cacheService.cacheAccount(accountIds);
		Accounts accounts = new Accounts();
		accounts.setAccountsList(accountsList);
		return accounts;
	}

	@Override
	public void removeAccounts(List<AccountId> accountIds) {
		cacheService.removeAccounts(accountIds);
	}

}
