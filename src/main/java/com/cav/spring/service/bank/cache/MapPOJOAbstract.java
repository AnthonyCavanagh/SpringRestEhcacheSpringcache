package com.cav.spring.service.bank.cache;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.cav.spring.service.bank.entity.Fund;
import com.cav.spring.service.bank.model.funds.FundPOJO;
import com.cav.spring.service.bank.model.funds.Funds;

public abstract class MapPOJOAbstract {

	/**
	 * 
	 * @param date
	 * @return
	 */
	protected LocalDate mapToLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	protected Funds getFunds(Set<Fund> fundSets) {
		Funds funds = new Funds();
		List<FundPOJO> fundList = fundSets.stream().map(f -> mapFund(f)).collect(Collectors.toList());
		funds.setFundList(fundList);
		return funds;
	}

	private FundPOJO mapFund(Fund fundEntity) {
		FundPOJO fund = new FundPOJO();
		fund.setFundId(fundEntity.getFundId());
		fund.setFundName(fundEntity.getFundName());
		fund.setSell(fundEntity.getSell());
		fund.setBuy(fundEntity.getBuy());
		fund.setYield(fundEntity.getYeild());
		fund.setEffectiveDate(mapToLocalDate(fundEntity.getEffectiveDate()));
		return fund;
	}
}
