package com.example.shellpest_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

public class Fertiliza_Capturadas extends AppCompatActivity {

    String Usuario,Perfil,Huerta;

    ListView lv_Fertilizaciones;
    ItemAplicaciones Tabla;
    Adaptador_GridAplicaciones Adapter;
    ArrayList<ItemAplicaciones> arrayArticulos;
    Integer Renglon,nclic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fertiliza_capturadas);

        getSupportActionBar().hide();

        Usuario = getIntent().getStringExtra("usuario");
        Perfil = getIntent().getStringExtra("perfil");
        Huerta = getIntent().getStringExtra("huerta");

        lv_Fertilizaciones = (ListView) findViewById(R.id.lv_Fertilizaciones);

        arrayArticulos = new ArrayList<>();


        Cargagrid();

        Renglon=0;
        nclic=0;

        lv_Fertilizaciones.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(Renglon==l){
                    nclic++;
                }else{
                    nclic=1;
                }
                if (nclic>1){
                    Intent intento = new Intent(getApplicationContext(), Fertilizacion.class);
                    intento.putExtra("usuario2", Usuario);
                    intento.putExtra("perfil2", Perfil);
                    intento.putExtra("huerta2", Huerta);
                    intento.putExtra("ID",arrayArticulos.get(i).getId() );
                    intento.putExtra("CEPS",arrayArticulos.get(i).getcEPS() );

                    //Toast.makeText(this, jsonobject.optString("Id_Usuario")+","+jsonobject.optString("Id_Perfil")+","+jsonobject.optString("Id_Huerta"),Toast.LENGTH_SHORT).show();
                    startActivity(intento);
                    nclic=0;
                }
            }

        });

        lv_Fertilizaciones.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                nclic=0;
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(Fertiliza_Capturadas.this);
                dialogo1.setTitle("ELIMINAR REGISTRO SELECCIONADO");
                dialogo1.setMessage("¿ Quieres eliminar el registro seleccionado ?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //aceptar();

                        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
                        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
                        Date date1=objDate;

                        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(Fertiliza_Capturadas.this,"ShellPest",null,1);
                        SQLiteDatabase BD=SQLAdmin.getWritableDatabase();
                        //BD.beginTransaction();

                        int cantidad= BD.delete("t_Fertiliza_Det","Id_Fertiliza='"+arrayArticulos.get(i).getId()+"' and c_codigo_eps='"+arrayArticulos.get(i).getcEPS()+"' ",null);

                        if(cantidad>0){
                            int cantidad2= BD.delete("t_Fertiliza","Id_Fertiliza='"+arrayArticulos.get(i).getId()+"' and c_codigo_eps='"+arrayArticulos.get(i).getcEPS()+"'  ",null);

                            if(cantidad2>0){

                            }else{
                                Toast.makeText(Fertiliza_Capturadas.this,"Ocurrio un error al intentar eliminar la Fertilización seleccionada, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            //Toast.makeText(Puntos_Capturados.this,"Ocurrio un error al intentar eliminar el usuario logeado, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                        }

                        //BD.endTransaction();
                        BD.close();
                        Cargagrid();
                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                        //cancelar();
                    }
                });
                dialogo1.show();
                return false;
            }
        });
    }

    private void Cargagrid(){
        lv_Fertilizaciones.setAdapter(null);
        arrayArticulos.clear();

        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
        Date date1=objDate;

        //Toast.makeText(this,objSDF.format(date1),Toast.LENGTH_SHORT).show();
        String Consulta;
        if (Perfil.equals("001")){
            Consulta="select A.Id_Fertiliza, \n" +
                    "\tH.Nombre_Huerta,\n" +
                    "\t min(Ad.Fecha) || ' - ' || max(AD.Fecha) as Fecha , \n" +
                    "\t A.Id_Huerta,A.c_codigo_eps \n" +
                    "from t_Fertiliza as A \n" +
                    "inner join t_Huerta as H on H.Id_Huerta=A.Id_Huerta and A.c_codigo_eps=H.c_codigo_eps \n "+
                    "left join t_Fertiliza_Det as AD on AD.Id_Fertiliza=A.Id_Fertiliza and  AD.c_codigo_eps=A.c_codigo_eps \n" +
                    " where A.Enviado='0' group by A.Id_Fertiliza,A.c_codigo_eps ";
        }else{
            Consulta="select A.Id_Fertiliza, \n" +
                    "\tH.Nombre_Huerta,\n" +
                    "\t min(Ad.Fecha) || ' - ' || max(AD.Fecha) as Fecha , \n" +
                    "\t A.Id_Huerta,A.c_codigo_eps \n" +
                    "from t_Fertiliza as A \n" +
                    "left join t_Huerta as H on H.Id_Huerta=A.Id_Huerta and A.c_codigo_eps=H.c_codigo_eps \n "+
                    "left join t_Fertiliza_Det as AD on AD.Id_Fertiliza=A.Id_Fertiliza and  AD.c_codigo_eps=A.c_codigo_eps \n" +
                    "where A.Enviado='0' and ltrim(rtrim(A.Id_Huerta))+ltrim(rtrim(A.c_codigo_eps)) in (select ltrim(rtrim(tem.Id_Huerta))+ltrim(rtrim(tem.c_codigo_eps)) from t_Usuario_Huerta as tem where tem.Id_Usuario='"+Usuario+"') group by A.Id_Fertiliza,A.c_codigo_eps ";
        }

        Cursor Renglon =BD.rawQuery(Consulta,null);

        if(Renglon.moveToFirst()) {
            /*et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));*/
            if (Renglon.moveToFirst()) {
                do {
                    Tabla=new ItemAplicaciones(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4));
                    arrayArticulos.add(Tabla);
                } while (Renglon.moveToNext());

                BD.close();
            } else {
                Toast.makeText(this, "No hay datos en t_Fertiliza_Det guardados", Toast.LENGTH_SHORT).show();
                BD.close();
            }
        }
        if(arrayArticulos.size()>0){
            Adapter=new Adaptador_GridAplicaciones(getApplicationContext(),arrayArticulos);
            lv_Fertilizaciones.setAdapter(Adapter);
        }else{
            //Toast.makeText(activity_Monitoreo.this, "No exisyen datos guardados.", Toast.LENGTH_SHORT).show();
        }

    }

    public void Regresar(View view){
        this.onBackPressed();
    }

}