package com.example.aplicaciontfg2.entidades;

import java.util.Date;

public class ChatMessage {

    private String senderId, receiverId, message, dateTime;
    private Date dateObject;
    private String conversionId, conversionName;

    public ChatMessage(){


    }

    public ChatMessage(String senderId,String receiverId,String message,String dateTime,Date dateObject){

        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.dateTime = dateTime;
        this.dateObject = dateObject;

    }

    public ChatMessage(String senderId,String receiverId,String message,String dateTime,Date dateObject,String conversionId,String conversionName){

        this.senderId = senderId;
        this.receiverId = receiverId;
        this.message = message;
        this.dateTime = dateTime;
        this.dateObject = dateObject;
        this.conversionId = conversionId;
        this.conversionName = conversionName;

    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public Date getDateObject() {
        return dateObject;
    }

    public void setDateObject(Date dateObject) {
        this.dateObject = dateObject;
    }

    public String getConversionId() {
        return conversionId;
    }

    public void setConversionId(String conversionId) {
        this.conversionId = conversionId;
    }

    public String getConversionName() {
        return conversionName;
    }

    public void setConversionName(String conversionName) {
        this.conversionName = conversionName;
    }

}
