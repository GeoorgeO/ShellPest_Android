package com.example.shellpest_android;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador_Tabla extends BaseAdapter {
    ArrayList<Tablas_Sincronizadas> itemlist=new ArrayList<Tablas_Sincronizadas>();
    Context mContext;


    public Adaptador_Tabla(Context c, ArrayList<Tablas_Sincronizadas> ArrayTablas) {
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
        View itemView = inflater.inflate(R.layout.registro_grid,viewGroup,false);
        TextView text_Columna=(TextView)itemView.findViewById(R.id.text_Columna);
        TextView et_NActualizados=(TextView)itemView.findViewById(R.id.et_NActualizados);


        text_Columna.setText(""+itemlist.get(i).getColumna());
        et_NActualizados.setText(""+itemlist.get(i).getCantidad());


        return itemView;
    }
}
