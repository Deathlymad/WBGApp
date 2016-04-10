package com.webteam.wbgapp.wbgapp.structure;

import com.webteam.wbgapp.wbgapp.util.Util;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Deathlymad on 20.03.2016 .
 */
public class SubstitutePlan {


    public List<ArrayList<String>> pack() {
        String[] classes = {"5", "6", "7", "8", "9", "10", "11", "12"};
        ArrayList<ArrayList<String>> list = new ArrayList<>();
        for ( String _class: classes) {
            list.add(getSubstitutionByClass(_class));
        }
        return list;
    }

    public boolean isCurrent() {
        Calendar curr = Calendar.getInstance();
        Calendar date = Calendar.getInstance();
        date.setTime(_date);
        return curr.get(Calendar.YEAR) == date.get(Calendar.YEAR) && curr.get(Calendar.DAY_OF_YEAR) == date.get(Calendar.DAY_OF_YEAR); //check if the date is today
    }

    public class SubstituteEntry
    {
        public String getType() {
            return type;
        }

        public String getCommentary() {
            return commentary;
        }

        public String getRoom() {
            return room;
        }

        public String getSubject() {
            return subject;
        }

        public String getTime() {
            return time;
        }

        public String getTeacher() {
            return teacher;
        }

        public String getGrade() {
            return grade;
        }

        private final String grade;
        private final String teacher;
        private final String time;
        private final String subject;
        private final String room;
        private final String commentary;
        private final String type;

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

        public Element getElement(Document doc)
        {
            Element e = doc.createElement("vertretung");
            Element e1 = doc.createElement("stunde");
            e1.appendChild(doc.createTextNode(String.valueOf(time)));
            e.appendChild(e1);
            e1 = doc.createElement("vertretungslehrer");
            e1.appendChild(doc.createTextNode(String.valueOf(teacher)));
            e.appendChild(e1);
            e1 = doc.createElement("klasse");
            e1.appendChild(doc.createTextNode(String.valueOf(grade)));
            e.appendChild(e1);
            e1 = doc.createElement("vertretungsfach");
            e1.appendChild(doc.createTextNode(String.valueOf(subject)));
            e.appendChild(e1);
            e1 = doc.createElement("vertretungsraum");
            e1.appendChild(doc.createTextNode(String.valueOf(room)));
            e.appendChild(e1);
            e1 = doc.createElement("bemerkung");
            e1.appendChild(doc.createTextNode(String.valueOf(commentary)));
            e.appendChild(e1);
            e1 = doc.createElement("art");
            e1.appendChild(doc.createTextNode(String.valueOf(type)));
            e.appendChild(e1);
            return e;
        }

        public JSONObject pack() throws JSONException
        {
            JSONObject obj = new JSONObject();
            obj.put("stunde", time);
            obj.put("vertretungslehrer", teacher);
            obj.put("klasse", grade);
            obj.put("vertretungsfach", subject);
            obj.put("vertretungsraum", room);
            obj.put("bemerkung", commentary);
            obj.put("art", type);
            return obj;
        }
    }

    private List<SubstituteEntry> _plan;
    private ArrayList<Integer> classSort;
    private Date _date;

    public SubstitutePlan(Element xmlSubstitues) throws ParseException {
        _plan = new ArrayList<>();
        Element e = xmlSubstitues;
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

    public Element toXML(Document doc) {
        Element root = doc.createElement("vertretungsplan");
        Calendar cal = Calendar.getInstance();
        cal.setTime(_date);
        root.setAttribute("tag", String.valueOf(cal.get(Calendar.DAY_OF_MONTH)));
        root.setAttribute("monat", String.valueOf(cal.get(Calendar.MONTH)));
        root.setAttribute("jahr", String.valueOf(cal.get(Calendar.YEAR)));

        for ( SubstituteEntry entry : _plan ) {
            root.appendChild(entry.getElement(doc));
        }
        return root;
    }

    public ArrayList<String> getSubstitutionByClass( String _class)
    {
        ArrayList<String> entrys = new ArrayList<>();
        for(SubstituteEntry entry : _plan)
            if (entry.getGrade().contains(_class))
            {
                try {
                    entrys.add(entry.pack().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        return entrys;
    }
}
