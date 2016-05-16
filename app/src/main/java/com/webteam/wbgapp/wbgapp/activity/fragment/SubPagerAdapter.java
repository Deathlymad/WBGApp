package com.webteam.wbgapp.wbgapp.activity.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.webteam.wbgapp.wbgapp.net.BackgroundService;
import com.webteam.wbgapp.wbgapp.net.SubPlanLoader;
import com.webteam.wbgapp.wbgapp.structure.SubstitutePlan;
import com.webteam.wbgapp.wbgapp.util.Constants;

/**
 * Created by Deathlymad on 10.04.2016.
 */
public class SubPagerAdapter extends FragmentStatePagerAdapter implements BackgroundService.UpdateListener {

    SubstitutePlan plan = null;

    public SubPagerAdapter(FragmentManager fm) {
        super(fm);
        BackgroundService.registerUpdate(this);
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
    public void onUpdate(String Type) {
            this.plan = BackgroundService.getSubPlan();
    }

    @Override
    public String getUpdateType() {
        return Constants.INTENT_GET_SUB_PLAN;
    }
}
