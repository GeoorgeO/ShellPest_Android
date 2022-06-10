package com.example.shellpest_android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class Adaptador_GridEntradaGasolina extends BaseAdapter {

    ArrayList<Itementradasgasolina> itemlist = new ArrayList<Itementradasgasolina>();
    Context mContext;

    public Adaptador_GridEntradaGasolina(Context c, ArrayList<Itementradasgasolina> ArrayTablas){
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
        View itemView = inflater.inflate(R.layout.item_grid_entradasgasolina, viewGroup, false);

        TextView txtv_folioregistroGas = (TextView) itemView.findViewById(R.id.txtv_folioregistroGas);
        TextView txtv_fecharegistroGas = (TextView) itemView.findViewById(R.id.txtv_fecharegistroGas);
        TextView txtv_huertaregistroGas = (TextView) itemView.findViewById(R.id.txtv_huertaregistroGas);
        TextView txtv_tiporegistroGas = (TextView)itemView.findViewById(R.id.txtv_tiporegistroGas);
        TextView txtv_cantregistroGas = (TextView) itemView.findViewById(R.id.txtv_cantregistroGas);

        txtv_folioregistroGas.setText(""+itemlist.get(i).getC_folio_gas());
        txtv_fecharegistroGas.setText(""+itemlist.get(i).getD_fechaingreso_gas());
        txtv_huertaregistroGas.setText(""+itemlist.get(i).getId_Huerta());
        txtv_tiporegistroGas.setText(""+itemlist.get(i).getV_tipo_gas());
        txtv_cantregistroGas.setText(""+itemlist.get(i).getV_cantingreso_gas());

        return itemView;
    }
}
