package com.example.shellpest_android;

public class ItemSalidas {
    private String Id,Nombre_Almacen,Fecha,Id_Almacen,cEps;

    public ItemSalidas(String Id,String Nombre_Almacen,String Fecha, String Id_Almacen,String cEps){
        this.Id=Id;
        this.Nombre_Almacen=Nombre_Almacen;
        this.Fecha=Fecha;
        this.Id_Almacen=Id_Almacen;
        this.cEps=cEps;
    }

    public String getId(){
        return Id;
    }
    public String getAlmacen(){
        return Nombre_Almacen;
    }
    public String getFecha(){
        return Fecha;
    }
    public String getcAlmacen(){
        return Id_Almacen;
    }

    public String getcEPS(){
        return cEps;
    }
}
