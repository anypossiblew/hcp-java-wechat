package com.sap.c4c.wechat.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;

@Entity
@NamedQuery(name = "AllMessages", query = "select m from Message m")
public class Message {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    public Long id;
    public String type;
    public String text;

    public Message() {}

    public Message(String type, String text) {
        this.type = type;
        this.text = text;
    }

    @Override
    public String toString() {
        return String.format(
                "Message[id=%d, type='%s', text='%s']",
                id, type, text);
    }

}
