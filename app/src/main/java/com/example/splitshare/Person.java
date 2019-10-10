package com.example.splitshare;

public class Person {

    private String ph_number;
    private String name;
    private int curPaid;
    private int paid;
    private float share;
    private boolean check;
    private float balance;

    public Person(String name, String ph_number) {
        this.name = name;
        this.ph_number = ph_number;
        this.curPaid = 0;
        this.paid = 0;
        this.share = 0;
        this.check = false;
        this.balance = 0;
    }

    public Person(String name) {
        this.name = name;
        this.ph_number = "";
        this.curPaid = 0;
        this.paid = 0;
        this.share = 0;
        this.check = false;
        this.balance = 0;
    }

    public String getPh_number() {
        return ph_number;
    }

    public void setPh_number(String ph_number) {
        this.ph_number = ph_number;
    }

    public float getBalance() {
        return balance;
    }

    public void setBalance(float balance) {
        this.balance = balance;
    }

    public int getCurPaid() {
        return curPaid;
    }

    public void setCurPaid(int curPaid) {
        this.curPaid = curPaid;
    }

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public float getShare() {
        return share;
    }

    public void setShare(float share) {
        this.share = share;
    }

}
