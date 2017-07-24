package com.example.dell.myscada.ui.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.example.dell.myscada.model.FragmentInfo;
import com.example.dell.myscada.model.mEnergy;
import com.example.dell.myscada.ui.ChartPager;
import com.example.dell.myscada.ui.ContentPager;
import com.example.dell.myscada.ui.EventmentPager;
import com.example.dell.myscada.ui.FlyviewPager;
import com.example.dell.myscada.ui.WorktimePager;

import java.util.List;

/**
 * Created by langchou on 11/30/16.
 */

public class FragmentViewPagerAdapter extends FragmentStatePagerAdapter {

    private List<FragmentInfo> fragmentInfoList;
    private mEnergy energy;

    final private String TAG = "Position";

    public FragmentViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void setFragmentInfo(List<FragmentInfo> fragmentInfoList, mEnergy energy){
        this.fragmentInfoList = fragmentInfoList;
        this.energy = energy;
    }

    @Override
    public Fragment getItem(int position) {
        if(fragmentInfoList == null || position >= fragmentInfoList.size()){
            return null;
        }
        if(position == 0){
            Log.d(TAG,"---->"+position);
            return new ContentPager().setBaseInfo(fragmentInfoList.get(position), energy);
        }
//        if(position == 1){
//            Log.d(TAG,"---->"+position);
//            return new WorktimePager().setBaseInfo(fragmentInfoList.get(position));}
        if(position == 1){
            return new EventmentPager().setBaseInfo(fragmentInfoList.get(position), energy);
        }if(position == 2){
            return new ChartPager().setBaseInfo(fragmentInfoList.get(position), energy);
        }
        else {
            Log.d(TAG,"---->"+position);
            return null;
        }

    }

    @Override
    public int getCount() {
        if(fragmentInfoList == null){
            return 0;
        }
        return fragmentInfoList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentInfoList.get(position).getTitle();
    }
}
