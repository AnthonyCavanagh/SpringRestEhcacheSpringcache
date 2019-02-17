package com.cav.spring.service.bank.cache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.stereotype.Service;

import com.cav.spring.service.bank.model.accounts.AccountId;
import com.cav.spring.service.bank.model.accounts.AccountPOJO;
import com.cav.spring.service.bank.model.funds.FundPOJO;
import com.cav.spring.service.bank.repository.AccountRepository;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;


@Service
public class AccountCacheServiceImpl implements AccountCacheService {
	
	@Autowired
	private AccountRepository repository;
	
	@Autowired
	private EhCacheManagerFactoryBean ehcache;


	@Override
	/**
	 * an update Accounts, adds new funds to the account, so will add the funds to the cache
	 * only if there is no funds already. This is a many to many mapping.
	 *  So the funds may be in the cache but still needed to be added to the mapping for accounts.
	 *  Funds are added to the cache at creation, as they are the most searched item in this scenario.
	 */
	public void updateAccounts(List<AccountPOJO> accounts) {
		Cache fundCachce = ehcache.getObject().getCache("fundCachce");
		for(AccountPOJO account : accounts){
			for(FundPOJO fund : account.getFunds().getFundList()){
				Element elem = fundCachce.get(fund.getFundId());
				if (elem == null){
					Element element = new Element(fund.getFundId(), fund);
					fundCachce.put(element);
				}
			}
		}
		repository.updateAccounts(accounts);
	}


	@Override
	public List<AccountPOJO> cacheAccount(List<AccountId> accountIds) {
		List<AccountPOJO> returnList = new ArrayList<AccountPOJO>();
		List<Long> requestIds = new ArrayList<Long>();
		Cache accountCachce = ehcache.getObject().getCache("accountCachce");
		for (AccountId accountId : accountIds) {
			Element elem = accountCachce.get(accountId.getId());
			if (elem != null) {
				AccountPOJO account = (AccountPOJO) elem.getObjectValue();
				returnList.add(account);
			} else {
				requestIds.add(accountId.getId());
			}
			if (!requestIds.isEmpty()) {
				List<AccountPOJO> accounts = repository.findAccounts(requestIds);
				for (AccountPOJO account : accounts) {
					Element elemaccount = new Element(account.getAccountid(), account);
					accountCachce.put(elemaccount);
					returnList.add(account);
				}
			}
		}
		return returnList;
	}


	@Override
	public void removeAccounts(List<AccountId> accountIds) {
		Cache accountCachce = ehcache.getObject().getCache("accountCachce");
		List<Long> deleteIds = new ArrayList<Long>();
		for(AccountId accountId : accountIds){
			accountCachce.remove(accountId.getId());
			deleteIds.add(accountId.getId());
		}
		repository.removeAccounts(deleteIds);
	}

}
