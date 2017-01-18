package com.sap.c4c.wechat.web;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sap.c4c.wechat.model.Message;
import com.sap.c4c.wechat.service.MessageService;

@Controller
@RequestMapping("/message")
public class MessageController {

	@Autowired
	private MessageService messageService;

	@RequestMapping(value = "", method = RequestMethod.GET)
  public @ResponseBody List<Message> get(HttpServletResponse response) throws ServletException {

  	response.setContentType("application/json;charset=UTF-8");

  	return messageService.getAll();
  }

	@RequestMapping(value = "", method = RequestMethod.POST)
  public @ResponseBody void post(HttpServletRequest request) throws ServletException {

    // Extract name of person to be added from request
    String type = request.getParameter("Type");
    String text = request.getParameter("Text");

    Message message = new Message(type, text);

    if (message.type != null && !message.type.trim().isEmpty()) {
      messageService.createMessage(message);
    }
  }

	public MessageService getMessageService() {
		return messageService;
	}

	public void setMessageService(MessageService messageService) {
		this.messageService = messageService;
	}
}
