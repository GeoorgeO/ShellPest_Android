package com.example.shellpest_android;

public class Itemaplicacion {
    private String Fecha,Nombre_Producto,Dosis,Nombre_Unidad;
    private String cProducto,cUnidad,cEps,CC;

    public Itemaplicacion(String Fecha, String cProducto,String Dosis,String cUnidad,String Nombre_Producto,String Nombre_Unidad,String cEps,String CC){
        this.Fecha=Fecha;
        this.Nombre_Producto=Nombre_Producto;
        this.Dosis=Dosis;
        this.Nombre_Unidad=Nombre_Unidad;

        this.cProducto=cProducto;
        this.cUnidad=cUnidad;
        this.cEps=cEps;

        this.CC=CC;
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

    public String getCC(){
        return CC;
    }

    public String getcProducto(){
        return cProducto;
    }

    public String getcUnidad(){
        return cUnidad;
    }

    public String getcEps(){
        return cEps;
    }

    public void setCantidad(String Dosis){
        this.Dosis=Dosis;
    }
}
