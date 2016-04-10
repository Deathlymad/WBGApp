package com.webteam.wbgapp.wbgapp.activity.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.webteam.wbgapp.wbgapp.net.SubPlanLoader;
import com.webteam.wbgapp.wbgapp.structure.SubstitutePlan;

/**
 * Created by Deathlymad on 10.04.2016.
 */
public class SubPagerAdapter extends FragmentStatePagerAdapter implements SubPlanLoader.SubPlanUpdateFinished {

    SubstitutePlan plan = null;

    public SubPagerAdapter(FragmentManager fm) {
        super(fm);
        SubPlanLoader loader = new SubPlanLoader();
        loader.schedulePull();
        loader.registerOnUpdateFinished(this);
        loader.start();
    }

    @Override
    public Fragment getItem(int position) {
        if (plan == null)
            return SubListFragment.newInstance(position);
        return SubListFragment.newInstance(position, plan);
    }

    @Override
    public int getCount() {
        return 8;
    }

    @Override
    public void onUpdate(SubstitutePlan plan) {
        this.plan = plan;
    }
}
