package com.webteam.wbgapp.wbgapp.structure;

import com.webteam.wbgapp.wbgapp.util.Util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by Deathlymad on 20.03.2016 .
 */
public class SubstitutePlan {
    public enum SubstitutionType {
        Entfall,
        Betreuung,
        Vertretung,
        Klausur,
        Raumvertretung;

        public String getName(SubstitutionType type) {
            switch (type) {
                case Entfall:
                    return "Entfall"; //TODO: needs to be lokalized
                case Betreuung:
                    return "Betreuung";
                case Vertretung:
                    return "Vertretung";
                case Klausur:
                    return "Klausur";
                case Raumvertretung:
                    return "Raumvertretung";
                default:
                    return null;
            }
        }
    }

    private class SubstituteEntry
    {
        private final String grade, teacher, time, subject, room, commentary;
        SubstitutePlan.SubstitutionType type;

        SubstituteEntry(Element n)
        {
            time = n.getAttribute("stunde");
            teacher = n.getAttribute("vertretungslehrer");
            grade = n.getAttribute("klasse");
            subject = n.getAttribute("vertretungsfach");
            room = n.getAttribute("vertretungsraum");
            commentary = n.getAttribute("bemerkung");
            type = SubstitutionType.valueOf(n.getAttribute("art"));
        }
    }

    private List<SubstituteEntry> _plan;
    private Date _date;

    public SubstitutePlan(Document xmlSubstitues) throws ParseException {
        Element e = xmlSubstitues.getDocumentElement();
        String date;
        date = e.getAttribute("jahr") + "-";
        date += e.getAttribute("monat") + "-";
        date += e.getAttribute("tag");
        _date = Util.getDateFromString(date);

        NodeList nodes = e.getElementsByTagName("vertretung");
        for (int i = 0; i < nodes.getLength(); i++)
            _plan.add(new SubstituteEntry((Element)nodes.item(i)));
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
