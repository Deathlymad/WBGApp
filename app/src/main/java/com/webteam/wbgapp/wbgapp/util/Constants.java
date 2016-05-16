package com.webteam.wbgapp.wbgapp.util;

/**
 * Created by Deathlymad on 07.05.2016.
 */
public class Constants {

    public static final String SERVER_URL = "http://beta.wbgym.de/wbgapp-webservice";
    public static final String BACKROUND_CLASS = "com.webteam.wbgapp.wbgapp.net.BackgroundService";

    public static final String NEWS_ARTICLE_DATA = "com.webteam.wbgapp.wbgapp.NEWS";
    public static final String EVENT_ARTICLE_DATA = "com.webteam.wbgapp.wbgapp.EVENTS";

    //BackgroundService Intents
    public static final String INTENT_GET_SUB_PLAN = "GetSubPlan";

    public static final String INTENT_GET_NEXT_SUB_PLAN = "GetNextSubPlan";

    public static final String INTENT_GET_NEXT_EVENT = "GetEvent";
    public static final String INTENT_GET_EVENT_CONTENT = "GetEventData";

    public static final String INTENT_GET_NEXT_NEWS = "GetNews";
    public static final String INTENT_GET_NEWS_CONTENT = "GetNewsData";

    //File Data Locations
    public static final String FILE_EVENT = "chachedEvents.bin";
    public static final String FILE_NEWS = "chachedNews.bin";
}
