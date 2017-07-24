package com.example.dell.myscada.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.dell.myscada.Activity.WindActivity;
import com.example.dell.myscada.R;
import com.example.dell.myscada.model.FragmentInfo;
import com.example.dell.myscada.model.TababoveViewHolder;
import com.example.dell.myscada.model.mEnergy;
import com.example.dell.myscada.ui.adapter.FragmentViewPagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuankaifeng on 11/29/16.
 */
public class FirstPager extends Fragment {
    private View  rootView;
    private Button button;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private List<FragmentInfo> fragmentInfoList;
    private List<TababoveViewHolder> tababoveViewHolders;
    public static final String WINDER = "winder";
    private mEnergy energy = new mEnergy();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return initView();
    }

    public View initView(){
        if(rootView == null){
            rootView = View.inflate(getActivity(), R.layout.fragment_wind_pager,null);
            button = (Button) rootView.findViewById(R.id.back_wind);
            tabLayout = (TabLayout) rootView.findViewById(R.id.tab_layout_title);
            viewPager = (ViewPager) rootView.findViewById(R.id.view_pager_content);
            initViewPager();
        }else{
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null){//防止已经被添加过父view了，但是一般不会的
                parent.removeView(rootView);
            }
        }
        Log.e("tag","initView()");
        return rootView;
    }

    public  List<FragmentInfo>  initFragmentInfo(){
        String[] titles = new String[]{"概况","事件","图表"};
        fragmentInfoList = new ArrayList<>();
        tababoveViewHolders = new ArrayList<>();
        for(String t :titles){
            FragmentInfo info = new FragmentInfo(0,t);
            tabLayout.addTab(tabLayout.newTab().setCustomView(new TababoveViewHolder(getContext(),t).getTabView()));
            fragmentInfoList.add(info);
        }
        Log.e("tag","initFragmentInfo()");
        return fragmentInfoList;
    }

    public void initViewPager(){
        initFragmentInfo();
        Log.e("tag","initViewPager()" + energy.getId());
        FragmentViewPagerAdapter pagerAdapter = new FragmentViewPagerAdapter(getChildFragmentManager());
        pagerAdapter.setFragmentInfo(fragmentInfoList, energy);
        viewPager.setAdapter(pagerAdapter);
        //        tabLayout.setupWithViewPager(viewPager);这个不建议使用了 他只能从Pager中获取title
        //通过相互添加监听器来进行互动-----关键
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                getActivity().onBackPressed();
//                FragmentManager fragmentManager = getFragmentManager();
//                fragmentManager.popBackStack();
            }
        });
    }

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
        energy = ((WindActivity) activity).getEnergy();
    }

    public void refresh(mEnergy energy){
        Log.e("tag","refresh() ");
        TextView energy_id = (TextView) rootView.findViewById(R.id.energy_id);
        TextView energy_temperature = (TextView) rootView.findViewById(R.id.energy_temperature);
        TextView energy_speed = (TextView) rootView.findViewById(R.id.energy_speed);
        TextView energy_date = (TextView) rootView.findViewById(R.id.textTime1);
        energy_id.setText(energy.getId());
        energy_speed.setText(energy.getSpeed()+" m/s");
        energy_temperature.setText(energy.getTemperature()+" ºC");
        energy_date.setText(energy.getDate());
    }

  /*  public static FirstPager newInstance(String argument){
        Bundle bundle = new Bundle();
        bundle.putString(WINDER,argument);
        FirstPager firstPager = new FirstPager();
        firstPager.setArguments(bundle);
        return firstPager;
    }
    */

    private void finish(){
        getActivity().onBackPressed();
    }

}
