package com.webteam.wbgapp.wbgapp.net;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by Deathlymad on 19.03.2016.
 */
public abstract class FTPRequest extends Thread {

    public void setUrl(String url) {
        _url = url;
    }

    public void setUser(String user) {
        _user = user;
    }

    public void setPw(String pw) {
        _pw = pw;
    }

    String _url, _user, _pw;

    public FTPRequest (String URL, String user, String pw){
        _url = URL;
        _user = user;
        _pw = pw;
    }



    protected abstract void getFiles(FTPClient client);

    @Override
    public void run()
    {
        if (_url == null || _user == null || _pw == null)
            return;
        try {
            FTPClient mFTPClient = new FTPClient();
            // connecting to the host
            mFTPClient.connect(_url, 21);
            boolean status = mFTPClient.login(_user, _pw);
            // now check the reply code, if positive mean connection success
            if (status && FTPReply.isPositiveCompletion(mFTPClient.getReplyCode())) {
               /* Set File Transfer Mode
                *
                * To avoid corruption issue you must specified a correct
                * transfer mode, such as ASCII_FILE_TYPE, BINARY_FILE_TYPE,
                * EBCDIC_FILE_TYPE .etc. Here, I use BINARY_FILE_TYPE
                * for transferring text, image, and compressed files.
                */
                mFTPClient.setFileType(FTP.BINARY_FILE_TYPE);
                mFTPClient.enterLocalPassiveMode();
            }

            getFiles(mFTPClient);

            mFTPClient.logout();
            mFTPClient.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
