package com.sap.c4c.wechat.service.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.sap.c4c.wechat.model.Message;
import com.sap.c4c.wechat.service.MessageService;

@Component
public class MessageServiceImpl implements MessageService {

	@PersistenceContext
	private EntityManager em;

	public EntityManager getEm() {
		return em;
	}

	public void setEm(EntityManager em) {
		this.em = em;
	}

	@Transactional(readOnly=true)
	public List<Message> getAll() {
		@SuppressWarnings("unchecked")
		List<Message> resultList = em.createNamedQuery("AllMessages").getResultList();
		return resultList;
	}

	@Transactional
	public void createMessage(Message message) {

		em.persist(message);

	}
}
