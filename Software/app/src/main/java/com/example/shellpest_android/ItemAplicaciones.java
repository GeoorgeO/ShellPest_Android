package com.example.shellpest_android;

public class ItemAplicaciones {
    private String Id,Nombre_Huerta,Fecha,Id_huerta;

    public ItemAplicaciones(String Id,String Nombre_Huerta,String Fecha, String Id_huerta){
        this.Id=Id;
        this.Nombre_Huerta=Nombre_Huerta;
        this.Fecha=Fecha;
        this.Id_huerta=Id_huerta;
    }

    public String getId(){
        return Id;
    }
    public String getHuerta(){
        return Nombre_Huerta;
    }
    public String getFecha(){
        return Fecha;
    }
    public String getcHuerta(){
        return Id_huerta;
    }

}
