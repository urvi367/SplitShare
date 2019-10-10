package com.example.splitshare;

import android.net.Uri;

public class User {
    public String username;
    Uri photourl;
    public String email;

    // Default constructor required for calls to
    // DataSnapshot.getValue(User.class)
    public User() {
    }

    public User(String email) {
        this.photourl=null;
        this.username = null;
        this.email = email;
    }

    public Uri getPhotourl() {
        return photourl;
    }

    public void setPhotourl(Uri photourl) {
        this.photourl = photourl;
    }

    public String getName() {
        return username;
    }

    public void setName(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
