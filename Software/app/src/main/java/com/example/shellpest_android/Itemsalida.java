package com.example.shellpest_android;

public class Itemsalida {
    private String Fecha,Nombre_Producto,Cantidad,Nombre_Unidad,Centro_Costos;
    private String cProducto,cUnidad,Existencia,c_codigo_eps;

    public Itemsalida(String Fecha, String Nombre_Producto,String Cantidad,String Nombre_Unidad,String Centro_Costos,String cProducto,String cUnidad,String Existencia,String c_codigo_eps){
        this.Fecha=Fecha;
        this.Nombre_Producto=Nombre_Producto;
        this.Cantidad=Cantidad;
        this.Nombre_Unidad=Nombre_Unidad;
        this.Centro_Costos=Centro_Costos;
        this.cProducto=cProducto;
        this.cUnidad=cUnidad;
        this.Existencia=Existencia;
        this.c_codigo_eps=c_codigo_eps;
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

    public String getExistencia(){return Existencia;}

    public String getceps(){return c_codigo_eps;}

    public void  setCentro_Costos(String Centro_Costos){
         this.Centro_Costos=Centro_Costos;
    }

}
