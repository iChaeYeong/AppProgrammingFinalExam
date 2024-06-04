package com.example.finalexam0514;

public class Shop {
    private int id;
    private String name;
    private String address;
    private String info;
    private String openTime;
    private String user;

    public Shop(int id, String name, String address, String info, String openTime, String user) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.info = info;
        this.openTime = openTime;
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getInfo() {
        return info;
    }

    public String getOpenTime() {
        return openTime;
    }

    public String getUser() {
        return user;
    }
}