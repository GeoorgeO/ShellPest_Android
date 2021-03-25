package com.example.shellpest_android;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AdminSQLiteOpenHelper extends SQLiteOpenHelper{
    public AdminSQLiteOpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase BD) {
        BD.execSQL("create table UsuarioLogin(Id_Usuario text primary key,Contrasena text, Id_Perfil text,Id_Huerta text)");
        BD.execSQL("create table FechaSincroniza(id_Sincroniza text primary key,Fecha_Sincroniza text)");
        BD.execSQL("create table t_Calidad(Id_Calidad text primary key,Nombre_Calidad text )");
        BD.execSQL("create table t_Cultivo(Id_Cultivo text primary key,Nombre_Cultivo text)");
        BD.execSQL("create table t_Duenio(Id_Duenio text primary key,Nombre_Duenio text)");
        BD.execSQL("create table t_Deteccion(Id_Deteccion text primary key,Nombre_Deteccion text)");
        BD.execSQL("create table t_Enfermedad(Id_Enfermedad text primary key,Nombre_Enfermedad text)");
        BD.execSQL("create table t_Humbral(Id_Humbral text primary key,Valor_Humbral text,Nombre_Humbral text,Color_Humbral text)");
        BD.execSQL("create table t_Plagas(Id_Plagas text primary key,Nombre_Plagas text)");
        BD.execSQL("create table t_Productor(Id_Productor text primary key,Nombre_Productor text)");
        BD.execSQL("create table t_Pais(Id_Pais text primary key,Nombre_Pais text)");
        BD.execSQL("create table t_Estado(Id_Estado text primary key,Nombre_Estado text,Id_Pais text)");
        BD.execSQL("create table t_Ciudades(Id_Ciudad text primary key,Nombre_Ciudad text,Id_Estado text)");
        BD.execSQL("create table t_Huerta(Id_Huerta text primary key,Nombre_Huerta text,Registro_Huerta text,Id_Productor text,Id_Estado text,Id_Ciudad text,Id_Calidad text,Id_Cultivo text,zona_Huerta text,banda_Huerta text,este_Huerta text,norte_Huerta text,asnm_Huerta text,latitud_Huerta text,longitud_Huerta text,Activa_Huerta text,Id_zona text)");
        BD.execSQL("create table t_Bloque(Id_Bloque text primary key,Id_Huerta text,Nombre_Bloque text)");
        BD.execSQL("create table t_Puntocontrol(Id_PuntoControl text primary key,Id_Bloque text,Nombre_PuntoControl text,n_coordenadaX text,n_coordenadaY text)");
        BD.execSQL("create table t_Zona(Id_zona text primary key,Nombre_zona text)");
        BD.execSQL("create table t_Monitoreo_PE(Fecha text,Id_Huerta text,Id_Plagas text,Id_Enfermedad text,Id_PuntoControl text,Id_Deteccion text,Id_Individuo text,Id_Usuario text,Id_Humbral text,n_coordenadaX text,n_coordenadaY text)");
        BD.execSQL("create table t_Monitoreo_Eliminados_PE(Fecha text,Id_Huerta text,Id_enfermedad text,Id_Plagas text,Id_PuntoControl text,Id_Deteccion text,Id_Individuo text,Id_Usuario text,Id_Humbral text,n_coordenadaX text,n_coordenadaY text)");
        BD.execSQL("create table t_Individuo (Id_Individuo text primary key,No_Individuo text)");
        BD.execSQL("create table t_Monitoreo (Id_monitoreo text primary key,Id_zona text,Id_Plagas text,Id_Enfermedad text,Id_Deteccion text,Id_Individuo text,Id_Humbral text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS UsuarioLogin");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS FechaSincroniza");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Calidad");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Cultivo");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Duenio");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Deteccion");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Enfermedad");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Humbral");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Plagas");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Productor");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Pais");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Estado");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Ciudades");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Huerta");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Bloque");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Puntocontrol");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Zona");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Monitoreo_PE");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS t_Monitoreo_Eliminados_PE");
        onCreate(sqLiteDatabase);
    }
}
