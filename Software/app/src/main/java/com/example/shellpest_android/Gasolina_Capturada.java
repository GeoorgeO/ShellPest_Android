package com.example.shellpest_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Gasolina_Capturada extends AppCompatActivity {

    ListView lv_GridGasolina;

    Itemgasolina Tabla;
    Adaptador_GridGasolina Adapter;
    ArrayList<Itemgasolina> arrayGas;
    TextView MensajeToast;
    View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gasolina_capturada);
        getSupportActionBar().hide();

        lv_GridGasolina = (ListView) findViewById(R.id.lv_GridGasolina);

        arrayGas = new ArrayList<>();

        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custome_toast, (ViewGroup) findViewById(R.id.toast_layout_root));
        MensajeToast = (TextView) layout.findViewById(R.id.MensajeToast);

        cargaGrid();

        lv_GridGasolina.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //SE VA A UTILIZAR SI SE SELECCIONA ALGO DEL LIST VIEW Y CON LLEVA ALGUNA ACCION
                AlertDialog.Builder dialogo = new AlertDialog.Builder(Gasolina_Capturada.this);
                dialogo.setTitle("ELIMINAR REGISTRO SELECCIONADO");
                dialogo.setMessage("¿ Desea eliminar el registro seleccionado ?");
                dialogo.setCancelable(false);
                dialogo.setPositiveButton("CONFIRMAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
                        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
                        Date date1=objDate;

                        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(Gasolina_Capturada.this,"ShellPest",null,1);
                        SQLiteDatabase BD=SQLAdmin.getWritableDatabase();

                        Cursor RenglonElimGas =BD.rawQuery("select G.d_fechacrea_gas,\n" +
                                "\tG.c_folio_gas,\n"+
                                "\tG.d_fechainicio_gas,\n"+
                                "\tG.d_fechafin_gas,\n"+
                                "\tG.c_codigo_eps,\n"+
                                "\tG.Id_Huerta,\n"+
                                "\tG.v_Bloques_gas,\n"+
                                "\tG.Id_ActivosGas,\n"+
                                "\tG.c_codigo_emp,\n"+
                                "\tG.c_codigo_act,\n"+
                                "\tG.v_cantingreso_gas,\n"+
                                "\tG.v_cantsaldo_gas,\n"+
                                "\tG.v_tipo_gas,\n"+
                                "\tG.v_horometro_gas,\n"+
                                "\tG.v_kminicial_gas,\n"+
                                "\tG.v_kmfinal_gas,\n"+
                                "\tG.v_observaciones_gas \n"+
                                "from t_Consumo_Gasolina as G",null);


                        if (RenglonElimGas.moveToFirst()){
                            ContentValues registrosEGas = new ContentValues();
                            do{
                                registrosEGas.put("d_fechacrea_gas",RenglonElimGas.getString(0));
                                registrosEGas.put("c_folio_gas", RenglonElimGas.getString(1));
                                registrosEGas.put("d_fechainicio_gas", RenglonElimGas.getString(2));
                                registrosEGas.put("d_fechafin_gas", RenglonElimGas.getString(3));
                                registrosEGas.put("c_codigo_eps", RenglonElimGas.getString(4));//c_codigo_eps
                                registrosEGas.put("Id_Huerta", RenglonElimGas.getString(5));//Id_Huerta
                                registrosEGas.put("v_Bloques_gas", RenglonElimGas.getString(6));
                                registrosEGas.put("Id_ActivosGas", RenglonElimGas.getString(7));//Id_ActivosGas
                                registrosEGas.put("c_codigo_emp", RenglonElimGas.getString(8));//c_codigo_emp
                                registrosEGas.put("c_codigo_act", RenglonElimGas.getString(9));//c_codigo_act
                                registrosEGas.put("v_cantingreso_gas", RenglonElimGas.getString(10));
                                registrosEGas.put("v_cantsaldo_gas", RenglonElimGas.getString(11));
                                registrosEGas.put("v_tipo_gas", RenglonElimGas.getString(12));
                                registrosEGas.put("v_horometro_gas", RenglonElimGas.getString(13));
                                registrosEGas.put("v_kminicial_gas", RenglonElimGas.getString(14));
                                registrosEGas.put("v_kmfinal_gas", RenglonElimGas.getString(15));
                                registrosEGas.put("v_observaciones_gas", RenglonElimGas.getString(16));
                                BD.insert("t_Eliminados_Gasolina",null,registrosEGas);
                                Log.e("Renglon eliminar", registrosEGas.toString());

                                int cantidad= BD.delete("t_Consumo_Gasolina","d_fechainicio_gas='"+arrayGas.get(i).getD_fechainicio_gas()+
                                        "' and d_fechafin_gas='"+arrayGas.get(i).getD_fechafin_gas()+
                                        "' and  Id_ActivosGas='"+arrayGas.get(i).getId_ActivosGas()+"'",null);

                                if(cantidad>0){

                                }else{
                                    //Toast.makeText(Puntos_Capturados.this,"Ocurrio un error al intentar eliminar el usuario logeado, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
                                }
                            }while(RenglonElimGas.moveToNext());
                        }else{

                        }
                        BD.close();
                        cargaGrid();
                    }
                });
                dialogo.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });
                dialogo.show();
                return false;
            }
        });
    }

    public void cerrar(View view){
        super.onBackPressed();
    }

    private void cargaGrid(){
        lv_GridGasolina.setAdapter(null);
        arrayGas.clear(); //se usa para guardar los datos y cargarlos en el grid

        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Cursor Renglon =BD.rawQuery("select G.d_fechacrea_gas,\n" +
                "\tG.c_folio_gas,\n"+
                "\tG.d_fechainicio_gas,\n"+
                "\tG.d_fechafin_gas,\n"+
                "\tG.c_codigo_eps,\n"+
                "\tG.Id_Huerta,\n"+
                "\tG.v_Bloques_gas,\n"+
                "\tG.Id_ActivosGas,\n"+
                "\tG.c_codigo_emp,\n"+
                "\tG.c_codigo_act,\n"+
                "\tG.v_cantingreso_gas,\n"+
                "\tG.v_cantsaldo_gas,\n"+
                "\tG.v_tipo_gas,\n"+
                "\tG.v_horometro_gas,\n"+
                "\tG.v_kminicial_gas,\n"+
                "\tG.v_kmfinal_gas,\n"+
                "\tG.v_observaciones_gas \n"+
                "from t_Consumo_Gasolina as G",null);

        if(Renglon.moveToFirst()) {
            if (Renglon.moveToFirst()) {
                do {
                    Tabla=new Itemgasolina(Renglon.getString(0),Renglon.getString(1),Renglon.getString(2),Renglon.getString(3),Renglon.getString(4),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7),Renglon.getString(8),Renglon.getString(9),Renglon.getString(10),Renglon.getString(11),Renglon.getString(12),Renglon.getString(13),Renglon.getString(14),Renglon.getString(15),Renglon.getString(16));
                    arrayGas.add(Tabla);
                    Log.e("array GAS", arrayGas.toString());
                } while (Renglon.moveToNext());
                BD.close();
            } else {
                MensajeToast.setText("No hay datos en t_Consumo_Gasolina guardados");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
                BD.close();
            }
        }

        if(arrayGas.size()>0){
            Adapter=new Adaptador_GridGasolina(getApplicationContext(),arrayGas);
            lv_GridGasolina.setAdapter(Adapter);
            Log.e("Adaptador", lv_GridGasolina.toString());
        }else{
            //Toast.makeText(Gasolina.this, "No existen datos guardados.", Toast.LENGTH_SHORT).show();
        }

    }
}