package com.example.shellpest_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador_GridMonitorio extends BaseAdapter {
    ArrayList<Itemmonitoreo> itemlist=new ArrayList<Itemmonitoreo>();
    Context mContext;

    public Adaptador_GridMonitorio(Context c, ArrayList<Itemmonitoreo> ArrayTablas){
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
        View itemView = inflater.inflate(R.layout.item_grid_monitoreo,viewGroup,false);
        TextView txt_Punto=(TextView)itemView.findViewById(R.id.txt_Punto);
        TextView txt_PE=(TextView)itemView.findViewById(R.id.txt_PE);
        TextView txt_Organo=(TextView)itemView.findViewById(R.id.txt_Organo);
        TextView txt_Individuo=(TextView)itemView.findViewById(R.id.txt_Individuo);


        txt_Punto.setText(""+itemlist.get(i).getPuntoControl());
        txt_PE.setText(""+itemlist.get(i).getPE());
        txt_Organo.setText(""+itemlist.get(i).getDeteccion());
        txt_Individuo.setText(""+itemlist.get(i).getIndividuo());


        return itemView;
    }
}
