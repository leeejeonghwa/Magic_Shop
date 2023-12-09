package com.example.magic_shop;

public class User {
    private String userName;
    private String userID;
    private String userType;


    public User(String userName, String userID, String userType) {
        this.userName = userName;
        this.userID = userID;
        this.userType = userType;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
