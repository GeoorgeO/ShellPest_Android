package com.example.shellpest_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

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
        TextView txtv_GFechaIni = (TextView) itemView.findViewById(R.id.txtv_GFechaIni);
        TextView txtv_GFechaFin = (TextView) itemView.findViewById(R.id.txtv_GFechaFin);
        TextView txtv_GCantIni = (TextView) itemView.findViewById(R.id.txtv_GCantIni);
        TextView txtv_GCantSaldo = (TextView) itemView.findViewById(R.id.txtv_GCantSaldo);
        TextView txtv_GActivo = (TextView) itemView.findViewById(R.id.txtv_GActivo);
        TextView txtv_GTipo = (TextView) itemView.findViewById(R.id.txtv_GTipo);

        txtv_GFechaIni.setText(""+itemlist.get(i).getD_fechainicio_gas());
        txtv_GFechaFin.setText(""+itemlist.get(i).getD_fechafin_gas());
        txtv_GCantIni.setText(""+itemlist.get(i).getV_cantingreso_gas());
        txtv_GCantSaldo.setText(""+itemlist.get(i).getV_cantsaldo_gas());
        txtv_GActivo.setText(""+itemlist.get(i).getId_ActivosGas());
        txtv_GTipo.setText(""+itemlist.get(i).getV_tipo_gas());

        return itemView;
    }
}
