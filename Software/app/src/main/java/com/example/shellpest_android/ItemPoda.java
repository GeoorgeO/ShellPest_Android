package com.example.shellpest_android;

public class ItemPoda {
    private String Fecha,Id_Bloque,c_codigo_act,v_nombre_act,c_codigo_eps;


    public ItemPoda(String IniFecha, String IniId_Bloque, String Inic_codigo_act, String Iniv_nombre_act,String Inic_codigo_eps){
        this.Fecha=IniFecha;
        this.Id_Bloque=IniId_Bloque;
        this.c_codigo_act=Inic_codigo_act;
        this.v_nombre_act=Iniv_nombre_act;
        this.c_codigo_eps=Inic_codigo_eps;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getId_Bloque() {
        return Id_Bloque;
    }

    public void setId_Bloque(String id_Bloque) {
        Id_Bloque = id_Bloque;
    }

    public String getC_codigo_act() {
        return c_codigo_act;
    }

    public void setC_codigo_act(String c_codigo_act) {
        this.c_codigo_act = c_codigo_act;
    }

    public String getV_nombre_act() {
        return v_nombre_act;
    }

    public void setV_nombre_act(String v_nombre_act) {
        this.v_nombre_act = v_nombre_act;
    }

    public String getC_codigo_eps(){
        return c_codigo_eps;
    }

    public void setC_codigo_eps(String c_codigo_eps){
        this.c_codigo_eps=c_codigo_eps;
    }

}
