package com.example.shellpest_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador_GridCosecha extends BaseAdapter {

    ArrayList<ItemCosecha> itemlist=new ArrayList<ItemCosecha>();
    Context mContext;

    public Adaptador_GridCosecha(Context mContext,ArrayList<ItemCosecha> ArrayTablas) {
        this.mContext = mContext;
        this.itemlist=ArrayTablas;
    }

    @Override
    public int getCount() {
        return 0;
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
        View itemView = inflater.inflate(R.layout.item_grid_cosecha,viewGroup,false);
        TextView tv_Blq=(TextView)itemView.findViewById(R.id.tv_Blq);
        TextView tv_ibico=(TextView)itemView.findViewById(R.id.tv_ibico);
        TextView tv_icos=(TextView)itemView.findViewById(R.id.tv_icos);
        TextView tv_ides=(TextView)itemView.findViewById(R.id.tv_ides);
        TextView tv_ipep=(TextView)itemView.findViewById(R.id.tv_ipep);
        TextView tv_idir=(TextView)itemView.findViewById(R.id.tv_idir);

        tv_Blq.setText(""+itemlist.get(i).getNombre_Blq());
        tv_ibico.setText(""+itemlist.get(i).getBICO());
        tv_icos.setText(""+itemlist.get(i).getN_c_cosecha());
        tv_ides.setText(""+itemlist.get(i).getN_c_desecho());
        tv_ipep.setText(""+itemlist.get(i).getN_c_pepena());
        tv_idir.setText(""+itemlist.get(i).getN_c_diario());

        return itemView;
    }
}
