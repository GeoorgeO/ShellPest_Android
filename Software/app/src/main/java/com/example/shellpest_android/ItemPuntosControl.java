package com.example.shellpest_android;

public class ItemPuntosControl {
    private String Nombre_PuntoControl,Nombre_Huerta,NoPE;
    public ItemPuntosControl(String Nombre_PuntoControl, String Nombre_Huerta,String NoPE){
        this.Nombre_PuntoControl=Nombre_PuntoControl;
        this.Nombre_Huerta=Nombre_Huerta;
        this.NoPE=NoPE;
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

}
