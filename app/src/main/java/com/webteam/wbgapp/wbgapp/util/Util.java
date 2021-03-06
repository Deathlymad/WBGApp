package com.webteam.wbgapp.wbgapp.util;

import android.content.Context;
import android.content.Intent;
import android.provider.CalendarContract;

import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

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
        return (StringEscapeUtils.unescapeHtml(StringEscapeUtils.unescapeJava(str)));
    }
    public static String escUnicode(String str) {
        return StringEscapeUtils.escapeJava(StringEscapeUtils.escapeHtml(str));
    }

    public static String getDateString(Date date)
    {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault()) + " " + getStringFromDate(date);
    }

    public static void registerEventToCalender(Context context, String title, Date beginTStamp, Date endTStamp, String location, String desc)
    {
        Calendar calBegin = Calendar.getInstance();
        calBegin.setTime(beginTStamp);
        Calendar calEnd = Calendar.getInstance();
        calEnd.setTime(endTStamp);
        Intent intent = new Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calBegin.getTimeInMillis())
                .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calEnd.getTimeInMillis())
                .putExtra(CalendarContract.Events.TITLE, title)
                .putExtra(CalendarContract.Events.DESCRIPTION, desc)
                .putExtra(CalendarContract.Events.EVENT_LOCATION, location)
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}
