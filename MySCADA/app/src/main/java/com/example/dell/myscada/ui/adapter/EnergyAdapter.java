package com.example.dell.myscada.ui.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.dell.myscada.Activity.WindActivity;
import com.example.dell.myscada.R;
import com.example.dell.myscada.model.mEnergy;
import com.example.dell.myscada.ui.customview.WindView;

import java.util.List;

public class EnergyAdapter extends RecyclerView.Adapter<EnergyAdapter.ViewHolder> {
//    private final LayoutInflater mInflater;
    private List<mEnergy> mEnergyList;
//    private OnItemClickListener mOnItemClickListener;


    public EnergyAdapter(List<mEnergy> mEnergyList){
        super();
        this.mEnergyList = mEnergyList;
//        this.mInflater = LayoutInflater.from(context);
    }


    static class ViewHolder extends RecyclerView.ViewHolder{
        WindView energyWind;
        TextView energyId;
        TextView energyCapacity;
        TextView energySpeed;

        public ViewHolder(View view){
            super(view);
            energyWind = (WindView)view.findViewById(R.id.windView);
            energyId = (TextView)view.findViewById(R.id.mEnergy_id);
            energyCapacity = (TextView)view.findViewById(R.id.mEnergy_Capacity);
            energySpeed = (TextView)view.findViewById(R.id.mEnergy_speed);
        }
    }

    public void setEnergyList(List<mEnergy> energyList){
        this.mEnergyList = energyList;
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        final View view = inflater
                .inflate(R.layout.list_view_item,parent,false);

        final ViewHolder holder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mEnergy energy = mEnergyList.get(holder.getAdapterPosition());
                WindActivity.actionStart(parent.getContext(), energy);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position){
        mEnergy energy = mEnergyList.get(position);
        holder.energyWind.setmCurrentState(energy.getState());
        holder.energyId.setText(energy.getId());
        holder.energyCapacity.setText(String.format("%.2f", energy.getCapacity() )+ " kW");
        holder.energySpeed.setText(String.format("%.2f", energy.getSpeed()) + " m/s");
/*
        if(mOnItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    mOnItemClickListener.onClick(position);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    mOnItemClickListener.onLongClick(position);
                    return false;
                }
            });
        }
        */
//        if(mOnItemClickListener != null){
//            holder.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mOnItemClickListener.onClick(position);
//                }
//            });
//            holder.itemView.setOnClickListener(new View.OnLongClickListener(){
//                @Override
//                public boolean onLongClick(View v){
//                    mOnItemClickListener.onLongClick(position);
//                    return false;
//                }
//            });
//        }

    }

    @Override
    public int getItemCount(){
        if(mEnergyList == null){
            return 0;
        }
        return mEnergyList.size();
    }

/*    public interface OnItemClickListener{
        void onClick(int position);
        void onLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener ){
        this.mOnItemClickListener=onItemClickListener;
    }
*/
}

//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.TextView;
//
//
//import com.example.dell.myscada.R;
//import com.example.dell.myscada.model.mEnergy;
//import com.example.dell.myscada.ui.customview.WindView;
//
//import java.util.List;
//
///**
// * Created by Dell on 2017/3/9.
// */
//
//public class EnergyAdapter extends ArrayAdapter<mEnergy>{
//    private int resourceId;
//
//    public EnergyAdapter(Context context, int textViewResourceId, List<mEnergy> objects){
//        super(context,textViewResourceId,objects);
//        resourceId = textViewResourceId;
//    }
//
//    @Override
//    public View getView(int position,View convertView,ViewGroup parent){
//        mEnergy energy = getItem(position);
//        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);
//        WindView Energy_imageView = (WindView)view.findViewById(R.id.windView);
//        TextView Energy_id = (TextView)view.findViewById(R.id.mEnergy_id);
////        TextView Energy_state = (TextView)view.findViewById(R.id.mEnergy_state);
//        TextView Energy_capacity = (TextView)view.findViewById(R.id.mEnergy_Capacity);
//        TextView Energy_speed = (TextView)view.findViewById(R.id.mEnergy_speed);
//        Energy_id.setText(energy.getId());
//        Energy_imageView.setmCurrentState(energy.getImageId());
////        Energy_state.setText(energy.getState());
//        Energy_capacity.setText(energy.getCapacity()+"kw");
//        Energy_speed.setText(energy.getSpeed()+"m/s");
//        return view;
//    }
//
//}

