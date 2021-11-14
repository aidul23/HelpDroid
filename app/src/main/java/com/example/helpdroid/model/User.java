package com.example.helpdroid.model;

public class User {
    String userName;
    String userEmail;
    String userPhoneNumber;
    String trustedPhoneNumber1;

    public User(String userName, String userEmail, String userPhoneNumber, String trustedPhoneNumber1) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhoneNumber = userPhoneNumber;
        this.trustedPhoneNumber1 = trustedPhoneNumber1;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getTrustedPhoneNumber1() {
        return trustedPhoneNumber1;
    }

    public void setTrustedPhoneNumber1(String trustedPhoneNumber1) {
        this.trustedPhoneNumber1 = trustedPhoneNumber1;
    }
}
