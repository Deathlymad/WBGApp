package com.webteam.wbgapp.wbgapp.structure;

import android.content.SharedPreferences;

import com.webteam.wbgapp.wbgapp.net.BackgroundService;
import com.webteam.wbgapp.wbgapp.util.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Deathlymad on 16.01.2016 .
 */
public class Account implements BackgroundService.UpdateListener{

    private final String _username;
    private String _email;
    private Integer _grade;
    private Integer _formselector;
    private String _name;
    private final String _pwHash;


    public Account(String user, String pw, SharedPreferences mem) {
        _username = user;
        _pwHash = encrypt(pw);

        mem.edit().putString("login", getLogin()).commit();
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
            byte[] digest = crypt.digest();

            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < digest.length; i++) {
                if ((0xff & digest[i]) < 0x10) {
                    sb.append("0").append(Integer.toHexString((0xFF & digest[i])));
                } else {
                    sb.append(Integer.toHexString(0xFF & digest[i]));
                }
            }

            sha1 = sb.toString();
        } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return sha1;
    }

    @Override
    public void onUpdate(String Type) {
        JSONObject data = BackgroundService._accData;
        _email = _username + "@wbgym.de";
        try{
            _name = data.getString("name");
            _grade = data.getInt("grade");
            _formselector = data.getInt("formselector");
        }catch(JSONException e)
        {
            e.printStackTrace();
        }

    }

    @Override
    public String getUpdateType() {
        return Constants.INTENT_CHECK_LOGIN;
    }
}
