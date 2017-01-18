package com.sap.c4c.wechat.service;

import java.util.List;

import com.sap.c4c.wechat.model.Message;

public interface MessageService {
	public List<Message> getAll();

	public void createMessage(Message message);
}
