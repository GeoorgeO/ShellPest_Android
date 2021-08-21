package com.example.shellpest_android;

public class Itemaplicacion {
    private String Fecha,Nombre_Producto,Dosis,Unidades_aplicadas,Nombre_Unidad;
    private String cProducto,cUnidad;

    public Itemaplicacion(String Fecha, String cProducto,String Dosis,String Unidades_aplicadas,String cUnidad,String Nombre_Producto,String Nombre_Unidad){
        this.Fecha=Fecha;
        this.Nombre_Producto=Nombre_Producto;
        this.Dosis=Dosis;
        this.Nombre_Unidad=Nombre_Unidad;
        this.Unidades_aplicadas=Unidades_aplicadas;
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
        return Dosis;
    }

    public String getNombre_Unidad(){
        return Nombre_Unidad;
    }

    public String  getUnidades_aplicadas(){
        return Unidades_aplicadas;
    }

    public String getcProducto(){
        return cProducto;
    }

    public String getcUnidad(){
        return cUnidad;
    }
}
