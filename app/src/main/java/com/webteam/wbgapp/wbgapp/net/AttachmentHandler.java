package com.webteam.wbgapp.wbgapp.net;

import android.os.AsyncTask;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created by Deathlymad on 23.03.2016 .
 */
public class AttachmentHandler  extends AsyncTask<IRequest, Void, String> {

    IRequest req;

    private void pullAttachment(String serverPath, String filePath) throws IOException
    {
        URL url = new URL(serverPath);

        InputStream in = new BufferedInputStream(url.openStream());
        FileOutputStream fos = new FileOutputStream(filePath);
        byte[] buf = new byte[1024];
        int n=0;
        while (-1!=(n=in.read(buf)))
        {
            fos.write(buf, 0, n);
        }
        fos.flush();
        fos.close();
        in.close();
    }

    @Override
    protected String doInBackground(IRequest... params) {
        req = params[0];
        String[] data = req.getRequest();
        String sp = data[0];
        String fp = data[1];
        try {
            pullAttachment(sp, fp);
            return fp;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(String res)
    {
        if (res == null)
            return;
        super.onPostExecute(res);
        if (req != null)
            req.handleResults(res);
    }
}
