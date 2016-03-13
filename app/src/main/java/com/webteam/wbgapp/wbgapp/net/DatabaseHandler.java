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

public class DatabaseHandler extends AsyncTask<IRequest, Void, String> {

    public Exception ex;
    public IRequest req;

    private String getData(String... req) throws IOException, URISyntaxException {
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
            reader.readLine(); //pull the meta charset first
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (Exception e) { e.printStackTrace(); }

        return sb.toString();
    }

    @Override
    protected String doInBackground(IRequest... params) {
        try {
            req = params[0];
            return getData(req.getRequest());
        } catch (Exception e) {
           ex = e;
        }
        return null;
    }

    @Override
    protected void onPostExecute(String res) {
        super.onPostExecute(res);
        if (req != null)
            req.handleResults(res);
    }
}
