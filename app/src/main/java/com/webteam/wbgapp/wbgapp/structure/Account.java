package com.webteam.wbgapp.wbgapp.structure;

import android.content.SharedPreferences;

import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Deathlymad on 16.01.2016 .
 */
public class Account {

    private final String _username;
    private String _email;
    private Integer _grade;
    private Integer _formselector;
    private String _name;
    private final String _pwHash;


    public Account(String user, String pw, SharedPreferences mem) {
        _username = user;
        _pwHash = encrypt(pw);
        mem.edit().putString("login", getLogin());

        mem.edit().apply();
    }

    public String getName() {
        return _name;
    }

    public String getLogin()
    {
        return "&username=" + _username + "&password=" + _pwHash;
    }

    private static String encrypt(String password) {
        String sha1 = "";
        try {
            MessageDigest crypt = MessageDigest.getInstance("SHA-1");
            crypt.reset();
            crypt.update(password.getBytes("UTF-8"));
            sha1 = new String(crypt.digest());
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

}
