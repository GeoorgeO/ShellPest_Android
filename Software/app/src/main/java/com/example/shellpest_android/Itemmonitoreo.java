package com.example.shellpest_android;

public class Itemmonitoreo {
    String Nombre_PuntoControl,PE,Nombre_Deteccion,Nombre_Individuo;

    public Itemmonitoreo(String Nombre_PuntoControl, String PE,String Nombre_Deteccion,String Nombre_Individuo){
        this.Nombre_PuntoControl=Nombre_PuntoControl;
        this.PE=PE;
        this.Nombre_Deteccion=Nombre_Deteccion;
        this.Nombre_Individuo=Nombre_Individuo;
    }

    public String getPuntoControl(){
        return Nombre_PuntoControl;
    }

    public void setPuntoControl(String Nombre_PuntoControl){
        Nombre_PuntoControl=Nombre_PuntoControl;
    }

    public String getPE(){
        return PE;
    }

    public void setPE(String PE){
        PE=PE;
    }

    public String getDeteccion(){
        return Nombre_Deteccion;
    }

    public void setDeteccion(String Nombre_Deteccion){
        Nombre_Deteccion=Nombre_Deteccion;
    }

    public String getIndividuo(){
        return Nombre_Individuo;
    }

    public void setIndividuo(String Nombre_Individuo){
        Nombre_Individuo=Nombre_Individuo;
    }

}
