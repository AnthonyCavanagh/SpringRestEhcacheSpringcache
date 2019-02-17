package com.cav.spring.service.bank.repository;

import java.util.List;

import org.hibernate.Cache;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cav.spring.service.bank.entity.Account;
import com.cav.spring.service.bank.entity.Bank;
import com.cav.spring.service.bank.model.accounts.AccountId;
import com.cav.spring.service.bank.model.banks.BankId;
import com.cav.spring.service.bank.model.banks.BankPOJO;
import com.cav.spring.service.bank.model.banks.Banks;
import com.cav.spring.service.bank.entity.Bank;


public class BankRepositoryImpl extends MappEntitiesAbstract  implements BankRepository {

	private SessionFactory sessionFactory;

	private static final Logger logger = LoggerFactory.getLogger(BankRepositoryImpl.class);

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}
	
	@Override
	public List<BankPOJO> findBanks(List<Long> bankIds) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		Query res = session.createQuery("FROM Bank b WHERE b.bankId IN :bankIds")
				.setParameterList("bankIds", bankIds);
		List<Bank> banksList = res.setCacheable(true).list();
		List<BankPOJO> banks = mapBanks(banksList, true, true);
		tx.commit();
		return banks;
	}
	
	@Override
	public void createBanks(List<BankPOJO> banks) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		for (BankPOJO bank : banks) {
			Object entity = session.createQuery("FROM Bank b WHERE b.bankId = :bankId")
					.setParameter("bankId", bank.getBankId()).uniqueResult();
			if (entity == null) {
				Bank BankEntity = createBankEntity(bank);
				session.merge(BankEntity);
				session.flush();
			} else {
				logger.error("Bank already exist with BankId " + bank.getBankId());
			}
		}
		tx.commit();
	}

	@Override
	public void updateBanks(List<BankPOJO> banks) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		for (BankPOJO bank : banks) {
			Object entity = session.createQuery("FROM Bank b WHERE b.bankId = :bankId")
					.setParameter("bankId", bank.getBankId()).uniqueResult();
			if (entity != null) {
				Bank BankEntity = updateBankEntity(bank, (Bank) entity);
				session.merge(BankEntity);
				session.flush();
			} else {
				logger.error("Bank does not exist with BankId " + bank.getBankId());
			}
		}
		tx.commit();
	}

	@Override
	public void removeBanks(List<Long> bankIds) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		String hql = "delete FROM Bank b WHERE b.bankId IN :bankIds";
		session.createQuery(hql).setParameterList("bankIds", bankIds).setCacheable(true).executeUpdate();
		tx.commit();
	}

	@Override
	public void removeAccounts(List<BankId> bankIds) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		for (BankId bankId : bankIds) {

			Object entity = session.createQuery("FROM Bank b WHERE b.bankId = :bankId")
					.setParameter("bankId", bankId.getId()).uniqueResult();
			if (entity != null) {
				Bank bank = (Bank) entity;
				for (AccountId accountId : bankId.getAccountIds()) {
					Object accountEntity = session.createQuery("FROM Account a WHERE a.accountId = :accountId")
							.setParameter("accountId", accountId.getId()).uniqueResult();
					if (accountEntity != null) {
						bank.removeAccounts((Account) accountEntity);
					}
				}
				session.merge(bank);
				session.flush();
			}
			tx.commit();
		}
		
	}
	

}
