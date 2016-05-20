package com.webteam.wbgapp.wbgapp.structure;

import com.webteam.wbgapp.wbgapp.util.Util;

import org.json.JSONArray;
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

        SubstituteEntry(JSONObject n) throws JSONException
        {
            time = n.getString("stunde");
            teacher = n.getString("vertretungslehrer");
            grade = n.getString("klasse");
            subject = n.getString("vertretungsfach");
            room = n.getString("vertretungsraum");
            commentary = n.getString("bemerkung");
            type = n.getString("art");
        }

        public JSONObject pack() throws JSONException
        {
            JSONObject obj = new JSONObject();
            obj.put("stunde", time)
            .put("vertretungslehrer", teacher)
            .put("klasse", grade)
            .put("vertretungsfach", subject)
            .put("vertretungsraum", room)
            .put("bemerkung", commentary)
            .put("art", type);
            return obj;
        }
    }

    private List<SubstituteEntry> _plan;
    private Date _date;

    public SubstitutePlan(JSONObject data) throws JSONException, ParseException {
        _plan = new ArrayList<>();

        JSONObject dateObj = data.getJSONObject("@attributes");
        String date;
        date = dateObj.getString("tag") + ".";
        date += dateObj.getString("monat") + ".";
        date += dateObj.getString("jahr");
        _date = Util.getDateFromString(date);

        JSONArray substitutions = data.getJSONArray("vertretung");
        for (int i = 0; i < substitutions.length(); i++)
        {
            _plan.add(new SubstituteEntry(new JSONObject(Util.unescUnicode(substitutions.getString(i)))));
        }
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

    public String toString()
    {
        try {
            JSONObject subData = new JSONObject();

            JSONObject dateStruct = new JSONObject();
            Calendar cal = Calendar.getInstance();
            cal.setTime(_date);
            dateStruct.put("tag", cal.get(Calendar.DAY_OF_MONTH));
            dateStruct.put("monat", cal.get(Calendar.MONTH));
            dateStruct.put("jahr", cal.get(Calendar.YEAR));

            JSONArray subs = new JSONArray();
            for (SubstituteEntry entry : _plan)
                subs.put(entry.pack().toString());

            subData .put("@attributes", dateStruct)
                    .put("vertretung", subs);
            return subData.toString();
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }
}
