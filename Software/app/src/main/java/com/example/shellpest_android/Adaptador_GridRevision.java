package com.example.shellpest_android;

import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador_GridRevision extends BaseAdapter {

    ArrayList<ItemRevision> itemlist=new ArrayList<ItemRevision>();
    Context mContext;

    public Adaptador_GridRevision(Context mContext,ArrayList<ItemRevision> ArrayTablas) {
        this.mContext = mContext;
        this.itemlist=ArrayTablas;
    }


    @Override
    public int getCount() {
        return itemlist.size();
    }

    @Override
    public Object getItem(int position) {
        return itemlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_grid_revision,parent,false);
        TextView tv_tBloque=(TextView)itemView.findViewById(R.id.tv_tBloque);
        TextView tv_flor=(TextView)itemView.findViewById(R.id.tv_flor);
        TextView tv_narbol=(TextView)itemView.findViewById(R.id.tv_narbol);
        TextView tv_fruta=(TextView)itemView.findViewById(R.id.tv_fruta);
        TextView tv_nhumedad=(TextView)itemView.findViewById(R.id.tv_nhumedad);

        tv_tBloque.setText(""+itemlist.get(position).getId_Bloque());
        tv_flor.setText(""+itemlist.get(position).getFloracion());
        tv_narbol.setText(""+itemlist.get(position).getN_Arboles());
        tv_fruta.setText(""+itemlist.get(position).getFruta());
        tv_nhumedad.setText(""+itemlist.get(position).getNivel_Humedad());

        return itemView;
    }


}
