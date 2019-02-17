package com.cav.spring.service.bank.cache;

import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import com.cav.spring.service.bank.AbstractTest;
import com.cav.spring.service.bank.repository.AccountRepository;
import com.cav.spring.service.bank.repository.FundRepository;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@ContextConfiguration(locations = "classpath:springTest.xml")
public abstract class AbstractcacheTest extends AbstractTest {
	
	@Autowired
	protected EhCacheManagerFactoryBean ehcache;

	@Mock
	protected FundRepository fundRepository;
	
	
	
	@Mock
	protected EhCacheManagerFactoryBean ehcacheMock;
	
	@Mock
	protected CacheManager cacheManager;
	
	
	protected Cache createCache(String name) {
		return ehcache.getObject().getCache(name);
	}

}
