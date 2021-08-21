package com.example.shellpest_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador_GridAplicaciones extends BaseAdapter {
    ArrayList<ItemAplicaciones> itemlist=new ArrayList<ItemAplicaciones>();
    Context mContext;

    public Adaptador_GridAplicaciones(Context c, ArrayList<ItemAplicaciones> ArrayTablas){
        mContext = c;
        itemlist=ArrayTablas;
    }

    @Override
    public int getCount() {
        return itemlist.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_grid_aplicaciones,viewGroup,false);
        TextView txt_Id=(TextView)itemView.findViewById(R.id.txt_Id);
        TextView txt_Huerta=(TextView)itemView.findViewById(R.id.txt_Huerta);
        TextView txt_FechaApli=(TextView)itemView.findViewById(R.id.txt_FechaApli);



        txt_Id.setText(""+itemlist.get(i).getId());
        txt_Huerta.setText(""+itemlist.get(i).getHuerta());
        txt_FechaApli.setText(""+itemlist.get(i).getFecha());



        return itemView;
    }
}
