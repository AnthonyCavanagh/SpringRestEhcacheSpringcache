package com.cav.spring.service.bank.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import com.cav.spring.service.bank.model.funds.FundId;
import com.cav.spring.service.bank.model.funds.FundPOJO;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;


import static org.mockito.Mockito.when;


import static org.junit.Assert.assertEquals;


public class FundCacheServiceTest extends AbstractcacheTest {

	@InjectMocks
	protected FundCacheService service = new FundCacheServiceImpl();
	
	@Before
	public void initMocks() {
		MockitoAnnotations.initMocks(this);
	}

	@Test
	public void testreturnFundFromDB() {
		Cache cache = createCache("fundCachce");

		List<Long> flist = new ArrayList<Long>();
		flist.add(1234L);
		
		when(ehcacheMock.getObject()).thenReturn(cacheManager);
		when(cacheManager.getCache("fundCachce")).thenReturn(cache);
		List <FundPOJO> fundList = new ArrayList<FundPOJO>();
		fundList.add(createFund(1234L));
		when(fundRepository.findFunds(flist)).thenReturn(fundList);
		
		List<FundId> fundIds = new ArrayList<FundId>();
		FundId fundId = new FundId();
		fundId.setId(1234L);
		fundIds.add(fundId); 
		fundList = service.casheFunds(fundIds);
		
		//Will return the fund from the repos
		assertEquals(1234L,fundList.iterator().next().getFundId());
	}
	
	@Test
	public void testreturnFundFromCache() {
		FundPOJO fund = createFund(1234L);
		Cache cache = createCache("fundCachce");
		Element elemFund = new Element(fund.getFundId(), fund);
		cache.put(elemFund);

		List<Long> flist = new ArrayList<Long>();
		flist.add(1234L);
		
		when(ehcacheMock.getObject()).thenReturn(cacheManager);
		when(cacheManager.getCache("fundCachce")).thenReturn(cache);
		List <FundPOJO> fundList = new ArrayList<FundPOJO>();
		fundList.add(createFund(1234L));
		when(fundRepository.findFunds(flist)).thenReturn(Collections.EMPTY_LIST);
		
		List<FundId> fundIds = new ArrayList<FundId>();
		FundId fundId = new FundId();
		fundId.setId(1234L);
		fundIds.add(fundId); 
		fundList = service.casheFunds(fundIds);
		
		assertEquals(1234L,fundList.iterator().next().getFundId());
	}
	
	@Test
	public void testreturnFundFromCacheAndDB() {
		FundPOJO fund = createFund(1234L);
		Cache cache = createCache("fundCachce");
		Element elemFund = new Element(fund.getFundId(), fund);
		cache.put(elemFund);

		List<Long> flist = new ArrayList<Long>();
		flist.add(1235L);
		
		when(ehcacheMock.getObject()).thenReturn(cacheManager);
		when(cacheManager.getCache("fundCachce")).thenReturn(cache);
		List <FundPOJO> fundList = new ArrayList<FundPOJO>();
		fundList.add(createFund(1235L));
		when(fundRepository.findFunds(flist)).thenReturn(fundList);
		
		List<FundId> fundIds = new ArrayList<FundId>();
		FundId fundId = new FundId();
		fundId.setId(1234L);
		fundIds.add(fundId); 
		fundId = new FundId();
		fundId.setId(1235L);
		fundIds.add(fundId);
		fundList = service.casheFunds(fundIds);
		Iterator<FundPOJO> iter = fundList.iterator();
		assertEquals(1234L,iter.next().getFundId());
		assertEquals(1235L,iter.next().getFundId());
	}
	
}
