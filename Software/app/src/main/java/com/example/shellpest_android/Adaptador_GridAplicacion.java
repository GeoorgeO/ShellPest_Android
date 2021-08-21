package com.example.shellpest_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador_GridAplicacion extends BaseAdapter {

    ArrayList<Itemaplicacion> itemlist=new ArrayList<Itemaplicacion>();
    Context mContext;

    public Adaptador_GridAplicacion(Context c, ArrayList<Itemaplicacion> ArrayTablas){
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
        View itemView = inflater.inflate(R.layout.item_grid_aplicacion,viewGroup,false);
        TextView text_Fecha=(TextView)itemView.findViewById(R.id.text_Fecha);
        TextView text_ApliProducto=(TextView)itemView.findViewById(R.id.text_ApliProducto);
        TextView text_ApliCantidad=(TextView)itemView.findViewById(R.id.text_ApliCantidad);
        TextView text_ApliUnidad=(TextView)itemView.findViewById(R.id.text_ApliUnidad);
        TextView text_ApliPipadas=(TextView)itemView.findViewById(R.id.text_ApliPipadas);


        text_Fecha.setText(""+itemlist.get(i).getFecha());
        text_ApliProducto.setText(""+itemlist.get(i).getNombre_Producto());
        text_ApliCantidad.setText(""+itemlist.get(i).getCantidad());
        text_ApliUnidad.setText(""+itemlist.get(i).getNombre_Unidad());
        text_ApliPipadas.setText(""+itemlist.get(i).getUnidades_aplicadas());


        return itemView;
    }

}
