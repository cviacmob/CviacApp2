package com.cviac.com.cviac.app.datamodels;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.activeandroid.util.SQLiteUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.icu.text.DateFormat.DAY;
import static android.icu.text.DateFormat.HOUR24;
import static android.icu.text.DateFormat.getDateInstance;
import static android.icu.text.DateFormat.getDateTimeInstance;


@Table(name = "ConvMessages")
public class ConvMessage extends Model {

    @Column(name = "msg")
    private String msg;

    @Column(name = "isMine")
    private boolean isMine;

    @Column(name = "ctime")
    private Date ctime;

    @Column(name = "converseid")
    private String converseid;

    @Column(name = "sendername")
    private String senderName;

    @Column(name = "sender")
    private String sender;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "msgid", unique = true)
    private String msgid;

    @Column(name = "status")
    private int status;


    public ConvMessage() {
        super();
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isMine() {
        return isMine;
    }

    public void setMine(boolean mine) {
        isMine = mine;
    }

    public Date getCtime() {
        return ctime;
    }

    public void setCtime(Date ctime) {
        this.ctime = ctime;
    }

    public String getConverseid() {
        return converseid;
    }

    public void setConverseid(String converseid) {
        this.converseid = converseid;
    }

    public String getSenderName() {
        return senderName;
    }

    public void setSenderName(String senderName) {
        this.senderName = senderName;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public static void updateStatus(String msgId, int status) {
        new Update(ConvMessage.class)
                .set("status = ?", status)
                .where("msgid = ?", msgId)
                .execute();
        return;
    }


    public static List<ConvMessage> getAll(String converseid) {
        return new Select()
                .from(ConvMessage.class)
                .where("converseid = ?", converseid)
                //.orderBy("Name ASC")
                .execute();
    }

//    public static List<ConvMessage> getConversations() {
//        List<ConvMessage> conversations = SQLiteUtils.rawQuery(ConvMessage.class, "select * from (select * from ChatMessages ORDER BY ctime asc) AS x GROUP BY sender ORDER BY ctime DESC",
//                new String[]{});
//        return conversations;
//    }

//	public static List<ConvMessage> getMessagesFromConversation(int userId, int teamId, String conversationId, boolean isGroupConversation) {
//		List<ConvMessage> messages = new Select().from(ConvMessage.class).where("userId=? AND teamId=? AND conversation_id=? AND is_group_conversation=?", userId, teamId, conversationId, isGroupConversation).orderBy("created_time DESC").execute();
//		return messages;
//	}
//
//	public static void deleteMessages(int teamId) {
//		new Delete().from(ConvMessage.class).where("teamId=?", teamId).execute();
//	}


}
