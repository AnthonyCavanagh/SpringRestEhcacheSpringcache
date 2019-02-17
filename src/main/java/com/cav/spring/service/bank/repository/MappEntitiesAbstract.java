package com.cav.spring.service.bank.repository;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.cav.spring.service.bank.entity.Account;
import com.cav.spring.service.bank.entity.Bank;
import com.cav.spring.service.bank.entity.Fund;
import com.cav.spring.service.bank.model.accounts.AccountPOJO;
import com.cav.spring.service.bank.model.accounts.Accounts;
import com.cav.spring.service.bank.model.banks.BankPOJO;
import com.cav.spring.service.bank.model.banks.Banks;
import com.cav.spring.service.bank.model.funds.FundPOJO;
import com.cav.spring.service.bank.model.funds.Funds;

public abstract class MappEntitiesAbstract {
	
	
	/**
	 * 
	 * @param banksList
	 * @param account
	 * @param fund
	 * @return
	 */
	protected List<BankPOJO> mapBanks(List<Bank> bankEntities, boolean account, boolean fund) {
		Banks banks = new Banks();
		List<BankPOJO> bankList = bankEntities.stream().map(b -> mapBank(b, account, fund)).collect(Collectors.toList());;
		return bankList;
	}
	
	/**
	 * Creates Bank Entitys
	 * @param b
	 * @return
	 */
	protected Bank createBankEntity(BankPOJO b) {
		Bank be = new Bank();
		be.setActive(b.getActive());
		be.setBankCode(b.getBankCode());
		be.setBankId(b.getBankId());
		be.setBankName(b.getBankName());
		be.setContactDetailsCode(b.getContactDetailsCode());
		be.setEffectiveDate(mappDateToEntity(b.getEffectiveDate()));
		List<Account> accounts = createAccountEntities(b.getAccounts());
		for (Account account : accounts) {
			be.setAccounts(account);
		}
		return be;
	}
	
	/**
	 * 
	 * @param bank
	 * @param bankEntity
	 * @return
	 */
	protected Bank updateBankEntity(BankPOJO bank, Bank bankEntity) {
		if(bank.getBankName() != null) {
			bankEntity.setBankName(bank.getBankName());
		} 
		if(bank.getBankCode() != null) {
			bankEntity.setBankCode(bank.getBankCode());
		}
		if(bank.getContactDetailsCode() != null) {
			bankEntity.setContactDetailsCode(bank.getContactDetailsCode());
		}
		if(bank.getActive() != null) {
			bankEntity.setActive(bank.getActive());
		}
		List<Account> accounts = createAccountEntities(bank.getAccounts());
		for (Account account : accounts) {
			bankEntity.setAccounts(account);
		}
		return bankEntity;
	}
	
	/**
	 * 
	 * @param accountList
	 * @param fund
	 * @return
	 */
	protected List<AccountPOJO> mapAccounts(List <Account> accountList, boolean fund) {
		Accounts accounts = new Accounts();
		List<AccountPOJO> accountPOJOList = accountList.stream().map(a -> mapAccount(a,fund)).collect(Collectors.toList());
		return accountPOJOList;
	}
	
	/**
	 * Updates Account Entity
	 * @param account
	 * @param ae
	 * @return
	 */
	protected Account updateAccountEntity(AccountPOJO account, Account accountEntity) {
		if(account.getAccountName() != null) {
			accountEntity.setAccountName(account.getAccountName());
		}
		List<Fund> fundEntities = createFundEntities(account.getFunds());
		for (Fund fund : fundEntities) {
			accountEntity.setFunds(fund);
		}
		return accountEntity;
	}
	
	/**
	 * 
	 * @param fundList
	 * @return
	 */
	protected List<FundPOJO> mapFunds(List<Fund> fundList) {
		Funds funds = new Funds();
		List<FundPOJO> fundPOJOList = fundList.stream().map(f -> mapFund(f)).collect(Collectors.toList());
		return fundPOJOList;
	}
	
	/**
	 * 
	 * @param fund
	 * @param fe
	 * @return
	 */
	protected Fund updateFundEntity(FundPOJO fund, Fund fe) {
		if(fund.getFundName() != null){
			fe.setFundName(fund.getFundName());
		}
		if(fund.getSell() != null){
			fe.setSell(fund.getSell());
		}
		if(fund.getBuy() != null){
			fe.setBuy(fund.getBuy());
		}
		if(fund.getYield() != null){
			fe.setYeild(fund.getYield());
		}
		return fe;
	}
	
	
	private BankPOJO mapBank(Bank be, boolean accounts, boolean funds) {
		BankPOJO bank = new BankPOJO();
		bank.setActive(be.getActive());
		bank.setBankCode(be.getBankCode());
		bank.setBankId(be.getBankId());
		bank.setBankName(be.getBankName());
		bank.setContactDetailsCode(be.getContactDetailsCode());
		bank.setEffectiveDate(mapToLocalDate(be.getEffectiveDate()));
		if (accounts) {
			bank.setAccounts(mapAccounts(be.getAccounts(), funds));
		}
		return bank;
	}
	
	private Accounts mapAccounts(Set<Account> accountSet, boolean fund) {
		Accounts accounts = new Accounts();
		List<AccountPOJO> accountPOJOList = accountSet.stream().map(f -> mapAccount(f,fund)).collect(Collectors.toList());
		accounts.setAccountsList(accountPOJOList);
		return accounts;
	}
	
	private AccountPOJO mapAccount(Account accountEntity, boolean funds) {
		AccountPOJO account = new AccountPOJO();
		account.setAccountid(accountEntity.getAccountid());
		account.setAccountName(accountEntity.getAccountName());
		account.setEffectiveDate(mapToLocalDate(accountEntity.getEffectiveDate()));
		if (funds) {
			account.setFunds(mapFunds(accountEntity.getFunds()));
		}
		return account;
	}
	
	private List<Account> createAccountEntities(Accounts accounts) {
		if (accounts == null) {
			return Collections.emptyList();
		}
		List<AccountPOJO> accountList = accounts.getAccountsList();
		List<Account> entities = accountList.stream().map(a -> createAccountEntity(a)).collect(Collectors.toList());
		return entities;
	}
		
	private Account createAccountEntity(AccountPOJO account) {
		Account accountEntity = new Account();
		accountEntity.setAccountid(account.getAccountid());
		accountEntity.setAccountName(account.getAccountName());
		accountEntity.setEffectiveDate(mappDateToEntity(account.getEffectiveDate()));
		List<Fund> fundEntities = createFundEntities(account.getFunds());
		for (Fund fund : fundEntities) {
			accountEntity.setFunds(fund);
		}
		return accountEntity;
	}
	
	
	private List<Fund> createFundEntities(Funds funds) {
		if (funds == null) {
			return Collections.emptyList();
		}
		List<FundPOJO> fundList = funds.getFundList();
		List<Fund> entities = fundList.stream().map(f -> createFundEntity(f)).collect(Collectors.toList());
		return entities;
	}
	  
	private Fund createFundEntity(FundPOJO fund) {
		Fund fundEntity = new Fund();
		fundEntity.setFundId(fund.getFundId());
		fundEntity.setFundName(fund.getFundName());
		fundEntity.setSell(fund.getSell());
		fundEntity.setBuy(fund.getBuy());
		fundEntity.setYeild(fund.getYield());
		fundEntity.setEffectiveDate(mappDateToEntity(fund.getEffectiveDate()));
		return fundEntity;
	}
	
	private Funds mapFunds(Set<Fund> fundSet) {
		Funds funds = new Funds();
		List<FundPOJO> fundPOJOList = fundSet.stream().map(f -> mapFund(f)).collect(Collectors.toList());
		funds.setFundList(fundPOJOList);
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
	
	public LocalDate mapToLocalDate(Date date) {
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}
	
	private Date mappDateToEntity(LocalDate date) {
		if (date != null) {
			return Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant());
		}
		return null;
	}


}
