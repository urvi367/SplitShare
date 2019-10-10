package com.example.splitshare;

import java.util.ArrayList;

public class Event{

    private String eventname;
    private int total;
    private ArrayList<String> pplname;
    private String name;

    public Event(String eventname) {
        this.eventname = eventname;
        this.name="";
        this.total = 0;
        this.pplname = new ArrayList<String>();
    }

    public Event() {
        this.eventname = "";
        this.name="";
        this.total = 0;
        this.pplname = new ArrayList<String>();
    }

    public void ToString()
    {
        for(int i=0;i<pplname.size();i++)
            this.name += pplname.get(i) + ", ";
        this.name=this.name.substring(0, name.length()-1);

        this.pplname.clear();
    }

    public ArrayList<String> getPplname() {
        return pplname;
    }

    public void setPplname(String name) {
        this.pplname.add(name);
    }

    public void removePplname (String name)
    {
        this.pplname.remove(name);
    }


    public String getEventname() {
        return eventname;
    }

    public void setEventname(String eventname) {
        this.eventname = eventname;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
