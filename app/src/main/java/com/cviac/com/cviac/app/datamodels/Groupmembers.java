package com.cviac.com.cviac.app.datamodels;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.util.Date;

/**
 * Created by Administrator on 3/20/2017.
 */
@Table(name = "Groupmembers")
public class Groupmembers extends Model {

    @Column(name = "group_id")
    private String group_id;
    @Column(name = "member_id")
    private String member_id;
    @Column(name = "gdate")
    private Date gdate;

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

    public Date getGdate() {
        return gdate;
    }

    public void setGdate(Date gdate) {
        this.gdate = gdate;
    }
}
