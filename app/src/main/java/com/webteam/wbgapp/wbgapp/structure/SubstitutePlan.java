package com.webteam.wbgapp.wbgapp.structure;

import com.webteam.wbgapp.wbgapp.util.Util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.FileOutputStream;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Deathlymad on 20.03.2016 .
 */
public class SubstitutePlan {


    private class SubstituteEntry
    {
        private final String grade, teacher, time, subject, room, commentary, type;

        SubstituteEntry(Element n)
        {
            time = n.getElementsByTagName("stunde").item(0).getTextContent();
            teacher = n.getElementsByTagName("vertretungslehrer").item(0).getTextContent();
            grade = n.getElementsByTagName("klasse").item(0).getTextContent();
            subject = n.getElementsByTagName("vertretungsfach").item(0).getTextContent();
            room = n.getElementsByTagName("vertretungsraum").item(0).getTextContent();
            commentary = n.getElementsByTagName("bemerkung").item(0).getTextContent();
            type = n.getElementsByTagName("art").item(0).getTextContent();
        }
    }

    private List<SubstituteEntry> _plan;
    private ArrayList<Integer> classSort;
    private Date _date;

    public SubstitutePlan(Document xmlSubstitues) throws ParseException {
        _plan = new ArrayList<>();
        Element e = xmlSubstitues.getDocumentElement();
        String date;
        date = e.getAttribute("tag") + ".";
        date += e.getAttribute("monat") + ".";
        date += e.getAttribute("jahr");
        _date = Util.getDateFromString(date);

        NodeList nodes = e.getElementsByTagName("vertretung");
        for (int i = 0; i < nodes.getLength(); i++)
        {
            Element el = (Element) nodes.item(i);
            _plan.add(new SubstituteEntry(el));
        }
    }

    public void save(FileOutputStream file) {
    }

    public void getSubstitutionByClass()
    {
        //
    }

    public void getSubstitutionByPlan()
    {
        //
    }
}
