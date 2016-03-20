package com.webteam.wbgapp.wbgapp.net;

import com.webteam.wbgapp.wbgapp.structure.Account;
import com.webteam.wbgapp.wbgapp.structure.SubstitutePlan;

import org.apache.commons.net.ftp.FTPClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import java.io.IOException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * Created by Deathlymad on 20.03.2016 .
 */
public class SubstitutePlanRequest extends FTPRequest implements IRequest{

    private boolean _done;
    private String _file;
    private Document _xmlSubstitues;
    private Account _acc;

    public SubstitutePlanRequest(Account acc) {
        super(null, null, null);
        _acc = acc;
        _done = false;
        new DatabaseHandler().execute(this);
    }

    @Override
    protected void getFiles(FTPClient client) {
        if (!_done)
        {
            try {
                _xmlSubstitues = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(client.retrieveFileStream("")); //don't try to tell what i can't put in one line
            } catch (SAXException | IOException | ParserConfigurationException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public String[] getRequest() {
        return new String[]{"ftp_substitute" + _acc.getLogin()};
    }

    @Override
    public void handleResults(String... result) {
        String res = result[0];
        try {
            JSONObject obj = new JSONObject(res);
            setUrl(obj.getString("url"));
            setUser(obj.getString("user"));
            setPw(obj.getString("pw"));
            start();
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public SubstitutePlan getPlan()
    {
        return new SubstitutePlan(_xmlSubstitues);
    }
}
