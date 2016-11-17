package com.cviac.datamodel.cviacapp;

public class Event {
    String names;
    String types;
    String discription;

    public String getName() {
        return names;
    }

    public String getType() {
        return types;
    }

    public String getDiscription() {
        return discription;
    }

    public void setName(String name) {
        this.names = name;
    }

    public void setType(String type) {
        this.types = type;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

}
