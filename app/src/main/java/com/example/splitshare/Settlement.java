package com.example.splitshare;

public class Settlement {

    private Person person1, person2;
    private float amount;


    Settlement(Person person1, Person person2, float amount)
    {
        this.person1=person1;
        this.person2=person2;
        this.amount=amount;
    }

    public Person getPerson1() {
        return person1;
    }

    public String getPerson1Name() {
        return person1.getName();
    }

    public void setPerson1(Person person1) {
        this.person1 = person1;
    }

    public Person getPerson2() {
        return person2;
    }

    public String getPerson2Name() {
        return person2.getName();
    }

    public void setPerson2(Person person2) {
        this.person2 = person2;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

}
