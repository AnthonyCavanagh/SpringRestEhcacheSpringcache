package com.cav.spring.service.bank.cache;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cav.spring.service.bank.model.accounts.AccountPOJO;
import com.cav.spring.service.bank.model.funds.FundPOJO;
import com.cav.spring.service.bank.repository.AccountRepository;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;


public class AccountCacheServiceTest extends AbstractcacheTest {

	@InjectMocks
	protected AccountCacheService service = new AccountCacheServiceImpl();
	
	@Mock
	private AccountRepository repository;
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void updateAccountsFunds(){
		Cache cache = createCache("fundCachce");
		when(ehcacheMock.getObject()).thenReturn(cacheManager);
		when(cacheManager.getCache("fundCachce")).thenReturn(cache);
		
		// Will add newly created funds to the cache.
		service.updateAccounts(createAccountFund(DEFAULT_ACCOUNT_ID, DEFAULT_FUND_ID).getAccountsList());
		FundPOJO fund  = (FundPOJO) cache.get(DEFAULT_FUND_ID).getObjectValue();
		assertEquals(DEFAULT_FUND_ID, fund.getFundId());
		cache.removeAll();
	}
	
	@Test
	public void cacheAccountAccountInCacheTest(){
		Cache cache = createCache("accountCachce");
		AccountPOJO account = createaccount(DEFAULT_ACCOUNT_ID);
		Element element =  new Element(account.getAccountid(), account);
		cache.put(element);
		
		when(ehcacheMock.getObject()).thenReturn(cacheManager);
		when(cacheManager.getCache("accountCachce")).thenReturn(cache);
		createAccountsIds(DEFAULT_ACCOUNT_ID);
		service.cacheAccount(createAccountsIds(DEFAULT_ACCOUNT_ID));
		account = (AccountPOJO) cache.get(DEFAULT_ACCOUNT_ID).getObjectValue();
		assertEquals(DEFAULT_ACCOUNT_ID,account.getAccountid());
		cache.removeAll();
	}
	
	@Test
	public void removeAccountsTest(){
		Cache cache = createCache("accountCachce");
		AccountPOJO account = createaccount(DEFAULT_ACCOUNT_ID);
		Element element =  new Element(account.getAccountid(), account);
		cache.put(element);
		
		when(ehcacheMock.getObject()).thenReturn(cacheManager);
		when(cacheManager.getCache("accountCachce")).thenReturn(cache);
		createAccountsIds(DEFAULT_ACCOUNT_ID);
		service.removeAccounts(createAccountsIds(DEFAULT_ACCOUNT_ID));
		assertNull(cache.get(DEFAULT_ACCOUNT_ID));
	}

}
