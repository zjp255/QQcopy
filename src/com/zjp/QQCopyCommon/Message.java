package com.zjp.QQCopyCommon;

import java.io.Serializable;

/**
 * @author ZhuJinPeng
 * @version 1.0
 */
public class Message implements Serializable {
    private static final long serialVersionUID = 1L;
    private String sender;//发送者
    private String getter;//接收者
    private String content;//消息内容
    private String sendTime;//发送时间
    private String messageType;

    private byte[] fileInfo;

    public void setFileInfo(byte[] fileInfo) {
        this.fileInfo = fileInfo;
    }

    public byte[] getFileInfo() {
        return fileInfo;
    }
    public Message(String sender, String getter, String content, String sendTime, String messageType) {
        this.sender = sender;
        this.getter = getter;
        this.content = content;
        this.sendTime = sendTime;
        this.messageType = messageType;
    }

    public Message(String getter, String sendTime, String messageType) {
        this.sender = "System";
        this.getter = getter;
        this.sendTime = sendTime;
        this.messageType = messageType;
    }


    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getGetter() {
        return getter;
    }

    public void setGetter(String getter) {
        this.getter = getter;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }
}
