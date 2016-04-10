package com.webteam.wbgapp.wbgapp.activity.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.structure.Event;
import com.webteam.wbgapp.wbgapp.structure.News;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Deathlymad on 10.04.2016.
 */
public class EventListAdapter extends ArrayAdapter<Event> {
    public EventListAdapter(Context context, int resource, int textViewResourceId, List<Event> objects) {
        super(context, resource, textViewResourceId, objects);
    }



    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.display_event_element, parent, false);
        }


        Event _news = getItem(position);

        TextView _teaser = (TextView)v.findViewById(R.id.article_element_title);
        _teaser.setText(_news.getTitle());

        TextView _teaserDate = (TextView)v.findViewById(R.id.article_element_date);
        _teaserDate.setText(_news.getDateString());
        return v;
    }
}
