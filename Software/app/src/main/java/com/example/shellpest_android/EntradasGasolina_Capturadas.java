package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EntradasGasolina_Capturadas extends AppCompatActivity {

    String Usuario,Perfil,Huerta;
    ListView lv_GridGashuerta;

    Itementradasgasolina Tabla;
    Adaptador_GridEntradaGasolina Adapter;
    ArrayList<Itementradasgasolina> arrayEGas;
    TextView MensajeToast;
    View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entradas_gasolina_capturadas);
        getSupportActionBar().hide();

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");

        lv_GridGashuerta = (ListView) findViewById(R.id.lv_GridGashuerta);

        arrayEGas = new ArrayList<>();

        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custome_toast, (ViewGroup) findViewById(R.id.toast_layout_root));
        MensajeToast = (TextView) layout.findViewById(R.id.MensajeToast);

        cargaGrid();

        lv_GridGashuerta.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                return false;
            }
        });

    }

    public void cerrar(View view){
        super.onBackPressed();
    }


    private void cargaGrid(){
        lv_GridGashuerta.setAdapter(null);
        arrayEGas.clear(); //se usa para guardar los datos y cargarlos en el grid

        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Cursor Renglon =BD.rawQuery("select EG.d_fechacrea_gas,\n" +
                "\tEG.c_folio_gas,\n"+
                "\tEG.d_fechaingreso_gas,\n"+
                "\tEG.c_codigo_eps,\n"+
                "\tEG.Id_Huerta,\n"+
                "\tEG.c_codigo_emp,\n"+
                "\tEG.v_tipo_gas,\n"+
                "\tEG.v_cantingreso_gas,\n"+
                "\tEG.v_observaciones_gas, \n"+
                "\tH.Nombre_Huerta\n"+
                "from t_Entradas_Gasolina as EG, t_Huerta as H where H.Id_Huerta = EG.Id_Huerta",null);

        String diaini,mesini,anoini;
        if(Renglon.moveToFirst()) {
            if (Renglon.moveToFirst()) {
                do {
                    String fechaIni=Renglon.getString(2);
                    anoini=fechaIni.substring(6);mesini=fechaIni.substring(2,4);diaini=fechaIni.substring(0,2);
                    Tabla = new Itementradasgasolina(Renglon.getString(0),Renglon.getString(1),diaini+"/"+mesini+"/"+anoini,Renglon.getString(3),Renglon.getString(9),Renglon.getString(5),Renglon.getString(6),Renglon.getString(7),Renglon.getString(8));
                    arrayEGas.add(Tabla);
                    Log.e("array EntradaGAS", arrayEGas.toString());
                } while (Renglon.moveToNext());
                BD.close();
            } else {
                MensajeToast.setText("No hay datos en t_Entradas_Gasolina guardados");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
                BD.close();
            }
        }

        if(arrayEGas.size()>0){
            Adapter=new Adaptador_GridEntradaGasolina(getApplicationContext(),arrayEGas);
            lv_GridGashuerta.setAdapter(Adapter);
            Log.e("Adaptador", lv_GridGashuerta.toString());
        }else{
            //Toast.makeText(Gasolina.this, "No existen datos guardados.", Toast.LENGTH_SHORT).show();
        }

    }

}