package com.cav.spring.service.bank.cache;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.stereotype.Service;

import com.cav.spring.service.bank.model.accounts.AccountPOJO;
import com.cav.spring.service.bank.model.banks.BankId;
import com.cav.spring.service.bank.model.banks.BankPOJO;
import com.cav.spring.service.bank.model.banks.Banks;
import com.cav.spring.service.bank.model.funds.FundPOJO;
import com.cav.spring.service.bank.repository.BankRepository;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

@Service
public class BankCacheServiceImpl implements BankCacheService{
	
	@Autowired
	private BankRepository repository;
	
	@Autowired
	private EhCacheManagerFactoryBean ehcache;

	@Override
	/**
	 * an create Banks, adds new banks and adds new funds to the bank, so will add the funds to the cache
	 * only if there is no funds already. This is a many to many mapping.
	 *  So the funds may be in the cache but still needed to be added to the mapping for Banks to banks to funds.
	 *  Funds are added to the cache at creation, as they are the most searched item in this scenario.
	 */
	public void createBanks(Banks banks) {
		Cache fundCachce = ehcache.getObject().getCache("fundCachce");
		for(BankPOJO bank :banks.getBankList()){
			for(AccountPOJO account : bank.getAccounts().getAccountsList()){
				for(FundPOJO fund : account.getFunds().getFundList()){
					Element elem = fundCachce.get(fund.getFundId());
					if (elem == null){
						Element element = new Element(fund.getFundId(), fund);
						fundCachce.put(element);
					}
				}
			}
		}
		repository.createBanks(banks.getBankList());
	}

	@Override
	/**
	 * Same rules as for update, only interested in the funds. For keeping the Bank cache
	 * updated rely on, the bank being read, and relying on the Cache Entry Expiration
	 * for stale data.
	 */
	public void updateBanks(Banks banks) {
		Cache fundCachce = ehcache.getObject().getCache("fundCachce");
		for(BankPOJO bank :banks.getBankList()){
			for(AccountPOJO account : bank.getAccounts().getAccountsList()){
				for(FundPOJO fund : account.getFunds().getFundList()){
					Element elem = fundCachce.get(fund.getFundId());
					if (elem == null){
						Element element = new Element(fund.getFundId(), fund);
						fundCachce.put(element);
					}
				}
			}
		}
		repository.updateBanks(banks.getBankList());
		
	}

	@Override
	/**
	 * The only time banks will be cached. As this is about caching, I will cache banks but in 
	 * the real world I would argue against caching, and use the DB only, and rely on lazt loading
	 * with user set flags to see if banks, and funds should be returned.
	 */
	public List<BankPOJO> cacheBanks(List<BankId> bankIds) {
		Cache bankCachce = ehcache.getObject().getCache("bankCachce");
		List<BankPOJO> returnList = new ArrayList<BankPOJO>();
		List<Long> requestIds = new ArrayList<Long>();
		for (BankId bankId : bankIds) {
			Element elem = bankCachce.get(bankId.getId());
			if (elem != null) {
				BankPOJO bank = (BankPOJO) elem.getObjectValue();
				returnList.add(bank);
			} else {
				requestIds.add(bankId.getId());
			}
			if (!requestIds.isEmpty()) {
				List<BankPOJO> banks = repository.findBanks(requestIds);
				for (BankPOJO bank : banks) {
					Element elembank = new Element(bank.getBankId(), bank);
					bankCachce.put(elembank);
					returnList.add(bank);
				}
			}
		}
		return returnList;
	}

	@Override
	public void removeBanks(List<BankId> bankIds) {
		Cache bankCachce = ehcache.getObject().getCache("bankCachce");
		List<Long> ids = new ArrayList<Long>();
		for(BankId bankId :bankIds){
			bankCachce.remove(bankId.getId());
			ids.add(bankId.getId());
		}
		repository.removeBanks(ids);
	}

}
