package com.cviac.com.cviac.app.xmpp;

import java.util.Date;
import java.util.Random;

public class ChatMessage {

    public String msg, sender, converseid, receiver, senderName;
    public String ctime;
    public String msgid;
    public boolean isMine;// Did I send the message.

    public ChatMessage(String converseId,String Sender,  String Receiver, String messageString,
                       String ID, boolean isMINE) {
        msg = messageString;
        isMine = isMINE;
        sender = Sender;
        msgid = ID;
        receiver = Receiver;
        senderName = sender;
        converseid= converseId;

        ctime = "";
    }

    public void setSenderName(String name) {
        senderName = name;
    }

    public void setMsgID() {

        msgid += "-" + String.format("%02d", new Random().nextInt(100));
        ;
    }
}