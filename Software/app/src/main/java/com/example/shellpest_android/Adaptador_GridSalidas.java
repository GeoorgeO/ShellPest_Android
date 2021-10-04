package com.example.shellpest_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador_GridSalidas extends BaseAdapter {
    ArrayList<ItemSalidas> itemlist=new ArrayList<ItemSalidas>();
    Context mContext;

    public Adaptador_GridSalidas(Context c, ArrayList<ItemSalidas> ArrayTablas){
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
        View itemView = inflater.inflate(R.layout.item_grid_salidas,viewGroup,false);
        TextView tv_IdSalida=(TextView)itemView.findViewById(R.id.tv_IdSalida);
        TextView tv_FechaSal=(TextView)itemView.findViewById(R.id.tv_FechaSal);
        TextView tv_AlmacenSal=(TextView)itemView.findViewById(R.id.tv_AlmacenSal);
        TextView tv_EpsSal=(TextView)itemView.findViewById(R.id.tv_EpsSal);



        tv_IdSalida.setText(""+itemlist.get(i).getId());
        tv_FechaSal.setText(""+itemlist.get(i).getFecha());
        tv_AlmacenSal.setText(""+itemlist.get(i).getAlmacen());
        tv_EpsSal.setText(""+itemlist.get(i).getcEPS());



        return itemView;
    }
}
