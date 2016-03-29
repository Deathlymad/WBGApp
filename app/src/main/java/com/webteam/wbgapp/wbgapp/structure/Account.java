package com.webteam.wbgapp.wbgapp.structure;

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


    public Account(String user, String pw) {
        _username = user;
        _pwHash = encryptPassword(pw);
    }

    public String getName() {
        return _name;
    }

    private String encryptPassword(String password) {
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
