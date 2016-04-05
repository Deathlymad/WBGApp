package com.webteam.wbgapp.wbgapp.net;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

public class DatabaseHandler extends AsyncTask<IRequest, Void, Void> {

    private String s;

    private void getData(String... req) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        String link = "http://wbgapp.malte-projects.de/webservice.php?type=";
        for (String s : req)
            link += s;
        HttpGet request = new HttpGet( link);
        HttpResponse response = client.execute(request);
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader =new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 65728);
            String line;
            reader.readLine();
            while ((line = reader.readLine()) != null) {
                    sb.append(line);
            }
        } catch (Exception e) { e.printStackTrace(); }

        s =  sb.toString();
    }

    @Override
    protected Void doInBackground(IRequest... params) {
        try {
            IRequest req = params[0];
            getData(req.getRequest());

            if (req != null)
                req.handleResults(s);
        } catch (Exception e) {}
        return null;
    }
}
