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

    private void readToView(View v, int id, JSONObject obj, String name) throws JSONException {
        String data = obj.getString(name);
        if (data.equals("{}"))
            data = "";
        ((TextView) v.findViewById(id)).setText(data);
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
            readToView( v, R.id.TV_class, obj, "klasse");
            readToView( v, R.id.TV_lesson, obj, "stunde");
            readToView( v, R.id.TV_type, obj, "art");
            readToView( v, R.id.TV_substituteteacher, obj, "vertretungslehrer");
            readToView( v, R.id.TV_substituteroom, obj, "vertretungsraum");
            readToView( v, R.id.TV_substitute_subject, obj, "vertretungsfach");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return v;
    }
}
