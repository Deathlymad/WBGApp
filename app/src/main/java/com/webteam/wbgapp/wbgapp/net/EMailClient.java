package com.webteam.wbgapp.wbgapp.net;

import com.webteam.wbgapp.wbgapp.structure.Account;

import org.apache.commons.net.pop3.*;

import java.io.IOException;
import java.io.Reader;
import java.nio.CharBuffer;

/**
 * Created by Deathlymad on 24.03.2016 .
 */
public class EMailClient {
    private POP3Client client;

    public EMailClient(Account login) throws IOException
    {
        client = new POP3Client();
        client.connect("pop3.wbgym.de");
        client.login(login.getEMail(), login.getPassword());
    }

    public POP3MessageInfo[] getEMailData() throws IOException
    {
        return client.listMessages();
    }

    public int getEMailAmt() throws IOException
    {
        return getEMailData().length;
    }

    public CharBuffer getEmail(int id) throws IOException
    {
        if (client.getState() == POP3.TRANSACTION_STATE) {
            POP3MessageInfo info = client.listMessage(id);
            int bytes;
            bytes = info.size;
            Reader r = client.retrieveMessage(id);
            CharBuffer buffer = CharBuffer.allocate(bytes);
            if (bytes == r.read(buffer))
                return buffer;
            else
                System.err.println("Bytes disappeared while reading E-Mail O.o");
                //bytes disappeared,
        }
        return null;
    }
}
