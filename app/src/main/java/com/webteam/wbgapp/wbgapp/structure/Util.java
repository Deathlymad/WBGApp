package com.webteam.wbgapp.wbgapp.structure;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.Date;

class Util {

    public static Date getDateFromTStamp(long tstamp)
    {
        return new Date(tstamp * 1000L);
    }

    public static String unescUnicode(String str)
    {
        return StringEscapeUtils.unescapeHtml(StringEscapeUtils.unescapeJava(str));
    }
    public static String escUnicode(String str)
    {
        return StringEscapeUtils.escapeHtml(StringEscapeUtils.escapeJava(str));
    }
}
