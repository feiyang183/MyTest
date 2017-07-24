package com.example.dell.myscada.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.example.dell.myscada.R;
import com.example.dell.myscada.model.FragmentInfo;


/**
 * Created by yuankaifeng on 11/29/16.
 */

public class FlyviewPager extends Fragment {
    private View  rootView;
    private TextView tv;
    private FragmentInfo baseInfo;
    private TableLayout tableLayout;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    public View initView(){
        if(rootView == null){
            rootView = View.inflate(getActivity(), R.layout.flyview_pager,null);
            tableLayout = (TableLayout) rootView.findViewById(R.id.table2);
//            tv.setText(this.getClass().getSimpleName()+"----"+baseInfo.getTitle());
        }else{
            ViewGroup parent = (ViewGroup) rootView.getParent();
            if(parent != null){
                parent.removeView(rootView);
            }
        }
        return rootView;
    }

    public FlyviewPager setBaseInfo(FragmentInfo baseInfo){
        this.baseInfo = baseInfo;
        return this;
    }

    public FragmentInfo getBaseInfo() {
        return baseInfo;
    }
}
