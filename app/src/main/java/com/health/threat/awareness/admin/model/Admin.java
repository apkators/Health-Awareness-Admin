package com.health.threat.awareness.admin.model;

public class Admin {
    private String Email;
    private String Id;
    private String Mobile;
    private String Name;

    public Admin(String email, String id, String mobile, String name) {
        Email = email;
        Id = id;
        Mobile = mobile;
        Name = name;
    }

    public Admin() {

    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

}
