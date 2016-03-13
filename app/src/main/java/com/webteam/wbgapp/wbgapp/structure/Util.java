package com.webteam.wbgapp.wbgapp.structure;

import org.apache.commons.lang.StringEscapeUtils;

import java.util.Date;

/**
 * Created by Deathlymad on 12.03.2016.
 */
public class Util {

    public static Date getDateFromTStamp(long tstamp)
    {
        return new Date(tstamp * 1000L);
    }

    public static String convUnicode(String str)
    {
        return StringEscapeUtils.unescapeHtml(StringEscapeUtils.unescapeJava(str));
    }
}
