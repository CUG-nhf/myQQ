package org.myqq.common;

import java.io.Serializable;

public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    private String message;
    private String sender;
    private String receiver;
    private String sendTime;
    private Type type;

    public Message() {
        this.message = "";
        this.sender = "";
        this.receiver = "";
        this.sendTime = "";
        this.type = Type.NULL;
    }

    public Message(String message, String sender, String receiver, String sendTime, Type type) {
        this.message = message;
        this.sender = sender;
        this.receiver = receiver;
        this.sendTime = sendTime;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public enum Type {
        NULL,
        TEXT,
        IMAGE,
        FILE
    }
}
