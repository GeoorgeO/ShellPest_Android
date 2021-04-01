package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Puntos_Capturados extends AppCompatActivity {
String Usuario,Perfil,Huerta;

    ListView lv_Puntos;
    ItemPuntosControl Tabla;
    Adaptador_GridPuntosControl Adapter;
    ArrayList<ItemPuntosControl> arrayArticulos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puntos_capturados);

        Usuario = getIntent().getStringExtra("usuario");
        Perfil = getIntent().getStringExtra("perfil");
        Huerta = getIntent().getStringExtra("huerta");

        lv_Puntos = (ListView) findViewById(R.id.lv_Puntos);

        arrayArticulos = new ArrayList<>();
        Cargagrid();
    }

    private void Cargagrid(){
        lv_Puntos.setAdapter(null);
        arrayArticulos.clear();

        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        Toast.makeText(this,objSDF.format(date1),Toast.LENGTH_SHORT).show();

        Cursor Renglon =BD.rawQuery("select P.Nombre_PuntoControl, \n" +
                "\tH.Nombre_Huerta,\n" +
                "\t count(M.Id_Individuo) \n" +
                "from t_Monitoreo_PE as M \n" +
                "inner join t_Puntocontrol as P on M.Id_PuntoControl=P.Id_PuntoControl \n" +
                "inner join t_Bloque as B on P.Id_Bloque=B.Id_Bloque \n" +
                "left join t_Huerta as H on B.Id_Huerta=H.Id_Huerta\n" +
                "where M.Fecha='"+objSDF.format(date1)+"' group by M.Fecha,H.Nombre_Huerta,P.Id_PuntoControl,P.Nombre_PuntoControl ",null);

        if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.moveToFirst()) {

                do {
                    Tabla=new ItemPuntosControl(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2));
                    arrayArticulos.add(Tabla);
                } while (Renglon.moveToNext());


                BD.close();
            } else {
                Toast.makeText(this, "No hay datos en t_Monitoreo_PE guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridPuntosControl(getApplicationContext(),arrayArticulos);
            lv_Puntos.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }

    }

}