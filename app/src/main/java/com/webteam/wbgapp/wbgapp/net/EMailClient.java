package com.webteam.wbgapp.wbgapp.net;

import org.apache.commons.net.pop3.*;

/**
 * Created by Deathlymad on 24.03.2016 .
 */
public class EMailClient {
    private POP3Client client;

    EMailClient()
    {
        client = new POP3Client();
    }
}
