package com.cav.spring.service.bank.repository;

import java.util.Iterator;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.cav.spring.service.bank.model.accounts.AccountPOJO;
import com.cav.spring.service.bank.model.banks.BankPOJO;
import com.cav.spring.service.bank.model.banks.Banks;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class BankRepositoryTest extends AbstractRepositoryTest {

	@Before
	public void setUp(){
		super.setUp();
	}
	
	@After
	public void cleanUp(){
		super.cleanUp();
	}
	
	@Test
	public void testCreateBank(){
		List<BankPOJO> banks = bankRepository.findBanks(createIds(DEFAULT_BANK_ID));
		assertEquals(DEFAULT_BANK_ID,banks.iterator().next().getBankId());
	}
	
	@Test
	public void testUpdateBank(){	
		BankPOJO bank = createBank(DEFAULT_BANK_ID);
		bank.setBankName("UpDatedBankName");
		Banks banks = new Banks();
		banks.getBankList().add(bank);
		bankRepository.updateBanks(banks.getBankList());
		
		List<BankPOJO> bankList = bankRepository.findBanks(createIds(DEFAULT_BANK_ID));
		bank = bankList.iterator().next();
		assertEquals(DEFAULT_BANK_ID,bank.getBankId());
		assertEquals("UpDatedBankName",bank.getBankName());
	}
	
	@Test
	public void addSecondAccountToBank(){
		List<BankPOJO> bankList = createNewAccount(DEFAULT_BANK_ID,NEW_ACCOUNT_ID);
		Banks banks = new Banks();
		banks.setBankList(bankList);
		bankRepository.updateBanks(banks.getBankList());
		
		bankList = bankRepository.findBanks(createIds(DEFAULT_BANK_ID));
		BankPOJO bank = bankList.iterator().next();
		assertEquals(DEFAULT_BANK_ID,bank.getBankId());
		List<AccountPOJO> accountList = bank.getAccounts().getAccountsList();
		assertEquals(2, accountList.size());
	}
	
	@Test
	public void removeAccountFromBank(){
		bankRepository.removeAccounts(createBankAccountIds(DEFAULT_BANK_ID, DEFAULT_ACCOUNT_ID));
		List<BankPOJO> bankList = bankRepository.findBanks(createIds(DEFAULT_BANK_ID));
		BankPOJO bank = bankList.iterator().next();
		assertEquals(DEFAULT_BANK_ID,bank.getBankId());
		assertTrue(bank.getAccounts().getAccountsList().isEmpty());
	}
}
