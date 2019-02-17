package com.cav.spring.service.bank.repository;

import java.util.List;

import com.cav.spring.service.bank.entity.Account;
import com.cav.spring.service.bank.model.accounts.AccountId;
import com.cav.spring.service.bank.model.accounts.AccountPOJO;
import com.cav.spring.service.bank.model.accounts.Accounts;



public interface AccountRepository {
	
	public List<AccountPOJO> findAccounts(List<Long> accountIds);
	public void updateAccounts(List<AccountPOJO> accounts);
	public void removeAccounts(List<Long> accountIds);
	public void removeFunds(List<AccountId> accountIds);

}
