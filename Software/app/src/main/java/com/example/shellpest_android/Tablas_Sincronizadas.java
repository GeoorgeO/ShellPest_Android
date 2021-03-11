package com.example.shellpest_android;


public class Tablas_Sincronizadas  {

    String Columna;
    int Cantidad;

    public Tablas_Sincronizadas(String columna, int cantidad){
    this.Columna=columna;
    this.Cantidad=cantidad;
    }

    public String getColumna(){
        return Columna;
    }

    public void setColumna(String columna){
        Columna=columna;
    }

    public int getCantidad(){
        return Cantidad;
    }

    public void setCantidad(int cantidad){
        Cantidad=cantidad;
    }
}
