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
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

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

        lv_GridGasolina.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //SE VA A UTILIZAR SI SE SELECCIONA ALGO DEL LIST VIEW Y CON LLEVA ALGUNA ACCION
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