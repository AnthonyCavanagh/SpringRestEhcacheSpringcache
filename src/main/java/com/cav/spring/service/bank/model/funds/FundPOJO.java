package com.cav.spring.service.bank.model.funds;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;

public class FundPOJO {
	private long fundId;
    private String fundName;
    private BigDecimal buy;
    private BigDecimal  sell;
    private Float yield;
    
    //@JsonDeserialize(using = LocalDateDeserializer.class)
    //@JsonSerialize(using = LocalDateSerializer.class)
    private LocalDate effectiveDate;
    private String active;

	public FundPOJO() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
	public FundPOJO(long fundId, String fundName, BigDecimal buy, BigDecimal sell, Float yield, LocalDate effectiveDate,
			String active) {
		super();
		this.fundId = fundId;
		this.fundName = fundName;
		this.buy = buy;
		this.sell = sell;
		this.yield = yield;
		this.effectiveDate = effectiveDate;
		this.active = active;
	}


	public long getFundId() {
		return fundId;
	}
	public void setFundId(long fundId) {
		this.fundId = fundId;
	}
	public String getFundName() {
		return fundName;
	}
	public void setFundName(String fundName) {
		this.fundName = fundName;
	}
	public BigDecimal getBuy() {
		return buy;
	}
	public void setBuy(BigDecimal buy) {
		this.buy = buy;
	}
	public BigDecimal getSell() {
		return sell;
	}
	public void setSell(BigDecimal sell) {
		this.sell = sell;
	}
	public Float getYield() {
		return yield;
	}
	public void setYield(Float yield) {
		this.yield = yield;
	}
	public LocalDate getEffectiveDate() {
		return effectiveDate;
	}
	public void setEffectiveDate(LocalDate effectiveDate) {
		this.effectiveDate = effectiveDate;
	}

	public String getActive() {
		return active;
	}
	public void setActive(String active) {
		this.active = active;
	}
	@Override
	public String toString() {
		return "FundPOJO [fundId=" + fundId + ", fundName=" + fundName + ", buy=" + buy + ", sell=" + sell + ", yield="
				+ yield + ", effectiveDate=" + effectiveDate + ", active=" + active + "]";
	}
}
