package com.cav.spring.service.bank.cache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.stereotype.Service;

import com.cav.spring.service.bank.model.funds.FundId;
import com.cav.spring.service.bank.model.funds.FundPOJO;
import com.cav.spring.service.bank.repository.FundRepository;


import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;


@Service
public class FundCacheServiceImpl implements FundCacheService {

	@Autowired
	private FundRepository repository;

	@Autowired
	private EhCacheManagerFactoryBean ehcache;


	@Override
	/**
	 * Updating stale data in the cache by relying on the Cache Entry Expiration
	 * But another way is write through cache
	 */
	public void updateFunds(List<FundPOJO> funds) {
		repository.updateFunds(funds);

	}

	@Override
	/**
	 * Will cache all funds, read by the users
	 */
	public List<FundPOJO> casheFunds(List<FundId> fundIds) {
		List<FundPOJO> returnList = new ArrayList<FundPOJO>();
		List<Long> requestIds = new ArrayList<Long>();
		Cache fundCachce = ehcache.getObject().getCache("fundCachce");
		for (FundId findId : fundIds) {
			Element elem = fundCachce.get(findId.getId());
			if (elem != null) {
				FundPOJO fund = (FundPOJO) elem.getObjectValue();
				returnList.add(fund);
			} else {
				requestIds.add(findId.getId());
			}
			if (!requestIds.isEmpty()) {
				List<FundPOJO> funds = repository.findFunds(requestIds);
				for (FundPOJO fund : funds) {
					Element elemFund = new Element(fund.getFundId(), fund);
					fundCachce.put(elemFund);
					returnList.add(fund);
				}
			}
		}
		return returnList;
	}


	@Override
	/**
	 *  will delete all funds from the cache as well as the DB.
	 */
	public void removeFunds(List<FundId> fundIds) {
		List<Long> deleteIds = new ArrayList<Long>();
		Cache fundCachce = ehcache.getObject().getCache("fundCachce");
		for (FundId findId : fundIds) {
			fundCachce.remove(findId.getId());
			deleteIds.add(findId.getId());
		}
		repository.removeFunds(deleteIds);
	}
}
