package com.example.demo.model;

import java.io.Serializable;

//TODO Delete not used
public class Company implements Serializable {

    private static final long serialVersionUID = 1L;

    private String name;
    private String contactPerson;
    private String mobileNumber;

    // Constructor for initializing the object for testing purpose
    public Company(String name, String contactPerson, String mobileNumber){
        this.name = name;
        this.contactPerson = contactPerson;
        this.mobileNumber = mobileNumber;
    }

    public String getName(){
        return this.name;
    }
    public String getContactPerson(){
        return this.contactPerson;
    }
    public String getMobileNumber(){
        return this.mobileNumber;
    }
}
