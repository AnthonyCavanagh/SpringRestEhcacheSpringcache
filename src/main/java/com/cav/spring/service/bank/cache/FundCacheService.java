package com.cav.spring.service.bank.cache;

import java.util.List;

import com.cav.spring.service.bank.entity.Fund;
import com.cav.spring.service.bank.model.funds.FundId;
import com.cav.spring.service.bank.model.funds.FundPOJO;

public interface FundCacheService {

	public void updateFunds(List<FundPOJO> funds);
	public List<FundPOJO> casheFunds(List <FundId> fIds);
	public void removeFunds(List<FundId> fundIds);
}
