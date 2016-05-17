package com.webteam.wbgapp.wbgapp.activity.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.structure.News;

import java.util.List;

/**
 * Created by Deathlymad on 10.04.2016.
 */
public class NewsListAdapter extends ArrayAdapter<News> {
    public NewsListAdapter(Context context, int resource, int textViewResourceId, List<News> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.display_news_element, parent, false);
        }

        News _news = getItem(position);

        TextView _teaser = (TextView)v.findViewById(R.id.article_element_title);
        _teaser.setText(_news.getTitle());

        TextView _teaserDate = (TextView)v.findViewById(R.id.article_element_date);
        _teaserDate.setText(_news.getDateString());

        return v;
    }

    public News get(int id)
    {
        int i = 0;
        int max = getCount();
        while (i < max)
        {
            if (getItem(i).getID() == id)
                return getItem(i);
            else
                i++;
        }
        return null;
    }
}
