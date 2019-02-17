package com.cav.spring.service.bank.repository;

import java.util.List;

import org.hibernate.Cache;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cav.spring.service.bank.controller.BankServiceController;
import com.cav.spring.service.bank.entity.Fund;
import com.cav.spring.service.bank.model.funds.FundId;
import com.cav.spring.service.bank.model.funds.FundPOJO;
import com.cav.spring.service.bank.model.funds.Funds;

import org.hibernate.Transaction;


public class FundRepositoryImpl extends MappEntitiesAbstract implements FundRepository {

	private SessionFactory sessionFactory;
	
	private static final Logger logger = LoggerFactory.getLogger(FundRepositoryImpl.class);	

	public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
	@Override
	public List<FundPOJO> findFunds(List<Long> fundIds) {
		Session session = this.sessionFactory.getCurrentSession();
		Cache cache = sessionFactory.getCache();
		Transaction tx = session.beginTransaction();
		Query res = session.createQuery("FROM Fund f WHERE f.fundId IN :fundIds").setParameterList("fundIds", fundIds);
		List <Fund>fundsList = res.setCacheable(true).list();
		List<FundPOJO> funds = mapFunds(fundsList);
		tx.commit();
		return funds;
	}
	

	@Override
	public void removeFunds(List<Long> fundIds) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		 String hql = "delete from Fund f where f.fundId IN :fundIds";
		 session.createQuery(hql).setParameterList("fundIds", fundIds).setCacheable(true).executeUpdate();
		 tx.commit();
	}

	@Override
	public void updateFunds(List<FundPOJO> funds) {
		Session session = this.sessionFactory.getCurrentSession();
		Transaction tx = session.beginTransaction();
		for(FundPOJO fund: funds){
			Object entity = session.createQuery("FROM Fund f WHERE f.fundId = :fundId").setParameter("fundId", fund.getFundId()).uniqueResult();
			if(entity != null){
				Fund fundEntity = updateFundEntity(fund, (Fund)entity);
				session.update(fundEntity);
				session.flush();
			} else {
				logger.error("Fund does not exist with fundId "+fund.getFundId());
			}
		}
		tx.commit();
	}

}
