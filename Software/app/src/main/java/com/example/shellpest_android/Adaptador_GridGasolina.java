package com.example.shellpest_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador_GridGasolina extends BaseAdapter {
    ArrayList<Itemgasolina> itemlist = new ArrayList<Itemgasolina>();
    Context mContext;

    public Adaptador_GridGasolina(Context c, ArrayList<Itemgasolina> ArrayTablas) {
        mContext = c;
        itemlist = ArrayTablas;
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
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.item_grid_gasolina, viewGroup, false);
        TextView txtv_GFechaCons = (TextView) itemView.findViewById(R.id.txtv_GFechaCons);
        TextView txtv_GActivo = (TextView) itemView.findViewById(R.id.txtv_GActivo);
        TextView txtv_GRespons = (TextView) itemView.findViewById(R.id.txtv_GRespons);
        TextView txtv_GActividad = (TextView) itemView.findViewById(R.id.txtv_GActividad);
        TextView txtv_GCantUtil = (TextView) itemView.findViewById(R.id.txtv_GCantUtil);
        TextView txtv_GTipo = (TextView) itemView.findViewById(R.id.txtv_GTipo);

        txtv_GFechaCons.setText(""+itemlist.get(i).getD_fechaconsumo_gas());
        txtv_GActivo.setText(""+itemlist.get(i).getId_ActivosGas());
        txtv_GRespons.setText(""+itemlist.get(i).getC_codigo_emp());
        txtv_GActividad.setText(""+itemlist.get(i).getC_codigo_act());
        txtv_GCantUtil.setText(""+itemlist.get(i).getV_cantutilizada_gas());
        txtv_GTipo.setText(""+itemlist.get(i).getV_tipo_gas());

        return itemView;
    }
}
