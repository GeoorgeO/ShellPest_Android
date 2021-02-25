package com.example.shellpest_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Login_Usuario extends AppCompatActivity {

    private EditText et_Usuario,et_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_usuario);

        et_Usuario=(EditText)findViewById(R.id.et_Usuario);
        et_Password=(EditText)findViewById(R.id.et_Password);

    }

    public void Agrega_Usuario(View view){
        AdminSQLiteOpenHelper SQLAdmin =new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD = SQLAdmin.getWritableDatabase();

        String Id_Usuario=et_Usuario.getText().toString();
        String Contrasena=et_Password.getText().toString();

        ContentValues registro= new ContentValues();
        registro.put("Id_Usuario",Id_Usuario);
        registro.put("Contrasena",Contrasena);

        BD.insert("UsuarioLogin",null,registro);
        BD.close();
    }

    public void Existe_Usuario(View view){
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getReadableDatabase();

        Cursor Renglon =BD.rawQuery("select Id_Usuario,Contrasena from UsuarioLogin",null);

        if(Renglon.moveToFirst()){
            et_Usuario.setText(Renglon.getString(0));
            et_Password.setText(Renglon.getString(1));



            BD.close();
        }else{
            Toast.makeText(this,"No hay usuarios guardados",Toast.LENGTH_SHORT).show();
            BD.close();
        }

        String Id_Usuario=et_Usuario.getText().toString();
        String Contrasena=et_Password.getText().toString();
        if(!Id_Usuario.isEmpty() && !Contrasena.isEmpty()){

        }else{
            Toast.makeText(this,"Es necesario ingresar usuario y contraseña",Toast.LENGTH_SHORT).show();
        }
    }

    public void Eliminar_UsuarioLogin(View view){
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getWritableDatabase();

        int cantidad= BD.delete("ShellPest","",null);
        BD.close();

        if(cantidad>0){
            Toast.makeText(this,"Se Finalizo la sesion con exito.",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Ocurrio un error al intentar eliminar el usuario logeado, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
        }
    }

    public void Modificar_Usuario(View view){
        AdminSQLiteOpenHelper SQLAdmin= new AdminSQLiteOpenHelper(this,"ShellPest",null,1);
        SQLiteDatabase BD=SQLAdmin.getWritableDatabase();

        String Id_Usuario=et_Usuario.getText().toString();
        String Contrasena=et_Password.getText().toString();

        ContentValues registro = new ContentValues();
        registro.put("Id_Usuario",Id_Usuario);
        registro.put("Contrasena",Contrasena);

        int cantidad=BD.update("UsuarioLogin",registro,"Id_Usuario="+Id_Usuario,null);
        BD.close();
        if(cantidad>0){
            Toast.makeText(this,"Se actualizo el usuario correctamente.",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Ocurrio un error al intentar actualizar el usuario, favor de notificar al administrador del sistema.",Toast.LENGTH_SHORT).show();
        }
    }
}