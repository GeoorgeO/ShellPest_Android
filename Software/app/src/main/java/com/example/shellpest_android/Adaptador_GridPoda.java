package com.example.shellpest_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador_GridPoda extends BaseAdapter {

    ArrayList<ItemPoda> itemlist=new ArrayList<ItemPoda>();
    Context mContext;

    public Adaptador_GridPoda(Context mContext,ArrayList<ItemPoda> ArrayTablas) {
        this.mContext = mContext;
        this.itemlist=ArrayTablas;
    }

    @Override
    public int getCount() {
        return itemlist.size();
    }

    @Override
    public Object getItem(int i) {
        return itemlist.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_grid_poda,viewGroup,false);
        TextView tv_c_codigo_act=(TextView)itemView.findViewById(R.id.tv_c_codigo_act);
        TextView tv_v_nombre_act=(TextView)itemView.findViewById(R.id.tv_v_nombre_act);

        tv_c_codigo_act.setText(""+itemlist.get(i).getC_codigo_act());
        tv_v_nombre_act.setText(""+itemlist.get(i).getV_nombre_act());

        return itemView;
    }
}
