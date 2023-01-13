package com.example.hmwrk;

public class UserDataPacket {
    private String username;
    private String password;
    private String repeatPassword;
    int age;

    public UserDataPacket(){}
    public UserDataPacket(String username,
                          String password,
                          String repeatPassword,
                          int age){
        this.username=username;
        this.password=password;
        this.repeatPassword=repeatPassword;
        this.age=age;
    }

    public int getAge() {
        return age;
    }

    public String getPassword() {
        return password;
    }

    public String getUsername() {
        return username;
    }

    public boolean passwordsMatch(){
        return password.equals(repeatPassword);
    }
}
