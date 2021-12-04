package com.example.NewsFeed;

import java.util.ArrayList;

public class UserInfo {
    private String name,email,college,codechef,codeforces,hackerrank;
    private ArrayList<String> title;
    private  ArrayList<String> description;

    UserInfo()
    {

    }
    public UserInfo(String name,String email)
    {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public ArrayList<String> getTitle() {
        return title;
    }

    public ArrayList<String> getDescription() {
        return description;
    }

    public  void addTitle(String ti){
        title.add(ti);
        return;
    }

    public void  addDescription(String des){
        description.add(des);
    }
    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
