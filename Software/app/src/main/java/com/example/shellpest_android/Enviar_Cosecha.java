package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

public class Enviar_Cosecha extends AppCompatActivity {

    ConexionInternet obj;
    public String MyIp;
    TextView txt_NBloques2,txt_RFechas2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enviar_cosecha);

        getSupportActionBar().hide();
        obj = new ConexionInternet(this);
        if (obj.isConnected()==false ) {
            Toast.makeText(this, "Es necesario una conexion a internet", Toast.LENGTH_SHORT).show();
            super.onBackPressed();
        }
        txt_NBloques2=(TextView)findViewById(R.id.txt_NBloques2);
        txt_RFechas2=(TextView)findViewById(R.id.txt_RFechas2);

        CargaDatos();
    }

    private void CargaDatos(){
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon = BD.rawQuery("select (select count(Id_bloque) as P from t_Cosecha) as Puntos,min(Fecha) as Fini,max(Fecha) as Ffin from t_Cosecha " , null);

        if (Renglon.moveToFirst()) {

            do {
                txt_NBloques2.setText(Renglon.getString(0));

                txt_RFechas2.setText(Renglon.getString(1)+" - "+Renglon.getString(2));
            } while (Renglon.moveToNext());
        } else {
            Toast.makeText(this, "No hay datos guardados para enviar", Toast.LENGTH_SHORT).show();
        }
    }

}