package com.example.hmwrk;

public class UserData {
    private String username;
    private int age;

    private long id;

    UserData(){}
    UserData(User user){
        username = user.getUsername();
        age = user.getAge();
        id = user.getId();
    }

}
