package com.cav.spring.service.bank.repository;


import java.util.List;

import com.cav.spring.service.bank.entity.Fund;
import com.cav.spring.service.bank.model.accounts.AccountPOJO;
import com.cav.spring.service.bank.model.funds.FundId;
import com.cav.spring.service.bank.model.funds.FundPOJO;
import com.cav.spring.service.bank.model.funds.Funds;

public interface FundRepository  {
	
	public List<FundPOJO> findFunds(List<Long>fundIdS);
	public void updateFunds(List<FundPOJO> funds);
	public void removeFunds(List<Long> fundIds);

}
