package com.example.shellpest_android;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Entradas_Gasolina extends AppCompatActivity {

    public String Usuario, Perfil, Huerta,Responsable, Tipo;
    public String Magna = "Magna - 87 - octanos", Premium = "Premium - 92 - octanos", Diesel= "Diesel", FechaCrea="";

    EditText etxt_fechaingresoGas, etxt_folioregGas, etxt_cantidadingresoGas,etxt_observacionesregistroGas;
    Spinner sp_empresaregistroGas,sp_huertaregistroGas, sp_responsableregistroGas,sp_tiporegistroGas;
    TextView MensajeToast;
    View layout;

    private AdaptadorSpinner CopiHue, CopiActivo, CopiEmp, CopiResp, CopiTipo;
    private ArrayList<ItemDatoSpinner> ItemSPEmp, ItemSPHue, ItemSPResp, ItemSPTipo;

    Boolean solounaEmpresa, solounaHuerta;
    private int dia,mes, anio;

    int LineHuerta, LineEmpresa, LineTipo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entradas_gasolina);
        getSupportActionBar().hide();

        Usuario = getIntent().getStringExtra("usuario2");
        Perfil = getIntent().getStringExtra("perfil2");
        Huerta = getIntent().getStringExtra("huerta2");

        solounaEmpresa = false;
        solounaHuerta = false;

        LayoutInflater inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custome_toast, (ViewGroup) findViewById(R.id.toast_layout_root));
        MensajeToast = (TextView) layout.findViewById(R.id.MensajeToast);

        etxt_folioregGas = (EditText) findViewById(R.id.etxt_folioregGas);
        etxt_fechaingresoGas = (EditText) findViewById(R.id.etxt_fechaingresoregGas);
        etxt_cantidadingresoGas = (EditText) findViewById(R.id.etxt_cantidadingresoGas);
        etxt_observacionesregistroGas = (EditText) findViewById(R.id.etxt_observacionesregistroGas);
        sp_empresaregistroGas = (Spinner) findViewById(R.id.sp_empresaregistroGas);
        sp_huertaregistroGas = (Spinner) findViewById(R.id.sp_huertaregistroGas);
        sp_responsableregistroGas = (Spinner) findViewById(R.id.sp_responsableregistroGas);
        sp_tiporegistroGas = (Spinner) findViewById(R.id.sp_tiporegistroGas);

        LineHuerta=0;
        LineEmpresa=0;
        LineTipo=0;

        Date objDate = new Date();
        SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy");
        Date date1 = objDate;

        etxt_fechaingresoGas.setText("");
        etxt_fechaingresoGas.setInputType(InputType.TYPE_NULL);
        etxt_fechaingresoGas.requestFocus();

        cargarEmpresa();
        //Toast.makeText(this, "CARGA EMPRESA", Toast.LENGTH_SHORT).show();
        CopiEmp = new AdaptadorSpinner(this, ItemSPEmp);
        sp_empresaregistroGas.setAdapter(CopiEmp);

        if(sp_empresaregistroGas.getCount()==2){
            sp_empresaregistroGas.setSelection(1);
        }

        sp_empresaregistroGas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(ItemSPHue==null){
                    cargarHuerta();
                    CopiHue = new AdaptadorSpinner(Entradas_Gasolina.this, ItemSPHue);
                    sp_huertaregistroGas.setAdapter(CopiHue);

                    if(sp_huertaregistroGas.getCount()==2){
                        sp_huertaregistroGas.setSelection(1);
                        //cargaGrid();
                    }else{
                        if (sp_huertaregistroGas.getCount()<=1){
                            ItemSPHue.add(new ItemDatoSpinner("Huerta"));
                            CopiHue = new AdaptadorSpinner(Entradas_Gasolina.this, ItemSPHue);
                            sp_huertaregistroGas.setAdapter(CopiHue);
                        }
                    }
                }else{
                    if ((ItemSPHue.size()<=1) || LineEmpresa!=i){
                        cargarHuerta();
                        CopiHue = new AdaptadorSpinner(Entradas_Gasolina.this, ItemSPHue);
                        sp_huertaregistroGas.setAdapter(CopiHue);
                        if (LineHuerta > 0){
                            LineHuerta = 0;
                        }else{
                            if(sp_huertaregistroGas.getCount() == 2){
                                sp_huertaregistroGas.setSelection(1);
                            }else{
                                if (sp_huertaregistroGas.getCount() <= 1){
                                    ItemSPHue = new ArrayList<>();
                                    ItemSPHue.add(new ItemDatoSpinner("Huerta"));
                                    CopiHue = new AdaptadorSpinner(Entradas_Gasolina.this, ItemSPHue);
                                    sp_huertaregistroGas.setAdapter(CopiHue);
                                }
                            }
                        }
                    }
                }

                if(i>0){
                    //cargaGrid();
                }else{
                    //lv_GridGasolina.setAdapter(null);
                    //arrayGas.clear();
                }

                LineEmpresa = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_huertaregistroGas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Huerta = CopiHue.getItem(i).getTexto().substring(0,5);
                Log.e("Huerta ",Huerta);

                cargarResponsable();
                CopiResp = new AdaptadorSpinner(getApplicationContext(), ItemSPResp);
                sp_responsableregistroGas.setAdapter(CopiResp);
                LineHuerta = i;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_responsableregistroGas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                Responsable = CopiResp.getItem(i).getTexto().substring(0,6);
                Log.e("Responsable", Responsable);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        cargarTipogas();
        CopiTipo = new AdaptadorSpinner(getApplicationContext(), ItemSPTipo);
        sp_tiporegistroGas.setAdapter(CopiTipo);

        sp_tiporegistroGas.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                Tipo = CopiTipo.getItem(i).getTexto();
                Log.e("Tipo", Tipo);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void onClick(View view) {
        if(view==etxt_fechaingresoGas){
            final Calendar c=Calendar.getInstance();
            Date objDate = new Date();
            dia=c.get(Calendar.DAY_OF_MONTH);
            mes=c.get(Calendar.MONTH);
            anio=c.get(Calendar.YEAR);

            DatePickerDialog dtpd=new DatePickerDialog(this, (datePicker, i, i1, i2) -> etxt_fechaingresoGas.setText(rellenarCeros(String.valueOf(i2),2)+"/"+rellenarCeros(String.valueOf((i1+1)),2)+"/"+i),anio,mes,dia);
            dtpd.show();
        }

    }

    private String rellenarCeros (String Valor,int NCaracteres){
        if (Valor.length()<NCaracteres){
            int faltan=0;
            faltan=NCaracteres - Valor.length();
            if (faltan==3){
                return "000"+Valor;
            }
            if (faltan==2){
                return "00"+Valor;
            }
            if (faltan==1){
                return "0"+Valor;
            }
        }
        return Valor;
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
            Renglon = BD.rawQuery("select Id_Huerta,Nombre_Huerta,Id_zona from t_Huerta where Activa_Huerta='True' and c_codigo_eps='"+CopiEmp.getItem(sp_empresaregistroGas.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
        }else{
            Renglon = BD.rawQuery("select Hue.Id_Huerta,Hue.Nombre_Huerta,Hue.Id_zona from t_Huerta as Hue inner join t_Usuario_Huerta as UH ON Hue.Id_Huerta=UH.Id_Huerta where UH.Id_Usuario='"+Usuario+"' and Hue.Activa_Huerta='True' and UH.c_codigo_eps='"+CopiEmp.getItem(sp_empresaregistroGas.getSelectedItemPosition()).getTexto().substring(0,2)+"'",null);
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

    private void cargarResponsable(){
        CopiResp = null;
        ItemSPResp = new ArrayList<>();
        ItemSPResp.add(new ItemDatoSpinner("Responsable"));

        AdminSQLiteOpenHelper SQLAdmin = new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getReadableDatabase();
        Cursor Renglon;
        String Consulta;

        Consulta="select EH.c_codigo_emp, EH.Id_Huerta, EH.Nombre_Completo from t_Empleados_Huerta as EH where ltrim(rtrim(EH.Id_Huerta))='"+Huerta+"' ";
        Renglon=BD.rawQuery(Consulta,null);


        if(Renglon.moveToFirst()){

            do {
                ItemSPResp.add(new ItemDatoSpinner(Renglon.getString(0)+"-"+Renglon.getString(1)+"- "+Renglon.getString(2)));
            } while(Renglon.moveToNext());

            BD.close();
        }else{
            BD.close();
        }

    }

    private void cargarTipogas(){
        CopiTipo = null;

        ItemSPTipo = new ArrayList<>();
        ItemSPTipo.add(new ItemDatoSpinner("Tipo"));
        ItemSPTipo.add(new ItemDatoSpinner("   " + Magna));
        ItemSPTipo.add(new ItemDatoSpinner("   " + Premium));
        ItemSPTipo.add(new ItemDatoSpinner("   " + Diesel));

        sp_tiporegistroGas.setGravity(Gravity.CENTER);

    }

    public void agregarDatos (View view){
        boolean Falta = false, FaltaFolio = false;
        String fechaini=etxt_fechaingresoGas.getText().toString().replace("/","");
        String tipogas=CopiTipo.getItem(sp_tiporegistroGas.getSelectedItemPosition()).getTexto().replace("-", "");
        String Mensaje = "";
        if (sp_tiporegistroGas.getSelectedItemPosition() > 0){ }else{
            Falta = true;
            Mensaje = "Falta seleccionar TIPO, Verifica por favor.";
        }if (etxt_cantidadingresoGas.getText().length() > 0){ }else{
            Falta = true;
            Mensaje = "Falta ingresar CANTIDAD INICIAL, Verifica por favor.";
        }if (sp_responsableregistroGas.getSelectedItemPosition() > 0){ }else{
            Falta = true;
            Mensaje = "Falta seleccionar RESPONSABLE, Verifica por favor.";
        }if (sp_huertaregistroGas.getSelectedItemPosition() > 0){ }else{
            Falta = true;
            Mensaje = "Falta seleccionar HUERTA, Verifica por favor.";
        }if(sp_empresaregistroGas.getSelectedItemPosition() > 0){ }else{
            Falta = true;
            Mensaje = "Falta seleccionar EMPRESA, Verifica por favor.";
        }
        if(etxt_fechaingresoGas.getText().length() > 0){ }else{
            Falta = true;
            Mensaje = "Falta seleccionar FECHA INGRESO, Verifica por favor.";
        }if (etxt_folioregGas.getText().length() > 0){ }else{
            FaltaFolio = true;
        }

        if (!Falta){
            AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
            SQLiteDatabase BD = SQLAdmin.getWritableDatabase();
            ContentValues registroGas = new ContentValues();

            Date objDate = new Date(); // Sistema actual La fecha y la hora se asignan a objDate
            SimpleDateFormat objSDF = new SimpleDateFormat("dd/MM/yyyy"); // La cadena de formato de fecha se pasa como un argumento al objeto
            Date date=objDate;

            if (FaltaFolio){
                AlertDialog.Builder dialogo = new AlertDialog.Builder(Entradas_Gasolina.this, R.style.Theme_AppCompat_Dialog_Alert);
                dialogo.setTitle("NO SE HA INGRESADO FOLIO");
                dialogo.setMessage("No ha ingresado folio, si selecciona ACEPTAR se agregará el registro SIN FOLIO. \n   ¿Desea continuar?");
                dialogo.setCancelable(true);
                dialogo.setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        registroGas.put("d_fechacrea_gas",objSDF.format(date).replace("/",""));
                        registroGas.put("c_folio_gas", etxt_folioregGas.getText().toString());
                        registroGas.put("d_fechaingreso_gas", fechaini);
                        registroGas.put("c_codigo_eps", CopiEmp.getItem(sp_empresaregistroGas.getSelectedItemPosition()).getTexto().substring(0,2));//c_codigo_eps
                        registroGas.put("Id_Huerta", CopiHue.getItem(sp_huertaregistroGas.getSelectedItemPosition()).getTexto().substring(0,5));//Id_Huerta
                        registroGas.put("c_codigo_emp",CopiResp.getItem(sp_responsableregistroGas.getSelectedItemPosition()).getTexto().substring(0,6));//c_codigo_emp
                        registroGas.put("v_tipo_gas", tipogas);
                        registroGas.put("v_cantingreso_gas", etxt_cantidadingresoGas.getText().toString());
                        registroGas.put("v_observaciones_gas", etxt_observacionesregistroGas.getText().toString());
                        Log.e("Registro", registroGas.toString());
                        BD.insert("t_Entradas_Gasolina",null,registroGas);

                        MensajeToast.setText("Agregado a la Base de Datos");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();

                        limpiar();
                    }
                });
                dialogo.setNegativeButton("CANCELAR", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MensajeToast.setText("Registra el Folio por favor");
                        Toast toast = new Toast(getApplicationContext());
                        toast.setDuration(Toast.LENGTH_SHORT);
                        toast.setView(layout);
                        toast.show();
                    }
                });
                dialogo.show();

            }else{
                registroGas.put("d_fechacrea_gas",objSDF.format(date).replace("/",""));
                registroGas.put("c_folio_gas", etxt_folioregGas.getText().toString());
                registroGas.put("d_fechaingreso_gas", fechaini);
                registroGas.put("c_codigo_eps", CopiEmp.getItem(sp_empresaregistroGas.getSelectedItemPosition()).getTexto().substring(0,2));//c_codigo_eps
                registroGas.put("Id_Huerta", CopiHue.getItem(sp_huertaregistroGas.getSelectedItemPosition()).getTexto().substring(0,5));//Id_Huerta
                registroGas.put("c_codigo_emp",CopiResp.getItem(sp_responsableregistroGas.getSelectedItemPosition()).getTexto().substring(0,6));//c_codigo_emp
                registroGas.put("v_tipo_gas", tipogas);
                registroGas.put("v_cantingreso_gas", etxt_cantidadingresoGas.getText().toString());
                registroGas.put("v_observaciones_gas", etxt_observacionesregistroGas.getText().toString());
                Log.e("Registro", registroGas.toString());
                BD.insert("t_Entradas_Gasolina",null,registroGas);

                MensajeToast.setText("Agregado a la Base de Datos");
                Toast toast = new Toast(getApplicationContext());
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();

                limpiar();
            }

        }else{
            MensajeToast.setText(Mensaje);
            Toast toast = new Toast(getApplicationContext());
            toast.setDuration(Toast.LENGTH_SHORT);
            toast.setView(layout);
            toast.show();
        }
    }

    private void limpiar(){
        etxt_folioregGas.setText("");
        etxt_fechaingresoGas.setText("");
        sp_empresaregistroGas.setSelection(0);
        sp_huertaregistroGas.setSelection(0);
        sp_responsableregistroGas.setSelection(0);
        etxt_cantidadingresoGas.setText("");
        sp_tiporegistroGas.setSelection(0);
        etxt_observacionesregistroGas.setText("");
    }

    public void registrarGas(View view){
        Intent intento = new Intent(this, Gasolina.class);
        intento.putExtra("usuario2", Usuario);
        intento.putExtra("perfil2", Perfil);
        intento.putExtra("huerta2", Huerta);

        startActivity(intento);
        finish();
    }

    public void gasolinaHuerta(View view){
        Intent intento = new Intent(this, EntradasGasolina_Consulta.class);
        intento.putExtra("usuario2", Usuario);
        intento.putExtra("perfil2", Perfil);
        intento.putExtra("huerta2", Huerta);

        startActivity(intento);
        finish();
    }
}