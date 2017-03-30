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
@Table(name = "groupinfo")
public class GroupInfo extends Model {

    @Column(name = "name")
    String name;

    @Column(name = "grpID")
    String grpID;

    @Column(name = "createdDate")
    Date createdDate;

    @Column(name = "owner")
    String owner;

    public GroupInfo() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrpID() {
        return grpID;
    }

    public void setGrpID(String grpID) {
        this.grpID = grpID;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    static public List<GroupInfo> getGroups() {
        return new Select()
                .from(GroupInfo.class)
                .execute();
    }
}
