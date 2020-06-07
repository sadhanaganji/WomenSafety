package com.e.womansafety;

import com.google.firebase.auth.FirebaseAuth;


public class Person {
       String personId;
     String personName;
     String personNumber;
     String uid;
    private String userID;
    //userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

    public Person(){

    }

    public Person(String personId, String personName, String personNumber,String uid) {
        this.personId = personId;
        this.personName = personName;
        this.personNumber = personNumber;
        this.uid=uid;
    }


    public String getPersonId() {
        return personId;
    }

    public String getPersonName() {
        return personName;
    }


    public String getPersonNumber() {
        return personNumber;
    }
    public  String getuid() {
        return FirebaseAuth.getInstance().getCurrentUser().getUid();
    }
}

