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
 * Created by Dell on 2017/3/8.
 */
public class TabItemViewHolder {
    private String title;
    private Drawable icon;
    private View tabItemView;
    private TextView tvTitle;
    private ImageView imageView;
    private Context context;
    private Class<? extends Fragment> fragmentClass;

    public TabItemViewHolder(Context context){
        this.context = context;
    }

    public TabItemViewHolder(Context context, String title){
        this.context = context;
        this.title = title;
    }

    public TabItemViewHolder(Context context, int iconID){
        this.context = context;
        setIcon(iconID);
    }

    public TabItemViewHolder(Context context, String title, Drawable icon){
        this.context = context;
        this.title = title;
        this.icon = icon;
    }

    public TabItemViewHolder(Context context, String title, int iconID){
        this.context = context;
        this.title = title;
        setIcon(iconID);
    }

    public TabItemViewHolder(Context context, String title, int iconID, Class<? extends Fragment> fragmentClass){
        this.context = context;
        this.title = title;
        setIcon(iconID);
        this.fragmentClass = fragmentClass;
    }

    public View getTabView(){
        if(tabItemView == null){
            tabItemView = View.inflate(context, R.layout.tab_view_item,null);
            tvTitle = (TextView) tabItemView.findViewById(R.id.tab_title);
            imageView = (ImageView) tabItemView.findViewById(R.id.tab_icon);
        }
        if(TextUtils.isEmpty(title)){
            tvTitle.setVisibility(View.GONE);
        }else{
            tvTitle.setVisibility(View.VISIBLE);
            tvTitle.setText(title);
        }
        if(icon != null ){
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageDrawable(icon);
        }else{
            imageView.setVisibility(View.GONE);
        }
        return tabItemView;
    }

    public TabItemViewHolder setIcon(Drawable icon ){
        this.icon = icon;
        if(imageView !=null){
            if(icon != null ){
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageDrawable(icon);
            }else{
                imageView.setVisibility(View.GONE);
            }
        }
        return this;
    }

    public TabItemViewHolder setIcon(int id){
        return setIcon(context.getResources().getDrawable(id));
    }

    public TabItemViewHolder setTitle(int id){
        return setTitle(context.getResources().getText(id).toString());
    }

    public TabItemViewHolder setTitle(String title ){
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
        tabItemView.setSelected(selected);
        imageView.setSelected(selected);
        tvTitle.setSelected(selected);
    }
}
