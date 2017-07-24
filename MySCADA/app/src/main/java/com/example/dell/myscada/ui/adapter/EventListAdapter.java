package com.example.dell.myscada.ui.adapter;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dell.myscada.R;

import java.util.ArrayList;

/**
 * Created by Dell on 2017/5/18.
 */

public class EventListAdapter extends BaseAdapter {
    private final LayoutInflater mInflater;
    private ArrayList<String> list;
    private int gree;
    private int red;
    private int yellow;

    public void setPopList(ArrayList<String> popList){
        this.list = popList;
    }

    public EventListAdapter(Context context){
        super();
        this.mInflater = LayoutInflater.from(context);
        gree = ContextCompat.getColor(context, R.color.green);
        red = ContextCompat.getColor(context, R.color.red);
        yellow = ContextCompat.getColor(context, R.color.yellow);
        Log.e("颜色初始：", "gree: "+gree+"red: "+red+"orange: "+yellow);
        list = new ArrayList<String>();
    }

    @Override
    public int getCount(){
        return list.size();
    }

    @Override
    public Object getItem(int arg0){
        return null;
    }

    @Override
    public long getItemId(int position){
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        ListHolder holder = null;

        if(convertView == null){
            holder = new ListHolder();
            convertView = mInflater.inflate(R.layout.event_view_item, null);
            holder.eventID = (TextView) convertView.findViewById(R.id.event_nb);
            holder.eventSurvey = (TextView) convertView.findViewById(R.id.event_survey);
            holder.eventTime = (TextView) convertView.findViewById(R.id.event_time);
            convertView.setTag(holder);
        }else {
            holder = (ListHolder) convertView.getTag();
        }
        String[] list2 = String.valueOf(list.get(position)).split("@_@@");
        if(list2[0].substring(0,1).equals("I")){
            holder.eventID.setTextColor(gree);
            holder.eventSurvey.setTextColor(gree);
            holder.eventTime.setTextColor(gree);
        } else if (list2[0].substring(0,1).equals("T")) {
            holder.eventID.setTextColor(red);
            holder.eventSurvey.setTextColor(red);
            holder.eventTime.setTextColor(red);
        } else if (list2[0].substring(0,1).equals("A")){
            holder.eventID.setTextColor(yellow);
            holder.eventSurvey.setTextColor(yellow);
            holder.eventTime.setTextColor(yellow);
        }
        holder.eventID.setText(list2[0]);
        holder.eventSurvey.setText(list2[1]);
        holder.eventTime.setText(list2[2]);
        Log.e("颜色",list2[0]+" "+holder.eventSurvey.getCurrentTextColor());
        return convertView;
    }

    class ListHolder{
        public TextView eventID;
        public TextView eventSurvey;
        public TextView eventTime;
    }
}
