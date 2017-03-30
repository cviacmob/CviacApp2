package com.cviac.com.cviac.app.datamodels;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.util.Date;
import java.util.List;

/**
 * Created by Cviac on 27/03/2017.
 */
@Table(name = "GroupMemberInfo")
public class GroupMemberInfo extends Model{

    @Column(name = "group_id")
    private String group_id;
    @Column(name = "member_id")
    private String member_id;
    @Column(name = "invitedate")
    private Date invitedate;

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public Date getInvitedate() {
        return invitedate;
    }

    public void setInvitedate(Date invitedate) {
        this.invitedate = invitedate;
    }


    public static List<GroupMemberInfo> getmembers(String groupname) {
        return new Select()
                .from(GroupMemberInfo.class)
                .where("group_id = ?", groupname)
                .execute();

    }
}


