package com.example.shellpest_android;

public class ItemPuntosControl {
    private String Nombre_PuntoControl,Nombre_Huerta,NoPE,cPto,cEPS,Nombre_Eps,Fecha;
    public ItemPuntosControl(String Nombre_PuntoControl, String Nombre_Huerta,String NoPE,String cPto,String cEPS,String Nombre_Eps,String Fecha){
        this.Nombre_PuntoControl=Nombre_PuntoControl;
        this.Nombre_Huerta=Nombre_Huerta;
        this.NoPE=NoPE;
        this.cPto=cPto;
        this.cEPS=cEPS;
        this.Nombre_Eps=Nombre_Eps;
        this.Fecha=Fecha;
    }
    public String getPuntoControl(){
        return Nombre_PuntoControl;
    }

    public void setPuntoControl(String Nombre_PuntoControl){
        this.Nombre_PuntoControl=Nombre_PuntoControl;
    }
    public String getHuerta(){
        return Nombre_Huerta;
    }

    public void setHuerta(String Nombre_Huerta){
       this.Nombre_Huerta=Nombre_Huerta;
    }
    public String getNoPE(){
        return NoPE;
    }

    public void setNoPE(String NoPE){
        this.NoPE=NoPE;
    }

    public String getcPto(){
        return cPto;
    }

    public String getcEPS(){
        return cEPS;
    }

    public String getNombre_Eps(){
        return Nombre_Eps;
    }

    public String getFecha(){
        return Fecha;
    }

}
