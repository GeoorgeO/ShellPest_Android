package com.example.shellpest_android;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class AdaptadorSpinner extends ArrayAdapter<ItemDatoSpinner> {
    public AdaptadorSpinner(Context context, ArrayList<ItemDatoSpinner> Item){
        super(context,0,Item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position,View convertView, ViewGroup parent){
        if (convertView==null){
            convertView= LayoutInflater.from(getContext()).inflate(R.layout.dato_spinner,parent,false);
        }
        TextView textViewName=convertView.findViewById(R.id.text_Item);
        ItemDatoSpinner ItemActual=getItem(position);
        if(ItemActual!=null){
            textViewName.setText(ItemActual.getTexto());
            if (position==0){
                textViewName.setBackgroundColor(Color.rgb(151,182,170));
            }else{
                textViewName.setBackgroundColor(Color.WHITE);
            }
        }


        return convertView;
    }
}
