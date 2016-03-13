package com.webteam.wbgapp.wbgapp.net;

/**
 * Created by Deathlymad on 01.02.2016.
 */
public interface IRequest {
    String[] getRequest();

    void handleResults(String... result);
}
