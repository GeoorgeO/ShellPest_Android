package com.example.shellpest_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador_GridSalida extends BaseAdapter {
    ArrayList<Itemsalida> itemlist=new ArrayList<Itemsalida>();
    Context mContext;

    public Adaptador_GridSalida(Context c, ArrayList<Itemsalida> ArrayTablas){
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
        View itemView = inflater.inflate(R.layout.item_grid_salida,viewGroup,false);
        TextView text_Fecha=(TextView)itemView.findViewById(R.id.txt_Fecha);
        TextView text_Producto=(TextView)itemView.findViewById(R.id.text_Producto);
        TextView text_Cantidad=(TextView)itemView.findViewById(R.id.text_Cantidad);
        TextView text_Unidad=(TextView)itemView.findViewById(R.id.text_Unidad);
        TextView text_CC=(TextView)itemView.findViewById(R.id.text_CC);


        text_Fecha.setText(""+itemlist.get(i).getFecha());
        text_Producto.setText(""+itemlist.get(i).getNombre_Producto());
        text_Cantidad.setText(""+itemlist.get(i).getCantidad());
        text_Unidad.setText(""+itemlist.get(i).getNombre_Unidad());
        text_CC.setText(""+itemlist.get(i).getCentro_Costos());


        return itemView;
    }
}
