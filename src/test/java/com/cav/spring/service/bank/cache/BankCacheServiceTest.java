package com.cav.spring.service.bank.cache;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.cav.spring.service.bank.model.banks.BankPOJO;
import com.cav.spring.service.bank.model.banks.Banks;
import com.cav.spring.service.bank.model.funds.FundPOJO;
import com.cav.spring.service.bank.repository.BankRepository;

import net.sf.ehcache.Cache;



public class BankCacheServiceTest extends AbstractcacheTest {

	@InjectMocks
	protected BankCacheService service = new BankCacheServiceImpl();
	
	@Mock
	private BankRepository repository;
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}
	
	@Test
	public void createBanksTest(){
		//When creating a new bank only funds are cached
		Cache cache = createCache("fundCachce");
		when(ehcacheMock.getObject()).thenReturn(cacheManager);
		when(cacheManager.getCache("fundCachce")).thenReturn(cache);
		Banks banks = new Banks();
		banks.getBankList().addAll(createBankAccountFund(DEFAULT_BANK_ID, DEFAULT_ACCOUNT_ID, DEFAULT_FUND_ID));
		service.createBanks(banks);
		
		FundPOJO fund  = (FundPOJO) cache.get(DEFAULT_FUND_ID).getObjectValue();
		assertEquals(DEFAULT_FUND_ID, fund.getFundId());
		cache.removeAll();
	}
	
	

	@Test
	public void updateBanksTest(){
		//When creating a new bank only funds are cached
		Cache cache = createCache("fundCachce");
		when(ehcacheMock.getObject()).thenReturn(cacheManager);
		when(cacheManager.getCache("fundCachce")).thenReturn(cache);
		Banks banks = new Banks();
		banks.getBankList().addAll(createBankAccountFund(DEFAULT_BANK_ID, DEFAULT_ACCOUNT_ID, DEFAULT_FUND_ID));
		service.createBanks(banks);
		
		FundPOJO fund  = (FundPOJO) cache.get(DEFAULT_FUND_ID).getObjectValue();
		assertEquals(DEFAULT_FUND_ID, fund.getFundId());
		cache.removeAll();
	}
	
	@Test
	public void cacheBanksTest(){
		Cache cache = createCache("bankCachce");
		when(ehcacheMock.getObject()).thenReturn(cacheManager);
		when(cacheManager.getCache("bankCachce")).thenReturn(cache);
		// Will add a bank to the bank cache from the repos.
		when(repository.findBanks(createIds(DEFAULT_BANK_ID))).thenReturn(createBankAccountFund(DEFAULT_BANK_ID, DEFAULT_ACCOUNT_ID, DEFAULT_FUND_ID));
		service.cacheBanks(createBankIds(DEFAULT_BANK_ID));
		
		BankPOJO bank  = (BankPOJO) cache.get(DEFAULT_BANK_ID).getObjectValue();
		assertEquals(DEFAULT_BANK_ID, bank.getBankId());
		cache.removeAll();
	}
}
