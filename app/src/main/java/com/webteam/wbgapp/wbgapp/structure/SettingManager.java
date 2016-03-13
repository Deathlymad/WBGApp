package com.webteam.wbgapp.wbgapp.structure;

import java.io.FileOutputStream;
import java.util.Map;

/**
* Created by Deathlymad on 16.01.2016.
        */
public class SettingManager {
    public static final String FILE_NAME = "PersistentSettingData.bin";

    private Account _currAccount; //needs to encrypt itself
    private Map<String, String> _settings;

    public SettingManager() {
        _currAccount = null;
    }

    public Account getCurrAccount() {
        return _currAccount;
    }

    public void setCurrAccount(Account currAccount) {
        _currAccount = currAccount;
    }

    public void save(FileOutputStream fileHandle) {
        if (_currAccount != null)
            _currAccount.save(fileHandle);
    }
}
