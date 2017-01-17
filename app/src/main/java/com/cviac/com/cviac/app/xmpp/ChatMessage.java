package com.cviac.com.cviac.app.xmpp;

import java.util.Date;
import java.util.Random;

public class ChatMessage {

    public String body, sender, receiver, senderName;
    public Date datetime;
    public String msgid;
    public boolean isMine;// Did I send the message.

    public ChatMessage(String Sender,  String Receiver, String messageString,
                       String ID, boolean isMINE) {
        body = messageString;
        isMine = isMINE;
        sender = Sender;
        msgid = ID;
        receiver = Receiver;
        senderName = sender;
        datetime = new Date();
    }

    public void setSenderName(String name) {
        senderName = name;
    }

    public void setMsgID() {

        msgid += "-" + String.format("%02d", new Random().nextInt(100));
        ;
    }
}