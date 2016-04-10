package com.webteam.wbgapp.wbgapp.activity.fragment;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Deathlymad on 10.04.2016.
 */
public class SubListAdapter extends ArrayAdapter<String> {
    public SubListAdapter(Context context, int resource, int textViewResourceId, List<String> objects) {
        super(context, resource, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(getContext());
            v = vi.inflate(R.layout.element_layout, parent, false);
        }

        try {
            JSONObject obj = new JSONObject(getItem(position));
            ((TextView) v.findViewById(R.id.TV_class)).setText(obj.getString("klasse"));
            ((TextView) v.findViewById(R.id.TV_lesson)).setText(obj.getString("stunde"));
            ((TextView) v.findViewById(R.id.TV_type)).setText(obj.getString("art"));
            ((TextView) v.findViewById(R.id.TV_substituteteacher)).setText(obj.getString("vertretungslehrer"));
            ((TextView) v.findViewById(R.id.TV_substituteroom)).setText(obj.getString("vertretungsraum"));
            ((TextView) v.findViewById(R.id.TV_substitute_subject)).setText(obj.getString("vertretungsfach"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }
}
