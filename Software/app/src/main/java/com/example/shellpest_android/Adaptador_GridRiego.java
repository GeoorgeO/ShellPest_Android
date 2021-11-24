package com.example.shellpest_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador_GridRiego extends BaseAdapter {
    ArrayList<ItemRiego> itemlist=new ArrayList<ItemRiego>();
    Context mContext;

    public Adaptador_GridRiego(Context c, ArrayList<ItemRiego> ArrayTablas){
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
        View itemView = inflater.inflate(R.layout.item_grid_riego,viewGroup,false);
        TextView txt_RBloque=(TextView)itemView.findViewById(R.id.txt_RBloque);
        TextView txt_RPrecipitacion=(TextView)itemView.findViewById(R.id.txt_RPrecipitacion);
        TextView txt_RCaudalIni=(TextView)itemView.findViewById(R.id.txt_RCaudalIni);
        TextView txt_RCaudalFin=(TextView)itemView.findViewById(R.id.txt_RCaudalFin);
        TextView txt_RRiego=(TextView)itemView.findViewById(R.id.txt_RRiego);
        TextView txt_RTemperatura=(TextView)itemView.findViewById(R.id.txt_RTemperatura);
        TextView txt_RET=(TextView)itemView.findViewById(R.id.txt_RET);


        txt_RBloque.setText(""+itemlist.get(i).getNombre_Bloque());
        txt_RPrecipitacion.setText(""+itemlist.get(i).getPrecipitacion_Sistema());
        txt_RCaudalIni.setText(""+itemlist.get(i).getCaudal_Inicio());
        txt_RCaudalFin.setText(""+itemlist.get(i).getCaudal_Fin());
        txt_RRiego.setText(""+itemlist.get(i).getHoras_Riego());
        txt_RTemperatura.setText(""+itemlist.get(i).getTemperatura());
        txt_RET.setText(""+itemlist.get(i).getET());


        return itemView;
    }
}
