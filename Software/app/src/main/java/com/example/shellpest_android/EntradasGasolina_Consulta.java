package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class EntradasGasolina_Consulta extends AppCompatActivity {

    TextView txtv_entradaEmpresa, txtv_entradaHuerta, txtv_entradaTipo, txtv_entradaCantidad;
    Spinner sp_empresaEntradaGas, sp_huertaEntradaGas, sp_tipoEntradaGas;

    private AdaptadorSpinner CopiHue, CopiEmp, CopiTipo;
    private ArrayList<ItemDatoSpinner> ItemSPEmp, ItemSPHue, ItemSPTipo;

    public String Magna = "Magna - 87 - octanos", Premium = "Premium - 92 - octanos", Diesel= "Diesel", FechaCrea="";
    public String Usuario, Perfil, Huerta, Tipo;
    int LineHuerta, LineEmpresa,  LineTipo;
    Boolean ban = false, banSuma = false;

    TextView MensajeToast;
    View layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entradas_gasolina_consulta);
        getSupportActionBar().hide();

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");
        Log.e("Usuario", Usuario);

        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custome_toast, (ViewGroup) findViewById(R.id.toast_layout_root));
        MensajeToast = (TextView) layout.findViewById(R.id.MensajeToast);

        txtv_entradaEmpresa = (TextView) findViewById(R.id.txtv_entradaEmpresa);
        txtv_entradaHuerta = (TextView) findViewById(R.id.txtv_entradaHuerta);
        txtv_entradaTipo = (TextView) findViewById(R.id.txtv_entradaTipo);
        txtv_entradaCantidad = (TextView) findViewById(R.id.txtv_entradaCantidad);

        sp_empresaEntradaGas = (Spinner) findViewById(R.id.sp_empresaEntradaGas);
        sp_huertaEntradaGas = (Spinner) findViewById(R.id.sp_huertaEntradaGas);
        sp_tipoEntradaGas = (Spinner) findViewById(R.id.sp_tipoEntradaGas);

        cargarEmpresa();
        CopiEmp = new AdaptadorSpinner(this, ItemSPEmp);
        sp_empresaEntradaGas.setAdapter(CopiEmp);

        if(sp_empresaEntradaGas.getCount()==2){
            sp_empresaEntradaGas.setSelection(1);
        }

        sp_empresaEntradaGas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                limpiar();
                if(ItemSPHue==null){
                    cargarHuerta();
                    CopiHue = new AdaptadorSpinner(EntradasGasolina_Consulta.this, ItemSPHue);
                    sp_huertaEntradaGas.setAdapter(CopiHue);

                    if(sp_huertaEntradaGas.getCount()==2){
                        sp_huertaEntradaGas.setSelection(1);
                    }else{
                        if (sp_huertaEntradaGas.getCount()<=1){
                            ItemSPHue.add(new ItemDatoSpinner("Huerta"));
                            CopiHue = new AdaptadorSpinner(EntradasGasolina_Consulta.this, ItemSPHue);
                            sp_huertaEntradaGas.setAdapter(CopiHue);
                        }
                    }
                }else{
                    if ((ItemSPHue.size()<=1) || LineEmpresa!=i){
                        cargarHuerta();
                        CopiHue = new AdaptadorSpinner(EntradasGasolina_Consulta.this, ItemSPHue);
                        sp_huertaEntradaGas.setAdapter(CopiHue);
                        if (LineHuerta > 0){
                            LineHuerta = 0;
                        }else{
                            if(sp_huertaEntradaGas.getCount() == 2){
                                sp_huertaEntradaGas.setSelection(1);
                            }else{
                                if (sp_huertaEntradaGas.getCount() <= 1){
                                    ItemSPHue = new ArrayList<>();
                                    ItemSPHue.add(new ItemDatoSpinner("Huerta"));
                                    CopiHue = new AdaptadorSpinner(EntradasGasolina_Consulta.this, ItemSPHue);
                                    sp_huertaEntradaGas.setAdapter(CopiHue);
                                }
                            }
                        }
                    }
                }
                LineEmpresa = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_huertaEntradaGas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                limpiar();
                Huerta = CopiHue.getItem(i).getTexto().substring(0,5);
                Log.e("Huerta ",Huerta);

                cargarTipogas();
                CopiTipo = new AdaptadorSpinner(getApplicationContext(), ItemSPTipo);
                sp_tipoEntradaGas.setAdapter(CopiTipo);

                LineHuerta = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_tipoEntradaGas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                limpiar();
                Tipo = CopiTipo.getItem(i).getTexto();
                Log.e("Tipo", Tipo);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



    }

    public void cerrar(View view){
        super.onBackPressed();
    }

    public void registrosHuerta(View view){
        Intent intento = new Intent(EntradasGasolina_Consulta.this, EntradasGasolina_Capturadas.class);
        intento.putExtra("usuario2", Usuario);
        intento.putExtra("perfil2", Perfil);
        intento.putExtra("huerta2", Huerta);

        startActivity(intento);
        finish();
    }

    public void CargaDatos(View view){
        String tipogas = CopiTipo.getItem(sp_tipoEntradaGas.getSelectedItemPosition()).getTexto().replace("-", "");
        String SumaIngreso, SumaConsumo;
        Double Ingreso = 0.0, Consumo = 0.0;

        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this, "ShellPest", null, 1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();

        Cursor Renglon = BD.rawQuery("select E.v_abrevia_eps, H.Nombre_Huerta from t_Entradas_Gasolina as EG, conempresa as E, t_Huerta as H " +
                " where E.c_codigo_eps='"+ CopiEmp.getItem(sp_empresaEntradaGas.getSelectedItemPosition()).getTexto().substring(0,2) +"' " +
                " and H.Id_Huerta= '"+Huerta+"'", null);

        Cursor Renglon2 = BD.rawQuery("select Sum(v_cantingreso_gas) as SumaCantidad, v_tipo_gas from t_Entradas_Gasolina " +
                "where Id_Huerta= '"+Huerta+"' and v_tipo_gas='"+ tipogas +"'", null);

        Cursor Renglon3 = BD.rawQuery("select Sum(v_cantutilizada_gas) as SumaUtilizada  from t_Consumo_Gasolina" +
                " where Id_Huerta= '"+Huerta+"' and v_tipo_gas='"+ tipogas +"'", null);

        if (Renglon2.moveToFirst()){
            do{
                if(Renglon2.getString(0) == null){
                    MensajeToast.setText("No se encuentra la información correspondiente");
                    Toast toast = new Toast(getApplicationContext());
                    toast.setDuration(Toast.LENGTH_SHORT);
                    toast.setView(layout);
                    toast.show();
                }else{
                    txtv_entradaTipo.setText(Renglon2.getString(1));
                    SumaIngreso = Renglon2.getString(0);
                    Ingreso = Double.parseDouble(SumaIngreso);
                    ban = true;
                    banSuma = true;
                }
            }while (Renglon2.moveToNext());
        }else{
            MensajeToast.setText("No se encuentra la información correspondiente");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }

        if (ban == true){
            if (Renglon.moveToFirst()){
                do{
                        txtv_entradaEmpresa.setText(Renglon.getString(0));
                        txtv_entradaHuerta.setText(Renglon.getString(1));
                }while (Renglon.moveToNext());
            }else{
                MensajeToast.setText("No se encuentra la información correspondiente");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
            }
        }

        if (Renglon3.moveToFirst()){
            do{
                if (Renglon3.getString(0) == null){

                }else{
                    SumaConsumo = Renglon3.getString(0);
                    Consumo = Double.parseDouble(SumaConsumo);
                    banSuma = true;

                }
            }while (Renglon3.moveToNext());
        }else{
            MensajeToast.setText("No se encuentra la información correspondiente");
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }

        if (banSuma == true){
            Double Cantidad = Ingreso - Consumo;
            Log.e("Ingreso", Ingreso.toString());
            Log.e("Consumo", Consumo.toString());
            txtv_entradaCantidad.setText("Cantidad total en huerta: \n"+ Cantidad + " lt");
        }


    }

    private void cargarTipogas(){
        CopiTipo = null;

        ItemSPTipo = new ArrayList<>();
        ItemSPTipo.add(new ItemDatoSpinner("Tipo"));
        ItemSPTipo.add(new ItemDatoSpinner("   " + Magna));
        ItemSPTipo.add(new ItemDatoSpinner("   " + Premium));
        ItemSPTipo.add(new ItemDatoSpinner("   " + Diesel));

        sp_tipoEntradaGas.setGravity(Gravity.CENTER);

    }

    private void cargarEmpresa(){
        CopiEmp = null;
        ItemSPEmp = new ArrayList<>();
        ItemSPEmp.add(new ItemDatoSpinner("Empresa"));

        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon10;
        String Consulta;

        Consulta="select E.c_codigo_eps,E.v_abrevia_eps from conempresa as E inner join t_Usuario_Empresa as UE on UE.c_codigo_eps=E.c_codigo_eps where ltrim(rtrim(UE.Id_Usuario))='"+Usuario+"' ";
        Renglon10=BD.rawQuery(Consulta,null);

        if(Renglon10.moveToFirst()){

            do {
                ItemSPEmp.add(new ItemDatoSpinner(Renglon10.getString(0)+" - "+Renglon10.getString(1)));
            } while(Renglon10.moveToNext());

            BD.close();
        }else{
            BD.close();
        }

    }

    private void cargarHuerta(){
        CopiHue = null;
        ItemSPHue = new ArrayList<>();
        ItemSPHue.add(new ItemDatoSpinner("Huerta"));
        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon;

        if(Perfil.equals("001")){
            Renglon = BD.rawQuery("select Id_Huerta,Nombre_Huerta,Id_zona from t_Huerta where Activa_Huerta='True' and c_codigo_eps='"+CopiEmp.getItem(sp_empresaEntradaGas.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
        }else{
            Renglon = BD.rawQuery("select Hue.Id_Huerta,Hue.Nombre_Huerta,Hue.Id_zona from t_Huerta as Hue inner join t_Usuario_Huerta as UH ON Hue.Id_Huerta=UH.Id_Huerta where UH.Id_Usuario='"+Usuario+"' and Hue.Activa_Huerta='True' and UH.c_codigo_eps='"+CopiEmp.getItem(sp_empresaEntradaGas.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
        }

        if(Renglon.getCount()>1){
            ItemSPHue.add(new ItemDatoSpinner("Huerta"));
        }

        if(Renglon.moveToFirst()){
            do{
                ItemSPHue.add(new ItemDatoSpinner(Renglon.getString(0)+" - "+Renglon.getString(1)+" - "+Renglon.getString(2)));
            }while(Renglon.moveToNext());
        }else{
            /*Toast ToastMensaje = Toast.makeText(this, "No se encontraron datos en huertas", Toast.LENGTH_SHORT);
            ToastMensaje.show();*/
            BD.close();
        }
        BD.close();
    }

    private void limpiar(){
        ban = false;
        banSuma = false;
        txtv_entradaEmpresa.setText("-");
        txtv_entradaHuerta.setText("-");
        txtv_entradaTipo.setText("-");
        txtv_entradaCantidad.setText("-");
    }

}