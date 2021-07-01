package com.example.shellpest_android;

public class Itemaplicacion {
    private String Fecha,Nombre_Producto,Cantidad,Nombre_Unidad,Centro_Costos;
    private String cProducto,cUnidad;

    public Itemaplicacion(String Fecha, String Nombre_Producto,String Cantidad,String Nombre_Unidad,String Centro_Costos,String cProducto,String cUnidad){
        this.Fecha=Fecha;
        this.Nombre_Producto=Nombre_Producto;
        this.Cantidad=Cantidad;
        this.Nombre_Unidad=Nombre_Unidad;
        this.Centro_Costos=Centro_Costos;
        this.cProducto=cProducto;
        this.cUnidad=cUnidad;

    }

    public String getFecha(){
        return Fecha;
    }

    /*public void setFecha(String Fecha){
        Fecha=Fecha;
    }*/

    public String getNombre_Producto(){
        return Nombre_Producto;
    }

    public String getCantidad(){
        return Cantidad;
    }

    public String getNombre_Unidad(){
        return Nombre_Unidad;
    }

    public String  getCentro_Costos(){
        return Centro_Costos;
    }

    public String getcProducto(){
        return cProducto;
    }

    public String getcUnidad(){
        return cUnidad;
    }
}
