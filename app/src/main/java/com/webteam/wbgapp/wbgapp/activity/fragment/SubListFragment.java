package com.webteam.wbgapp.wbgapp.activity.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.webteam.wbgapp.wbgapp.R;
import com.webteam.wbgapp.wbgapp.structure.SubstitutePlan;

import java.util.ArrayList;

/**
 * Created by Deathlymad on 10.04.2016.
 */
public class SubListFragment extends android.support.v4.app.ListFragment{
    int mNum;
    ArrayList<String> Substitutions;
    SubListAdapter adapter = null;
    /**
     * Create a new instance of CountingFragment, providing "num"
     * as an argument.
     */
    static SubListFragment newInstance(int num) {
        SubListFragment f = new SubListFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }

    static SubListFragment newInstance(int num, SubstitutePlan plan) {
        SubListFragment f = new SubListFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        args.putStringArrayList("plan", plan.getSubstitutionByClass(Integer.toString(num + 5) + (num < 6 ? "/" : "")));
        f.setArguments(args);

        return f;
    }

    /**
     * When creating, retrieve this instance's number from its arguments.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNum = getArguments() != null ? getArguments().getInt("num") : 1;
        Substitutions = getArguments() != null ? getArguments().getStringArrayList("plan") : new ArrayList<String>();
    }

    /**
     * The Fragment's UI is just a simple text view showing its
     * instance number.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.pager_layout, container, false);
        View tv = v.findViewById(R.id.sub_page_title);
        ((TextView)tv).setText("Klasse " + (mNum + 5));
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
            adapter = new SubListAdapter(getActivity(), R.layout.element_layout, R.id.TV_class, Substitutions == null ? new ArrayList<String>() : Substitutions);
        setListAdapter(adapter);
    }
}
