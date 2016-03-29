package com.webteam.wbgapp.wbgapp.util;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    public static int readInt(String name, JSONObject obj) throws JSONException
    {
        String nbr = obj.getString(name);

        if (nbr == null || nbr.equals(""))
            return 0;
        else
            return Integer.parseInt(nbr);
    }

    public static String getStringFromTStamp(long date) {
        return dateFormat.format(getDateFromTStamp(date));
    }
    public static String getStringFromDate(Date d)
    {
        return dateFormat.format(d);
    }
    public static Date getDateFromString(String s) throws ParseException {
        return dateFormat.parse(s);
    }
    public static Date getDateFromTStamp(long tstamp)
    {
        return new Date(tstamp * 1000L);
    }
    public static long getTStampFromDate(Date date)
    {
        return date.getTime() / 1000L;
    }

    public static String unescUnicode(String str) {
        return StringEscapeUtils.unescapeHtml(StringEscapeUtils.unescapeJava(str));
    }
    public static String escUnicode(String str) {
        return StringEscapeUtils.escapeHtml(StringEscapeUtils.escapeJava(str));
    }
}
