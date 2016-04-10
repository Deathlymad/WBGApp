package com.webteam.wbgapp.wbgapp.net;

import com.webteam.wbgapp.wbgapp.structure.SubstitutePlan;
import com.webteam.wbgapp.wbgapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import cz.msebera.android.httpclient.HttpResponse;
import cz.msebera.android.httpclient.client.HttpClient;
import cz.msebera.android.httpclient.client.methods.HttpGet;
import cz.msebera.android.httpclient.impl.client.HttpClientBuilder;

public class SubPlanLoader extends Thread {

    private enum _TaskType
    {
        SAVE,    //check if date or name changed, regenerate SubPlan
        GET,
        GET_NEXT,     //Pull from Server
        LOAD,    //Load from File
        NULL
    }

    public interface SubPlanUpdateFinished
    {
        void onUpdate(SubstitutePlan plan);
    }

    boolean _running = true;
    SubstitutePlan _SubPlanToday, _SubPlanTomorrow;
    Queue<_TaskType> tasks = new LinkedList<>();
    private FileInputStream _fInPos = null;
    private FileOutputStream _fOutPos = null;

    private ArrayList<SubPlanUpdateFinished> onUpdateFinished = new ArrayList<>();

    public ArrayList<String> getPlanForClass(String _class) {
        ArrayList<String> arr = null;
        if (_SubPlanToday != null)
            arr =  _SubPlanToday.getSubstitutionByClass(_class);

            return arr;
    }

    private String getSubPlanURLs() throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet( "http://wbgapp.malte-projects.de/webservice.php?type=substituteplan");
        HttpResponse response = client.execute(request);

        StringBuilder sb = new StringBuilder();
        BufferedReader reader =new BufferedReader(new InputStreamReader(response.getEntity().getContent()), 65535);
        String line;
        reader.readLine();
        while ((line = reader.readLine()) != null) {
                sb.append(line);
        }
        return  sb.toString();
    }

    private void pullSubPlans() throws IOException, JSONException, ParserConfigurationException, SAXException, ParseException {
        JSONObject planURLs = new JSONObject(Util.unescUnicode(getSubPlanURLs())); //check if URLS exist (weekend and stuffs)

        try {
            String url = planURLs.getString("today");
            _SubPlanToday = new SubstitutePlan(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new URL("http://wbgym.de/files/vertretungsplan/public" + url.substring(url.lastIndexOf("/")) ).openStream()).getDocumentElement());
        } catch (JSONException | FileNotFoundException e) {
            e.printStackTrace();
            _SubPlanToday = null;
        }
        pullNextSubPlan();
    }

    private void pullNextSubPlan() throws IOException, JSONException, ParserConfigurationException, SAXException, ParseException {
        JSONObject planURLs = new JSONObject(Util.unescUnicode(getSubPlanURLs())); //check if URLS exist (weekend and stuffs)
        try {
            String url = planURLs.getString("tomorrow");
            _SubPlanTomorrow = new SubstitutePlan(DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new URL("http://wbgym.de/files/vertretungsplan/public" + url.substring(url.lastIndexOf("/")) ).openStream()).getDocumentElement());
        } catch (JSONException | FileNotFoundException e) {
            e.printStackTrace();
            _SubPlanTomorrow = null;
        }
    }
    private void loadSubPlans() throws IOException, NullPointerException, ParserConfigurationException, SAXException, ParseException {
        if (_fInPos == null)
            throw new NullPointerException("Invalid FileInputStream passed to loader");
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(_fInPos);
        _SubPlanToday = new SubstitutePlan((Element)doc.getDocumentElement().getElementsByTagName("today").item(0));
        if (!_SubPlanToday.isCurrent())
        {
            if (_SubPlanTomorrow.isCurrent()) { //move up the next Plan
                _SubPlanToday = _SubPlanTomorrow;
                _SubPlanTomorrow = null;
            }
            schedulePullNext();
        }
        _SubPlanTomorrow = new SubstitutePlan((Element)doc.getDocumentElement().getElementsByTagName("tomorrow").item(0));

        _fInPos.close();
        _fInPos = null;
    }

    private void saveSubPlans() throws ParserConfigurationException, TransformerException, IOException {
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = doc.createElement("SubPlans");
        root.appendChild(doc.createElement("today").appendChild( _SubPlanToday.toXML(doc)));
        root.appendChild(doc.createElement("tomorrow").appendChild(_SubPlanToday.toXML(doc)));

        Result res = new StreamResult(_fOutPos);

        Transformer xformer = TransformerFactory.newInstance().newTransformer();
        xformer.transform(new DOMSource(doc), res);

        _fOutPos.flush();
        _fOutPos.close();
        _fOutPos = null;

    }


    public void registerOnUpdateFinished(SubPlanUpdateFinished callback)
    {
        onUpdateFinished.add(callback);
    }

    public void scheduleSave(FileOutputStream fs)
    {
        if (_running) {
            _fOutPos = fs;
            tasks.add(_TaskType.LOAD);
        }
    }

    public void scheduleLoad(FileInputStream fs)
    {
        if (_running)
        {
            _fInPos = fs;
            tasks.add(_TaskType.LOAD);
        }
    }
    public void schedulePull()
    {
        if (_running)
            tasks.add(_TaskType.GET);
    }

    private void schedulePullNext() {
        if (_running)
            tasks.add(_TaskType.GET_NEXT);
    }

    private void update()
    {
        for (SubPlanUpdateFinished callback : onUpdateFinished)
            callback.onUpdate(_SubPlanToday);
    }

    @Override
    public void run() {
        while (_running || !tasks.isEmpty())
        {
            try{
                if (!tasks.isEmpty())
                    switch (tasks.remove())
                    {
                        case LOAD:
                            loadSubPlans();
                            update();
                            break;
                        case GET:
                            pullSubPlans();
                            update();
                            break;
                        case GET_NEXT:
                            pullNextSubPlan();
                            update();
                            break;
                        case SAVE:
                            saveSubPlans();
                            break;
                        default:
                            if (!tasks.isEmpty())
                                throw new IllegalArgumentException("Invalid Enum Passed to SubPlanTaskArray");
                            break;
                    }
            } catch (IOException | ParseException | ParserConfigurationException | SAXException | JSONException | TransformerException e)
            {
                e.printStackTrace();
            }
        }
    }
}
