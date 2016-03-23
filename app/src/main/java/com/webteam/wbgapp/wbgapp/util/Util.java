package com.webteam.wbgapp.wbgapp.util;

import org.apache.commons.lang.StringEscapeUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {

    public static Date getDateFromString(String s) throws ParseException {
        return new SimpleDateFormat("yyyy-MM-dd").parse(s);
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
