package com.example.dell.myscada.model;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dell.myscada.R;

/**
 * Created by Dell on 2017/3/14.
 */

public class TababoveViewHolder {
    private String title;
    private View tababoveView;
    private TextView tvTitle;
    private Context context;
    private Class<? extends Fragment> fragmentClass;

    public TababoveViewHolder(Context context){
        this.context = context;
    }

    public TababoveViewHolder(Context context, String title){
        this.context = context;
        this.title = title;
    }

    public TababoveViewHolder(Context context, String title, Class<? extends Fragment> fragmentClass){
        this.context = context;
        this.title = title;
        this.fragmentClass = fragmentClass;
    }

    public View getTabView(){
        if(tababoveView == null){
            tababoveView = View.inflate(context, R.layout.tab_view_above,null);
            tvTitle = (TextView) tababoveView.findViewById(R.id.tab_abovetitle);
        }
        if(TextUtils.isEmpty(title)){
            tvTitle.setVisibility(View.GONE);
        }else{
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        return tababoveView;
    }

    public TababoveViewHolder setTitle(int id){
        return setTitle(context.getResources().getText(id).toString());
    }

    public TababoveViewHolder setTitle(String title ){
        this.title  = title;
        if(tvTitle !=null) {
            if (TextUtils.isEmpty(title)) {
                tvTitle.setVisibility(View.GONE);
            } else {
                tvTitle.setVisibility(View.VISIBLE);
                tvTitle.setText(title);
            }
        }
        return this;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return fragmentClass;
    }

    public void setFragmentClass(Class<? extends Fragment> fragmentClass) {
        this.fragmentClass = fragmentClass;
    }

    /**
     * 用于外部手动调用
     * @param selected
     */
    public void setSelected(boolean selected){
        tababoveView.setSelected(selected);
        tvTitle.setSelected(selected);
    }
}
