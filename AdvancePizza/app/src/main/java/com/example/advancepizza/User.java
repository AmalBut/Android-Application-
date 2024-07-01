package com.example.advancepizza;

import android.database.Cursor;

//import org.mindrot.jbcrypt.BCrypt;

import java.util.Arrays;

public class User {
    //
    public static Cursor currentUser;
    private String firstName;
    private String lastName;
    private String gender;
    private String email;
    private String password;
    private String phoneNumber;

    private String permission;

    private byte[] profilePicture;



    public User() {
    }

    public User(String firstName, String lastName, String gender, String email, String password,String phoneNumber,  byte[] profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;

    }


    public User(String firstName, String lastName, String gender, String email, String password,String phoneNumber,  byte[] profilePicture, String Permission) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.profilePicture = profilePicture;
        this.permission=Permission;

    }

    public static Cursor getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(Cursor currentUser) {
        User.currentUser = currentUser;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public byte[] getProfilePicture() {
        return profilePicture;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setProfilePicture(byte[] profilePicture) {
        this.profilePicture = profilePicture;
    }


    @Override
    public String toString() {
        return "User{" +
                "firstName='" + firstName + '\'' +
                ", LastName='" + lastName + '\'' +
                ", Gender='" + gender + '\'' +
                ", Email='" + email + '\'' +
                ", Password='" + password + '\'' +
                ", PhoneNumber='" + phoneNumber + '\'' +
                ", ProfilePicture=" + Arrays.toString(profilePicture) +
                '}';
    }

/*
    public static String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainTextPassword, String hashedPassword){
        return BCrypt.checkpw(plainTextPassword, hashedPassword);
    }*/
}
