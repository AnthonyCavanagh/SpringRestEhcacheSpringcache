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
import com.cav.spring.service.bank.entity.Fund;
import com.cav.spring.service.bank.model.accounts.AccountId;
import com.cav.spring.service.bank.model.accounts.AccountPOJO;
import com.cav.spring.service.bank.model.accounts.Accounts;
import com.cav.spring.service.bank.model.funds.FundId;

public class AccountRepositoryImpl extends MappEntitiesAbstract implements AccountRepository {

	private SessionFactory sessionFactory;

	private static final Logger logger = LoggerFactory.getLogger(AccountRepositoryImpl.class);

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	@Override
	public List<AccountPOJO>  findAccounts(List<Long> accountIds) {
		Session session = this.sessionFactory.getCurrentSession();
		Cache cache = sessionFactory.getCache();
		Transaction tx = session.beginTransaction();
		Query res = session.createQuery("FROM Account a WHERE a.accountId IN :accountIds")
				.setParameterList("accountIds", accountIds);
		List<Account> accountsList = res.setCacheable(true).list();
		List<AccountPOJO> accounts = mapAccounts(accountsList, true);
		tx.commit();
		return accounts;
	}

	@Override
	public void updateAccounts(List<AccountPOJO> accounts) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		for (AccountPOJO account : accounts) {
			Object entity = session.createQuery("FROM Account a WHERE a.accountId = :accountId")
					.setParameter("accountId", account.getAccountid()).uniqueResult();
			if (entity != null) {
				Account accountEntity = updateAccountEntity(account, (Account) entity);
				session.merge(accountEntity);
				session.flush();
			} else {
				logger.error("Account does not exist with accountId " + account.getAccountid());
			}
		}
		tx.commit();
	}

	@Override
	public void removeAccounts(List<Long> accountIds) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		String hql = "delete from Account a WHERE a.accountId IN :accountIds";
		session.createQuery(hql).setParameterList("accountIds", accountIds).setCacheable(true).executeUpdate();
		tx.commit();
	}

	@Override
	public void removeFunds(List<AccountId> accountIds) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		for(AccountId accountId : accountIds){
			Object entity = session.createQuery("FROM Account a WHERE a.accountId = :accountId")
					.setParameter("accountId", accountId.getId()).uniqueResult();
			if (entity != null) {
				Account account = (Account) entity;
				for(FundId fundId : accountId.getFundIds()){
					Object fundEntity = session.createQuery("FROM Fund f WHERE f.fundId = :fundId").setParameter("fundId", fundId.getId()).uniqueResult();
					if(fundEntity != null){
						account.removeFunds((Fund) fundEntity);
					}
				}
				session.merge(account);
				session.flush();
			}
		}
		tx.commit();
	}
		

}
