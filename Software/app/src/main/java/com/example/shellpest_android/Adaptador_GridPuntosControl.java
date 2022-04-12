package com.example.shellpest_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador_GridPuntosControl extends BaseAdapter {
    ArrayList<ItemPuntosControl> itemlist=new ArrayList<ItemPuntosControl>();
    Context mContext;

    public Adaptador_GridPuntosControl(Context c, ArrayList<ItemPuntosControl> ArrayTablas){
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
        View itemView = inflater.inflate(R.layout.item_grid_puntos,viewGroup,false);
        TextView txt_IPunto=(TextView)itemView.findViewById(R.id.txt_IPunto);
        TextView txt_IHuerta=(TextView)itemView.findViewById(R.id.txt_IHuerta);
        TextView txt_INoPE=(TextView)itemView.findViewById(R.id.txt_INoPE);
        TextView txt_IFecha=(TextView)itemView.findViewById(R.id.txt_IFecha);



        txt_IPunto.setText(""+itemlist.get(i).getPuntoControl());
        txt_IHuerta.setText(""+itemlist.get(i).getHuerta());
        txt_INoPE.setText(""+itemlist.get(i).getNoPE());
        txt_IFecha.setText(""+itemlist.get(i).getFecha());


        return itemView;
    }
}
