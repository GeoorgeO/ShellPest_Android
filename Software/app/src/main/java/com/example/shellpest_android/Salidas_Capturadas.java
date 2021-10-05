package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Salidas_Capturadas extends AppCompatActivity {

    String Usuario,Perfil,Huerta;

    ListView lv_GSalidas;
    ItemSalidas Tabla;
    Adaptador_GridSalidas Adapter;
    ArrayList<ItemSalidas> arrayArticulos;
    Integer Renglon,nclic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salidas_capturadas);

        getSupportActionBar().hide();

        Usuario = getIntent().getStringExtra("usuario");
        Perfil = getIntent().getStringExtra("perfil");
        Huerta = getIntent().getStringExtra("huerta");

        lv_GSalidas = (ListView) findViewById(R.id.lv_GSalidas);

        arrayArticulos = new ArrayList<>();


        Cargagrid();

        Renglon=0;
        nclic=0;

        lv_GSalidas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(Renglon==l){
                    nclic++;
                }else{
                    nclic=1;
                }
                if (nclic>1){
                    Intent intento = new Intent(getApplicationContext(), Salidas.class);
                    intento.putExtra("usuario2", Usuario);
                    intento.putExtra("perfil2", Perfil);
                    intento.putExtra("huerta2", Huerta);
                    intento.putExtra("ID",arrayArticulos.get(i).getId() );
                    intento.putExtra("CEPS",arrayArticulos.get(i).getcEPS() );

                    //Toast.makeText(this, jsonobject.optString("Id_Usuario")+","+jsonobject.optString("Id_Perfil")+","+jsonobject.optString("Id_Huerta"),Toast.LENGTH_SHORT).show();
                    startActivity(intento);
                    nclic=0;
                    finish();
                }
            }
        });

    }

    private void Cargagrid(){
        lv_GSalidas.setAdapter(null);
        arrayArticulos.clear();

        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        //Toast.makeText(this,objSDF.format(date1),Toast.LENGTH_SHORT).show();
        String Consulta;
        if (Perfil.equals("001")){
            Consulta="select A.Id_Salida, \n" +
                    "\t H.Nombre_Almacen,\n" +
                    "\t Fecha , \n" +
                    "\t A.Id_Almacen,A.c_codigo_eps \n" +
                    "from t_Salidas as A \n" +
                    "left join t_Almacen as H on H.Id_Almacen=A.Id_Almacen and A.c_codigo_eps=H.c_codigo_eps \n "+
                    "\t left join t_Huerta as Hue on Hue.Id_Huerta=H.Id_Huerta and Hue.c_codigo_eps=H.c_codigo_eps";
        }else{
            Consulta="select A.Id_Salida, \n" +
                    "\t H.Nombre_Almacen,\n" +
                    "\t Fecha , \n" +
                    "\t A.Id_Almacen,A.c_codigo_eps \n" +
                    "from t_Salidas as A \n" +
                    "left join t_Almacen as H on H.Id_Almacen=A.Id_Almacen and A.c_codigo_eps=H.c_codigo_eps \n "+
                    "\t left join t_Huerta as Hue on Hue.Id_Huerta=H.Id_Huerta and Hue.c_codigo_eps=H.c_codigo_eps \n" +
                    "where ltrim(rtrim(H.Id_Huerta))+ltrim(rtrim(A.c_codigo_eps)) in (select ltrim(rtrim(tem.Id_Huerta))+ltrim(rtrim(tem.c_codigo_eps)) from t_Usuario_Huerta as tem where tem.Id_Usuario='"+Usuario+"') ";
        }

        Cursor Renglon =BD.rawQuery(Consulta,null);

        if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.moveToFirst()) {

                do {
                    Tabla=new ItemSalidas(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4));
                    arrayArticulos.add(Tabla);
                } while (Renglon.moveToNext());


                BD.close();
            } else {
                Toast.makeText(this, "No hay datos en t_Aplicaciones_Det guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridSalidas(getApplicationContext(),arrayArticulos);
            lv_GSalidas.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }

    }

    public void Regresar(View view){
        this.onBackPressed();
    }

}